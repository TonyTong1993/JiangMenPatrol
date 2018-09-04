package com.ecity.cswatersupply.emergency.network.request;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetEQZQSBListParameter implements IRequestParameter {

    public GetEQZQSBListParameter() {
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", "0006");
        map.put("type", "");
        map.put("eqid", "");
        return map;
    }
}
