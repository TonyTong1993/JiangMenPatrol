package com.ecity.cswatersupply.workorder.menu;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;

import com.ecity.android.contactmanchooser.model.ContactMan;
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

public class AuditAsk4HelpWorkOrderCommandXtd extends AMenuCommand {
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
        data.putInt(CustomViewInflater.REPORT_TITLE, R.string.title_workorder_audit_ask4help);
        data.putString(CustomViewInflater.REPORT_COMFROM, WorkOrderOperator.class.getName());
        data.putInt(CustomViewInflater.BOTTOM_TOOLBAR_MODE, CustomViewInflater.TWO_BTNS);
        // 传入数据包
        data.putBoolean(WorkOrderOperator.KEY_AUDIT, true);// 所有审核页面都要传
        data.putString(WorkOrder.KEY_SUB_STATE, WorkOrderState.ASKFORHELP.value);
        data.putString(WorkOrderOperator.KEY_CACHE, AuditAsk4HelpWorkOrderCommandXtd.class.getName());
        data.putInt(WorkOrderOperator.KEY_EVENTID, UIEventStatus.WORKORDER_COMMON_INSPECT_REPORT);
        data.putInt(WorkOrderOperator.KEY_REPORT_SUCCESS_MSG, R.string.audit_ask4help_workorder_status);

        setFilterContacts(data);

        UIHelper.startActivityWithExtra(CustomReportActivity1.class, data);
        return true;
    }

    private void setFilterContacts(Bundle data) {
        WorkOrder workOrder = (WorkOrder) data.getSerializable(WorkOrder.KEY_SERIAL);
        String mainAssigneeName = workOrder.getAttribute("assign_main_workman");
        String assistantAssigneeNamesStr = workOrder.getAttribute("assign_assistances");

        ArrayList<ContactMan> mainAssignees = new ArrayList<ContactMan>();
        ContactMan mainAssignee = new ContactMan();
        mainAssignee.setName(mainAssigneeName);
        mainAssignees.add(mainAssignee);

        ArrayList<ContactMan> assistantAssignees = new ArrayList<ContactMan>();
        String[] assistantAssigneeNames = assistantAssigneeNamesStr.split(",");
        for (String name : assistantAssigneeNames) {
            ContactMan assignee = new ContactMan();
            assignee.setName(name);
            assistantAssignees.add(assignee);
        }

        CustomViewInflater.setContactsFilter(CustomViewInflater.KEY_CONTACT_MAN_FILTER_MAIN_ASSIGNEE, mainAssignees);
        CustomViewInflater.setContactsFilter(CustomViewInflater.KEY_CONTACT_MAN_FILTER_ASSISTANT_ASSIGNEES, assistantAssignees);
    }
}
