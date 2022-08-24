package theme_plugin_project.dialogs.providers;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ThemeListContentProvider implements IStructuredContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		
		if (inputElement instanceof ArrayList) {
			ArrayList arrayList = (ArrayList) inputElement;
			File file = new File(Platform.getInstallLocation().getURL().getPath() + "\\themecss\\");
			File[] listFiles = file.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					if (pathname.getName().endsWith(".css")) {
						return true;
					}
					return false;
				}
			});

			for (File cssFile : listFiles) {
				arrayList.add(cssFile);
			}
			return arrayList.toArray();
			
		}
		return null;
	}

	@Override
	public void dispose() {
		
		
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		
		
	}

}
