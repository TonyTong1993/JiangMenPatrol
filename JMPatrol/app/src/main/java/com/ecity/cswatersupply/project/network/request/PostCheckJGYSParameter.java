package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;

import java.util.List;
import java.util.Map;

public class PostCheckJGYSParameter extends AReportInspectItemParameter {
    private List<InspectItem> items;
    private String recordid;

    public PostCheckJGYSParameter(List<InspectItem> items, String recordid) {
        this.items = items;
        this.recordid = recordid;
    }

    @Override
    protected void fillSimpleFields(Map<String, String> map) {
        User currentUser = HostApplication.getApplication().getCurrentUser();
        map.put("jgid", recordid);
        map.put("userid", currentUser.getId());
        map.put("username", currentUser.getTrueName());
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
