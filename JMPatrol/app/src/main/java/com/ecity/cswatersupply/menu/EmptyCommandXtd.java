package com.ecity.cswatersupply.menu;

public class EmptyCommandXtd extends AMenuCommand {

    @Override
    public int getMenuIconResourceId(String iconName) {
        return 0; // App launcher icon
    }

    @Override
    public boolean execute() {
        return false;
    }
}
