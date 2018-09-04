package com.ecity.cswatersupply.network.request;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetAllPumpsParemeter implements IRequestParameter {

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("_t", "833");
        map.put("f", "json");
        return map;
    }

}
