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

/**
 * 转单处理
 * @author gaokai
 *
 */
public class HandleTransferWorkOrderCommandXtd extends AMenuCommand {

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
        data.putInt(CustomViewInflater.REPORT_TITLE, R.string.title_workorder_handle_transfer);
        data.putString(CustomViewInflater.REPORT_COMFROM, WorkOrderOperator.class.getName());
        data.putInt(CustomViewInflater.BOTTOM_TOOLBAR_MODE, CustomViewInflater.TWO_BTNS);
        // 传入数据包
        data.putBoolean(WorkOrderOperator.KEY_AUDIT, true); //走审核流程
        data.putString(WorkOrder.KEY_SUB_STATE, WorkOrderState.TRANSFER.value);
        data.putString(WorkOrderOperator.KEY_CACHE, HandleTransferWorkOrderCommandXtd.class.getName());
        data.putInt(WorkOrderOperator.KEY_EVENTID, UIEventStatus.WORKORDER_COMMON_INSPECT_REPORT);
        data.putInt(WorkOrderOperator.KEY_REPORT_SUCCESS_MSG, R.string.audit_transfer_workorder_status);
        UIHelper.startActivityWithExtra(CustomReportActivity1.class, data);
        return true;
    }
}