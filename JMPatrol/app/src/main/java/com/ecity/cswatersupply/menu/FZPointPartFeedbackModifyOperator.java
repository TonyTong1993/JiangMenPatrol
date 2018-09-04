package com.ecity.cswatersupply.menu;

import android.os.Bundle;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTask;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPointPart;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;
import com.ecity.cswatersupply.network.request.ReportPointInPlaceParameter;
import com.ecity.cswatersupply.network.response.eventreport.EventReportResponse;
import com.ecity.cswatersupply.service.ReportEventService;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.ui.activities.planningtask.PlanningTaskManager;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.FZInspectItemUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

import java.util.ArrayList;
import java.util.List;

public class FZPointPartFeedbackModifyOperator extends EventReportOperator1 {
    public static final String INTENT_KEY_TASK = "INTENT_KEY_TASK";
    public static final String INTENT_KEY_POINT_PART = "INTENT_KEY_POINT_PART";
    private Z3PlanTaskPointPart pointPart;
    private Z3PlanTask task;
    private int taskId;

    private void handleIntent() {
        Bundle bundle = getActivity().getIntent().getExtras();
        pointPart = (Z3PlanTaskPointPart) bundle.getSerializable(INTENT_KEY_POINT_PART);
        task = (Z3PlanTask) bundle.getSerializable(INTENT_KEY_TASK);
        taskId = task.getTaskid();
    }

    @Override
    public List<InspectItem> getDataSource() {
        EventBusUtil.register(this);
        SessionManager.reportType = 1;
        handleIntent();

        ReportEventService.getInstance().getFZModifyReportForms(pointPart.getGroupid(), String.valueOf(pointPart.getTpointid()));
        return null;
    }

    @Override
    protected String getServiceUrl() {
        return ServiceUrlManager.getInstance().getInPlaceFeedBackUrl();
    }

    @Override
    protected AReportInspectItemParameter getRequestParameter() {
        User user = HostApplication.getApplication().getCurrentUser();
        return new ReportPointInPlaceParameter(user, taskId, pointPart);
    }

    @Override
    protected String getFunctionKey() {
        return "CHECKREPORT";
    }

    @Override
    public void notifyBackEvent(CustomReportActivity1 activity) {
        if (null != activity) {
            EventBusUtil.unregister(this);
            activity.finish();
        }
    }

    @Override
    public void notifyBackEventWhenFinishReport(CustomReportActivity1 activity) {
        //取消检查项缓存信息
        deleteInspeTempInfo();
        seedUiEvent();
        notifyBackEvent(activity);
    }

    private void deleteInspeTempInfo() {
        ArrayList<InspectItem> items = (ArrayList<InspectItem>) SessionManager.content.getInspectItems();
        for (InspectItem item : items) {
            resetValues(item);
        }
    }

    private void resetValues(InspectItem item) {

        if (null == item) {
            return;
        }

        if (item.getType() == EInspectItemType.GROUP && !ListUtil.isEmpty(item.getChilds())) {
            for (InspectItem tmpItem : item.getChilds()) {
                resetValues(tmpItem);
            }
        } else {
            item.setValue("");
        }
    }

    private void seedUiEvent() {
        UIEvent event = new UIEvent(UIEventStatus.PLANNINGTASK_POINT_FEEDBACK_NOTIFICATION);
        pointPart.setFeedBack(true);
        event.setData(pointPart);
        EventBusUtil.post(event);
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.EVENT_GET_FZ_MODIFY_INSPECTS:
                EventReportResponse response = event.getData();
                handleResponse(response);
                break;
            default:
                break;
        }
    }

    private void handleResponse(EventReportResponse response) {
        List<InspectItem> items = response.getItems();
        InspectItem item = new InspectItem();
        item.setName(pointPart.getGroupid());
        item.setAlias(response.getTableName());
        item.setType(EInspectItemType.GROUP);
        item.setVisible(true);
        item.setChilds(items);
        if(null == mInspectItems) {
            mInspectItems = new ArrayList<>();
        }
        mInspectItems.add(item);
        SessionManager.currentPointPartIntMapOpretor = pointPart;
        FZInspectItemUtil.setSatelliteInfo(mInspectItems);
        getActivity().fillDatas(mInspectItems);
    }

}
