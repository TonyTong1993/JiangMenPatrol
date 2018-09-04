package com.ecity.cswatersupply.workorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.adpter.WorkOrderSearchAdapter;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.view.CustomWorkOrderActivity;
import com.lee.pullrefresh.ui.PullRefreshExpandableListView;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

/**
 * 工单查询
 *
 * @author gaokai
 */
public class WorkOrderSearchActivity extends BaseActivity implements OnGroupClickListener, OnChildClickListener {
    public static final String INTENT_KEY_WORK_ORDERS = "INTENT_KEY_WORK_ORDERS";
    public static final String INTENT_KEY_WORK_ORDERS_SEARCH_TYPE = "INTENT_KEY_WORK_ORDERS_SEARCH_TYPE";
    public static final String INTENT_KEY_WORK_ORDERS_SEARCH_RESULT = "INTENT_KEY_WORK_ORDERS_SEARCH_RESULT";
    private List<WorkOrder> dataSource;
    private List<List<WorkOrder>> searchResult;
    private List<String> groupTitles;
    private TextView tvSearch;
    private Button btn_search;
    private String nextStepId;

    private ExpandableListView expandableListView;
    private PullRefreshExpandableListView mPullExpandableListView;
    private WorkOrderSearchAdapter mAdapter;
    private TextView tvSearchResultBlankTips;
    private String workOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_workorder);
        EventBusUtil.register(this);
        initView();
        initDataSource();
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
        String requestType = getIntent().getStringExtra(INTENT_KEY_WORK_ORDERS_SEARCH_TYPE);
        if (!StringUtil.isBlank(requestType)) {
            nextStepId = workOrder.getAttributes().get(WorkOrder.KEY_ID);
            workOrderId = workOrder.getAttributes().get(WorkOrder.KEY_CODE);
            SessionManager.CurrentProcessinstanceID = nextStepId;
            WorkOrderService.instance.getWorkOrderCategoryById(workOrder.getAttributes().get(WorkOrder.KEY_ID));
        } else {
            finishActivity(workOrder);
        }

        return false;
    }

    private void finishActivity(WorkOrder workOrder) {
        UIEvent event = new UIEvent(UIEventStatus.WORKORDER_SEARCH_RESULT_CLICKED, workOrder.getAttributes().get(WorkOrder.KEY_ID));
        EventBusUtil.post(event);
        finish();
    }

    private void initView() {
        CustomTitleView title = (CustomTitleView) findViewById(R.id.customTitleView1);
        title.setBtnStyle(BtnStyle.RIGHT_ACTION);
        title.setTitleText(R.string.workorder_search_title);
        tvSearch = (TextView) findViewById(R.id.tv_search);
        btn_search = (Button) findViewById(R.id.btn_search);
        tvSearchResultBlankTips = (TextView) findViewById(R.id.tv_normal_content_blank);

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

    @SuppressWarnings("unchecked")
    private void initDataSource() {
        dataSource = (List<WorkOrder>) getIntent().getSerializableExtra(INTENT_KEY_WORK_ORDERS);
    }

    private void bindEvents() {
        btn_search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String requestType = getIntent().getStringExtra(INTENT_KEY_WORK_ORDERS_SEARCH_TYPE);
                if (!StringUtil.isBlank(requestType)) {
                    searchRequestWorkOrders();
                } else {//本地搜索
                    searchLocalWorkOrders();
                }
            }
        });

        tvSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId != EditorInfo.IME_ACTION_SEARCH) && (event.getKeyCode() != KeyEvent.KEYCODE_ENTER)) {
                    return false;
                }
                searchLocalWorkOrders();

                return true;
            }
        });

        tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (tvSearch.getText().length() == 0) {
                    showSearchResultBlankView(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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

    private void filterWorkOrders(String value) {
        filterWorkOrders(WorkOrder.KEY_CODE, value, R.string.workorder_search_group_code);
        filterWorkOrders(WorkOrder.KEY_CONTENT, value, R.string.workorder_search_group_content);
        filterWorkOrders(WorkOrder.KEY_ADDRESS, value, R.string.workorder_search_group_address);
        filterWorkOrders(WorkOrder.KEY_MAIN_MAN, value, R.string.workorder_search_group_main_assignee);
        filterWorkOrders(WorkOrder.KEY_FROM_ALIAS, value, R.string.workorder_search_group_source);
    }

    private void filterWorkOrders(String propertyKey, String value, int groupTitleId) {
        List<WorkOrder> filteredOrders = new ArrayList<WorkOrder>();
        for (WorkOrder workOrder : dataSource) {
            String property = workOrder.getAttributes().get(propertyKey);
            if ((property != null) && (property.contains(value))) {
                filteredOrders.add(workOrder);
            }
        }

        if (!ListUtil.isEmpty(filteredOrders)) {
            groupTitles.add(getString(groupTitleId));
            searchResult.add(filteredOrders);
        }
    }

    private void searchRequestWorkOrders() {
        String searchStr = tvSearch.getText().toString().trim();
        if (!StringUtil.isEmpty(searchStr)) {
            WorkOrderService.instance.getWorkOrderSearchResult(searchStr);
        }
    }

    private void searchLocalWorkOrders() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tvSearch.getWindowToken(), 0);
        clearSearchResult();
        String searchStr = tvSearch.getText().toString().trim();
        if (!ListUtil.isEmpty(dataSource) && !StringUtil.isBlank(searchStr)) {
            filterWorkOrders(searchStr);
        }
        showSearchResultBlankView(isSearchResultBlank(searchResult));
        refreshListView();
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
                showSearchResultBlankView(isSearchResultBlank(searchResult));
                refreshListView();
                break;
            case ResponseEventStatus.WORKORDER_DOWN_CATEGORY_WORKORDER:
                handleGetWorkOrderCategoryData(event);
                break;
            default:
                break;
        }
    }

    private void handleGetWorkOrderCategoryData(ResponseEvent event) {
        String category = event.getData();
        String orderBy = "";
        Bundle bundle = new Bundle();
        if (Constants.WAIT_PROCESS.contains(category)) {
            orderBy = Constants.WAIT_PROCESS_ORDER;
        } else if (Constants.EXCUTE_PEOCESS.contains(category)) {
            orderBy = Constants.EXCUTE_PEOCESS_ORDER;
        } else if (Constants.FINISH_PROCESS.contains(category)) {
            orderBy = Constants.FINISH_PROCESS_ORDER;
        }
        bundle.putString(Constants.WORK_ORDER_GROUP_ORDER_BY, orderBy);
        bundle.putString(Constants.INTENT_KEY_NEXT_STEP_ID, nextStepId);
        bundle.putString(WorkOrder.KEY_CODE, workOrderId);
        bundle.putString(Constants.INTENT_KEY_NEXT_CATEGORY_NAME, category);
        UIHelper.startActivityWithExtra(CustomWorkOrderActivity.class, bundle);
        nextStepId = null;
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

    private boolean isSearchResultBlank(List<List<WorkOrder>> searchResult) {
        return (searchResult.size() == 0);
    }

    private void showSearchResultBlankView(boolean isShow) {
        if (isShow) {
            mPullExpandableListView.setVisibility(View.GONE);
            tvSearchResultBlankTips.setVisibility(View.VISIBLE);
        } else {
            mPullExpandableListView.setVisibility(View.VISIBLE);
            tvSearchResultBlankTips.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }
}
