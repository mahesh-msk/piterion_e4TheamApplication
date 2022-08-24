 
package theme_plugin_project.dialogs;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.swt.widgets.Display;

public class UserDialogHandler {
	@Execute
	public void execute(@Preference(nodePath = "Theme_Plugin_Project") IEclipsePreferences prefs) {
		UserDialog dialog=new UserDialog(Display.getDefault().getActiveShell(),prefs);
		dialog.open();
		
	}
		
}