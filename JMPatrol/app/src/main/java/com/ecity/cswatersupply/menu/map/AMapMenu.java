package com.ecity.cswatersupply.menu.map;

import com.ecity.cswatersupply.menu.AMenu;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.esri.android.map.MapView;

public class AMapMenu extends AMenu {

    private static final long serialVersionUID = 1L;

    private String subname;
    private boolean deleteable;
    private boolean dragable;
    private boolean configurable;

    private AMapMenuCommand mapCommand = null;

    public AMapMenu() {
        this(null);
    }

    public AMapMenu(AMapMenuCommand mapCommand) {
        deleteable = true;
        dragable = true;
        this.mapCommand = mapCommand;
    }

    public void setMapMenuCommand(AMapMenuCommand mapCommand) {
        this.mapCommand = mapCommand;
    }

    public AMapMenuCommand getMapMenuCommand() {
        return mapCommand;
    }

    public boolean execute(MapView mapView, IMapOperationContext fragment) {
        if (null != mapCommand) {
            return mapCommand.execute(mapView, fragment);
        }
        return false;
    }

    public int getMenuIconResourceId() {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    public String getSubName() {
        return subname;
    }

    public void setSubName(String subname) {
        this.subname = subname;
    }

    public boolean isDeleteable() {
        return deleteable;
    }

    public void setDeleteable(boolean deleteable) {
        this.deleteable = deleteable;
    }

    public boolean isDragable() {
        return dragable;
    }

    public void setDragable(boolean dragable) {
        this.dragable = dragable;
    }

    public boolean isConfigurable() {
        return configurable;
    }

    public void setConfigurable(boolean configurable) {
        this.configurable = configurable;
    }
}