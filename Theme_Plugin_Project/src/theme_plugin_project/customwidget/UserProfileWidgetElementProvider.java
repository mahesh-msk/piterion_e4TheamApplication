package theme_plugin_project.customwidget;

import org.eclipse.e4.ui.css.core.dom.IElementProvider;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.w3c.dom.Element;

public class UserProfileWidgetElementProvider implements IElementProvider {

	public UserProfileWidgetElementProvider() {
	}

	@Override
	public Element getElement(Object element, CSSEngine engine) {
		if (element instanceof UserProfileWidget) {
			return new UserProfileElementAdapter((UserProfileWidget) element, engine);
		}
		return null;
	}

}
