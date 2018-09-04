package com.ecity.cswatersupply.project.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.project.activity.ProjectDetailFragmentActivity;
import com.ecity.cswatersupply.project.activity.ProjectSearchActivity;
import com.ecity.cswatersupply.project.model.Project;
import com.ecity.cswatersupply.project.service.ProjectService;
import com.ecity.cswatersupply.project.service.ProjectServiceUrlManager;
import com.ecity.cswatersupply.project.view.ABaseProjectPropertyView;
import com.ecity.cswatersupply.project.view.ProspectiveDesignListView;

import java.util.Map;

public class ProspectiveDesignListFragment extends ACommonListFragment {

    public static final String INTENT_KEY_PROJECT_STATE = "INTENT_KEY_PROJECT_STATE";
    private String commonFilter;
    private String startTime;
    private String endTime;
    private Project project;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected String requestAllInfoUrl() {
        return ProjectServiceUrlManager.getInstance().getKcsjProList();
    }

    @Override
    protected Class<? extends ABaseProjectPropertyView> getProjectPropertyView() {
        return ProspectiveDesignListView.class;
    }

    @Override
    protected void fillParamters(Map<String, String> map, int position) {
        User currentUser = HostApplication.getApplication().getCurrentUser();
        String roleCode = currentUser.getRoleCode();

        if (roleCode.contains(Constants.JMGC_KEY_JSDW)) {
            if (getPosition() == 0) {
                map.put("state", "4,7");
            } else {
                map.put("state", "1,2,3,5,6,8,9");
            }
        } else if (roleCode.contains(Constants.JMGC_KEY_SJDW)) {
            if (getPosition() == 0) {
                map.put("state", "1,2,6,9");
            } else {
                map.put("state", "3,4,5,7,8");
            }
        } else {
            if (getPosition() == 0) {
                map.put("state", "1,2,3,4,6,7,9");
            } else {
                map.put("state", "5,8");
            }
        }

        map.put("pageno", String.valueOf(pageNo));
        map.put("pagesize", String.valueOf(pageSize));
        map.put("userid", currentUser.getId());
        map.put("name", commonFilter);
        map.put("startdate", startTime);
        map.put("enddate", endTime);
    }

    @Override
    protected String getTitle() {
        return null;
    }

    @Override
    protected OnItemClickListener prepareOnItemClickListener() {
        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = HostApplication.getApplication().getCurrentUser();
                project = getDataSource().get(position);
                String state = project.getAttrState(Project.ATTR_STATE);
                if (state.equals("1") && user.getRoleCode().contains(Constants.JMGC_KEY_SJDW)) {
                    handleViewProject();
                } else {
                    gotoKcsjDetailActivity();
                }


            }
        };
    }

    private void handleViewProject() {
        User user = HostApplication.getApplication().getCurrentUser();
        ProjectService.getInstance().getViewKcsjProject(project.getAttributeValue(Project.KANCHA_GID), user, false);
    }

    @Override
    protected void gotoKcsjDetailActivity() {
        Intent intent = new Intent(getActivity(), ProjectDetailFragmentActivity.class);
        intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_PROJECT_ID, project.getAttributeValue(Project.ATTR_ID));
        intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_RECORD_ID, project.getAttributeValue(Project.KANCHA_GID));
        intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_FRAGMENT_CLASS_NAME, ProspectiveInfoFragment.class.getName());
        intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_LOG_TYPE, "kcsj");
        intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_TAB_TITLES, getDetailScreenTabTitles());
        intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_IS_FROM_PROJECT_SCREEN, true);
        intent.putExtra(INTENT_KEY_PROJECT_STATE, project.getAttrState(Project.ATTR_STATE));
        startActivity(intent);
    }

    private String[] getDetailScreenTabTitles() {
        String[] str = {"基本信息", "委托信息", "延期信息", "提交信息"};
        return str;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ProjectSearchActivity.REQUEST_EDIT_FITLER) {
            if (data == null) {
                return;
            }
            commonFilter = data.getStringExtra(ProjectSearchActivity.INTENT_KEY_FILTER_COMMON);
            startTime = data.getStringExtra(ProjectSearchActivity.INTENT_KEY_FILTER_START_TIME);
            endTime = data.getStringExtra(ProjectSearchActivity.INTENT_KEY_FILTER_END_TIME);
            requestFromBegin(getPosition());
        }
    }
}
