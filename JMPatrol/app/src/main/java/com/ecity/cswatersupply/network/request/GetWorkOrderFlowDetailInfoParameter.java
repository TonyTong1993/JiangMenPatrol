package com.ecity.cswatersupply.network.request;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

//工单详情－流程信息－流程信息详情
public class GetWorkOrderFlowDetailInfoParameter implements IRequestParameter {
    private String gid;
    private String type;

    public GetWorkOrderFlowDetailInfoParameter(String gid, String type) {
        this.gid = gid;
        this.type = type;
    }
    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", type);
        map.put("gid", gid);
        return map;
    }
}
