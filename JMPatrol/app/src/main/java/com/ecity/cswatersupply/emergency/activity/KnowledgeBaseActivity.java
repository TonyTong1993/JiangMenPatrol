package com.ecity.cswatersupply.emergency.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.adapter.KnowledgeAdapter;
import com.ecity.cswatersupply.emergency.model.DownloadFragmentInfo;
import com.ecity.cswatersupply.emergency.model.EDownloadFragmentType;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.viewpagerindicator.TabPageIndicator;

public class KnowledgeBaseActivity extends FragmentActivity {
    public static final String EXTRA_TITLE = "extra_title";
    private TabPageIndicator mIndicator;
    private ViewPager mViewPager;
    private CustomTitleView customTitleView;
    private KnowledgeAdapter mAdapter;
    private List<DownloadFragmentInfo> fragmentInfos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);
        initFragmentInfo();
        initUI();
        initTitleView();
    }

    private void initUI() {
        customTitleView = (CustomTitleView) findViewById(R.id.customTitleView);
        customTitleView.setTitleText(getString(R.string.emergency_knowledge));
        mIndicator = (TabPageIndicator) findViewById(R.id.tpg_indicator);
        mIndicator.setMaxVisiableItemNum(3);
        mViewPager = (ViewPager) findViewById(R.id.vpg_pager);
        mAdapter = new KnowledgeAdapter(getSupportFragmentManager(),fragmentInfos);
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager, 0);
    }

    private void initTitleView() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }
        if (bundle.containsKey(EXTRA_TITLE)) {
            customTitleView.setTitleText(bundle.getString(EXTRA_TITLE));
        }
    }
    
    private void initFragmentInfo(){
        fragmentInfos = new ArrayList<DownloadFragmentInfo>();
        DownloadFragmentInfo emgcPlan = new DownloadFragmentInfo(getString(R.string.emergency_knowledge_plan), EDownloadFragmentType.EMERGENCY_PLAN);
        DownloadFragmentInfo emgcSuggest = new DownloadFragmentInfo(getString(R.string.emergency_knowledge_suggest), EDownloadFragmentType.EMERGENCY_SUGGEST);
        DownloadFragmentInfo eqKnowledge = new DownloadFragmentInfo(getString(R.string.emergency_earthquake_knowledge), EDownloadFragmentType.EARTHQUAKE_KNOWLEDGE);
        
        fragmentInfos.add(emgcPlan);
        fragmentInfos.add(emgcSuggest);
        fragmentInfos.add(eqKnowledge);
    }

    @Override
    public void setTitle(CharSequence title) {

        super.setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {

        super.setTitle(titleId);
    }

    public void onBackButtonClicked(View v) {
        finish();
    }

}
