package com.ecity.cswatersupply.emergency.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.adapter.ReportQueryTabAdapter;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.viewpager.SViewPager;

/**
 * Created by Administrator on 2017/7/13.
 */

public class ReportQueryActivity extends FragmentActivity {

    private IndicatorViewPager mIndicatorViewPager;
    private SViewPager viewPager;
    private CustomTitleView title;
    private Indicator indicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workorder_detail_fragment);
        initData();
        initView();
    }

    public void onBackButtonClicked(View view){
        this.finish();
    }

    private void initData() {
    }

    private void initView() {
        ImageView iv_detail_gps = (ImageView) findViewById(R.id.iv_detail_gps);
        iv_detail_gps.setVisibility(View.GONE);

        title = (CustomTitleView) findViewById(R.id.customTitleView1);
        title.setBtnStyle(CustomTitleView.BtnStyle.RIGHT_ACTION);
        title.setTitleText(R.string.title_report_query);

        viewPager = (SViewPager) findViewById(R.id.tabmain_viewPager);

        viewPager.setCanScroll(true);
        indicator = (Indicator) findViewById(R.id.tabmain_indicator);
        mIndicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        mIndicatorViewPager.setAdapter(new ReportQueryTabAdapter(this, getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(mIndicatorViewPager.getViewPager().getAdapter().getCount());
    }
}
