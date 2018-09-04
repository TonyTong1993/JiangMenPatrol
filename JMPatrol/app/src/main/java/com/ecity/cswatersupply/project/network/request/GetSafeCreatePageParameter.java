package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;


public class GetSafeCreatePageParameter implements IRequestParameter {
    private User user;

    public GetSafeCreatePageParameter(User user) {
        this.user = user;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", user.getId());
        return map;
    }

}
