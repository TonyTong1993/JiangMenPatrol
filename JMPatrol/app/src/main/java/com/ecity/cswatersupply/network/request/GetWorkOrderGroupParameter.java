package com.ecity.cswatersupply.network.request;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetWorkOrderGroupParameter implements IRequestParameter {
    private String userid;
    private String groupid;

    public GetWorkOrderGroupParameter(String userid, String groupid) {
        this.userid = userid;
        this.groupid = groupid;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("groupid",groupid);
        return map;
    }
}
