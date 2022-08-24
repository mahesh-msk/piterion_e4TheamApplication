package theme_plugin_project;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessRemovals;
import org.eclipse.swt.widgets.Display;
import org.osgi.service.prefs.Preferences;

import theme_plugin_project.handlers.ThemeChangeHandler;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine;

/**
 * This is a stub implementation containing e4 LifeCycle annotated methods.<br />
 * There is a corresponding entry in <em>plugin.xml</em> (under the
 * <em>org.eclipse.core.runtime.products' extension point</em>) that references
 * this class.
 **/

@SuppressWarnings({ "restriction", "unused" })
public class E4LifeCycle {
	
	@PostContextCreate
	void postContextCreate(IEclipseContext workbenchContext,Display display) {
		
		
//	   String cssURI = Platform.getInstallLocation().getURL().getPath() + "themecss/custom.css";
//	   workbenchContext.set(E4Workbench.CSS_RESOURCE_URI_ARG, cssURI);
//       IThemeEngine engine = ThemeHelper.getEngine();
//       System.out.println(engine);
		      
	}
	
	@PreSave
	void preSave(IEclipseContext workbenchContext) {
	}

	@ProcessAdditions
	void processAdditions(IEclipseContext workbenchContext) {
		
	}

	@ProcessRemovals
	void processRemovals(IEclipseContext workbenchContext) {
		
	}
}
