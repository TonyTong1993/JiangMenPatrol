package com.ecity.cswatersupply.menu;

import java.util.List;

import android.content.Intent;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.event.Event;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.ui.activities.MainActivity;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;

public class Event2WorkOrderOperator extends ACommonReportOperator1 {
    public static final String INTENT_KEY_EVENT = "INTENT_KEY_EVENT";
    public static final String INTENT_KEY_TASK_X = "INTENT_KEY_TASK_X";
    public static final String INTENT_KEY_TASK_Y = "INTENT_KEY_TASK_Y";
    public static final String INTENT_KEY_TASK_ADDRESS = "INTENT_KEY_TASK_ADDRESS";

    private Event event;
    private List<InspectItem> inspectItems;

    public Event2WorkOrderOperator() {
    }

    @Override
    public List<InspectItem> getDataSource() {
        EventBusUtil.register(this);
        Intent intent = getActivity().getIntent();
        event = (Event) intent.getExtras().getSerializable(INTENT_KEY_EVENT);
        LoadingDialogUtil.show(getActivity(), ResourceUtil.getStringById(R.string.page_loading));
        WorkOrderService.instance. getEvent2WorkOrderForm(event.getId());

        return null;
    }

    public void notifyBackEvent(CustomReportActivity1 activity) {
        finishActivity(activity);
    }

    public void submit2Server(List<InspectItem> datas) {
        LoadingDialogUtil.show(getActivity(), R.string.uploading_now);
        EventBusUtil.register(this);
        LoadingDialogUtil.setCancelable(false);
        User reporter = HostApplication.getApplication().getCurrentUser();
        WorkOrderService.instance.submitEvent2WorkOrderForm(reporter, event, inspectItems);
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            UIEvent uiEvent = new UIEvent(UIEventStatus.TOAST, event.getMessage(), null);
            uiEvent.setTargetClass(MainActivity.class);
            EventBusUtil.post(uiEvent);
            if (ResponseEventStatus.WORKORDER_GET_EVENT_2_WORK_ORDER_FORM == event.getId()) {
                finishActivity(getActivity());
            }
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.WORKORDER_GET_EVENT_2_WORK_ORDER_FORM:
                processGetEvent2WorkOrderFormResponse(event);
                break;
            case ResponseEventStatus.WORKORDER_SUBMIT_EVENT_2_WORK_ORDER_FORM:
                processSubmitEvent2WorkOrderFormResponse(event);
                break;
            default:
                break;
        }
    }

    private void processGetEvent2WorkOrderFormResponse(ResponseEvent event) {
        inspectItems = event.getData();
        getActivity().fillDatas(inspectItems);
        EventBusUtil.unregister(this);
        LoadingDialogUtil.dismiss();
    }

    private void processSubmitEvent2WorkOrderFormResponse(ResponseEvent event) {
        EventBusUtil.unregister(this);
        LoadingDialogUtil.dismiss();
        UIEvent uiEvent = new UIEvent(UIEventStatus.TOAST, getActivity().getString(R.string.event_management_to_work_order_success), null);
        uiEvent.setTargetClass(MainActivity.class);
        EventBusUtil.post(uiEvent);
        getActivity().finish();
    }

    private void finishActivity(CustomReportActivity1 activity) {
        LoadingDialogUtil.dismiss();
        EventBusUtil.unregister(this);
        activity.finish();
    }
}
