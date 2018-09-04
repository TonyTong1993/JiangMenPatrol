package com.ecity.cswatersupply.project.activity.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.project.activity.ProjectDetailFragmentActivity;
import com.ecity.cswatersupply.project.activity.ProjectSearchActivity;
import com.ecity.cswatersupply.project.model.Project;
import com.ecity.cswatersupply.project.service.ProjectServiceUrlManager;
import com.ecity.cswatersupply.project.view.ABaseProjectPropertyView;
import com.ecity.cswatersupply.project.view.WorkLoadProjectPropertyView;

import java.util.Map;

public class WorkLoadProjectFragment extends ACommonListFragment {
    private String commonFilter;
    private String startTime;
    private String endTime;

    @Override
    protected String requestAllInfoUrl() {
        return ProjectServiceUrlManager.getInstance().getWorkloadProjectsUrl();
    }

    @Override
    protected Class<? extends ABaseProjectPropertyView> getProjectPropertyView() {
        return WorkLoadProjectPropertyView.class;
    }

    @Override
    protected void fillParamters(Map<String, String> map, int position) {
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(pageSize));
        if (!StringUtil.isEmpty(commonFilter)) {
            map.put("filter", commonFilter);
        }
        if (!StringUtil.isEmpty(startTime)) {
            map.put("startTime", startTime);
        }
        if (!StringUtil.isEmpty(endTime)) {
            map.put("endTime", endTime);
        }
        User user = HostApplication.getApplication().getCurrentUser();
        String roleCode = user.getRoleCode();//工作量状态：0-已申请 1-申请通过 2-审核不通过 3-重新申请
        if (roleCode.contains(Constants.JMGC_KEY_JSDW)) {
            if (getPosition() == 0) {
                map.put("status", "0,3");
            } else {
                map.put("status", "1,2");
            }
        } else if (roleCode.contains(Constants.JMGC_KEY_SGDW)) {
            if (getPosition() == 0) {
                map.put("status", "2");
            } else {
                map.put("status", "0,1,3");
            }
        } else {
            if (position == 0) {
                map.put("status", "0,2,3");
            } else {
                map.put("status", "1");
            }
        }
    }

    @Override
    protected OnItemClickListener prepareOnItemClickListener() {
        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Project project = getDataSource().get(position);
                Intent intent = new Intent(getActivity(), ProjectDetailFragmentActivity.class);
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_PROJECT_ID, project.getAttributeValue("proid"));
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_RECORD_ID, project.getAttributeValue("gid"));
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_FRAGMENT_CLASS_NAME, WorkLoadInfoFragment.class.getName());
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_LOG_TYPE, "workload");
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_TAB_TITLES, new String[]{""});
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_IS_FROM_PROJECT_SCREEN, true);
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_TITLE_NAME, getString(R.string.project_title_workload_detail));
                startActivity(intent);
            }
        };
    }

    @Override
    protected String getTitle() {
        return null;
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

    @Override
    protected void gotoKcsjDetailActivity() {
    }
}
