package com.ecity.cswatersupply.workorder;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.WorkOrderOperationLogAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.WorkOrderOperationLogBean;
import com.ecity.cswatersupply.service.WorkOrderOperationLogService;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

/*工单的日志回溯*/
public class WorkOrderOperationLogActivity extends BaseActivity {
    private CustomTitleView titleView;
    public PullToRefreshListView refreshLogDateBackView;
    private ListView logDateBackListView;
    private WorkOrderOperationLogAdapter logDateBackAdapter;
    private List<WorkOrderOperationLogBean> listData;
    private WorkOrder currrentWorkOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workorder_operation_log);
        EventBusUtil.register(this);
        listData = new ArrayList<WorkOrderOperationLogBean>();
        initUI();
        bindEvent();
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    @SuppressLint("NewApi")
    private void initUI() {
        titleView = (CustomTitleView) findViewById(R.id.view_tile_log_date_back);
        titleView.setTitleText(R.string.log_date_back);
        titleView.setBtnStyle(CustomTitleView.BtnStyle.ONLY_BACK);
        refreshLogDateBackView = (PullToRefreshListView) findViewById(R.id.lv_log_date_back);
        refreshLogDateBackView.setLastUpdateTime();
        refreshLogDateBackView.doPullRefreshing(true, 500);
        logDateBackListView = refreshLogDateBackView.getRefreshableView();
    }



    private void bindEvent() {
        refreshLogDateBackView.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                initDataResource();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });

    }

    public void initDataResource() {
        Intent intent = this.getIntent(); 
        currrentWorkOrder=(WorkOrder)intent.getSerializableExtra("currentWorkOrder");
        WorkOrderOperationLogService.getInstance().getLogDateBacks(currrentWorkOrder);
    }

    //初始化数据（取自联网数据）

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLogDateBackView.doPullRefreshing(true, 500);
    }

    public void onEventMainThread(UIEvent event) {
        
        switch (event.getId()) {
            case UIEventStatus.WORKORDER_LOG_DATE_BACK_NOTIFICATION:
                refreshLogDateBackView.doPullRefreshing(true, 500);
                //                initDataResource();
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
            case ResponseEventStatus.WORKORDER_OPERATION_LOGS:
                handleWorkOrderOperationLogs(event);
                break;
            default:
                break;
        }
    }

    private void handleWorkOrderOperationLogs(ResponseEvent event) {
        listData = event.getData();
        logDateBackAdapter = new WorkOrderOperationLogAdapter(listData, this);
        logDateBackListView.setAdapter(logDateBackAdapter);
        logDateBackAdapter.notifyDataSetChanged();
        refreshLogDateBackView.onPullDownRefreshComplete();
        refreshLogDateBackView.onPullUpRefreshComplete();
        refreshLogDateBackView.setLastUpdateTime();
    }
}
