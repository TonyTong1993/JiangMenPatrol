package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.network.RequestParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/30.
 */

public class GetWaterMeterListParameter implements RequestParameter.IRequestParameter {
    private int pageNo;
    private int pageSize;
    private String type; //项目状态
    private String status; //状态，支持超期
    private String startTime;
    private String endTime;

    public GetWaterMeterListParameter(int pageNo, int pageSize, String type, String status, String startTime, String endTime) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.type = type;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(pageSize));
        if (type.equals("0")) {
            map.put("type", "");
        } else {
            map.put("type", type);
        }
        map.put("status", status);
        map.put("createtime0", startTime);
        map.put("createtime1", endTime);

        return map;
    }
}
