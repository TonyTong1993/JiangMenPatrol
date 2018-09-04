package com.ecity.cswatersupply.network.request;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

//工单详情－流程信息－流程信息详情
public class GetWorkOrderLogInfoParameter implements IRequestParameter {
    private String processInstanceId;

    public GetWorkOrderLogInfoParameter(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("processinstanceid", processInstanceId);
        return map;
    }
}
