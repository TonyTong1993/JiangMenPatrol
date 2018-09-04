package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class GetCloseEventParameter implements IRequestParameter {
    private User user;
    private String eventId;

    public GetCloseEventParameter(User user, String eventId) {
        this.user = user;
        this.eventId = eventId;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", user.getId());
        map.put("eventid", eventId);
        map.put("where", "");
        map.put("formplat", "mobile");
        return map;
    }

}
