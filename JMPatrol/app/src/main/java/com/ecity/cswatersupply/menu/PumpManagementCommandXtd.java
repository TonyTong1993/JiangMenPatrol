package com.ecity.cswatersupply.menu;

import com.ecity.cswatersupply.ui.activities.PumpManagementActivity;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;

/***
 * Created by MaoShouBei on 2017/5/12.
 */

public class PumpManagementCommandXtd extends AMenuCommand {
    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        UIHelper.startActivityWithoutExtra(PumpManagementActivity.class);
        return false;
    }
}
