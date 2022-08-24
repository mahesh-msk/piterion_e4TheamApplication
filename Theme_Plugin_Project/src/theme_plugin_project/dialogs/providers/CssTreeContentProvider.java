package theme_plugin_project.dialogs.providers;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.dom.CSSStyleDeclarationImpl;
import com.steadystate.css.dom.CSSStyleRuleImpl;

@SuppressWarnings({ "restriction", "rawtypes", "unchecked" })
public class CssTreeContentProvider implements ITreeContentProvider {
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof CSSStyleSheet) {
			CSSStyleSheet cssStyleSheet = (CSSStyleSheet) inputElement;
			CSSRuleList cssRules = cssStyleSheet.getCssRules();
			
			int length = cssRules.getLength();

			ArrayList rules = new ArrayList();

			for (int i = 0; i < length; i++) {
				// CSSStyleRuleImpl item = (CSSStyleRuleImpl) cssRules.item(i);
				if(!cssRules.item(i).getCssText().startsWith("*[class=\"MPartStack\"]") && !cssRules.item(i).getCssText().startsWith("#MyCSSTagForlblTheme")){
					rules.add(cssRules.item(i));
				} 
				
			}
			return rules.toArray();
		}
		return new ArrayList().toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof CSSStyleRuleImpl) {
			CSSStyleRuleImpl cssRule = (CSSStyleRuleImpl) parentElement;
			CSSStyleDeclarationImpl style = (CSSStyleDeclarationImpl) cssRule.getStyle();

			return style.getProperties().toArray();

		} else {

			org.eclipse.e4.ui.css.core.impl.dom.CSSStyleRuleImpl cssStyleRuleImpl = (org.eclipse.e4.ui.css.core.impl.dom.CSSStyleRuleImpl) parentElement;

			org.eclipse.e4.ui.css.core.impl.dom.CSSStyleDeclarationImpl style = (org.eclipse.e4.ui.css.core.impl.dom.CSSStyleDeclarationImpl) cssStyleRuleImpl
					.getStyle();

			ArrayList properties = new ArrayList();
			int size = style.getCSSPropertyList().getLength();
			for (int i = 0; i < size; i++) {
				properties.add(style.getCSSPropertyList().item(i));
			}

			return properties.toArray();
		}
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof CSSRule) {

			return true;
		}
		return false;

	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {

	}

}
