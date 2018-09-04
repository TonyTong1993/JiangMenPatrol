package com.ecity.cswatersupply.workorder.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.BuildConfig;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.ui.activities.WorkOrderDetailFragmentActivity;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.PropertyChangeManager;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.WorkOrderSearchActivity;
import com.ecity.cswatersupply.workorder.adpter.WorkOrderAdapter;
import com.ecity.cswatersupply.workorder.adpter.WorkOrderAdapter.IExpandListener;
import com.ecity.cswatersupply.workorder.data.Injection;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderGroupEnum;
import com.ecity.cswatersupply.workorder.presenter.WorkOrderPresenter;
import com.ecity.cswatersupply.workorder.view.WorkOrderContract.Presenter;
import com.ecity.cswatersupply.workorder.widght.AudioTextView;
import com.ecity.cswatersupply.xg.model.NotificationType;
import com.lee.pullrefresh.ui.PullRefreshExpandableListView;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.squareup.leakcanary.watcher.Preconditions;
import com.squareup.leakcanary.watcher.RefWatcher;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

/**
 * 工单处置页面
 * 
 * @author gaokai
 *
 */
public class WorkOrderActivity extends BaseActivity implements WorkOrderContract.View, OnChildClickListener, OnRefreshListener<ExpandableListView> {
    private AudioTextView statusBar;
    private WorkOrderAdapter mAdapter;
    private CustomTitleView viewTitle;
    private ExpandableListView expandableListView;
    private WorkOrderContract.Presenter mPresenter;
    private PullRefreshExpandableListView mPullExpandableListView;
    private String nextStepId;
    private static final int TIME_STATUSBAR_SHOW = 2 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workorder_new);
        EventBusUtil.register(this);
        new WorkOrderPresenter("", Injection.provideTasksRepository(getApplicationContext()), this);
        initTitle();
        initView();
        bindEvents();
        initData();
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
        if (BuildConfig.DEBUG) {
            // 测试有无内存泄漏，打包时要删除掉
            RefWatcher refWatcher = HostApplication.getRefWatcher(this);
            refWatcher.watch(this);
        }
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
        mPresenter.onLoadingNewData(true);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.mPresenter = Preconditions.checkNotNull(presenter, "WorkOrderContract.Presenter");
    }

    @Override
    public void updateList(List<List<WorkOrder>> mergedWorkorders) {
        mAdapter.setChildrenList(mergedWorkorders);
    }

    @Override
    public void showStatusBar(int msgResId) {
        String msg = ResourceUtil.getStringById(msgResId);
        showStatusBar(msg);
    }

    @Override
    public void showStatusBar(CharSequence msg) {
        if (statusBar.getVisibility() == View.VISIBLE) {
            statusBar.setVisibility(View.GONE);
        }
        setStatusBarParams(msg);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (this != null && statusBar.getVisibility() == View.VISIBLE) {
                    statusBar.setAnimation(AnimationUtils.loadAnimation(WorkOrderActivity.this, R.anim.status_bar_out));
                    statusBar.setVisibility(View.GONE);
                }
            }
        }, TIME_STATUSBAR_SHOW);
    }

    @Override
    public void showCompletionStatus() {
        mPullExpandableListView.onPullDownRefreshComplete();
        mPullExpandableListView.onPullUpRefreshComplete();
        mPullExpandableListView.setLastUpdateTime();
        if (!StringUtil.isBlank(nextStepId)) {
            scrollToWorkOrder(nextStepId, WorkOrder.KEY_ID);
            nextStepId = null;
        }
    }

    @Override
    public void showTop(List<List<WorkOrder>> workOrders) {
        updateList(workOrders);
        expandableListView.setSelection(0);
    }

    @Override
    public void showLoadingDialog(int msgResId) {
        LoadingDialogUtil.show(this, msgResId);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        boolean isComlete = WorkOrderGroupEnum.values()[groupPosition] == WorkOrderGroupEnum.COMLETE;
        Bundle workOrder = new Bundle();
        workOrder.putSerializable(WorkOrder.KEY_SERIAL, mAdapter.getChild(groupPosition, childPosition));
        workOrder.putBoolean("isCompletedOrder", isComlete);// 是否是完结工单
        UIHelper.startActivityWithExtra(WorkOrderDetailFragmentActivity.class, workOrder);
        return false;
    }

    private void setStatusBarParams(CharSequence msg) {
        statusBar.setText(msg);
        statusBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.status_bar_in));
        statusBar.setVisibility(View.VISIBLE);
    }

    private void initTitle() {
        viewTitle = (CustomTitleView) findViewById(R.id.customTitleView1);
        viewTitle.setTitleText(ResourceUtil.getStringById(R.string.WorkOrder_title));
        viewTitle.setBtnStyle(CustomTitleView.BtnStyle.ONLY_BACK);
    }

    private void initView() {
        this.statusBar = (AudioTextView) findViewById(R.id.tv_status_bar);
        mPullExpandableListView = (PullRefreshExpandableListView) findViewById(R.id.expandableListView1);
        mPullExpandableListView.setOnRefreshListener(this);
        mAdapter = new WorkOrderAdapter(this, new IExpandListener() {

            @Override
            public void onExpand(int position) {
                mPresenter.setGroupPosition(position);
            }
        });
        expandableListView = mPullExpandableListView.getRefreshableView();
        // ExpandableListView设置adapter之前，要先设置GroupList
        initExpandableListViewData();
        expandableListView.setAdapter(mAdapter);
        expandableListView.setOnChildClickListener(this);
        mPullExpandableListView.doPullRefreshing(true, 500);
        PropertyChangeManager.getInstance().clearNoticationByKey(NotificationType.workOrder.toString());
    }

    private void bindEvents() {
        findViewById(R.id.ll_searchview).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearchWorkOrders();
            }
        });
    }

    private void initExpandableListViewData() {
        // 这里以后要读取数据库，先放入上次的工单
        WorkOrderGroupEnum[] temp = WorkOrderGroupEnum.values();
        List<WorkOrderGroupEnum> groupData = new ArrayList<WorkOrderGroupEnum>(Arrays.asList(temp));
        mAdapter.setGroupList(groupData);
        List<List<WorkOrder>> childrenList = new ArrayList<List<WorkOrder>>();
        for (int i = 0; i < groupData.size(); i++) {
            childrenList.add(new ArrayList<WorkOrder>());
        }
        mAdapter.setChildrenList(childrenList);
    }

    private void handleAcceptEvent(ResponseEvent event) {
        if (event.getStatus() == ResponseEventStatus.ERROR) {
            ToastUtil.showShort(event.getMessage());
            return;
        }

        LoadingDialogUtil.dismiss();
        showStatusBar(ResourceUtil.getStringById(R.string.accept_workorder_status));
        mPresenter.onLoadingNewData(false);
    }

    private void gotoSearchWorkOrders() {
        if (mAdapter == null) {
            return;
        }

        if (ListUtil.isEmpty(mAdapter.getChildrenList())) {
            return;
        }

        List<WorkOrder> workOrders = getWorkOrderSearchSource(mAdapter.getChildrenList());
        Bundle bundle = new Bundle();
        bundle.putSerializable(WorkOrderSearchActivity.INTENT_KEY_WORK_ORDERS, (Serializable) workOrders);
        UIHelper.startActivityWithExtra(WorkOrderSearchActivity.class, bundle);
    }

    private List<WorkOrder> getWorkOrderSearchSource(List<List<WorkOrder>> groupedWorkOrders) {
        List<WorkOrder> workOrders = new ArrayList<WorkOrder>();
        for (List<WorkOrder> workOrderGroup : groupedWorkOrders) {
            workOrders.addAll(workOrderGroup);
        }

        return workOrders;
    }

    private void initData() {
        nextStepId = getIntent().getStringExtra(Constants.INTENT_KEY_NEXT_STEP_ID);
    }

    public void onEventMainThread(ResponseEvent event) {
        switch (event.getId()) {
            case ResponseEventStatus.WORKORDER_DOWN_ALL_WORKORDER:
                mPresenter.handleDownloadedWorkOrder(R.string.workorder_new, event);
                break;
            case ResponseEventStatus.WORKORDER_ACCEPT_EVENT:
                handleAcceptEvent(event);
                break;
            case ResponseEventStatus.WORKORDER_CANCEL_DELAY:
                showStatusBar(ResourceUtil.getStringById(R.string.cancel_delay_workorder_status));
                mPresenter.onLoadingNewData(false);
                break;
            case ResponseEventStatus.WORKORDER_CANCEL_ASSIST:
                showStatusBar(ResourceUtil.getStringById(R.string.cancel_assist_workorder_status));
                mPresenter.onLoadingNewData(false);
                break;
            case ResponseEventStatus.WORKORDER_CANCEL_TRANSFER:
                showStatusBar(ResourceUtil.getStringById(R.string.cancel_transfer_workorder_status));
                mPresenter.onLoadingNewData(false);
                break;
            case ResponseEventStatus.WORKORDER_CANCEL_RETURN:
                showStatusBar(ResourceUtil.getStringById(R.string.cancel_return_workorder_status));
                mPresenter.onLoadingNewData(false);
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(UIEvent event) {
        switch (event.getId()) {
            case UIEventStatus.ON_LOAD_DATA_SUCCESS:
                Object obj = event.getData();
                mPresenter.updateUI(R.string.workorder_new, obj);
                break;
            case UIEventStatus.ON_LOAD_DATA_ERROR:
                String msg = event.getMessage();
                mPresenter.updateUI(R.string.workorder_new, null);
                showStatusBar(msg);
                break;
            case UIEventStatus.ON_LOAD_DATA_FINISH:
                showCompletionStatus();
                break;
            case UIEventStatus.WORKORDER_ACCEPT: // 从按钮传来
                mPresenter.applyAcceptWorkOrder((String) event.getData());
                break;
            case UIEventStatus.WORKORDER_COMMON_INSPECT_REPORT:// 所有检查项上报的回调
                showStatusBar(event.getMessage());
                mPresenter.onLoadingNewData(false);
                break;
            case UIEventStatus.WORKORDER_SEARCH_RESULT_CLICKED:
                String workOrderCode = event.getData();
                scrollToWorkOrder(workOrderCode, WorkOrder.KEY_CODE);
                break;
            default:
                break;
        }
    }

    private void scrollToWorkOrder(String attrValue, String attrKey) {
        List<List<WorkOrder>> workOrderGroups = mAdapter.getChildrenList();
        boolean isWorkOrderFound = false;
        for (int groupIndex = 0; groupIndex < workOrderGroups.size(); groupIndex++) {
            List<WorkOrder> workOrders = workOrderGroups.get(groupIndex);
            int childIndex = 0;
            for (childIndex = 0; childIndex < workOrders.size(); childIndex++) {
                WorkOrder workOrder = workOrders.get(childIndex);
                if (attrValue.equals(workOrder.getAttributes().get(attrKey))) {
                    isWorkOrderFound = true;
                    break;
                }
            }

            if (isWorkOrderFound) {
                expandableListView.expandGroup(groupIndex);
                expandableListView.setSelectedChild(groupIndex, childIndex, true);
                break;
            }
        }
    }
}
