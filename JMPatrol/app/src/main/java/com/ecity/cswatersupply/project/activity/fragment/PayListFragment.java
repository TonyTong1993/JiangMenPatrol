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
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.project.activity.ProjectDetailFragmentActivity;
import com.ecity.cswatersupply.project.activity.ProjectSearchActivity;
import com.ecity.cswatersupply.project.model.Project;
import com.ecity.cswatersupply.project.service.ProjectServiceUrlManager;
import com.ecity.cswatersupply.project.view.ABaseProjectPropertyView;
import com.ecity.cswatersupply.project.view.PayListView;

import java.util.Map;

public class PayListFragment extends ACommonListFragment {

    private String commonFilter;
    private String startTime;
    private String endTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    protected String requestAllInfoUrl() {
        return ProjectServiceUrlManager.getInstance().getPayProList();
    }

    @Override
    protected Class<? extends ABaseProjectPropertyView> getProjectPropertyView() {
        return PayListView.class;
    }

    @Override
    protected void fillParamters(Map<String, String> map, int position) {
        User currentUser = HostApplication.getApplication().getCurrentUser();
        map.put("pageno", String.valueOf(pageNo));
        map.put("pagesize", String.valueOf(pageSize));
        map.put("userid", currentUser.getId());
        map.put("name", commonFilter);
        map.put("startdate", startTime);
        map.put("enddate", endTime);

        String roleCode = currentUser.getRoleCode();//资金支付状态： -1-已录入 0-“” 1-已申请 3-审核通过 4-审核不通过
        if (roleCode.contains(Constants.JMGC_KEY_JSDW)) {
            if (getPosition() == 0) {
                map.put("state", "1");
            } else {
                map.put("state", "-1,3,4");
            }
        } else if (roleCode.contains(Constants.JMGC_KEY_SGDW)) {
            if (getPosition() == 0) {
                map.put("state", "4");
            } else {
                map.put("state", "-1,1,3");
            }
        } else {
            if (getPosition() == 0) {
                map.put("state", "1,4");
            } else {
                map.put("state", "-1,3");
            }
        }
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
                Project project = getDataSource().get(position);
                Intent intent = new Intent(getActivity(), ProjectDetailFragmentActivity.class);
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_PROJECT_ID, project.getAttributeValue(Project.ATTR_ID));
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_RECORD_ID, project.getAttributeValue(Project.PAY_GID));
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_FRAGMENT_CLASS_NAME, PayInfoFragment.class.getName());
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_LOG_TYPE, "pay");
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_TAB_TITLES, getDetailScreenTabTitles());
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_TITLE_NAME, getString(R.string.project_title_pay_detail));
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_IS_FROM_PROJECT_SCREEN, true);
                startActivity(intent);
            }
        };
    }

    private String[] getDetailScreenTabTitles() {
        String[] str = { "" };
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

    @Override
    protected void gotoKcsjDetailActivity() {
    }
}
