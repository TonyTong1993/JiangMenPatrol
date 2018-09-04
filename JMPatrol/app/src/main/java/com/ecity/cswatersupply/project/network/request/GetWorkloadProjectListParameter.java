package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetWorkloadProjectListParameter implements IRequestParameter {
    private int pageNo;
    private int pageSize;
    private String filter;
    private String startTime;
    private String endTime;
    private int status;

    public GetWorkloadProjectListParameter(String filter, int pageNo, int pageSize, String startTime, String endTime, int status) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.filter = filter;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(pageSize));
        map.put("filter", filter);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("status", String.valueOf(status));

        return map;
    }
}
