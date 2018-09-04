package com.ecity.cswatersupply.project.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.project.service.ProjectServiceUrlManager;

import java.util.Map;

public class ProjectInfoFragment extends AProjectCommonInspectItemFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = super.onCreateView(inflater, container, savedInstanceState);
        requestId = getSupportedRequestIds()[position];
        queryInfo();

        return convertView;
    }

    @Override
    protected String getQueryInfoUrl() {
        String url = "";
        switch (position) {
            case 0:
                url = ProjectServiceUrlManager.getInstance().getProBaseInfo();
                break;
            case 1:
                url = ProjectServiceUrlManager.getInstance().getProBaseWorkload();
                break;
            case 2:
                url = ProjectServiceUrlManager.getInstance().getProBaseProgress();
                break;
            case 3:
                url = ProjectServiceUrlManager.getInstance().getProBaseChage();
                break;
            case 4:
                url = ProjectServiceUrlManager.getInstance().getProBasePay();
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
        return getString(R.string.str_searching);
    }

    @Override
    protected void fillQueryParameters(Map<String, String> map) {
        map.put("proid", projectId);
        if (proType.equals("1")) {
            map.put("proType", "2");
        }
    }

    private int[] getSupportedRequestIds() {
        return new int[]{ResponseEventStatus.PROJECT_GET_PROJECT_DETAIL_TAB_BASEINFO, ResponseEventStatus.PROJECT_GET_PROJECT_DETAIL_TAB_WORKLOAD,
                ResponseEventStatus.PROJECT_GET_PROJECT_DETAIL_TAB_PROGRESS, ResponseEventStatus.PROJECT_GET_PROJECT_DETAIL_TAB_CHANGE,
                ResponseEventStatus.PROJECT_GET_PROJECT_DETAIL_TAB_PAY};
    }

    @Override
    protected Class<? extends AProjectCommonInspectItemFragment> getFragmentClass() {
        return ProjectInfoFragment.class;
    }
}
