package com.ecity.cswatersupply.ui.activities;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.ListUtil;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

public abstract class ABasePullToRefreshActivity<T> extends BaseActivity {
    private CustomTitleView viewTitle;
    private PullToRefreshListView refreshListView;
    private ListView lvRecords;
    private List<T> dataSource;
    private ArrayListAdapter<T> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(prepareLayoutResource());
        dataSource = prepareDataSource();
        internalInitUI();
    }

    protected abstract String getScreenTitle();

    protected abstract ArrayListAdapter<T> prepareListViewAdapter();

    protected abstract List<T> prepareDataSource();

    protected abstract OnItemClickListener prepareOnItemClickListener();

    protected abstract OnRefreshListener<ListView> prepareOnRefreshListener();

    protected void updateDataSource(List<T> dataSource) {
        this.dataSource = dataSource;
        adapter.setList(dataSource);
    }

    protected boolean isPullLoadEnabled() {
        return true;
    }

    protected boolean isPullRefreshEnabled() {
        return true;
    }

    protected int prepareLayoutResource() {
        return R.layout.activity_common_pulltorefresh_listview;
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    private void internalInitUI() {
        viewTitle = (CustomTitleView) findViewById(R.id.view_title);
        viewTitle.setTitleText(getScreenTitle());

        refreshListView = (PullToRefreshListView) findViewById(R.id.lv_container);
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
        return viewTitle;
    }

    public PullToRefreshListView getRefreshListView() {
        return refreshListView;
    }
}
