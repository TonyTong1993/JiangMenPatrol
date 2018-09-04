package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class PunishStatePrintingParameter implements IRequestParameter{
    private String eventID;
    
    public PunishStatePrintingParameter(String eventID){
        this.eventID = eventID;
    }
    
    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("eventid", eventID);
        return map;
    }

}
