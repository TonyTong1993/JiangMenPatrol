package com.ecity.cswatersupply.menu;

import com.ecity.cswatersupply.utils.GsonUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrder;

public class AppMenu extends AMenu {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String subname;
    private boolean deleteable;
    private boolean dragable;
    private boolean configurable;

    private AMenuCommand command = null;
    private String titleType;

    public AppMenu() {
        this(null);
    }

    public AppMenu(AMenuCommand comand) {
        deleteable = true;
        dragable = true;
        this.command = comand;
    }

    public void setAMenuCommand(AMenuCommand comand) {
        this.command = comand;
    }

    public AMenuCommand getAMenuCommand() {
        return command;
    }

    public boolean execute() {
        if (null != command) {
            return command.execute();
        }
        return false;
    }

    public int getMenuIconResourceId() {
        if (null != command)
            return command.getMenuIconResourceId(iconName);
        return -1;
    }

    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
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

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public boolean equals(Object other) {
        if (this == other) // 先检查是否其自反性，后比较other是否为空。这样效率高
            return true;
        if (other == null)
            return false;
        if (!(other instanceof WorkOrder))
            return false;
        final AppMenu appMenu = (AppMenu) other;
        return appMenu.getName().equalsIgnoreCase(this.name);
    }
}