package com.ecity.cswatersupply.service;

import org.json.JSONObject;

import com.ecity.android.httpforandroid.http.HttpRequestType;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.network.ARequestCallback;
import com.ecity.cswatersupply.network.RequestExecutor;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.network.request.PunishStateEndingParameter;
import com.ecity.cswatersupply.network.request.PunishStatePrintingParameter;
import com.ecity.cswatersupply.network.response.AServerResponse;
import com.ecity.cswatersupply.utils.JsonUtil;

public class PunishStateService {
    private static PunishStateService instance;
    static {
        instance = new PunishStateService();
    }

    private PunishStateService() {

    }

    public static PunishStateService getInstance() {
        return instance;
    }

    public void postPunishState(final String url, final String eventID) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.POST;
            }

            @Override
            public String getUrl() {
                return url;
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new PunishStatePrintingParameter(eventID);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parsePunishStateChange(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PUNISHMENT_STATE_CHANGE;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

        });
    }

    public void postPunishState(final String url, final String eventID, final String amount) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.POST;
            }

            @Override
            public String getUrl() {
                return url;
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new PunishStateEndingParameter(eventID, amount);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PUNISHMENT_STATE_CHANGE;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parsePunishStateChange(jsonObj);
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }
}
