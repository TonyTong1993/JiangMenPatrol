package com.ecity.cswatersupply.ui.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.adapter.EventListAdapter;
import com.ecity.cswatersupply.adapter.PunishListAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.menu.Event2TaskOperator;
import com.ecity.cswatersupply.menu.Event2WorkOrderOperator;
import com.ecity.cswatersupply.menu.EventReportOperator1;
import com.ecity.cswatersupply.menu.PunishDetailOperator;
import com.ecity.cswatersupply.model.event.ConstructionEvent;
import com.ecity.cswatersupply.model.event.Event;
import com.ecity.cswatersupply.model.event.EventType;
import com.ecity.cswatersupply.model.event.GISReportEvent;
import com.ecity.cswatersupply.model.event.MeasureEvent;
import com.ecity.cswatersupply.model.event.PatrolEvent;
import com.ecity.cswatersupply.model.event.PointLeakageEvent;
import com.ecity.cswatersupply.model.event.PumpEvent;
import com.ecity.cswatersupply.model.event.PunishmentEvent;
import com.ecity.cswatersupply.model.event.RepairementEvent;
import com.ecity.cswatersupply.service.PunishStateService;
import com.ecity.cswatersupply.service.ReportEventService;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.fragment.TodoFragment;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.ACache;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.JsonUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.xg.util.NotificationUtil;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;
import com.zzz.ecity.android.applibrary.dialog.AlertView;

import org.json.JSONArray;
import org.json.JSONObject;

