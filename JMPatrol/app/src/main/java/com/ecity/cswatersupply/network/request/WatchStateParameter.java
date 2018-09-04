package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class WatchStateParameter implements IRequestParameter {
    private User user;

    public WatchStateParameter(User user) {
        this.user = user;
    }

    @Override
    public Map<String, String> toMap() {
        
        Map<String, String> map = new HashMap<String, String>();
        JSONObject json = new JSONObject();
        try {
            json.put("userid", user.getGid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.put("queryOption", json.toString());
        return map;
    }

}
