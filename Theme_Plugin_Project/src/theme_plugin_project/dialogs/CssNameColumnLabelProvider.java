package theme_plugin_project.dialogs;

import org.eclipse.e4.ui.css.core.dom.CSSProperty;
import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.steadystate.css.dom.CSSStyleRuleImpl;
import com.steadystate.css.dom.Property;

public class CssNameColumnLabelProvider extends ColumnLabelProvider {
	
    @Override
    public String getText(Object element) {
    	

		if (element instanceof CSSStyleRuleImpl) {
			CSSStyleRuleImpl cssrule = (CSSStyleRuleImpl) element;

			
				if (cssrule.getSelectorText().toString().equals(".CustomButton")) {
					return "Button Application";
				}else if (cssrule.getSelectorText().toString().equals(".MPartStack")) {
					return "Part Stack";
				}else if (cssrule.getSelectorText().toString().equals("#ApplicationLabel")) {
					return "Application Lable";
				}else if (cssrule.getSelectorText().toString().equals("#ProjectLabel")) {
					return "Project Lable";
				}

				return cssrule.getSelectorText();

		

		} else if (element instanceof Property) {
			Property property = (Property) element;
		
				String substring = property.getName();

				return substring;
	

		} else if (element instanceof org.eclipse.e4.ui.css.core.impl.dom.CSSStyleRuleImpl) {
			org.eclipse.e4.ui.css.core.impl.dom.CSSStyleRuleImpl cssStyleRuleImpl = (org.eclipse.e4.ui.css.core.impl.dom.CSSStyleRuleImpl) element;

				if (cssStyleRuleImpl.getSelectorText().toString().equals("*[class=\"MPartStack\"]")) {
					return "Part Stack";
				}else if (cssStyleRuleImpl.getSelectorText().toString().equals("*#ApplicationLabel")) {
					return "Application Lable";
				}else if (cssStyleRuleImpl.getSelectorText().toString().equals("*#ProjectLabel")) {
					return "Project Lable";
				}

				return cssStyleRuleImpl.getSelectorText();

		} else if (element instanceof CSSProperty) {
			CSSProperty cssProperty = (CSSProperty) element;

				String substring = cssProperty.getName();

				return substring;
	

		}
		return null;

    }


}
