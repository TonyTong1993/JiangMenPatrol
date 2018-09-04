package com.ecity.cswatersupply.project.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.project.adapter.WaterMeterListInfoTabAdapter;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.viewpager.SViewPager;

/**
 * Created by Administrator on 2017/3/31.
 */

public class WaterMeterListActivity extends FragmentActivity {

    private IndicatorViewPager mIndicatorViewPager;
    private SViewPager viewPager;
    private CustomTitleView title;
    private Indicator indicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workorder_detail_fragment);
        initView();
    }

    private void initView() {
        String type = getIntent().getStringExtra(ProjectWaterMeterSummaryListActivity.INTENT_KEY_TYPE);
        String stratTime = getIntent().getStringExtra(ProjectWaterMeterSummaryListActivity.INTENT_KEY_START_TIME);
        String endTime = getIntent().getStringExtra(ProjectWaterMeterSummaryListActivity.INTENT_KEY_END_TIME);
        int position = getIntent().getIntExtra(ProjectWaterMeterSummaryListActivity.INTENT_KEY_STATE, -1);

        ImageView iv_detail_gps = (ImageView) findViewById(R.id.iv_detail_gps);
        iv_detail_gps.setVisibility(View.GONE);

        title = (CustomTitleView) findViewById(R.id.customTitleView1);
        title.setBtnStyle(CustomTitleView.BtnStyle.RIGHT_ACTION);
        title.setTitleText("项目列表");

        viewPager = (SViewPager) findViewById(R.id.tabmain_viewPager);
        viewPager.setCanScroll(true);
        indicator = (Indicator) findViewById(R.id.tabmain_indicator);
        mIndicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        mIndicatorViewPager.setAdapter(new WaterMeterListInfoTabAdapter(this, getSupportFragmentManager(), type, stratTime, endTime));
        viewPager.setOffscreenPageLimit(mIndicatorViewPager.getViewPager().getAdapter().getCount());
        if (position != -1) {
            viewPager.setCurrentItem(position);
        }
    }
}
