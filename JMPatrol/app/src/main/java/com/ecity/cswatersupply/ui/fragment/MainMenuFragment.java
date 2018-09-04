package com.ecity.cswatersupply.ui.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.MainMenuAdapter;
import com.ecity.cswatersupply.menu.AppMenu;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.xg.util.NotificationUtil;


@SuppressLint("ValidFragment")
public class MainMenuFragment extends Fragment {
    private GridView mGridView;
    private CustomTitleView customTitleView;
    private MainMenuAdapter mAdapter;
    private List<AppMenu> menus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_menu_grid, null);
        customTitleView = (CustomTitleView) v.findViewById(R.id.customTitleView);
        customTitleView.setBtnStyle(CustomTitleView.BtnStyle.NOBTN);
        customTitleView.setTitleText(getActivity().getResources().getString(R.string.fragment_patrol_title));
        mGridView = (GridView) v.findViewById(R.id.main_menu_grid);
        bindData(menus);
        initListener();
        return v;
    }

    public MainMenuFragment() {

    }
    public MainMenuFragment(List<AppMenu> menus) {
        this.menus = menus;
    }

    public void updateDataSet(List<AppMenu> menus) {
        mAdapter.setItems(menus);
        mAdapter.notifyDataSetChanged();
    }

    private void bindData(List<AppMenu> menus) {
        mAdapter = new MainMenuAdapter(getActivity(), menus);
        mGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void initListener() {
        mGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    NotificationUtil.clearNotificationById(getActivity(), NotificationUtil.SPECIAL_NOTIFICATION);
                    menus.get(position).execute();
                } catch (Exception e) {
                    LogUtil.e(this, e);
                }
            }
        });
    }

    public void setMenus(List<AppMenu> menus) {
        this.menus = menus;
    }

}
