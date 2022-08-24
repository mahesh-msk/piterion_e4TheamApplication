 
package theme_plugin_project.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;

import theme_plugin_project.dialogs.TreeViewerDialog;

public class TreeViewerHandler {
	@Execute
	public void execute(Shell shell) {
		
		
		TreeViewerDialog dialog = new TreeViewerDialog(shell);
		int open = dialog.open();
		if(open==IDialogConstants.OK_ID)
		{
	
			
		}
		
	}
		
}