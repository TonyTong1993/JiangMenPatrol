package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class SimpleParameter implements IRequestParameter{

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("f", "json");
        map.put( "_t", System.currentTimeMillis() + "");       
        return map;
    }

}
