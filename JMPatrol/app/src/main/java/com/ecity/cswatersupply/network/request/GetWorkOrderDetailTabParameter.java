package com.ecity.cswatersupply.network.request;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetWorkOrderDetailTabParameter implements IRequestParameter {
    private String processInstanceId;


    public GetWorkOrderDetailTabParameter(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("processInstanceId", processInstanceId);
        map.put("plat", "mobile");
        return map;
    }
}
