package com.ecity.cswatersupply.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.WorkOrderDetailTabModel;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.workorder.WorkOrderLogBackActivity;
import com.ecity.cswatersupply.workorder.adpter.WorkOrderDetailTabAdapter1;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.viewpager.SViewPager;

import java.util.ArrayList;
import java.util.List;

/****
 * 工单详情FragmentActivity
 * Gxx 2017-04-07
 */
public class WorkOrderDetailFragmentActivity1 extends FragmentActivity {
    private String processInstanceId;
    private List<WorkOrderDetailTabModel> tabs;
    private IndicatorViewPager mIndicatorViewPager;
    private WorkOrderDetailTabModel currentWorkOrderLogInfoTab;
    private WorkOrder mWorkOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(R.style.Theme_TabPageIndicatorDefaults);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_workorder_detail_fragment);
        HostApplication.getApplication().getAppManager().addActivity(this);
        EventBusUtil.register(this);
        initData();
        initUI();
        requestWorkOrderDetail();
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    public void onBackButtonClicked(View v) {
        finish();
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    private void initUI() {
        initTitle();
    }

    private void initTitle() {
        CustomTitleView title = (CustomTitleView) findViewById(R.id.customTitleView1);
        title.setBtnStyle(BtnStyle.RIGHT_ACTION);
        title.setTitleText(R.string.workorder_detail_title);
        title.setRightActionBtnText(R.string.log_date_back);
    }

    private void setTabPageIndicator() {
        SViewPager viewPager = (SViewPager) findViewById(R.id.tabmain_viewPager);
        viewPager.setCanScroll(true);
        Indicator indicator = (Indicator) findViewById(R.id.tabmain_indicator);
        mIndicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        mIndicatorViewPager.setAdapter(new WorkOrderDetailTabAdapter1(this, tabs, getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(mIndicatorViewPager.getViewPager().getAdapter().getCount());
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        mWorkOrder = (WorkOrder) bundle.getSerializable(WorkOrder.KEY_SERIAL);
        processInstanceId = mWorkOrder.getAttribute(WorkOrder.KEY_ID);
    }

    public void onActionButtonClicked(View v) {
        Intent intent = new Intent(HostApplication.getApplication().getApplicationContext(), WorkOrderLogBackActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("currentWorkOrderLogInfoTab", currentWorkOrderLogInfoTab);
        bundle.putString("processInstanceId", processInstanceId);
        intent.putExtras(bundle);
        HostApplication.getApplication().getAppManager().currentActivity().startActivity(intent);
    }

    private void requestWorkOrderDetail() {
        LoadingDialogUtil.show(this, R.string.workorder_getting_detail);
        WorkOrderService.instance.getWorkOrderDetailTab(processInstanceId);
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.WORKORDER_GET_DETAIL_TAB:
                handleWorkOrderDetailTabs(event);
                break;
            default:
                break;
        }
    }

    private void handleWorkOrderDetailTabs(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        if (event.getStatus() == ResponseEventStatus.ERROR) {
            ToastUtil.showShort(event.getMessage());
            return;
        }
        tabs = event.getData();
        refreshTabs();
    }

    private void refreshTabs() {
        List<WorkOrderDetailTabModel> temps = new ArrayList<>();
        for (WorkOrderDetailTabModel tab : tabs) {
            if (tab.getName().equals(ResourceUtil.getStringById(R.string.workorder_detail_tab_log))) {
                currentWorkOrderLogInfoTab = tab;
                temps.add(tab);
            }
        }
        tabs.removeAll(temps);
        temps.clear();

        setTabPageIndicator();
    }
}
