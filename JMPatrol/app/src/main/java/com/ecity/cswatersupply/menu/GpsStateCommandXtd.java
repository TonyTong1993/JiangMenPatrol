package com.ecity.cswatersupply.menu;

import com.ecity.cswatersupply.ui.activities.GpsStateActivity;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;

public class GpsStateCommandXtd extends AMenuCommand {

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        UIHelper.startActivityWithoutExtra(GpsStateActivity.class);
        //return false;

        /*Intent intent = new Intent(HostApplication.getApplication().getAppManager().currentActivity(), WorkOrderFinishActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CustomReportActivity.REPORT_COMFROM, WorkOrderFinishOperator.class.getName());
        intent.putExtras(bundle);
        HostApplication.getApplication().getAppManager().currentActivity().startActivity(intent);*/

        return true;

    }
}
