package com.ecity.cswatersupply.project.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.project.activity.fragment.ACommonListFragment;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;

public class ProjectListInfoTabAdapter extends IndicatorFragmentPagerAdapter {
    private LayoutInflater inflater;
    private Fragment[] mFragements;
    private Context context;
    private static final int TAB_INDEXT_NOT_HANDLE = 0; //未处理
    private static final int TAB_INDEXT_HANDLE = 1; //已处理
    private static final int TAB_COUNT = 2;
    private Class<? extends ACommonListFragment> fragmentClass;

    public ProjectListInfoTabAdapter(Context context, FragmentManager fragmentManager, Class<? extends ACommonListFragment> fragmentClass) {
        super(fragmentManager);
        this.context = context;
        this.fragmentClass = fragmentClass;
        inflater = LayoutInflater.from(context.getApplicationContext());
        mFragements = new Fragment[getCount()];
    }

    public String getTitle(int position) {
        int strId = -1;

        switch (position) {
            case TAB_INDEXT_NOT_HANDLE:
                strId = R.string.project_prospective_not_handle;
                break;
            case TAB_INDEXT_HANDLE:
                strId = R.string.project_prospective_handle;
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
            case TAB_INDEXT_NOT_HANDLE:
                strId = R.string.project_prospective_not_handle;
                iconId = R.drawable.main_tab_messege;
                break;
            case TAB_INDEXT_HANDLE:
                strId = R.string.project_prospective_handle;
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
    public Fragment getFragmentForPage(int position) {
        if (mFragements[position] != null) {
            return mFragements[position];
        }

        ACommonListFragment fragment = null;
        switch (position) {
            case TAB_INDEXT_NOT_HANDLE:
                fragment = getFragment();
                fragment.setPosition(position);

                break;
            case TAB_INDEXT_HANDLE:
                fragment = getFragment();
                fragment.setPosition(position);
                break;
            default:
                break;
        }

        mFragements[position] = fragment;

        return fragment;
    }

    private ACommonListFragment getFragment() {
        try {
            ACommonListFragment fragment = fragmentClass.newInstance();
            return fragment;
        } catch (InstantiationException e) {
            LogUtil.e(this, e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            LogUtil.e(this, e);
            throw new RuntimeException(e);
        }
    }
}
