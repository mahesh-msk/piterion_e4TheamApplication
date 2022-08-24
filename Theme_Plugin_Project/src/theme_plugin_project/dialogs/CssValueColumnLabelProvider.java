package theme_plugin_project.dialogs;

import org.eclipse.e4.ui.css.core.dom.CSSProperty;
import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.steadystate.css.dom.CSSStyleRuleImpl;
import com.steadystate.css.dom.Property;

public class CssValueColumnLabelProvider extends ColumnLabelProvider {

	   @Override
       public String getText(Object element) {

		 if (element instanceof Property) {
				Property property = (Property) element;
				

					return property.getValue().getCssText();

			} else if (element instanceof CSSProperty) {
				CSSProperty cssProperty = (CSSProperty) element;
		
					return cssProperty.getValue().getCssText();
			}
		
		 return null;
		
       }
	   
}
