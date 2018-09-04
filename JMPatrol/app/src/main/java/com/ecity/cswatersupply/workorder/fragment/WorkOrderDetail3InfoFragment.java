package com.ecity.cswatersupply.workorder.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.ui.activities.WorkOrderDetailFragmentActivity;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.workorder.WorkOrderUtil;

/**
 * 工单详情－ 基本信息/勘察信息／维修信息 Fragment
 * @author ecity
 *
 */
@SuppressLint("ValidFragment")
public class WorkOrderDetail3InfoFragment extends Fragment {
    private WorkOrderDetailFragmentActivity mWorkOrderDetailActivity;
    private LinearLayout mLlContainer;
    private String tabName;
    private TextView tvMessageBlank;
    private static Map<String, WorkOrderDetail3InfoFragment> tabName2Instances = new HashMap<String, WorkOrderDetail3InfoFragment>();
    private List<InspectItem> items = new ArrayList<InspectItem>();

    private WorkOrderDetail3InfoFragment(String tabName) {
        this.tabName = tabName;
    }

    public static WorkOrderDetail3InfoFragment getInstance(String tabName) {
        WorkOrderDetail3InfoFragment instance = tabName2Instances.get(tabName);
        if (instance == null) {
            instance = new WorkOrderDetail3InfoFragment(tabName);
            tabName2Instances.put(tabName, instance);
        }

        return instance;
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBusUtil.register(this);
        View convertView = inflater.inflate(R.layout.fragment_workorder_base_info, null);
        mLlContainer = (LinearLayout) convertView.findViewById(R.id.ll_container);
        mWorkOrderDetailActivity = (WorkOrderDetailFragmentActivity) getActivity();
        tvMessageBlank = (TextView) convertView.findViewById(R.id.tv_normal_content_blank);

        return convertView;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUI();
    }

    @Override
    public void onDestroyView() {
        EventBusUtil.unregister(this);
        super.onDestroyView();
    }

    public void onBackButtonClicked(View view) {
        this.mWorkOrderDetailActivity.finish();
    }

    public void onActionButtonClicked(View view) {
    }

    public String getTabName() {
        return tabName;
    }

    private void refreshUI() {
        mLlContainer.removeAllViews();
        //判断工单状态
        String status = WorkOrderUtil.getWorkOrderStateString(mWorkOrderDetailActivity.getCurrentWorkOrder().getAttributes());
        if ((status.equals("待分派")) || (status.equals("待接单"))) {
            if (getTabName().equals("勘察信息") || getTabName().equals("维修信息")) {
                tvMessageBlank.setVisibility(View.VISIBLE);
                mLlContainer.setVisibility(View.GONE);
            } else {
                items = mWorkOrderDetailActivity.getWorkOrderInfoByAlias("基本信息");
                tvMessageBlank.setVisibility(View.GONE);
                mLlContainer.setVisibility(View.VISIBLE);
            }
        } else {
            items = mWorkOrderDetailActivity.getWorkOrderInfoByAlias(getTabName());
        }

        if (ListUtil.isEmpty(items)) {
            return;
        }

        CustomViewInflater customViewInflater = new CustomViewInflater(getActivity());
        for (InspectItem item : items) {
            mLlContainer.addView(customViewInflater.inflate(item));
        }
    }

    public void onEventMainThread(UIEvent event) {
        if (event.getId() == UIEventStatus.WORKORDER_DETAIL_REFRESH_FRAGMENT) {
            refreshUI();
        }
    }

}
