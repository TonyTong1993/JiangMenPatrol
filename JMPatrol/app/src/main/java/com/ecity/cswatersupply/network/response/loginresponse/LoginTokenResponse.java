package com.ecity.cswatersupply.network.response.loginresponse;

import com.ecity.cswatersupply.network.response.AServerResponse;


/**
 * Author by zzht, Email tongwanhua1993@163.com, Date on 2018/9/4.
 * PS: Not easy to write code, please indicate.
 */
public class LoginTokenResponse extends AServerResponse {
    private String token;

    public LoginTokenResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
