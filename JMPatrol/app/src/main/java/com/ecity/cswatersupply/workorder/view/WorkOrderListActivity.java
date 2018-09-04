package com.ecity.cswatersupply.workorder.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.ui.activities.WorkOrderDetailFragmentActivity1;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.PropertyChangeManager;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.WorkOrderSearchActivity1;
import com.ecity.cswatersupply.workorder.adpter.WorkOrderListAdapter1;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.xg.model.NotificationType;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 工单列表界面
 * Gxx 2017-04-06
 */
public class WorkOrderListActivity extends BaseActivity {
    public final static int WORK_ORDER_SEARCH_REQUEST_CODE = 0;
    private WorkOrderListAdapter1 mAdapter;
    private ListView lvRecords;
    private CustomTitleView titleView;
    private PullToRefreshListView refreshWorkOrderListView;
    private boolean isNeedBtnViews = true;
    private int pageNum;
    private List<WorkOrder> workOrderList;
    private WorkOrder searchedWorkOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workorder_filter);
        EventBusUtil.register(this);
        initUI();
        bindEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(null == searchedWorkOrder) {
            refreshWorkOrderList();
        } else {
            scrollToWorkOrder(searchedWorkOrder);
        }
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == WORK_ORDER_SEARCH_REQUEST_CODE) {
            searchedWorkOrder = (WorkOrder) data.getExtras().getSerializable("workOrder");
        }
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    private void handleRefreshComplete() {
        refreshWorkOrderListView.onPullDownRefreshComplete();
        refreshWorkOrderListView.onPullUpRefreshComplete();
        refreshWorkOrderListView.setLastUpdateTime();
    }

    private void bindEvents() {
        findViewById(R.id.ll_searchview).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkOrderListActivity.this, WorkOrderSearchActivity1.class);
                startActivityForResult(intent, WORK_ORDER_SEARCH_REQUEST_CODE);
            }
        });

        lvRecords.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WorkOrder workOrder = mAdapter.getList().get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(WorkOrder.KEY_SERIAL, workOrder);
                UIHelper.startActivityWithExtra(WorkOrderDetailFragmentActivity1.class, bundle);
            }
        });

        refreshWorkOrderListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshWorkOrderList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestData();
            }
        });
    }

    private void requestData() {
        LoadingDialogUtil.show(this, ResourceUtil.getStringById(R.string.workorder_downloading));
        pageNum ++;
        WorkOrderService.instance.queryWorkOrders(pageNum, 15, "","");
    }

    private void refreshWorkOrderList() {
        pageNum = 0;
        workOrderList.clear();
        requestData();
    }

    private void initUI() {
        initTitle();
        refreshWorkOrderListView = (PullToRefreshListView) findViewById(R.id.commonListView1);
        refreshWorkOrderListView.setPullLoadEnabled(true);
        refreshWorkOrderListView.setPullRefreshEnabled(true);
        lvRecords = refreshWorkOrderListView.getRefreshableView();
        lvRecords.setDivider(ResourceUtil.getDrawableResourceById(R.drawable.shape_list_divider));

        workOrderList = new ArrayList<>();
        PropertyChangeManager.getInstance().clearNoticationByKey(NotificationType.workOrder.toString());
    }

    private void initTitle() {
        titleView = (CustomTitleView) findViewById(R.id.customTitleView1);
        titleView.setBtnStyle(CustomTitleView.BtnStyle.ONLY_BACK);
        titleView.setTitleText("工单列表");
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.WORKORDER_QUERY_WORKORDER_LIST:
                handleWorkOrderList(event);
                break;
            default:
                break;
        }
    }

    private void handleWorkOrderList(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        List<WorkOrder> subworkOrderList = event.getData();
        if (subworkOrderList.size() == 0) {
            if(pageNum == 1) {
                ToastUtil.showShort(getResources().getString(R.string.menu_workorder_blank_content));
            } else {
                ToastUtil.showShort(getResources().getString(R.string.menu_workorder_no_more));
            }
            return;
        }
        workOrderList.addAll(subworkOrderList);
        mAdapter = new WorkOrderListAdapter1(this, workOrderList, isNeedBtnViews);
        lvRecords.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        handleRefreshComplete();
    }

    /****
     * 定位到搜索点击的工单
     * @param order
     */
    private void scrollToWorkOrder(WorkOrder order) {
        List<WorkOrder> workOrders = mAdapter.getList();
        boolean isWorkOrderFound = false;
        for (int childIndex = 0; childIndex < workOrders.size(); childIndex++) {
            WorkOrder workOrder = workOrders.get(childIndex);
            String code1 = order.getAttributes().get(WorkOrder.KEY_CODE);
            String code2 = workOrder.getAttributes().get(WorkOrder.KEY_CODE);
            if (code1.equals(code2)) {
                isWorkOrderFound = true;
            }
            if (isWorkOrderFound) {
                lvRecords.setSelection(childIndex);
                break;
            }
        }
        searchedWorkOrder = null;
    }
}
