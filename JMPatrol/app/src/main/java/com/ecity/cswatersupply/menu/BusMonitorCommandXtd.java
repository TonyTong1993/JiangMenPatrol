package com.ecity.cswatersupply.menu;

import android.os.Bundle;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.menu.map.BusMonitorOperatorXtd;
import com.ecity.cswatersupply.menu.map.PersonMonitorOperatorXtd;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;

/***
 * Created by MaoShouBei on 2017/5/25.
 */

public class BusMonitorCommandXtd extends AMenuCommand {
    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        Bundle bundle = new Bundle();
        bundle.putString(MapActivity.MAP_TITLE, ResourceUtil.getStringById(R.string.main_meun_subname_busmonitor));
        bundle.putString(MapActivity.MAP_OPERATOR, BusMonitorOperatorXtd.class.getName());
        UIHelper.startActivityWithExtra(MapActivity.class, bundle);
        return true;
    }
}
