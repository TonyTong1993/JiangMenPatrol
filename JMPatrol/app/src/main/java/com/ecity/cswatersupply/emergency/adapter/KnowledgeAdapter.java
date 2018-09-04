package com.ecity.cswatersupply.emergency.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ecity.cswatersupply.emergency.fragment.EmergencyPlanFragment;
import com.ecity.cswatersupply.emergency.fragment.KnowLedgeFragmentFactory;
import com.ecity.cswatersupply.emergency.model.DownloadFragmentInfo;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.viewpagerindicator.TitleProvider;

public class KnowledgeAdapter extends FragmentPagerAdapter implements TitleProvider {
    private List<DownloadFragmentInfo> infos;

    public KnowledgeAdapter(FragmentManager fm, List<DownloadFragmentInfo> infos) {
        super(fm);
        this.infos = infos;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (infos.get(position).getType()) {
            case EMERGENCY_PLAN:
                fragment = new EmergencyPlanFragment(1, ResponseEventStatus.EMERGENCY_GET_KNOWBASE_EMERGENCY_PLAN);
                break;

            case EMERGENCY_SUGGEST:
                fragment = new EmergencyPlanFragment(2, ResponseEventStatus.EMERGENCY_GET_KNOWBASE_EMERGENCY_IDEA);
                break;

            case EARTHQUAKE_KNOWLEDGE:
                fragment = new EmergencyPlanFragment(3, ResponseEventStatus.EMERGENCY_GET_KNOWBASE_EMERGENCY_COMMON);
                break;
        }
        return fragment;
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
