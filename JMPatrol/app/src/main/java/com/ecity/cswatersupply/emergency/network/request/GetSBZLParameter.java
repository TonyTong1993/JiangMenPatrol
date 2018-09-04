package com.ecity.cswatersupply.emergency.network.request;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetSBZLParameter implements IRequestParameter{

    private int eqid;
    public GetSBZLParameter(int eqid) {
        this.eqid = eqid;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("eqid", String.valueOf(eqid));
        return map;
    }

}
