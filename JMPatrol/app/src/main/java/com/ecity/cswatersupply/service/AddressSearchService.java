package com.ecity.cswatersupply.service;

import org.json.JSONObject;

import com.ecity.android.httpforandroid.http.HttpRequestType;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.network.ARequestCallback;
import com.ecity.cswatersupply.network.RequestExecutor;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.network.response.AServerResponse;
import com.ecity.cswatersupply.utils.JsonUtil;

public class AddressSearchService {
    private volatile static AddressSearchService instance;

    private AddressSearchService() {
    }

    public static AddressSearchService getInstance() {
        if (instance == null) {
            synchronized (AddressSearchService.class) {
                if (instance == null) {
                    instance = new AddressSearchService();
                }
            }
        }
        return instance;
    }

    public void getPlanAndPipeAddress(final IRequestParameter parameter) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {
            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getPlanAndPipeAddress();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return parameter;
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.MAP_GET_PLAN_AND_PIPE_ADDRESS;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parsePlanAndPipeAddress(jsonObj);
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

        });
    }

}