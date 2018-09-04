package com.ecity.cswatersupply.workorder.menu;

import android.os.Bundle;
import android.view.View;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.menu.AMenuCommand;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.presenter.WorkOrderOperator;

/**
 * 现场勘察
 * 
 * @author gaokai
 *
 */
public class ExploreWorkOrderCommandXtd extends AMenuCommand {
    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public boolean executeWithExtra(View v, Bundle data) {
        if (data == null) {
            return false;
        }
        data.putString(CustomViewInflater.REPORT_COMFROM, WorkOrderOperator.class.getName());
        data.putInt(CustomViewInflater.REPORT_TITLE, R.string.title_workorder_explore);
        
        data.putString(WorkOrderOperator.KEY_CACHE, ExploreWorkOrderCommandXtd.class.getName());
        data.putInt(WorkOrderOperator.KEY_EVENTID, UIEventStatus.WORKORDER_COMMON_INSPECT_REPORT);
        data.putInt(WorkOrderOperator.KEY_REPORT_SUCCESS_MSG, R.string.explore_workorder_status);
        UIHelper.startActivityWithExtra(CustomReportActivity1.class, data);
        return true;
    }
}
