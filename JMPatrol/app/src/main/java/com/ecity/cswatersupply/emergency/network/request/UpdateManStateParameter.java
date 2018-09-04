package com.ecity.cswatersupply.emergency.network.request;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateManStateParameter implements IRequestParameter{
    private boolean isLogin;

    public UpdateManStateParameter(boolean isLogin) {
        this.isLogin = isLogin;
    }

    @Override
    public Map<String, String> toMap() {
        User user = HostApplication.getApplication().getCurrentUser();
        Map<String, String> map = new HashMap<String, String>();
        map.put("userID", user.getId());
        if(isLogin) {
            map.put("state", "1");
        } else {
            map.put("state", "0");
        }
        return map;
    }
}
