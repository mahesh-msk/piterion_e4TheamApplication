/*
 * FILE:            AdminDialog.java
 *
 * SW-COMPONENT:    Theme_Plugin_Project
 *
 * DESCRIPTION:     -
 *
 * COPYRIGHT:       © 2015 - 2022 Robert Bosch GmbH
 *
 * The reproduction, distribution and utilization of this file as
 * well as the communication of its contents to others without express
 * authorization is prohibited. Offenders will be held liable for the
 * payment of damages. All rights reserved in the event of the grant
 * of a patent, utility model or design.
 */
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;

import theme_plugin_project.dialogs.nattables.AdminHistoryBaseObjectNattable;
import theme_plugin_project.dialogs.nattables.AdminHistoryRelationsNattable;

public class AdminDialog extends Dialog {

	private Composite page1;
	private Group group;
	private Composite page2;

	private Text txtAdminNamefiltertext;
	private Text txtObjectNamefiltertext;
	private Text txtLimitfilter;
	private Text txtAdmimAreafiltertext;
	private Text txtProjectfiltertext;
	private Text txtProjectApplicationfiltertext;
	private Text txtUserNamefiltertext;
	private Text txtStartApplicationfiltertext;
	private Text txtSitefiltertext;
	private Text txtGroupNamefiltertext;
	private Text txtUserApplicationfiltertext;
	private Text txtDirectoryfiltertext;
	private Text txtRolefiltertext;
	private IEclipsePreferences prefs;
	private NatTable adminHistoryRelationsNatTable;
	private NatTable adminHistoryBaseObjectNatTable;
	private Text txtAdminNameBaseObjectfiltertext;
	private AdminHistoryRelationsNattable adminHistoryRelationshipNatTableContainer;
	private AdminHistoryBaseObjectNattable adminHistoryBaseObjectContainer;
	private Text txtBaseObjectLimitfilter;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @param prefs
	 */
	public AdminDialog(Shell parentShell, IEclipsePreferences prefs) {
		super(parentShell);
		this.prefs = prefs;
		setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.MODELESS | SWT.ON_TOP | SWT.SHELL_TRIM | SWT.RESIZE);

	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("Admin Dialog");
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

