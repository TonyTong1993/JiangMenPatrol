package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class PunishStateEndingParameter implements IRequestParameter{
    private String eventId;
    private String amount;
    
    
    public PunishStateEndingParameter(String eventId, String amount) {
        super();
        this.eventId = eventId;
        this.amount = amount;
    }


    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("eventid", eventId);
        map.put("amount", amount);
        return map;
    }

}
