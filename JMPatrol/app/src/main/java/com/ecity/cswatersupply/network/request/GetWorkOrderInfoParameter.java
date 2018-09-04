package com.ecity.cswatersupply.network.request;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetWorkOrderInfoParameter implements IRequestParameter {
    private String properties;
    private String processInstanceId;


    public GetWorkOrderInfoParameter(String properties, String processInstanceId) {
        this.properties = properties;
        this.processInstanceId = processInstanceId;
    }

    @Override
    public Map<String, String> toMap() {
        User currentUser = HostApplication.getApplication().getCurrentUser();
        Map<String, String> map = new HashMap<String, String>();
        map.put("processinstanceid", processInstanceId);
        map.put("properties", properties);
        map.put("userid", currentUser.getId());
        return map;
    }
}
