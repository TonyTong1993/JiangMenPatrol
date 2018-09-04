package com.ecity.cswatersupply.emergency.service;

import org.json.JSONObject;

import com.ecity.android.httpforandroid.http.HttpRequestType;
import com.ecity.cswatersupply.emergency.utils.AnalyticalDataKnowledgeJsonUtil;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.network.ARequestCallback;
import com.ecity.cswatersupply.network.RequestExecutor;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.network.response.AServerResponse;
import com.ecity.cswatersupply.service.ServiceUrlManager;


public class KnowledgeBaseNetwork {

    /**
     * 知识库网络请求入口  应急预案 应急建议  地震常识
     */
    private static KnowledgeBaseNetwork instance;

    static {
        instance = new KnowledgeBaseNetwork();
    }

    private KnowledgeBaseNetwork() {

    }

    public static KnowledgeBaseNetwork getInstance() {
        return instance;
    }

    //emergency response plan
    public void getEmergencyeRsponse(final IRequestParameter parameter) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getEmergencyResponseUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return parameter;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return AnalyticalDataKnowledgeJsonUtil.emergencyResponseData(jsonObj);
            }

            @Override
            public int getEventId() {
                String type = parameter.toMap().get("type");
                switch (type) {
                    case "1":
                        return ResponseEventStatus.EMERGENCY_GET_KNOWBASE_EMERGENCY_PLAN;
                    case "2":
                        return ResponseEventStatus.EMERGENCY_GET_KNOWBASE_EMERGENCY_IDEA;
                    case "3":
                        return ResponseEventStatus.EMERGENCY_GET_KNOWBASE_EMERGENCY_COMMON;
                }
                return ResponseEventStatus.EMERGENCY_GET_KNOWBASE_EMERGENCY_PLAN;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }
        });
    }


}
