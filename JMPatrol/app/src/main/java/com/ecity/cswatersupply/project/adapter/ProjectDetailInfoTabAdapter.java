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
import com.ecity.cswatersupply.project.activity.fragment.AProjectCommonInspectItemFragment;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;

public class ProjectDetailInfoTabAdapter extends IndicatorFragmentPagerAdapter {
    private LayoutInflater inflater;
    private Fragment[] fragments;
    private String proType;
    private String projectId;
    private String[] tabTitles;
    private Class<AProjectCommonInspectItemFragment> fragmentClass;
    private String recordId;

    public ProjectDetailInfoTabAdapter(Context context, FragmentManager fragmentManager, String[] tabTitles, Class<AProjectCommonInspectItemFragment> fragmentClass,
                                       String projectId, String recordId, String proType) {
        super(fragmentManager);

        this.inflater = LayoutInflater.from(context.getApplicationContext());
        this.projectId = projectId;
        this.recordId = recordId;
        this.tabTitles = tabTitles;
        this.proType = proType;
        this.fragmentClass = fragmentClass;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.tab_bottom, container, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.tv_tab_name);
        textView.setText(tabTitles[position]);

        return convertView;
    }

    @Override
    public Fragment getFragmentForPage(int position) {
        if (fragments == null) {
            fragments = new Fragment[getCount()];
        }

        if (fragments[position] != null) {
            return fragments[position];
        }

        AProjectCommonInspectItemFragment fragment;
        try {
            fragment = fragmentClass.newInstance();
        } catch (InstantiationException e) {
            LogUtil.e(this, e);
            throw new RuntimeException(e); // 出现这个异常必然是代码问题,直接退出.
        } catch (IllegalAccessException e) {
            LogUtil.e(this, e);
            throw new RuntimeException(e); // 出现这个异常必然是代码问题,直接退出.
        }

        fragment.setProjectId(projectId);
        fragment.setPosition(position);
        fragment.setRecordId(recordId);
        fragment.setProType(proType);
        fragments[position] = fragment;

        return fragment;
    }
}
