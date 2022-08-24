package theme_plugin_project.dialogs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;

import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.Property;

public class PropertyDoubleClickListener implements IDoubleClickListener {

	private TreeViewer treeViewer;

	public PropertyDoubleClickListener(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}

	@Override
	public void doubleClick(DoubleClickEvent event) {

		IStructuredSelection iStructuredSelection = (IStructuredSelection) event.getSelection();
		Object element = iStructuredSelection.getFirstElement();
		if (element instanceof Property) {
			Property property = (Property) element;

			String key = property.getName();
			if (key.equals("font")) {
				configureFont(property);
			} else if (key.equals("color") || key.equals("background-color")) {
				{
					configureColor(property);
				}

			}
			treeViewer.refresh();

		}

	}

	private void configureColor(Property property) {
		String cssText = property.getValue().getCssText();
		System.out.println(cssText);
		RGB oldRGB = null;
		if(cssText.startsWith("rgb")){
		String rgbString = cssText.replace("rgb(", "").replace(")", "");
		String[] split = rgbString.split(",");
		// rgb(128, 0, 64)
		
		int oldRed = Integer.parseInt(split[0].trim());
		int oldGreen = Integer.parseInt(split[1].trim());
		int oldBlue = Integer.parseInt(split[2].trim());
		
	
		 oldRGB = new RGB(oldRed, oldGreen, oldBlue);
		}
		ColorDialog colorDialog = new ColorDialog(Display.getDefault().getActiveShell());
		
		if(cssText!=null)
		colorDialog.setRGB(oldRGB);
		RGB rgb = colorDialog.open();

		if (rgb == null) {
			return;
		}
		int red = rgb.red;
		int green = rgb.green;
		int blue = rgb.blue;

		String rbgColor = "rgb(" + red + "," + green + "," + blue + ")";
		CSSValueImpl val = new CSSValueImpl();
		val.setValue(rbgColor);
		property.setValue(val);
	}

	private void configureFont(Property property) {
		String cssText = property.getValue().getCssText();

		Pattern p = Pattern.compile("([0-9])");
		String font = "";
		int fontSize = 0;

		String[] splitString = cssText.split(" ");
		for (String string : splitString) {
			Matcher m = p.matcher(string);
			boolean b = m.find();
			if (b) {
				String replace = string.replace("px", "");
				fontSize = Integer.parseInt(replace);
			} else {
				font += string + " ";
			}
		}
		System.out.println(font);
		System.out.println(fontSize);

		FontDialog fd = new FontDialog(Display.getDefault().getActiveShell(), SWT.NONE);
		fd.setText("Select Font");
		fd.setRGB(new RGB(0, 0, 255));
		FontData defaultFont = new FontData(font.trim(), fontSize, SWT.BOLD);
		fd.setFontData(defaultFont);
		FontData fontData = fd.open();

		if (fontData == null) {
			return;
		}

		String fontName = fontData.getName();
		String fontPixel = fontData.getHeight() + "px";
		String newfont = fontName + " " + fontPixel;

		CSSValueImpl val = new CSSValueImpl();
		val.setValue(newfont);
		property.setValue(val);
	}

}
