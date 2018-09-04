package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
//工单详情－流程信息－流程信息详情
public class WorkOrderDetailFlowInfoDetailParameter implements IRequestParameter {
    private String processinstanceid;
    private String type;

    public WorkOrderDetailFlowInfoDetailParameter(String processinstanceid,String type) {
        this.processinstanceid = processinstanceid;
        this.type = type;
    }
    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", type);
        map.put("processinstanceid", processinstanceid);
        return map;
    }
}
