package com.ecity.cswatersupply.menu;

import android.os.Handler;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.FileUploader;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;
import com.ecity.cswatersupply.network.request.ReportTaskFormParameter;
import com.ecity.cswatersupply.network.response.eventreport.EventReportResponse;
import com.ecity.cswatersupply.service.ReportEventService;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.utils.CacheManager;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.z3app.android.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ValveReportOperator extends EventReportOperator1 {

    @Override
    protected String getServiceUrl() {
        return ServiceUrlManager.getInstance().getReportValveFormUrl();
    }

    @Override
    protected String getReportImageUrl() {
        return ServiceUrlManager.getInstance().getReportValveImageUrl();
    }

    @Override
    protected AReportInspectItemParameter getRequestParameter() {
        return new ReportTaskFormParameter(getInspectItems());
    }

    @Override
    protected void uploadForm(List<String> filePaths, String url, Map<String, String> parameters, Handler handler) {
        LoadingDialogUtil.show(getActivity(), R.string.uploading_now);
        //先更新数据库中的阀门数据
        ReportEventService.getInstance().updateValveStates(getInspectItems());
    }

    @Override
    protected int getRequestId() {
        return ResponseEventStatus.EVENT_REPORT_GET_VALVE_SWITCH;
    }

    @Override
    protected void requestDataSource() {
        ReportEventService.getInstance().getValveSwitchForm();
    }

    @Override
    public void notifyBackEvent(final CustomReportActivity1 activity) {
        super.notifyBackEvent(activity);
    }

    public void onEventMainThread(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        if (!event.isOK()) {
            ToastUtil.showLong(event.getMessage());
            return;
        }

        if(event.getId() == getRequestId()) {
            super.onEventMainThread(event);
        } else {
            if (event.getId() == ResponseEventStatus.EVENT_UPDATE_VALVE_INFO) {
                LoadingDialogUtil.show(getActivity(), R.string.uploading_now);
                //上传表单
                FileUploader.execute(new ArrayList<String>(), getServiceUrl(), getRequestParameter().toMap(), getEventHandler());
            }
        }
    }
}
