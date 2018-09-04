package com.ecity.cswatersupply.ui.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.FlowInfoBean;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.utils.CoordTransfer;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.workorder.WorkOrderOperationLogActivity;
import com.ecity.cswatersupply.workorder.WorkOrderUtil;
import com.ecity.cswatersupply.workorder.adpter.WorkOrderDetailTabAdapter;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.mobile.android.bdlbslibrary.utils.BaiDuUtils;
import com.ecity.mobile.android.bdlbslibrary.utils.LocGeoPoint;
import com.ecity.mobile.android.library.gpsengine.GPSEngine;
import com.esri.core.geometry.Point;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.viewpager.SViewPager;
import com.z3app.android.util.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ecity.cswatersupply.utils.DateUtil.convertTimeString2Date;
import static com.ecity.cswatersupply.workorder.widght.WorkOrderListItemBasicView.MAX_TIME_GAP;
import static com.ecity.cswatersupply.workorder.widght.WorkOrderListItemBasicView.WORK_ORDER_BEYONDED_DEADLINE;
import static com.ecity.cswatersupply.workorder.widght.WorkOrderListItemBasicView.WORK_ORDER_GOING_TO_BEYOND_DEADLINE;
import static com.ecity.cswatersupply.workorder.widght.WorkOrderListItemBasicView.WORK_ORDER_NOT_BEYOND_DEADLINE;

/*工单详情FragmentActivity*/
public class WorkOrderDetailFragmentActivity extends FragmentActivity {
    public final static int GOTO_BAIDU_SHOW_DIALOG = 0;
    public final static int GOTO_BAIDU_SUCCESS_DIALOG = 1;
    public final static int GOTO_BAIDU_ERROR_DIALOG = 2;
    public final static int GOTO_BAIDU_NO_LOCALPOINT_DIALOG = 3;
    public final static int GOTO_BAIDU_NO_TAGRTPOINT_DIALOG = 4;

    public static final String INTENT_KEY_NOTIFICATION = "INTENT_KEY_NOTIFICATION";
    private boolean isCompletedOrder = false;
    private WorkOrder currentWorkOrder;
    private List<InspectItem> mInspectItems;
    private IndicatorViewPager mIndicatorViewPager;
    private boolean isExcuteProcessWorkOrder = false;
    /**
     * 工单详情。key：“基本信息”、“勘察信息”、“流程信息”、“维修信息”；value：每组信息对应的InspectItem。
     */
    private Map<String, List<InspectItem>> workOrderDetailInfo = new HashMap<String, List<InspectItem>>();
    private List<FlowInfoBean> listData = new ArrayList<FlowInfoBean>();

