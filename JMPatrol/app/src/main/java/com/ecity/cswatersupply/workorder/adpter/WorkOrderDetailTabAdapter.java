package com.ecity.cswatersupply.workorder.adpter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.workorder.fragment.WorkOrderDetail3InfoFragment;
import com.ecity.cswatersupply.workorder.fragment.WorkOrderDetailFlowInfoFragment;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;

public class WorkOrderDetailTabAdapter extends IndicatorFragmentPagerAdapter {
    private LayoutInflater inflater;
    private Fragment[] mFragements;
    private Context context;
    private static final int TAB_INDEXT_BSIC_INFO = 0; //工单详情－基本信息
    private static final int TAB_INDEXT_EXPLORE_INFO = 1; //工单详情－勘查信息
    private static final int TAB_INDEX_FLOW_INFO = 2; //工单详情－流程信息
    private static final int TAB_INDEX_REPAIR_INFO = 3; //工单详情－维修信息
    private static final int TAB_COUNT = 4;

    public WorkOrderDetailTabAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;
        inflater = LayoutInflater.from(context.getApplicationContext());
        mFragements = new Fragment[getCount()];
    }

    public String getTitle(int position) {
        int strId = -1;

        switch (position) {
            case TAB_INDEXT_BSIC_INFO:
                strId = R.string.detail_tab_basic_info;
                break;
            case TAB_INDEXT_EXPLORE_INFO:
                strId = R.string.detail_tab_explore_info;
                break;
            case TAB_INDEX_FLOW_INFO:
                strId = R.string.detail_tab_flow_info;
                break;
            case TAB_INDEX_REPAIR_INFO:
                strId = R.string.detail_tab_repair_info;
                break;
            default:
                break;
        }

        return context.getString(strId);
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.tab_bottom, container, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.tv_tab_name);

        int strId = -1;

        switch (position) {
            case TAB_INDEXT_BSIC_INFO:
                strId = R.string.detail_tab_basic_info;
                break;
            case TAB_INDEXT_EXPLORE_INFO:
                strId = R.string.detail_tab_explore_info;
                break;
            case TAB_INDEX_FLOW_INFO:
                strId = R.string.detail_tab_flow_info;
                break;
            case TAB_INDEX_REPAIR_INFO:
                strId = R.string.detail_tab_repair_info;
                break;
            default:
                break;
        }
        textView.setText(strId);
        if (strId == R.string.detail_tab_repair_info) {
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
        switch (position) {
            case TAB_INDEXT_BSIC_INFO:
                fragment = WorkOrderDetail3InfoFragment.getInstance(getTitle(position));
                break;
            case TAB_INDEXT_EXPLORE_INFO:
                fragment = WorkOrderDetail3InfoFragment.getInstance(getTitle(position));
                break;
            case TAB_INDEX_FLOW_INFO:
                fragment = WorkOrderDetailFlowInfoFragment.getInstance();
                break;
            case TAB_INDEX_REPAIR_INFO:
                fragment = WorkOrderDetail3InfoFragment.getInstance(getTitle(position));
                break;
            default:
                break;
        }

        if (fragment != null) {
            mFragements[position] = fragment;
        }

        return fragment;
    }

}
