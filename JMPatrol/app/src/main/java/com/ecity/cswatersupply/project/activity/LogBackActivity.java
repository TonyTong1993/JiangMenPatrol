package com.ecity.cswatersupply.project.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.project.adapter.ProjectLogAdapter;
import com.ecity.cswatersupply.project.model.ProjectLogBean;
import com.ecity.cswatersupply.project.service.ProjectService;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 *  江门工程－日志回溯
 *  @example
 *      Bundle data = new Bundle();<br>
 *      data.putString(LogBackActivity.INTENT_KEY_LOG_BACK_TYPE, "workload");<br>
 *      data.putString(LogBackActivity.INTENT_KEY_LOG_BACK_TITLE, "工作量日志");<br>
 *      data.putString(LogBackActivity.INTENT_KEY_LOG_BACK_PROJECTID, "5");<br>
 *      UIHelper.startActivityWithExtra(LogBackActivity.class, data);
 * @author qiwei
 * 
 * */
public class LogBackActivity extends BaseActivity {
    public static final String INTENT_KEY_LOG_BACK_TITLE = "INTENT_KEY_LOG_BACK_TITLE";//标题
    public static final String INTENT_KEY_LOG_BACK_TYPE = "INTENT_KEY_LOG_BACK_TYPE";//日志类型
    public static final String INTENT_KEY_LOG_BACK_PROJECTID = "INTENT_KEY_LOG_BACK_PROJECTID";//projectId
    private CustomTitleView titleView;
    public PullToRefreshListView refreshLogDateBackView;
    private ListView logDateBackListView;
    private ProjectLogAdapter logDateBackAdapter;
    private List<ProjectLogBean> listData = new ArrayList<ProjectLogBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workorder_operation_log);
        EventBusUtil.register(this);
        initData();
        initUI();
        bindEvent();
    }

    private void initData() {
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    @SuppressLint("NewApi")
    private void initUI() {
        titleView = (CustomTitleView) findViewById(R.id.view_tile_log_date_back);
        titleView.setTitleText(getIntent().getStringExtra(INTENT_KEY_LOG_BACK_TITLE));
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
        String logBackType = getIntent().getStringExtra(INTENT_KEY_LOG_BACK_TYPE);
        String projectId = getIntent().getStringExtra(INTENT_KEY_LOG_BACK_PROJECTID);
        ProjectService.getInstance().getLogDateBacks(logBackType, projectId);
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

    public void onEventMainThread(UIEvent event) {

        switch (event.getId()) {
            case UIEventStatus.WORKORDER_LOG_DATE_BACK_NOTIFICATION:
                refreshLogDateBackView.doPullRefreshing(true, 500);
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
            case ResponseEventStatus.PROJECT_GET_PROJECTS_LOG_BACK:
                handleProjectLogBacks(event);
                break;
            default:
                break;
        }
    }

    private void handleProjectLogBacks(ResponseEvent event) {
        listData = event.getData();
        logDateBackAdapter = new ProjectLogAdapter(listData, this);
        logDateBackListView.setAdapter(logDateBackAdapter);
        logDateBackAdapter.notifyDataSetChanged();
        refreshLogDateBackView.onPullDownRefreshComplete();
        refreshLogDateBackView.onPullUpRefreshComplete();
        refreshLogDateBackView.setLastUpdateTime();
    }
}
