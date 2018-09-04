package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class PunishmentListParameter implements IRequestParameter {
    public User user;

    public PunishmentListParameter(User user) {
        this.user = user;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", user.getId());
        map.put("eventtype", "2");
        map.put("where", "");
        map.put("formplat", "mobile");
        return map;
    }

}
