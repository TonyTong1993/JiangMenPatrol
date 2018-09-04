package com.ecity.cswatersupply.workorder.view;

import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.WorkOrderSearchActivity;
import com.ecity.cswatersupply.workorder.adpter.WorkOrderFilterAdapter;
import com.ecity.cswatersupply.workorder.model.WorkOrderGroupEnum;
import com.ecity.cswatersupply.xg.util.NotificationUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

/**
 * 工单一级界面
 */
public class WorkOrderFilterActivity extends BaseActivity {
    private WorkOrderFilterAdapter mAdapter;
    private CustomTitleView viewTitle;
    private ListView mListView;
    private Map<String, String> categoryAmount;
    private String nextStepId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtil.register(this);
        setContentView(R.layout.activity_workorder_filter_new);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nextStepId == null) {
            WorkOrderService.instance.getWorkOrderCategoryAmount();
        } else {
            WorkOrderService.instance.getWorkOrderCategoryFilter(nextStepId);
        }
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    private void initTitle() {
        viewTitle = (CustomTitleView) findViewById(R.id.customTitleView1);
        viewTitle.setTitleText(ResourceUtil.getStringById(R.string.WorkOrder_title));
        viewTitle.setBtnStyle(CustomTitleView.BtnStyle.ONLY_BACK);
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.expandableListView1);
        mAdapter = new WorkOrderFilterAdapter(this, categoryAmount);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void initData() {
        nextStepId = getIntent().getStringExtra(Constants.INTENT_KEY_NEXT_STEP_ID);
    }

    private void bindEvents() {
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotificationUtil.clearNotificationById(WorkOrderFilterActivity.this, NotificationUtil.SPECIAL_NOTIFICATION);
                UIHelper.startActivityWithExtra(CustomWorkOrderActivity.class, getBundleData(null, false, position));
            }
        });
        findViewById(R.id.ll_searchview).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearchWorkOrders();
            }
        });
    }

    private void gotoSearchWorkOrders() {
        if (mAdapter == null) {
            return;
        }

        //        List<WorkOrder> workOrders = getWorkOrderSearchSource(mAdapter.getChildrenList());
        Bundle bundle = new Bundle();
        bundle.putString(WorkOrderSearchActivity.INTENT_KEY_WORK_ORDERS_SEARCH_TYPE, WorkOrderSearchActivity.INTENT_KEY_WORK_ORDERS_SEARCH_TYPE);
        //        bundle.putSerializable(WorkOrderSearchActivity.INTENT_KEY_WORK_ORDERS, (Serializable) workOrders);
        UIHelper.startActivityWithExtra(WorkOrderSearchActivity.class, bundle);
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            ToastUtil.showLong(event.getMessage());
            nextStepId = null;
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.WORKORDER_DOWN_CATEGORY_AMOUNT_WORKORDER:
                handleGetCategoryAmount(event);
                break;
            case ResponseEventStatus.WORKORDER_DOWN_CATEGORY__FILTER_WORKORDER:
                handleGetCategoryName(event);
                break;
            default:
                break;
        }
    }

    private void handleGetCategoryAmount(ResponseEvent event) {
        categoryAmount = event.getData();
        initTitle();
        initView();
        bindEvents();
    }

    private void handleGetCategoryName(ResponseEvent event) {
        String category = event.getData();
        if (StringUtil.isBlank(category)) {
            ToastUtil.showShort(getString(R.string.workorder_category_not_found));
            WorkOrderService.instance.getWorkOrderCategoryAmount();
        } else {
            UIHelper.startActivityWithExtra(CustomWorkOrderActivity.class, getBundleData(category, true, -1));
            nextStepId = null;
        }
    }

    private Bundle getBundleData(String category, boolean isHandleResult, int position) {
        Bundle bundle = new Bundle();
        String orderBy = "";
        if (!isHandleResult) {//列表点击
            if (WorkOrderGroupEnum.values()[position].equals(WorkOrderGroupEnum.NOHANDLE)) {
                category = Constants.WAIT_PROCESS;
                orderBy = Constants.WAIT_PROCESS_ORDER;
            } else if (WorkOrderGroupEnum.values()[position].equals(WorkOrderGroupEnum.HANDLLING)) {
                category = Constants.EXCUTE_PEOCESS;
                orderBy = Constants.EXCUTE_PEOCESS_ORDER;
            } else if (WorkOrderGroupEnum.values()[position].equals(WorkOrderGroupEnum.COMLETE)) {
                category = Constants.FINISH_PROCESS;
                orderBy = Constants.FINISH_PROCESS_ORDER;
            }
            bundle.putInt(Constants.WORK_ORDER_GROUP_POSITION, position);
            bundle.putInt(Constants.WORK_ORDER_GROUP_TILE, WorkOrderGroupEnum.values()[position].getTitle());
        } else {
            if (!StringUtil.isBlank(SessionManager.CurrentProcessinstanceID)) {
                nextStepId = SessionManager.CurrentProcessinstanceID;
                SessionManager.CurrentProcessinstanceID = "";
            }
            if (Constants.WAIT_PROCESS.contains(category)) {
                orderBy = Constants.WAIT_PROCESS_ORDER;
            } else if (Constants.EXCUTE_PEOCESS.contains(category)) {
                orderBy = Constants.EXCUTE_PEOCESS_ORDER;
            } else if (Constants.FINISH_PROCESS.contains(category)) {
                orderBy = Constants.FINISH_PROCESS_ORDER;
            }
            bundle.putString(Constants.INTENT_KEY_GOUP_NEXT_STEP_ID, nextStepId);
        }
        bundle.putString(Constants.WORK_ORDER_GROUP_ORDER_BY, orderBy);
        bundle.putString(Constants.INTENT_KEY_NEXT_CATEGORY_NAME, category);

        return bundle;
    }

}
