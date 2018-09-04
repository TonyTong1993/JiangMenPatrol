package com.ecity.cswatersupply.emergency.network.request;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetEQRefugeParameter implements IRequestParameter {

    public GetEQRefugeParameter() {
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("f", "json");
        map.put("returnGeometry", "true");
        return map;
    }
}
