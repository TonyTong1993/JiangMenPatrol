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
 * 派单
 * 
 * @author gaokai
 *
 */
public class DispatchWorkOrderCommandXtd extends AMenuCommand {

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
        data.putInt(CustomViewInflater.REPORT_TITLE, R.string.title_workorder_dispatch);
        data.putString(CustomViewInflater.REPORT_COMFROM, WorkOrderOperator.class.getName());
        data.putInt(CustomViewInflater.BOTTOM_SINLEBTN_TXT, R.string.bottom_single_btn_dispatch);
        // 传入数据包
        data.putString(WorkOrderOperator.KEY_CACHE, DispatchWorkOrderCommandXtd.class.getName()); // Question: KEY_CACHE用来做什么？
        data.putInt(WorkOrderOperator.KEY_EVENTID, UIEventStatus.WORKORDER_COMMON_INSPECT_REPORT);
        data.putInt(WorkOrderOperator.KEY_REPORT_SUCCESS_MSG, R.string.dispatch_workorder_status);
        UIHelper.startActivityWithExtra(CustomReportActivity1.class, data);
        return true;
    }
}
