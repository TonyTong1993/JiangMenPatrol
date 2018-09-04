package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.workorder.model.WorkOrder;

public class WorkOrderDetailFlowInfoParameter implements IRequestParameter {
    private WorkOrder workOrder;

    public WorkOrderDetailFlowInfoParameter(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        
        map.put("processinstanceid", workOrder.getAttribute(WorkOrder.KEY_ID));
        
        return map;
    }
}
