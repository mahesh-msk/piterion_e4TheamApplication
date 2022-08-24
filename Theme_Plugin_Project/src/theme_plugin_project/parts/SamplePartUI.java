package theme_plugin_project.parts;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class SamplePartUI extends Composite{
	
	/** The eclipse context. */
	private IEclipseContext eclipseContext;

	/**
	 * Member variable for resourceManager
	 */
	protected ResourceManager resourceManager;

	/** The tool item. */
	transient protected ToolItem toolItem;

	/** The lbl project list. */
	private Label lblProjectList;

	/** The cb project list. */
	protected TableCombo cbProjectList;

	/** The live message console. */
	
	/** The user app project app list. */
	
	/** The h user app composite. */
	private Composite hUserAppComposite;
	
	/** The h project app composite. */
	private Composite hProjectAppComposite;
	
	/** The v user composite. */
	private Composite vUserComposite;
	
	/** The v project comp. */
	private Composite vProjectComp;
	
	/** The content panel. */
	private Composite contentPanel;
	
	/**
	 * Instantiates a new xm menu part UI.
	 *
	 * @param parent
	 *            the parent
	 * @param style
	 *            the style
	 */
	public SamplePartUI(final Composite parent, final int style) {
		super(parent, style);
	}

	/**
	 * Create UI.
	 *
	 * @param eclipseContext
	 *            the eclipse context
	 */
	protected void createUI(IEclipseContext eclipseContext) {
		this.eclipseContext = eclipseContext;
		try {
			GridLayoutFactory.fillDefaults().numColumns(1).applyTo(this);
			this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			
			createTopComp();
			Label lblSeprator = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(lblSeprator);

			createBottomComp();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Create top comp.
	 */
	private void createTopComp() {
		Composite topComposite = new Composite(this, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(3).applyTo(topComposite);
		topComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		Label lblEmpty = new Label(topComposite, SWT.NONE);
		lblEmpty.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label lblLogo = new Label(topComposite, SWT.BORDER);
		//lblLogo.setImage(new Image(Display.getDefault(), "./icons/banner.jpg"));
		GridDataFactory.fillDefaults().grab(false, false).align(SWT.CENTER, SWT.TOP).applyTo(lblLogo);

		lblEmpty = new Label(topComposite, SWT.NONE);
		lblEmpty.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		lblEmpty = new Label(topComposite, SWT.NONE);
		lblEmpty.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Composite projectListComposite = new Composite(topComposite, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(2).extendedMargins(0, 0, 0, 0).applyTo(projectListComposite);
		GridData projectListLayoutData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
		projectListLayoutData.widthHint = 400;
		projectListComposite.setLayoutData(projectListLayoutData);

		this.lblProjectList = new Label(projectListComposite, SWT.NONE);
		this.lblProjectList.setText("Project List");

		this.cbProjectList = new TableCombo(projectListComposite, SWT.BORDER);
		this.cbProjectList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		final ToolBar toolbar = new ToolBar(topComposite, SWT.NONE);
		this.toolItem = new ToolItem(toolbar, SWT.FLAT);
		this.toolItem.setText("vertical");
		//this.toolItem.setImage(new Image(Display.getDefault(), "icons/horizontal.png"));
		toolbar.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false));
		this.toolItem.setToolTipText("Horizontal");
		
		/*lblEmpty = new Label(topComposite, SWT.NONE);
		lblEmpty.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));*/
	}
	
	/**
	 * Create bottom comp.
	 */
	private void createBottomComp() {
		final SashForm mainVerticalSashform = new SashForm(this, SWT.VERTICAL | SWT.BORDER);
		mainVerticalSashform.setLayout(new GridLayout(1, false));
		mainVerticalSashform.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		createApplicationComp(mainVerticalSashform);

		eclipseContext.set(SashForm.class, mainVerticalSashform);

	}

	/**
	 * Create application comp.
	 *
	 * @param mainVerticalSashform
	 *            the main vertical sashform
	 */
	public void createApplicationComp(SashForm mainVerticalSashform) {

		// create the main composite where stack layout is applied
		this.contentPanel = new Composite(mainVerticalSashform, SWT.NONE);
		
		final StackLayout layout = new StackLayout();
		contentPanel.setLayout(layout);
		
		GridData btnlayoutDataForApp = new GridData(SWT.FILL, SWT.FILL, true, false);
		btnlayoutDataForApp.widthHint = 180;
		GridData horiCompLayoutData = new GridData(SWT.CENTER, SWT.CENTER, false, true);

		// create the horizontal page's content
		final Composite horizontalComposite = new Composite(contentPanel, SWT.NONE);
		horizontalComposite.setLayout(new GridLayout(2, false));
		horizontalComposite.setLayoutData(horiCompLayoutData);
		
		final Label hLblUserApplications = new Label(horizontalComposite, SWT.BOLD);
		hLblUserApplications.setText("User Applications");
		GridDataFactory.fillDefaults().grab(true, false).indent(2, 0).align(SWT.CENTER, SWT.CENTER)
				.applyTo(hLblUserApplications);
		
		final Label hLblProjectApplications = new Label(horizontalComposite, SWT.BOLD);
		hLblProjectApplications.setText("Project Applications");
		GridDataFactory.fillDefaults().grab(true, false).indent(2, 0).align(SWT.CENTER, SWT.CENTER)
				.applyTo(hLblProjectApplications);

		ScrolledComposite hUserAppscrolledComposite = new ScrolledComposite(horizontalComposite, SWT.BORDER | SWT.V_SCROLL);
		hUserAppscrolledComposite.setExpandHorizontal(true);
		hUserAppscrolledComposite.setExpandVertical(true);
		hUserAppscrolledComposite.setLayoutData(horiCompLayoutData);

		this.hUserAppComposite = new Composite(hUserAppscrolledComposite, SWT.NONE);
		hUserAppComposite.setLayout(new GridLayout(1, false));
		hUserAppComposite.setLayoutData(horiCompLayoutData);
		
		for(int i = 0 ; i <= 5 ; i++){
			Button button = new Button(hUserAppComposite, SWT.NONE);
			button.setText("Application " +i);
		}

		ScrolledComposite hProjectAppscrolledComposite = new ScrolledComposite(horizontalComposite, SWT.BORDER | SWT.V_SCROLL);
		hProjectAppscrolledComposite.setExpandHorizontal(true);
		hProjectAppscrolledComposite.setExpandVertical(true);
		hProjectAppscrolledComposite.setLayoutData(horiCompLayoutData);

		this.hProjectAppComposite = new Composite(hProjectAppscrolledComposite, SWT.NONE);
		hProjectAppComposite.setLayout(new GridLayout(1, false));
		hProjectAppComposite.setLayoutData(horiCompLayoutData);
		
		for(int i = 0 ; i <= 5 ; i++){
			Button button = new Button(hProjectAppComposite, SWT.NONE);
			button.setText("Application " +i);
		}
		
		hUserAppscrolledComposite.setContent(hUserAppComposite);
		hUserAppscrolledComposite.setMinSize(hUserAppComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		hUserAppscrolledComposite.notifyListeners(SWT.Resize, new Event());
		hUserAppscrolledComposite.setAlwaysShowScrollBars(true);
		
		hProjectAppscrolledComposite.setContent(hProjectAppComposite);
		hProjectAppscrolledComposite.setMinSize(hProjectAppComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		hProjectAppscrolledComposite.notifyListeners(SWT.Resize, new Event());
		hProjectAppscrolledComposite.setAlwaysShowScrollBars(true);

		// create the vertical page's content
		final ScrolledComposite verticalScrolledComposite = new ScrolledComposite(contentPanel, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		verticalScrolledComposite.setLayout(new GridLayout());
		verticalScrolledComposite.setExpandHorizontal(true);
		verticalScrolledComposite.setExpandVertical(true);

		Composite vMainComposite = new Composite(verticalScrolledComposite, SWT.NONE);
		vMainComposite.setLayout(new GridLayout(1, false));
		vMainComposite.setLayoutData(horiCompLayoutData);
		
		final Label vLblUserApplications = new Label(vMainComposite, SWT.BOLD);
		vLblUserApplications.setText("User Applications");
		GridDataFactory.fillDefaults().grab(true, false).indent(2, 0).align(SWT.CENTER, SWT.CENTER)
				.applyTo(vLblUserApplications);
		
		this.vUserComposite = new Composite(vMainComposite, SWT.NONE);
		vUserComposite.setLayout(new GridLayout(1, false));
		vUserComposite.setLayoutData(horiCompLayoutData);
		
		for(int i = 0 ; i <= 5 ; i++){
			Button button = new Button(vUserComposite, SWT.NONE);
			button.setText("Application " +i);
		}
		
		final Label vLblProjectApplications = new Label(vMainComposite, SWT.BOLD);
		vLblProjectApplications.setText("Project Applications");
		GridDataFactory.fillDefaults().grab(true, false).indent(2, 0).align(SWT.CENTER, SWT.CENTER)
				.applyTo(vLblProjectApplications);

		this.vProjectComp = new Composite(vMainComposite, SWT.NONE);
		vProjectComp.setLayout(new GridLayout(1, false));
		vProjectComp.setLayoutData(horiCompLayoutData);
		
		for(int i = 0 ; i <= 5 ; i++){
			Button button = new Button(vProjectComp, SWT.NONE);
			button.setText("Application " +i);
		}

		// Initially pass null so that it loads the project from environment variable
		
		verticalScrolledComposite.setContent(vMainComposite);
		verticalScrolledComposite.setMinSize(vMainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		layout.topControl = horizontalComposite;
		
		this.toolItem.addListener(SWT.Selection, event -> {
			if (((ToolItem) event.widget).getToolTipText().equals("Horizontal")) {
				layout.topControl = verticalScrolledComposite;
				contentPanel.layout();
				//this.toolItem.setImage(new Image(Display.getDefault(), "./icons/horizontal.png"));
				this.toolItem.setText("Horizontal");
			} else {
				layout.topControl = horizontalComposite;
				contentPanel.layout();
				//this.toolItem.setImage(new Image(Display.getDefault(), "./icons/horizontal.png"));
				this.toolItem.setText("Vertical");
			}
		});
	}
	

}
