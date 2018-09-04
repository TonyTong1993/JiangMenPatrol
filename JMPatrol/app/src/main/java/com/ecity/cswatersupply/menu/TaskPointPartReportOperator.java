package com.ecity.cswatersupply.menu;

import java.util.List;

import android.content.Intent;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPointPart;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;
import com.ecity.cswatersupply.network.request.ReportInspectItemsParameter;
import com.ecity.cswatersupply.service.ServiceUrlManager;

public class TaskPointPartReportOperator extends EventReportOperator1 {
    public static final String INTENT_KEY_TASK_ID = "INTENT_KEY_TASK_ID";
    public static final String INTENT_KEY_POINT_PART = "INTENT_KEY_POINT_PART";
    public static final String INTENT_KEY_INSPECTITEMS = "INTENT_KEY_INSPECTITEMS";
    public List<InspectItem> inspectItems = null;
    private Z3PlanTaskPointPart pointPart;
    private int taskId;

    public TaskPointPartReportOperator() {
    }

    @Override
    public List<InspectItem> getDataSource() {
        Intent intent = getActivity().getIntent();
        pointPart = (Z3PlanTaskPointPart) intent.getExtras().getSerializable(INTENT_KEY_POINT_PART);
        taskId = intent.getExtras().getInt(INTENT_KEY_TASK_ID);
        inspectItems = (List<InspectItem>) intent.getExtras().getSerializable(INTENT_KEY_INSPECTITEMS);
        return inspectItems;    
    }

    @Override
    protected String getServiceUrl() {
        return ServiceUrlManager.getInstance().getReportInspectItemsUrl();
    }

    @Override
    protected AReportInspectItemParameter getRequestParameter() {
        User user = HostApplication.getApplication().getCurrentUser();
        return new ReportInspectItemsParameter(user, taskId, pointPart);
    }

    @Override
    protected String getFunctionKey() {
        return "CHECKREPORT";
    }
}
