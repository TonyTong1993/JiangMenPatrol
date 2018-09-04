package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class GetPlanAndPipeAddressParameter implements IRequestParameter {
    private String text;
    private String size;

    public GetPlanAndPipeAddressParameter(String text, String size) {
        this.text = text;
        this.size = size;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("text", text);
        map.put("size", size);
        return map;
    }

}
