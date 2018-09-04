package com.ecity.cswatersupply.ui.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.utils.ListUtil;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;

public abstract class ABaseListViewFragment<T> extends Fragment {
    private PullToRefreshListView refreshListView;
    private ListView lvRecords;
    private List<T> dataSource;
    private ArrayListAdapter<T> adapter;
    private CustomTitleView customTitleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(prepareLayoutResource(), null);
        internalInitUI(view);
        dataSource = prepareDataSource();
        initData();
        return view;
    }

    protected abstract ArrayListAdapter<T> prepareListViewAdapter();

    protected abstract List<T> prepareDataSource();

    protected abstract OnItemClickListener prepareOnItemClickListener();

    protected abstract OnRefreshListener<ListView> prepareOnRefreshListener();

    protected boolean isPullLoadEnabled() {
        return true;
    }

    protected boolean isPullRefreshEnabled() {
        return true;
    }

    protected int prepareLayoutResource() {
        return R.layout.fragment_common_list;
    }

    protected abstract String getTitle();

    private void internalInitUI(View view) {
        customTitleView = (CustomTitleView) view.findViewById(R.id.customTitleView);
        customTitleView.setBtnStyle(BtnStyle.NOBTN);
        customTitleView.setTitleText(getTitle());
        refreshListView = (PullToRefreshListView) view.findViewById(R.id.lv_container);
    }

    private void initData() {
        refreshListView.setPullLoadEnabled(isPullLoadEnabled());
        refreshListView.setPullRefreshEnabled(isPullRefreshEnabled());
        lvRecords = refreshListView.getRefreshableView();
        refreshListView.setOnRefreshListener(prepareOnRefreshListener());
        adapter = prepareListViewAdapter();
        adapter.setList(dataSource);
        lvRecords.setAdapter(adapter);
        lvRecords.setOnItemClickListener(prepareOnItemClickListener());
    }

    public List<T> getDataSource() {
        return ListUtil.getSafeList(dataSource);
    }

    public ArrayListAdapter<T> getAdapter() {
        return adapter;
    }
    public CustomTitleView getTitleView() {
        return customTitleView;
    }
    public PullToRefreshListView getRefreshListView() {
        return refreshListView;
    }
    
    protected void updateDataSource(List<T> dataSource) {
        this.dataSource = dataSource;
        adapter.setList(dataSource);
    }
}
