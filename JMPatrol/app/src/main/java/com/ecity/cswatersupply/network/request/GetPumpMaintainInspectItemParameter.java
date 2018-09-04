package com.ecity.cswatersupply.network.request;

import com.ecity.cswatersupply.network.RequestParameter;

import java.util.HashMap;
import java.util.Map;

/***  
 * Created by MaoShouBei on 2017/5/19.
 */

public class GetPumpMaintainInspectItemParameter implements RequestParameter.IRequestParameter {
    private String processInstanceId;

    public GetPumpMaintainInspectItemParameter(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("etypeid", "11");
        map.put("businesskey", processInstanceId);
        return map;
    }
}
