package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class WorkOrderCategoryParameter implements IRequestParameter {
    private String id;
    public WorkOrderCategoryParameter(String id) {
        this.id= id;
    }

    @Override
    public Map<String, String> toMap() {
        User mUser = HostApplication.getApplication().getCurrentUser();
        Map<String, String> map = new HashMap<String, String>();

        map.put("userid", mUser.getGid());
        map.put("processinstanceid",id);
        return map;
    }
}
