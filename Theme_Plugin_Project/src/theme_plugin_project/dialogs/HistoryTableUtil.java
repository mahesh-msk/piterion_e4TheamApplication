package theme_plugin_project.dialogs;

import java.util.HashMap;
import java.util.Map;

import javax.swing.event.ListDataListener;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.config.DefaultRowStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.PaddingDecorator;
import org.eclipse.nebula.widgets.nattable.selection.config.DefaultSelectionStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.style.BorderStyle;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.style.VerticalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.BorderStyle.LineStyleEnum;
import org.eclipse.nebula.widgets.nattable.ui.menu.HeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuBuilder;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;

import theme_plugin_project.dialogs.configurations.HistoryNatTableToolTip;
import theme_plugin_project.dialogs.configurations.StyledColumnHeaderConfiguration;
import theme_plugin_project.dialogs.configurations.StyledRowHeaderConfiguration;

public class HistoryTableUtil {
	
	
	
	private static Map<String, String> userHistoryPropToLabelMap;
	private static Map<String, String> adminHistoryBaseObjectPropToLabelMap;
	private static Map<String, String> adminHistoryRelationsPropToLabelMap;
	
	
	 /**
     * Register an attribute to be applied to all cells with the highlight
     * label. A similar approach can be used to bind styling to an arbitrary
     * group of cells
     */
    public static void addColumnHighlight(IConfigRegistry configRegistry) {
        Style style = new Style();
        style.setAttributeValue(
                CellStyleAttributes.FOREGROUND_COLOR,
                GUIHelper.COLOR_BLUE);
        style.setAttributeValue(
                CellStyleAttributes.HORIZONTAL_ALIGNMENT,
                HorizontalAlignmentEnum.RIGHT);

        configRegistry.registerConfigAttribute(
                CellConfigAttributes.CELL_STYLE, // attribute to apply
                style, // value of the attribute
                DisplayMode.NORMAL, // apply during normal rendering i.e not
                                    // during selection or edit
                "BodyLabel_1"); // apply the above for all cells with this label
    }
    

    public static void addCustomStyling(NatTable natTable, ListDataProvider dataProvider) {
        // Setup NatTable default styling
    	HistoryNatTableToolTip tooltip = new HistoryNatTableToolTip(natTable, dataProvider);

        // NOTE: Getting the colors and fonts from the GUIHelper ensures that
        // they are disposed properly (required by SWT)
        DefaultNatTableStyleConfiguration natTableConfiguration = new DefaultNatTableStyleConfiguration();
        natTableConfiguration.bgColor = GUIHelper.getColor(249, 172, 7);
        natTableConfiguration.fgColor = GUIHelper.getColor(30, 76, 19);
        natTableConfiguration.hAlign = HorizontalAlignmentEnum.LEFT;
        natTableConfiguration.vAlign = VerticalAlignmentEnum.TOP;

        // A custom painter can be plugged in to paint the cells differently
        natTableConfiguration.cellPainter = new PaddingDecorator(new TextPainter(), 1);

        // Setup even odd row colors - row colors override the NatTable default
        // colors
        DefaultRowStyleConfiguration rowStyleConfiguration = new DefaultRowStyleConfiguration();
        rowStyleConfiguration.oddRowBgColor = GUIHelper.getColor(254, 251, 243);
        rowStyleConfiguration.evenRowBgColor = GUIHelper.COLOR_WHITE;

        // Setup selection styling
        DefaultSelectionStyleConfiguration selectionStyle = new DefaultSelectionStyleConfiguration();
        selectionStyle.selectionFont = GUIHelper.getFont(new FontData("Verdana", 8, SWT.NORMAL));
        selectionStyle.selectionBgColor = GUIHelper.getColor(217, 232, 251);
        selectionStyle.selectionFgColor = GUIHelper.COLOR_BLACK;
        selectionStyle.anchorBorderStyle = new BorderStyle(1, GUIHelper.COLOR_DARK_GRAY, LineStyleEnum.SOLID);
        selectionStyle.anchorBgColor = GUIHelper.getColor(255, 0, 0);
        selectionStyle.selectedHeaderBgColor = GUIHelper.getColor(156, 209, 103);

        // Add all style configurations to NatTable
        natTable.addConfiguration(natTableConfiguration);
        natTable.addConfiguration(rowStyleConfiguration);
        natTable.addConfiguration(selectionStyle);

        // Column/Row header style and custom painters
        natTable.addConfiguration(new StyledRowHeaderConfiguration());
        natTable.addConfiguration(new StyledColumnHeaderConfiguration());

        // Add popup menu - build your own popup menu using the PopupMenuBuilder
        natTable.addConfiguration(new HeaderMenuConfiguration(natTable) {
        	@Override
        	protected PopupMenuBuilder createColumnHeaderMenu(NatTable natTable) {
        		 return new PopupMenuBuilder(natTable).withHideColumnMenuItem()
        	                .withShowAllColumnsMenuItem()
        	                .withAutoResizeSelectedColumnsMenuItem()
        	                .withClearAllFilters();
        	}
        });
    }
	

	
	/**
	 * Table Property Names
	 */
	private static String[] userHistoryPropNames = new String[] { "id", "logTime", "userName", "host", "site", "adminArea", "project",
			"application", "pid", "result", "args" };
	
	
	private static String[] adminHistoryRelationsPropName = new String[] { "id", "logTime", "adminName", "apiRequestPath", "operation", "adminArea",
			"project", "projectApplication", "userName", "startApplication", "relationType", "status", "site",
			"groupName", "userApplication", "directory", "role", "errorMessage", "result" };
	