		final ScrolledComposite sc = new ScrolledComposite(container, SWT.BORDER | SWT.V_SCROLL);
		sc.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 200).create());
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);

		final Composite containerMain = new Composite(sc, SWT.NULL);
		containerMain.setLayout(GridLayoutFactory.swtDefaults().numColumns(3).create());

		final StackLayout layout = new StackLayout();

		Button btnRelationHistory = new Button(containerMain, SWT.RADIO);
		btnRelationHistory.setText("Relations History");
		btnRelationHistory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				layout.topControl = page1;
				group.layout();

			}
		});

		Button btnBaseDataHistory = new Button(containerMain, SWT.RADIO);
		btnBaseDataHistory.setText("Base Data History");
		btnBaseDataHistory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				layout.topControl = page2;
				group.layout();
			}
		});

		Button excelExportButton = new Button(containerMain, SWT.PUSH);
		excelExportButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		excelExportButton.setText("Export");
		excelExportButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (layout.topControl == page1) {
					adminHistoryRelationsNatTable
							.doCommand(new ExportCommand(adminHistoryRelationsNatTable.getConfigRegistry(),
									adminHistoryRelationsNatTable.getShell()));
				} else {
					adminHistoryBaseObjectNatTable
							.doCommand(new ExportCommand(adminHistoryBaseObjectNatTable.getConfigRegistry(),
									adminHistoryBaseObjectNatTable.getShell()));
				}
			}
		});

		GridData gd_group = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);

		group = new Group(containerMain, SWT.NONE);
		group.setLayoutData(gd_group);

		group.setLayout(layout);

		generateAdminRelationTable();

		generateAdminBaseObjectTable();

		sc.setContent(containerMain);
		sc.setMinSize(containerMain.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		return container;

	}

	private void createFilterComposite1(Composite parent) {
		Group composite = new Group(parent, SWT.SMOOTH);
//		composite.setExpanded(false);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		composite.setLayout(new GridLayout(6, false));
		composite.setText("AdminHistoryFilter Filter");

		// LogTime
		Label lblLogTime = new Label(composite, SWT.NONE);
		lblLogTime.setText("Log Time");

		new Label(composite, SWT.NONE);

		Label lblCdate = new Label(composite, SWT.NONE);
		lblCdate.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		lblCdate.setText("Start Date");
		CDateTime startDate = new CDateTime(composite, CDT.BORDER | CDT.DROP_DOWN | CDT.DATE_LONG | CDT.TIME_MEDIUM);
//		startDate.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));
		String stDate = prefs.get("adminHistory.startDate", "");

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

		CDateTime endDate = new CDateTime(composite, CDT.BORDER | CDT.TIME_MEDIUM | CDT.DROP_DOWN | CDT.DATE_LONG);
//		endDate.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));
		String eDate = prefs.get("adminHistory.endDate", "");

		if (!eDate.isEmpty()) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			try {
				endDate.setSelection(simpleDateFormat.parse(eDate));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		Label lblAdminName = new Label(composite, SWT.NONE);
		lblAdminName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		lblAdminName.setText("Admin Name");
		Combo cmbAdminNameHistorytbl = new Combo(composite, SWT.NONE);
		cmbAdminNameHistorytbl.setItems("=", "LIKE", "<>");
		GridData gd_cmbUser = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_cmbUser.widthHint = 34;
		cmbAdminNameHistorytbl.setLayoutData(gd_cmbUser);

		txtAdminNamefiltertext = new Text(composite, SWT.BORDER);
		txtAdminNamefiltertext.setText("");
		txtAdminNamefiltertext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		String adminNameValue = prefs.get("adminHistory.adminName", "");
		if (!adminNameValue.isEmpty()) {
			String[] value = adminNameValue.split(",");
			cmbAdminNameHistorytbl.setText(value[0]);
			txtAdminNamefiltertext.setText(value[1]);

		}
		// operation
		Label lblOperation = new Label(composite, SWT.NONE);
		lblOperation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOperation.setText("Operation");

		Combo cmbOperation = new Combo(composite, SWT.NONE);
		cmbOperation.setItems("=", "LIKE", "<>");
		cmbOperation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Combo operationFilter = new Combo(composite, SWT.NONE);
		operationFilter.setItems("OperationDone", "NotDone");
		operationFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));

		String operaionValue = prefs.get("adminHistory.operation", "");
		if (!operaionValue.isEmpty()) {
			String[] value = operaionValue.split(",");
			cmbOperation.setText(value[0]);
			operationFilter.setText(value[1]);

		}

		// Adminarea
		Label lblAdinArea = new Label(composite, SWT.NONE);
		lblAdinArea.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAdinArea.setText("AdminArea");

		Combo cmbAdminArea = new Combo(composite, SWT.NONE);
		cmbAdminArea.setItems("=", "LIKE", "<>");
		cmbAdminArea.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		txtAdmimAreafiltertext = new Text(composite, SWT.BORDER);
		txtAdmimAreafiltertext.setText("");
		txtAdmimAreafiltertext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		String adminAreaValue = prefs.get("adminHistory.adminArea", "");
		if (!adminAreaValue.isEmpty()) {
			String[] value = adminAreaValue.split(",");
			cmbAdminArea.setText(value[0]);
			txtAdmimAreafiltertext.setText(value[1]);

		}

		// Project
		Label lblProject = new Label(composite, SWT.NONE);
		lblProject.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProject.setText("Project");

		Combo cmbProject = new Combo(composite, SWT.NONE);
		cmbProject.setItems("=", "LIKE", "<>");
		cmbProject.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		txtProjectfiltertext = new Text(composite, SWT.BORDER);
		txtProjectfiltertext.setText("");
		txtProjectfiltertext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		String projectValue = prefs.get("adminHistory.project", "");
		if (!projectValue.isEmpty()) {
			String[] value = projectValue.split(",");
			cmbProject.setText(value[0]);
			txtProjectfiltertext.setText(value[1]);

		}

		// ProjectApplication

		Label lblProjectApplication = new Label(composite, SWT.NONE);
		lblProjectApplication.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProjectApplication.setText("ProjectApplication");

		Combo cmbProjectApplication = new Combo(composite, SWT.NONE);
		cmbProjectApplication.setItems("=", "LIKE", "<>");
		cmbProjectApplication.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		txtProjectApplicationfiltertext = new Text(composite, SWT.BORDER);
		txtProjectApplicationfiltertext.setText("");
		txtProjectApplicationfiltertext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		String projectApplicationValue = prefs.get("adminHistory.projectApplication", "");
		if (!projectApplicationValue.isEmpty()) {
			String[] value = projectApplicationValue.split(",");
			cmbProjectApplication.setText(value[0]);
			txtProjectApplicationfiltertext.setText(value[1]);
		}
		// UserName

		Label lblUserName = new Label(composite, SWT.NONE);
		lblUserName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUserName.setText("UserName");

		Combo cmbUserName = new Combo(composite, SWT.NONE);
		cmbUserName.setItems("=", "LIKE", "<>");
		cmbUserName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		txtUserNamefiltertext = new Text(composite, SWT.BORDER);
		txtUserNamefiltertext.setText("");
		txtUserNamefiltertext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		String userNameValue = prefs.get("adminHistory.userName", "");
		if (!userNameValue.isEmpty()) {
			String[] value = userNameValue.split(",");
			cmbUserName.setText(value[0]);
			txtUserNamefiltertext.setText(value[1]);
		}

		// StartApplication

		Label lblStartApplication = new Label(composite, SWT.NONE);
		lblStartApplication.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStartApplication.setText("StartApplication");

		Combo cmbStartApplication = new Combo(composite, SWT.NONE);
		cmbStartApplication.setItems("=", "LIKE", "<>");
		cmbStartApplication.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		txtStartApplicationfiltertext = new Text(composite, SWT.BORDER);
		txtStartApplicationfiltertext.setText("");
		txtStartApplicationfiltertext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		String startApplication = prefs.get("adminHistory.startApplication", "");
		if (!startApplication.isEmpty()) {
			String[] values = startApplication.split(",");
			cmbStartApplication.setText(values[0]);
			txtStartApplicationfiltertext.setText(values[1]);

		}

		// RelationType

		Label lblRelationType = new Label(composite, SWT.NONE);
		lblRelationType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRelationType.setText("RelationType");

		Combo cmbRelationType = new Combo(composite, SWT.NONE);
		cmbRelationType.setItems("=", "LIKE", "<>");
		cmbRelationType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Combo relationTypeFilter = new Combo(composite, SWT.NONE);
		relationTypeFilter.setItems("OperationDone", "NotDone");
		relationTypeFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		String relationType = prefs.get("adminHistory.relationType", "");
		if (!relationType.isEmpty()) {
			String[] values = relationType.split(",");
			cmbRelationType.setText(values[0]);
			relationTypeFilter.setText(values[1]);

		}

		// Status

		Label lblStatus = new Label(composite, SWT.NONE);
		lblStatus.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStatus.setText("Status");

		Combo cmbStatus = new Combo(composite, SWT.NONE);
		cmbStatus.setItems("=", "LIKE", "<>");
		cmbStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Combo statusFilter = new Combo(composite, SWT.NONE);
		statusFilter.setItems("OperationDone", "NotDone");
		statusFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		String status = prefs.get("adminHistory.status", "");
		if (!status.isEmpty()) {
			String[] values = status.split(",");
			cmbStatus.setText(values[0]);
			statusFilter.setText(values[1]);

		}

		// Site
		Label lblSite = new Label(composite, SWT.NONE);
		lblSite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSite.setText("Site");

		Combo cmbSite = new Combo(composite, SWT.NONE);
		cmbSite.setItems("=", "LIKE", "<>");
		cmbSite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		txtSitefiltertext = new Text(composite, SWT.BORDER);
		txtSitefiltertext.setText("");
		txtSitefiltertext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		String site = prefs.get("adminHistory.site", "");
		if (!site.isEmpty()) {
			String[] values = site.split(",");
			cmbSite.setText(values[0]);
			txtSitefiltertext.setText(values[1]);

		}

		// GroupName
		Label lblGroupName = new Label(composite, SWT.NONE);
		lblGroupName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblGroupName.setText("GroupName");

		Combo cmbGroupName = new Combo(composite, SWT.NONE);
		cmbGroupName.setItems("=", "LIKE", "<>");
		cmbGroupName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		txtGroupNamefiltertext = new Text(composite, SWT.BORDER);
		txtGroupNamefiltertext.setText("");
		txtGroupNamefiltertext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		String groupName = prefs.get("adminHistory.groupName", "");
		if (!groupName.isEmpty()) {
			String[] values = groupName.split(",");

			cmbGroupName.setText(values[0]);
			txtGroupNamefiltertext.setText(values[1]);
		}

		// UserApplication

		Label lblUserApplication = new Label(composite, SWT.NONE);
		lblUserApplication.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUserApplication.setText("UserApplication");

		Combo cmbUserApplication = new Combo(composite, SWT.NONE);
		cmbUserApplication.setItems("=", "LIKE", "<>");
		cmbUserApplication.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		txtUserApplicationfiltertext = new Text(composite, SWT.BORDER);
		txtUserApplicationfiltertext.setText("");
		txtUserApplicationfiltertext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		String userApplication = prefs.get("adminHistory.userApplication", "");
		if (!userApplication.isEmpty()) {
			String[] values = userApplication.split(",");
			cmbUserApplication.setText(values[0]);
			txtUserApplicationfiltertext.setText(values[1]);
		}

		// Directory
		Label lblDirectory = new Label(composite, SWT.NONE);
		lblDirectory.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDirectory.setText("Directory");

		Combo cmbDirectory = new Combo(composite, SWT.NONE);
		cmbDirectory.setItems("=", "LIKE", "<>");
		cmbDirectory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		txtDirectoryfiltertext = new Text(composite, SWT.BORDER);
		txtDirectoryfiltertext.setText("");
		txtDirectoryfiltertext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		String directory = prefs.get("adminHistory.directory", "");
		if (!directory.isEmpty()) {
			String[] values = directory.split(",");
			cmbDirectory.setText(values[0]);
			txtDirectoryfiltertext.setText(values[1]);

		}

		// Role

		Label lblRole = new Label(composite, SWT.NONE);
		lblRole.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRole.setText("Role");

		Combo cmbRole = new Combo(composite, SWT.NONE);
		cmbRole.setItems("=", "LIKE", "<>");
		cmbRole.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		txtRolefiltertext = new Text(composite, SWT.BORDER);
		txtRolefiltertext.setText("");
		txtRolefiltertext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		String role = prefs.get("adminHistory.role", "");
		if (!role.isEmpty()) {
			String[] values = role.split(",");
			cmbRole.setText(values[0]);
			txtRolefiltertext.setText(values[1]);
		}

		// Limit
		Label lblLimit = new Label(composite, SWT.NONE);
		lblLimit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLimit.setText("Limit");

		txtLimitfilter = new Text(composite, SWT.BORDER);
		txtLimitfilter.setText("");
		txtLimitfilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		String limit = prefs.get("adminHistory.limit", "1000");

		txtLimitfilter.setText(limit);

		txtLimitfilter.addVerifyListener(e -> {

			String string = e.text;
			char[] chars = new char[string.length()];
			string.getChars(0, chars.length, chars, 0);
			for (char element : chars) {
				if ((('a' > element) || (element > 'z'))) {
					e.doit = true;

				} else {
					e.doit = false;
				}

			}

		});

		// ButtonComposite
		Composite buttonBarComposite = new Composite(composite, SWT.NONE);
		buttonBarComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));
		buttonBarComposite.setLayout(new GridLayout(2, false));

		// DefaultButton

		Button defaultButton = new Button(buttonBarComposite, SWT.PUSH);
		defaultButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		defaultButton.setText("Default");
		defaultButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				prefs.put("adminHistory.adminName", "");
				cmbAdminNameHistorytbl.setText("");
				txtAdminNamefiltertext.setText("");
				prefs.put("adminHistory.operation", "");
				cmbOperation.setText("");
				operationFilter.setText("");
				prefs.put("adminHistory.adminArea", "");
				cmbAdminArea.setText("");
				txtAdmimAreafiltertext.setText("");
				prefs.put("adminHistory.project", "");
				cmbProject.setText("");
				txtProjectfiltertext.setText("");
				prefs.put("adminHistory.projectApplication", "");
				cmbProjectApplication.setText("");
				txtProjectApplicationfiltertext.setText("");
				prefs.put("adminHistory.userName", "");
				cmbUserName.setText("");
				txtUserNamefiltertext.setText("");
				prefs.put("adminHistory.startApplication", "");
				cmbStartApplication.setText("");
				txtStartApplicationfiltertext.setText("");
				prefs.put("adminHistory.relationType", "");
				cmbRelationType.setText("");
				relationTypeFilter.setText("");
				prefs.put("adminHistory.status", "");
				cmbStatus.setText("");
				statusFilter.setText("");
				prefs.put("adminHistory.site", "");
				cmbSite.setText("");
				txtSitefiltertext.setText("");
				prefs.put("adminHistory.groupName", "");
				cmbGroupName.setText("");
				txtGroupNamefiltertext.setText("");
				prefs.put("adminHistory.userApplication", "");
				cmbUserApplication.setText("");
				txtUserApplicationfiltertext.setText("");
				prefs.put("adminHistory.directory", "");
				cmbDirectory.setText("");
				txtDirectoryfiltertext.setText("");
				prefs.put("adminHistory.role", "");
				cmbRole.setText("");
				txtRolefiltertext.setText("");

				txtLimitfilter.setText("1000");
				prefs.put("adminHistory.limit", "1000");

			}
		});

		// ApplyButton
		Button applyButton = new Button(buttonBarComposite, SWT.PUSH);
		applyButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		applyButton.setText("Apply");
		applyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				StringBuilder query = new StringBuilder();
				query.append("Select * from ADMIN_HISTORY_RELATIONS_TBL Where ");
				ArrayList<String> conditions = new ArrayList<>();

				String stDateString = null;
				if (startDate.getSelection() != null) {
					Date stDate = startDate.getSelection();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
					stDateString = simpleDateFormat.format(stDate);
					prefs.put("adminHistory.startDate", stDateString);
				}

				String endDateString = null;
				if (endDate.getSelection() != null) {
					Date stDate = endDate.getSelection();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
					endDateString = simpleDateFormat.format(stDate);
					prefs.put("adminHistory.endDate", endDateString);
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

				String adminRule = cmbAdminNameHistorytbl.getText();
				String adminNameFltr = txtAdminNamefiltertext.getText();
				if (!adminNameFltr.isEmpty()) {
					String condition = "ADMIN_NAME " + adminRule + " '" + adminNameFltr + "'";
					conditions.add(condition);
					prefs.put("adminHistory.adminName", adminRule + "," + adminNameFltr);

				}
				String operationRule = cmbOperation.getText();
				String operationFltr = operationFilter.getText();
				if (!operationFltr.isEmpty()) {
					String condition = "OPERATION " + operationRule + " '" + operationFltr + "'";
					conditions.add(condition);
					prefs.put("adminHistory.operation", operationRule + "," + operationFltr);
				}
				String adminAreaRule = cmbAdminArea.getText();
				String adminAreaFltr = txtAdmimAreafiltertext.getText();
				if (!adminAreaFltr.isEmpty()) {
					String condition = "ADMIN_AREA " + adminAreaRule + " '" + adminAreaFltr + "'";
					conditions.add(condition);
					prefs.put("adminHistory.adminArea", adminAreaRule + "," + adminAreaFltr);
				}
				String projectRule = cmbProject.getText();
				String projectFilter = txtProjectfiltertext.getText();
				if (!projectFilter.isEmpty()) {
					String condition = "PROJECT " + projectRule + " '" + projectFilter + "'";
					conditions.add(condition);
					prefs.put("adminHistory.project", projectRule + "," + projectFilter);

				}
				String projectApplicationRule = cmbProjectApplication.getText();
				String projectApplicationFilter = txtProjectApplicationfiltertext.getText();
				if (!projectApplicationFilter.isEmpty()) {
					String condition = "PROJECT_APPLICATION " + projectApplicationRule + " '" + projectApplicationFilter
							+ "'";
					conditions.add(condition);
					prefs.put("adminHistory.projectApplication",
							projectApplicationRule + "," + projectApplicationFilter);
				}
				String userNameRule = cmbUserName.getText();
				String userNameFilter = txtUserNamefiltertext.getText();
				if (!userNameFilter.isEmpty()) {
					String condition = "USER_NAME " + userNameRule + " '" + userNameFilter + "'";
					conditions.add(condition);
					prefs.put("adminHistory.userName", userNameRule + "," + userNameFilter);
				}
				String startApplicationRule = cmbStartApplication.getText();
				String startApplicationFilter = txtStartApplicationfiltertext.getText();
				if (!startApplicationFilter.isEmpty()) {
					String condition = "START_APPLICATION " + startApplicationRule + " '" + startApplicationFilter
							+ "'";
					conditions.add(condition);
					prefs.put("adminHistory.startApplication", startApplicationRule + "," + startApplicationFilter);
				}
				String relationTypeRule = cmbRelationType.getText();
				String relationTypeFltr = relationTypeFilter.getText();
				if (!relationTypeFltr.isEmpty()) {
					String condition = "RELATION_TYPE " + relationTypeRule + " '" + relationTypeFltr + "'";
					conditions.add(condition);
					prefs.put("adminHistory.relationType", relationTypeRule + "," + relationTypeFltr);
				}
				String statusRule = cmbStatus.getText();
				String statusFltr = statusFilter.getText();
				if (!statusFltr.isEmpty()) {
					String condition = "STATUS " + statusRule + " '" + statusFltr + "'";
					conditions.add(condition);
					prefs.put("adminHistory.status", statusRule + "," + statusFltr);
				}
				String siteRule = cmbSite.getText();
				String siteFilter = txtSitefiltertext.getText();
				if (!siteFilter.isEmpty()) {
					String condition = " SITE " + siteRule + " '" + siteFilter + "'";
					conditions.add(condition);
					prefs.put("adminHistory.site", siteRule + "," + siteFilter);

				}
				String groupNameRule = cmbGroupName.getText();
				String groupNameFilter = txtGroupNamefiltertext.getText();
				if (!groupNameFilter.isEmpty()) {
					String condition = "GROUP_NAME " + groupNameRule + " '" + groupNameFilter + "'";
					conditions.add(condition);
					prefs.put("adminHistory.groupName", groupNameRule + "," + groupNameFilter);
				}
				String userApplicationRule = cmbUserApplication.getText();
				String userApplicationFilter = txtUserApplicationfiltertext.getText();
				if (!userApplicationFilter.isEmpty()) {
					String condition = "USER_APPLICATION " + userApplicationRule + " '" + userApplicationFilter + "'";
					conditions.add(condition);
					prefs.put("adminHistory.userApplication", userApplicationRule + "," + userApplicationFilter);
				}
				String directoryRule = cmbDirectory.getText();
				String directoryFilter = txtDirectoryfiltertext.getText();
				if (!directoryFilter.isEmpty()) {
					String condition = "DIRECTORY " + directoryRule + " '" + directoryFilter + "'";
					conditions.add(condition);
					prefs.put("adminHistory.directory", directoryRule + "," + directoryFilter);

				}
				String roleRule = cmbRole.getText();
				String roleFilter = txtRolefiltertext.getText();
				if (!roleFilter.isEmpty()) {
					String condition = "ROLE " + roleRule + " '" + roleFilter + "'";
					conditions.add(condition);
					prefs.put("adminHistory.role", roleRule + "," + roleFilter);
				}
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
						query.append(" AND ROWNUM<= " + queryLimit);
					}
					prefs.put("adminHistory.limit", limitString);
				}

				System.out.println(query);

				adminHistoryRelationshipNatTableContainer.updateNatTable(query.toString());

			}
		});
		try {
			prefs.flush();
		} catch (BackingStoreException e1) {
			e1.printStackTrace();
		}
