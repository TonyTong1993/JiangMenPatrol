package com.ecity.cswatersupply.menu;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.Content;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTask;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPointPart;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;
import com.ecity.cswatersupply.network.request.ReportPointInPlaceParameter;
import com.ecity.cswatersupply.project.FuZhouPatrolModuleConfig;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.ui.activities.planningtask.PlanningTaskManager;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.FZInspectItemUtil;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.z3app.android.util.StringUtil;
import com.z3pipe.mobile.android.corssdk.model.EQulityType;
import com.z3pipe.mobile.android.corssdk.model.SatelliteInfo;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

public class PointPartInPlaceFeedbackOperator extends EventReportOperator1 {
    public static final String INTENT_KEY_TASK = "INTENT_KEY_TASK";
    public static final String INTENT_KEY_POINT_PART = "INTENT_KEY_POINT_PART";
    public static final String INTENT_KEY_POINT_LOCATION = "INTENT_KEY_POINT_LOCATION";
    public static final String INTENT_KEY_INSPECTITEMS = "INTENT_KEY_INSPECTITEMS";
    private Z3PlanTaskPointPart pointPart;
    private Z3PlanTask task;
    private int taskId;

    public PointPartInPlaceFeedbackOperator() {
    }

    private void handleIntent() {
        Bundle bundle = getActivity().getIntent().getExtras();
        pointPart = (Z3PlanTaskPointPart) bundle.getSerializable(INTENT_KEY_POINT_PART);
        task = (Z3PlanTask) bundle.getSerializable(INTENT_KEY_TASK);
        taskId = task.getTaskid();
    }

    @Override
    public List<InspectItem> getDataSource() {
        SessionManager.reportType = 1;
        handleIntent();
        SessionManager.content = null;
        if (!StringUtil.isBlank(String.valueOf(task.getGroupid())) && !"0".equals(String.valueOf(task.getGroupid()))) {
            for (Content c : SessionManager.contentsList) {
                if (pointPart.getGroupid().equals(c.getGroupid())) {
                    SessionManager.content = c;
                }
            }
        } else if (!StringUtil.isBlank(pointPart.getEquipid())) {
            for (Content c : SessionManager.contentsList) {
                if (pointPart.getEtypeid().equalsIgnoreCase(c.getEtypeid())) {
                    SessionManager.content = c;
                }
            }
        }
        if (SessionManager.content == null) {
            return null;
        }
        try {
            if (FuZhouPatrolModuleConfig.getConfig().isModuleUseable()) {
                SessionManager.currentPointPartIntMapOpretor = pointPart;
                FZInspectItemUtil.setSatelliteInfo(SessionManager.content.getInspectItems());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return SessionManager.content.getInspectItems();
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
        //保存点反馈的状态到本地数据
        updatePointFeedBackStatus();
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

    private void updatePointFeedBackStatus() {
        String currentTime = DateUtil.getCurrentTime();
        PlanningTaskManager.getInstance().updatePointFeedbackStatus(true, currentTime, pointPart.getGid(), taskId);
    }

    private void seedUiEvent() {
        UIEvent event = new UIEvent(UIEventStatus.PLANNINGTASK_POINT_FEEDBACK_NOTIFICATION);
        pointPart.setFeedBack(true);
        event.setData(pointPart);
        EventBusUtil.post(event);
    }

}
