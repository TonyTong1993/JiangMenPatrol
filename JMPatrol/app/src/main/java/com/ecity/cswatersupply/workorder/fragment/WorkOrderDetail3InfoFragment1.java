package com.ecity.cswatersupply.workorder.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.WorkOrderDetailTabModel;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.ui.activities.WorkOrderDetailFragmentActivity1;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.workorder.WorkOrderUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工单详情－ 基本信息/勘察信息／维修信息 Fragment
 * @author Gxx
 *
 */
@SuppressLint("ValidFragment")
public class WorkOrderDetail3InfoFragment1 extends Fragment {
    private WorkOrderDetailFragmentActivity1 mWorkOrderDetailActivity;
    private LinearLayout mLlContainer;
    private WorkOrderDetailTabModel tab;
    private TextView tvMessageBlank;
    private int eventId;
    private List<InspectItem> items = new ArrayList<InspectItem>();
    private static Map<String, WorkOrderDetail3InfoFragment1> tabName2Instances = new HashMap<String, WorkOrderDetail3InfoFragment1>();

    private WorkOrderDetail3InfoFragment1(WorkOrderDetailTabModel tab, int eventId) {
        this.tab = tab;
        this.eventId = eventId;
    }

    public static WorkOrderDetail3InfoFragment1 getInstance(WorkOrderDetailTabModel tab, int eventId) {
        WorkOrderDetail3InfoFragment1 instance = tabName2Instances.get(tab.getName());
        if (instance == null) {
            instance = new WorkOrderDetail3InfoFragment1(tab, eventId);
            tabName2Instances.put(tab.getName(), instance);
        }

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBusUtil.register(this);
        View convertView = inflater.inflate(R.layout.fragment_workorder_base_info, null);
        mLlContainer = (LinearLayout) convertView.findViewById(R.id.ll_container);
        mWorkOrderDetailActivity = (WorkOrderDetailFragmentActivity1) getActivity();
        tvMessageBlank = (TextView) convertView.findViewById(R.id.tv_normal_content_blank);
        return convertView;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestDetailInfo();
    }

    @Override
    public void onDestroyView() {
        EventBusUtil.unregister(this);
        super.onDestroyView();
    }

    private void requestDetailInfo() {
        LoadingDialogUtil.show(mWorkOrderDetailActivity,R.string.workorder_downloading);
        String url = WorkOrderUtil.getWorkOrderDetailUrl(tab);
        String processInstanceId = mWorkOrderDetailActivity.getProcessInstanceId();
        Map<String, String> map = WorkOrderUtil.getWorkOrderDetailParam(tab, processInstanceId);
        WorkOrderService.instance.getWorkOrderInfo(url, map, eventId);
    }

    public void onBackButtonClicked(View view) {
        this.mWorkOrderDetailActivity.finish();
    }

    private void refreshUI() {
        mLlContainer.removeAllViews();
        if (ListUtil.isEmpty(items)) {
            tvMessageBlank.setVisibility(View.VISIBLE);
            return;
        }
        tvMessageBlank.setVisibility(View.GONE);
        CustomViewInflater customViewInflater = new CustomViewInflater(getActivity());
        for (InspectItem item : items) {
            mLlContainer.addView(customViewInflater.inflate(item));
        }
    }

    public void onEventMainThread(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        if(!event.isOK()) {
            ToastUtil.showLong(event.getMessage());
            return;
        }

        if(eventId == event.getId()) {
            handleGetWorkOrderInfoEvent(event);
        }
    }

    private void handleGetWorkOrderInfoEvent(ResponseEvent event) {
        Map<String, List<InspectItem>> workOrderDetailInfo = event.getData();
        if(tab.getName().equals(ResourceUtil.getStringById(R.string.workorder_detail_tab_basic))) {
            items = workOrderDetailInfo.get(ResourceUtil.getStringById(R.string.workorder_detail_param_basic));
        } else if(tab.getName().equals(ResourceUtil.getStringById(R.string.workorder_detail_tab_explor))) {
            items = workOrderDetailInfo.get(ResourceUtil.getStringById(R.string.workorder_detail_param_explor));
        } else if(tab.getName().equals(ResourceUtil.getStringById(R.string.workorder_detail_tab_material))) {
            items = workOrderDetailInfo.get(ResourceUtil.getStringById(R.string.workorder_detail_param_material));
        } else if(tab.getName().equals(ResourceUtil.getStringById(R.string.workorder_detail_tab_repair))) {
            items = workOrderDetailInfo.get(ResourceUtil.getStringById(R.string.workorder_detail_param_repair));
        }

        refreshUI();
    }
}
