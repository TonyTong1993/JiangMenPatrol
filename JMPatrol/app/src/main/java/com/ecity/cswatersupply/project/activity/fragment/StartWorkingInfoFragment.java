package com.ecity.cswatersupply.project.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;
import com.ecity.cswatersupply.project.network.request.PostCheckKGSQParameter;
import com.ecity.cswatersupply.project.service.ProjectServiceUrlManager;

import java.util.Map;

public class StartWorkingInfoFragment extends AProjectCommonInspectItemFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = super.onCreateView(inflater, container, savedInstanceState);
        requestId = ResponseEventStatus.PROJECT_GET_START_WORKING_DETAIL;
        queryInfo();
        return convertView;
    }

    @Override
    protected String getQueryInfoUrl() {
        return ProjectServiceUrlManager.getInstance().getProKgInfo();
    }

    @Override
    protected int getQueryInfoRequestId() {
        return requestId;
    }

    @Override
    protected String getQueryInfoMessage() {
        return getString(R.string.str_searching);
    }

    @Override
    protected String getSubmitInfoUrl() {
        return ProjectServiceUrlManager.getInstance().checkProKg();
    }

    @Override
    protected AReportInspectItemParameter getSubmitInfoParameter() {
        return new PostCheckKGSQParameter(items, recordId);
    }

    @Override
    protected int getSubmitInfoRequestId() {
        return ResponseEventStatus.PROJECT_GET_START_WORKING_CHECK;
    }

    @Override
    protected void fillQueryParameters(Map<String, String> map) {
        User currentUser = HostApplication.getApplication().getCurrentUser();
        map.put("kgid", recordId);
        map.put("proid", projectId);
        map.put("userid", currentUser.getId());
    }

    @Override
    protected Class<? extends AProjectCommonInspectItemFragment> getFragmentClass() {
        return StartWorkingInfoFragment.class;
    }
}
