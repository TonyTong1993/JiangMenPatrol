package com.ecity.cswatersupply.emergency.menu;

import android.os.Bundle;

import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.emergency.activity.EarthQuakeEmergencyInvestigationActivity;
import com.ecity.cswatersupply.emergency.activity.EarthquakeLocalInfoActivity;
import com.ecity.cswatersupply.emergency.activity.ImportEarthQuakeListActivity;
import com.ecity.cswatersupply.menu.AMenuCommand;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;

/***
 * 现场调查菜单
 *
 * 武汉地震的灾情详报
 */
public class EarthQuakeFieldInvestigationCommand extends AMenuCommand {
    public static final String FLAG_FROM_EARTHQUAKE_XCDC = "FLAG_FROM_EARTHQUAKE_XCDC";

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        Bundle bundle = new Bundle();
        bundle.putString(FLAG_FROM_EARTHQUAKE_XCDC, FLAG_FROM_EARTHQUAKE_XCDC);
        SessionManager.isImageNotEdit = false;
//        UIHelper.startActivityWithExtra(EarthquakeLocalInfoActivity.class, bundle);
        UIHelper.startActivityWithExtra(ImportEarthQuakeListActivity.class, bundle);
        return true;
    }

}
