package com.ecity.cswatersupply.workorder.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.WorkOrderSummaryDetailAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.ui.activities.WorkOrderDetailFragmentActivity;
import com.ecity.cswatersupply.ui.fragment.TodoFragment;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.ACache;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.PropertyChangeManager;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.WorkOrderSearchActivity;
import com.ecity.cswatersupply.workorder.adpter.WorkOrderListAdapter;
import com.ecity.cswatersupply.workorder.menu.WorkOrderPopupFilterMenu;
import com.ecity.cswatersupply.workorder.menu.WorkOrderPopupFilterMenu.OnMenuClickListener;
import com.ecity.cswatersupply.workorder.menu.WorkOrderPopupFilterMenu.OnPopupMenuDismissListener;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderGroupEnum;
import com.ecity.cswatersupply.workorder.model.WorkOrderPieStaticsData;
import com.ecity.cswatersupply.workorder.model.WorkOrderPopupMenuModel;
import com.ecity.cswatersupply.workorder.widght.AudioTextView;
import com.ecity.cswatersupply.xg.model.NotificationType;
import com.ecity.cswatersupply.xg.util.NotificationUtil;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;
import com.zzz.ecity.android.applibrary.utils.ScreenUtil;

/**
 * 工单处置页面
 *
 * @author qiwei
 *
 */
