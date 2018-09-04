package com.ecity.cswatersupply.emergency.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class GetEQZQSBInspectInfoParameter implements IRequestParameter {

    public GetEQZQSBInspectInfoParameter() {
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", "0001");
        return map;
    }
}
