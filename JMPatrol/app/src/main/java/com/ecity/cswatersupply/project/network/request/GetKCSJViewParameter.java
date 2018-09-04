package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetKCSJViewParameter implements IRequestParameter {
    private String kcid;
    private User user;

    public GetKCSJViewParameter(String kcid, User user) {
        this.kcid = kcid;
        this.user = user;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("kcid", kcid);
        map.put("userid", user.getId());
        map.put("username", user.getTrueName());

        return map;
    }

}
