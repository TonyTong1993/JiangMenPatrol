package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class LoginParameter implements IRequestParameter {
    private String token;

    public LoginParameter(String token) {
        this.token = token;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("_type", "json");
        map.put("token", token);
        map.put("sys", "mobile");
        return map;
    }

}
