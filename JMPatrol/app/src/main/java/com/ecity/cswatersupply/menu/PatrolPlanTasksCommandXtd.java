package com.ecity.cswatersupply.menu;

import android.content.Intent;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.ui.activities.planningtask.PlanningTaskActivity;
import com.ecity.cswatersupply.utils.ResourceUtil;

public class PatrolPlanTasksCommandXtd extends AMenuCommand {

    @Override
    public int getMenuIconResourceId(String iconName) {

        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {

        Intent intent = new Intent(HostApplication.getApplication().getApplicationContext(), PlanningTaskActivity.class);
        intent.putExtra("isContents", 1);
        intent.putExtra("type", HostApplication.getApplication().getApplicationContext().getResources().getString(R.string.planningtask_type_p));
        SessionManager.isContent = 1;
        HostApplication.getApplication().getAppManager().currentActivity().startActivity(intent);
        return true;
    }

}
