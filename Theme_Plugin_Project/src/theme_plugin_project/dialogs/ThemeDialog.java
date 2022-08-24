package theme_plugin_project.dialogs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.ui.css.core.dom.CSSProperty;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.engine.CSSSWTEngineImpl;
import org.eclipse.e4.ui.css.swt.internal.theme.ThemeEngine;
import org.eclipse.e4.ui.css.swt.theme.ITheme;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.osgi.service.prefs.Preferences;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.dom.CSSStyleRuleImpl;
import com.steadystate.css.dom.CSSStyleSheetImpl;
import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.Property;
import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.parser.CSSOMParser;

import theme_plugin_project.dialogs.providers.CssThemeLabelProvider;
import theme_plugin_project.dialogs.providers.CssTreeContentProvider;
import theme_plugin_project.dialogs.providers.ThemeListContentProvider;

@SuppressWarnings({ "restriction", "rawtypes","unchecked" })

public class ThemeDialog extends Dialog {

	private final int NUM_OF_THEMES = 2;
	private IThemeEngine engine;
	private ComboViewer comboViewer;
	private ITheme firstElement;
	private File iCSSTheme;
	private TreeViewer treeViewer;
	private CSSStyleSheetImpl stylesheet;

	private Button btnDeleteTheme;
	private Text text_1;
	private Button btnAddTheme;
	File file = new File(Platform.getInstallLocation().getURL().getPath() + "\\themecss\\custom.css");
	private Label lblNewLabel;
	private Label lblTheme;
	 
	
	// make sure you dispose these buttons when viewer input changes
				Map<Object, Label> buttons = new HashMap<Object, Label>();
				Map<Object, TreeEditor> editors = new HashMap<Object, TreeEditor>();
				private String fontPixel;
				private String newfont;

