package com.ecity.cswatersupply.ui.activities;

import java.util.ArrayList;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.adapter.EventTypeAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.menu.EventReportOperator1;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.event.EventType;
import com.ecity.cswatersupply.service.ReportEventService;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.esri.core.map.Graphic;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

public class ReportEventTypeSelectActivity extends BaseActivity {
    private CustomTitleView reportTypeTitleView;
    private Map<String,ArrayList<String>> eventType;
    private ArrayList<Integer> eventTypeImage;
    private ListView eventTypeListView;
    private Graphic currentDevice;
    private int taskid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_eventreport_typeselect);
        EventBusUtil.register(this);
        initUI();
        currentDevice = (Graphic) getIntent().getSerializableExtra("device");
        taskid = getIntent().getIntExtra(Constants.PLAN_TASK_ID,0);
        setOnclick();
    }

    private void setOnclick() {
        eventTypeListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int eventTypeId = 0;
                try {
                    eventTypeId = Integer.parseInt(eventType.get("eventtype").get(position));
                } catch (NumberFormatException e) {
                    LogUtil.e("ReportEventTypeSelectActivity", e);
                }
                transform2CustomReportMain(eventType.get("eventname").get(position), eventTypeId);
            }
        });
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    private void transform2CustomReportMain(String title, int eventType) {
        Bundle bundle = new Bundle();
        SessionManager.reportType = 0;//选择上报类型为事件上报
        bundle.putString(CustomViewInflater.REPORT_TITLE, title);
        bundle.putString(CustomViewInflater.REPORT_COMFROM, EventReportOperator1.class.getName());
        bundle.putInt(CustomViewInflater.EVENTTYPE, eventType);
        if(EventType.POINT_LEAK == EventType.valueOf(eventType)) {
            bundle.putSerializable(CustomViewInflater.EVENT_LEAK_CURRENT_SELECT_DEVICE, currentDevice);
        }
        if(0 != taskid) {
            bundle.putInt(Constants.PLAN_TASK_ID, taskid);
        }
        UIHelper.startActivityWithExtra(CustomMainReportActivity1.class, bundle);
    }

    private void initGridData() {
        eventTypeImage = new ArrayList<Integer>();
        eventTypeImage.add(R.drawable.event_icon_repair);
        eventTypeImage.add(R.drawable.event_icon_leak);
        eventTypeImage.add(R.drawable.event_icon_ticket);
        eventTypeImage.add(R.drawable.event_icon_construction);
        User user = HostApplication.getApplication().getCurrentUser();
        ReportEventService.getInstance().getEventType(user);
    }

    private void initUI() {
        reportTypeTitleView = (CustomTitleView) findViewById(R.id.view_title_report_event_type);
        reportTypeTitleView.setTitleText(R.string.event_report_title);
        eventTypeListView = (ListView) findViewById(R.id.gv_eventtype_select);
        initGridData();
    }
    
    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.EVENT_REPORT_GET_TYPE_LIST:
                eventType = event.getData();
                EventTypeAdapter eventTypeAdapter = new EventTypeAdapter(eventType.get("eventname"), eventTypeImage, this);
                eventTypeListView.setAdapter(eventTypeAdapter);
                break;
            default:
                break;
        }
    }
}
