package com.ecity.cswatersupply.workorder.adpter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.WorkOrderDetailTabModel;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.workorder.fragment.WorkOrderDetail3InfoFragment;
import com.ecity.cswatersupply.workorder.fragment.WorkOrderDetail3InfoFragment1;
import com.ecity.cswatersupply.workorder.fragment.WorkOrderDetailFlowInfoFragment;
import com.ecity.cswatersupply.workorder.fragment.WorkOrderDetailFlowInfoFragment1;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkOrderDetailTabAdapter1 extends IndicatorFragmentPagerAdapter {
    private LayoutInflater inflater;
    private List<WorkOrderDetailTabModel> list;
    private Fragment[] mFragements;
    private Map<String, Integer> tabEventIdMap = new HashMap<String, Integer>();

    public WorkOrderDetailTabAdapter1(Context context, List<WorkOrderDetailTabModel> list, FragmentManager fragmentManager) {
        super(fragmentManager);
        inflater = LayoutInflater.from(context.getApplicationContext());
        this.list = list;
        mFragements = new Fragment[getCount()];
        initData();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.tab_bottom, container, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.tv_tab_name);
        WorkOrderDetailTabModel tab = list.get(position);
        textView.setText(tab.getName());
        if (position == list.size() - 1) {
            View spinner_tab = convertView.findViewById(R.id.spinner_tab);
            spinner_tab.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public Fragment getFragmentForPage(int position) {
        if (mFragements[position] != null) {
            return mFragements[position];
        }

        Fragment fragment = null;
        WorkOrderDetailTabModel tab = list.get(position);
        String tabName = tab.getName();
        if(tabName.equals(ResourceUtil.getStringById(R.string.workorder_detail_tab_audit))) {
            fragment = WorkOrderDetailFlowInfoFragment1.getInstance(tab, tabEventIdMap.get(tabName));
        } else {
            fragment = WorkOrderDetail3InfoFragment1.getInstance(tab, tabEventIdMap.get(tabName));
        }

        return fragment;
    }

    private void initData() {
        tabEventIdMap.put(ResourceUtil.getStringById(R.string.workorder_detail_tab_basic), ResponseEventStatus.WORKORDER_GET_DETAIL_BASIC);
        tabEventIdMap.put(ResourceUtil.getStringById(R.string.workorder_detail_tab_explor),ResponseEventStatus.WORKORDER_GET_DETAIL_EXPLOR);
        tabEventIdMap.put(ResourceUtil.getStringById(R.string.workorder_detail_tab_material),ResponseEventStatus.WORKORDER_GET_DETAIL_MATERIAL);
        tabEventIdMap.put(ResourceUtil.getStringById(R.string.workorder_detail_tab_audit),ResponseEventStatus.WORKORDER_GET_DETAIL_AUDIT);
        tabEventIdMap.put(ResourceUtil.getStringById(R.string.workorder_detail_tab_repair),ResponseEventStatus.WORKORDER_GET_DETAIL_REPAIR);
    }
}
