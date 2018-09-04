package com.ecity.cswatersupply.project.activity;

import android.os.Bundle;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.project.activity.fragment.AProjectCommonInspectItemFragment;
import com.ecity.cswatersupply.project.activity.fragment.ProjectInfoFragment;
import com.ecity.cswatersupply.project.model.Project;
import com.ecity.cswatersupply.project.service.ProjectServiceUrlManager;
import com.ecity.cswatersupply.project.view.ABaseProjectPropertyView;
import com.ecity.cswatersupply.project.view.ProjectPropertyView;

import java.util.HashMap;
import java.util.Map;

public class GeneralProjectListActivity extends AProjectListActivity {
    public static final String INTENT_KEY_PROJECT_STATUS = "INTENT_KEY_PROJECT_STATUS";
    public static final String INTENT_KEY_PROJECT_TYPE = "INTENT_KEY_PROJECT_TYPE";
    public static final String INTENT_KEY_PROJECT_END_TIME = "INTENT_KEY_PROJECT_CURRENT_DATE";
    public static final String INTENT_KEY_PROJECT_START_TIME = "INTENT_KEY_PROJECT_SELECT_DATE";

    private String type;
    private String status;
    private String startTime;
    private String endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDateSource();
        requestProjects();
    }

    @Override
    protected Class<? extends ABaseProjectPropertyView> getProjectPropertyView() {
        return ProjectPropertyView.class;
    }

    @Override
    protected String getScreenTitle() {
        return getString(R.string.project_list_title);
    }

    @Override
    protected Class<? extends AProjectCommonInspectItemFragment> getDetailFragmentClass() {
        return ProjectInfoFragment.class;
    }

    @Override
    protected String getRecordId(Project project) {
        return null;
    }

    @Override
    protected String[] getDetailScreenTabTitles() {
        return new String[] { getString(R.string.detail_tab_basic_info), getString(R.string.project_workload), getString(R.string.project_progress),
                getString(R.string.project_change), getString(R.string.project_pay) };
    }

    @Override
    protected String getQueryInfoUrl() {
        return ProjectServiceUrlManager.getInstance().queryPro4Analysis();
    }

    @Override
    protected Map<String, String> getQueryInfoParameters() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(PAGE_SIZE));
        map.put("type", type);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("status", status);

        return map;
    }

    private void initDateSource() {
        type = getIntent().getStringExtra(INTENT_KEY_PROJECT_TYPE);
        status = getIntent().getStringExtra(INTENT_KEY_PROJECT_STATUS);
        startTime = getIntent().getStringExtra(INTENT_KEY_PROJECT_START_TIME);
        endTime = getIntent().getStringExtra(INTENT_KEY_PROJECT_END_TIME);
    }
}
