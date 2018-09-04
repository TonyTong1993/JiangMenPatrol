package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class WorkOrderParameter implements IRequestParameter {

    public WorkOrderParameter() {
    }

    @Override
    public Map<String, String> toMap() {
        User mUser = HostApplication.getApplication().getCurrentUser();
        Map<String, String> map = new HashMap<String, String>();

        JSONObject properties = new JSONObject();
        try {
            properties.put("userid", mUser.getGid());
            properties.put("plat", "mobile");
            properties.put("pagesize", "1000");
            properties.put("pagenum", "1");
        } catch (JSONException e) {
            LogUtil.e(this, e);
        }
        map.put("properties", properties.toString());
        return map;
    }
}
