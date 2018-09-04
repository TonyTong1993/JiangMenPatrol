package com.ecity.cswatersupply.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.adapter.MainTabAdapter;
import com.ecity.cswatersupply.adapter.planningtask.PlanTasksAdapter;
import com.ecity.cswatersupply.contact.model.ContactGroup;
import com.ecity.cswatersupply.emergency.EQModuleConfig;
import com.ecity.cswatersupply.emergency.service.EmergencyService;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.menu.AppMenu;
import com.ecity.cswatersupply.menu.map.BusMonitorOperatorXtd;
import com.ecity.cswatersupply.model.PatrolBusInfo;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.Content;
import com.ecity.cswatersupply.model.metaconfig.QueryLayerIDs;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTask;
import com.ecity.cswatersupply.project.FuZhouPatrolModuleConfig;
import com.ecity.cswatersupply.project.activity.ProjectDetailFragmentActivity;
import com.ecity.cswatersupply.service.AppStatusService;
import com.ecity.cswatersupply.service.CORSLocationService;
import com.ecity.cswatersupply.service.MessageService;
import com.ecity.cswatersupply.service.PatrolPositionReceiver;
import com.ecity.cswatersupply.service.PatrolService;
import com.ecity.cswatersupply.service.PlanningTaskService;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.service.TaskArrivalService;
import com.ecity.cswatersupply.service.UserService;
import com.ecity.cswatersupply.utils.ACache;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.MetaDownloadUtil;
import com.ecity.cswatersupply.utils.PatrolReportPositionBeanBuilder;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.test.FuzhouPatrolLoationProducer;
import com.ecity.cswatersupply.utils.test.JiangMenPatrolLoationProducer;
import com.ecity.cswatersupply.xg.INotificationProcesser;
import com.ecity.cswatersupply.xg.MyXGRegisterCallback;
import com.ecity.cswatersupply.xg.XGPushUtil;
import com.ecity.cswatersupply.xg.model.Notification;
import com.ecity.cswatersupply.xg.model.NotificationType;
import com.ecity.cswatersupply.xg.service.NotificationListener;
import com.ecity.cswatersupply.xg.util.NotificationFunctionMappingFactory;
import com.ecity.cswatersupply.xg.util.NotificationUtil;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.viewpager.SViewPager;
import com.z3app.android.util.FileUtil;
import com.zzz.ecity.android.applibrary.model.PositionConfig;
import com.zzz.ecity.android.applibrary.service.PositionService;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.ecity.cswatersupply.xg.util.NotificationUtil.SPECIAL_NOTIFICATION;

public class MainActivity extends FragmentActivity {
    private long mExitTime;
    public static final String INTENT_KEY_NOTIFICATION = "INTENT_KEY_NOTIFICATION";
    private IndicatorViewPager mIndicatorViewPager;
    private PatrolPositionReceiver patrolPositionReceiver;
    private int callResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.Theme_MainScreenTabPageIndicator);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        HostApplication.getApplication().getAppManager().addActivity(this);
        EventBusUtil.register(this);
        registerXG();
        initUI();