public class CustomWorkOrderActivity extends BaseActivity {
    private AudioTextView statusBar;
    private WorkOrderListAdapter mAdapter;
    private ListView lvRecords;
    private CustomTitleView titleView;
    private String title;
    private TextView tvAction;
    private WorkOrderContract.Presenter mPresenter;
    private PullToRefreshListView refreshWorkOrderListView;
    private String nextStepId;
    private WorkOrderPieStaticsData pieData;
    private WorkOrderPopupFilterMenu popupMenu;
    private String nextFilter;
    private String orderBy;
    private int titleNameId;
    private static final int TIME_STATUSBAR_SHOW = 2 * 1000;
    private int groupPosition;
    private boolean isNeedBtnViews = true;
    private int pageNum;
    private int tempPage;
    private List<WorkOrder> workOrderList;
    private boolean isOperateFromWorkOrderBack = true;
    private boolean isFirstIn = true;
    private boolean isRequestDone = false;
    private String nextStepIdFromNotification;
    private boolean isExecuteProcess = false;
    private String workOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workorder_filter);
        EventBusUtil.register(this);
        getNextStepIdFromCache();
        initUI();
        initData();
        bindEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNextStepIdFromCache();
        if (pieData != null) {
            getPieData();
        } else {
            if (isFirstIn) {
                isFirstIn = false;
                refreshWorkOrderList();
            } else { //zzz 2017-06-02 由于完工上报之后工单不刷新  if (!isOperateFromWorkOrderBack)
                requestLastPageData();
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    public void showStatusBar(int msgResId) {
        String msg = ResourceUtil.getStringById(msgResId);
        showStatusBar(msg);
    }

    public void showStatusBar(CharSequence msg) {
        if (statusBar.getVisibility() == View.VISIBLE) {
            statusBar.setVisibility(View.GONE);
        }
        setStatusBarParams(msg);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (this != null && statusBar.getVisibility() == View.VISIBLE) {
                    statusBar.setAnimation(AnimationUtils.loadAnimation(CustomWorkOrderActivity.this, R.anim.status_bar_out));
                    statusBar.setVisibility(View.GONE);
                }
            }
        }, TIME_STATUSBAR_SHOW);
    }

    private void handleRefreshComplete() {
        refreshWorkOrderListView.onPullDownRefreshComplete();
        refreshWorkOrderListView.onPullUpRefreshComplete();
        refreshWorkOrderListView.setLastUpdateTime();
    }

    public void showCompletionStatus() {
        refreshWorkOrderListView.onPullDownRefreshComplete();
        refreshWorkOrderListView.onPullUpRefreshComplete();
        refreshWorkOrderListView.setLastUpdateTime();
        if (!StringUtil.isBlank(nextStepId)) {
            scrollToWorkOrder(nextStepId, WorkOrder.KEY_ID);
            nextStepId = null;
        }
    }

    private void setStatusBarParams(CharSequence msg) {
        statusBar.setText(msg);
        statusBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.status_bar_in));
        statusBar.setVisibility(View.VISIBLE);
    }

    public void onActionButtonClicked(View view) {
        showPopupMenu(view);
    }

    private void showPopupMenu(View view) {
        int width = (int) getResources().getDimension(R.dimen.workorder_popmenu_w_s);
        int xPos = ScreenUtil.getScreenWidth(this) - width;
        popupMenu.showAdDropDown(titleView.imgv_tag, xPos, (int) getResources().getDimension(R.dimen.activity_title_height));
    }

    private int getGroupPosition(String categoryName) {
        int groupTag = 0;
        for (WorkOrderPopupMenuModel p : WorkOrderPopupMenuModel.values()) {
            if (p.getTagName().equals(categoryName)) {
                groupTag = p.getTag();
            }
        }
        return groupTag;
    }

    private int getValidMenuId() {
        int id = 0;
        if (!StringUtil.isBlank(getIntent().getStringExtra(Constants.INTENT_KEY_NEXT_CATEGORY_NAME))) {
            if (titleNameId == WorkOrderGroupEnum.COMLETE.getTitle()) {
                id = R.array.workorder_pop_menu_finish;
            } else if (titleNameId == WorkOrderGroupEnum.HANDLLING.getTitle()) {
                id = R.array.workorder_pop_menu_operator;
            } else {
                id = R.array.workorder_pop_menu_todo;
            }
        } else {
            int groupPosition = getIntent().getExtras().getInt(Constants.WORK_ORDER_GROUP_POSITION);
            switch (groupPosition) {
                case 0:
                    id = R.array.workorder_pop_menu_todo;
                    break;
                case 1:
                    id = R.array.workorder_pop_menu_operator;
                    break;
                case 2:
                    id = R.array.workorder_pop_menu_finish;
                    break;
            }
        }
        return id;
    }

    private void bindEvents() {
        findViewById(R.id.ll_searchview).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearchWorkOrders();
            }
        });

        lvRecords.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotificationUtil.clearNotificationById(CustomWorkOrderActivity.this, NotificationUtil.SPECIAL_NOTIFICATION);
                mAdapter.setSelectedItemPosition(position);
                mAdapter.notifyDataSetInvalidated();
                int groupPosition = getIntent().getExtras().getInt(Constants.WORK_ORDER_GROUP_POSITION);
                if (!StringUtil.isBlank(getIntent().getStringExtra(Constants.INTENT_KEY_NEXT_CATEGORY_NAME))){
                    if(Constants.EXCUTE_PEOCESS.contains(getIntent().getStringExtra(Constants.INTENT_KEY_NEXT_CATEGORY_NAME))){
                        isExecuteProcess = true;
                    }
                }
                boolean isComlete = WorkOrderGroupEnum.values()[groupPosition] == WorkOrderGroupEnum.COMLETE;
                Bundle workOrder = new Bundle();
                workOrder.putSerializable(WorkOrder.KEY_SERIAL, mAdapter.getList().get(position));
                workOrder.putBoolean("isCompletedOrder", isComlete);// 是否是完结工单
                workOrder.putBoolean("isExcuteProcessWorkOrder", isExecuteProcess);// 是否是属于正在处理中的工单
                UIHelper.startActivityWithExtra(WorkOrderDetailFragmentActivity.class, workOrder);
            }
        });

        refreshWorkOrderListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (pieData != null) {
                    getPieData();
                } else {
                    refreshWorkOrderList();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (pieData != null) {
                    getPieData();
                } else {
                    requestData();
                }
            }
        });
    }

    private void requestData() {
        LoadingDialogUtil.show(this, ResourceUtil.getStringById(R.string.workorder_downloading));
        WorkOrderService.instance.downLoadWorkOrdersByFilter(nextFilter, pageNum, orderBy);
        pageNum ++;
    }

    private void initPopView() {
        groupPosition = getIntent().getExtras().getInt(Constants.WORK_ORDER_GROUP_POSITION);
        tvAction.setText(getResources().getString(R.string.menu_workorder_default));
        Drawable nav_up = getResources().getDrawable(R.drawable.arrow_down_white);
        nav_up.setBounds(0, 3, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        tvAction.setCompoundDrawables(null, null, nav_up, null);
        tvAction.setCompoundDrawablePadding(5);
        popupMenu = new WorkOrderPopupFilterMenu(this);
        int width = ResourceUtil.getDimensionPixelSizeById(R.dimen.workorder_popmenu_w);
        popupMenu.initPopup(tvAction, width, LayoutParams.WRAP_CONTENT);
        String categoryName = getIntent().getStringExtra(Constants.INTENT_KEY_NEXT_CATEGORY_NAME);
        if (!StringUtil.isBlank(categoryName)) {
            groupPosition = getGroupPosition(categoryName);
        }
        popupMenu.setMenu(getValidMenuId(), groupPosition);
        if (pieData != null) {
            tvAction.setVisibility(View.GONE);
        }
        popupMenu.setOnActionItemClickListener(new OnMenuClickListener() {

            @Override
            public void onMenuItemClick(WorkOrderPopupMenuModel menu, int pos) {
                tvAction.setText(menu.getName());
                nextFilter = menu.getTagName();
                refreshWorkOrderList();
            }
        });
        popupMenu.setOnPopupMenuDismissListener(new OnPopupMenuDismissListener() {

            @Override
            public void onDismiss() {
            }
        });
    }

    private void initUI() {
        titleView = (CustomTitleView) findViewById(R.id.customTitleView1);
        titleView.setBtnStyle(CustomTitleView.BtnStyle.RIGHT_ACTION);
        refreshWorkOrderListView = (PullToRefreshListView) findViewById(R.id.commonListView1);
        refreshWorkOrderListView.setPullLoadEnabled(true);
        refreshWorkOrderListView.setPullRefreshEnabled(true);
        lvRecords = refreshWorkOrderListView.getRefreshableView();
        lvRecords.setDivider(ResourceUtil.getDrawableResourceById(R.drawable.shape_list_divider));
        tvAction = (TextView) titleView.findViewById(R.id.tv_action);
        this.statusBar = (AudioTextView) findViewById(R.id.tv_status_bar);
        PropertyChangeManager.getInstance().clearNoticationByKey(NotificationType.workOrder.toString());
    }

    private void initTitle() {
        if (!StringUtil.isBlank(getIntent().getStringExtra(Constants.INTENT_KEY_NEXT_CATEGORY_NAME))) {
            setTitleNameByNotification();
        } else {
            titleView.setTitleText(title);
        }
    }

    private void setTitleNameByNotification() {
        String titleText = getIntent().getStringExtra(Constants.INTENT_KEY_NEXT_CATEGORY_NAME);
        for (WorkOrderPopupMenuModel menu : WorkOrderPopupMenuModel.values()) {
            if (menu.getTagName().endsWith(titleText)) {
                if (Constants.WORKORDER_FINISH_CATEGORIES.contains(titleText)) {
                    titleNameId = WorkOrderGroupEnum.COMLETE.getTitle();
                    titleView.setTitleText(titleNameId);
                } else if (Constants.WORKORDER_OPERATOR_CATEGORIES.contains(titleText)) {
                    titleNameId = WorkOrderGroupEnum.HANDLLING.getTitle();
                    titleView.setTitleText(titleNameId);
                } else {
                    titleNameId = WorkOrderGroupEnum.NOHANDLE.getTitle();
                    titleView.setTitleText(titleNameId);
                }
            }
        }
    }

    private void handleAcceptEvent(ResponseEvent event) {
        if (event.getStatus() == ResponseEventStatus.ERROR) {
            ToastUtil.showShort(event.getMessage());
            return;
        }
        LoadingDialogUtil.dismiss();
        showStatusBar(ResourceUtil.getStringById(R.string.accept_workorder_status));
        getProcessInstanceIdFromReport(event);
        isOperateFromWorkOrderBack = false;
        requestLastPageData();
    }

    private void gotoSearchWorkOrders() {
        if (mAdapter == null) {
            return;
        }

        if (ListUtil.isEmpty(mAdapter.getList())) {
            return;
        }

        List<WorkOrder> workOrders = getWorkOrderSearchSource(mAdapter.getList());
        Bundle bundle = new Bundle();
        bundle.putSerializable(WorkOrderSearchActivity.INTENT_KEY_WORK_ORDERS, (Serializable) workOrders);
        bundle.putInt(Constants.WORK_ORDER_GROUP_POSITION, getIntent().getExtras().getInt(Constants.WORK_ORDER_GROUP_POSITION));
        UIHelper.startActivityWithExtra(WorkOrderSearchActivity.class, bundle);
    }

    private List<WorkOrder> getWorkOrderSearchSource(List<WorkOrder> groupedWorkOrders) {
        List<WorkOrder> workOrders = new ArrayList<WorkOrder>();
        for (WorkOrder workOrderGroup : groupedWorkOrders) {
            workOrders.add(workOrderGroup);
        }

        return workOrders;
    }

    private void initData() {
        //来自搜索页面的nextStepId
        if (!StringUtil.isBlank(nextStepId = getIntent().getStringExtra(Constants.INTENT_KEY_NEXT_STEP_ID))) {
            nextStepId = getIntent().getStringExtra(Constants.INTENT_KEY_NEXT_STEP_ID);
        } else {
            nextStepId = "";
        }

        if (!StringUtil.isBlank(workOrderId = getIntent().getStringExtra(WorkOrder.KEY_CODE))) {
            workOrderId = getIntent().getStringExtra(WorkOrder.KEY_CODE);
        } else {
            workOrderId = "";
        }
        //来自点击消息通知栏的nextStepId
        if (!StringUtil.isBlank(getIntent().getStringExtra(Constants.INTENT_KEY_GOUP_NEXT_STEP_ID))) {
            nextStepId = getIntent().getStringExtra(Constants.INTENT_KEY_GOUP_NEXT_STEP_ID);
        }  else {
            nextStepId = "";
        }

        if (!StringUtil.isBlank(getIntent().getStringExtra(Constants.INTENT_KEY_NEXT_CATEGORY_NAME))) {
            nextFilter = getIntent().getStringExtra(Constants.INTENT_KEY_NEXT_CATEGORY_NAME);
        } else {
            nextFilter = "";
        }

        if (!StringUtil.isBlank(getIntent().getStringExtra(Constants.WORK_ORDER_GROUP_ORDER_BY))) {
            orderBy = getIntent().getStringExtra(Constants.WORK_ORDER_GROUP_ORDER_BY);
        }
        if (getIntent().getSerializableExtra(Constants.WORKORDER_SUMMARY_DETAIL) != null) {
            pieData = (WorkOrderPieStaticsData) getIntent().getSerializableExtra(Constants.WORKORDER_SUMMARY_DETAIL);
            if (getIntent().getStringExtra(WorkOrderSummaryDetailAdapter.TIELE_FORMAT_SUMMARY_DETAIL) != null) {
                title = getIntent().getStringExtra(WorkOrderSummaryDetailAdapter.TIELE_FORMAT_SUMMARY_DETAIL);
            } else {
                title = pieData.getCategory();
            }
            isNeedBtnViews = false;
        } else {
            title = ResourceUtil.getStringById(getIntent().getIntExtra(Constants.WORK_ORDER_GROUP_TILE, 0));
        }
        workOrderList = new ArrayList<WorkOrder>();
        initTitle();
        initPopView();
        if (groupPosition == 0) {
            groupPosition = getIntent().getExtras().getInt(Constants.WORK_ORDER_GROUP_POSITION);
        }
        mAdapter = new WorkOrderListAdapter(this, workOrderList, groupPosition, isNeedBtnViews, nextStepIdFromNotification);
        lvRecords.setAdapter(mAdapter);
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.WORKORDER_DOWN_ALL_WORKORDER:
                mPresenter.handleDownloadedWorkOrder(R.string.workorder_new, event);
                break;
            case ResponseEventStatus.WORKORDER_DOWN_WORKORER_SUMMARY_DETAIL:
                handleFilterWorkOrder(event);
                break;
            case ResponseEventStatus.WORKORDER_DOWN_FILTER_WORKORDER:
                handleFilterWorkOrder(event);
                break;
            case ResponseEventStatus.WORKORDER_ACCEPT_EVENT:
                handleAcceptEvent(event);
                break;
            case ResponseEventStatus.WORKORDER_CANCEL_DELAY:
                showStatusBar(ResourceUtil.getStringById(R.string.cancel_delay_workorder_status));
                handleReportProcessItem(event);
                break;
            case ResponseEventStatus.WORKORDER_CANCEL_ASSIST:
                showStatusBar(ResourceUtil.getStringById(R.string.cancel_assist_workorder_status));
                handleReportProcessItem(event);
                break;
            case ResponseEventStatus.WORKORDER_CANCEL_TRANSFER:
                showStatusBar(ResourceUtil.getStringById(R.string.cancel_transfer_workorder_status));
                handleReportProcessItem(event);
                break;
            case ResponseEventStatus.WORKORDER_CANCEL_RETURN:
                showStatusBar(ResourceUtil.getStringById(R.string.cancel_return_workorder_status));
                handleReportProcessItem(event);
                break;
            case ResponseEventStatus.WORKORDER_BACK_TRANSFER_CANCEL:
                showStatusBar(ResourceUtil.getStringById(R.string.cancel_back_transfer_workorder_status));
                handleReportProcessItem(event);
                break;
            case ResponseEventStatus.WORKORDER_DOWN_By_WORKORDER_ID:
                handleGetWorkOrderByWorkOrderId(event);
                break;
            default:
                break;
        }
    }

    private void handleReportProcessItem(ResponseEvent event) {
        if (event.getStatus() == ResponseEventStatus.ERROR) {
            ToastUtil.showShort(event.getMessage());
            return;
        }
        LoadingDialogUtil.dismiss();
        getProcessInstanceIdFromReport(event);
        //刷新界面
        isOperateFromWorkOrderBack = false;
        requestLastPageData();
    }

    private void handleFilterWorkOrder(ResponseEvent event) {
        List<WorkOrder> subworkOrderList = event.getData();
        if (!isOperateFromWorkOrderBack) {
            isOperateFromWorkOrderBack = !isOperateFromWorkOrderBack;
            if (isRequestDone) {
                workOrderList.clear();
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
            if (subworkOrderList.size() == 0) {
                if (isRequestDone) {
                    ToastUtil.showShort(getResources().getString(R.string.menu_workorder_blank_content));
                } else {
                    ToastUtil.showShort(getResources().getString(R.string.menu_workorder_no_more));
                    handleRefreshComplete();
                }
                LoadingDialogUtil.dismiss();
                return;
            }
        } else {
            if (pageNum == 2) {
                workOrderList.clear();
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
            if (subworkOrderList.size() == 0) {
                if (pageNum == 2) {
                    ToastUtil.showShort(getResources().getString(R.string.menu_workorder_blank_content));
                } else {
                    ToastUtil.showShort(getResources().getString(R.string.menu_workorder_no_more));
                    handleRefreshComplete();
                }
                LoadingDialogUtil.dismiss();
                return;
            } else {
                tempPage = pageNum - 1;
            }
        }
        workOrderList.addAll(subworkOrderList);

        if (!isSearchResultInCurrentWorkOrderList(workOrderList)) {
            WorkOrderService.instance.downLoadWorkOrdersByWorkOderId(nextFilter, 0, orderBy, workOrderId);
            workOrderId = null;
        } else {
            setListViewDataAndRefreshUI();
        }
    }

    public void onEventMainThread(UIEvent event) {
        switch (event.getId()) {
            case UIEventStatus.ON_LOAD_DATA_SUCCESS:
                handleRefreshComplete();
                break;
            case UIEventStatus.ON_LOAD_DATA_ERROR:
                String msg = event.getMessage();
                showStatusBar(msg);
                break;
            case UIEventStatus.ON_LOAD_DATA_FINISH:
                showCompletionStatus();
                break;
            case UIEventStatus.WORKORDER_ACCEPT: // 从按钮传来
                handleApplyAcceptWorkOrder((String) event.getData());
                handleRefreshComplete();
                break;
            case UIEventStatus.WORKORDER_COMMON_INSPECT_REPORT:// 所有检查项上报的回调
                showStatusBar(event.getMessage());
                handleRefreshComplete();
                break;
            case UIEventStatus.WORKORDER_SEARCH_RESULT_CLICKED:
                nextStepId = event.getData();
                break;
            case UIEventStatus.WORKORDER_OPERATE_BACK://由工单详情页面以及工单上报页面 响应返回按钮返回的
                isOperateFromWorkOrderBack = true;
                break;
            case UIEventStatus.WORKORDER_OPERATE_REPORT://由工单上报页面 响应上报按钮返回的
                isOperateFromWorkOrderBack = false;
                nextStepId = event.getData();
                break;
            default:
                break;
        }
    }

    private void handleApplyAcceptWorkOrder(String gid) {
        LoadingDialogUtil.show(this, getResources().getString(R.string.accept_workorder));
        Map<String, String> params = new HashMap<String, String>();
        params.put(WorkOrder.KEY_ID, gid);
        JSONObject paramJson = new JSONObject();
        try {
            paramJson.put("userid", HostApplication.getApplication().getCurrentUser().getId());
            paramJson.putOpt(WorkOrder.KEY_STATE, getCurrentWorkOrder(gid).getAttributes().get(WorkOrder.KEY_STATE));
        } catch (JSONException e) {
            LogUtil.e(this, e);
        }
        params.put("properties", paramJson.toString());
        WorkOrderService.instance.handleWorkOrder(ServiceUrlManager.getInstance().getSubmitFormDataUrl(), ResponseEventStatus.WORKORDER_ACCEPT_EVENT, params);
    }

    private WorkOrder getCurrentWorkOrder(String gid) {
        for (WorkOrder workOrder : mAdapter.getList()) {
            String currentGid = workOrder.getAttributes().get(WorkOrder.KEY_ID);
            if (gid.equals(currentGid)) {
                return workOrder;
            }
        }
        return null;
    }

    private void scrollToWorkOrder(String attrValue, String attrKey) {
        List<WorkOrder> workOrders = mAdapter.getList();
        boolean isWorkOrderFound = false;
        int childIndex = 0;
        for (childIndex = 0; childIndex < workOrders.size(); childIndex++) {
            WorkOrder workOrder = workOrders.get(childIndex);
            if (attrValue.equals(workOrder.getAttributes().get(attrKey))) {
                isWorkOrderFound = true;
            }
            if (isWorkOrderFound) {
                lvRecords.setSelection(childIndex);
                mAdapter.setSelectedItemPosition(childIndex);
                mAdapter.notifyDataSetInvalidated();
                break;
            }
        }
    }

    private void refreshWorkOrderList() {
        pageNum = 1;
        requestData();
    }

    private void getPieData() {
        pageNum = 1;
        LoadingDialogUtil.show(this, ResourceUtil.getStringById(R.string.workorder_downloading));
        WorkOrderService.instance.getSummaryDetailWorkOrders(pieData, false);
        pageNum++;
    }

    /**
     * 请求上一次被操作（工单流转、上报等操作）工单所在的页的数据
     */
    private void requestLastPageData() {
        isRequestDone = false;
        LoadingDialogUtil.show(this, ResourceUtil.getStringById(R.string.workorder_downloading));
        WorkOrderService.instance.downLoadWorkOrdersByFilter(nextFilter, tempPage, orderBy);
        isRequestDone = true;
    }

    /**
     * 获取工单流转时当前工单的processInstanceId
     */
    private void getProcessInstanceIdFromReport(ResponseEvent event) {
        JSONObject jsonObject = event.getData();
        nextStepId = jsonObject.optString("processInstanceId");
    }

    private void getNextStepIdFromCache() {
        ACache aCache = ACache.get(CustomWorkOrderActivity.this);
        nextStepIdFromNotification = aCache.getAsString(TodoFragment.NOTIFICATION_ITEMS_ID);
    }

    private void clearNotification(String nextStepId) {
        if (null != nextStepIdFromNotification && nextStepIdFromNotification.equals(nextStepId)) {
            NotificationUtil.clearNotification(CustomWorkOrderActivity.this);
        }
    }

    private boolean isSearchResultInCurrentWorkOrderList(List<WorkOrder> workOrders) {
        if (StringUtil.isBlank(workOrderId)) {
            return true;
        }
        for (WorkOrder workOrder : workOrders) {
            if (workOrderId.equals(workOrder.getAttributes().get(WorkOrder.KEY_CODE))) {
                return true;
            }
        }
        return false;
    }

    private void handleGetWorkOrderByWorkOrderId(ResponseEvent event) {
        List<WorkOrder> tempWorkOrderList = new ArrayList<>();
        List<WorkOrder> searchWorkOrderList = event.getData();
        WorkOrder searchWorkOrder = searchWorkOrderList.get(0);
        tempWorkOrderList.add(0, searchWorkOrder);
        tempWorkOrderList.addAll(workOrderList);
        workOrderList.clear();
        workOrderList.addAll(tempWorkOrderList);
        tempWorkOrderList.clear();

        setListViewDataAndRefreshUI();
    }

    private void setListViewDataAndRefreshUI() {
        LoadingDialogUtil.dismiss();
        mAdapter.setList(workOrderList);
        mAdapter.notifyDataSetChanged();
        showCompletionStatus();
    }
}
