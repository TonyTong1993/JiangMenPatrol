package com.example.zzht.jmpatrol.services;

import com.ecity.android.httpforandroid.http.HttpRequestType;
import com.example.zzht.jmpatrol.event.ResponseEventStatus;
import com.example.zzht.jmpatrol.global.ServiceUrlManager;
import com.example.zzht.jmpatrol.network.ARequestCallback;
import com.example.zzht.jmpatrol.network.RequestExecutor;
import com.example.zzht.jmpatrol.network.RequestParameter.*;
import com.example.zzht.jmpatrol.network.reponse.LoginResponse;

/**
 * Author by zzht, Email tongwanhua1993@163.com, Date on 2018/9/3.
 * PS: Not easy to write code, please indicate.
 */
public class UserService {
    static void login(String account,String password) {
        RequestExecutor.execute(new ARequestCallback<LoginResponse>() {
            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return null;
            }

            @Override
            public IRequestParameter prepareParameters() {
                return null;
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.LoginEvent;
            }

            @Override
            public Class<LoginResponse> getResponseClass() {
                return LoginResponse.class;
            }
        });
    }

}
