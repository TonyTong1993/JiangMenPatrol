package com.ecity.cswatersupply.network.request;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

public class UpdatePatrolManStateParameter implements IRequestParameter {
    private boolean isLogin;

    public UpdatePatrolManStateParameter(boolean isLogin) {
        this.isLogin = isLogin;
    }

    @Override
    public Map<String, String> toMap() {
        User user = HostApplication.getApplication().getCurrentUser();
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", user.getGid());
        map.put("isLogin", String.valueOf(isLogin));
        return map;
    }

}
