package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.workorder.model.WorkOrder;

public class WorkOrderDetailBasicInfoParameter implements IRequestParameter {
    private WorkOrder workOrder;

    public WorkOrderDetailBasicInfoParameter(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    @Override
    public Map<String, String> toMap() {
        User mUser = HostApplication.getApplication().getCurrentUser();
        Map<String, String> map = new HashMap<String, String>();
        
        JSONObject properties = new JSONObject();
        try {
            //groupID是固定参数
            properties.put("groupid", "0006");
            properties.put("plat", "mobile");
            properties.put("userid", Integer.valueOf(mUser.getId()));
           
        } catch (JSONException e) {
            LogUtil.e(this, e);
        }
        map.put("processinstanceid", workOrder.getAttribute(WorkOrder.KEY_ID));
        map.put("properties", properties.toString());
        
        return map;
    }
}
