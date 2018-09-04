package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetProjectForMapParameter implements IRequestParameter {
    private String type;

    public GetProjectForMapParameter(String type) {
        this.type = type;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", type);

        return map;
    }

}
