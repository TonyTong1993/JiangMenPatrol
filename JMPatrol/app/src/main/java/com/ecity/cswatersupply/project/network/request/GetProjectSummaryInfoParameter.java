package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetProjectSummaryInfoParameter implements IRequestParameter {
    private int type;
    private int year;
    private int month;
    private String startTime;
    private String endTime;

    public GetProjectSummaryInfoParameter(int type, int year, int month, String startTime, String endTime) {
        this.type = type;
        this.year = year;
        this.month = month;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", String.valueOf(type));
        map.put("year", String.valueOf(year));
        map.put("month", String.valueOf(month));
        map.put("startTime", startTime);
        map.put("endTime", endTime);

        return map;
    }
}
