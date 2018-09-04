package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetProjectSummaryWaterMeterInfoParameter implements IRequestParameter {
    private int year;
    private int month;

    public GetProjectSummaryWaterMeterInfoParameter(int year, int month) {
        this.year = year;
        this.month = month;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("year", String.valueOf(year));
        if (month != 0) {
            map.put("month", String.valueOf(month));
        } else {
            map.put("month", "");
        }

        return map;
    }
}
