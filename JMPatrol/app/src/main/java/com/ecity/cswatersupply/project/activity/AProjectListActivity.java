package com.ecity.cswatersupply.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.project.activity.fragment.AProjectCommonInspectItemFragment;
import com.ecity.cswatersupply.project.adapter.ProjectListAdapter;
import com.ecity.cswatersupply.project.model.Project;
import com.ecity.cswatersupply.project.service.ProjectService;
import com.ecity.cswatersupply.project.util.ProjectAdapter;
import com.ecity.cswatersupply.project.view.ABaseProjectPropertyView;
import com.ecity.cswatersupply.ui.activities.ABasePullToRefreshActivity;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public abstract class AProjectListActivity extends ABasePullToRefreshActivity<Project> {
    protected int pageNo = 1;
    protected static final int PAGE_SIZE = 15;

    /**
     * 查询信息的请求地址
     *
     * @return
     */
    protected abstract String getQueryInfoUrl();

    /**
     * 查询信息的请求参数
     *
     * @return
     */
    protected abstract Map<String, String> getQueryInfoParameters();

    /**
     * 项目列表展示每一行的view
     *
     * @return
     */
    protected abstract Class<? extends ABaseProjectPropertyView> getProjectPropertyView();

    /**
     * 获取详情界面的fragment的类
     *
     * @return
     */
    protected abstract Class<? extends AProjectCommonInspectItemFragment> getDetailFragmentClass();

    /**
     * 点击项目后，获取记录的id。若功能不需要，返回null。
     *
     * @param project 点击的项目
     * @return
     */
    protected abstract String getRecordId(Project project);

    /**
     * 详情页面的tab标题
     *
     * @return
     */
    protected abstract String[] getDetailScreenTabTitles();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtil.register(this);
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    @Override
    protected ArrayListAdapter<Project> prepareListViewAdapter() {
        ABaseProjectPropertyView propertyView = null;
        try {
            propertyView = getProjectPropertyView().newInstance();
        } catch (InstantiationException e) {
            LogUtil.e(this, e);
            throw new RuntimeException(e); // 出现这个异常必然是程序问题，直接退出
        } catch (IllegalAccessException e) {
            LogUtil.e(this, e);
            throw new RuntimeException(e); // 出现这个异常必然是程序问题，直接退出
        }

        return new ProjectListAdapter(this, propertyView);
    }

    @Override
    protected List<Project> prepareDataSource() {
        return null;
    }

    @Override
    protected boolean isPullRefreshEnabled() {
        return false;
    }

    @Override
    protected OnItemClickListener prepareOnItemClickListener() {
        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AProjectListActivity.this, ProjectDetailFragmentActivity.class);
                Project project = getDataSource().get(position);
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_PROJECT_ID, project.getAttributeValue(Project.ATTR_ID));
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_RECORD_ID, getRecordId(project));
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_FRAGMENT_CLASS_NAME, getDetailFragmentClass().getName());
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_TAB_TITLES, getDetailScreenTabTitles());
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_PROJECT_TYPE, project.getAttributeValue(Project.ATTR_TYPE));
                startActivity(intent);
            }
        };
    }

    @Override
    protected OnRefreshListener<ListView> prepareOnRefreshListener() {
        return new OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestProjects();
            }
        };
    }

    protected void requestProjects() {
        if (pageNo == 1) {
            LoadingDialogUtil.show(this, R.string.str_searching);
        }
        ProjectService.getInstance().sendGetRequest(getQueryInfoUrl(), ResponseEventStatus.PROJECT_GET_PROJECTS, getQueryInfoParameters());
    }

    private void handleGetProjectResponse(ResponseEvent event) {
        LoadingDialogUtil.dismiss();

        JSONObject response = event.getData();
        List<Project> projects = ProjectAdapter.parseProjects(response);
        ProjectAdapter.parseProjectFieldMeta(response);
        if (ListUtil.isEmpty(projects)) {
            ToastUtil.showShort(getString(R.string.message_no_more_record));
            getRefreshListView().setHasMoreData(false);
        } else {
            List<Project> dataSource = getDataSource();
            dataSource.addAll(projects);
            updateDataSource(dataSource);
            getRefreshListView().onPullUpRefreshComplete();
            getRefreshListView().setLastUpdateTime();
            pageNo++;
        }
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.PROJECT_GET_PROJECTS:
                handleGetProjectResponse(event);
                break;
            default:
                break;
        }
    }
}
