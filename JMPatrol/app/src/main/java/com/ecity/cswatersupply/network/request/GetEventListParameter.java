package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.event.EventType;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class GetEventListParameter implements IRequestParameter {
    private User user;
    private EventType eventType;

    public GetEventListParameter(User user, EventType eventType) {
        this.user = user;
        this.eventType = eventType;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", user.getId());
        map.put("eventtype", String.valueOf(eventType.getValue()));
        map.put("where", "");
        map.put("formplat", "mobile");
        return map;
    }

}
