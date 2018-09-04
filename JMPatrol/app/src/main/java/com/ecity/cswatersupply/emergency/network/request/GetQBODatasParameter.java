package com.ecity.cswatersupply.emergency.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class GetQBODatasParameter implements IRequestParameter{

    private int type;
    public GetQBODatasParameter(int type) {
        this.type = type;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", String.valueOf(type));
        return map;
    }

}
