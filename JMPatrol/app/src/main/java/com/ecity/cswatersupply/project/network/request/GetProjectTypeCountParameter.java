package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetProjectTypeCountParameter implements IRequestParameter {
    private String type;
    private String startTime;
    private String endTime;

    public GetProjectTypeCountParameter(String type, String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", type);
        map.put("startTime", startTime);
        map.put("endTime", endTime);

        return map;
    }

}
