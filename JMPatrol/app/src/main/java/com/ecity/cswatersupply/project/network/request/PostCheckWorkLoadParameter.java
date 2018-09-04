package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;

import java.util.List;
import java.util.Map;

public class PostCheckWorkLoadParameter extends AReportInspectItemParameter {
    private List<InspectItem> items;
    private String projectId;
    private String recordid;

    public PostCheckWorkLoadParameter(List<InspectItem> items, String projectId, String recordid) {
        this.items = items;
        this.projectId = projectId;
        this.recordid = recordid;
    }

    @Override
    protected void fillSimpleFields(Map<String, String> map) {
        User currentUser = HostApplication.getApplication().getCurrentUser();
        map.put("proid", projectId);
        map.put("recordId", recordid);
        map.put("checkerId", currentUser.getId());
        map.put("checkerName", currentUser.getTrueName());
    }

    @Override
    protected String getInspectItemsKey() {
        return "properties";
    }

    @Override
    protected List<InspectItem> getInspectItems() {
        return items;
    }
}
