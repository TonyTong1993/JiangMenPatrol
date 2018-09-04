package com.ecity.cswatersupply.workorder.model;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class WorkOrderPieStaticsParamter implements IRequestParameter {
    private  WorkOrderPieStaticsData pieData;
    private boolean isPieChart;

    public WorkOrderPieStaticsParamter( WorkOrderPieStaticsData pieData,boolean isPieChart) {
        this.pieData = pieData;
        this.isPieChart = isPieChart;

    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        JSONObject json = new JSONObject();
        try {
            json.put("group", pieData.getGroup());
            json.put("strDate", pieData.getStrDate());
            json.put("endDate", pieData.getEndDate());
            if (isPieChart){
                json.put("dType", pieData.isYearType() ? "year" : "month");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.put("queryOption", json.toString());
        return map;
    }

}