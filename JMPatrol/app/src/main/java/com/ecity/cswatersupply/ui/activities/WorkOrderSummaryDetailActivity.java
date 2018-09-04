package com.ecity.cswatersupply.ui.activities;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.WorkOrderSummaryDetailAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.SummaryDetailModel;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrderPieStaticsData;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

/**
 * 工单汇总详情信息Activity
 * @author qw
 */
public class WorkOrderSummaryDetailActivity extends BaseActivity {

    private List<SummaryDetailModel> detailDatas = new ArrayList<SummaryDetailModel>();
    private CustomTitleView title_view;
    protected ListView mListView;
    private WorkOrderSummaryDetailAdapter adapter;
    private WorkOrderPieStaticsData pieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_details);
        EventBusUtil.register(this);
        initTitleView();
        initView();
        requestListSourceDatas();
    }

    private void initTitleView() {
        title_view = (CustomTitleView) findViewById(R.id.title_view);
        title_view.setBtnStyle(BtnStyle.ONLY_BACK);
        title_view.setTitleText(getFormatTitle());
    }

    private String getFormatTitle() {
        String title = "";
        if (getIntent().getSerializableExtra(Constants.WORKORDER_SUMMARY_DETAIL) != null) {
            setPieData((WorkOrderPieStaticsData) getIntent().getSerializableExtra(Constants.WORKORDER_SUMMARY_DETAIL));
            String years = pieData.getEndDate().split("-")[0];
            String months = pieData.getEndDate().split("-")[1];
            String formatYear = years + getResources().getString(R.string.date_year_str);
            title = pieData.isYearType() ? formatYear : formatYear + months + getResources().getString(R.string.date_month_str);
            title = title + getResources().getString(R.string.menu_summary_static);
        } else {
            title = ResourceUtil.getStringById(getIntent().getIntExtra(Constants.WORK_ORDER_GROUP_TILE, 0));
        }
        return title;

    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.ll_summary_details);
        mListView.setDivider(ResourceUtil.getDrawableResourceById(R.drawable.shape_list_divider));
        mListView.setDividerHeight(ResourceUtil.getDimensionPixelSizeById(R.dimen.margin_spacing_level_10));
        adapter = new WorkOrderSummaryDetailAdapter(this);
        mListView.setAdapter(adapter);
    }

    public void onBackButtonClicked(View v) {
        finish();
    }

    public void requestListSourceDatas() {
        LoadingDialogUtil.show(this, R.string.str_get_summary_detail_infos);
        WorkOrderService.instance.getSummaryDetailWorkOrders(pieData, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    public void onEventMainThread(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        if (!event.isOK()) {
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.WORKORDER_DOWN_WORKORER_SUMMARY_DETAIL_FROM_TOTAL:
                handleGetSummaryDetailInfo(event);
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private void handleGetSummaryDetailInfo(ResponseEvent event) {
        setDetailDatas((List<SummaryDetailModel>) event.getData());
        if (ListUtil.isEmpty(detailDatas) || detailDatas.size() == 0) {
            Toast.makeText(getApplicationContext(), R.string.no_summary_detail_infos, Toast.LENGTH_SHORT).show();
        }
        adapter.setList(detailDatas);
        adapter.notifyDataSetChanged();
    }

    public List<SummaryDetailModel> getDetailDatas() {
        return detailDatas;
    }

    public void setDetailDatas(List<SummaryDetailModel> detailDatas) {
        this.detailDatas = detailDatas;
    }

    public WorkOrderPieStaticsData getPieData() {
        return pieData;
    }

    public void setPieData(WorkOrderPieStaticsData pieData) {
        this.pieData = pieData;
    }

}
