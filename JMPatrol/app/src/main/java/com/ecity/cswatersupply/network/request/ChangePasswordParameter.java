package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class ChangePasswordParameter implements IRequestParameter {
    private User user;
    private String oldPassword;
    private String newPassword;
    private String json;

    public ChangePasswordParameter(String json, User user, String oldPassword, String newPassword) {
        this.user = user;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.json = json;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("_type", json);
        map.put("username", user.getLoginName());
        map.put("oldpsw", oldPassword);
        map.put("newpsw", newPassword);

        return map;
    }
}
