package theme_plugin_project.dialogs.providers;

import org.eclipse.e4.ui.css.core.dom.CSSProperty;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.steadystate.css.dom.Property;

public class CssPreviewColumnLabelProvider extends ColumnLabelProvider {
	
	   @Override
       public String getText(Object element) {
        

           return null;
       }
       
     @Override
   public Color getBackground(Object element) {
   	
   	  Color color = null;
		 if (element instanceof CSSProperty) {
				CSSProperty cssProperty = (CSSProperty) element;
		
		
					String cssText = cssProperty.getValue().getCssText();
					if(cssText.startsWith("rgb")) {
						String[] values = cssText.split(",");
						Integer red= Integer.parseInt(values[0].substring(4,values[0].length()).trim());
						Integer green= Integer.parseInt(values[1].trim());
						Integer blue= Integer.parseInt(values[2].substring(0,values[2].length()-1).trim());
						return new Color(Display.getDefault(), new RGB(red, green, blue));
					}
		
			} else if (element instanceof Property) {
				Property property = (Property) element;
		
				String cssText1 = property.getValue().getCssText();
				if(cssText1.startsWith("rgb")) {
					String[] values1 = cssText1.split(","); 
					Integer red1= Integer.parseInt(values1[0].substring(4,values1[0].length()).trim());
					Integer green1= Integer.parseInt(values1[1].trim());
					Integer blue1= Integer.parseInt(values1[2].substring(0,values1[2].length()-1).trim());
					
					return new Color(Display.getDefault(), new RGB(red1, green1, blue1));
				}
			

			}
		return color;
   }

}
