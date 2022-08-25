/*
 * FILE:            UserProfileCSSHandler.java
 *
 * SW-COMPONENT:    Theme_Plugin_Project
 *
 * DESCRIPTION:     -
 *
 * COPYRIGHT:       © 2015 - 2022 Robert Bosch GmbH
 *
 * The reproduction, distribution and utilization of this file as
 * well as the communication of its contents to others without express
 * authorization is prohibited. Offenders will be held liable for the
 * payment of damages. All rights reserved in the event of the grant
 * of a patent, utility model or design.
 */
package theme_plugin_project.customwidget;

import org.eclipse.e4.ui.css.core.dom.properties.ICSSPropertyHandler;
import org.eclipse.e4.ui.css.core.dom.properties.converters.ICSSValueConverter;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.properties.AbstractCSSPropertySWTHandler;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.w3c.dom.css.CSSValue;

public class UserProfileCSSHandler extends AbstractCSSPropertySWTHandler implements ICSSPropertyHandler {
	private static final String HEADER_COLOR = "header-background-color";
	private static final String FONT = "font";
	private static final String BUTTON_BG = "button-bg-color";
	private static final String BUTTON_COLOR = "button-color";
	private static final String BUTTON_HEADER = "button-header";

	public UserProfileCSSHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void applyCSSProperty(Control control, String property, CSSValue value, String pseudo, CSSEngine engine)
			throws Exception {
		if (control instanceof UserProfileWidget) {
			UserProfileWidget userProfileWidget = (UserProfileWidget) control;
			if (HEADER_COLOR.equalsIgnoreCase(property) && value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE) {
				Color newColor = (Color) engine.convert(value, Color.class, control.getDisplay());
				userProfileWidget.setHeaderColor(newColor);
			} else if (BUTTON_BG.equalsIgnoreCase(property)
					&& value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE) {
				Color newColor = (Color) engine.convert(value, Color.class, control.getDisplay());
				userProfileWidget.setBackground(newColor);
			} else if (BUTTON_COLOR.equalsIgnoreCase(property)
					&& value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE) {
				Color newColor = (Color) engine.convert(value, Color.class, control.getDisplay());
				userProfileWidget.setForeground(newColor);
			} else if (BUTTON_HEADER.equalsIgnoreCase(property)
					&& value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE) {
				Color newColor = (Color) engine.convert(value, Color.class, control.getDisplay());
				userProfileWidget.setHeaderColor(newColor);
			}
		}

	}

	@Override
	protected String retrieveCSSProperty(Control control, String property, String pseudo, CSSEngine engine)
			throws Exception {
		if (control instanceof UserProfileWidget) {
			UserProfileWidget userProfileWidget = (UserProfileWidget) control;
			if (HEADER_COLOR.equalsIgnoreCase(property)) {
				ICSSValueConverter cssValueConverter = engine.getCSSValueConverter(String.class);
				return cssValueConverter.convert(userProfileWidget.getHeaderColor(), engine, null);
			}
			if (BUTTON_BG.equalsIgnoreCase(property)) {
				ICSSValueConverter cssValueConverter = engine.getCSSValueConverter(String.class);
				return cssValueConverter.convert(userProfileWidget.getBackground(), engine, null);
			} else if (BUTTON_COLOR.equalsIgnoreCase(property)) {
				ICSSValueConverter cssValueConverter = engine.getCSSValueConverter(String.class);
				return cssValueConverter.convert(userProfileWidget.getForeground(), engine, null);
			} else if (BUTTON_HEADER.equalsIgnoreCase(property)) {
				ICSSValueConverter cssValueConverter = engine.getCSSValueConverter(String.class);
				return cssValueConverter.convert(userProfileWidget.getHeaderColor(), engine, null);
			}
		}
		return null;
	}

}
