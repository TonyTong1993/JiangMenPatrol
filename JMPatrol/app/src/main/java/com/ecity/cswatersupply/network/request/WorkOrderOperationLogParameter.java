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

public class WorkOrderOperationLogParameter implements IRequestParameter {
    private WorkOrder workOrder;

    public WorkOrderOperationLogParameter(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    @Override
    public Map<String, String> toMap() {
        User mUser = HostApplication.getApplication().getCurrentUser();
        Map<String, String> map = new HashMap<String, String>();

        JSONObject properties = new JSONObject();
        try {
            properties.put("groupid", mUser.getGroupId());
            properties.put("plat", "mobile");
        } catch (JSONException e) {
            LogUtil.e(this, e);
        }
        map.put("properties", properties.toString());
        map.put("processinstanceid", workOrder.getAttribute(WorkOrder.KEY_ID));
        return map;
    }
}
