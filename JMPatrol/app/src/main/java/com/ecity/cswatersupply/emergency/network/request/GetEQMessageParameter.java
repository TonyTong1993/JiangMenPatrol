package com.ecity.cswatersupply.emergency.network.request;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class GetEQMessageParameter implements IRequestParameter {

    public GetEQMessageParameter() {
    }

    @Override
    public Map<String, String> toMap() {
        User currentUser = HostApplication.getApplication().getCurrentUser();
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", currentUser.getId());
        return map;
    }
}
