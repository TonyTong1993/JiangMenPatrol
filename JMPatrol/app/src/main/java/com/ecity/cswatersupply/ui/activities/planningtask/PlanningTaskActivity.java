package com.ecity.cswatersupply.ui.activities.planningtask;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.adapter.planningtask.PlanTasksAdapter;
import com.ecity.cswatersupply.adapter.planningtask.PlanningTaskListAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.menu.map.ConstructionPlanningTaskOperatorXtd;
import com.ecity.cswatersupply.menu.map.FleflowReportPlanningTaskOperatorXtd;
import com.ecity.cswatersupply.menu.map.PlanningTaskOperatorXtd;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTask;
import com.ecity.cswatersupply.project.FuZhouPatrolModuleConfig;
import com.ecity.cswatersupply.service.PlanningTaskService;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.ui.fragment.TodoFragment;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.ACache;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.PropertyChangeManager;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.xg.model.NotificationType;
import com.ecity.cswatersupply.xg.util.NotificationUtil;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.squareup.leakcanary.watcher.RefWatcher;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

public class PlanningTaskActivity extends BaseActivity {

    private static final int PATROLFLAG = 1;
    private static final int FLEXFLOWFLAG = 2;
    private final int FIRST_TYPE = 0;
    private final int SECOND_TYPE = 1;
    private ListView mListView;
    private CustomTitleView title;
    private PullToRefreshListView mPullListView;
    private PlanningTaskListAdapter mAdapter;
    private ArrayList<Z3PlanTask> mPlanTaskList;
    private int itemViewType;
    //养护任务判断;isConserveTask为true是养护任务,false是巡检任务
    public boolean isConserveTask;
    public String type;
    private String planIdFromNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planningtask);
        EventBusUtil.register(this);
        getPlanIdFromCache();
        initView();
        bindEvent();
    }

    private void bindEvent() {
        mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 网络获取
                initPlanTaskByNet();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemViewType = mAdapter.getItemViewType(position);
                NotificationUtil.clearNotificationById(PlanningTaskActivity.this,NotificationUtil.SPECIAL_NOTIFICATION);
                //clearNotification(String.valueOf(mPlanTaskList.get(position).getPlanid()));
                switch (itemViewType) {
                    case FIRST_TYPE:
                        if (null != mPlanTaskList && position < mPlanTaskList.size()) {
                            Intent intent = new Intent(PlanningTaskActivity.this, MapActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            intent.putExtra(MapActivity.MAP_TITLE, mPlanTaskList.get(position).getPlanname());
                            if (isConserveTask) {
                                intent.putExtra(MapActivity.MAP_OPERATOR, FleflowReportPlanningTaskOperatorXtd.class.getName());
                                intent.putExtra(MapActivity.TASKFLAG, FLEXFLOWFLAG);
                            } else {
                                intent.putExtra(MapActivity.MAP_OPERATOR, PlanningTaskOperatorXtd.class.getName());
                                intent.putExtra(MapActivity.TASKFLAG, PATROLFLAG);
                            }
                            intent.putExtra("planning_tasks", mPlanTaskList.get(position));
                            startActivity(intent);
                        }
                        break;
                    case SECOND_TYPE:
                        if (null != mPlanTaskList && position < mPlanTaskList.size()) {
                            Intent intent = new Intent(PlanningTaskActivity.this, MapActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            intent.putExtra(MapActivity.MAP_TITLE, mPlanTaskList.get(position).getPlanname());
                            if (!isConserveTask) {
                                intent.putExtra(MapActivity.MAP_OPERATOR, ConstructionPlanningTaskOperatorXtd.class.getName());
                            }
                            intent.putExtra("planning_tasks", mPlanTaskList.get(position));
                            startActivity(intent);
                        }
                        break;
                    default:
                        break;
                }

            }
        });
    }

    @SuppressLint("NewApi")
    private void initView() {
        title = (CustomTitleView) findViewById(R.id.customTitleView);
        try {
            if (FuZhouPatrolModuleConfig.getConfig().isModuleUseable()) {
                isConserveTask = true;
                title.setTitleText(this.getResources().getString(R.string.planningtask_pa_title));
            }else{
                //判断是否为养护任务
                if (getIntent().getIntExtra("isContents", SessionManager.isContent) == 2) {
                    isConserveTask = true;
                    title.setTitleText(this.getResources().getString(R.string.planningtask_flex_title));
                } else if (getIntent().getIntExtra("isContents", SessionManager.isContent) == 1) {
                    isConserveTask = false;
                    title.setTitleText(this.getResources().getString(R.string.planningtask_pa_title));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        type = getIntent().getStringExtra("type");
        title.setBtnStyle(CustomTitleView.BtnStyle.ONLY_BACK);
        title.tv_title.setBackground(ResourceUtil.getDrawableResourceById(R.drawable.selector_titleview_titletxt_shape));
        mPullListView = (PullToRefreshListView) findViewById(R.id.lv_container);
        mPullListView.setVerticalScrollBarEnabled(true);
        mListView = mPullListView.getRefreshableView();
        mListView.setVerticalScrollBarEnabled(true);
        mListView.setDivider(ResourceUtil.getDrawableResourceById(R.drawable.shape_list_divider));
        mListView.setDividerHeight(ResourceUtil.getDimensionPixelSizeById(R.dimen.margin_spacing_level_4));

        clearBadgeViewCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPlanIdFromCache();
        initPlanTaskByNet();
    }

    // 从网络获取任务数据
    private void initPlanTaskByNet() {
        LoadingDialogUtil.show(this, R.string.plantask_loading);
        PlanningTaskService.getInstance().getPlanningTasks(ServiceUrlManager.getInstance().getPlanningTaskUrl(), HostApplication.getApplication().getCurrentUser(), type);
    }

    private void handleGetPlanningTasks(final ResponseEvent event) {
        String eventObject = event.getData();
        PlanTasksAdapter.getInstance().getPlanTasksAdapter(eventObject);
    }

    private void initViewListData(ArrayList<Z3PlanTask> planTaskListByNet) {
        if (null != planTaskListByNet && planTaskListByNet.size() > 0) {
            if (ListUtil.isEmpty(planTaskListByNet)) {
                LoadingDialogUtil.dismiss();
                mPullListView.onPullDownRefreshComplete();
                mPullListView.onPullUpRefreshComplete();
                mPullListView.setLastUpdateTime();
                if (getIntent().getIntExtra("isContents", SessionManager.isContent) == 2) {
                    ToastUtil.showLong(this.getResources().getString(R.string.get_no_ff_planningtask));
                    return;
                } else if (getIntent().getIntExtra("isContents", SessionManager.isContent) == 1) {
                    ToastUtil.showLong(this.getResources().getString(R.string.get_no_patrol_planningtask));
                    return;
                }
            }
            mAdapter = new PlanningTaskListAdapter(this, isConserveTask);
            mListView.setAdapter(mAdapter);
            mAdapter.setList(planTaskListByNet);
            mPlanTaskList = planTaskListByNet;
            SessionManager.CurrentPlanTasks = planTaskListByNet;
        } else {
            ToastUtil.showShort(ResourceUtil.getStringById(R.string.get_no_planningtask));
        }
        LoadingDialogUtil.dismiss();
        mPullListView.onPullDownRefreshComplete();
        mPullListView.onPullUpRefreshComplete();
        mPullListView.setLastUpdateTime();
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
        RefWatcher refWatcher = HostApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }

    public void onBackButtonClicked(View v) {
        finish();
    }

    /**
     * EventBus methods begin.
     */
    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            mPullListView.onPullDownRefreshComplete();
            mPullListView.onPullUpRefreshComplete();
            mPullListView.setLastUpdateTime();
            ToastUtil.showLong(event.getMessage());
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.PLANNINGTASK_LIST:
                handleGetPlanningTasks(event);
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(UIEvent event) {
        LoadingDialogUtil.dismiss();
        mPullListView.onPullDownRefreshComplete();
        mPullListView.onPullUpRefreshComplete();
        mPullListView.setLastUpdateTime();
        if (event == null) {
            return;
        }
        switch (event.getId()) {
            case UIEventStatus.PLANNINGTASK_POINT_PART_NOTIFICATION:
                if (null != mAdapter) {
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case UIEventStatus.PLANNINGTASK_ADAPTER_TASK:
                ArrayList<Z3PlanTask> PlanTaskListByNet = event.getData();
                initViewListData(PlanTaskListByNet);
                break;
            case UIEventStatus.PLANNINGTASK_NO_DATA:
                ToastUtil.showShort(ResourceUtil.getStringById(R.string.get_no_planningtask));
                if (null != mAdapter) {
                    ArrayList<Z3PlanTask> planTaskList = new ArrayList<Z3PlanTask>();
                    mAdapter.setList(planTaskList);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }

    /**
     * EventBus methods end.
     */

    //隐藏反馈项
    public void hideFeedbackItem() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_feedback);
        ll.setVisibility(View.GONE);
    }

    private void clearBadgeViewCount() {
        if (getIntent().getIntExtra("isContents", SessionManager.isContent) == 2) {
            PropertyChangeManager.getInstance().clearNoticationByKey(NotificationType.yh_badge_view_key.toString());
        } else if (getIntent().getIntExtra("isContents", SessionManager.isContent) == 1) {
            PropertyChangeManager.getInstance().clearNoticationByKey(NotificationType.xj_badge_view_key.toString());
        }
    }

    private void getPlanIdFromCache() {
        ACache aCache = ACache.get(PlanningTaskActivity.this);
        String notificationItemsId = aCache.getAsString(TodoFragment.NOTIFICATION_ITEMS_ID);
        if (null != notificationItemsId) {
            String strArray[] = notificationItemsId.split(",");
            if (strArray.length > 1) {
                planIdFromNotification = strArray[0];
            }
        }
    }

    private void clearNotification(String planId) {
        if (null != planIdFromNotification && planIdFromNotification.equals(planId)) {
            NotificationUtil.clearNotification(PlanningTaskActivity.this);
        }
    }
}