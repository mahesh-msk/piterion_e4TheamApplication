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
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDoubleDisplayConverter;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.layer.CompositeLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.layer.NatGridLayerPainter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import theme_plugin_project.dialogs.HistoryTableUtil;
import theme_plugin_project.dialogs.gridlayers.UserHistoryTblGridLayer;
import theme_plugin_project.model.UserHistoryTbl;

public class UserHistoryTblNattable {

	private UserHistoryTblGridLayer natTableLayer;
	private ArrayList<UserHistoryTbl> userHistoryTableList;
	private NatTable natTable;
	private String limit;

	public NatTable createUserHistoryNatTable(Composite parent, String limit) {
		this.limit = limit;
		IConfigRegistry configRegistry = new ConfigRegistry();

		userHistoryTableList = retrieveUserHistoryTableList(null);
		natTableLayer = new UserHistoryTblGridLayer(configRegistry, userHistoryTableList);

		// UserHistoryTblGridLayer underlyingLayer = new
		// UserHistoryTblGridLayer(configRegistry, userHistoryTableList);

		DataLayer bodyDataLayer = natTableLayer.getBodyDataLayer();
		ListDataProvider dataProvider = natTableLayer.getBodyDataProvider();

		// NOTE: Register the accumulator on the body data layer.
		// This ensures that the labels are bound to the column index and are
		// unaffected by column order.
		final ColumnOverrideLabelAccumulator columnLabelAccumulator = new ColumnOverrideLabelAccumulator(bodyDataLayer);
		bodyDataLayer.setConfigLabelAccumulator(columnLabelAccumulator);
		CompositeLayer compositeLayer = new CompositeLayer(1, 2);
//        compositeLayer.setChildLayer(GridRegion.COLUMN_HEADER, columnHeaderLayer, 0, 0);
//        compositeLayer.setChildLayer(GridRegion.BODY, viewportLayer, 0, 1);
		natTable = new NatTable(parent, natTableLayer, false);

		NatGridLayerPainter layerPainter = new NatGridLayerPainter(natTable);
		natTable.setLayerPainter(layerPainter);

		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);

		gd_table.minimumWidth = 400;
		gd_table.minimumHeight = 400;
		natTable.setLayoutData(gd_table);
		natTable.setSize(1000, 500);

		HistoryTableUtil.addCustomStyling(natTable, dataProvider);
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		HistoryTableUtil.addColumnHighlight(configRegistry);

		// natTable.addConfiguration(new DebugMenuConfiguration(natTable));
		natTable.addConfiguration(new FilterRowCustomConfiguration());

		natTable.setConfigRegistry(configRegistry);
		natTable.configure();

		return natTable;
	}

	private ArrayList<UserHistoryTbl> retrieveUserHistoryTableList(String query) {

		ArrayList<UserHistoryTbl> userHistoryTableList = new ArrayList();
		if (query == null) {
			int i;

			for (i = 0; i < 1000; i++) {
				UserHistoryTbl userHistoryTbl = new UserHistoryTbl(new Long("100"), new Date(1000000), "ABC");

				userHistoryTbl.setUserName("XYZ" + i);
				userHistoryTableList.add(userHistoryTbl);

			}
		} else {
			int i;

			for (i = 0; i < 1000; i++) {
				UserHistoryTbl userHistoryTbl = new UserHistoryTbl(new Long("100"), new Date(1000000), "DEF");

				userHistoryTbl.setUserName("ABC" + i);
				userHistoryTableList.add(userHistoryTbl);

			}

		}
		return userHistoryTableList;
	}

	static class FilterRowCustomConfiguration extends AbstractRegistryConfiguration {

		final DefaultDoubleDisplayConverter doubleDisplayConverter = new DefaultDoubleDisplayConverter();

		@Override
		public void configureRegistry(IConfigRegistry configRegistry) {
			// // Configure custom comparator on the rating column
			// configRegistry.registerConfigAttribute(
			// FilterRowConfigAttributes.FILTER_COMPARATOR,
			// getIngnorecaseComparator(),
			// DisplayMode.NORMAL,
			// FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 2);
			//
			// // If threshold comparison is used we have to convert the string
			// // entered by the user to the correct underlying type (double),
			// so
			// // that it can be compared
			//
			// // Configure Bid column
			// configRegistry.registerConfigAttribute(
			// FilterRowConfigAttributes.FILTER_DISPLAY_CONVERTER,
			// this.doubleDisplayConverter,
			// DisplayMode.NORMAL,
			// FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 5);
			// configRegistry.registerConfigAttribute(
			// FilterRowConfigAttributes.TEXT_MATCHING_MODE,
			// TextMatchingMode.REGULAR_EXPRESSION,
			// DisplayMode.NORMAL,
			// FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 5);
			//
			// // Configure Ask column
			// configRegistry.registerConfigAttribute(
			// FilterRowConfigAttributes.FILTER_DISPLAY_CONVERTER,
			// this.doubleDisplayConverter,
			// DisplayMode.NORMAL,
			// FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 6);
			// configRegistry.registerConfigAttribute(
			// FilterRowConfigAttributes.TEXT_MATCHING_MODE,
			// TextMatchingMode.REGULAR_EXPRESSION,
			// DisplayMode.NORMAL,
			// FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 6);
			//
			// // Configure a combo box on the pricing type column
			//
			// // Register a combo box editor to be displayed in the filter row
			// // cell when a value is selected from the combo, the object is
			// // converted to a string using the converter (registered below)
			// configRegistry.registerConfigAttribute(
			// EditConfigAttributes.CELL_EDITOR,
			// new ComboBoxCellEditor(Arrays.asList(new PricingTypeBean("MN"),
			// new PricingTypeBean("AT"))),
			// DisplayMode.NORMAL,
			// FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 4);
			//
			// // The pricing bean object in column is converted to using this
			// // display converter
			// // A 'text' match is then performed against the value from the
			// combo
			// // box
			// configRegistry.registerConfigAttribute(
			// FilterRowConfigAttributes.FILTER_DISPLAY_CONVERTER,
			// new PricingTypeBeanDisplayConverter(),
			// DisplayMode.NORMAL,
			// FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 4);
			//
			// configRegistry.registerConfigAttribute(
			// CellConfigAttributes.DISPLAY_CONVERTER,
			// new PricingTypeBeanDisplayConverter(),
			// DisplayMode.NORMAL,
			// FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 4);
			//
			// configRegistry.registerConfigAttribute(
			// CellConfigAttributes.DISPLAY_CONVERTER,
			// new PricingTypeBeanDisplayConverter(),
			// DisplayMode.NORMAL,
			// "PRICING_TYPE_PROP_NAME");
		}
	}

	private static Comparator getIngnorecaseComparator() {
		return new Comparator() {
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}

			@Override
			public int compare(Object arg0, Object arg1) {
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
