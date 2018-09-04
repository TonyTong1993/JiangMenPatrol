package com.ecity.cswatersupply.workorder.adpter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.workorder.fragment.WorkOrderFinishFragmentBaseInfo;
import com.ecity.cswatersupply.workorder.fragment.WorkOrderFinishFragmentMaterielInfo;
import com.ecity.cswatersupply.workorder.fragment.WorkOrderFinishFragmentPersonnelInfo;
import com.viewpagerindicator.TitleProvider;

public class WorkOrderFinishAdapter extends FragmentPagerAdapter implements TitleProvider {
    private List<String> mTitles;

    public WorkOrderFinishAdapter(FragmentManager fm, List<String> titles) {
        super(fm);
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return WorkOrderFinishFragmentBaseInfo.getInstance();
            case 1:
                return WorkOrderFinishFragmentMaterielInfo.getInstance();
            case 2:
                return WorkOrderFinishFragmentPersonnelInfo.getInstance();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        if (ListUtil.isEmpty(mTitles)) {
            return 1;
        }
        return mTitles.size();
    }

    @Override
    public String getTitle(int arg0) {
        if (ListUtil.isEmpty(mTitles)) {
            return "";
        }
        return mTitles.get(arg0);
    }

    @Override
    public int getIconId(int position) {
        return 0;
    }
}
