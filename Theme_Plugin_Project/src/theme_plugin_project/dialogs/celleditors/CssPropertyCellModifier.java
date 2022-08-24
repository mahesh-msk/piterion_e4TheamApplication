package theme_plugin_project.dialogs.celleditors;

import org.eclipse.jface.viewers.ICellModifier;

import com.steadystate.css.dom.Property;



public class CssPropertyCellModifier implements ICellModifier {



	@Override
	public Object getValue(Object element, String property) {
		if (element instanceof Property) {
			Property sheet = (Property) element;
			if (property.equals("0")) {
				return sheet.getName();
			} else if (property.equals("1")) {
				return sheet.getValue().getCssText();
			}
		}

		return null;
	}

	@Override
	public boolean canModify(Object element, String property) {
		if (element instanceof Property) {
			Property sheet = (Property) element;
			if(property.equals("0")) {
				return false;
			}
			return true;
		}
		
		return false;

		
	}

	@Override
	public void modify(Object element, String property, Object value) {

	}

}
