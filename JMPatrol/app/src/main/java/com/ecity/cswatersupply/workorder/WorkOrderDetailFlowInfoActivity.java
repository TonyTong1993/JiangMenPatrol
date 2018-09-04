package com.ecity.cswatersupply.workorder;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

/***
 * 流程信息详情
 * 
 * @author jiangqiwei
 * 
 * */
public class WorkOrderDetailFlowInfoActivity extends BaseActivity {
    private CustomTitleView titleView;
    private List<InspectItem> flowDetailInfo;
    private LinearLayout mLlContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workorder_detail_flow_info);
        EventBusUtil.register(this);
        flowDetailInfo = new ArrayList<InspectItem>();
        initUI();
        initData();
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    @SuppressLint("NewApi")
    private void initUI() {
        titleView = (CustomTitleView) findViewById(R.id.view_tile_flow_info_detail);
        titleView.setTitleText(R.string.title_flow_info_detail);
        titleView.setBtnStyle(CustomTitleView.BtnStyle.ONLY_BACK);
        mLlContainer = (LinearLayout) findViewById(R.id.ll_container);
    }

    public void initData() {
        String eventType = null;
        String processinstanceid = null;
        try {
            eventType = getIntent().getExtras().getString("EVENTTYPE");
            processinstanceid = getIntent().getExtras().getString("PROCESSINSTANCEID");

        } catch (NumberFormatException e) {
            LogUtil.e(this, e);
        }
        WorkOrderService.instance.getFlowInfoDetails(processinstanceid, eventType);
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.WORKORDER_DETAIL_FLOW_INFO:
                handleWorkOrderFlowInfoDetails(event);
                break;
            default:
                break;
        }
    }

    private void handleWorkOrderFlowInfoDetails(ResponseEvent event) {
        flowDetailInfo = event.getData();

        if (ListUtil.isEmpty(flowDetailInfo)) {
            return;
        }

        CustomViewInflater customViewInflater = new CustomViewInflater(this);
        for (InspectItem item : flowDetailInfo) {
            mLlContainer.addView(customViewInflater.inflate(item));
        }
    }

}
