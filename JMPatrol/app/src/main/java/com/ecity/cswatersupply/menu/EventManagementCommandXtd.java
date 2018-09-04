package com.ecity.cswatersupply.menu;

import com.ecity.cswatersupply.ui.activities.ReportEventTypeSelectActivity;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;

public class EventManagementCommandXtd extends AMenuCommand {

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        UIHelper.startActivityWithoutExtra(ReportEventTypeSelectActivity.class);
        return true;
    }

}
