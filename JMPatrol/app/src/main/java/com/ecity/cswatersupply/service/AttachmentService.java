package com.ecity.cswatersupply.service;

import org.json.JSONObject;

import com.ecity.android.httpforandroid.http.HttpRequestType;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.network.ARequestCallback;
import com.ecity.cswatersupply.network.RequestExecutor;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.network.request.SimpleParameter;
import com.ecity.cswatersupply.network.response.AServerResponse;
import com.ecity.cswatersupply.utils.JsonUtil;

public enum AttachmentService {

    instance;

    AttachmentService() {

    }

    public void getAttachments(final String workOrderId, final String tableName) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getAttachmentUrl(tableName, workOrderId);
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new SimpleParameter();
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_QUERY_ATTACHMENT;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return null;
            }      
            
            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseImageModels(jsonObj);
            }
        });
    }
}
