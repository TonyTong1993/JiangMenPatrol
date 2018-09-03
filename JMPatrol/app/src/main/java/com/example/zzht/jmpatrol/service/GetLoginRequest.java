package com.example.zzht.jmpatrol.service;

import com.ecity.android.httpexecutor.AbstractBaseRequest;
import com.ecity.android.httpexecutor.AbstractRequestCallback;
import com.example.zzht.jmpatrol.global.ServiceUrlManager;
import com.example.zzht.jmpatrol.utils.MainUIEvent;

import java.util.HashMap;
import java.util.Map;

public class GetLoginRequest extends AbstractBaseRequest {
    private String username;
    private String password;

    public GetLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    @Override
    protected AbstractRequestCallback getAbstractRequestCallback() {
        AbstractRequestCallback callback = new AbstractRequestCallback() {
            @Override
            public boolean isFile() {
                return false;
            }

            @Override
            public String getFilePath() {
                return null;
            }

            @Override
            public boolean isPost() {
                return false;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getLoginUrl();
            }

            @Override
            public Map<String, String> getParameter() throws Exception {
                HashMap<String, String> map = new HashMap<>(16);
                map.put("username", username);
                map.put("password", password);
                map.put("sys", "mobile");
                map.put("isEncrypt", "true");
                return map;
            }

            @Override
            public int getEventId() {
                return MainUIEvent.GET_LOGIN_REQUEST;
            }
        };
        return callback;
    }
}
