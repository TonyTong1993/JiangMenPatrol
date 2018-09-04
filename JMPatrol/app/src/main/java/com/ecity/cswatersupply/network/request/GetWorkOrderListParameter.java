package com.ecity.cswatersupply.network.request;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetWorkOrderListParameter implements IRequestParameter {
    private int pageNum;
    private int pageSize;
    private String strWhere;
    private String workorderState;

    public GetWorkOrderListParameter(int pageNum, int pageSize, String strWhere, String workorderState) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.strWhere = strWhere;
        this.workorderState = workorderState;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();

        map.put("properties", getJsonStr());
        map.put("f", "json");

        return map;
    }

    private String getJsonStr() {
        User mUser = HostApplication.getApplication().getCurrentUser();
        JSONObject object = new JSONObject();
        try {
            object.put("userid", mUser.getGid());
            object.put("plat", "mobile");
            object.put("pagesize", String.valueOf(pageSize));
            object.put("pagenum", String.valueOf(pageNum));
            object.put("strWhere", strWhere);
            object.put("workorderState", workorderState);
            object.put("returnCount", "false");
            object.put("queryall", "true");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}