	private static String[] adminHistoryBaseObjectPropNames = new String[] { "id", "logTime", "adminName", "apiRequestPath", "operation", "objectName",
			"changes", "errorMessage", "result" };

	public static String[] getUserHistoryPropNames() {
		return userHistoryPropNames;
	}

	public static String[] getAdminHistoryRelationsPropName() {
		return adminHistoryRelationsPropName;
	}
	
	public static String[] getAdminHistoryBaseObjectPropNames() {
		return adminHistoryBaseObjectPropNames;
	}
	/**
	 * End : Table Property Names
	 */

	public static Map<String, String> getUserHistoryPropToLabelMap() {
		if(userHistoryPropToLabelMap == null) {
			userHistoryPropToLabelMap = new HashMap<>();
			userHistoryPropToLabelMap.put("id", "ID");
			userHistoryPropToLabelMap.put("logTime", "LOGTIME");
			userHistoryPropToLabelMap.put("userName", "USERNAME");
			userHistoryPropToLabelMap.put("host", "HOST");
			userHistoryPropToLabelMap.put("site", "SITE");
			userHistoryPropToLabelMap.put("adminArea", "ADMINAREA");
			userHistoryPropToLabelMap.put("project", "PROJECT");
			userHistoryPropToLabelMap.put("application", "APPLICATION");
			userHistoryPropToLabelMap.put("pid", "PID");
			userHistoryPropToLabelMap.put("result", "RESULT");
			userHistoryPropToLabelMap.put("args", "ARGS");
			userHistoryPropToLabelMap.put("isUserStatus", "ISUSERSTATUS");
		}
		return userHistoryPropToLabelMap;
	}
	
	public static Map<String, String> getAdminHistoryBaseObjectPropToLabelMap() {
		if(adminHistoryBaseObjectPropToLabelMap == null) {
			adminHistoryBaseObjectPropToLabelMap = new HashMap<>();
			adminHistoryBaseObjectPropToLabelMap.put("id", "ID");
			adminHistoryBaseObjectPropToLabelMap.put("logTime", "LOGIME");
			adminHistoryBaseObjectPropToLabelMap.put("adminName", "ADMINNAME");
			adminHistoryBaseObjectPropToLabelMap.put("apiRequestPath", "APIREQUESTPATH");
			adminHistoryBaseObjectPropToLabelMap.put("operation", "OPERATION");
			adminHistoryBaseObjectPropToLabelMap.put("objectName", "OBJECTNAME");
			adminHistoryBaseObjectPropToLabelMap.put("changes", "CHANGES");
			adminHistoryBaseObjectPropToLabelMap.put("errorMessage", "ERRORMESSAGE");
			adminHistoryBaseObjectPropToLabelMap.put("result", "RESULT");
		}
		return adminHistoryBaseObjectPropToLabelMap;
	}
	
	public static Map<String, String> getAdminHistoryRelationsPropToLabelMap() {
		if(adminHistoryRelationsPropToLabelMap == null) {
			adminHistoryRelationsPropToLabelMap = new HashMap<>();
			adminHistoryRelationsPropToLabelMap.put("id", "ID");
			adminHistoryRelationsPropToLabelMap.put("logTime", "LOGIME");
			adminHistoryRelationsPropToLabelMap.put("adminName", "ADMINNAME");
			adminHistoryRelationsPropToLabelMap.put("apiRequestPath", "APIREQUESTPATH");
			adminHistoryRelationsPropToLabelMap.put("operation", "OPERATION");
			adminHistoryRelationsPropToLabelMap.put("adminArea", "ADMINAREA");
			adminHistoryRelationsPropToLabelMap.put("project", "PROJECT");
			adminHistoryRelationsPropToLabelMap.put("projectApplication", "PROJECTAPPLICATION");
			adminHistoryRelationsPropToLabelMap.put("userName", "USERNAME");
			adminHistoryRelationsPropToLabelMap.put("startApplication", "STARTAPPLICATION");
			adminHistoryRelationsPropToLabelMap.put("relationType", "RELATIONTYPE");
			adminHistoryRelationsPropToLabelMap.put("status", "STATUS");
			adminHistoryRelationsPropToLabelMap.put("site", "SITE");
			adminHistoryRelationsPropToLabelMap.put("groupName", "GROUPNAME");
			adminHistoryRelationsPropToLabelMap.put("userApplication", "USERAPPLICATION");
			adminHistoryRelationsPropToLabelMap.put(" directory", "DIRECTORY");
			adminHistoryRelationsPropToLabelMap.put("role", "ROLE");
			adminHistoryRelationsPropToLabelMap.put("errorMessage", "ERRORMESSAGE");
			adminHistoryRelationsPropToLabelMap.put("result", "RESULT");
		}
		return adminHistoryRelationsPropToLabelMap;
	}

	

	

}
