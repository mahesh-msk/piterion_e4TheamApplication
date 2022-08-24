package theme_plugin_project.labelprvider;

import java.io.File;

import org.eclipse.jface.viewers.LabelProvider;

public class OpenDialogueLableProvider extends LabelProvider {
	@Override
	public String getText(Object element) {
		if (element instanceof File) {
			File file = (File) element;
			return file.getName();
			
		}
		return super.getText(element);
	}

}
