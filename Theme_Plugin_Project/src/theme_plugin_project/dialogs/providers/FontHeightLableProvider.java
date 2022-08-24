package theme_plugin_project.dialogs.providers;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;

public class FontHeightLableProvider extends LabelProvider implements ILabelProvider {
	@Override
	public String getText(Object element) {
     
			return element.toString();

}
}