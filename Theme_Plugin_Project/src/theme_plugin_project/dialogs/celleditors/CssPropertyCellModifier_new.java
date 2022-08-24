package theme_plugin_project.dialogs.celleditors;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.RGB;

import com.steadystate.css.dom.Property;

public class CssPropertyCellModifier_new implements ICellModifier {

	private TreeViewer treeViewer;

	public CssPropertyCellModifier_new(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;	
	}

	@Override
	public boolean canModify(Object element, String property) {

		return false;

	}

	@Override
	public Object getValue(Object element, String property) {
		if (element instanceof Property) {
			Property sheet = (Property) element;
			if (property.equals("0")) {
				return sheet.getName();
			} else if (property.equals("1")) {
				  if(sheet.getValue().getCssText().startsWith("rgb")){
					  //sheet.setValue((RGB) sheet.getValue().getCssText());
				  }
			//	 sheet.getValue().getCssText();
			}
		}

		return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {

		if (element instanceof Property) {
			Property sheet = (Property) element;
			if (property.equals("0")) {
				sheet.setName((String) value);
			} else if (property.equals("1")) {
				sheet.getValue().setCssText((String) value);
			}
			treeViewer.refresh();
		}

	}

}
