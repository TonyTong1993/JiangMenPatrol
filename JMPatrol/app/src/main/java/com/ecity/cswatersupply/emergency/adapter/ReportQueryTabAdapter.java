package com.ecity.cswatersupply.emergency.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.fragment.ReportQueryInfoFragment;
import com.shizhefei.fragment.LazyFragment;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;

public class ReportQueryTabAdapter extends IndicatorFragmentPagerAdapter {
    private LayoutInflater inflater;
    private LazyFragment[] mFragements;
    private Context context;
    private static final int TAB_ZQSB = 0; //灾情速报
    private static final int TAB_XCDC = 1; //现场调查
    private static final int TAB_COUNT = 2;

    public ReportQueryTabAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;
        inflater = LayoutInflater.from(context.getApplicationContext());
        mFragements = new LazyFragment[getCount()];
    }

    public String getTitle(int position) {
        int strId = -1;

        switch (position) {
            case TAB_ZQSB:
                strId = R.string.str_emergency_condition_report;
                break;
            case TAB_XCDC:
                strId = R.string.str_emergency_detail_report;
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
            convertView = inflater.inflate(R.layout.tab_main_bottom, container, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.tv_tab_name);

        int strId = -1;
        int iconId = -1;

        switch (position) {
            case TAB_ZQSB:
                strId = R.string.str_emergency_condition_report;
                iconId = R.drawable.main_tab_messege;
                break;
            case TAB_XCDC:
                strId = R.string.str_emergency_detail_report;
                iconId = R.drawable.main_tab_my;
                break;
            default:
                break;
        }
        textView.setText(strId);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, iconId, 0, 0);
        if (strId == R.string.detail_tab_repair_info) {
            View spinner_tab = convertView.findViewById(R.id.spinner_tab);
            spinner_tab.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public LazyFragment getFragmentForPage(int position) {
        if (mFragements[position] != null) {
            return mFragements[position];
        }

        LazyFragment fragment = null;
        switch (position) {
            case TAB_ZQSB:
                fragment = new ReportQueryInfoFragment(position);
                break;
            case TAB_XCDC:
                fragment = new ReportQueryInfoFragment(position);
                break;
            default:
                break;
        }

        mFragements[position] = fragment;

        return fragment;
    }
}
