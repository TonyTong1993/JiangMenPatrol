package com.ecity.cswatersupply.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.project.adapter.SafeManageDetailAdapter;
import com.ecity.cswatersupply.project.network.response.SafeDetailStepModel;
import com.ecity.cswatersupply.project.network.response.SafeEventListModel;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.viewpager.SViewPager;

import java.util.List;

/**
 * Created by Administrator on 2017/5/25.
 */

public class SafeManageDetailActivity extends FragmentActivity {

    private SViewPager viewPager;
    private Indicator indicator;
    private IndicatorViewPager mIndicatorViewPager;
    private SafeEventListModel model;
    private List<SafeDetailStepModel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workorder_detail_fragment);
        EventBusUtil.register(this);
        initDataSource();
        initView();
        setOnClickListener();
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    public void onBackButtonClicked(View view){
        this.finish();
    }

    private void initDataSource() {
        model = (SafeEventListModel) getIntent().getSerializableExtra(SafeManageEventListActivity.SELECT_EVENT);
        data = (List<SafeDetailStepModel>) getIntent().getSerializableExtra(SafeManageEventListActivity.SELECT_DATA);
    }

    private void initView() {
        CustomTitleView customTitleView1 = (CustomTitleView) findViewById(R.id.customTitleView1);
        customTitleView1.setTitleText("安全问题详情");

        ImageView iv_detail_gps = (ImageView) findViewById(R.id.iv_detail_gps);
        iv_detail_gps.setVisibility(View.GONE);

        viewPager = (SViewPager) findViewById(R.id.tabmain_viewPager);
        viewPager.setCanScroll(true);
        indicator = (Indicator) findViewById(R.id.tabmain_indicator);
        mIndicatorViewPager = new IndicatorViewPager(indicator, viewPager);

        SafeManageDetailAdapter adapter = new SafeManageDetailAdapter(this, getSupportFragmentManager(), data);
        mIndicatorViewPager.setAdapter(adapter);
        viewPager.setCanScroll(true);
        viewPager.setOffscreenPageLimit(adapter.getCount());
    }

    private void setOnClickListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (CustomViewInflater.pendingViewInflater != null) {
            CustomViewInflater.pendingViewInflater.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.PROJECT_SAFE_MANAGE_GET_DETAIL_TAB:
                LoadingDialogUtil.dismiss();
//                handleGetSafeDetailTabResponse(event);
                break;
            default:
                break;
        }
    }

    public SafeEventListModel getModel() {
        return model;
    }
}

