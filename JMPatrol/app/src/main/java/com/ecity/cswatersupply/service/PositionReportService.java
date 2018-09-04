package com.ecity.cswatersupply.service;

import java.util.List;

import com.ecity.android.httpforandroid.http.HttpRequestType;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.GPSPositionBean;
import com.ecity.cswatersupply.network.ARequestCallback;
import com.ecity.cswatersupply.network.RequestExecutor;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.network.request.PostPositionsParameter;
import com.ecity.cswatersupply.network.response.AServerResponse;

public class PositionReportService {
    private static PositionReportService instance;
    static {
        instance = new PositionReportService();
    }

    private PositionReportService() {

    }

    public static PositionReportService getInstance() {
        return instance;
    }

    public void reportPosition(final List<GPSPositionBean> reportBeanList) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.POST;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getReportPositionUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new PostPositionsParameter(reportBeanList);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.SUBMIT_POSITIONS_DONE;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }
}
