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

public class WorkOrderFinishFragmentBaseInfo extends Fragment {
    private static volatile WorkOrderFinishFragmentBaseInfo instance;

    private WorkOrderFinishActivity mWorkOrderFinishActivity;
    private InspectItem mInspectItem;
    private LinearLayout mLlContainer;

    private WorkOrderFinishFragmentBaseInfo() {
    }

    public static WorkOrderFinishFragmentBaseInfo getInstance() {
        if (null == instance) {
            synchronized (WorkOrderFinishFragmentBaseInfo.class) {
                if (null == instance) {
                    instance = new WorkOrderFinishFragmentBaseInfo();
                }
            }
        }

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_workorder_base_info, null);
        mLlContainer = (LinearLayout) convertView.findViewById(R.id.ll_container);
        mWorkOrderFinishActivity = (WorkOrderFinishActivity) getActivity();
        if (!ListUtil.isEmpty(mWorkOrderFinishActivity.getAllInspectItems())) {
            mInspectItem = mWorkOrderFinishActivity.getAllInspectItems().get(0);
        }
        if (null == mInspectItem || ListUtil.isEmpty(mInspectItem.getChilds())) {
            return convertView;
        }

        WorkOrderFinishActivity activity = (WorkOrderFinishActivity) getActivity();
        for (InspectItem item : mInspectItem.getChilds()) {
            View view = activity.getCustomViewInflater().inflate(item);
            mLlContainer.addView(view);
        }

        return convertView;
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