	/**
	 * 
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ThemeDialog(Shell parentShell, IThemeEngine engine) {

		super(parentShell);
		this.engine = engine;
	}

	@Override
	protected void configureShell(Shell shell) {

		super.configureShell(shell);
		shell.setText("Theme Selector Dialog");
		shell.setSize(500, 400);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(3, false));

		lblTheme = new Label(container, SWT.NONE);
		lblTheme.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTheme.setText("Theme");
		lblTheme.setData("org.eclipse.e4.ui.css.id", "MyCSSTagForlblTheme");

		comboViewer = new ComboViewer(container, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		comboViewer.setContentProvider(new ThemeListContentProvider());
		LabelProvider cssItemsLabelProvider = new CssThemeLabelProvider();
		comboViewer.setLabelProvider(cssItemsLabelProvider);

		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			private Object element;

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection iStructuredSelection = (IStructuredSelection) event.getSelection();
				if (iStructuredSelection.isEmpty()) {
					Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
					return;
					
				}
				element = iStructuredSelection.getFirstElement();
				if (element instanceof File) {
					File file = (File) element;
					CSSStyleSheet cssFile = parseCssFile(file);

					btnDeleteTheme.setEnabled(true);
					btnAddTheme.setEnabled(false);
					
					disposeTableEditors();
					treeViewer.setInput(cssFile);
					treeViewer.expandAll();
					String str = file.getName();
					String str1 = str.replace(".css", "");
					text_1.setText(str1);

					text_1.setEnabled(true);
				} else {

					btnDeleteTheme.setEnabled(false);
					ITheme iTheme = (ITheme) element;

					text_1.setText(iTheme.getLabel());
					text_1.setEditable(false);

					CSSEngine object = (CSSSWTEngineImpl) ((ThemeEngine) engine).getCSSEngines().toArray()[0];
					// System.out.println((object.getDocumentCSS()).getStyleSheets().item(0));

					disposeTableEditors();
					
					treeViewer.setInput((object.getDocumentCSS()).getStyleSheets().item(0));
					treeViewer.expandAll();
				}

			}
		});


		ArrayList themes = new ArrayList(((ThemeEngine) engine).getThemes());

		comboViewer.setInput(themes);

		new Label(container, SWT.NONE);

		Composite composite_1 = new Composite(container, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

		lblNewLabel = new Label(composite_1, SWT.NONE);
		lblNewLabel.setBounds(23, 0, 32, 25);
		lblNewLabel.setText("Name");
		lblNewLabel.setData("org.eclipse.e4.ui.css.id", "MyCSSTagForlblTheme");

		text_1 = new Text(container, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		treeViewer = new TreeViewer(container, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		// TreeColumn trclmnName = new TreeColumn(tree, SWT.NONE);
		// trclmnName.setWidth(116);
		// trclmnName.setText("Name");
		//
		// TreeColumn trclmnValue = new TreeColumn(tree, SWT.NONE);
		// trclmnValue.setWidth(88);
		// trclmnValue.setText("Value");
		//
		// TreeColumn trclmnPreview = new TreeColumn(tree, SWT.NONE);
		// trclmnPreview.setWidth(55);
		// trclmnPreview.setText("Preview");

		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnNewColumn = treeViewerColumn_1.getColumn();
		trclmnNewColumn.setWidth(140);
		trclmnNewColumn.setText("Name");
		treeViewerColumn_1.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof CSSStyleRuleImpl) {
					CSSStyleRuleImpl cssrule = (CSSStyleRuleImpl) element;

					if (cssrule.getSelectorText().toString().equals(".CustomButton")) {
						return "Button Application";
					} else if (cssrule.getSelectorText().toString().equals(".MPartStack")) {
						return "Part Stack";
					} else if (cssrule.getSelectorText().toString().equals("#ApplicationLabel")) {
						return "Application Lable";
					} else if (cssrule.getSelectorText().toString().equals("#ProjectLabel")) {
						return "Project Lable";
					}

					return cssrule.getSelectorText();

				} else if (element instanceof Property) {
					Property property = (Property) element;

						String substring = property.getName();

						return substring;
					
					

				} else if (element instanceof org.eclipse.e4.ui.css.core.impl.dom.CSSStyleRuleImpl) {
					org.eclipse.e4.ui.css.core.impl.dom.CSSStyleRuleImpl cssStyleRuleImpl = (org.eclipse.e4.ui.css.core.impl.dom.CSSStyleRuleImpl) element;

						if (cssStyleRuleImpl.getSelectorText().toString().equals("*[class=\"MPartStack\"]")) {
							return "Part Stack";
						}else if (cssStyleRuleImpl.getSelectorText().toString().equals("*#ApplicationLabel")) {
							return "Application Lable";
						}else if (cssStyleRuleImpl.getSelectorText().toString().equals("*#ProjectLabel")) {
							return "Project Lable";
						}

						return cssStyleRuleImpl.getSelectorText();

				} else if (element instanceof CSSProperty) {
					CSSProperty cssProperty = (CSSProperty) element;

						String substring = cssProperty.getName();

						return substring;
					
					}

				return null;

			}
		});

		TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnNewColumn_1 = treeViewerColumn_2.getColumn();
		trclmnNewColumn_1.setWidth(140);
		trclmnNewColumn_1.setText("Value");
		treeViewerColumn_2.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				if(element instanceof Property) {
					Property property = (Property) element;

						return property.getValue().getCssText();

					

				} else if (element instanceof CSSProperty) {
					CSSProperty cssProperty = (CSSProperty) element;
					
						return cssProperty.getValue().getCssText();

					

				}
				return null;
			}
			
			
		});

		TreeViewerColumn treeViewerColumn_3 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnNewColumn_2 = treeViewerColumn_3.getColumn();
		trclmnNewColumn_2.setWidth(56);
		trclmnNewColumn_2.setText("Preview");
		treeViewerColumn_3.setLabelProvider(new ColumnLabelProvider(){
			
			@Override
			public String getText(Object element) {
				// TODO Auto-generated method stub
				return "";
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
		});

		TreeViewerColumn treeViewerColumn_4 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnNewColumn_3 = treeViewerColumn_4.getColumn();
		trclmnNewColumn_3.setWidth(20);
		trclmnNewColumn_3.setText("");

		treeViewerColumn_4.setLabelProvider(new ColumnLabelProvider() {
			

			@Override
			public void update(ViewerCell cell) {

				TreeItem item = (TreeItem) cell.getItem();
				
				if (item.getData() instanceof Property) {
					Label button;
					if (buttons.containsKey(cell.getElement())) {
						button = buttons.get(cell.getElement());
					} else {
						
						 Image image = new Image(Display.getCurrent(), "D://img//rsz_edit.png");
						 
					 
						button = new Label((Composite) cell.getViewerRow().getControl(), SWT.NONE);
						button.setImage(image);
						button.setToolTipText("Click to open the property");
						button.setBackground(new Color(Display.getCurrent(), new RGB(255,255,255)));
						
						button.setSize(16,16);
					
						//button.setText("Click");
						button.setData(item.getData());
						buttons.put(cell.getElement(), button);
						
						button.addMouseListener(new MouseAdapter() {
							   @Override
							   public void mouseUp(MouseEvent event) {
							      super.mouseUp(event);

							      if (event.getSource() instanceof Label) {
							         Label label = (Label)event.getSource();
			         
							         Object element = button.getData();
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
							      }
							   }
							});

					}
					TreeEditor editor = new TreeEditor(item.getParent());
					editor.grabHorizontal = true;
					editor.grabVertical = true;
					editor.setEditor(button, item, cell.getColumnIndex());
					editor.layout();
				} else {
					if (editors.get(cell.getElement()) != null) {
						editors.get(cell.getElement()).getEditor().dispose(); 
						editors.get(cell.getElement()).dispose();
						editors.remove(cell.getElement());
						buttons.remove(cell.getElement());
					}
					cell.getControl().redraw();
				}
			}

		});

		treeViewer.setContentProvider(new CssTreeContentProvider());
	
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

		btnAddTheme = new Button(composite, SWT.NONE);
		btnAddTheme.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAddTheme.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				addNewTheme(btnAddTheme);
			}
		});
		btnAddTheme.setText("Add Theme");
		btnAddTheme.setEnabled(validateThemeCount());

		btnDeleteTheme = new Button(composite, SWT.NONE);
		btnDeleteTheme.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		btnDeleteTheme.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) comboViewer.getSelection();
				Object firstElement2 = selection.getFirstElement();
				if (firstElement2 instanceof File) {
					File file = (File) firstElement2;
					boolean openConfirm = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), "Warning",
							"Do you want to delete the selected theme?");
					if (openConfirm) {
						file.delete();

						themes.remove(file);
						((ThemeEngine) engine).resetCurrentTheme();
						engine.setTheme(((ThemeEngine) engine).getThemes().get(0), true);

						comboViewer.refresh();

						final ISelection cvSelection = new StructuredSelection(
								((ThemeEngine) engine).getThemes().get(0));

						comboViewer.setSelection(cvSelection);
						btnAddTheme.setEnabled(true);

					}
				}

			}
		});
		btnDeleteTheme.setText("Delete Theme");

		// checking for preference store
		Preferences preferences = InstanceScope.INSTANCE.getNode("Theme_Plugin_Project");
		Preferences sub1 = preferences.node("node1");

		// System.out.println(sub1.get("cusstomCss", "default"));

		// File file = new File(sub1.get("cusstomCssPath", "default"));

		if (sub1.get("cusstomCssName", "default").equals("custom.css")) {

			if (file.exists()) {

				final ISelection cvSelection = new StructuredSelection(file);
				comboViewer.setSelection(cvSelection);
				btnAddTheme.setEnabled(false);
				btnDeleteTheme.setEnabled(true);

			} else {
				final ISelection cvSelection = new StructuredSelection(((ThemeEngine) engine).getThemes().get(0));
				comboViewer.setSelection(cvSelection);
				btnAddTheme.setEnabled(true);
				btnDeleteTheme.setEnabled(false);

			}
		} else {

			if (file.exists()) {
				final ISelection cvSelection = new StructuredSelection(((ThemeEngine) engine).getThemes().get(0));
				comboViewer.setSelection(cvSelection);
				btnAddTheme.setEnabled(false);
				btnDeleteTheme.setEnabled(false);
			} else {
				final ISelection cvSelection = new StructuredSelection(((ThemeEngine) engine).getThemes().get(0));
				comboViewer.setSelection(cvSelection);
				btnAddTheme.setEnabled(true);
				btnDeleteTheme.setEnabled(false);
			}

		}

		return container;

	}

	private boolean validateThemeCount() {
		int itemCount = comboViewer.getCombo().getItemCount();
		return itemCount < NUM_OF_THEMES;

	}

	private CSSStyleSheet parseCssFile(File cssFile) {
		try {
			InputSource inputSource = new InputSource(new FileReader(cssFile));
			CSSOMParser parser = new CSSOMParser();
			stylesheet = (CSSStyleSheetImpl) parser.parseStyleSheet(inputSource, null, null);

			CSSRuleList cssRules = stylesheet.getCssRules();

			int length = cssRules.getLength();

			ArrayList rules = new ArrayList();

			CSSStyleSheetImpl newStyleSheetImpl = new CSSStyleSheetImpl();

			int j = 0;
			for (int i = 0; i < length; i++) {

				CSSStyleRuleImpl item = (CSSStyleRuleImpl) cssRules.item(i);
				if (item.getSelectorText().equals("Composite Label") || item.getSelectorText().equals("Composite")
						|| item.getSelectorText().equals("SashForm") || item.getSelectorText().equals(".MPartStack")
						|| item.getSelectorText().equals("ScrolledComposite")
						|| item.getSelectorText().equals("ApplicationLabel")
						|| item.getSelectorText().equals("ProjectLabel")
						|| item.getSelectorText().equals("UserProfileWidget")
						|| item.getSelectorText().equals("#MyCSSTagForlblTheme")) {
					rules.add(item);
					String cssText = item.getCssText();
					System.out.println(cssText);
					newStyleSheetImpl.insertRule(cssText, j);
					j++;
				}
				;
			}

			stylesheet = newStyleSheetImpl;

			return newStyleSheetImpl;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	@Override
	protected void okPressed() {
		IStructuredSelection selection = (IStructuredSelection) comboViewer.getSelection();
		if (selection.getFirstElement() instanceof ITheme) {
			firstElement = (ITheme) selection.getFirstElement();
		} else {
			iCSSTheme = (File) selection.getFirstElement();
			CSSFormat format = new CSSFormat();
			format.setRgbAsHex(true);

			try {
				FileOutputStream fos = new FileOutputStream(iCSSTheme);
				String cssText = this.stylesheet.getCssText(format);
				fos.write(cssText.getBytes());
				fos.close();
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		super.okPressed();
	}

	public ITheme getSelectedTheme() {

		return firstElement;

	}

	public File getCSSEngine() {
		return iCSSTheme;
	}

	private void addNewTheme(Button btnAddTheme) {

		try {

			if (!file.exists()) {
				file.createNewFile();

				CSSStyleSheetImpl newStyleSheetImpl = new CSSStyleSheetImpl();
				newStyleSheetImpl
						.insertRule(".MPartStack {swt-maximize-visible: false;	swt-minimize-visible: false; } \n", 0);
				newStyleSheetImpl
						.insertRule("Composite Label { font: Constantia 10px Normal; color: rgb(128,128,192) } \n", 1);
				newStyleSheetImpl.insertRule("Composite  { color: #00ff40 }", 2);
				newStyleSheetImpl.insertRule("SashForm  { color: #00ff40 }", 3);
				newStyleSheetImpl.insertRule(
						"ScrolledComposite { background-color: rgb(128,128,192) ; color: rgb(0,0,0) } \n", 4);
				newStyleSheetImpl.insertRule("ApplicationLabel {font: Arabic Transparent 12px; } \n", 5);
				newStyleSheetImpl.insertRule("ProjectLabel {font: Arabic Transparent 11px; } \n", 6);
				newStyleSheetImpl.insertRule("UserProfileWidget { header-background-color: rgb(255,0,0) } \n", 7);
				newStyleSheetImpl.insertRule("#MyCSSTagForlblTheme { color: rgb(102, 255, 102) } \n", 8);

				CSSFormat format = new CSSFormat();
				format.setRgbAsHex(true);

				try {
					FileOutputStream fos = new FileOutputStream(file);
					String cssText = newStyleSheetImpl.getCssText(format);
					fos.write(cssText.getBytes());
					fos.close();
				} catch (FileNotFoundException e1) {

					e1.printStackTrace();
				} catch (IOException e2) {

					e2.printStackTrace();
				}

				System.out.println("file created");

			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		comboViewer.refresh();
		btnAddTheme.setEnabled(validateThemeCount());
	}

	private void disposeTableEditors() {
		Collection<Label> values = buttons.values();
		for (Label button : values) {
			button.dispose();
		}
		
		Collection<TreeEditor> editorList = editors.values();
		for (TreeEditor treeEditor : editorList) {
			treeEditor.getEditor().dispose();
			treeEditor.dispose();
		}
		
		editors.clear();
		buttons.clear();
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

		CSSValueImpl val = new CSSValueImpl();
		val.setValue(newfont);
		property.setValue(val);
	}
}
