package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class PunishmentDetailsParameter implements IRequestParameter{
    private String eventId;
    private String eventType;
    

    public PunishmentDetailsParameter(String eventId, String eventType) {
        super();
        this.eventId = eventId;
        this.eventType = eventType;
    }


    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("eventid", eventId);
        map.put("eventtype", eventType);
        map.put("plat", "mobile");
        return map;
    }

}
