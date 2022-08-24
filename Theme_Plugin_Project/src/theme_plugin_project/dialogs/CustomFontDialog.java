package theme_plugin_project.dialogs;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.e4.ui.css.swt.internal.theme.ThemeEngine;
import org.eclipse.e4.ui.services.IStylingEngine;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.wb.swt.SWTResourceManager;

import theme_plugin_project.dialogs.providers.FontHeightLableProvider;
import theme_plugin_project.dialogs.providers.FontNameLableProvider;
import org.eclipse.swt.widgets.Text;

public class CustomFontDialog extends Dialog {

	private ListViewer listViewer_2;
	private ListViewer listViewer;
	private ListViewer listViewer_1;
	private Label txtAabbccyyzz;
	private String ftName = "Segoe UI";
	private String fStyle = null;
	private int fSize = 10;
	private String dfont;
	private int dfontSize;
	private String dfontStyle;
	private FontData fntData;
	private Map<String, Integer> fontSty;
	private Label lblFont;
	private Label lblNewLabel;
	private Label lblSize;
	private int fontStyle;

	public CustomFontDialog(Shell parentShell, int Style, String dfont, int dfontSize, String dfontStyle) {
		super(parentShell);
		this.dfont = dfont;
		this.dfontSize = dfontSize;
		this.dfontStyle = dfontStyle;

	}

	@Override
	protected void configureShell(Shell shell) {

		super.configureShell(shell);
		shell.setText("Select Font");
		shell.setSize(400, 350);
	}

	// IStylingEngine is injected
	@Inject IStylingEngine engine;

	
	@Override
	protected Control createDialogArea(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_composite.heightHint = 167;
		composite.setLayoutData(gd_composite);

		lblFont = new Label(composite, SWT.NONE);
		lblFont.setText("Font");
		lblFont.setData("org.eclipse.e4.ui.css.id", "MyCSSTagForlblTheme");

		lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setText("Style");
		lblNewLabel.setData("org.eclipse.e4.ui.css.id", "MyCSSTagForlblTheme");

		lblSize = new Label(composite, SWT.NONE);
		lblSize.setText("Size");
		lblSize.setData("org.eclipse.e4.ui.css.id", "MyCSSTagForlblTheme");

//		Composite composite_2 = new Composite(composite, SWT.NONE);
//		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		listViewer = new ListViewer(composite, SWT.BORDER | SWT.V_SCROLL);
		List list = listViewer.getList();
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

//		Composite composite_3 = new Composite(composite, SWT.NONE);
//		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		listViewer_1 = new ListViewer(composite, SWT.BORDER | SWT.V_SCROLL);
		List list_1 = listViewer_1.getList();
		list_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

//		Composite composite_4 = new Composite(composite, SWT.NONE);
//		composite_4.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));

		listViewer_2 = new ListViewer(composite, SWT.BORDER | SWT.V_SCROLL);
		List list_2 = listViewer_2.getList();
		list_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

//		Composite composite_1 = new Composite(container, SWT.NONE);
//		composite_1.setToolTipText("Sample");
//		composite_1.setLayout(new GridLayout(1, false));
//		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			
		txtAabbccyyzz = new Label(composite, SWT.BORDER |  SWT.CENTER);
		
		txtAabbccyyzz.setText("aAbBcCyYzZ");
		GridData gd_txtAabbccyyzz = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
		gd_txtAabbccyyzz.heightHint = 28;
		txtAabbccyyzz.setLayoutData(gd_txtAabbccyyzz);
	
	//	txtAabbccyyzz.setSize(100, 100);
	
		ISelectionChangedListener listner  = new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				
				IStructuredSelection selection = (IStructuredSelection) listViewer.getSelection();
				IStructuredSelection selection1 = (IStructuredSelection) listViewer_1.getSelection();
				IStructuredSelection selection2 = (IStructuredSelection) listViewer_2.getSelection();

				ftName = (!selection.isEmpty()) ? selection.getFirstElement().toString() : "Arial";
				fStyle = (!selection1.isEmpty()) ? selection1.getFirstElement().toString() : "Normal";
				fSize = (!selection2.isEmpty()) ? Integer.parseInt(selection2.getFirstElement().toString()) : 10;
				
				Integer integer = fontSty.get(fStyle);
				fontStyle = SWT.NONE;

				switch (integer) {
				case 1:
					fontStyle = SWT.ITALIC;
					break;
				case 2:
					fontStyle = SWT.BOLD;
					break;
				
				default:
					break;
				}
							
				txtAabbccyyzz.setFont(new org.eclipse.swt.graphics.Font(Display.getCurrent(), ftName, fSize, fontStyle));

				
			}
		};
		
		listViewer.addSelectionChangedListener(listner);
		listViewer_1.addSelectionChangedListener(listner);
		listViewer_2.addSelectionChangedListener(listner);
		
		
		
		ArrayList<String> fontName = new ArrayList<>();
		String fonts[] = 
			      GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

			    for ( int i = 0; i < fonts.length; i++ )
			    {
			    	fontName.add(fonts[i]);
			    }
		ArrayList<Integer> fontheight = new ArrayList<>(Arrays.asList(2, 4, 6, 8, 10));

		fontSty = new HashMap<String, Integer>();
		fontSty.put("Normal", 0);
		fontSty.put("Italic", 1);
		fontSty.put("Bold", 2);

		listViewer.setContentProvider(new ArrayContentProvider());
		listViewer.setLabelProvider(new FontNameLableProvider());
		listViewer.setInput(fontName);

		listViewer_1.setContentProvider(new ArrayContentProvider());
		listViewer_1.setLabelProvider(new FontHeightLableProvider());
		listViewer_1.setInput(fontSty.keySet());

		listViewer_2.setContentProvider(new ArrayContentProvider());
		listViewer_2.setLabelProvider(new FontStyleLableProvider());
		listViewer_2.setInput(fontheight);

		final ISelection nmSelection = new StructuredSelection(this.dfont.trim());
		listViewer.setSelection(nmSelection);

		final ISelection styleSelection = new StructuredSelection("Normal");
		listViewer_1.setSelection(styleSelection);

		final ISelection sizeSelection = new StructuredSelection(this.dfontSize);
		listViewer_2.setSelection(sizeSelection);

		return composite;
	}

	@Override
	protected void okPressed() {
		IStructuredSelection selection = (IStructuredSelection) listViewer.getSelection();
		IStructuredSelection selection1 = (IStructuredSelection) listViewer_1.getSelection();
		IStructuredSelection selection2 = (IStructuredSelection) listViewer_2.getSelection();

		ftName = (!selection.isEmpty()) ? selection.getFirstElement().toString() : "Arial";
		fStyle = (!selection1.isEmpty()) ? selection1.getFirstElement().toString() : "Normal";
		fSize = (!selection2.isEmpty()) ? Integer.parseInt(selection2.getFirstElement().toString()) : 10;

		Integer integer = fontSty.get(fStyle);
		fontStyle = SWT.NONE;

		switch (integer) {
		case 1:
			fontStyle = SWT.ITALIC;
			break;
		case 2:
			fontStyle = SWT.BOLD;
			break;
		
		default:
			break;
		}

		fntData = new FontData(ftName, fSize, fontStyle);

		System.out.println("Font Data Selected:" + ftName + ", " + fStyle + ", " + fSize);

		super.okPressed();
	}

	public FontData getFontData() {
		return fntData;
	}

}
