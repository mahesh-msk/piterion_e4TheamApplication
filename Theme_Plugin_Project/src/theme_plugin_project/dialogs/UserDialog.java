package theme_plugin_project.dialogs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.export.command.ExportCommand;
import org.eclipse.nebula.widgets.pgroup.PGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;

import theme_plugin_project.dialogs.nattables.UserHistoryTblNattable;

public class UserDialog extends Dialog {

	private Composite page1;
	private Group group;
	private Composite page2;
	private IEclipsePreferences prefs;
	private NatTable userStatusNatTable;
	private NatTable userHistoryNatTable;
	private UserHistoryTblNattable userHistoryTableContainer;
	private UserHistoryTblNattable userStatusTableContainer;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @param prefs
	 */
	public UserDialog(Shell parentShell, IEclipsePreferences prefs) {
		super(parentShell);
		this.prefs = prefs;
		setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.MODELESS | SWT.ON_TOP | SWT.SHELL_TRIM | SWT.RESIZE);

	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("User Dialog");
		super.configureShell(newShell);

	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(GridLayoutFactory.fillDefaults().create());

		final ScrolledComposite sc = new ScrolledComposite(container, SWT.BORDER | SWT.V_SCROLL|SWT.H_SCROLL);
		sc.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 200).create());
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setMinSize(300, 300);
		sc.addListener(SWT.Resize,new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				int height=sc.getClientArea().height;
				sc.setMinSize(container.computeSize(height, SWT.DEFAULT));
				
			}
		});
		

		final Composite containerMain = new Composite(sc, SWT.NULL);
		containerMain.setLayout(GridLayoutFactory.swtDefaults().numColumns(3).create());

		final StackLayout layout = new StackLayout();

		Button btnUserHistory = new Button(containerMain, SWT.RADIO);
		btnUserHistory.setText("User History");
		btnUserHistory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				layout.topControl = page1;
				group.layout();

			}
		});

		Button btnUserStatus = new Button(containerMain, SWT.RADIO);
		btnUserStatus.setText("User Status");
		btnUserStatus.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				layout.topControl = page2;
				group.layout();
			}
		});

		Button btnExcelExport = new Button(containerMain, SWT.PUSH);
		btnExcelExport.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnExcelExport.setText("Export");
		btnExcelExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (layout.topControl == page1) {
					userHistoryNatTable.doCommand(
							new ExportCommand(userHistoryNatTable.getConfigRegistry(), userHistoryNatTable.getShell()));
				} else {
					userStatusNatTable.doCommand(
							new ExportCommand(userStatusNatTable.getConfigRegistry(), userStatusNatTable.getShell()));
				}
			}
		});

		GridData gd_group = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);

		group = new Group(containerMain, SWT.NONE);

		group.setLayoutData(gd_group);

		group.setLayout(layout);

		generateUserHistoryTable();

		generateUserStatusTable();
		layout.topControl = page2;
		group.layout();

		sc.setContent(containerMain);
		sc.setMinSize(containerMain.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		return container;

	}

	private void createUserHistoryFilterComposite(Composite parent) {

		PGroup composite = new PGroup(parent, SWT.SMOOTH);

		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		composite.setLayout(new GridLayout(6, false));
		composite.setText("User History Filter");

		Label lblLogTime = new Label(composite, SWT.NONE);
		lblLogTime.setText("Log Time");
		new Label(composite, SWT.NONE);

		Label lblCdate = new Label(composite, SWT.NONE);
		lblCdate.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		lblCdate.setText("Start Date");
		CDateTime startDate = new CDateTime(composite, CDT.DROP_DOWN | CDT.TIME_MEDIUM | CDT.DATE_LONG | CDT.DATE_LONG);
		startDate.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));
		String stDate = prefs.get("userHistory.startDate", "");
		if (!stDate.isEmpty()) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			try {
				startDate.setSelection(simpleDateFormat.parse(stDate));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		Label lblEndDate = new Label(composite, SWT.NONE);
		lblEndDate.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		lblEndDate.setText("End Date");
		CDateTime endDate = new CDateTime(composite, CDT.DROP_DOWN | CDT.TIME_MEDIUM | CDT.DATE_LONG | CDT.DATE_LONG);
		endDate.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));
		String eDate = prefs.get("userHistory.endDate", "");
		if (!eDate.isEmpty()) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			try {
				endDate.setSelection(simpleDateFormat.parse(eDate));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		Label lblUserName = new Label(composite, SWT.NONE);
		lblUserName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUserName.setText("User Name");

		Combo cmbUser = new Combo(composite, SWT.NONE);
		cmbUser.setItems(new String[] { "=", "LIKE", "<>" });
		GridData gd_cmbUser = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_cmbUser.widthHint = 34;
		cmbUser.setLayoutData(gd_cmbUser);

		Text txtUsernameHistoryfiltertext = new Text(composite, SWT.BORDER);
		txtUsernameHistoryfiltertext.setText("");
		txtUsernameHistoryfiltertext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		String userName = prefs.get("userHistoryTable.userName", "");
		if (!userName.isEmpty()) {
			String[] values = userName.split(",");
			cmbUser.setText(values[0]);
			txtUsernameHistoryfiltertext.setText(values[1]);

		}

		Label lblHost = new Label(composite, SWT.NONE);
		lblHost.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHost.setText("Host");

		Combo cmbHost = new Combo(composite, SWT.NONE);
		cmbHost.setItems(new String[] { "=", "LIKE", "<>" });
		cmbHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Text txtHostUserHistoryfilter = new Text(composite, SWT.BORDER);
		txtHostUserHistoryfilter.setText("");
		txtHostUserHistoryfilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		String host = prefs.get("userHistoryTable.host", "");
		if (!host.isEmpty()) {
			String[] values = host.split(",");
			cmbHost.setText(values[0]);
			txtHostUserHistoryfilter.setText(values[1]);
		}

		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Site");

		Combo cmbSite = new Combo(composite, SWT.NONE);
		cmbSite.setItems(new String[] { "=", "LIKE", "<>" });
		cmbSite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Text txtSiteUserHistoryfilter = new Text(composite, SWT.BORDER);
		txtSiteUserHistoryfilter.setText("");
		txtSiteUserHistoryfilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		String site = prefs.get("userHistoryTable.site", "");
		if (!site.isEmpty()) {
			String[] values = site.split(",");
			cmbSite.setText(values[0]);
			txtSiteUserHistoryfilter.setText(values[1]);
		}

		Label lblAdminarea = new Label(composite, SWT.NONE);
		lblAdminarea.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAdminarea.setText("AdminArea");

		Combo cmbAdminArea = new Combo(composite, SWT.NONE);
		cmbAdminArea.setItems(new String[] { "=", "LIKE", "<>" });
		cmbAdminArea.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Text txtAdminareafilter = new Text(composite, SWT.BORDER);
		txtAdminareafilter.setText("");
		txtAdminareafilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		String adminArea = prefs.get("userHistoryTable.adminArea", "");

		if (!adminArea.isEmpty()) {
			String[] values = adminArea.split(",");
			cmbAdminArea.setText(values[0]);
			txtAdminareafilter.setText(values[1]);

		}

		Label lblProject = new Label(composite, SWT.NONE);
		lblProject.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProject.setText("Project");

		Combo cmbProject = new Combo(composite, SWT.NONE);
		cmbProject.setItems(new String[] { "=", "LIKE", "<>" });
		cmbProject.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Text txtProjectfilter = new Text(composite, SWT.BORDER);
		txtProjectfilter.setText("");
		txtProjectfilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		String project = prefs.get("userHistoryTable.projet", "");
		if (!project.isEmpty()) {
			String[] values = project.split(",");
			cmbProject.setText(values[0]);
			txtProjectfilter.setText(values[1]);
		}

		Label lblApplication = new Label(composite, SWT.NONE);
		lblApplication.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblApplication.setText("Application");

		Combo cmbApplication = new Combo(composite, SWT.NONE);
		cmbApplication.setItems(new String[] { "=", "LIKE", "<>" });
		cmbApplication.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Text txtApplicationfilter = new Text(composite, SWT.BORDER);
		txtApplicationfilter.setText("");
		txtApplicationfilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		String application = prefs.get("userHistoryTable.application", "");
		if (!application.isEmpty()) {
			String[] values = application.split(",");
			cmbApplication.setText(values[0]);
			txtApplicationfilter.setText(values[1]);

		}

		Label lblResult = new Label(composite, SWT.NONE);
		lblResult.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblResult.setText("Result");

		Combo cmbResult = new Combo(composite, SWT.NONE);
		cmbResult.setItems(new String[] { "=", "<>" });
		cmbResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Combo resultFilter = new Combo(composite, SWT.NONE);
		resultFilter.setItems(new String[] { "SUCCESS", "FAILURE" });
		resultFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));

		String result = prefs.get("userHistoryTable.result", "");
		if (!result.isEmpty()) {
			String[] values = result.split(",");
			cmbResult.setText(values[0]);
			resultFilter.setText(values[1]);
		}

		Label lblLimit = new Label(composite, SWT.NONE);
		lblLimit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLimit.setText("Limit");

		Text txtUserHistoryLimitfilter = new Text(composite, SWT.BORDER);
		String limit = prefs.get("userHistoryTable.limit", "1000");
		txtUserHistoryLimitfilter.setText(limit);
		txtUserHistoryLimitfilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		txtUserHistoryLimitfilter.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {

				String string = e.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('a' <= chars[i] && chars[i] <= 'z')) {
						e.doit = true;

					} else {
						e.doit = false;
					}

				}

			}
		});

		Composite buttonBarComposite = new Composite(composite, SWT.NONE);
		buttonBarComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));
		buttonBarComposite.setLayout(new GridLayout(2, false));

		Button defaultButton = new Button(buttonBarComposite, SWT.PUSH);
		defaultButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		defaultButton.setText("Default");
		defaultButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				prefs.put("userHistoryTable.userName", "");
				cmbUser.setText("");
				txtUsernameHistoryfiltertext.setText("");
				prefs.put("userHistoryTable.host", "");
				cmbHost.setText("");
				txtHostUserHistoryfilter.setText("");
				prefs.put("userHistoryTable.site", "");
				cmbSite.setText("");
				txtSiteUserHistoryfilter.setText("");
				prefs.put("userHistoryTable.adminArea", "");
				cmbAdminArea.setText("");
				txtAdminareafilter.setText("");
				prefs.put("userHistoryTable.projet", "");
				cmbProject.setText("");
				txtProjectfilter.setText("");
				prefs.put("userHistoryTable.application", "");
				cmbApplication.setText("");
				txtApplicationfilter.setText("");
				prefs.put("userHistoryTable.result", "");
				cmbResult.setText("");
				resultFilter.setText("");
				prefs.put("userHistoryTable.limit", "1000");
				txtUserHistoryLimitfilter.setText("1000");

			}
		});

		Button applyButton = new Button(buttonBarComposite, SWT.PUSH);
		applyButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		applyButton.setText("Apply");
		applyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				StringBuilder query = new StringBuilder();
				query.append("select * from  USER_HISTORY_TBL where ");

				ArrayList<String> conditions = new ArrayList<String>();
				String stDateString = null;
				if (startDate.getSelection() != null) {
					Date stDate = startDate.getSelection();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
					stDateString = simpleDateFormat.format(stDate);
					prefs.put("userHistory.startDate", stDateString);
				}
				String endDateString = null;
				if (endDate.getSelection() != null) {
					Date eDate = endDate.getSelection();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
					endDateString = simpleDateFormat.format(eDate);
					prefs.put("userHistory.endDate", endDateString);
				}
				if (stDateString != null && endDateString != null) {
					String condition = "LOG_TIME > " + stDateString + " AND LOG_TIME < " + endDateString;
					conditions.add(condition);
				} else if (stDateString != null) {
					String condition = "LOG_TIME > " + stDateString;
					conditions.add(condition);
				} else if (endDateString != null) {
					String condition = "LOG_TIME < " + endDateString;
					conditions.add(condition);
				}

				String userRule = cmbUser.getText();
				String userFilterText = txtUsernameHistoryfiltertext.getText();
				if (!userFilterText.isEmpty()) {
					String condition = "USER_NAME " + userRule + " '" + userFilterText + "'";
					conditions.add(condition);
					prefs.put("userHistoryTable.userName", userRule + "," + userFilterText);

				}
				String hostRule = cmbHost.getText();
				String hostFilter = txtHostUserHistoryfilter.getText();
				if (!hostFilter.isEmpty()) {
					String condition = "HOST " + hostRule + " '" + hostFilter + "'";
					conditions.add(condition);
					prefs.put("userHistoryTable.host", hostRule + "," + hostFilter);
				}
				String siteRule = cmbSite.getText();
				String siteFilter = txtSiteUserHistoryfilter.getText();
				if (!siteFilter.isEmpty()) {
					String condition = "SITE " + siteRule + " '" + siteFilter + "'";
					conditions.add(condition);
					prefs.put("userHistoryTable.site", siteRule + "," + siteFilter);
				}
				String adminRule = cmbAdminArea.getText();
				String adminFilter = txtAdminareafilter.getText();
				if (!adminFilter.isEmpty()) {
					String condition = "ADMIN_AREA " + adminRule + " '" + adminFilter + "'";
					conditions.add(condition);
					prefs.put("userHistoryTable.adminArea", adminRule + "," + adminFilter);
				}
				String projectRule = cmbProject.getText();
				String projectFilter = txtProjectfilter.getText();
				if (!projectFilter.isEmpty()) {
					String condition = "PROJECT " + projectRule + " '" + projectFilter + "'";
					conditions.add(condition);
					prefs.put("userHistoryTable.projet", projectRule + "," + projectFilter);
				}

				String applicationRule = cmbApplication.getText();
				String applicationFilter = txtApplicationfilter.getText();
				if (!applicationFilter.isEmpty()) {
					String condition = "APPLICATION " + applicationRule + " '" + applicationFilter + "'";
					conditions.add(condition);
					prefs.put("userHistoryTable.application", applicationRule + "," + applicationFilter);
				}
				String resultRule = cmbResult.getText();
				String resultFltr = resultFilter.getText();
				if (!resultFltr.isEmpty()) {
					String condition = "RESULT " + resultRule + " '" + resultFltr + "'";
					conditions.add(condition);
					prefs.put("userHistoryTable.result", resultRule + "," + resultFltr);
				}

				conditions.add("IS_USER_STATUS = FALSE");

				for (String condition : conditions) {
					if (conditions.indexOf(condition) == 0) {
						query.append(condition);
					} else {
						query.append(" AND " + condition);
					}
				}
				String limitString = txtUserHistoryLimitfilter.getText();

				if (!limitString.isEmpty()) {
					Integer queryLimit = Integer.parseInt(limitString);
					if (conditions.size() == 0) {
						query.append("ROWNUM<= " + queryLimit);
					} else {
						query.append(" AND ROWNUM<= " + queryLimit);
					}
					prefs.put("userHistoryTable.limit", "1000");
				}
				System.out.println(query);
				userHistoryTableContainer.updateNatTable(query.toString());
			}
		});
		try {
			prefs.flush();
		} catch (BackingStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		composite.setExpanded(false);

	}

	private void createUserStatusFilterComposite(Composite parent) {

		PGroup composite = new PGroup(parent, SWT.SMOOTH);

		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		composite.setLayout(new GridLayout(6, false));
		composite.setText("User Status Filter");

		Label lblLogTime = new Label(composite, SWT.NONE);
		lblLogTime.setText("Log Time");
		new Label(composite, SWT.NONE);

		Label lblCdate = new Label(composite, SWT.NONE);
		lblCdate.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		lblCdate.setText("Start Date");
		CDateTime startDate = new CDateTime(composite, CDT.DROP_DOWN | CDT.TIME_MEDIUM | CDT.DATE_LONG | CDT.DATE_LONG);
		startDate.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));
		String stDate = prefs.get("userStatusTable.startDate", "");
		if (!stDate.isEmpty()) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			try {
				startDate.setSelection(simpleDateFormat.parse(stDate));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		Label lblEndDate = new Label(composite, SWT.NONE);
		lblEndDate.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		lblEndDate.setText("End Date");
		CDateTime endDate = new CDateTime(composite, CDT.DROP_DOWN | CDT.TIME_MEDIUM | CDT.DATE_LONG | CDT.DATE_LONG);
		endDate.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));
		String eDate = prefs.get("userStatusTable.endDate", "");
		if (!eDate.isEmpty()) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			try {
				endDate.setSelection(simpleDateFormat.parse(eDate));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		Label lblUserName = new Label(composite, SWT.NONE);
		lblUserName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUserName.setText("User Name");

		Combo cmbUser = new Combo(composite, SWT.NONE);
		cmbUser.setItems(new String[] { "=", "LIKE", "<>" });
		GridData gd_cmbUser = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_cmbUser.widthHint = 34;
		cmbUser.setLayoutData(gd_cmbUser);

		Text txtUsernameHistoryfiltertext = new Text(composite, SWT.BORDER);
		txtUsernameHistoryfiltertext.setText("");
		txtUsernameHistoryfiltertext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		String userName = prefs.get("userStatusTable.userName", "");
		if (!userName.isEmpty()) {
			String[] values = userName.split(",");
			cmbUser.setText(values[0]);
			txtUsernameHistoryfiltertext.setText(values[1]);

		}

		Label lblHost = new Label(composite, SWT.NONE);
		lblHost.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHost.setText("Host");

		Combo cmbHost = new Combo(composite, SWT.NONE);
		cmbHost.setItems(new String[] { "=", "LIKE", "<>" });
		cmbHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Text txtHostUserHistoryfilter = new Text(composite, SWT.BORDER);
		txtHostUserHistoryfilter.setText("");
		txtHostUserHistoryfilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		String host = prefs.get("userStatusTable.host", "");
		if (!host.isEmpty()) {
			String[] values = host.split(",");
			cmbHost.setText(values[0]);
			txtHostUserHistoryfilter.setText(values[1]);
		}

		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Site");

		Combo cmbSite = new Combo(composite, SWT.NONE);
		cmbSite.setItems(new String[] { "=", "LIKE", "<>" });
		cmbSite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Text txtSiteUserHistoryfilter = new Text(composite, SWT.BORDER);
		txtSiteUserHistoryfilter.setText("");
		txtSiteUserHistoryfilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		String site = prefs.get("userStatusTable.site", "");
		if (!site.isEmpty()) {
			String[] values = site.split(",");
			cmbSite.setText(values[0]);
			txtSiteUserHistoryfilter.setText(values[1]);
		}

		Label lblAdminarea = new Label(composite, SWT.NONE);
		lblAdminarea.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAdminarea.setText("AdminArea");

		Combo cmbAdminArea = new Combo(composite, SWT.NONE);
		cmbAdminArea.setItems(new String[] { "=", "LIKE", "<>" });
		cmbAdminArea.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Text txtAdminareafilter = new Text(composite, SWT.BORDER);
		txtAdminareafilter.setText("");
		txtAdminareafilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		String adminArea = prefs.get("userStatusTable.adminArea", "");

		if (!adminArea.isEmpty()) {
			String[] values = adminArea.split(",");
			cmbAdminArea.setText(values[0]);
			txtAdminareafilter.setText(values[1]);

		}

		Label lblProject = new Label(composite, SWT.NONE);
		lblProject.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProject.setText("Project");

		Combo cmbProject = new Combo(composite, SWT.NONE);
		cmbProject.setItems(new String[] { "=", "LIKE", "<>" });
		cmbProject.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Text txtProjectfilter = new Text(composite, SWT.BORDER);
		txtProjectfilter.setText("");
		txtProjectfilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		String project = prefs.get("userStatusTable.projet", "");
		if (!project.isEmpty()) {
			String[] values = project.split(",");
			cmbProject.setText(values[0]);
			txtProjectfilter.setText(values[1]);
		}

		Label lblApplication = new Label(composite, SWT.NONE);
		lblApplication.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblApplication.setText("Application");

		Combo cmbApplication = new Combo(composite, SWT.NONE);
		cmbApplication.setItems(new String[] { "=", "LIKE", "<>" });
		cmbApplication.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Text txtApplicationfilter = new Text(composite, SWT.BORDER);
		txtApplicationfilter.setText("");
		txtApplicationfilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		String application = prefs.get("userStatusTable.application", "");
		if (!application.isEmpty()) {
			String[] values = application.split(",");
			cmbApplication.setText(values[0]);
			txtApplicationfilter.setText(values[1]);

		}

		Label lblResult = new Label(composite, SWT.NONE);
		lblResult.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblResult.setText("Result");

		Combo cmbResult = new Combo(composite, SWT.NONE);
		cmbResult.setItems(new String[] { "=", "<>" });
		cmbResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Combo resultFilter = new Combo(composite, SWT.NONE);
		resultFilter.setItems(new String[] { "SUCCESS", "FAILURE" });
		resultFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));

		String result = prefs.get("userStatusTable.result", "");
		if (!result.isEmpty()) {
			String[] values = result.split(",");
			cmbResult.setText(values[0]);
			resultFilter.setText(values[1]);
		}

		Label lblLimit = new Label(composite, SWT.NONE);
		lblLimit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLimit.setText("Limit");

		Text txtLimitfilter = new Text(composite, SWT.BORDER);

		String limit = prefs.get("usetStatusTable.limit", "1000");

		txtLimitfilter.setText(limit);

		txtLimitfilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		txtLimitfilter.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {

				String string = e.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('a' <= chars[i] && chars[i] <= 'z')) {
						e.doit = true;

					} else {
						e.doit = false;
					}

				}

			}
		});

		Composite buttonBarComposite = new Composite(composite, SWT.NONE);
		buttonBarComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));
		buttonBarComposite.setLayout(new GridLayout(2, false));

		Button defaultButton = new Button(buttonBarComposite, SWT.PUSH);
		defaultButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		defaultButton.setText("Default");
		defaultButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				prefs.put("userStatusTable.userName", "");
				cmbUser.setText("");
				txtUsernameHistoryfiltertext.setText("");
				prefs.put("userStatusTable.host", "");
				cmbHost.setText("");
				txtHostUserHistoryfilter.setText("");
				prefs.put("userStatusTable.site", "");
				cmbSite.setText("");
				txtSiteUserHistoryfilter.setText("");
				prefs.put("userStatusTable.adminArea", "");
				cmbAdminArea.setText("");
				txtAdminareafilter.setText("");
				prefs.put("userStatusTable.projet", "");
				cmbProject.setText("");
				txtProjectfilter.setText("");
				prefs.put("userStatusTable.application", "");
				cmbApplication.setText("");
				txtApplicationfilter.setText("");
				prefs.put("userStatusTable.result", "");
				cmbResult.setText("");
				resultFilter.setText("");
				prefs.put("userStatusTable.limit", "1000");
				txtLimitfilter.setText("1000");

			}
		});

		Button applyButton = new Button(buttonBarComposite, SWT.PUSH);
		applyButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		applyButton.setText("Apply");
		applyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				StringBuilder query = new StringBuilder();
				query.append("select * from  USER_HISTORY_TBL where ");

				ArrayList<String> conditions = new ArrayList<String>();
				String stDateString = null;
				if (startDate.getSelection() != null) {
					Date stDate = startDate.getSelection();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
					stDateString = simpleDateFormat.format(stDate);
					prefs.put("userStatusTable.startDate", stDateString);
				}
				String endDateString = null;
				if (endDate.getSelection() != null) {
					Date stDate = endDate.getSelection();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
					endDateString = simpleDateFormat.format(stDate);
					prefs.put("userStatusTable.endDate", endDateString);
				}
				if (stDateString != null && endDateString != null) {
					String condition = "LOG_TIME > " + stDateString + " AND LOG_TIME < " + endDateString;
					conditions.add(condition);
				} else if (stDateString != null) {
					String condition = "LOG_TIME > " + stDateString;
					conditions.add(condition);
				} else if (endDateString != null) {
					String condition = "LOG_TIME < " + endDateString;
					conditions.add(condition);
				}

				String userRule = cmbUser.getText();
				String userFilterText = txtUsernameHistoryfiltertext.getText();
				if (!userFilterText.isEmpty()) {
					String condition = "USER_NAME " + userRule + " '" + userFilterText + "'";
					conditions.add(condition);
					prefs.put("userStatusTable.userName", userRule + "," + userFilterText);
				}
				String hostRule = cmbHost.getText();
				String hostFilter = txtHostUserHistoryfilter.getText();
				if (!hostFilter.isEmpty()) {
					String condition = "HOST " + hostRule + " '" + hostFilter + "'";
					conditions.add(condition);
					prefs.put("userStatusTable.host", hostRule + "," + hostFilter);
				}
				String siteRule = cmbSite.getText();
				String siteFilter = txtSiteUserHistoryfilter.getText();
				if (!siteFilter.isEmpty()) {
					String condition = "SITE " + siteRule + " '" + siteFilter + "'";
					conditions.add(condition);
					prefs.put("userStatusTable.site", siteRule + "," + siteFilter);
				}
				String adminRule = cmbAdminArea.getText();
				String adminFilter = txtAdminareafilter.getText();
				if (!adminFilter.isEmpty()) {
					String condition = "ADMIN_AREA " + adminRule + " '" + adminFilter + "'";
					conditions.add(condition);
					prefs.put("userStatusTable.adminArea", adminRule + "," + adminFilter);
				}
				String projectRule = cmbProject.getText();
				String projectFilter = txtProjectfilter.getText();
				if (!projectFilter.isEmpty()) {
					String condition = "PROJECT " + projectRule + " '" + projectFilter + "'";
					conditions.add(condition);
					prefs.put("userStatusTable.projet", projectRule + "," + projectFilter);
				}

				String applicationRule = cmbApplication.getText();
				String applicationFilter = txtApplicationfilter.getText();
				if (!applicationFilter.isEmpty()) {
					String condition = "APPLICATION " + applicationRule + " '" + applicationFilter + "'";
					conditions.add(condition);
					prefs.put("userStatusTable.application", applicationRule + "," + applicationFilter);
				}
				String resultRule = cmbResult.getText();
				String resultFltr = resultFilter.getText();
				if (!resultFltr.isEmpty()) {
					String condition = "RESULT " + resultRule + " '" + resultFltr + "'";
					conditions.add(condition);
					prefs.put("userStatusTable.result", resultRule + "," + resultFltr);
				}

				conditions.add("IS_USER_STATUS = TRUE");

				for (String condition : conditions) {
					if (conditions.indexOf(condition) == 0) {
						query.append(condition);
					} else {
						query.append(" AND " + condition);
					}
				}
				String limitString = txtLimitfilter.getText();

				if (!limitString.isEmpty()) {
					Integer queryLimit = Integer.parseInt(limitString);
					if (conditions.size() == 0) {
						query.append("ROWNUM<= " + queryLimit);
					} else {
						query.append("AND ROWNUM<= " + queryLimit);
					}
					prefs.put("userStatusTable.limit", "");
				}
				System.out.println(query);

				userStatusTableContainer.updateNatTable(query.toString());

			}
		});
		try {
			prefs.flush();
		} catch (BackingStoreException e1) {
			e1.printStackTrace();
		}

		composite.setExpanded(false);

	}

	private void generateUserStatusTable() {
		page2 = new Composite(group, SWT.NONE);
		createUserStatusFilterComposite(page2);
		page2.setLayout(new GridLayout(1, false));

		userStatusTableContainer = new UserHistoryTblNattable();
		userStatusNatTable = userStatusTableContainer.createUserHistoryNatTable(page2, "True");
	}

	@SuppressWarnings("unchecked")
	private void generateUserHistoryTable() {
		page1 = new Composite(group, SWT.NONE);
		createUserHistoryFilterComposite(page1);
		page1.setLayout(new GridLayout(1, false));

		userHistoryTableContainer = new UserHistoryTblNattable();
		userHistoryNatTable = userHistoryTableContainer.createUserHistoryNatTable(page1, "False");

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
	protected boolean isResizable() {
		return true;

	}

	@Override
	protected Point getInitialSize() {
		return new Point(800, 500);
	}

}