//		composite.setExpanded(false);

	}

	private void createFilterComposite(Composite parent) {

		Group composite = new Group(parent, SWT.SMOOTH);

		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		composite.setLayout(new GridLayout(6, false));
		composite.setText("AdminBaseObjectFilter Filter");

		Label lblLogTime = new Label(composite, SWT.NONE);
		lblLogTime.setText("Log Time");

		new Label(composite, SWT.NONE);
		// combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		// combo.setItems();

		Label lblCdate = new Label(composite, SWT.NONE);
		lblCdate.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		lblCdate.setText("Start Date");
		CDateTime startDate = new CDateTime(composite, CDT.DROP_DOWN | CDT.TIME_MEDIUM | CDT.DATE_LONG | CDT.DATE_LONG);
//		startDate.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));
		String stDate = prefs.get("adminBaseObject.stDate", "");

		if (!stDate.isEmpty()) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			try {
				startDate.setSelection(simpleDateFormat.parse(stDate));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		Label lblCdate1 = new Label(composite, SWT.NONE);
		lblCdate1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		lblCdate1.setText("End Date");
		CDateTime endDate = new CDateTime(composite, CDT.DROP_DOWN | CDT.TIME_MEDIUM | CDT.DATE_LONG | CDT.DATE_LONG);
//		endDate.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));
		String eDate = prefs.get("adminBaseObject.eDate", "");
		if (!eDate.isEmpty()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			try {
				endDate.setSelection(dateFormat.parse(eDate));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		// AdminName

		Label lblAdminName = new Label(composite, SWT.NONE);
		lblAdminName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAdminName.setText("Admin Name");

		Combo cmbAdminName = new Combo(composite, SWT.NONE);
		cmbAdminName.setItems("=", "LIKE", "<>");
		GridData gd_cmbUser = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_cmbUser.widthHint = 34;
		cmbAdminName.setLayoutData(gd_cmbUser);

		txtAdminNameBaseObjectfiltertext = new Text(composite, SWT.BORDER);
		txtAdminNameBaseObjectfiltertext.setText("");
		txtAdminNameBaseObjectfiltertext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		String adminNameBaseObjectValue = prefs.get("adminBaseObject.adminName", "");
		if (!adminNameBaseObjectValue.isEmpty()) {
			String[] values = adminNameBaseObjectValue.split(",");
			cmbAdminName.setText(values[0]);
			txtAdminNameBaseObjectfiltertext.setText(values[1]);
		}

		// operation
		Label lblOperation = new Label(composite, SWT.NONE);
		lblOperation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOperation.setText("Operation");

		Combo cmbOperation = new Combo(composite, SWT.NONE);
		cmbOperation.setItems("=", "<>");
		cmbOperation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Combo operationFilter = new Combo(composite, SWT.NONE);
		operationFilter.setItems("OperationDone", "NotDone");
		operationFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		String operationValue = prefs.get("adminBaseObject.operation", "");
		if (!operationValue.isEmpty()) {
			String[] values = operationValue.split(",");
			cmbOperation.setText(values[0]);
			operationFilter.setText(values[1]);
		}

		// ObjectName

		Label lblObjectName = new Label(composite, SWT.NONE);
		lblObjectName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblObjectName.setText("Object Name");
		Combo cmbObjectName = new Combo(composite, SWT.NONE);
		cmbObjectName.setItems("=", "LIKE", "<>");

		txtObjectNamefiltertext = new Text(composite, SWT.BORDER);
		txtObjectNamefiltertext.setText("");
		txtObjectNamefiltertext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		String objectNameValue = prefs.get("adminBaseObject.objectName", "");
		if (!objectNameValue.isEmpty()) {
			String[] values = objectNameValue.split(",");
			cmbObjectName.setText(values[0]);
			txtObjectNamefiltertext.setText(values[1]);

		}

		// Result
		Label lblResult = new Label(composite, SWT.NONE);
		lblResult.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblResult.setText("Result");

		Combo cmbResult = new Combo(composite, SWT.NONE);
		cmbResult.setItems("=", "<>");
		cmbResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Combo resultFilter = new Combo(composite, SWT.NONE);
		resultFilter.setItems("SUCCESS", "FAILURE");
		resultFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		String resultValue = prefs.get("adminBaseObject.result", "");
		if (!resultValue.isEmpty()) {
			String[] values = resultValue.split(",");
			cmbResult.setText(values[0]);
			resultFilter.setText(values[1]);

		}
		// Limit
		Label lblLimit = new Label(composite, SWT.NONE);
		lblLimit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLimit.setText("Limit");

		txtBaseObjectLimitfilter = new Text(composite, SWT.BORDER);
		txtBaseObjectLimitfilter.setText("");
		txtBaseObjectLimitfilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));

		String limit = prefs.get("adminBaseObject.limit", "1000");

		txtBaseObjectLimitfilter.setText(limit);

		txtBaseObjectLimitfilter.addVerifyListener(e -> {

			String string = e.text;
			char[] chars = new char[string.length()];
			string.getChars(0, chars.length, chars, 0);
			for (char element : chars) {
				if ((('a' > element) || (element > 'z'))) {
					e.doit = true;

				} else {
					e.doit = false;
				}

			}

		});

		// ButtonComposite
		Composite buttonBarComposite = new Composite(composite, SWT.NONE);
		buttonBarComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));
		buttonBarComposite.setLayout(new GridLayout(2, false));

		// DefaultButton

		Button defaultButton = new Button(buttonBarComposite, SWT.PUSH);
		defaultButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		defaultButton.setText("Default");
		defaultButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				prefs.put("adminBaseObject.adminName", "");
				cmbAdminName.setText("");
				txtAdminNameBaseObjectfiltertext.setText("");
				prefs.put("adminBaseObject.operation", "");
				cmbOperation.setText("");
				operationFilter.setText("");
				prefs.put("adminBaseObject.objectName", "");
				cmbObjectName.setText("");
				txtObjectNamefiltertext.setText("");
				prefs.put("adminBaseObject.result", "");
				cmbResult.setText("");
				resultFilter.setText("");
				txtBaseObjectLimitfilter.setText("");
				prefs.get("adminBaseObject.limit", "");
				prefs.get("adminBaseObject.limit", "1000");
				txtBaseObjectLimitfilter.setText("1000");

			}
		});

		// ApplyButton
		Button applyButton = new Button(buttonBarComposite, SWT.PUSH);
		applyButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		applyButton.setText("Apply");
		applyButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				StringBuilder query = new StringBuilder();
				query.append("select * from ADMIN_HISTORY_BASE_OBJECTS_TBL where ");
				ArrayList<String> conditions = new ArrayList<>();

				String stDateString = null;
				if (startDate.getSelection() != null) {
					Date stDate = startDate.getSelection();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
					stDateString = simpleDateFormat.format(stDate);
					prefs.put("adminBaseObject.stDate", stDateString);
				}

				String endDateString = null;
				if (endDate.getSelection() != null) {
					Date eDate = endDate.getSelection();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
					endDateString = simpleDateFormat.format(eDate);
					prefs.put("adminBaseObject.eDate", endDateString);
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

				String adminNameRule = cmbAdminName.getText();
				String adminNameFilterText = txtAdminNameBaseObjectfiltertext.getText();
				if (!adminNameFilterText.isEmpty()) {
					String condition = "ADMIN_NAME " + adminNameRule + " '" + adminNameFilterText + "'";
					conditions.add(condition);
					prefs.put("adminBaseObject.adminName", adminNameRule + "," + adminNameFilterText);
				}

				String operationRule = cmbOperation.getText();
				String operationFltr = operationFilter.getText();
				if (!operationFltr.isEmpty()) {
					String condition = "OPERATION " + operationRule + " '" + operationFltr + "'";
					conditions.add(condition);
					prefs.put("adminBaseObject.operation", operationRule + "," + operationFltr);
				}

				String objectNameRule = cmbObjectName.getText();
				String objectFilter = txtObjectNamefiltertext.getText();
				if (!objectFilter.isEmpty()) {
					String condition = "OBJECT_NAME " + objectNameRule + " '" + objectFilter + "'";
					conditions.add(condition);
					prefs.put("adminBaseObject.objectName", objectNameRule + "," + objectFilter);
				}

				String resultRule = cmbResult.getText();
				String resultFltr = resultFilter.getText();
				if (!resultFltr.isEmpty()) {
					String condition = "RESULT " + resultRule + " '" + resultFltr + "'";
					conditions.add(condition);
					prefs.put("adminBaseObject.result", resultRule + "," + resultFltr);
				}
				for (String condition : conditions) {
					if (conditions.indexOf(condition) == 0) {
						query.append(condition);
					} else {
						query.append(" AND " + condition);
					}
				}
				String limitString = txtBaseObjectLimitfilter.getText();
				if (!limitString.isEmpty()) {
					Integer queryLimit = Integer.parseInt(limitString);
					if (conditions.size() == 0) {
						query.append("ROWNUM<= " + queryLimit);
					} else {
						query.append("AND ROWNUM<= " + queryLimit);
					}
					prefs.put("adminBaseObject.limit", limitString);
				}

				System.out.println(query);
				adminHistoryBaseObjectContainer.updateNatTable(query.toString());

			}
		});
		try {
			prefs.flush();
		} catch (BackingStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		composite.setExpanded(false);

	}

	private void generateAdminBaseObjectTable() {

		page2 = new Composite(group, SWT.NONE);
		createFilterComposite(page2);
		page2.setLayout(new GridLayout(1, false));

		adminHistoryBaseObjectContainer = new AdminHistoryBaseObjectNattable();
		adminHistoryBaseObjectNatTable = adminHistoryBaseObjectContainer.createAdminHistoryBaseObjectNatTable(page2,
				txtBaseObjectLimitfilter.getText());

	}

	private void generateAdminRelationTable() {
		page1 = new Composite(group, SWT.NONE);
		createFilterComposite1(page1);
		page1.setLayout(new GridLayout(1, false));
		adminHistoryRelationshipNatTableContainer = new AdminHistoryRelationsNattable();
		adminHistoryRelationsNatTable = adminHistoryRelationshipNatTableContainer
				.createAdminHistoryRelationsNatTable(page1, txtLimitfilter.getText());
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
