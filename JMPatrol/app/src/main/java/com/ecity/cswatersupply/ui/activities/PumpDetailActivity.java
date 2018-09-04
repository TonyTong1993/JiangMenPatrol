package com.ecity.cswatersupply.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.adapter.PumpRepairAndMaintainRecordAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.menu.PunishDetailOperator;
import com.ecity.cswatersupply.menu.map.PumpMaintainInfoOperator;
import com.ecity.cswatersupply.model.PumpRepairAndMaintainInfoModel;
import com.ecity.cswatersupply.model.checkitem.PumpInsSelectValue;
import com.ecity.cswatersupply.model.event.EventType;
import com.ecity.cswatersupply.service.ReportEventService;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.JsonUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/***
 * Created by MaoShouBei on 2017/5/12.
 */

public class PumpDetailActivity extends BaseActivity {
    public static final String INTENT_KEY_FROM_PUMP_DETAIL_FUNC = "intentKeyFromPumpDetailFunc";
    public static final String PUMP_URL = "pumpUrl";
    public static final String WEB_VIEW_TITLE_NAME = "menuName";
    public static final String WEB_VIEW_TITLE_TYPE = "titleType";


    private CustomTitleView titleView;
    private TextView tvPumpNO;
    private TextView tvPumpName;
    private TextView tvPumpAddress;
    private TextView tvMonitor;
    private TextView tvDocument;
    private ListView lvRecords;
    private TextView tvPumpsRecordCount;
    private PullToRefreshListView refreshPunishListView;
    private PumpRepairAndMaintainRecordAdapter mPumpRepairAndMaintainRecordAdapter;
    private PumpInsSelectValue selectValue;
    private List<PumpRepairAndMaintainInfoModel> mList = new ArrayList<PumpRepairAndMaintainInfoModel>();
    private WorkOrder currentWorkOrder;
    private String processInstanceId;
    private String pumpMonitorUrl;
    private String pumpFileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtil.register(this);
        setContentView(R.layout.activity_pump_detail);
        initUI();
        initTitle();
        initData();
        bindEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPumpsRecordData();
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    private void initUI() {
        tvPumpNO = (TextView) findViewById(R.id.tv_pump_no);
        tvPumpName = (TextView) findViewById(R.id.tv_pump_name);
        tvPumpAddress = (TextView) findViewById(R.id.tv_pump_address);
        titleView = (CustomTitleView) findViewById(R.id.customTitleView1);
        titleView.setBtnStyle(CustomTitleView.BtnStyle.RIGHT_ACTION);
        tvMonitor = (TextView) findViewById(R.id.tv_pump_monitor);
        tvDocument = (TextView) findViewById(R.id.tv_pump_document);
        tvPumpsRecordCount = (TextView) findViewById(R.id.pump_record_count);
        refreshPunishListView = (PullToRefreshListView) findViewById(R.id.lv_pumps_records);
        refreshPunishListView.setPullLoadEnabled(true);
        refreshPunishListView.setPullRefreshEnabled(true);
        lvRecords = refreshPunishListView.getRefreshableView();
        lvRecords.setDivider(ResourceUtil.getDrawableResourceById(R.drawable.shape_list_divider));
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        selectValue = (PumpInsSelectValue) bundle.getSerializable(PumpSelectActivity.SELECT_PUMP);
        tvPumpAddress.setText(selectValue.getPumpRoad());
        tvPumpName.setText(selectValue.getAlias());
        tvPumpNO.setText(selectValue.getPumpNO());
        mPumpRepairAndMaintainRecordAdapter = new PumpRepairAndMaintainRecordAdapter(this);
        updatePumpsRecordCount();
    }


    private void initTitle() {
        titleView.setTitleText(ResourceUtil.getStringById(R.string.pump_detail_title));
        titleView.setBtnStyle(CustomTitleView.BtnStyle.ONLY_BACK);
    }

