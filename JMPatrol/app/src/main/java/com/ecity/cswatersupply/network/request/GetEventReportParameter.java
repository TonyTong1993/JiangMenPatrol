package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class GetEventReportParameter implements IRequestParameter {
    private String requestKey;

    public GetEventReportParameter(String key) {
        this.requestKey = key;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("eventtype", requestKey);
        return map;
    }
}
