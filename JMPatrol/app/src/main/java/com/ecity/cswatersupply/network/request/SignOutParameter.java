package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
/*签退参数拼接*/
public class SignOutParameter implements IRequestParameter {
    private User user;

    public SignOutParameter(User user) {
        this.user = user;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", user.getGid());
        return map;
    }

}
