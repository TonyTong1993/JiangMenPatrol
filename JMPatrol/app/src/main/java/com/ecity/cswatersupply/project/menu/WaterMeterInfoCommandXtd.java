package com.ecity.cswatersupply.project.menu;

import android.os.Bundle;

import com.ecity.cswatersupply.menu.AMenuCommand;
import com.ecity.cswatersupply.project.activity.WaterMeterAllInfoActivity;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;

//水表报装统计
public class WaterMeterInfoCommandXtd extends AMenuCommand {

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        Bundle bundle = new Bundle();
//        bundle.putString(ProjectCommonListActivity.INTENT_KEY_FRAGMENT_CLASS_NAME, WaterReceiveListFragment.class.getName());
//        bundle.putString(ProjectCommonListActivity.INTENT_KEY_TITLE, HostApplication.getApplication().getResources().getString(R.string.project_water_receive));
        UIHelper.startActivityWithExtra(WaterMeterAllInfoActivity.class, bundle);
        return true;
    }
}
