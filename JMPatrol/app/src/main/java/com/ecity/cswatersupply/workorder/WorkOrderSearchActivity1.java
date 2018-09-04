package com.ecity.cswatersupply.workorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.adpter.WorkOrderSearchAdapter;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.view.CustomWorkOrderActivity;
import com.lee.pullrefresh.ui.PullRefreshExpandableListView;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WorkOrderSearchActivity1 extends BaseActivity implements OnGroupClickListener, OnChildClickListener {
    public final static int WORK_ORDER_SEARCH_RESULT_CODE = 1;
    private List<List<WorkOrder>> searchResult;
    private List<String> groupTitles;
    private TextView tvSearch;
    private Button btn_search;

    private ExpandableListView expandableListView;
    private PullRefreshExpandableListView mPullExpandableListView;
    private WorkOrderSearchAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_workorder);
        EventBusUtil.register(this);
        initView();
        bindEvents();
    }

    public void onBackButtonClicked(View v) {
        finish();
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return true;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        WorkOrder workOrder = mAdapter.getChild(groupPosition, childPosition);
        Intent intent = new Intent();
        intent.putExtra("workOrder", workOrder);
        setResult(WORK_ORDER_SEARCH_RESULT_CODE, intent);
        finish();
        return false;
    }

    private void initView() {
        CustomTitleView title = (CustomTitleView) findViewById(R.id.customTitleView1);
        title.setBtnStyle(BtnStyle.ONLY_BACK);
        title.setTitleText(R.string.workorder_search_title);
        tvSearch = (TextView) findViewById(R.id.tv_search);
        btn_search = (Button) findViewById(R.id.btn_search);

        mPullExpandableListView = (PullRefreshExpandableListView) findViewById(R.id.expandableListView1);
        mPullExpandableListView.setPullLoadEnabled(false);
        mPullExpandableListView.setPullRefreshEnabled(false);
        mAdapter = new WorkOrderSearchAdapter(this);
        expandableListView = mPullExpandableListView.getRefreshableView();
        // ExpandableListView设置adapter之前，要先设置GroupList
        initExpandableListViewData();
        expandableListView.setAdapter(mAdapter);
        expandableListView.setOnChildClickListener(this);
    }

    private void bindEvents() {
        btn_search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                searchRequestWorkOrders();
            }
        });
    }

    private void initExpandableListViewData() {
        List<String> groupTitles = new ArrayList<String>();
        mAdapter.setGroupList(groupTitles);
        List<List<WorkOrder>> childrenList = new ArrayList<List<WorkOrder>>();
        for (int i = 0; i < groupTitles.size(); i++) {
            childrenList.add(new ArrayList<WorkOrder>());
        }
        mAdapter.setChildrenList(childrenList);
    }

    private void clearSearchResult() {
        if (searchResult != null) {
            searchResult.clear();
        } else {
            searchResult = new ArrayList<List<WorkOrder>>();
        }

        if (groupTitles != null) {
            groupTitles.clear();
        } else {
            groupTitles = new ArrayList<String>();
        }
    }

    private void refreshListView() {
        mAdapter.setGroupList(groupTitles);
        mAdapter.setChildrenList(searchResult);
        for (int i = 0; i < groupTitles.size(); i++) {
            expandableListView.expandGroup(i);
        }
    }

    private void searchRequestWorkOrders() {
        String searchStr = tvSearch.getText().toString().trim();
        if (!StringUtil.isEmpty(searchStr)) {
            WorkOrderService.instance.getWorkOrderSearchResult1(searchStr);
        }
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.WORKORDER_SEARCH_WORKORDER_RESULT:
                clearSearchResult();
                handleGetSearchResult(event);
                refreshListView();
                break;
            default:
                break;
        }
    }

    private void handleGetSearchResult(ResponseEvent event) {
        Map<String, List<WorkOrder>> data = new HashMap<String, List<WorkOrder>>();
        data = event.getData();
        Iterator<String> keies = data.keySet().iterator();
        while (keies.hasNext()) {
            String keyStr = keies.next();
            List<WorkOrder> filteredOrders = new ArrayList<WorkOrder>();
            if (data.get(keyStr).size() != 0) {
                for (WorkOrder workOrder : data.get(keyStr)) {
                    filteredOrders.add(workOrder);
                }
                if (!ListUtil.isEmpty(filteredOrders)) {
                    groupTitles.add(keyStr);
                    searchResult.add(filteredOrders);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }
}
