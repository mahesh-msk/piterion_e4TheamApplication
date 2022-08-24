
package theme_plugin_project.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.css.core.dom.ExtendedDocumentCSS;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.internal.theme.ThemeEngine;
import org.eclipse.e4.ui.css.swt.theme.ITheme;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.w3c.css.sac.CSSParseException;
import org.w3c.dom.stylesheets.StyleSheet;
import org.w3c.dom.stylesheets.StyleSheetList;

import theme_plugin_project.dialogs.OpenDialog;

public class OpendialogHandler {
	private FileInputStream fileInputStream;



	@Execute
	public void execute(Shell shell, IThemeEngine themeEngine) throws IOException {
		
		
		OpenDialog dialog = new OpenDialog(shell, themeEngine);
		int open = dialog.open();
		if(open==IDialogConstants.OK_ID)
		{
			((ThemeEngine) themeEngine).resetCurrentTheme();
			
			ITheme selectedTheme = dialog.getSelectedTheme();
			if(selectedTheme != null) {
				themeEngine.setTheme(selectedTheme, true);
			} else {
				
				File cssEngine = dialog.getCSSEngine();
				applyCSS(themeEngine, cssEngine);
				
			}
			
		}
		

	}
	
	

	private void applyCSS(IThemeEngine themeEngine, File cssEngine) throws IOException {
		if (themeEngine == null) {
			
			return;
		}
		long start = System.nanoTime();

		StringBuilder sb = new StringBuilder();
	
		// FIXME: expose these new protocols: resetCurrentTheme() and
		// getCSSEngines()
		((ThemeEngine) themeEngine).resetCurrentTheme();

		int count = 0;
		for (CSSEngine engine : ((ThemeEngine) themeEngine).getCSSEngines()) {
			if (count++ > 0) {
				sb.append("\n\n");
			}
			sb.append("Engine[").append(engine.getClass().getSimpleName()).append("]");
			ExtendedDocumentCSS doc = (ExtendedDocumentCSS) engine.getDocumentCSS();
			List<StyleSheet> sheets = new ArrayList<>();
			StyleSheetList list = doc.getStyleSheets();
			for (int i = 0; i < list.getLength(); i++) {
				sheets.add(list.item(i));
			}

			try {
				fileInputStream = new FileInputStream(cssEngine);
				sheets.add(0, engine.parseStyleSheet(fileInputStream));
				doc.removeAllStyleSheets();
				for (StyleSheet sheet : sheets) {
					doc.addStyleSheet(sheet);
				}
				engine.reapply();

				long nanoDiff = System.nanoTime() - start;
				sb.append("\nTime: ").append(nanoDiff / 1000000).append("ms");
			} catch (CSSParseException e) {
				sb.append("\nError: line ").append(e.getLineNumber()).append(" col ").append(e.getColumnNumber())
						.append(": ").append(e.getLocalizedMessage());
			} catch (IOException e) {
				sb.append("\nError: ").append(e.getLocalizedMessage());
			} finally {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fileInputStream.close();
			}
		}
	}

}