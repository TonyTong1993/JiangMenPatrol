package com.ecity.cswatersupply.menu;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.Content;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskLinePart;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;
import com.ecity.cswatersupply.network.request.ReportLineInPlaceParameter;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.ui.activities.planningtask.PlanningTaskManager;
import com.ecity.cswatersupply.utils.DateUtil;

public class LinePartInPlaceFeedbackOperator extends EventReportOperator1 {
    public static final String INTENT_KEY_TASK_ID = "INTENT_KEY_TASK_ID";
    public static final String INTENT_KEY_LINE_PART = "INTENT_KEY_LINE_PART";
    public static final String INTENT_KEY_LINE_LOCATION = "INTENT_KEY_LINE_LOCATION";
    public static final String INTENT_KEY_INSPECTITEMS = "INTENT_KEY_INSPECTITEMS";
    private Z3PlanTaskLinePart linePart;
    private int taskId;

    public LinePartInPlaceFeedbackOperator() {
    }

    @Override
    public List<InspectItem> getDataSource() {
        Intent intent = getActivity().getIntent();
        linePart = (Z3PlanTaskLinePart) intent.getExtras().getSerializable(INTENT_KEY_LINE_PART);
        taskId = intent.getExtras().getInt(INTENT_KEY_TASK_ID);
        for (Content c : SessionManager.contentsList) {
            if (linePart.getEtypeid().equalsIgnoreCase(c.getEtypeid())) {
                SessionManager.content = c;
            }
        }
        if (SessionManager.content == null) {
            return null;
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
        return new ReportLineInPlaceParameter(user, taskId, linePart);
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
        updateLineFeedBackStatus();
        seedUiEvent();
        notifyBackEvent(activity);
    }

    private void deleteInspeTempInfo() {
        ArrayList<InspectItem> items = (ArrayList<InspectItem>) SessionManager.content.getInspectItems();
        for (InspectItem item : items) {
            item.setValue("");
        }
    }

    private void updateLineFeedBackStatus() {
        String currentTime = DateUtil.getCurrentTime();
        PlanningTaskManager.getInstance().updateLineFeedbackStatus(true, currentTime, linePart.getGid(), taskId);
    }

    private void seedUiEvent() {
        UIEvent event = new UIEvent(UIEventStatus.PLANNINGTASK_LINE_FEEDBACK_NOTIFICATION);
        linePart.setFeedBack(true);
        event.setData(linePart);
        EventBusUtil.post(event);
    }
}
