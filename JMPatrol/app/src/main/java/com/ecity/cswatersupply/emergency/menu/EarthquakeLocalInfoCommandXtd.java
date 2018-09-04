package com.ecity.cswatersupply.emergency.menu;

import com.ecity.cswatersupply.emergency.activity.EarthquakeLocalInfoActivity;
import com.ecity.cswatersupply.menu.AMenuCommand;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;

/***
 * 本地震情菜单
 * @author Gxx 2016-11-22
 */
public class EarthquakeLocalInfoCommandXtd extends AMenuCommand{

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        UIHelper.startActivityWithoutExtra(EarthquakeLocalInfoActivity.class);
        return true;
    }
}
