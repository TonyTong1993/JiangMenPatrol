package com.ecity.cswatersupply.menu;

import android.content.Intent;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.ui.activities.planningtask.PlanningTaskActivity;
import com.ecity.cswatersupply.utils.ResourceUtil;

public class PlanningTasksCommandXtd extends AMenuCommand {

	@Override
	public int getMenuIconResourceId(String iconName) {
		
		return ResourceUtil.getDrawableResourceId(iconName);
	}

	@Override
	public boolean execute() {
	        
	    Intent intent = new Intent(HostApplication.getApplication()
                .getApplicationContext(), PlanningTaskActivity.class);
        HostApplication.getApplication().getAppManager().currentActivity()
                .startActivity(intent);
		return true;
	}

}
