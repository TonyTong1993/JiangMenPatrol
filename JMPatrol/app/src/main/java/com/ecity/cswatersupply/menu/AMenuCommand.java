package com.ecity.cswatersupply.menu;

import android.os.Bundle;
import android.view.View;

/**
 * 可执行菜单基类
 * 
 * @author SunShan'ai The base class of menu command.
 */
public abstract class AMenuCommand {
    public int id;
    /**
     * 
     * @param iconName
     */
    public abstract int getMenuIconResourceId(String iconName);

    /**
     * To do what when click the menu.
     * 
     * @return
     */
    public abstract boolean execute();

    public boolean executeWithExtra(View v, Bundle extraData) {
        return false;
    }
}
