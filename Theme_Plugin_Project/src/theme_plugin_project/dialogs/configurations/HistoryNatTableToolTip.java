package theme_plugin_project.dialogs.configurations;

import org.eclipse.jface.window.DefaultToolTip;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

public class HistoryNatTableToolTip extends DefaultToolTip {

	private NatTable natTable;
	private ListDataProvider dataProvider;

	public HistoryNatTableToolTip(NatTable natTable, ListDataProvider dataProvider) {
		super(natTable, ToolTip.NO_RECREATE, false);
		this.natTable = natTable;
		this.dataProvider = dataProvider;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.window.ToolTip#getToolTipArea(org.eclipse.swt.widgets
	 * .Event)
	 *
	 * Implementation here means the tooltip is not redrawn unless mouse hover
	 * moves outside of the current cell (the combination of ToolTip.NO_RECREATE
	 * style and override of this method).
	 */
	@Override
	protected Object getToolTipArea(Event event) {
		int col = this.natTable.getColumnPositionByX(event.x);
		int row = this.natTable.getRowPositionByY(event.y);

		return new Point(col, row);
	}
	
	@Override
	protected String getText(Event event) {
		int col = this.natTable.getColumnPositionByX(event.x);
		int row = this.natTable.getRowPositionByY(event.y);

		if (col == 0 && row == 0) {
			return "No of Rows " + (dataProvider.getList().size());
		}
		if (natTable.getCellByPosition(col, row).getDataValue() != null) {
			Object dataValue = natTable.getCellByPosition(col, row).getDataValue();
			if (dataValue != null) {
				return dataValue.toString();
			}
		}
		return "";
	}
	
	@Override
	protected boolean shouldCreateToolTip(Event event) {
		int col = this.natTable.getColumnPositionByX(event.x);
		int row = this.natTable.getRowPositionByY(event.y);
		if (col == 0 && row == 0) {
			return true;
		}
		if (natTable.getCellByPosition(col, row).getDataValue() != null) {
			Object dataValue = natTable.getCellByPosition(col, row).getDataValue();
			if(dataValue.toString().isEmpty()) {
				return false;
			} else {
				return true;
			}
		}
	  return false;
	}

	@Override
	protected Composite createToolTipContentArea(Event event, Composite parent) {
		// This is where you could get really creative with your tooltips...
		return super.createToolTipContentArea(event, parent);
	}
}