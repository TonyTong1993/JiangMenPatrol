package com.ecity.cswatersupply.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.menu.AppMenu;
import com.ecity.cswatersupply.service.UserService;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.MainTabViewHandleUtil;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;

import java.util.List;

public class MainTabAdapter extends IndicatorFragmentPagerAdapter {
    private Fragment[] mFragements;

    private List<List<AppMenu>> mTabs;
    private Context mContext;
    private LayoutInflater inflater;
    private List<AppMenu> dynamicTabMenus;

    public MainTabAdapter(Context context, FragmentManager fragmentManager, List<List<AppMenu>> tabs) {
        super(fragmentManager);
        dynamicTabMenus= UserService.getInstance().getDynamicTabMenus();
        inflater = LayoutInflater.from(context.getApplicationContext());
        this.mContext = context;
        this.mTabs = tabs;
        mFragements = new Fragment[getCount()];
    }

    @Override
    public int getCount() {
        int tabNum;
        if(!ListUtil.isEmpty(dynamicTabMenus)){
            tabNum=dynamicTabMenus.size();
        }else{
            HostApplication.ProjectStyle projectStyle = HostApplication.getApplication().getProjectStyle();
            if (projectStyle == HostApplication.ProjectStyle.PROJECT_CZDZ) {
                tabNum = 3;
            } else if (projectStyle == HostApplication.ProjectStyle.PROJECT_WHDZ) {
                tabNum = 4;
            } else if (projectStyle == HostApplication.ProjectStyle.PROJECT_FZXJ) {
                tabNum = 4;
            } else {
                tabNum = 4;
            }
        }
        return tabNum;
    }

    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.tab_main_bottom, container, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.tv_tab_name);
        MainTabViewHandleUtil.getTabViewStr(position, mTabs);
        textView.setText(MainTabViewHandleUtil.strId);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, MainTabViewHandleUtil.iconId, 0, 0);

        return convertView;

    }

    @Override
    public Fragment getFragmentForPage(int position) {
        if (mFragements[position] != null) {
            return mFragements[position];
        }
        Fragment fragment = null;
        MainTabViewHandleUtil.getTabViewStr(position, mTabs);
        fragment = MainTabViewHandleUtil.fragment;
        if (fragment != null) {
            mFragements[position] = fragment;
        }

        return fragment;
    }
}
