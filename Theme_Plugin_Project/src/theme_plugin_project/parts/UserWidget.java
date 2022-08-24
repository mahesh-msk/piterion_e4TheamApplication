 
package theme_plugin_project.parts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.core.engine.CSSErrorHandler;
import org.eclipse.e4.ui.css.swt.engine.CSSSWTEngineImpl;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import theme_plugin_project.customwidget.UserProfileWidget;

public class UserWidget {
	@Inject
	public UserWidget() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent, IThemeEngine themeEngine) {
		
		CSSEngine engine = new CSSSWTEngineImpl(parent.getDisplay());

		try {
			engine.parseStyleSheet(new FileInputStream("C:\\Users\\Lenovo-PC\\Downloads\\Theme_Plugin_Project\\css\\custom2.css"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		engine.setErrorHandler(new CSSErrorHandler() {
		  public void error(Exception e) {
		    e.printStackTrace();
		  }
		});
		
		
		
		// applying styles to the child nodes means that the engine
		// should recurse downwards, in this example, the engine
		// should style the children of the Shell
		
		
		parent.setLayout(new FillLayout());
		Label label = new Label(parent, SWT.NONE);
		label.setText("Sample LABEL");
		engine.applyStyles(label, true);
		
		themeEngine.addCSSEngine(engine);
		System.out.println(themeEngine.getThemes().size());
		
//		UserProfileWidget userProfileWidget=new UserProfileWidget(parent,SWT.None );
		
		
	}
	
	
	
	
}