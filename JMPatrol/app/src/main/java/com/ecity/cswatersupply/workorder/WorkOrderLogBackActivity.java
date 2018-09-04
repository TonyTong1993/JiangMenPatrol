package com.ecity.cswatersupply.workorder;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.WorkOrderOperationLogAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.WorkOrderDetailTabModel;
import com.ecity.cswatersupply.model.WorkOrderOperationLogBean;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * 工单的日志回溯
 */
public class WorkOrderLogBackActivity extends BaseActivity {
    private CustomTitleView titleView;
    public PullToRefreshListView refreshLogDateBackView;
    private ListView logDateBackListView;
    private WorkOrderOperationLogAdapter logDateBackAdapter;
    private List<WorkOrderOperationLogBean> listData;
    private WorkOrderDetailTabModel currrentTab;
    private String processInstanceId;

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
        Bundle bundle = getIntent().getExtras();
        currrentTab = (WorkOrderDetailTabModel)bundle.getSerializable("currentWorkOrderLogInfoTab");
        processInstanceId = bundle.getString("processInstanceId");
        requestWorkOrderLogInfo();
    }

    //初始化数据（取自联网数据）
    private void requestWorkOrderLogInfo() {
        LoadingDialogUtil.show(this, R.string.workorder_downloading);
        String url = WorkOrderUtil.getWorkOrderDetailUrl(currrentTab);
        WorkOrderService.instance.getLogDateBacks(url, processInstanceId);
    }

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

    public void onEventMainThread(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        if (!event.isOK()) {
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.WORKORDER_GET_DETAIL_LOG:
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
