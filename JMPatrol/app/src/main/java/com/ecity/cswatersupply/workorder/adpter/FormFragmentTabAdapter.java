package com.ecity.cswatersupply.workorder.adpter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.workorder.fragment.FormFragment;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;

import java.util.List;

/**
 * Created by Gxx on 2017/4/13.
 */
public class FormFragmentTabAdapter extends IndicatorFragmentPagerAdapter {
    private LayoutInflater inflater;
    private Fragment[] mFragments;
    private List<InspectItem> mInspectItems;

    public FormFragmentTabAdapter(Context context, List<InspectItem> inspectItems, FragmentManager fragmentManager) {
        super(fragmentManager);
        inflater = LayoutInflater.from(context.getApplicationContext());
        this.mInspectItems = inspectItems;
        mFragments = new Fragment[getCount()];
    }

    @Override
    public int getCount() {
        return mInspectItems.size();
    }

    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.tab_bottom, container, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.tv_tab_name);
        String name = mInspectItems.get(position).getAlias();
        textView.setText(name);
        View spinner_tab = convertView.findViewById(R.id.spinner_tab);
        spinner_tab.setVisibility(View.GONE);

        return convertView;
    }

    @Override
    public Fragment getFragmentForPage(int position) {
        if (mFragments[position] != null) {
            return mFragments[position];
        }

        List<InspectItem> items = mInspectItems.get(position).getChilds();
        String alias = mInspectItems.get(position).getAlias();
        Fragment fragment = FormFragment.getInstance(alias, items);
        mFragments[position] = fragment;

        return fragment;
    }
}
