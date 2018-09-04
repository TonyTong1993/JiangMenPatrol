package com.ecity.cswatersupply.menu.map;

import com.ecity.cswatersupply.menu.AEventCommandXtd;
import com.ecity.cswatersupply.menu.AMenu;

public class AEventMenu extends AMenu {

    private static final long serialVersionUID = 1L;

    private String subname;
    private boolean deleteable;
    private boolean dragable;
    private boolean configurable;

    private AEventCommandXtd eventCommand = null;

    public AEventMenu() {
        this(null);
    }

    public AEventMenu(AEventCommandXtd eventCommand) {
        deleteable = true;
        dragable = true;
        this.eventCommand = eventCommand;
    }

    public AEventCommandXtd getEventCommand() {
        return eventCommand;
    }

    public void setEventCommand(AEventCommandXtd eventCommand) {
        this.eventCommand = eventCommand;
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

    public boolean execute() {
        if (null != eventCommand) {
            return eventCommand.execute();
        }
        return false;
    }
}