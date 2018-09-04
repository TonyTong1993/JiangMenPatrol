package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.network.RequestParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/30.
 */

public class GetWaterMeterInfoParameter implements RequestParameter.IRequestParameter {
    private String type;
    private String startTime;
    private String endTime;

    public GetWaterMeterInfoParameter(String type, String startTime, String endTime) {
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("startTime", startTime);
        map.put("endTime", endTime);

        return map;
    }
}
