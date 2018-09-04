package com.ecity.cswatersupply.emergency.menu;

import android.os.Bundle;

import com.ecity.cswatersupply.emergency.activity.EarthQuakeInfoBaseListActivity;
import com.ecity.cswatersupply.menu.AMenuCommand;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;

/**
 * 破坏上报菜单
 * @author Gxx 2016-12-29
 */
public class DestructionReportCommandXtd extends AMenuCommand {

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        Bundle bundle = new Bundle();
        UIHelper.startActivityWithExtra(EarthQuakeInfoBaseListActivity.class,bundle);
        return true;
    }

}
