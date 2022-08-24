package theme_plugin_project.dialogs.nattables;

import java.util.ArrayList;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import theme_plugin_project.dialogs.HistoryTableUtil;
import theme_plugin_project.dialogs.gridlayers.AdminHistoryBaseObjectGridLayer;
import theme_plugin_project.model.AdminHistoryBaseObjectsTbl;

public class AdminHistoryBaseObjectNattable {

	private AdminHistoryBaseObjectGridLayer nattableLayer;
	private NatTable natTable;
	private String limit;

	public NatTable createAdminHistoryBaseObjectNatTable(Composite parent, String limit) {
		this.limit = limit;
		IConfigRegistry configRegistry = new ConfigRegistry();

		ArrayList<AdminHistoryBaseObjectsTbl> userHistoryTableList = retrieveUserHistoryTableList(null);

		nattableLayer = new AdminHistoryBaseObjectGridLayer(configRegistry, userHistoryTableList);

		DataLayer bodyDataLayer = nattableLayer.getBodyDataLayer();
		ListDataProvider dataProvider = nattableLayer.getBodyDataProvider();

		
		final ColumnOverrideLabelAccumulator columnLabelAccumulator = new ColumnOverrideLabelAccumulator(bodyDataLayer);
		bodyDataLayer.setConfigLabelAccumulator(columnLabelAccumulator);

		natTable = new NatTable(parent, nattableLayer, false);
		
		natTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		HistoryTableUtil.addCustomStyling(natTable, dataProvider);
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		HistoryTableUtil.addColumnHighlight(configRegistry);

		// natTable.addConfiguration(new DebugMenuConfiguration(natTable));
		natTable.addConfiguration(new FilterRowCustomConfiguration());

		natTable.setConfigRegistry(configRegistry);
		natTable.configure();

		return natTable;
	}

	private ArrayList<AdminHistoryBaseObjectsTbl> retrieveUserHistoryTableList(String query) {
		ArrayList adminHistoryBaseObjectList = new ArrayList();
		if (query == null) {
			int i;

			for (i = 0; i < 1000; i++) {

				AdminHistoryBaseObjectsTbl adminHistoryBaseObjectsTbl = new AdminHistoryBaseObjectsTbl(
						new Long("100" + i));
				adminHistoryBaseObjectsTbl.setChanges(
						"scrollComposite.setExpandVertical(true);" + "scrollComposite.setExpandHorizontal(true);");
				adminHistoryBaseObjectList.add(adminHistoryBaseObjectsTbl);

			}
		} else {

		}
		return adminHistoryBaseObjectList;
	}

	static class FilterRowCustomConfiguration extends AbstractRegistryConfiguration {

		@Override
		public void configureRegistry(IConfigRegistry configRegistry) {

		}
	}


	public void updateNatTable(String query) {
		nattableLayer.getBodyDataProvider().getList().clear();
		nattableLayer.getBodyDataProvider().getList().addAll(retrieveUserHistoryTableList(query));
		natTable.refresh();
	};

}
