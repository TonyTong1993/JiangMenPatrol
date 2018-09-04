package com.ecity.cswatersupply.emergency.network.request;

import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetNoticeContentParameter implements IRequestParameter {

    private String gid;

    public GetNoticeContentParameter(String gid) {
        this.gid = gid;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("gid", gid);
        return map;
    }

}