    MyHandler handler = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setTheme(R.style.Theme_MainScreenTabPageIndicator);
        this.setTheme(R.style.Theme_TabPageIndicatorDefaults);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_workorder_detail_fragment);
        HostApplication.getApplication().getAppManager().addActivity(this);
        EventBusUtil.register(this);
        initData();
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setIntent(null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            postNextStepId2CustomWorkOrderActivity();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackButtonClicked(View v) {
        postNextStepId2CustomWorkOrderActivity();
        finish();
    }

    public Map<String, List<InspectItem>> getAllInspectItems() {
        return workOrderDetailInfo;
    }

    public List<FlowInfoBean> getAllFlowInfoItems() {
        return listData;
    }

    /**
     * 根据tab名称获取对应部分的工单信息。
     * @param infoGroup tab名称。如“基本信息”。
     * @return
     */
    public List<InspectItem> getWorkOrderInfoByAlias(String infoGroup) {
        List<InspectItem> validateItem;
        String status = WorkOrderUtil.getWorkOrderStateString(currentWorkOrder.getAttributes());
        if ((infoGroup.equals(R.string.detail_tab_explore_info)) && ((status.equals("待分派")) || (status.equals("待接单")))) {
            validateItem = new ArrayList<InspectItem>();
        } else {
            validateItem = workOrderDetailInfo.get(infoGroup);
        }
        return validateItem;
    }

    private void initUI() {
        setTitle();
        setTabPageIndicator();
        if (!ListUtil.isEmpty(mInspectItems)) {
            fillDatas(workOrderDetailInfo);
        }
    }

    public void fillDatas(Map<String, List<InspectItem>> inspectItems) {
        this.workOrderDetailInfo = inspectItems;
    }

    //更新标题
    private void setTitle() {
        CustomTitleView title = (CustomTitleView) findViewById(R.id.customTitleView1);
        title.setBtnStyle(BtnStyle.RIGHT_ACTION);
        title.setTitleText(R.string.workorder_detail_title);
        title.setRightActionBtnText(R.string.log_date_back);

        String deadLineTime = WorkOrderUtil.getAliasOfValue(currentWorkOrder.getAttributes(), WorkOrder.KEY_ASKFINISH_TIME);
        if (isExcuteProcessWorkOrder) {
            if (judgeGapOfTime(deadLineTime) == WORK_ORDER_GOING_TO_BEYOND_DEADLINE) {
                title.setBackgroundResource(R.color.orange);
            } else if (judgeGapOfTime(deadLineTime) == WORK_ORDER_BEYONDED_DEADLINE) {
                title.setBackgroundResource(R.color.red);
            }
        }
    }

    //更新Tab分页
    private void setTabPageIndicator() {
        SViewPager viewPager = (SViewPager) findViewById(R.id.tabmain_viewPager);
        viewPager.setCanScroll(true);
        Indicator indicator = (Indicator) findViewById(R.id.tabmain_indicator);
        mIndicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        mIndicatorViewPager.setAdapter(new WorkOrderDetailTabAdapter(this, getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(mIndicatorViewPager.getViewPager().getAdapter().getCount());
    }

    private void initData() {
        this.currentWorkOrder = getWorkOrderFromIntent();
        Map<String, String> params = new HashMap<String, String>();
        params.put(WorkOrder.KEY_ID, currentWorkOrder.getAttributes().get(WorkOrder.KEY_ID));
        WorkOrderService.instance.getDetailInfo(currentWorkOrder);
        WorkOrderService.instance.getWorkOrderDetailFlowInfo(currentWorkOrder);
    }

    public void onActionButtonClicked(View v) {
        Intent intent = new Intent(HostApplication.getApplication().getApplicationContext(), WorkOrderOperationLogActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("currentWorkOrder", currentWorkOrder);
        intent.putExtras(bundle);
        HostApplication.getApplication().getAppManager().currentActivity().startActivity(intent);
    }

    public void onGPSBtnClicked(View view) {
        if (currentWorkOrder.hasLocationInfo()) {
            new Thread(new NavigationThread()).start();
        } else {
            ToastUtil.showLong(WorkOrderDetailFragmentActivity.this.getString(R.string.workorder_no_location));
        }
    }

    public class NavigationThread implements Runnable {
        @Override
        public void run() {
            try {
                Location locationStart = GPSEngine.getInstance().getLastLocation();
                if (null == locationStart) {
                    sendMessage(3);
                    return;
                }
                // 将GPS设备采集的原始GPS坐标转换成百度坐标
                LocGeoPoint inPoint = new LocGeoPoint(locationStart.getLongitude(), locationStart.getLatitude(), 0);
                LocGeoPoint outPoint = BaiDuUtils.getBaiduGpsPointByGps(inPoint);
                LatLng start = new LatLng(outPoint.y, outPoint.x);
                // 工单坐标 本地转WGS84（GPS使用的坐标系）
                Point point = currentWorkOrder.getPoint();
                if (point.getX() == 0 || point.getY() == 0) {
                    sendMessage(4);
                    return;
                }
                double[] endPoints = CoordTransfer.transToLatlon(point.getX(), point.getY());
                if (null != endPoints) {
                    LocGeoPoint endPoint = new LocGeoPoint(endPoints[0], endPoints[1], 0);
                    LocGeoPoint outPoint2 = BaiDuUtils.getBaiduGpsPointByGps(endPoint);
                    LatLng end = new LatLng(outPoint2.y, outPoint2.x);
                    startNavi(start.latitude, start.longitude, end.latitude, end.longitude);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(int what) {
        Message msg = new Message();
        msg.what = what;
        handler.sendMessage(msg);
    }

    public void startNavi(double mLat1, double mLon1, double mLat2, double mLon2) {
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(mLat2, mLon2);

        // 构建 导航参数
        NaviParaOption para = new NaviParaOption().startPoint(pt1).endPoint(pt2).startName("从这里开始").endName("到这里结束");
        try {
            sendMessage(0);
            boolean isshow = BaiduMapNavigation.openBaiduMapNavi(para, WorkOrderDetailFragmentActivity.this);
            if (isshow) {
                sendMessage(1);
            } else {
                sendMessage(2);
            }
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(WorkOrderDetailFragmentActivity.this);
            builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    private WorkOrder getWorkOrderFromIntent() {
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b == null) {
            return null;
        }
        Serializable ser = b.getSerializable(WorkOrder.KEY_SERIAL);
        isCompletedOrder = b.getBoolean("isCompletedOrder");
        isExcuteProcessWorkOrder = b.getBoolean("isExcuteProcessWorkOrder");
        if (ser == null) {
            return null;
        }
        return (WorkOrder) ser;
    }

    public WorkOrder getCurrentWorkOrder() {
        return currentWorkOrder;
    }

    public boolean isCompletedOrder() {
        return isCompletedOrder;
    }

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int flag = msg.what;
            switch (flag) {
                case GOTO_BAIDU_SHOW_DIALOG:
                    LoadingDialogUtil.show(WorkOrderDetailFragmentActivity.this, WorkOrderDetailFragmentActivity.this.getResources().getString(R.string.map_to_baidu));
                    break;
                case GOTO_BAIDU_SUCCESS_DIALOG:
                    LoadingDialogUtil.dismiss();
                    break;
                case GOTO_BAIDU_ERROR_DIALOG:
                    LoadingDialogUtil.dismiss();
                    ToastUtil.showShort(WorkOrderDetailFragmentActivity.this.getResources().getString(R.string.map_to_baidu_error));
                    break;
                case GOTO_BAIDU_NO_LOCALPOINT_DIALOG:
                    Toast.makeText(WorkOrderDetailFragmentActivity.this, WorkOrderDetailFragmentActivity.this.getResources().getString(R.string.no_GPS_location),
                            Toast.LENGTH_SHORT).show();
                    break;
                case GOTO_BAIDU_NO_TAGRTPOINT_DIALOG:
                    Toast.makeText(WorkOrderDetailFragmentActivity.this, WorkOrderDetailFragmentActivity.this.getResources().getString(R.string.chooce_taget_location_point),
                            Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    }

    private void handleGetWorkOrderDetailInfoEvent(ResponseEvent event) {
        workOrderDetailInfo = event.getData();
        UIEvent uiEvent = new UIEvent(UIEventStatus.WORKORDER_DETAIL_REFRESH_FRAGMENT);
        EventBusUtil.post(uiEvent);
    }

    private void handleGetWorkOrderFlowInfoEvent(ResponseEvent event) {
        listData = event.getData();
        UIEvent uiEvent = new UIEvent(UIEventStatus.WORKORDER_DETAIL_REFRESH_FLOW_INFO_FRAGMENT);
        EventBusUtil.post(uiEvent);
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.WORKORDER_GET_DETAIL_INFO:
                handleGetWorkOrderDetailInfoEvent(event);
                break;
            case ResponseEventStatus.WORKORDER_GET_FLOW_DETAIL_INFO:
                handleGetWorkOrderFlowInfoEvent(event);
                break;
            default:
                break;
        }
    }

    private void postNextStepId2CustomWorkOrderActivity() {
        EventBusUtil.post(new UIEvent(UIEventStatus.WORKORDER_OPERATE_BACK, null));
    }

    private int judgeGapOfTime(String deadlineTime) {
        String currentTime = DateUtil.getCurrentTime();
        if (StringUtil.isBlank(currentTime)) {
            return -1;
        }
        int hoursBetween = DateUtil.hoursBetween(convertTimeString2Date(currentTime), convertTimeString2Date(deadlineTime));
        if (hoursBetween > 0 && hoursBetween < MAX_TIME_GAP) {
            return WORK_ORDER_GOING_TO_BEYOND_DEADLINE;//即将超期
        } else if (hoursBetween < 0) {
            return WORK_ORDER_BEYONDED_DEADLINE;//已超期
        } else if (hoursBetween > MAX_TIME_GAP) {
            return WORK_ORDER_NOT_BEYOND_DEADLINE;//未超期
        }

        return -1;
    }
}
