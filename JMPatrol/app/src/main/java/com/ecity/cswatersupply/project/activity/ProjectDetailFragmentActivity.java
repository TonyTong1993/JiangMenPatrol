package com.ecity.cswatersupply.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.project.activity.fragment.AProjectCommonInspectItemFragment;
import com.ecity.cswatersupply.project.activity.fragment.ProspectiveDesignListFragment;
import com.ecity.cswatersupply.project.adapter.ProjectDetailInfoTabAdapter;
import com.ecity.cswatersupply.project.model.ProjectNotificationMap;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.xg.model.Notification;
import com.ecity.cswatersupply.xg.model.NotificationType;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.viewpager.SViewPager;

public class ProjectDetailFragmentActivity extends FragmentActivity {
    public static final String INTENT_KEY_PROJECT_NOTIFICATION = "INTENT_KEY_PROJECT_NOTIFICATION";
    public static final String INTENT_KEY_TITLE_NAME = "INTENT_KEY_TITLE_NAME";
    public static final String INTENT_KEY_PROJECT_ID = "INTENT_KEY_PROJECT_ID";
    public static final String INTENT_KEY_PROJECT_TYPE = "INTENT_KEY_PROJECT_TYPE";
    public static final String INTENT_KEY_RECORD_ID = "INTENT_KEY_RECORD_ID";
    public static final String INTENT_KEY_FRAGMENT_CLASS_NAME = "INTENT_KEY_CLASS_NAME";
    public static final String INTENT_KEY_TAB_TITLES = "INTENT_KEY_TAB_TITLES";
    public static final String INTENT_KEY_LOG_TYPE = "INTENT_KEY_LOG_TYPE";
    public static final String INTENT_KEY_IS_FROM_PROJECT_SCREEN = "INTENT_KEY_IS_FROM_PROJECT_SCREEN";

    private IndicatorViewPager mIndicatorViewPager;
    private String projectId;
    private String proType;
    private String recordId;
    private Class<AProjectCommonInspectItemFragment> fragmentClass;
    private String[] tabTitles;
    private SViewPager viewPager;
    private CustomTitleView title;
    private Indicator indicator;
    private String logType;
    private String titleName;
    private String state;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_workorder_detail_fragment);
        initDataSource();
        initView();
        setOnClickListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (CustomViewInflater.pendingViewInflater != null) {
            CustomViewInflater.pendingViewInflater.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    @SuppressWarnings("unchecked")
    private void initDataSource() {
        Notification notification = (Notification) getIntent().getSerializableExtra(INTENT_KEY_PROJECT_NOTIFICATION);
        if (notification != null) {
            NotificationType type = notification.getType();
            ProjectNotificationMap.getFragmetName(String.valueOf(type));
        }

        projectId = getIntent().getStringExtra(INTENT_KEY_PROJECT_ID);
        proType = getIntent().getStringExtra(INTENT_KEY_PROJECT_TYPE);
        recordId = getIntent().getStringExtra(INTENT_KEY_RECORD_ID);
        tabTitles = getIntent().getStringArrayExtra(INTENT_KEY_TAB_TITLES);
        logType = getIntent().getStringExtra(INTENT_KEY_LOG_TYPE);
        titleName = getIntent().getStringExtra(INTENT_KEY_TITLE_NAME);
        state = getIntent().getStringExtra(ProspectiveDesignListFragment.INTENT_KEY_PROJECT_STATE);

        if (notification != null) {
            projectId = String.valueOf(notification.getId());
            recordId = notification.getNextStepIds();
        }
        String className = getIntent().getStringExtra(INTENT_KEY_FRAGMENT_CLASS_NAME);
        if (className == null) {
            className = ProjectNotificationMap.fragmentName;
        }
        if (titleName == null) {
            titleName = ProjectNotificationMap.titleName;
        }
        if (tabTitles == null) {
            tabTitles = ProjectNotificationMap.tabTitles;
        }
        if (logType == null) {
            logType = ProjectNotificationMap.logType;
        }
        try {
            fragmentClass = (Class<AProjectCommonInspectItemFragment>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            LogUtil.e(this, e);
            throw new RuntimeException(e); // 出现这个异常必然是程序错误，直接退出
        }
    }

    private void initView() {
        ImageView iv_detail_gps = (ImageView) findViewById(R.id.iv_detail_gps);
        iv_detail_gps.setVisibility(View.GONE);

        if (tabTitles.length > 1) {
            showMultiTabs();
        } else {
            showOneTab();
        }
        title = (CustomTitleView) findViewById(R.id.customTitleView1);
        if (logType != null&&!logType.isEmpty()) {
            title.setBtnStyle(BtnStyle.RIGHT_ACTION);
        } else {
            title.setBtnStyle(BtnStyle.ONLY_BACK);
        }
        if (titleName.isEmpty()){
            titleName = "基本信息";
        }
        title.setTitleText(titleName);
        title.setRightActionBtnText(R.string.project_log_back);
    }

    @SuppressWarnings("deprecation")
    private void setOnClickListener() {
        title.setMessageDeleteListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString(LogBackActivity.INTENT_KEY_LOG_BACK_TYPE, logType);
                data.putString(LogBackActivity.INTENT_KEY_LOG_BACK_TITLE, "查看日志");
                if (logType.equals("sy")||logType.equals("js")||logType.equals("jg")){//由于服务的问题，这里试压试验和接水做单独处理传值
                    data.putString(LogBackActivity.INTENT_KEY_LOG_BACK_PROJECTID, recordId);
                }else{
                    data.putString(LogBackActivity.INTENT_KEY_LOG_BACK_PROJECTID, projectId);
                }
                UIHelper.startActivityWithExtra(LogBackActivity.class, data);
            }
        });

        if (viewPager == null) {
            return;
        }

        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                indicator.setCurrentItem(position, true);
                title.setTitleText(tabTitles[position]);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                indicator.onPageScrolled(arg0, arg1, arg2);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

    }

    private void showMultiTabs() {
        viewPager = (SViewPager) findViewById(R.id.tabmain_viewPager);
        viewPager.setCanScroll(true);
        indicator = (Indicator) findViewById(R.id.tabmain_indicator);
        mIndicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        ProjectDetailInfoTabAdapter adapter = new ProjectDetailInfoTabAdapter(this, getSupportFragmentManager(), tabTitles, fragmentClass, projectId, recordId, proType);
        mIndicatorViewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(mIndicatorViewPager.getViewPager().getAdapter().getCount());
        if (state != null) {
            switch (state) {
                case "1"://已委托
                case "2"://技术部查看
                case "3"://技术部退回
                    viewPager.setCurrentItem(1);
                    break;
                case "4"://延期申请
                case "5"://延期通过
                case "6"://延期驳回
                    viewPager.setCurrentItem(2);
                    break;
                case "7"://提交设计
                case "8"://设计通过
                case "9"://设计驳回
                    viewPager.setCurrentItem(3);
                    break;
            }
        }
    }

    private void showOneTab() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        AProjectCommonInspectItemFragment fragment;
        try {
            fragment = fragmentClass.newInstance();
        } catch (InstantiationException e) {
            LogUtil.e(this, e);
            throw new RuntimeException(e); // 出现这个异常必然是代码问题,直接退出.
        } catch (IllegalAccessException e) {
            LogUtil.e(this, e);
            throw new RuntimeException(e); // 出现这个异常必然是代码问题,直接退出.
        }

        fragment.setProjectId(projectId);
        fragment.setPosition(0);
        fragment.setRecordId(recordId);
        transaction.replace(R.id.view_detail_page, fragment);
        transaction.commit();
    }
}
