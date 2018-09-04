package com.ecity.cswatersupply.emergency.menu;

import com.ecity.cswatersupply.emergency.activity.EarthQuakeInfoBaseListActivity;
import com.ecity.cswatersupply.menu.AMenuCommand;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;

/***
 * 地震信息菜单
 * @author Gxx 2016-11-22
 */
public class EarthquakeInfoCommandXtd extends AMenuCommand{

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        UIHelper.startActivityWithoutExtra(EarthQuakeInfoBaseListActivity.class);
        return true;
    }
}
