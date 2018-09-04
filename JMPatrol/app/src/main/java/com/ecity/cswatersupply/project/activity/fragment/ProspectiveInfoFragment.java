package com.ecity.cswatersupply.project.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;
import com.ecity.cswatersupply.project.activity.ProjectDetailFragmentActivity;
import com.ecity.cswatersupply.project.activity.WorkLoadRecordListActivity;
import com.ecity.cswatersupply.project.network.request.PostCheckKCSJParameter;
import com.ecity.cswatersupply.project.service.ProjectServiceUrlManager;

import java.util.Map;

public class ProspectiveInfoFragment extends AProjectCommonInspectItemFragment {
    private Button btnMore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = super.onCreateView(inflater, container, savedInstanceState);
        requestId = getSupportedRequestIds()[position];
        if (position == 2) {
            initUI(convertView);
        }
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
        String url = "";
        switch (position) {
            case 0:
                url = ProjectServiceUrlManager.getInstance().getKanChaSheJiDetailUrl(position);
                break;
            case 1:
                url = ProjectServiceUrlManager.getInstance().getKanChaSheJiDetailUrl(position);
                break;
            case 2:
                url = ProjectServiceUrlManager.getInstance().getKanChaSheJiDetailUrl(position);
                break;
            case 3:
                url = ProjectServiceUrlManager.getInstance().getKanChaSheJiDetailUrl(position);
                break;
            default:
                break;
        }

        return url;
    }

    @Override
    protected int getQueryInfoRequestId() {
        return requestId;
    }

    @Override
    protected String getQueryInfoMessage() {
        return "正在获取历史信息";
    }

    @Override
    protected String getSubmitInfoUrl() {
        if (position == 1) {
            return ProjectServiceUrlManager.getInstance().backDesignDelegate();
        } else {
            return ProjectServiceUrlManager.getInstance().checkKcsj();
        }
    }

    @Override
    protected int getSubmitInfoRequestId() {
        return ResponseEventStatus.PROJECT_GET_PROJECT_CKSJ_CHECK;
    }

    @Override
    protected AReportInspectItemParameter getSubmitInfoParameter() {
        return new PostCheckKCSJParameter(items, position, recordId);
    }

    @Override
    protected String getSubmitInfoMessage() {
        return getString(R.string.str_submiting);
    }

    @Override
    protected void fillQueryParameters(Map<String, String> map) {
        User currentUser = HostApplication.getApplication().getCurrentUser();
        map.put("proid", projectId);
        map.put("kcid", recordId);
        map.put("userid", currentUser.getId());
    }

    private int[] getSupportedRequestIds() {
        return new int[]{ResponseEventStatus.PROJECT_GET_PROSPECTIVE_DETAIL_TAB_BASEINFO, ResponseEventStatus.PROJECT_GET_PROJECT_DETAIL_TAB_APPOINT,
                ResponseEventStatus.PROJECT_GET_PROJECT_DETAIL_TAB_DELAY, ResponseEventStatus.PROJECT_GET_PROJECT_DETAIL_TAB_COMMIT};
    }

    @Override
    protected Class<? extends AProjectCommonInspectItemFragment> getFragmentClass() {
        return ProspectiveInfoFragment.class;
    }

    private void setOnListener() {
        btnMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WorkLoadRecordListActivity.class);
                intent.putExtra(WorkLoadRecordListActivity.INTENT_KEY_PROJECT_ID, getProjectId());
                intent.putExtra(WorkLoadRecordListActivity.INTENT_KEY_PROJECT_URL, ProjectServiceUrlManager.getInstance().getKcsjCheckLog());
                intent.putExtra(WorkLoadRecordListActivity.INTENT_KEY_PROJECT_CLASS_NAME, ProspectiveInfoFragment.class.getName());
                intent.putExtra(WorkLoadRecordListActivity.INTENT_KEY_PROJECT_TITLE, getString(R.string.project_kcyq_records_title));
                intent.putExtra(WorkLoadRecordListActivity.INTENT_KEY_PROJECT_LOG_TYPE, "kcsj");
                startActivity(intent);
            }
        });
    }
}
