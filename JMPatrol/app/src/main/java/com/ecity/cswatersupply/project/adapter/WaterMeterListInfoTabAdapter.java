package com.ecity.cswatersupply.project.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.project.activity.fragment.WaterMeterListFragment;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;

public class WaterMeterListInfoTabAdapter extends IndicatorFragmentPagerAdapter {
    private LayoutInflater inflater;
    private Context context;
    private String type;
    private String startTime;
    private String endTime;
    private static final int TAB_INDEXT_WWG = 0; //未完工
    private static final int TAB_INDEXT_YWG = 2; //已完工
    private static final int TAB_INDEXT_GQ = 1; //挂起
    private static final int TAB_INDEXT_FJ = 3; //废件
    private static final int TAB_INDEXT_CQ = 4; //超期
    private static final int TAB_COUNT = 5;

    public WaterMeterListInfoTabAdapter(Context context, FragmentManager fragmentManager, String type, String stratTime, String endTime) {
        super(fragmentManager);
        this.context = context;
        this.type = type;
        this.startTime = stratTime;
        this.endTime = endTime;
        inflater = LayoutInflater.from(context.getApplicationContext());
    }

    public String getTitle(int position) {
        String str = "";

        switch (position) {
            case TAB_INDEXT_WWG:
                str = "未完工";
                break;
            case TAB_INDEXT_GQ:
                str = "挂起";
                break;
            case TAB_INDEXT_YWG:
                str = "已完成";
                break;
            case TAB_INDEXT_FJ:
                str = "废件";
                break;
            case TAB_INDEXT_CQ:
                str = "超期";
                break;
            default:
                break;
        }

        return str;
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

        textView.setText(getTitle(position));

        return convertView;
    }

    @Override
    public Fragment getFragmentForPage(int position) {
        Fragment fragment = null;

        switch (position) {
            case TAB_INDEXT_WWG:
                fragment = new WaterMeterListFragment(type, position + 1, startTime, endTime, ResponseEventStatus.PROJECT_GET_WATERMETER_LIST1);
                break;
            case TAB_INDEXT_GQ:
                fragment = new WaterMeterListFragment(type, position + 1, startTime, endTime, ResponseEventStatus.PROJECT_GET_WATERMETER_LIST2);
                break;
            case TAB_INDEXT_YWG:
                fragment = new WaterMeterListFragment(type, position + 1, startTime, endTime, ResponseEventStatus.PROJECT_GET_WATERMETER_LIST3);
                break;
            case TAB_INDEXT_FJ:
                fragment = new WaterMeterListFragment(type, position + 1, startTime, endTime, ResponseEventStatus.PROJECT_GET_WATERMETER_LIST4);
                break;
            case TAB_INDEXT_CQ:
                fragment = new WaterMeterListFragment(type, -1, startTime, endTime, ResponseEventStatus.PROJECT_GET_WATERMETER_LIST5);
                break;
            default:
                break;
        }

        return fragment;
    }
}