public class EventListActivity extends BaseActivity {
    public static final String INTENT_KEY_TITLE_ID = "INTENT_KEY_TITLE_ID";
    public static final String INTENT_KEY_EVENT_TYPE = "INTENT_KEY_EVENT_TYPE";
    public static final String INTENT_KEY_TASK_ID = "INTENT_KEY_TASK_ID";
    public PullToRefreshListView refreshPunishListView;
    private Map<String, String> statusMap;
    private CustomTitleView titleView;
    private ListView lvRecords;
    private BaseAdapter adapter;
    private List<Event> listData;
    private int titleId;
    private int eventType;
    private int taskid;
    private String nextStepId;
    private EventListAdapter.IEventOperationActionListener eventOperationActionListener;
    private Map<Integer, Integer> titleMap;
    private Event mEvent;
    private String eventIdFromNotification;
    private int currentPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punishment_report);
        EventBusUtil.register(this);
        getEventIdFromCache();
        listData = new ArrayList<Event>();
        statusMap = new HashMap<String, String>();
        initData();
        initUI();
        bindEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getEventIdFromCache();
        if(-1 == currentPosition) {
            requestEventList();
        } else {
            lvRecords.setSelection(currentPosition);
        }
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        currentPosition = -1;
        super.onDestroy();
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    public void onActionButtonClicked(View v) {
        SessionManager.reportType = 0;
        gotoNewEventScreen(titleId, eventType);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        titleId = bundle.getInt(INTENT_KEY_TITLE_ID, 0);
        eventType = bundle.getInt(INTENT_KEY_EVENT_TYPE, -1);
        taskid = bundle.getInt(INTENT_KEY_TASK_ID, 0);
        if(bundle.containsKey(Constants.INTENT_KEY_NEXT_STEP_ID)) {
            nextStepId = getIntent().getStringExtra(Constants.INTENT_KEY_NEXT_STEP_ID);
        }

        titleMap = new HashMap<>();
        titleMap.put(0, R.string.event_management_type_repair_tilte);
        titleMap.put(1, R.string.event_management_type_leak_tilte);
        titleMap.put(2, R.string.event_management_type_punishment_tilte);
        titleMap.put(3, R.string.event_management_type_construction_tilte);
        titleMap.put(4, R.string.event_management_type_pump_tilte);
        titleMap.put(5, R.string.event_management_type_measure_tilte);
        titleMap.put(6, R.string.event_management_type_patrol_tilte);
        titleMap.put(7, R.string.event_management_type_gis_tilte);

        if(-1 == eventType && !StringUtil.isBlank(nextStepId)) {
            eventType = Integer.valueOf(nextStepId.split(",")[1]);
        }
    }

    private void initUI() {
        titleView = (CustomTitleView) findViewById(R.id.view_tile_punishment_report);
        if(0 == titleId){
            titleId = titleMap.get(eventType);
        }
        titleView.setTitleText(titleId);
        titleView.setBtnStyle(CustomTitleView.BtnStyle.RIGHT_ACTION);
        titleView.setRightActionBtnText(R.string.punishment_report_title_rightaction);
        titleView.tv_rightSingle.setBackground(ResourceUtil.getDrawableResourceById(R.drawable.selector_titleview_titletxt_shape));
        refreshPunishListView = (PullToRefreshListView) findViewById(R.id.prlv_punishment);
        refreshPunishListView.setLastUpdateTime();
        refreshPunishListView.doPullRefreshing(true, 500);
        lvRecords = refreshPunishListView.getRefreshableView();
        lvRecords.setDivider(ResourceUtil.getDrawableResourceById(R.drawable.shape_list_divider));
        lvRecords.setDividerHeight(ResourceUtil.getDimensionPixelSizeById(R.dimen.margin_spacing_level_4));
    }

    private void gotoNewEventScreen(int title, int eventType) {
        Bundle bundle = new Bundle();
        bundle.putInt(CustomViewInflater.REPORT_TITLE, title);
        bundle.putString(CustomViewInflater.REPORT_COMFROM, EventReportOperator1.class.getName());
        bundle.putInt(CustomViewInflater.EVENTTYPE, eventType);
        bundle.putInt(Constants.PLAN_TASK_ID, taskid);
        UIHelper.startActivityWithExtra(CustomMainReportActivity1.class, bundle);
    }

    public void onEventMainThread(UIEvent event) {
        switch (event.getId()) {
            case UIEventStatus.PUNISHSTATE_NOTIFICATION:
                refreshPunishListView.doPullRefreshing(true, 500);
                break;
            case UIEventStatus.PUNISHSTATE_STATUS_REPORTING:
                int positionReport = event.getData();
                showDialog(positionReport, false, PunishmentEvent.STATUS_REPORTING);
                break;
            case UIEventStatus.PUNISHSTATE_STATUS_PRINTING:
                int positionPrinting = event.getData();
                showDialog(positionPrinting, true, PunishmentEvent.STATUS_PRINTING);
                break;
            case UIEventStatus.PUNISH_EVENT_CLOSE:
                Event closeEvent = event.getData();
                showCloseDialog(closeEvent.getAttribute(Event.ATTR_KEY_ID));
                break;
            case UIEventStatus.EVENT_ADDRESS_ON_CLICKED:
                currentPosition = event.getData();
                break;
            case UIEventStatus.EVENT_REPORT_FINISHED:
                currentPosition = -1;
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.EVENT_REPORT_GET_EVENT_LIST:
                Map<String, Object> mapEvents = event.getData();
                listData = (List<Event>) mapEvents.get("events");
                adapter = getEventAdatper(event);
                lvRecords.setAdapter(adapter);
                refreshPunishListView.onPullDownRefreshComplete();
                refreshPunishListView.onPullUpRefreshComplete();
                refreshPunishListView.setLastUpdateTime();
                break;
            case ResponseEventStatus.EVENT_REPORT_CLOSE_EVENT:
                ToastUtil.showLong(R.string.event_management_close_success);
                adapter.notifyDataSetChanged();
                refreshPunishListView.doPullRefreshing(true, 500);
                break;
            case ResponseEventStatus.EVENT_REPORT_GET_WORK_ORDER_INFO:
                LoadingDialogUtil.dismiss();
                JSONObject workOrderInfo = event.getData();
                JSONArray features = workOrderInfo.optJSONArray("features");
                if ((features != null) && (features.length() > 0)) {
                    JSONObject feature = features.optJSONObject(0);
                    WorkOrder workOrder = JsonUtil.parseSingleWorkOrder(feature);
                    if (workOrder != null) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(WorkOrder.KEY_SERIAL, workOrder);
                        UIHelper.startActivityWithExtra(WorkOrderDetailFragmentActivity.class, bundle);
                    }
                } else {
                    ToastUtil.showShort(R.string.event_management_back_null_work_order);
                }
                break;
            case ResponseEventStatus.EVENT_GET_WORK_ORDER_FIELDS:
                getWorkOrderInfoWithEventId(mEvent.getId());
                break;
            case ResponseEventStatus.WORKORDER_SUBMIT_EVENT_2_WORK_ORDER_FORM:
                adapter.notifyDataSetChanged();
                refreshPunishListView.doPullRefreshing(true, 500);
                break;
            default:
                break;
        }
    }

    private void showDialog(int position, Boolean isEdit, int punishState) {
        TextView tv_title;
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.view_alertdialog);
        if (isEdit) {
            tv_title = (TextView) dialog.getWindow().findViewById(R.id.dialog_title);
            tv_title.setText(R.string.dialog_title_printing);
        } else {
            dialog.getWindow().findViewById(R.id.rl_edit).setVisibility(View.GONE);
            tv_title = (TextView) dialog.getWindow().findViewById(R.id.dialog_title);
            tv_title.setText(R.string.dialog_title_reporting);
        }
        setButtonClick(dialog, punishState, position);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    private void setButtonClick(AlertDialog dialog, int punishState, int position) {
        TextView positive = (TextView) dialog.getWindow().findViewById(R.id.punish_dialog_positive);
        TextView negative = (TextView) dialog.getWindow().findViewById(R.id.punish_dialog_negative);
        ButtonClick lisButtonClick = new ButtonClick(dialog, position, punishState);
        positive.setOnClickListener(lisButtonClick);
        negative.setOnClickListener(lisButtonClick);
    }

    private class ButtonClick implements OnClickListener {
        private Dialog dialog;
        int position;
        int punishState;

        public ButtonClick(Dialog dialog, int position, int punishState) {
            this.dialog = dialog;
            this.position = position;
            this.punishState = punishState;
        }

        @Override
        public void onClick(View v) {
            Event event = listData.get(position);
            PunishmentEvent punishEvent = (PunishmentEvent) event;
            String punishId = punishEvent.getAttribute(PunishmentEvent.ATTR_KEY_ID);
            switch (v.getId()) {
                case R.id.punish_dialog_positive:
                    switch (punishState) {
                        case PunishmentEvent.STATUS_REPORTING:
                            PunishStateService.getInstance().postPunishState(ServiceUrlManager.getInstance().getPrintPunishUrl(), String.valueOf(punishId));
                            dialog.dismiss();
                            UIEvent reportEvent = new UIEvent(UIEventStatus.PUNISHSTATE_NOTIFICATION);
                            EventBusUtil.post(reportEvent);
                            break;
                        case PunishmentEvent.STATUS_PRINTING:
                            EditText et_amount = (EditText) dialog.getWindow().findViewById(R.id.et_punish_pay);
                            if (StringUtil.isBlank(et_amount.getText().toString())) {
                                ToastUtil.showShort(ResourceUtil.getStringById(R.string.dialog_amount_blank));
                                scrollToPunishEventItem(position);
                                dialog.dismiss();
                            } else {
                                PunishStateService.getInstance().postPunishState(ServiceUrlManager.getInstance().getPunishTicketUrl(), String.valueOf(punishId),
                                        et_amount.getText().toString());
                                dialog.dismiss();
                                UIEvent printingEvent = new UIEvent(UIEventStatus.PUNISHSTATE_NOTIFICATION);
                                EventBusUtil.post(printingEvent);
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case R.id.punish_dialog_negative:
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    }

    private void scrollToPunishEventItem(int position) {
        PunishmentEvent currentPunishEvent = (PunishmentEvent) listData.get(position);
        boolean isPunishEventFound = false;
        int index = 0;
        for (index = 0; index < listData.size(); index++) {
            Event event = listData.get(index);
            PunishmentEvent punishEvent = (PunishmentEvent) event;
            if (currentPunishEvent.equals(punishEvent.getAttribute(PunishmentEvent.ATTR_KEY_ID))) {
                isPunishEventFound = true;
            }
            if (isPunishEventFound) {
                lvRecords.setSelection(position);
                break;
            }
        }
    }

    private void bindEvent() {
        refreshPunishListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestEventList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });

        lvRecords.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                //clearNotification(listData.get(position).getId());
                NotificationUtil.clearNotificationById(EventListActivity.this, NotificationUtil.SPECIAL_NOTIFICATION);
                Bundle bundle = new Bundle();
                bundle.putInt(CustomViewInflater.BOTTOM_TOOLBAR_MODE, CustomViewInflater.NO_BTN);
                int detailTitleId = 0;
                if (EventType.REPAIRE.getValue() == eventType) {
                    detailTitleId = R.string.event_repair_details_title;
                } else if (EventType.POINT_LEAK.getValue() == eventType) {
                    detailTitleId = R.string.event_leak_details_title;
                } else if (EventType.PUNISHMENT.getValue() == eventType) {
                    detailTitleId = R.string.punishment_details_title;
                } else if(EventType.CONSTRUCTION.getValue() == eventType) {
                    detailTitleId = R.string.event_construct_details_title;
                } else if(EventType.PUMP.getValue() == eventType) {
                    detailTitleId = R.string.event_pump_details_title;
                } else if (EventType.PATROL.getValue() == eventType) {
                    detailTitleId = R.string.event_patrol_details_title;
                } else if(EventType.MEASURE.getValue() == eventType) {
                    detailTitleId = R.string.event_measure_details_title;
                } else {
                    detailTitleId = R.string.event_gis_report_title;
                }
                bundle.putInt(CustomViewInflater.REPORT_TITLE, detailTitleId);
                bundle.putString(CustomViewInflater.REPORT_COMFROM, PunishDetailOperator.class.getName());
                bundle.putInt(CustomViewInflater.EVENTTYPE, eventType);
                bundle.putString(CustomViewInflater.EVENTID, listData.get(position).getId());
                UIHelper.startActivityWithExtra(CustomMainReportActivity1.class, bundle);
            }
        });
    }

    private void requestEventList() {
        ReportEventService.getInstance().getEventList(HostApplication.getApplication().getCurrentUser(), EventType.valueOf(eventType));
    }

    private BaseAdapter getEventAdatper(ResponseEvent event) {
        BaseAdapter adapter = null;
        Map<String, Object> mapEvents = event.getData();
        statusMap = (Map<String, String>) mapEvents.get("status");
        eventOperationActionListener = new MyEventOperationActionListener();
        if (eventType == EventType.PUNISHMENT.getValue()) {
            List<PunishmentEvent> events = (List<PunishmentEvent>) mapEvents.get("events");
            adapter = new PunishListAdapter(this, events);
        } else if (eventType == EventType.REPAIRE.getValue()) {
            List<RepairementEvent> events = (List<RepairementEvent>) mapEvents.get("events");
            adapter = new EventListAdapter<RepairementEvent>(this, events, statusMap, eventOperationActionListener);
        } else if (eventType == EventType.POINT_LEAK.getValue()) {
            List<PointLeakageEvent> events = (List<PointLeakageEvent>) mapEvents.get("events");
            adapter = new EventListAdapter<PointLeakageEvent>(this, events, statusMap, eventOperationActionListener);
        } else if (eventType == EventType.CONSTRUCTION.getValue()){
            List<ConstructionEvent> events = (List<ConstructionEvent>) mapEvents.get("events");
            adapter = new EventListAdapter<ConstructionEvent>(this, events, statusMap, eventOperationActionListener);
        } else if (eventType == EventType.PUMP.getValue()) {
            List<PumpEvent> events = (List<PumpEvent>) mapEvents.get("events");
            adapter = new EventListAdapter<PumpEvent>(this, events, statusMap, eventOperationActionListener);
        } else if (eventType == EventType.MEASURE.getValue()) {
            List<MeasureEvent> events = (List<MeasureEvent>) mapEvents.get("events");
            adapter = new EventListAdapter<MeasureEvent>(this, events, statusMap, eventOperationActionListener);
        }else if(eventType == EventType.PATROL.getValue()){
            List<PatrolEvent> events = (List<PatrolEvent>) mapEvents.get("events");
            adapter = new EventListAdapter<PatrolEvent>(this, events, statusMap, eventOperationActionListener);
        } else if(eventType == EventType.GIS.getValue()) {
            List<GISReportEvent> events = (List<GISReportEvent>) mapEvents.get("events");
            adapter = new EventListAdapter<GISReportEvent>(this, events, statusMap, eventOperationActionListener);
        }

        return adapter;
    }

    private class MyEventOperationActionListener implements EventListAdapter.IEventOperationActionListener {

        @Override
        public void onLeftButtonClicked(Event event, int position) {
            int eventStatus = Integer.parseInt(event.getAttribute(Event.ATTR_KEY_STATE));
            //clearNotification(event.getId());
            NotificationUtil.clearNotificationById(EventListActivity.this, NotificationUtil.SPECIAL_NOTIFICATION);
            currentPosition = position;
            if (eventStatus == Event.STATUS_PROCESSED) {
                mEvent = event;
                requestEventWorkOrderInfo(event);
            } else {
                gotoNewWorkOrderScreen(event);
            }
        }

        @Override
        public void onRightButtonClicked(Event event, int position) {
            String eventId = event.getAttribute(Event.ATTR_KEY_ID);
            showCloseDialog(eventId);
        }

        private void gotoNewWorkOrderScreen(Event event) {
            Bundle data = new Bundle();
            setBundleData(event, data);
            UIHelper.startActivityWithExtra(CustomReportActivity1.class, data);
        }

        private void setBundleData(Event event, Bundle data) {
            int eventType = Integer.parseInt(event.getAttribute(Event.ATTR_KEY_EVENTTYPE));
            if (eventType == EventType.CONSTRUCTION.getValue()) {
                data.putInt(CustomViewInflater.REPORT_TITLE, R.string.event_management_to_task_title);
                data.putInt(CustomViewInflater.BOTTOM_SINLEBTN_TXT, R.string.event_management_to_task_button_title);
                data.putString(CustomViewInflater.REPORT_COMFROM, Event2TaskOperator.class.getName());
            } else {
                data.putInt(CustomViewInflater.REPORT_TITLE, R.string.event_management_to_work_order_title);
                data.putInt(CustomViewInflater.BOTTOM_SINLEBTN_TXT, R.string.event_management_to_work_order_button_title);
                data.putString(CustomViewInflater.REPORT_COMFROM, Event2WorkOrderOperator.class.getName());
            }
            data.putString(Event2WorkOrderOperator.INTENT_KEY_TASK_X, event.getAttribute(Event.ATTR_KEY_X));
            data.putString(Event2WorkOrderOperator.INTENT_KEY_TASK_Y, event.getAttribute(Event.ATTR_KEY_Y));
            data.putString(Event2WorkOrderOperator.INTENT_KEY_TASK_ADDRESS, event.getAttribute(Event.ATTR_KEY_ADDRESS));
            data.putSerializable(Event2WorkOrderOperator.INTENT_KEY_EVENT, event);
        }
    }

    private void showCloseDialog(final String eventId) {
        String msg = ResourceUtil.getStringById(R.string.event_save_context);
        AlertView dialog = new AlertView(EventListActivity.this, null, msg, new AlertView.OnAlertViewListener() {
            @Override
            public void back(boolean ok) {
                if (ok) {
                    ReportEventService.getInstance().closeEvent(HostApplication.getApplication().getCurrentUser(), eventId);
                }
            }
        }, AlertView.AlertStyle.OK_CANCEL_HIGHLIGHT_CANCEL);
        dialog.show();
    }

    private void requestEventWorkOrderInfo(Event event) {
        LoadingDialogUtil.show(this, R.string.please_wait);
        if (!isWorkOrderMetasHaveParsed()) {
            ReportEventService.getInstance().getWorkOrderField();
        } else {
            getWorkOrderInfoWithEventId(event.getId());
        }

    }

    private boolean isWorkOrderMetasHaveParsed() {
        return SessionManager.workOrderMetasHaveParsed;
    }

    private void getWorkOrderInfoWithEventId(String eventId) {
        if (isWorkOrderMetasHaveParsed()) {
            ReportEventService.getInstance().getWorkOrderInfoWithEventId(eventId);
        }
    }

    private void getEventIdFromCache() {
        ACache aCache = ACache.get(EventListActivity.this);
        String notificationItemsId = aCache.getAsString(TodoFragment.NOTIFICATION_ITEMS_ID);
        if (null != notificationItemsId) {
            String strArray[] = notificationItemsId.split(",");
            if (strArray.length > 1) {
                eventIdFromNotification = strArray[0];
            }
        }
    }

    private void clearNotification(String eventId) {
        if (null != eventIdFromNotification && eventIdFromNotification.equals(eventId)) {
            NotificationUtil.clearNotification(EventListActivity.this);
        }
    }
}
