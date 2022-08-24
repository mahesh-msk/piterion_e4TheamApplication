package theme_plugin_project.dialogs.celleditors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;

import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.Property;

import theme_plugin_project.dialogs.CustomFontDialog;

public class CssPropertyDialogCellEditor extends DialogCellEditor {
	
	

	private TreeViewer treeViewer;
	private String fontPixel;
	private String newfont;

	public CssPropertyDialogCellEditor(TreeViewer treeViewer) {
		super(treeViewer.getTree());
		this.treeViewer = treeViewer;   
	}
	
	

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		IStructuredSelection iStructuredSelection = (IStructuredSelection) treeViewer.getSelection();
		Object element = iStructuredSelection.getFirstElement();
		if (element instanceof Property) {
			Property property = (Property) element;

			String key = property.getName();
			if (key.equals("font")) {
				configureFont(property);
			} else if (key.equals("color") || key.equals("background-color") || key.equals("header-background-color")
					|| key.equals("foreground-color" )) {
				{
					configureColor(property);
				}

			}
			treeViewer.refresh();

		}
		return null;
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
		String fontStyle = "";
		boolean fnt = true;
		boolean sty = false;

		String[] splitString = cssText.split(" ");
		for (String string : splitString) {
			Matcher m = p.matcher(string);
			boolean b = m.find();
			if (b) {
				String replace = string.replace("px", "");
				fontSize = Integer.parseInt(replace);
				fnt = false;
				sty = true;

			} else if (fnt) {
				font += string + " ";

			} else if (sty) {
				fontStyle = string;

			}
		}
		System.out.println(font);
		System.out.println(fontSize);
		System.out.println(fontStyle);
		
		CustomFontDialog cusFD = new CustomFontDialog(Display.getDefault().getActiveShell(), SWT.NONE,font,fontSize,fontStyle);
		int open = cusFD.open();
		
		if (open == IDialogConstants.OK_ID) {
			
			FontData fontData = cusFD.getFontData();
			
			String fontName = fontData.getName();
			Integer fontStyl = fontData.getStyle();
			
			switch (fontStyl) {
			case SWT.NONE:
				fontStyle = "Normal";
				break;
			case SWT.ITALIC:
				fontStyle = "Italic";
				break;
			case SWT.BOLD:
				fontStyle = "Bold";
				break;
			
			default:
				break;
			}
			
					//System.out.println("Selected Style:"+ fontStyle_new);
					if (fontData.getHeight() > 10) {
						fontPixel = "10" + "px";
					} else {
						fontPixel = fontData.getHeight() + "px";
					}
			
					newfont = fontName + " " + fontPixel + " " + fontStyle;
		}

//		FontDialog fd = new FontDialog(Display.getDefault().getActiveShell(), SWT.NONE);
//		//CustomFontDialog fd = new CustomFontDialog(Display.getDefault().getActiveShell(), SWT.NONE);
//		fd.setText("Select Font");
//		//fd.setRGB(new RGB(0, 0, 255));
//		fd.setEffectsVisible(false); 
//		
//
//		FontData defaultFont = new FontData(font.trim(), fontSize, SWT.NORMAL);
//		fd.setFontData(defaultFont);
//		
//		FontData fontData = fd.open();
//
//		if (fontData == null) {
//			return;
//		}
//
//		String fontName = fontData.getName();
//
//		//System.out.println("Selected Style:"+ fontStyle_new);
//		if (fontData.getHeight() > 14) {
//			fontPixel = "14" + "px";
//		} else {
//			fontPixel = fontData.getHeight() + "px";
//		}
//
//		String newfont = fontName + " " + fontPixel;

		CSSValueImpl val = new CSSValueImpl();
		val.setValue(newfont);
		property.setValue(val);
	}




}
