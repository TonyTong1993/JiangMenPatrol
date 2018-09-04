package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class GetAllPatrolManParameter implements IRequestParameter {

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", "");
        map.put("f", "json");
        return map;
    }

}
