package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.workorder.model.WorkOrder;

public class InspectItemsParameter implements IRequestParameter {
    private String processInstanceId;

    public InspectItemsParameter(String processInstanceId){
        this.processInstanceId = processInstanceId;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(WorkOrder.KEY_ID, processInstanceId);
        return map;
    }

}
