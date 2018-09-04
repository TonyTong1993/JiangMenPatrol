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
import com.ecity.cswatersupply.project.activity.CommonHisRecordListActivity;
import com.ecity.cswatersupply.project.activity.ProjectDetailFragmentActivity;
import com.ecity.cswatersupply.project.network.request.PostCheckSYSYParameter;
import com.ecity.cswatersupply.project.service.ProjectServiceUrlManager;

import java.util.Map;

public class ProssureTestInfoFragment extends AProjectCommonInspectItemFragment {
    private Button btnMore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = super.onCreateView(inflater, container, savedInstanceState);
        requestId = ResponseEventStatus.PROJECT_GET_PROSSURE_DETAIL;
        queryInfo();
        initUI(convertView);
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

    private void setOnListener() {
        btnMore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CommonHisRecordListActivity.class);
                intent.putExtra(CommonHisRecordListActivity.INTENT_KEY_PROJECT_ID, getProjectId());
                intent.putExtra(CommonHisRecordListActivity.INTENT_KEY_PROJECT_URL, ProjectServiceUrlManager.getInstance().getSyHistory());
                intent.putExtra(CommonHisRecordListActivity.INTENT_KEY_PROJECT_CLASS_NAME, ProssureTestInfoFragment.class.getName());
                intent.putExtra(CommonHisRecordListActivity.INTENT_KEY_PROJECT_LOG_TYPE, "sy");
                startActivity(intent);
            }
        });
    }

    @Override
    protected String getQueryInfoUrl() {
        return ProjectServiceUrlManager.getInstance().getSyCheckDetail();
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
        return ProjectServiceUrlManager.getInstance().checkSy();
    }

    @Override
    protected AReportInspectItemParameter getSubmitInfoParameter() {
        return new PostCheckSYSYParameter(items, recordId);
    }

    @Override
    protected int getSubmitInfoRequestId() {
        return ResponseEventStatus.PROJECT_GET_PROSSURE_CHECK;
    }

    @Override
    protected void fillQueryParameters(Map<String, String> map) {
        User currentUser = HostApplication.getApplication().getCurrentUser();
        map.put("syid", recordId);
        map.put("userid", currentUser.getId());
        map.put("proid", projectId);
    }

    @Override
    protected Class<? extends AProjectCommonInspectItemFragment> getFragmentClass() {
        return ProssureTestInfoFragment.class;
    }
}
