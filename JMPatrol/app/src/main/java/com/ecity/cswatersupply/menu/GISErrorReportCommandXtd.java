package com.ecity.cswatersupply.menu;

import android.content.Intent;
import android.os.Bundle;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.activities.GISErrorReportActivity;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ResourceUtil;

public class GISErrorReportCommandXtd extends AMenuCommand {

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        Intent intent = new Intent(HostApplication.getApplication().getAppManager().currentActivity(), GISErrorReportActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CustomViewInflater.REPORT_COMFROM, GISErrorReportOperator.class.getName());
        bundle.putInt(CustomViewInflater.REPORT_TITLE, R.string.gis_error_title);
        intent.putExtras(bundle); 
        HostApplication.getApplication().getAppManager().currentActivity().startActivity(intent);

        return true;
    }
}
