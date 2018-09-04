package com.ecity.cswatersupply.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.project.activity.fragment.ProspectiveInfoFragment;
import com.ecity.cswatersupply.project.adapter.ProjectListAdapter;
import com.ecity.cswatersupply.project.model.Project;
import com.ecity.cswatersupply.project.service.ProjectService;
import com.ecity.cswatersupply.project.util.ProjectAdapter;
import com.ecity.cswatersupply.project.view.ABaseProjectPropertyView;
import com.ecity.cswatersupply.project.view.ProspectiveMoreListView;
import com.ecity.cswatersupply.project.view.WorkLoadProjectPropertyView;
import com.ecity.cswatersupply.ui.activities.ABasePullToRefreshActivity;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkLoadRecordListActivity extends ABasePullToRefreshActivity<Project> {
    public static final String INTENT_KEY_PROJECT_ID = "INTENT_KEY_PROJECT_ID";
    public static final String INTENT_KEY_PROJECT_URL = "INTENT_KEY_PROJECT_URL";
    public static final String INTENT_KEY_PROJECT_CLASS_NAME = "INTENT_KEY_PROJECT_CLASS_NAME";
    public static final String INTENT_KEY_PROJECT_TITLE = "INTENT_KEY_PROJECT_TITLE";
    public static final String INTENT_KEY_PROJECT_LOG_TYPE = "INTENT_KEY_PROJECT_LOG_TYPE";
    private String projectId;
    private String url;
    private String className;
    private String title;
    private String logType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        projectId = getIntent().getStringExtra(INTENT_KEY_PROJECT_ID);
        url = getIntent().getStringExtra(INTENT_KEY_PROJECT_URL);
        EventBusUtil.register(this);
        requestProjects();
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    @Override
    protected ArrayListAdapter<Project> prepareListViewAdapter() {
        title = getIntent().getStringExtra(INTENT_KEY_PROJECT_TITLE);
        ABaseProjectPropertyView propertyView;
        if (title.equals(getString(R.string.project_workload_records_title))) {
            propertyView = new WorkLoadProjectPropertyView();
        }else{
            propertyView = new ProspectiveMoreListView();
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
    protected boolean isPullLoadEnabled() {
        return false;
    }

    @Override
    protected OnItemClickListener prepareOnItemClickListener() {
        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                className = getIntent().getStringExtra(INTENT_KEY_PROJECT_CLASS_NAME);
                logType = getIntent().getStringExtra(INTENT_KEY_PROJECT_LOG_TYPE);
                if (className.equals(ProspectiveInfoFragment.class.getName())){
                    return;
                }
                Intent intent = new Intent(WorkLoadRecordListActivity.this, ProjectDetailFragmentActivity.class);
                Project project = getDataSource().get(position);
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_PROJECT_ID, project.getAttributeValue("proid"));
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_RECORD_ID, project.getAttributeValue("gid"));
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_FRAGMENT_CLASS_NAME, className);
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_LOG_TYPE, logType);
                startActivity(intent);
            }
        };
    }

    private String[] getDetailScreenTabTitles() {
        String[] str = {"基本信息", "委托信息", "延期信息", "提交信息"};
        return str;
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

    private void requestProjects() {
        LoadingDialogUtil.show(this, R.string.str_searching);
        Map<String, String> map = new HashMap<String, String>();
        map.put("proid", projectId);
        ProjectService.getInstance().sendGetRequest(url, ResponseEventStatus.PROJECT_WORKLOAD_GET_ALL_RECORDS, map);
    }

    private void handleGetRecordsResponse(ResponseEvent event) {
        LoadingDialogUtil.dismiss();

        JSONObject response = event.getData();
        List<Project> projects = parseProjects(response);
        ProjectAdapter.parseProjectFieldMeta(response);
        if (ListUtil.isEmpty(projects)) {
            ToastUtil.showShort(getString(R.string.message_no_more_record));
            getRefreshListView().setHasMoreData(false);
        } else {
            updateDataSource(projects);
            getRefreshListView().onPullUpRefreshComplete();
            getRefreshListView().setLastUpdateTime();
        }
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.PROJECT_WORKLOAD_GET_ALL_RECORDS:
                handleGetRecordsResponse(event);
                break;
            default:
                break;
        }
    }

    @Override
    protected String getScreenTitle() {
        title = getIntent().getStringExtra(INTENT_KEY_PROJECT_TITLE);
        return title;
    }

    private List<Project> parseProjects(JSONObject response) {
        JSONArray fields = response.optJSONArray("features");
        if ((fields == null) || (fields.length() == 0)) {
            return null;
        }

        List<Project> projects = new ArrayList<Project>();
        for (int i = 0; i < fields.length(); i++) {
            JSONObject json = fields.optJSONObject(i);
            if (json == null) {
                continue;
            }

            projects.add(new Project(json));
        }

        return projects;
    }
}
