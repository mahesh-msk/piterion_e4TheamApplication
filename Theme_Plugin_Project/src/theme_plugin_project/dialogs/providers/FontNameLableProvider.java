package theme_plugin_project.dialogs.providers;

import org.eclipse.jface.viewers.LabelProvider;

public class FontNameLableProvider extends LabelProvider {
	
	@Override
	public String getText(Object element) {
		
		//if (element instanceof String[]) {
			//String[] fontName = (String[]) element;
			return element.toString();
			
		//}

	}

}
