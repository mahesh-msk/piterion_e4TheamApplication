package theme_plugin_project.dialogs.gridlayers;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsEventLayer;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsSortModel;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.filterrow.DefaultGlazedListsStaticFilterStrategy;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowHeaderComposite;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultRowHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.stack.DefaultBodyLayerStack;
import org.eclipse.nebula.widgets.nattable.sort.SortHeaderLayer;
import org.eclipse.nebula.widgets.nattable.sort.config.SingleClickSortConfiguration;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TransformedList;
import theme_plugin_project.dialogs.HistoryTableUtil;
import theme_plugin_project.model.AdminHistoryBaseObjectsTbl;

public class AdminHistoryBaseObjectGridLayer extends GridLayer {

    private final ListDataProvider<AdminHistoryBaseObjectsTbl> bodyDataProvider;
    private final DataLayer bodyDataLayer;

    public AdminHistoryBaseObjectGridLayer(IConfigRegistry configRegistry, ArrayList<AdminHistoryBaseObjectsTbl> userHistoryTableList) {
        super(true);
		
        // Underlying data source
        EventList<AdminHistoryBaseObjectsTbl> eventList =
                GlazedLists.eventList(userHistoryTableList);
        TransformedList<AdminHistoryBaseObjectsTbl, AdminHistoryBaseObjectsTbl> rowObjectsGlazedList =
                GlazedLists.threadSafeList(eventList);
        SortedList<AdminHistoryBaseObjectsTbl> sortedList =
                new SortedList<AdminHistoryBaseObjectsTbl>(rowObjectsGlazedList, null);
        FilterList<AdminHistoryBaseObjectsTbl> filterList =
                new FilterList<AdminHistoryBaseObjectsTbl>(sortedList);
        String[] propertyNames = HistoryTableUtil.getAdminHistoryBaseObjectPropNames();
        Map<String, String> propertyToLabelMap = HistoryTableUtil.getAdminHistoryBaseObjectPropToLabelMap();

        // Body layer
        IColumnPropertyAccessor<AdminHistoryBaseObjectsTbl> columnPropertyAccessor =
                new ReflectiveColumnPropertyAccessor<AdminHistoryBaseObjectsTbl>(HistoryTableUtil.getAdminHistoryBaseObjectPropNames());

        this.bodyDataProvider = new ListDataProvider<AdminHistoryBaseObjectsTbl>(filterList, columnPropertyAccessor);
        // add a static filter that only shows AdminHistoryBaseObjectsTbls with a rating
        // other than "AAA"
        // bodyDataProvider = new
        // AbstractFilterListDataProvider<AdminHistoryBaseObjectsTbl>(filterList,
        // columnPropertyAccessor) {
        // @Override
        // protected boolean show(AdminHistoryBaseObjectsTbl object) {
        // return !(object.rating.equals("AAA"));
        // }
        // };

        this.bodyDataLayer = new DataLayer(this.bodyDataProvider);
        GlazedListsEventLayer<AdminHistoryBaseObjectsTbl> glazedListsEventLayer =
                new GlazedListsEventLayer<AdminHistoryBaseObjectsTbl>(this.bodyDataLayer, eventList);
        DefaultBodyLayerStack bodyLayer =
                new DefaultBodyLayerStack(glazedListsEventLayer);
        ColumnOverrideLabelAccumulator bodyLabelAccumulator =
                new ColumnOverrideLabelAccumulator(this.bodyDataLayer);
        this.bodyDataLayer.setConfigLabelAccumulator(bodyLabelAccumulator);

       

        // Column header layer
        IDataProvider columnHeaderDataProvider =
                new DefaultColumnHeaderDataProvider(propertyNames, propertyToLabelMap);
        DataLayer columnHeaderDataLayer =
                new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
        ColumnHeaderLayer columnHeaderLayer =
                new ColumnHeaderLayer(columnHeaderDataLayer, bodyLayer, bodyLayer.getSelectionLayer());

        SortHeaderLayer<AdminHistoryBaseObjectsTbl> sortHeaderLayer =
                new SortHeaderLayer<AdminHistoryBaseObjectsTbl>(
                        columnHeaderLayer,
                        new GlazedListsSortModel<AdminHistoryBaseObjectsTbl>(
                                sortedList, columnPropertyAccessor, configRegistry, columnHeaderDataLayer),
                        false);
        sortHeaderLayer.addConfiguration(new SingleClickSortConfiguration());

        // Note: The column header layer is wrapped in a filter row composite.
        // This plugs in the filter row functionality

        // DefaultGlazedListsFilterStrategy<AdminHistoryBaseObjectsTbl> filterStrategy =
        // new
        // DefaultGlazedListsFilterStrategy<AdminHistoryBaseObjectsTbl>(autoFilterMatcherEditor,
        // columnPropertyAccessor, configRegistry);
        DefaultGlazedListsStaticFilterStrategy<AdminHistoryBaseObjectsTbl> filterStrategy =
                new DefaultGlazedListsStaticFilterStrategy<AdminHistoryBaseObjectsTbl>(
                        filterList, columnPropertyAccessor, configRegistry);
        filterStrategy.addStaticFilter(new ca.odell.glazedlists.matchers.Matcher<AdminHistoryBaseObjectsTbl>() {
			
			@Override
			public boolean matches(AdminHistoryBaseObjectsTbl item) {
				// TODO Auto-generated method stub
				return true;
			}
		});

        FilterRowHeaderComposite<AdminHistoryBaseObjectsTbl> filterRowHeaderLayer =
                new FilterRowHeaderComposite<AdminHistoryBaseObjectsTbl>(
                        filterStrategy, sortHeaderLayer, columnHeaderDataProvider, configRegistry);

        ColumnOverrideLabelAccumulator labelAccumulator =
                new ColumnOverrideLabelAccumulator(columnHeaderDataLayer);
        columnHeaderDataLayer.setConfigLabelAccumulator(labelAccumulator);

        

        // Row header layer
        DefaultRowHeaderDataProvider rowHeaderDataProvider =
                new DefaultRowHeaderDataProvider(this.bodyDataProvider);
        DefaultRowHeaderDataLayer rowHeaderDataLayer =
                new DefaultRowHeaderDataLayer(rowHeaderDataProvider);
        RowHeaderLayer rowHeaderLayer =
                new RowHeaderLayer(rowHeaderDataLayer, bodyLayer, bodyLayer.getSelectionLayer());

        // Corner layer
        DefaultCornerDataProvider cornerDataProvider =
                new DefaultCornerDataProvider(columnHeaderDataProvider, rowHeaderDataProvider);
        DataLayer cornerDataLayer =
                new DataLayer(cornerDataProvider);
        CornerLayer cornerLayer =
                new CornerLayer(cornerDataLayer, rowHeaderLayer, filterRowHeaderLayer);

        // Grid
        setBodyLayer(bodyLayer);
        // Note: Set the filter row as the column header
        setColumnHeaderLayer(filterRowHeaderLayer);
        setRowHeaderLayer(rowHeaderLayer);
        setCornerLayer(cornerLayer);
    }

    public ListDataProvider<AdminHistoryBaseObjectsTbl> getBodyDataProvider() {
        return this.bodyDataProvider;
    }

    public DataLayer getBodyDataLayer() {
        return this.bodyDataLayer;
    }

}
