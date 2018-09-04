package com.ecity.cswatersupply.project.activity;

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
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.project.adapter.SafeProjectListAdapter;
import com.ecity.cswatersupply.project.network.request.SearchProjectParameter;
import com.ecity.cswatersupply.project.network.response.GetSafePorjectListResponse;
import com.ecity.cswatersupply.project.network.response.SafeProjectListModel;
import com.ecity.cswatersupply.project.service.ProjectService;
import com.ecity.cswatersupply.ui.activities.ABasePullToRefreshActivity;
import com.ecity.cswatersupply.ui.activities.MainActivity;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;

import java.util.List;

public class SafeManageProjectActivity extends ABasePullToRefreshActivity<SafeProjectListModel> {
    private int pageNo = 1;
    private int pageSize = 25;
    private LinearLayout ll_searchview;
    private int PROJECT_SEARCH = 100;
    private SearchProjectParameter searchParameter;

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
        return "安全管理";
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    @Override
    protected ArrayListAdapter<SafeProjectListModel> prepareListViewAdapter() {
        return new SafeProjectListAdapter(this);
    }


    @Override
    protected List<SafeProjectListModel> prepareDataSource() {
        return null;
    }

    @Override
    protected boolean isPullRefreshEnabled() {
        return true;
    }

    @Override
    protected boolean isPullLoadEnabled() {
        return true;
    }

    @Override
    protected OnItemClickListener prepareOnItemClickListener() {
        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SafeProjectListModel model = getDataSource().get(position);
                Intent intent = new Intent(SafeManageProjectActivity.this, SafeManageEventListActivity.class);
                intent.putExtra(SafeManageEventListActivity.SELECT_PROJECT, model);
                startActivity(intent);
            }
        };
    }

    private void initUI() {
        ll_searchview = (LinearLayout) findViewById(R.id.ll_searchview);
        ll_searchview.setVisibility(View.VISIBLE);
    }

    private void initDateSource() {

    }

    private void requestSummaryInfo() {
        if (searchParameter == null) {
            searchParameter = new SearchProjectParameter();
        }

        LoadingDialogUtil.show(this, "正在查询项目列表");
        User currentUser = HostApplication.getApplication().getCurrentUser();
        ProjectService.getInstance().querySafeProjects(currentUser, pageNo, pageSize, searchParameter);
    }

    private void handleGetSafePorjectResponse(ResponseEvent event) {
        GetSafePorjectListResponse data = event.getData();
        List<SafeProjectListModel> features = data.getFeatures();

        if (ListUtil.isEmpty(features)) {
            UIEvent uiEvent = new UIEvent(UIEventStatus.TOAST);
            uiEvent.setMessage(getString(R.string.message_no_more_record));
            uiEvent.setTargetClass(MainActivity.class);
            EventBusUtil.post(uiEvent);
            getRefreshListView().setHasMoreData(false);
            if (pageNo == 1) {//如果查询为空时，显示空列表
                updateDataSource(features);
            }
        } else {
            List<SafeProjectListModel> dataSource = getDataSource();
            if (pageNo == 1) {
                // 对于requestMessagesAfterDeletingMessages返回结果，要先把本地剩余的记录清空
                dataSource.clear();
            }
            dataSource.addAll(features);
            getRefreshListView().onPullUpRefreshComplete();
            getRefreshListView().onPullDownRefreshComplete();
            getRefreshListView().setLastUpdateTime();
            updateDataSource(dataSource);
            pageNo++;
        }
    }

    @Override
    protected OnRefreshListener<ListView> prepareOnRefreshListener() {
        return new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNo = 1;
                requestSummaryInfo();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestSummaryInfo();
            }
        };
    }

    private void setOnClickListener() {
        ll_searchview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SafeManageProjectActivity.this, ProjectSafeSearchActivity.class);
                startActivityForResult(intent, PROJECT_SEARCH);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PROJECT_SEARCH) {
            if (resultCode == 1 && data != null) {
                searchParameter = (SearchProjectParameter) data.getSerializableExtra(ProjectSafeSearchActivity.SEARCH_RESULT_DATA);
                pageNo = 1;
                requestSummaryInfo();
            }
        }
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.PROJECT_SAFE_MANAGE_GET_PROJECT:
                LoadingDialogUtil.dismiss();
                handleGetSafePorjectResponse(event);
                break;
            default:
                break;
        }
    }
}
