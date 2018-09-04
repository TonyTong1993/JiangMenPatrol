package com.ecity.cswatersupply.service;

import org.json.JSONObject;

import com.ecity.android.httpforandroid.http.HttpRequestType;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.network.ARequestCallback;
import com.ecity.cswatersupply.network.RequestExecutor;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.network.request.QueryNetNodeParameter;
import com.ecity.cswatersupply.network.response.AServerResponse;

public class NetService {
    private volatile static NetService instance;

    private NetService() {
    }

    public static NetService getInstance() {
        if (instance == null) {
            synchronized (NetService.class) {
                if (instance == null) {
                    instance = new NetService();
                }
            }
        }
        return instance;
    }

    public void queryNode(final String url, final String where, final boolean returnGeometry) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return url + "/query";
            }

            @Override
            public IRequestParameter prepareParameters() {
                QueryNetNodeParameter parameter = new QueryNetNodeParameter();
                parameter.setReturnGeometry(returnGeometry);
                parameter.setWhere(where);
                return parameter;
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.NET_SERVER_QUERY_NODE;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return jsonObj;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }
}