    private void bindEvents() {
        lvRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PumpDetailActivity.this.processInstanceId = mList.get(i).getBusinessKey();
                switch (Integer.valueOf(mList.get(i).getMaintainType())) {
                    case PumpRepairAndMaintainInfoModel.OPERATE_TYPE_EVENT_REPORT:
                        goToPumpEventDetailPage();
                        break;
                    case PumpRepairAndMaintainInfoModel.OPERATE_TYPE_MAINTAIN_REPORT:
                        goToPumpMaintainDetailPage();
                        break;
                    case PumpRepairAndMaintainInfoModel.OPERATE_TYPE_REPAIR:
                        goToWorkOrderDetailPage();
                        break;
                }
            }
        });

        refreshPunishListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestPumpsRecordData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestPumpsRecordData();
            }
        });

        tvDocument.setOnClickListener(myOnClickListener);
        tvMonitor.setOnClickListener(myOnClickListener);
    }

    private void requestPumpsRecordData() {
        LoadingDialogUtil.show(this, R.string.str_searching);
        mList.clear();
        String url = ServiceUrlManager.getInstance().getPumpRepairMaintainInfo();
        ReportEventService.getInstance().getPumpRepairMaintainInfo(url, selectValue.getGid());
    }


    private void updatePumpsRecordCount() {
        tvPumpsRecordCount.setText("（" + mList.size() + "）");
    }

    private void handleGetPumpsRecords(ResponseEvent event) {
        JSONObject jsonObject = event.getData();
        pumpMonitorUrl = jsonObject.optString("pumpMonitorUrl") + "?bianhao=" + selectValue.getPumpNO();
        pumpFileUrl = jsonObject.optString("pumpFileUrl") + "?id=" + selectValue.getPumpNO();

        mList = JsonUtil.parsePumpRepairMaintainInfo(jsonObject);
        sortListByTime(mList);
        filterList();
        updatePumpsRecordCount();
        mPumpRepairAndMaintainRecordAdapter.setList(mList);
        lvRecords.setAdapter(mPumpRepairAndMaintainRecordAdapter);
        mPumpRepairAndMaintainRecordAdapter.notifyDataSetChanged();
        refreshPunishListView.onPullDownRefreshComplete();
        refreshPunishListView.onPullUpRefreshComplete();
        refreshPunishListView.setLastUpdateTime();
    }

    private View.OnClickListener myOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_pump_monitor:
                    onBtnClick(pumpMonitorUrl, ResourceUtil.getStringById(R.string.pump_monitor_str));
                    break;
                case R.id.tv_pump_document:
                    onBtnClick(pumpFileUrl, ResourceUtil.getStringById(R.string.pump_document_str));
                    break;
            }
        }
    };

    public void onEventMainThread(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        if (!event.isOK()) {
            ToastUtil.showLong(event.getMessage());
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.EVENT_GET_PUMP_MAINTAIN_INFO:
                handleGetPumpsRecords(event);
                break;
            case ResponseEventStatus.EVENT_REPORT_GET_WORK_ORDER_INFO:
                handleGetWorkOrderByPid(event);
                break;
            case ResponseEventStatus.EVENT_GET_WORK_ORDER_FIELDS:
                getWorkOrderInfoWithPid(processInstanceId);
                break;
        }
    }

    private void goToPumpMaintainDetailPage() {
        Bundle bundle = new Bundle();
        bundle.putInt(CustomViewInflater.BOTTOM_TOOLBAR_MODE, CustomViewInflater.NO_BTN);
        bundle.putInt(CustomViewInflater.REPORT_TITLE, R.string.pump_maintain_detail_title);
        bundle.putString(CustomViewInflater.REPORT_COMFROM, PumpMaintainInfoOperator.class.getName());
        bundle.putString(WorkOrder.KEY_ID, processInstanceId);
        bundle.putBoolean(INTENT_KEY_FROM_PUMP_DETAIL_FUNC, true);
        UIHelper.startActivityWithExtra(CustomMainReportActivity1.class, bundle);
    }

    private void goToPumpEventDetailPage() {
        Bundle bundle = new Bundle();
        bundle.putInt(CustomViewInflater.BOTTOM_TOOLBAR_MODE, CustomViewInflater.NO_BTN);
        bundle.putInt(CustomViewInflater.REPORT_TITLE, R.string.event_pump_details_title);
        bundle.putString(CustomViewInflater.REPORT_COMFROM, PunishDetailOperator.class.getName());
        bundle.putInt(CustomViewInflater.EVENTTYPE, EventType.PUMP.getValue());
        bundle.putString(CustomViewInflater.EVENTID, processInstanceId);
        UIHelper.startActivityWithExtra(CustomMainReportActivity1.class, bundle);

    }

    private void goToWorkOrderDetailPage() {
        requestEventWorkOrderInfo(processInstanceId);
    }

    private void onBtnClick(String url, String webViewTitle) {
        Bundle bundle = new Bundle();
        bundle.putString(PUMP_URL, url);
        bundle.putString(WEB_VIEW_TITLE_NAME, webViewTitle);
        bundle.putString(WEB_VIEW_TITLE_TYPE, ResourceUtil.getStringById(R.string.pump_web_view_title_type_str));
        UIHelper.startActivityWithExtra(ShowPumpDataInWebViewActivity.class, bundle);
    }

    private void handleGetWorkOrderByPid(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        JSONObject workOrderInfo = event.getData();
        JSONArray features = workOrderInfo.optJSONArray("features");
        if ((features != null) && (features.length() > 0)) {
            JSONObject feature = features.optJSONObject(0);
            WorkOrder workOrder = JsonUtil.parseSingleWorkOrder(feature);
            if (workOrder != null) {
                this.currentWorkOrder = workOrder;
                Bundle bundle = new Bundle();
                bundle.putSerializable(WorkOrder.KEY_SERIAL, currentWorkOrder);
                UIHelper.startActivityWithExtra(WorkOrderDetailFragmentActivity.class, bundle);
            }
        }
    }

    private void requestEventWorkOrderInfo(String pid) {
        LoadingDialogUtil.show(this, R.string.please_wait);
        if (!isWorkOrderMetasHaveParsed()) {
            ReportEventService.getInstance().getWorkOrderField();
        } else {
            getWorkOrderInfoWithPid(pid);
        }
    }

    private boolean isWorkOrderMetasHaveParsed() {
        return SessionManager.workOrderMetasHaveParsed;
    }

    private void getWorkOrderInfoWithPid(String pid) {
        if (isWorkOrderMetasHaveParsed()) {
            ReportEventService.getInstance().getWorkOrderInfoWithPid(pid);
        }
    }

    private void sortListByTime(List<PumpRepairAndMaintainInfoModel> list) {
        Collections.sort(list, new Comparator<PumpRepairAndMaintainInfoModel>() {
            @Override
            public int compare(PumpRepairAndMaintainInfoModel o1, PumpRepairAndMaintainInfoModel o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date dt1 = format.parse(o1.getMaintainTime());
                    Date dt2 = format.parse(o2.getMaintainTime());
                    if (dt1.getTime() < dt2.getTime()) {
                        return 1;
                    } else if (dt1.getTime() > dt2.getTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

    private void filterList() {
        List<PumpRepairAndMaintainInfoModel> tempList = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            if (!mList.get(i).getMaintainUserName().equals("")) {
                tempList.add(mList.get(i));
            }
        }

        mList.clear();
        mList.addAll(tempList);
        tempList.clear();
    }

}
