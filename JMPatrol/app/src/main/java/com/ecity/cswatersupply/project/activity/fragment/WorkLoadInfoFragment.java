package com.ecity.cswatersupply.project.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;
import com.ecity.cswatersupply.project.activity.ProjectDetailFragmentActivity;
import com.ecity.cswatersupply.project.activity.WorkLoadRecordListActivity;
import com.ecity.cswatersupply.project.network.request.PostCheckWorkLoadParameter;
import com.ecity.cswatersupply.project.service.ProjectServiceUrlManager;

import java.util.Map;

public class WorkLoadInfoFragment extends AProjectCommonInspectItemFragment {
    private Button btnMore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = super.onCreateView(inflater, container, savedInstanceState);

        initUI(convertView);
        requestId = getSupportedRequestIds()[position];
        queryInfo();

        return convertView;
    }

    private void initUI(View convertView) {
        boolean showMoreButton = getActivity().getIntent().getBooleanExtra(ProjectDetailFragmentActivity.INTENT_KEY_IS_FROM_PROJECT_SCREEN, false);
        if (showMoreButton) {
            btnMore = (Button) convertView.findViewById(R.id.btn_more);
            btnMore.setVisibility(View.VISIBLE);
            setOnListener();
        }
    }

    @Override
    protected String getQueryInfoUrl() {
        return ProjectServiceUrlManager.getInstance().getWorkloadDetailUrl();
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
        return ProjectServiceUrlManager.getInstance().getCheckWorkloadUrl();
    }

    @Override
    protected void fillQueryParameters(Map<String, String> map) {
        User user = HostApplication.getApplication().getCurrentUser();
        map.put("proid", getProjectId());
        map.put("recordId", getRecordId());
        map.put("userid", user.getGid());
    }

    @Override
    protected int getSubmitInfoRequestId() {
        return ResponseEventStatus.PROJECT_WORKLOAD_CHECK;
    }

    @Override
    protected AReportInspectItemParameter getSubmitInfoParameter() {
        return new PostCheckWorkLoadParameter(items, projectId, recordId);
    }

    @Override
    protected String getSubmitInfoMessage() {
        return getString(R.string.str_submiting);
    }

    private int[] getSupportedRequestIds() {
        return new int[]{ResponseEventStatus.PROJECT_WORKLOAD_GET_RECORD_DETAIL};
    }

    private void setOnListener() {
        btnMore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WorkLoadRecordListActivity.class);
                intent.putExtra(WorkLoadRecordListActivity.INTENT_KEY_PROJECT_ID, getProjectId());
                intent.putExtra(WorkLoadRecordListActivity.INTENT_KEY_PROJECT_URL, ProjectServiceUrlManager.getInstance().getWorkloadRecordsUrl());
                intent.putExtra(WorkLoadRecordListActivity.INTENT_KEY_PROJECT_CLASS_NAME, getFragmentClass().getName());
                intent.putExtra(WorkLoadRecordListActivity.INTENT_KEY_PROJECT_TITLE, getString(R.string.project_workload_records_title));
                intent.putExtra(WorkLoadRecordListActivity.INTENT_KEY_PROJECT_LOG_TYPE, "workload");
                startActivity(intent);
            }
        });
    }

    @Override
    protected Class<? extends AProjectCommonInspectItemFragment> getFragmentClass() {
        return WorkLoadInfoFragment.class;
    }
}
