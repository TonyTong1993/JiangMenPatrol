package com.ecity.cswatersupply.network.request;

import com.ecity.cswatersupply.network.RequestParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jonathanma on 10/3/2017.
 */

public class GetEventWorkOrderInfoParameter implements RequestParameter.IRequestParameter {
    private String eventId;

    public GetEventWorkOrderInfoParameter(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("where", "eventId=" + eventId);
        map.put("f", "json");
        return map;
    }

}
