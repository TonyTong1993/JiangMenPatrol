package com.ecity.cswatersupply.network.request;

import java.util.List;
import java.util.Map;

import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskLinePart;

public class ReportLineInPlaceParameter extends AReportInspectItemParameter {
    private User user;
    private int taskId;
    private Z3PlanTaskLinePart linePart;

    public ReportLineInPlaceParameter(User user, int taskId, Z3PlanTaskLinePart linePart) {
        super();
        this.user = user;
        this.taskId = taskId;
        this.linePart = linePart;
    }

    @Override
    protected void fillSimpleFields(Map<String, String> map) {
        map.put("userid", user.getId());
        map.put("taskid", String.valueOf(taskId));
        map.put("lineid", String.valueOf(linePart.getTlineid()));
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
