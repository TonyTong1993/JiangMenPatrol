package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetProjectAllInfoParameter implements IRequestParameter {
    private int pageNo;
    private int pageSize;
    private String type;
    private String startTime;
    private String endTime;
    private String status;

    public GetProjectAllInfoParameter(String type, int pageNo, int pageSize, String startTime, String endTime, String status) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.type = type;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("pageno", String.valueOf(pageNo));
        map.put("pagesize", String.valueOf(pageSize));
        map.put("type", type);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("status", status);

        return map;
    }

}
