package theme_plugin_project.dialogs.nattables;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import theme_plugin_project.dialogs.HistoryTableUtil;
import theme_plugin_project.dialogs.gridlayers.AdminHistoryRelationsGridLayer;
import theme_plugin_project.model.AdminHistoryRelationsTbl;

public class AdminHistoryRelationsNattable {

	private NatTable natTable;
	private ArrayList<AdminHistoryRelationsTbl> userHistoryTableList;
	private AdminHistoryRelationsGridLayer natTableLayer;
	private String limit;

	public NatTable createAdminHistoryRelationsNatTable(Composite parent, String limit) {
		this.limit = limit;
		
		IConfigRegistry configRegistry = new ConfigRegistry();
		userHistoryTableList = retrieveUserHistoryTableList(null);
		natTableLayer = new AdminHistoryRelationsGridLayer(configRegistry,
				userHistoryTableList);

		DataLayer bodyDataLayer = natTableLayer.getBodyDataLayer();
		ListDataProvider<?> dataProvider = natTableLayer.getBodyDataProvider();

		// NOTE: Register the accumulator on the body data layer.
		// This ensures that the labels are bound to the column index and are
		// unaffected by column order.
		final ColumnOverrideLabelAccumulator columnLabelAccumulator = new ColumnOverrideLabelAccumulator(bodyDataLayer);
		bodyDataLayer.setConfigLabelAccumulator(columnLabelAccumulator);

		natTable = new NatTable(parent, natTableLayer, false);

		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.widthHint = 600;
		gd_table.heightHint = 600;
		natTable.setLayoutData(gd_table);
		HistoryTableUtil.addCustomStyling(natTable, dataProvider);
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		HistoryTableUtil.addColumnHighlight(configRegistry);

		// natTable.addConfiguration(new DebugMenuConfiguration(natTable));
		natTable.addConfiguration(new FilterRowCustomConfiguration());

		natTable.setConfigRegistry(configRegistry);
		natTable.configure();

		return natTable;
	}

	private ArrayList<AdminHistoryRelationsTbl> retrieveUserHistoryTableList(String query) {
		ArrayList<AdminHistoryRelationsTbl> adminHistoryRelationsList = new ArrayList<AdminHistoryRelationsTbl>();
		if (query == null) {
			int i;
			
			for (i = 0; i <= 1000; i++) {
				AdminHistoryRelationsTbl adminHistoryRelationsTbl1 = new AdminHistoryRelationsTbl(new Long("100" + i),
						new Date(1000000), "ABC", "ABC", "ABC");

				AdminHistoryRelationsTbl adminHistoryRelationsTbl2 = new AdminHistoryRelationsTbl(new Long("100" + i),
						new Date(1000000), "XYZ", "XYZ", "XYZ");

				adminHistoryRelationsList.add(adminHistoryRelationsTbl1);
				adminHistoryRelationsList.add(adminHistoryRelationsTbl2);
			}
		} else {
			//Write DB Code here that retrieves value basd on Query
int i;
			
			for (i = 0; i <= 1000; i++) {
				AdminHistoryRelationsTbl adminHistoryRelationsTbl1 = new AdminHistoryRelationsTbl(new Long("100" + i),
						new Date(1000000), "KLM", "ABC", "ABC");

				AdminHistoryRelationsTbl adminHistoryRelationsTbl2 = new AdminHistoryRelationsTbl(new Long("100" + i),
						new Date(1000000), "MNO", "XYZ", "XYZ");

				adminHistoryRelationsList.add(adminHistoryRelationsTbl1);
				adminHistoryRelationsList.add(adminHistoryRelationsTbl2);
			}
		}

		return adminHistoryRelationsList;
	}

	static class FilterRowCustomConfiguration extends AbstractRegistryConfiguration {


		@Override
		public void configureRegistry(IConfigRegistry configRegistry) {
//			configRegistry.registerConfigAttribute(
//                    CellConfigAttributes.CELL_PAINTER,
//                    new BackgroundPainter(new PaddingDecorator(new RichTextCellPainter(), 2, 5, 2, 5)),
//                    DisplayMode.NORMAL,
//                    ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + 4);

		}
	}

	private static Comparator<?> getIngnorecaseComparator() {
		return new Comparator<Object>() {
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}

			@Override
			public int compare(Object arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0.toString().compareToIgnoreCase(arg1.toString());
			}
		};
	}

	public void updateNatTable(String query) {

		natTableLayer.getBodyDataProvider().getList().clear();
		natTableLayer.getBodyDataProvider().getList().addAll(retrieveUserHistoryTableList(query));
		natTable.update();
		natTable.refresh();
	};

}
