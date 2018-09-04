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
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderState;
import com.ecity.cswatersupply.workorder.presenter.WorkOrderOperator;

public class ProcessWorkOrderCommandXtd extends AMenuCommand{

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
        
        data.putInt(CustomViewInflater.REPORT_TITLE, R.string.title_workorder_pocess);
        data.putString(CustomViewInflater.REPORT_COMFROM, WorkOrderOperator.class.getName());
        //传入数据包
        data.putBoolean(WorkOrderOperator.KEY_SUB_WORK_FLOW, true);
        data.putString(WorkOrder.KEY_SUB_STATE, WorkOrderState.POCESS.value);
        data.putString(WorkOrderOperator.KEY_CACHE, ProcessWorkOrderCommandXtd.class.getName());
        data.putInt(WorkOrderOperator.KEY_EVENTID, UIEventStatus.WORKORDER_COMMON_INSPECT_REPORT);
        data.putInt(WorkOrderOperator.KEY_REPORT_SUCCESS_MSG, R.string.pocess_workorder_status);
        UIHelper.startActivityWithExtra(CustomReportActivity1.class, data);
        return true;
    }
}
