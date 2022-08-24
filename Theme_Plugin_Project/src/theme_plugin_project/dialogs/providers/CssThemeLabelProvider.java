package theme_plugin_project.dialogs.providers;

import java.io.File;

import org.eclipse.e4.ui.css.swt.theme.ITheme;
import org.eclipse.jface.viewers.LabelProvider;

public class CssThemeLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		// TODO Auto-generated method stub
		if (element instanceof ITheme) {
			ITheme iTheme = (ITheme) element;
			return iTheme.getLabel();

		} else if (element instanceof File) {
			File file = (File) element;
			String name = file.getName();
			String s1 = name.replace(".css", "");
			String s2 = s1.toUpperCase();
			return s2;

		}
		return super.getText(element);
	}

}
