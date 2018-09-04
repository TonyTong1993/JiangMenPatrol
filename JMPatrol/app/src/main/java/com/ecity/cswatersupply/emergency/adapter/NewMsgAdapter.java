package com.ecity.cswatersupply.emergency.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ecity.cswatersupply.emergency.fragment.NewsAnnounFragmentFactory;
import com.ecity.cswatersupply.emergency.model.NewAnnountFragmentInfo;
import com.shizhefei.fragment.LazyFragment;
import com.viewpagerindicator.TitleProvider;

public class NewMsgAdapter extends FragmentPagerAdapter implements TitleProvider {

    private List<NewAnnountFragmentInfo> infos;

    public NewMsgAdapter(FragmentManager fm, List<NewAnnountFragmentInfo> infos) {
        super(fm);
        this.infos = infos;
    }

    @Override
    public LazyFragment getItem(int position) {
        return NewsAnnounFragmentFactory.getFragment(infos.get(position).getType());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return infos.get(position).getName();
    }

    @Override
    public int getCount() {
        return null == infos ? 0 : infos.size();
    }

    @Override
    public String getTitle(int position) {
        return getPageTitle(position).toString();
    }

    @Override
    public int getIconId(int position) {
        return infos.get(position).getIconId();
    }

}
