package com.ecity.cswatersupply.workorder.menu;

import android.os.Bundle;
import android.view.View;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.menu.AMenuCommand;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.WorkOrderFinishActivity;

public class AskFinishWorkOrderCommandXtd extends AMenuCommand {
    @Override
    public int getMenuIconResourceId(String iconName) {

        return 0;
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
        data.putInt(CustomViewInflater.REPORT_TITLE, R.string.title_askfinish);
        data.putString(CustomViewInflater.REPORT_COMFROM, WorkOrderFinishOperator.class.getName());
        //传入数据包
        data.putString(WorkOrderFinishOperator.KEY_CACHE, AskFinishWorkOrderCommandXtd.class.getName());
        data.putInt(WorkOrderFinishOperator.KEY_EVENTID, UIEventStatus.WORKORDER_COMMON_INSPECT_REPORT);
        data.putInt(WorkOrderFinishOperator.KEY_REPORT_SUCCESS_MSG, R.string.askfinish_workorder_status);
        UIHelper.startActivityWithExtra(WorkOrderFinishActivity.class, data);
        return true;
    }
}
