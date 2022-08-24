package theme_plugin_project.dialogs.providers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.e4.ui.css.core.dom.CSSProperty;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.steadystate.css.dom.CSSStyleRuleImpl;
import com.steadystate.css.dom.Property;
@SuppressWarnings("restriction")
public class CssTreeLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider,IFontProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof CSSStyleRuleImpl) {
			CSSStyleRuleImpl cssrule = (CSSStyleRuleImpl) element;

			switch (columnIndex) {
			case 0:
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

			default:
				break;
			}

		} else if (element instanceof Property) {
			Property property = (Property) element;
			switch (columnIndex) {
			case 0:

				String substring = property.getName();

				return substring;
			case 1:

				return property.getValue().getCssText();

			default:
				break;
			}

		} else if (element instanceof org.eclipse.e4.ui.css.core.impl.dom.CSSStyleRuleImpl) {
			org.eclipse.e4.ui.css.core.impl.dom.CSSStyleRuleImpl cssStyleRuleImpl = (org.eclipse.e4.ui.css.core.impl.dom.CSSStyleRuleImpl) element;

			switch (columnIndex) {
			case 0:
				if (cssStyleRuleImpl.getSelectorText().toString().equals("*[class=\"MPartStack\"]")) {
					return "Part Stack";
				}else if (cssStyleRuleImpl.getSelectorText().toString().equals("*#ApplicationLabel")) {
					return "Application Lable";
				}else if (cssStyleRuleImpl.getSelectorText().toString().equals("*#ProjectLabel")) {
					return "Project Lable";
				}

				return cssStyleRuleImpl.getSelectorText();

			default:
				break;
			}
		} else if (element instanceof CSSProperty) {
			CSSProperty cssProperty = (CSSProperty) element;
			switch (columnIndex) {
			case 0:

				String substring = cssProperty.getName();

				return substring;
			case 1:

				return cssProperty.getValue().getCssText();

			default:
				break;
			}

		}
		return null;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Color getBackground(Object element, int columnIndex) {
		Color color = null;
		 if (element instanceof CSSProperty) {
				CSSProperty cssProperty = (CSSProperty) element;
				switch (columnIndex) {
				
				case 2:
		
					String cssText = cssProperty.getValue().getCssText();
					if(cssText.startsWith("rgb")) {
						String[] values = cssText.split(",");
						Integer red= Integer.parseInt(values[0].substring(4,values[0].length()).trim());
						Integer green= Integer.parseInt(values[1].trim());
						Integer blue= Integer.parseInt(values[2].substring(0,values[2].length()-1).trim());
						return new Color(Display.getDefault(), new RGB(red, green, blue));
					}
					break;
				default:
					break;
				}

			} else if (element instanceof Property) {
				Property property = (Property) element;
				switch (columnIndex) {
				case 2:

				String cssText1 = property.getValue().getCssText();
				if(cssText1.startsWith("rgb")) {
					String[] values1 = cssText1.split(","); 
					Integer red1= Integer.parseInt(values1[0].substring(4,values1[0].length()).trim());
					Integer green1= Integer.parseInt(values1[1].trim());
					Integer blue1= Integer.parseInt(values1[2].substring(0,values1[2].length()-1).trim());
					
					return new Color(Display.getDefault(), new RGB(red1, green1, blue1));
				}
				break;				

				default:
					break;
				}

			}
		return color;
	}

	@Override
	public Font getFont(Object element) {
		Font font = null;
		if (element instanceof CSSProperty) {
			CSSProperty cssProperty = (CSSProperty) element;

			String cssText = cssProperty.getValue().getCssText();
			if (!cssText.startsWith("rgb")) {
		
				String font1 = "";
				int fontSize = 0;
				String fontStyle = "";
				boolean fnt = true;
				boolean sty = false;
				Pattern p = Pattern.compile("([0-9])");
		
				String[] splitString = cssText.split(" ");
				for (String string : splitString) {
					Matcher matcher = p.matcher(string);
					boolean b = matcher.find();
				
					if (b) {
						String replace = string.replaceAll("px|.0", "");
						fontSize = Integer.parseInt(replace);
						fnt= false;
						sty=true;
					
				    	} else if(fnt){
						font1 += string + " ";
		
					}
					else if(sty) {
						fontStyle = string;
					}
				
				}
			
				//return new Font(Display.getDefault(), new FontData(font1, fontSize, SWT.BOLD));
				return new Font(Display.getDefault(), new FontData("Arial", 10, SWT.NORMAL));

			}
		} else if (element instanceof Property) {
			Property property = (Property) element;

			String cssText1 = property.getValue().getCssText();
			if (!cssText1.startsWith("rgb")) {
		
				String font1 = "";
				int fontSize = 0;
				String fontStyle = "";
				boolean fnt = true;
				boolean sty = false;
				Pattern p = Pattern.compile("([0-9])");
		
				String[] splitString = cssText1.split(" ");
				for (String string : splitString) {
					Matcher matcher = p.matcher(string);
					boolean b = matcher.find();
				
					if (b) {
						String replace = string.replace("px", "");
						fontSize = Integer.parseInt(replace);
						fnt= false;
						sty=true;
					
				    	} else if(fnt){
						font1 += string + " ";
		
					}
					else if(sty) {
						fontStyle = string;

					}
				
				}
			
				//return new Font(Display.getDefault(), new FontData(font1, fontSize, SWT.NORMAL));
				return new Font(Display.getDefault(), new FontData("Arial", 10, SWT.NORMAL));

			}

		}

		return font;
	}
}