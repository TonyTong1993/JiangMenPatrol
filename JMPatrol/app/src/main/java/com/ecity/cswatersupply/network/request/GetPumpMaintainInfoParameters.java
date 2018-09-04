package com.ecity.cswatersupply.network.request;

import com.ecity.cswatersupply.network.RequestParameter;

import java.util.HashMap;
import java.util.Map;

/***  
 * Created by MaoShouBei on 2017/5/19.
 */

public class GetPumpMaintainInfoParameters implements RequestParameter.IRequestParameter {
    private String pumpGid;


    public GetPumpMaintainInfoParameters(String gid) {
        this.pumpGid = gid;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("pumpgid", pumpGid);
        return map;
    }
}
