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
import com.ecity.cswatersupply.project.view.WaterReceiveListView;

import java.util.Map;

public class WaterReceiveListFragment extends ACommonListFragment {

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
        return ProjectServiceUrlManager.getInstance().getJsProList();
    }

    @Override
    protected Class<? extends ABaseProjectPropertyView> getProjectPropertyView() {
        return WaterReceiveListView.class;
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
        String roleCode = currentUser.getRoleCode();//接水状态：0-未申请 1-已申请 2-工程部审核通过 3-工程部审核不通过 4-工程部退回 5-管网部通过 6-管网部不通过 7-上传结果（完成）
        if (roleCode.contains(Constants.JMGC_KEY_JSDW)) {
            if (getPosition() == 0) {
                map.put("state", "1");
            } else {
                map.put("state", "2,3,4,5,6,7");
            }
        } else if (roleCode.contains(Constants.JMGC_KEY_SGDW)) {
            if (getPosition() == 0) {
                map.put("state", "3,4,6");
            } else {
                map.put("state", "1,2,5,7");
            }
        } else if (roleCode.contains(Constants.JMGC_KEY_GWDW)) {
            if (getPosition() == 0) {
                map.put("state", "2");
            } else {
                map.put("state", "1,3,4,5,6,7");
            }
        } else {
            if (getPosition() == 0) {
                map.put("state", "1,3,4,6");
            } else {
                map.put("state", "2,5,7");
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
                //                if (project.getAttributeValue(Project.KAIGONG_GID).isEmpty()) {
                //                    ToastUtil.show("这条项目还未完成开工申请，请在pc端上操作", 0);
                //                    return;
                //                }

                Intent intent = new Intent(getActivity(), ProjectDetailFragmentActivity.class);
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_PROJECT_ID, project.getAttributeValue(Project.ATTR_ID));
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_RECORD_ID, project.getAttributeValue(Project.JS_GID));
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_FRAGMENT_CLASS_NAME, WaterReceiveInfoFragment.class.getName());
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_LOG_TYPE, "js");
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_TAB_TITLES, getDetailScreenTabTitles());
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_TITLE_NAME, getString(R.string.project_title_water_receive_detail));
                intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_IS_FROM_PROJECT_SCREEN, true);
                startActivity(intent);
            }
        };
    }

    private String[] getDetailScreenTabTitles() {
        String[] str = {""};
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
