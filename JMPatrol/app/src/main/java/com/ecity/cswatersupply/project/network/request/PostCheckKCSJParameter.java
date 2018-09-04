package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;

import java.util.List;
import java.util.Map;

public class PostCheckKCSJParameter extends AReportInspectItemParameter {
    private List<InspectItem> items;
    private int position;
    private String recordid;

    public PostCheckKCSJParameter(List<InspectItem> items, int position, String recordid) {
        this.items = items;
        this.position = position;
        this.recordid = recordid;
    }

    @Override
    protected void fillSimpleFields(Map<String, String> map) {
        User currentUser = HostApplication.getApplication().getCurrentUser();
        if (position == 2) {
            map.put("type", "1");
        } else if (position == 3) {
            map.put("type", "2");
        }
        map.put("kcid", recordid);
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
