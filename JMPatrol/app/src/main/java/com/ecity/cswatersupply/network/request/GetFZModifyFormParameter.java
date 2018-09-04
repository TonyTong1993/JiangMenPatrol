package com.ecity.cswatersupply.network.request;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetFZModifyFormParameter implements IRequestParameter {
    private String groupid;
    private String pointid;

    public GetFZModifyFormParameter(String groupid, String pointid) {
        this.groupid = groupid;
        this.pointid = pointid;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("groupid", groupid);
        map.put("pointid", pointid);
        return map;
    }
}
