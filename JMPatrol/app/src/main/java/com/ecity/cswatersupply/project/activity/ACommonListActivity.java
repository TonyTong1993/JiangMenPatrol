package com.ecity.cswatersupply.project.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.project.activity.fragment.ACommonListFragment;
import com.ecity.cswatersupply.project.adapter.ProjectListInfoTabAdapter;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.viewpager.SViewPager;

public class ACommonListActivity extends FragmentActivity {

    public static final String INTENT_KEY_FRAGMENT_CLASS_NAME = "INTENT_KEY_CLASS_NAME";

    private IndicatorViewPager mIndicatorViewPager;
    private SViewPager viewPager;
    private CustomTitleView title;
    private Indicator indicator;
    private Class<ACommonListFragment> fragmentClass;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_workorder_detail_fragment);
        initDataSource();
        initView();
    }

    public void onBackButtonClicked(View view) {
        this.finish();
    }

    @SuppressWarnings("unchecked")
    private void initDataSource() {
        String className = getIntent().getStringExtra(INTENT_KEY_FRAGMENT_CLASS_NAME);
        try {
            fragmentClass = (Class<ACommonListFragment>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            LogUtil.e(this, e);
        }
    }

    private void initView() {
        ImageView iv_detail_gps = (ImageView) findViewById(R.id.iv_detail_gps);
        iv_detail_gps.setVisibility(View.GONE);

        title = (CustomTitleView) findViewById(R.id.customTitleView1);
        title.setBtnStyle(BtnStyle.RIGHT_ACTION);
        title.setTitleText(R.string.project_prospective_design);

        viewPager = (SViewPager) findViewById(R.id.tabmain_viewPager);

        viewPager.setCanScroll(true);
        indicator = (Indicator) findViewById(R.id.tabmain_indicator);
        mIndicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        mIndicatorViewPager.setAdapter(new ProjectListInfoTabAdapter(this, getSupportFragmentManager(), fragmentClass));
        viewPager.setOffscreenPageLimit(mIndicatorViewPager.getViewPager().getAdapter().getCount());
    }
}
