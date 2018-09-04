package com.ecity.cswatersupply.network.request;

import com.ecity.cswatersupply.network.RequestParameter;

import java.util.HashMap;
import java.util.Map;

/***  
 * Created by MaoShouBei on 2017/5/23.
 */

public class GetWorkOrderParameter implements RequestParameter.IRequestParameter {
    private String processInstanceId;

    public GetWorkOrderParameter(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("where", "processinstanceid=" + processInstanceId);
        map.put("f", "json");
        return map;
    }
}
