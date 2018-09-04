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
import com.ecity.cswatersupply.project.activity.fragment.SafeManageDetailFragment;
import com.ecity.cswatersupply.project.network.response.SafeDetailStepModel;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;

import java.util.List;

public class SafeManageDetailAdapter extends IndicatorFragmentPagerAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<SafeDetailStepModel> data;
    private static final int TAB_COUNT = 3;


    public SafeManageDetailAdapter(Context context, FragmentManager fragmentManager, List<SafeDetailStepModel> data) {
        super(fragmentManager);
        inflater = LayoutInflater.from(context.getApplicationContext());
        this.mContext = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }


    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.tab_main_bottom, container, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.tv_tab_name);
        String str = "";
        int iconId = -1;

        switch (position) {
            case 0:
                iconId = R.drawable.main_tab_messege;
                break;
            case 1:
                iconId = R.drawable.main_tab_application;
                break;
            case 2:
                iconId = R.drawable.main_tab_map;
                break;
            default:
                break;
        }
        str = data.get(position).getStepname();
        textView.setText(str);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, iconId, 0, 0);

        return convertView;

    }

    @Override
    public Fragment getFragmentForPage(int position) {

        SafeManageDetailFragment fragment = null;
        switch (position) {
            case 0:
                fragment = new SafeManageDetailFragment(ResponseEventStatus.PROJECT_SAFE_MANAGE_GET_DETAIL_INFO1, data.get(position).getStep(),data.get(position));
                break;
            case 1:
                fragment = new SafeManageDetailFragment(ResponseEventStatus.PROJECT_SAFE_MANAGE_GET_DETAIL_INFO2, data.get(position).getStep(),data.get(position));
                break;
            case 2:
                fragment = new SafeManageDetailFragment(ResponseEventStatus.PROJECT_SAFE_MANAGE_GET_DETAIL_INFO3, data.get(position).getStep(),data.get(position));
                break;
            default:
                break;
        }
        return fragment;
    }
}
