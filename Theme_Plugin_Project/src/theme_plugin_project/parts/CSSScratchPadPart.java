package theme_plugin_project.parts;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import theme_plugin_project.customwidget.UserProfileWidget;

@SuppressWarnings("restriction")
public class CSSScratchPadPart {
	@Inject
	@Optional
	private IThemeEngine themeEngine;

	/*
	 * public CSSScratchPadPart(Shell parentShell, IThemeEngine themeEngine) {
	 * super(parentShell); this.themeEngine = themeEngine;
	 * setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE/* | SWT.PRIMARY_MODAL
	 */
	// );
	// setShellStyle(SWT.DIALOG_TRIM | SWT.MAX | SWT.RESIZE
	// | getDefaultOrientation());
	// }

	/*
	 * @Override protected void configureShell(Shell newShell) {
	 * super.configureShell(newShell); newShell.setText("CSS Scratchpad"); }
	 */

	private static final int APPLY_ID = IDialogConstants.OK_ID + 100;
	/**
	 * Collection of buttons created by the <code>createButton</code> method.
	 */
	private HashMap<Integer, Button> buttons = new HashMap<>();

	private Text cssText;
	private Text exceptions;
	private Text text;

	@PostConstruct
	protected Control createDialogArea(Composite parent) {

		Composite outer = parent;
		outer.setLayout(new FillLayout());

		UserProfileWidget widget = new UserProfileWidget(parent, SWT.NONE);
		return outer;
	}

	

}