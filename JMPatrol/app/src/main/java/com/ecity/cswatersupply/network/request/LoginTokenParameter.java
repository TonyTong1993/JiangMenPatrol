package com.ecity.cswatersupply.network.request;

import com.ecity.cswatersupply.network.RequestParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * Author by zzht, Email tongwanhua1993@163.com, Date on 2018/9/4.
 * PS: Not easy to write code, please indicate.
 */
public class LoginTokenParameter implements RequestParameter.IRequestParameter {
    private String account;
    private String password;

    public LoginTokenParameter(String account, String password) {
        this.account = account;
        this.password = password;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("_type", "json");
        map.put("loginName",account);
        map.put("password", password);
        map.put("sys", "android");
        return map;
    }
}
