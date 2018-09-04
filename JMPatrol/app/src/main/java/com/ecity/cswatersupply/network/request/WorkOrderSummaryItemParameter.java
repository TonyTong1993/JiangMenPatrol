package com.ecity.cswatersupply.network.request;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.workorder.model.WorkOrderPieStaticsData;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class WorkOrderSummaryItemParameter implements IRequestParameter {
    private WorkOrderPieStaticsData pieData;

    public WorkOrderSummaryItemParameter(WorkOrderPieStaticsData pieData) {
       this.pieData = pieData;
    }

    @Override
    public Map<String, String> toMap() {
        User mUser = HostApplication.getApplication().getCurrentUser();
        Map<String, String> map = new HashMap<String, String>();
        Map<String, String> result = new HashMap<String, String>();
        map.put("userid", mUser.getGid());
        map.put("group", pieData.getGroup());
        map.put("strDate", pieData.getStrDate());
        map.put("endDate", pieData.getEndDate());
        map.put("type", pieData.getCategory());
        map.put("dType", pieData.isYearType()?"year":"month");

        JSONObject json = new JSONObject(map);
        String queryOption = "";

        try {
            queryOption =  URLEncoder.encode(json.toString(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.put("queryOption", queryOption);
        return result;
    }
}
