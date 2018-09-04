package com.ecity.cswatersupply.project.menu;

import android.os.Bundle;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.menu.AMenuCommand;
import com.ecity.cswatersupply.project.activity.ProjectCommonListActivity;
import com.ecity.cswatersupply.project.activity.fragment.WorkLoadProjectFragment;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;

public class WorkLoadCommandXtd extends AMenuCommand {

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        Bundle bundle = new Bundle();
        bundle.putString(ProjectCommonListActivity.INTENT_KEY_FRAGMENT_CLASS_NAME, WorkLoadProjectFragment.class.getName());
        bundle.putString(ProjectCommonListActivity.INTENT_KEY_TITLE, HostApplication.getApplication().getResources().getString(R.string.project_workload_title));
        UIHelper.startActivityWithExtra(ProjectCommonListActivity.class, bundle);
        return true;
    }
}
