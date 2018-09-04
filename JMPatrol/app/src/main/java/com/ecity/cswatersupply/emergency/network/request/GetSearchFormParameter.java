package com.ecity.cswatersupply.emergency.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class GetSearchFormParameter implements IRequestParameter {

    private int key;
    public GetSearchFormParameter(int key) {
        this.key = key;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", String.valueOf(key));
        map.put("f", "json");
        map.put( "_t", System.currentTimeMillis() + "");   
        return map;
    }
}
