package com.ecity.cswatersupply.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.project.adapter.SafeEventListAdapter;
import com.ecity.cswatersupply.project.network.request.SearchProjectParameter;
import com.ecity.cswatersupply.project.network.response.GetSafeDetailStepResponse;
import com.ecity.cswatersupply.project.network.response.GetSafeEventListResponse;
import com.ecity.cswatersupply.project.network.response.SafeDetailStepModel;
import com.ecity.cswatersupply.project.network.response.SafeEventListModel;
import com.ecity.cswatersupply.project.network.response.SafeProjectListModel;
import com.ecity.cswatersupply.project.service.ProjectService;
import com.ecity.cswatersupply.ui.activities.ABasePullToRefreshActivity;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;

import java.io.Serializable;
import java.util.List;

public class SafeManageEventListActivity extends ABasePullToRefreshActivity<SafeEventListModel> {
    public static final String SELECT_PROJECT = "SELECT_PROJECT";
    public static final String SELECT_EVENT = "SELECT_EVENT";
    public static final String SELECT_DATA = "SELECT_DATA";
    private int PROJECT_SEARCH = 100;
    private SearchProjectParameter searchParameter;
    private SafeProjectListModel model;
    private List<SafeEventListModel> features;
    private SafeEventListModel selectModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtil.register(this);
        initDateSource();
        initUI();
        requestSummaryInfo();
        setOnClickListener();
    }

    @Override
    protected String getScreenTitle() {
        return "安全问题列表";
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    @Override
    protected ArrayListAdapter<SafeEventListModel> prepareListViewAdapter() {
        model = (SafeProjectListModel) getIntent().getSerializableExtra(SELECT_PROJECT);
        return new SafeEventListAdapter(this, model);
    }


    @Override
    protected List<SafeEventListModel> prepareDataSource() {
        return null;
    }

    @Override
    protected boolean isPullRefreshEnabled() {
        return false;
    }

    @Override
    protected boolean isPullLoadEnabled() {
        return false;
    }

    @Override
    protected OnItemClickListener prepareOnItemClickListener() {
        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectModel = features.get(position);
                LoadingDialogUtil.show(SafeManageEventListActivity.this, "正在获取数据");
                ProjectService.getInstance().getEventStep(selectModel);
            }
        };
    }

    private void handleGetSafeDetailTabResponse(ResponseEvent event) {
        GetSafeDetailStepResponse data = event.getData();
        List<SafeDetailStepModel> features = data.getFeatures();

        Intent intent = new Intent(SafeManageEventListActivity.this, SafeManageDetailActivity.class);
        intent.putExtra(SELECT_EVENT, selectModel);
        intent.putExtra(SELECT_DATA, (Serializable) features);
        startActivity(intent);
    }


    private void initUI() {
        getTitleView().setBtnStyle(CustomTitleView.BtnStyle.RIGHT_ACTION);
        getTitleView().setRightActionBtnText("发起");
        getTitleView().setMessageDeleteListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SafeManageEventListActivity.this, SafeCreatePageActivity.class);
                intent.putExtra(SELECT_PROJECT, model);
                startActivity(intent);
            }
        });
    }

    private void initDateSource() {
    }

    private void requestSummaryInfo() {
        LoadingDialogUtil.show(this, "正在查询问题列表");
        User currentUser = HostApplication.getApplication().getCurrentUser();
        ProjectService.getInstance().querySafeEventList(currentUser, model);
    }

    private void handleGetSafeEventListResponse(ResponseEvent event) {
        GetSafeEventListResponse data = event.getData();
        features = data.getFeatures();
        updateDataSource(features);

    }

    @Override
    protected OnRefreshListener<ListView> prepareOnRefreshListener() {
        return new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        };
    }

    private void setOnClickListener() {
    }


    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.PROJECT_SAFE_MANAGE_GET_EVENT_LIST:
                LoadingDialogUtil.dismiss();
                handleGetSafeEventListResponse(event);
                break;
            case ResponseEventStatus.PROJECT_SAFE_MANAGE_GET_DETAIL_TAB:
                LoadingDialogUtil.dismiss();
                handleGetSafeDetailTabResponse(event);
                break;
            default:
                break;
        }
    }
}
