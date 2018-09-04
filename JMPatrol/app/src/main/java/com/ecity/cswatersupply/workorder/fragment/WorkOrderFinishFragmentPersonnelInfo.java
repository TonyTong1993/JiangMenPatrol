package com.ecity.cswatersupply.workorder.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.workorder.WorkOrderFinishActivity;

public class WorkOrderFinishFragmentPersonnelInfo extends Fragment {

    private static volatile WorkOrderFinishFragmentPersonnelInfo instance;
    private InspectItem mInspectItem;
    private LinearLayout mLlContainer;
    private WorkOrderFinishActivity mWorkOrderFinishActivity;

    private WorkOrderFinishFragmentPersonnelInfo() {
    }

    public static WorkOrderFinishFragmentPersonnelInfo getInstance() {
        if (null == instance) {
            synchronized (WorkOrderFinishFragmentBaseInfo.class) {
                if (null == instance) {
                    instance = new WorkOrderFinishFragmentPersonnelInfo();
                }
            }
        }

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workorder_base_info, null);
        mLlContainer = (LinearLayout) view.findViewById(R.id.ll_container);
        mWorkOrderFinishActivity = (WorkOrderFinishActivity) getActivity();
        if (null != mWorkOrderFinishActivity.getAllInspectItems() && mWorkOrderFinishActivity.getAllInspectItems().size() > 2) {
            mInspectItem = mWorkOrderFinishActivity.getAllInspectItems().get(2);
        }
        if (null == mInspectItem || ListUtil.isEmpty(mInspectItem.getChilds())) {
            return view;
        }

        WorkOrderFinishActivity activity = (WorkOrderFinishActivity) getActivity();
        for (InspectItem item : mInspectItem.getChilds()) {
            mLlContainer.addView(activity.getCustomViewInflater().inflate(item));
        }

        return view;
    }

    public void onBackButtonClicked(View view) {
        this.mWorkOrderFinishActivity.finish();
    }

    /**
     * 提交方法
     * @param view
     */
    public void onActionButtonClicked(View view) {

    }
}
