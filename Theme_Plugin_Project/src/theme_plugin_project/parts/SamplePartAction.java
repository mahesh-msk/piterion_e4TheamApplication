package theme_plugin_project.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class SamplePartAction extends SamplePartUI{

	/** The eclipse context. */
	@Inject
	private IEclipseContext eclipseContext;
	
	/**
	 * Instantiates a new xm menu part action.
	 *
	 * @param parent the parent
	 */
	@Inject
	public SamplePartAction(final Composite parent) {
		super(parent, SWT.NONE);
	}

	/**
	 * Create UI.
	 */
	@PostConstruct
	private void createUI() {
		super.createUI(this.eclipseContext);
		initWidgets();
		initListeners();
	}

	/**
	 * Init widgets.
	 */
	private void initWidgets() {
		setProjectCombo();
	}
	
	/**
	 * Set project combo.
	 *
	 * @param projectsList the projects list
	 */
	private void setProjectCombo() {
		Table table = this.cbProjectList.getTable();
		for (int i= 0 ; i <= 5 ; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			String text = "Project" + i;
			//Image image = new Image(Display.getDefault(), "icons/rightarrow2.png");
			item.setText(text);
			//item.setImage(image);
		}
	}

	/**
	 * Init listeners.
	 */
	private void initListeners() {
		this.cbProjectList.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent event) {
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
	}

}
