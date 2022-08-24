
package theme_plugin_project.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.css.core.dom.ExtendedDocumentCSS;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.internal.theme.ThemeEngine;
import org.eclipse.e4.ui.css.swt.theme.ITheme;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.w3c.css.sac.CSSParseException;
import org.w3c.dom.stylesheets.StyleSheet;
import org.w3c.dom.stylesheets.StyleSheetList;

import theme_plugin_project.dialogs.ThemeDialog;

@SuppressWarnings("restriction")
public class ThemeChangeHandler {
	private FileInputStream fileInputStream;

	@Execute
	public void execute(Shell shell, IThemeEngine themeEngine) throws IOException, InvocationTargetException, InterruptedException {

		ThemeDialog dialog = new ThemeDialog(shell, themeEngine);
		int open = dialog.open();

		if (open == IDialogConstants.OK_ID) {
			
			Preferences preferences = InstanceScope.INSTANCE.getNode("Theme_Plugin_Project");
			Preferences sub1 = preferences.node("node1");		
	        // Delete the existing settings
	           try {
	               sub1.clear();
	               preferences.flush();
	           } catch (BackingStoreException e1) {
	               e1.printStackTrace();
	           }	

			((ThemeEngine) themeEngine).resetCurrentTheme();

			ITheme selectedTheme = dialog.getSelectedTheme();
			if (selectedTheme != null) {
				themeEngine.setTheme(selectedTheme, true);
			} else {

				File cssEngine = dialog.getCSSEngine();
				applyCSS(themeEngine, cssEngine);

				sub1.put("cusstomCssPath", cssEngine.getAbsolutePath());
				sub1.put("cusstomCssName", cssEngine.getName());

				try {
					// forces the application to save the preferences
					preferences.flush();
				} catch (BackingStoreException e2) {
					e2.printStackTrace();
				}
				
			}

		}

	}

	public void applyCSS(IThemeEngine themeEngine, File cssEngine) throws IOException, InvocationTargetException, InterruptedException {
		if (themeEngine == null) {

			return;
		}
		
		
		
		IRunnableWithProgress op = new IRunnableWithProgress() {
			
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				 monitor.beginTask("Applying CSS", 100);  
		            try {  
		            	
		            	long start = System.nanoTime();

		        		StringBuilder sb = new StringBuilder();

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
		        				Thread.sleep(100);
		        				monitor.worked(30);
		        			}

		        			try {
		        				fileInputStream = new FileInputStream(cssEngine);
		        				sheets.add(engine.parseStyleSheet(fileInputStream));
		        				doc.removeAllStyleSheets();
		        				for (StyleSheet sheet : sheets) {
		        					doc.addStyleSheet(sheet);
		        				}
		        				Thread.sleep(200);
		        				monitor.worked(50);
		        				engine.reapply();
		        				Thread.sleep(200);
		        				monitor.worked(70);
		        				long nanoDiff = System.nanoTime() - start;
		        				sb.append("\nTime: ").append(nanoDiff / 1000000).append("ms");
		        				Thread.sleep(50);
		        				monitor.worked(80);
		        			} catch (CSSParseException e) {
		        				sb.append("\nError: line ").append(e.getLineNumber()).append(" col ").append(e.getColumnNumber())
		        						.append(": ").append(e.getLocalizedMessage());
		        			} catch (IOException e) {
		        				sb.append("\nError: ").append(e.getLocalizedMessage());
		        			} finally {
		        				try {
		        					Thread.sleep(5000);
		        					monitor.worked(90);
		        				} catch (InterruptedException e) {
		        					e.printStackTrace();
		        				}
		        				fileInputStream.close();
		        				monitor.worked(100);
		        			}
		        		}
		        
		             
		            } catch (Exception e) {  
		                e.printStackTrace();  
		            }  
		            monitor.done(); 
				
			}
			
		};
		
		new ProgressMonitorDialog(new Shell()).run(false, false, op);
	
	}

}