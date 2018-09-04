package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;

import java.util.List;
import java.util.Map;

public class PostSafeCreatPageParameter extends AReportInspectItemParameter {
    private List<InspectItem> items;
    private String proid;
    private String eveid;

    public PostSafeCreatPageParameter(List<InspectItem> items, String proid, String eveid) {
        this.items = items;
        this.proid = proid;
        this.eveid = eveid;
    }

    @Override
    protected void fillSimpleFields(Map<String, String> map) {
        User currentUser = HostApplication.getApplication().getCurrentUser();
        map.put("proid", proid);
        if (eveid != null) {
            map.put("eveId", eveid);
        }
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
