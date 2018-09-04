package com.ecity.cswatersupply.service;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.httpforandroid.http.HttpRequestType;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.ARequestCallback;
import com.ecity.cswatersupply.network.RequestExecutor;
import com.ecity.cswatersupply.network.RequestParameter;
import com.ecity.cswatersupply.network.request.PlanningTaskParameter;
import com.ecity.cswatersupply.network.request.UpdatePatrolManStateParameter;
import com.ecity.cswatersupply.network.response.AServerResponse;
import com.ecity.cswatersupply.utils.ResourceUtil;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/7/13.
 */

public class PatrolService {
    private volatile static PatrolService instance;

    private PatrolService() {
    }

    public static PatrolService getInstance() {
        if (instance == null) {
            synchronized (PatrolService.class) {
                if (instance == null) {
                    instance = new PatrolService();
                }
            }
        }
        return instance;
    }

    public void updatePatrolManState(final boolean isLogin) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public RequestParameter.IRequestParameter prepareParameters() {
                return new UpdatePatrolManStateParameter(isLogin);
            }

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.UPDATE_PATROL_MAN_STATE;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().updatePatrolManStateUrl();
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return jsonObj;
            }
        });
    }
}
