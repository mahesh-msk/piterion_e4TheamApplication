package theme_plugin_project.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.jface.viewers.TreeViewerColumn;

public class TreeViewerDialog extends Dialog {

	public TreeViewerDialog(Shell parent) {
		super(parent);
	}
	
	@Override
	protected void configureShell(Shell shell) {
		
		super.configureShell(shell);
		shell.setText("Theme Selector Dialog");
		shell.setSize(500, 400);
	}
	
	
	@Override
	protected Control createDialogArea(Composite parent) {
		
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		
		TreeViewer treeViewer = new TreeViewer(container, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		
		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnFirstColumn = treeViewerColumn.getColumn();
		trclmnFirstColumn.setWidth(100);
		trclmnFirstColumn.setText("First Column");
		
		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnSecondColumn = treeViewerColumn_1.getColumn();
		trclmnSecondColumn.setWidth(100);
		trclmnSecondColumn.setText("Second Column");
		
		
		return super.createDialogArea(parent);
	}

}
