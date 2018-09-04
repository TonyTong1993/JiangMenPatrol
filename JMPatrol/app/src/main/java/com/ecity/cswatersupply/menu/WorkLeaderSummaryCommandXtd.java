package com.ecity.cswatersupply.menu;

import android.content.Intent;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.workorder.view.WorkLeaderDetailActivity;

public class WorkLeaderSummaryCommandXtd extends AMenuCommand {

    @Override
    public int getMenuIconResourceId(String iconName) {

        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        Intent intent = new Intent(HostApplication.getApplication().getApplicationContext(), WorkLeaderDetailActivity.class);
        HostApplication.getApplication().getAppManager().currentActivity().startActivity(intent);
        return true;
    }
}