//        updatePatrolManState();
        try {
            if (FuZhouPatrolModuleConfig.getConfig().isModuleUseable()) {
                CORSLocationService.startInstance(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        startPositionService();

        registerPatrolPositionReceiver();

        try {
            if (!EQModuleConfig.getConfig().isModuleUseable()) {
                initPatrolData();
                initPatrolBusData();
            } else {
                initContactData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TaskArrivalService.getInstance().start(HostApplication.getApplication(), Constants.TASK_ARRIVAL_DETECTION_INTERVAL, Constants.TASK_ARRIVAL_LINE_REPORT_INTERVAL);
        NotificationListener.start();

        String url = ServiceUrlManager.getInstance().getSpacialSearchUrl();
        MetaDownloadUtil.LoadMapMetas(url);
    }

    /***
     * 更新人员在线状态
     */
    private void updatePatrolManState() {
        PatrolService.getInstance().updatePatrolManState(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleNotification();
        if (QueryLayerIDs.getInstance().isMetaSetNull()) {
            String url = ServiceUrlManager.getInstance().getSpacialSearchUrl();
            MetaDownloadUtil.LoadMapMetas(url);
        }
        setIntent(null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onDestroy() {
        stopPositionService();
        unRegisterPatrolPositionReceiver();
        doOtherthingBeforeExist();
        TaskArrivalService.getInstance().stop();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if ((System.currentTimeMillis() - mExitTime) > 2000) {
//                EventBusUtil.post(new UIEvent(UIEventStatus.TOAST, ResourceUtil.getStringById(R.string.str_one_more_click_exit), this));
//                mExitTime = System.currentTimeMillis();
//            } else {
//                //startLogout();
//            }
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            callResult = data.getIntExtra("call_back", 0);
        }
    }

    public int getCallResult() {
        return callResult;
    }

    private void initUI() {
        List<List<AppMenu>> availableTabs = parseAvailableTabs();
        List<List<AppMenu>> availableRightTabs = parseAvailableRightTabs();
        SViewPager viewPager = (SViewPager) findViewById(R.id.main_tabmain_viewPager);
        viewPager.setCanScroll(false);
        Indicator indicator = (Indicator) findViewById(R.id.tabmain_indicator);
        mIndicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        mIndicatorViewPager.setAdapter(new MainTabAdapter(this, getSupportFragmentManager(), availableTabs));
        viewPager.setOffscreenPageLimit(mIndicatorViewPager.getViewPager().getAdapter().getCount());
        BitmapDrawable bg = (BitmapDrawable) getResources().getDrawable(R.drawable.css_main_menu_texture);
        bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
        findViewById(R.id.main_menu).setBackground(bg);
        mIndicatorViewPager.setOnIndicatorPageChangeListener(new IndicatorViewPager.OnIndicatorPageChangeListener() {
            @Override
            public void onIndicatorPageChange(int preItem, int currentItem) {
                NotificationUtil.clearNotificationById(MainActivity.this, NotificationUtil.SPECIAL_NOTIFICATION);
            }
        });
    }

    /**
     * 解析主界面按钮，并根据指定的每页按钮数量，装订page页
     */
    private List<List<AppMenu>> parseAvailableTabs() {
        List<AppMenu> appMenus = UserService.getInstance().getAvailableMenus();
        return createPager(appMenus, 9);
    }

    private List<List<AppMenu>> parseAvailableRightTabs() {
        List<AppMenu> appMenus = UserService.getInstance().getAvailableRightMenus();
        return createPager(appMenus, 9);
    }

    /**
     * 把所有按钮按照指定数量，装载成页
     *
     * @param appMenus 所有可用按钮
     * @param max      每页按钮的数量最大值
     * @return
     */
    private List<List<AppMenu>> createPager(List<AppMenu> appMenus, int max) {
        List<List<AppMenu>> availableTabs = new ArrayList<List<AppMenu>>();
        List<AppMenu> availableMenus = new ArrayList<AppMenu>();

        for (int i = 0; i < appMenus.size(); i++) {
            if (i == 0 || i % max != 0) {
                availableMenus.add(appMenus.get(i));
            } else if (i > 0 && i % max == 0) {
                availableTabs.add(availableMenus);
                availableMenus = new ArrayList<AppMenu>();
                availableMenus.add(appMenus.get(i));
            }
        }
        availableTabs.add(availableMenus);
        return availableTabs;
    }

    private void handleDownLoadFile(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        openApk((String) event.getData());
    }

    private void openApk(String filePath) {
        File f = new File(filePath);
        if (f != null && f.exists() && f.length() > 0) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            String type = FileUtil.getMIMEType(f.getName());
            intent.setDataAndType(Uri.fromFile(f), type);
            startActivity(intent);
        } else {
            ToastUtil.showShort(R.string.str_soft_update_fail);
        }
    }

    private void handleDownoadProgressUpdate(ResponseEvent event) {
        double progress = Double.valueOf(event.getMessage());
        String msg = ResourceUtil.getStringById(R.string.downloading_new_version) + String.format("%.1f", progress) + "%";
        LoadingDialogUtil.updateMessage(msg);
    }

    private void initPatrolData() {
        requestContents();
    }

    private void initPatrolBusData() {
        requestPatrolBuses();
    }

    private void initContactData() {
        EmergencyService.getInstance().getEmerContact();
    }

    private void requestPlanTask() {
        PlanningTaskService.getInstance().getPlanningTasksInMainActivity(ServiceUrlManager.getInstance().getPlanningTaskUrl(), HostApplication.getApplication().getCurrentUser());
    }

    private void handleGetPlanningTasks(final ResponseEvent event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String eventObject = event.getData();
                ArrayList<Z3PlanTask> PlanTaskListByNet = PlanTasksAdapter.getInstance().getPlanTasksAdapterInMainActivity(eventObject);
                SessionManager.CurrentPlanTasks = PlanTaskListByNet;
            }
        }).start();
    }

    //获取检查项
    private void requestContents() {
        PlanningTaskService.getInstance().getReportInspectItems(ServiceUrlManager.getInstance().getInspectItemUrl());
    }

    //获取巡检车辆数据
    private void requestPatrolBuses() {
        String groupCode = HostApplication.getApplication().getCurrentUser().getGroupCode();
        boolean isMonitor = true;
        String busNo = null;
        UserService.getInstance().getPatrolBus(groupCode, busNo, isMonitor);
    }

    private void handlePatrolBuses(final ResponseEvent event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<PatrolBusInfo> list = event.getData();
                for (PatrolBusInfo user : list) {
                    user.setShowFromSearch(true);
                }
                ACache aCache = ACache.get(MainActivity.this);
                aCache.put(BusMonitorOperatorXtd.PATROL_BUS_LIST,(Serializable)list);
            }
        }).start();
    }

    private void handleContents(final ResponseEvent event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String eventObject = event.getData();
                ArrayList<Content> contents = PlanTasksAdapter.getInstance().adaptContents(eventObject);
                SessionManager.contentsList = contents;
                UIEvent uievent = new UIEvent(UIEventStatus.PLANNINGTASK_GET_PLANTASK);
                EventBusUtil.post(uievent);
            }
        }).start();
    }

    private void registerXG() {
        User user = HostApplication.getApplication().getCurrentUser();
        XGPushUtil.registerPush(this, user.getLoginName(), MyXGRegisterCallback.getInstance());
    }

    private void startLogout() {
        doOtherthingBeforeExist();
        HostApplication.getApplication().exitApp(false);
    }

    private void doOtherthingBeforeExist() {
        EventBusUtil.unregister(this);
        AppStatusService.stopInstance(HostApplication.getApplication());
        HostApplication.getApplication().getAppManager().removeActivity(this);
        HostApplication.getApplication().doOtherthingBeforeExist(this);
    }

    private void startPositionService() {
        PositionConfig.setReportUserId(HostApplication.getApplication().getCurrentUser().getId());
        PositionConfig.setReportServerURL(ServiceUrlManager.getInstance().getReportPositionUrl());
        PositionConfig.setReportPositionBeanBuilderClassName(PatrolReportPositionBeanBuilder.class.getName());
        setupLocationNotificationBuilder();
        //start 模拟坐标

//        PositionConfig.setSimulateLocation(true);
//        PositionConfig.setLoationProducerClassName(FuzhouPatrolLoationProducer.class.getName());

        // end 模拟坐标
        PositionService.startInstance(MainActivity.this);
    }

    private void setupLocationNotificationBuilder() {
        android.app.Notification.Builder builder = new android.app.Notification.Builder(this);
        builder.setSmallIcon(R.drawable.logo_main);
        builder.setTicker(getText(R.string.app_name_cswatersupply));
        builder.setContentTitle(getText(R.string.app_name_cswatersupply));
        builder.setContentText(getText(R.string.app_is_running));
        builder.setWhen(System.currentTimeMillis()); //发送时间
        builder.setDefaults(android.app.Notification.DEFAULT_ALL);
        PositionConfig.setLocationNotificationBuilder(builder);
    }

    private void stopPositionService() {
        PositionService.stopInstance(MainActivity.this);
    }

    private void registerPatrolPositionReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(PositionConfig.ACTION_POSITION_NAME);
        patrolPositionReceiver = new PatrolPositionReceiver();
        registerReceiver(patrolPositionReceiver, filter);
    }

    private void unRegisterPatrolPositionReceiver() {
        if (null != patrolPositionReceiver) {
            try {
                unregisterReceiver(patrolPositionReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleNotification() {
        if (getIntent() == null) {
            return;
        }
        Notification notification = (Notification) getIntent().getSerializableExtra(INTENT_KEY_NOTIFICATION);
        if (notification == null) {
            return;
        }

        UIEvent event = new UIEvent(UIEventStatus.NOTIFICATION_VIEWED, this);
        event.setData(notification);
        EventBusUtil.post(event);
    }

    private void handleNotificationActivatedEvent(UIEvent event) {
        Notification notification = event.getData();

        MessageService.getInstance().readMessage(notification.getId());
        NotificationUtil.clearNotification(this);

        Class<?> cls = NotificationFunctionMappingFactory.getFunctionHandlerClass(notification.getType().toString());
        if (Activity.class.isAssignableFrom(cls)) {
            Intent intent = new Intent(this, cls);
            intent.putExtra(Constants.INTENT_KEY_NEXT_STEP_ID, notification.getNextStepIds());
            intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_PROJECT_NOTIFICATION, notification);
            //增加对任务的判断，如果是任务的通知
            if (notification.getType().toString().equalsIgnoreCase("xj_task")) {
                intent.putExtra("type", HostApplication.getApplication().getApplicationContext().getResources().getString(R.string.planningtask_type_p));
                SessionManager.isContent = 1;
                intent.putExtra("isContents", 1);
            } else if (notification.getType().toString().equalsIgnoreCase("yh_task")) {
                intent.putExtra("type", HostApplication.getApplication().getApplicationContext().getResources().getString(R.string.planningtask_type_f));
                SessionManager.isContent = 2;
                intent.putExtra("isContents", 2);
            }
            startActivity(intent);
        } else {
            if ((notification.getType() == NotificationType.zb_squeezed_out) || (notification.getType() == NotificationType.zb_time_out)) {
                return;
            }
            try {
                INotificationProcesser handler = (INotificationProcesser) cls.newInstance();
                handler.processNotification(this, notification);
            } catch (InstantiationException e) {
                LogUtil.e(this, e);
            } catch (IllegalAccessException e) {
                LogUtil.e(this, e);
            }
        }
    }

    public void onEventMainThread(UIEvent event) {
        switch (event.getId()) {
            case UIEventStatus.TOAST:
                if (event.isForTarget(this)) {
                    ToastUtil.showShort(event.getMessage());
                }
                break;
            case UIEventStatus.NOTIFICATION_VIEWED:
                handleNotificationActivatedEvent(event);
                break;
            case UIEventStatus.PLANNINGTASK_GET_PLANTASK:
                requestPlanTask();
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
            case ResponseEventStatus.FILE_OPERATION_DOWNLOAD_FINISH:
                if (event.isForTarget(this)) {
                    handleDownLoadFile(event);
                }
                break;
            case ResponseEventStatus.FILE_OPERATION_UPDATE_PROGRESS:
                if (event.isForTarget(this)) {
                    handleDownoadProgressUpdate(event);
                }
                break;
            case ResponseEventStatus.PLANNINGTASK_LIST_MAIN:
                handleGetPlanningTasks(event);
                break;
            case ResponseEventStatus.PLANNING_REPORTINSPECTITEMS:
                handleContents(event);
                break;
            case ResponseEventStatus.EMERGENCY_GET_CONTACT:
                handleContacts(event);
                break;
            case ResponseEventStatus.GET_PATROL_BUS:
                handlePatrolBuses(event);
                break;
            default:
                break;
        }
    }

    private void handleContacts(final ResponseEvent event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContactGroup contactGroup = event.getData();
                UIEvent event = new UIEvent(UIEventStatus.EMERGENCY_CONTACT_GET_CONTACTS, contactGroup);
                EventBusUtil.post(event);
            }
        }).start();
    }
}
