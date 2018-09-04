package com.ecity.cswatersupply.network.request;

import java.util.List;
import java.util.Map;

import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPointPart;

public class ReportInspectItemsParameter extends AReportInspectItemParameter {
    private User user;
    private int taskId;
    private Z3PlanTaskPointPart pointPart;

    public ReportInspectItemsParameter(User user, int taskId, Z3PlanTaskPointPart pointPart) {
        super();

        this.user = user;
        this.taskId = taskId;
        this.pointPart = pointPart;
    }

    @Override
    protected void fillSimpleFields(Map<String, String> map) {
        map.put("userid", user.getId());
        map.put("username", user.getLoginName());
        map.put("taskId", String.valueOf(taskId));
        map.put("targetId", String.valueOf(pointPart.getGid()));
        map.put("geoType", String.valueOf(getGeoType()));
    }

    @Override
    protected String getInspectItemsKey() {
        return "data";
    }

    @Override
    protected List<InspectItem> getInspectItems() {
//        return PlanTasksAdapter.getInstance().adaptInspectItems(pointPart.getContents());
        return SessionManager.content.getInspectItems();
    }
    
    private int getGeoType() {
        // all inspect items of pointPart have a same geoType, so we just get geoType of the first inspect item
//        return SessionManager.contentsList.get(0).getGeoType();
        return SessionManager.content.getInspectItems().get(0).getGeoType();
    }
}
