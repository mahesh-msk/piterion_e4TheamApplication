
package theme_plugin_project.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;

public class ChangeThemeHandler {
	private static final String DEFAULT_THEME = "Theme_Plugin_Project.default";
	private static final String CUSTOM_THEME = "Theme_Plugin_Project.custom";

	@Execute
	public void execute(IThemeEngine engine) {
		if (!engine.getActiveTheme().getId().equals(DEFAULT_THEME)) {
			engine.setTheme(DEFAULT_THEME, true);
		} else {
			engine.setTheme(CUSTOM_THEME, true);
		}

	}

}