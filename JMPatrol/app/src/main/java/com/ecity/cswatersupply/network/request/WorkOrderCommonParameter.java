package com.ecity.cswatersupply.network.request;

import java.util.Map;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

/**
 * 工单通用上报参数
 * 
 * @author gaokai
 *
 */
public class WorkOrderCommonParameter implements IRequestParameter {
    private Map<String, String> params;

    public WorkOrderCommonParameter(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public Map<String, String> toMap() {
        params.put("userid", HostApplication.getApplication().getCurrentUser().getId());
        return params;
    }
}
