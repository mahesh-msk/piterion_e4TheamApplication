package theme_plugin_project.dialogs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.engine.CSSSWTEngineImpl;
import org.eclipse.e4.ui.css.swt.internal.theme.ThemeEngine;
import org.eclipse.e4.ui.css.swt.theme.ITheme;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DialogCellEditor;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.osgi.service.prefs.Preferences;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;
import org.xml.sax.InputSource;

import com.steadystate.css.dom.CSSStyleRuleImpl;
import com.steadystate.css.dom.CSSStyleSheetImpl;
import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;

import theme_plugin_project.dialogs.providers.CssPreviewColumnLabelProvider;
import theme_plugin_project.dialogs.providers.CssThemeLabelProvider;
import theme_plugin_project.dialogs.providers.CssTreeContentProvider;
import theme_plugin_project.dialogs.providers.ThemeListContentProvider;

@SuppressWarnings({"restriction","unused","rawtypes"})

public class OpenDialog extends Dialog {

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
	private DialogCellEditor dialogCellEditor;
	/**
	 * 
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public OpenDialog(Shell parentShell, IThemeEngine engine) {

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

		comboViewer = new ComboViewer(container,SWT.READ_ONLY);
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
				if(iStructuredSelection.isEmpty()) {
					return;
				}
				element = iStructuredSelection.getFirstElement();
				if (element instanceof File) {
					File file = (File) element;
					CSSStyleSheet cssFile = parseCssFile(file);
					
					btnDeleteTheme.setEnabled(true);
					btnAddTheme.setEnabled(false);
					

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
		treeViewer = new TreeViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		Tree tree = treeViewer.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		tree.addListener(SWT.EraseItem, new  Listener() {
			
			@Override
			public void handleEvent(Event event) {
				
				//allow selection in tree?
				event.detail &= ~SWT.SELECTED;
				event.detail &= ~SWT.HOT;
				
			}
		});

//		TreeColumn trclmnName = new TreeColumn(tree, SWT.NONE);
//		trclmnName.setWidth(116);
//		trclmnName.setText("Name");
//
//		TreeColumn trclmnValue = new TreeColumn(tree, SWT.NONE);
//		trclmnValue.setWidth(88);
//		trclmnValue.setText("Value");
//		
//		TreeColumn trclmnPreview = new TreeColumn(tree, SWT.NONE);
//		trclmnPreview.setWidth(55);
//		trclmnPreview.setText("Preview");
		
		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnNewColumn = treeViewerColumn_1.getColumn();
		trclmnNewColumn.setWidth(123);
		trclmnNewColumn.setText("Name");	
		treeViewerColumn_1.setLabelProvider(new CssNameColumnLabelProvider());


		TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnNewColumn_1 = treeViewerColumn_2.getColumn();
		trclmnNewColumn_1.setWidth(94);
		trclmnNewColumn_1.setText("Value");
		treeViewerColumn_2.setLabelProvider(new CssValueColumnLabelProvider());
		
		TreeViewerColumn treeViewerColumn_3 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnNewColumn_2 = treeViewerColumn_3.getColumn();
		trclmnNewColumn_2.setWidth(58);
		trclmnNewColumn_2.setText("Preview");
		treeViewerColumn_3.setLabelProvider(new CssPreviewColumnLabelProvider());
		
		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn treeColumn = treeViewerColumn.getColumn();
		treeColumn.setWidth(47);
		
		 treeViewerColumn.setLabelProvider(new ColumnLabelProvider(){
	            //make sure you dispose these buttons when viewer input changes
	            Map<Object, Button> buttons = new HashMap<Object, Button>();


	            @Override
	            public void update(ViewerCell cell) {

	                TreeItem item = (TreeItem) cell.getItem();
	                Button button;
	                if(buttons.containsKey(cell.getElement()))
	                {
	                    button = buttons.get(cell.getElement());
	                }
	                else
	                {
	                     button = new Button((Composite) cell.getViewerRow().getControl(),SWT.NONE);
	                    button.setText("Click");
	                    buttons.put(cell.getElement(), button);
	                }
	                TreeEditor editor = new TreeEditor(item.getParent());
	                editor.grabHorizontal  = true;
	                editor.grabVertical = true;
	                editor.setEditor(button , item, cell.getColumnIndex());
	                editor.layout();
	            }

	        });
	
	
		
		treeViewer.setContentProvider(new CssTreeContentProvider());
	//	treeViewer.setLabelProvider(new CssTreeLabelProvider());
	
//		TreeColumn[] columns = tree.getColumns();
//		CellEditor[] cellEditors = new CellEditor[columns.length];
//		for (int i = 0; i < cellEditors.length; i++) {
//			if (i == 0) {
//				TextCellEditor textCellEditor = new TextCellEditor(tree);
//				Text text = (Text) textCellEditor.getControl();
//				cellEditors[i] = textCellEditor;
//				text.setData("org.eclipse.e4.ui.css.id", "MyCSSTagForlblTheme");
//			} else if (i == 1) {
//				dialogCellEditor = new CssPropertyDialogCellEditor(treeViewer);
//				cellEditors[i] = dialogCellEditor;
//				dialogCellEditor.getControl().setData("org.eclipse.e4.ui.css.id", "MyCSSTagForlblTheme");
//			} 
//		}
//		treeViewer.setCellEditors(cellEditors);
//		treeViewer.setCellModifier(new CssPropertyCellModifier());
//		
		
//
//		String[] strings = new String[] { "0", "1" };
//		treeViewer.setColumnProperties(strings);
//		
//		 treeViewer.addDoubleClickListener(new
//		 PropertyDoubleClickListener(treeViewer));

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
												
						final ISelection cvSelection = new StructuredSelection(((ThemeEngine) engine).getThemes().get(0));					
					
						comboViewer.setSelection(cvSelection);
						btnAddTheme.setEnabled(true);				
									
					}
				}

			}
		});
		btnDeleteTheme.setText("Delete Theme");
		
		//checking for preference store
		   Preferences preferences = InstanceScope.INSTANCE.getNode("Theme_Plugin_Project");
           Preferences sub1 = preferences.node("node1");

          // System.out.println(sub1.get("cusstomCss", "default"));
           
        //   File file = new File(sub1.get("cusstomCssPath", "default"));
		
		if(sub1.get("cusstomCssName", "default").equals("custom.css")){
			
			
		    
			if(file.exists()){
				
				final ISelection cvSelection = new StructuredSelection(file);
				comboViewer.setSelection(cvSelection);
				btnAddTheme.setEnabled(false);
				btnDeleteTheme.setEnabled(true);
		
				
			}
			else{
				final ISelection cvSelection = new StructuredSelection(((ThemeEngine) engine).getThemes().get(0));
				comboViewer.setSelection(cvSelection);
				btnAddTheme.setEnabled(true);
				btnDeleteTheme.setEnabled(false);
			
			}
		}	else{
			
			if(file.exists()){
				final ISelection cvSelection = new StructuredSelection(((ThemeEngine) engine).getThemes().get(0));
				comboViewer.setSelection(cvSelection);
				btnAddTheme.setEnabled(false);
				btnDeleteTheme.setEnabled(false);
			} else{
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
			org.w3c.css.sac.InputSource inputSource = new org.w3c.css.sac.InputSource(new FileReader(cssFile));
			CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
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
						|| item.getSelectorText().equals("ScrolledComposite") || item.getSelectorText().equals("ApplicationLabel") 
						|| item.getSelectorText().equals("ProjectLabel") || item.getSelectorText().equals("UserProfileWidget") 
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
				newStyleSheetImpl.insertRule(".MPartStack {swt-maximize-visible: false;	swt-minimize-visible: false; } \n",
					     0);
				newStyleSheetImpl.insertRule("Composite Label { font: Constantia 10px Normal; color: rgb(128,128,192) } \n",
						1);
				newStyleSheetImpl.insertRule("Composite  { color: #00ff40 }", 2);
				newStyleSheetImpl.insertRule("SashForm  { color: #00ff40 }", 3);
				newStyleSheetImpl.insertRule("ScrolledComposite { background-color: rgb(128,128,192) ; color: rgb(0,0,0) } \n",
					      4);
				newStyleSheetImpl.insertRule("ApplicationLabel {font: Arabic Transparent 12px; } \n",
					      5);
				newStyleSheetImpl.insertRule("ProjectLabel {font: Arabic Transparent 11px; } \n",
					     6);
				newStyleSheetImpl.insertRule("UserProfileWidget { header-background-color: rgb(255,0,0) } \n",
					      7);
			    newStyleSheetImpl.insertRule("#MyCSSTagForlblTheme { color: rgb(0,0,0) } \n",
							      8);

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
}
