package com.ecity.cswatersupply.network.request;

import com.ecity.cswatersupply.network.RequestParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/19.
 */

public class GetWorkOrderFieldParameter implements RequestParameter.IRequestParameter {
    public GetWorkOrderFieldParameter(){}

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("plat", "mobile");
        return map;
    }
}
