package com.ecity.cswatersupply.service;

import org.json.JSONObject;

import com.ecity.android.httpforandroid.http.HttpRequestType;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.network.ARequestCallback;
import com.ecity.cswatersupply.network.RequestExecutor;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.network.request.WorkOrderOperationLogParameter;
import com.ecity.cswatersupply.network.response.AServerResponse;
import com.ecity.cswatersupply.utils.JsonUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrder;

/**
 * 获取工单详情信息
 * 
 * @author jiangqiwei
 * 
 */
public class WorkOrderOperationLogService {
    private static WorkOrderOperationLogService instance;
    static {
        instance = new WorkOrderOperationLogService();
    }

    private WorkOrderOperationLogService() {

    }

    public static WorkOrderOperationLogService getInstance() {
        return instance;
    }

    public void getLogDateBacks(final WorkOrder workOrder) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getWorkOrderOperationLogUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new WorkOrderOperationLogParameter(workOrder);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseLogList(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WORKORDER_OPERATION_LOGS;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }
    
   
}
