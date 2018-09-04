package com.ecity.cswatersupply.menu;

import android.content.Intent;
import android.os.Bundle;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.activities.CustomMainReportActivity1;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ResourceUtil;

public class EventReportCommandXtd extends AMenuCommand {

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        Intent intent = new Intent(HostApplication.getApplication().getAppManager().currentActivity(), CustomMainReportActivity1.class);
        Bundle bundle = new Bundle();
        bundle.putInt(CustomViewInflater.REPORT_TITLE, R.string.event_report_title);
        bundle.putString(CustomViewInflater.REPORT_COMFROM, EventReportOperator1.class.getName());
        intent.putExtras(bundle);
        HostApplication.getApplication().getAppManager().currentActivity().startActivity(intent);

        return true;
    }
}
