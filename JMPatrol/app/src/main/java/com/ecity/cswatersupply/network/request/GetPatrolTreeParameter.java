package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class GetPatrolTreeParameter implements IRequestParameter {
    private User user;

    public GetPatrolTreeParameter(User user) {
        this.user = user;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        if (user == null) {
            map.put("userid", "");
        } else {
            map.put("userid", user.getId());
        }

        return map;
    }
}
