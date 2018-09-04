package com.ecity.cswatersupply.network.request;

import java.util.List;
import java.util.Map;

import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPointPart;

public class ReportPointInPlaceParameter extends AReportInspectItemParameter {
    private User user;
    private int taskId;
    private Z3PlanTaskPointPart pointPart;

    public ReportPointInPlaceParameter(User user, int taskId, Z3PlanTaskPointPart pointPart) {
        super();
        this.user = user;
        this.taskId = taskId;
        this.pointPart = pointPart;
    }

    @Override
    protected void fillSimpleFields(Map<String, String> map) {
        map.put("userid", user.getId());
        map.put("taskid", String.valueOf(taskId));
        map.put("pointid", String.valueOf(pointPart.getTpointid()));
    }

    @Override
    protected String getInspectItemsKey() {
        return "properties";
    }

    @Override
    protected List<InspectItem> getInspectItems() {
        return SessionManager.currentInspectItems;
    }
}
