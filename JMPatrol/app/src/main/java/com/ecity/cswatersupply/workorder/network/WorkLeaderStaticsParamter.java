package com.ecity.cswatersupply.workorder.network;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.workorder.model.WorkOrderPieStaticsData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WorkLeaderStaticsParamter implements IRequestParameter {
    private WorkOrderPieStaticsData pieData;

    public WorkLeaderStaticsParamter(WorkOrderPieStaticsData pieData) {
        this.pieData = pieData;

    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        try {
            User user = HostApplication.getApplication().getCurrentUser();
            map.put("userid", user.getId());
            map.put("starttime", pieData.getStrDate());
            map.put("endtime", pieData.getEndDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}