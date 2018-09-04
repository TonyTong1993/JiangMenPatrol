package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class GetPointPartAttrsParameter implements IRequestParameter {
    private String objectIds;

    public GetPointPartAttrsParameter(String mObjectIds) {
        this.objectIds = mObjectIds;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("f", "json");
        map.put("returnGeometry", "true");
        map.put("objectIds", objectIds);
        map.put("outFields", "*");
        return map;
    }

}
