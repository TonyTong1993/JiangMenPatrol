package com.ecity.cswatersupply.emergency.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class GetEQXCYJDCInspectInfoParameter implements IRequestParameter {

    public GetEQXCYJDCInspectInfoParameter() {
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", "0002");
        return map;
    }
}
