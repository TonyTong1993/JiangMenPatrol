package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetPatrolTreeParameter implements IRequestParameter {
    private User user;

    public GetPatrolTreeParameter(User user) {
        this.user = user;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", user.getId());
        map.put("forMobile", "true");

        return map;
    }
}
