package com.ecity.cswatersupply.emergency.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.adapter.NewMsgAdapter;
import com.ecity.cswatersupply.emergency.model.NewAnnountFragmentInfo;
import com.ecity.cswatersupply.emergency.model.NewsAnnounFragmentType;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.ResultCode;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.viewpagerindicator.TabPageIndicator;

public class NewsAnnounActivity extends FragmentActivity {
    public static final String EXTRA_TITLE = "extra_title";
    private TabPageIndicator mIndicator;
    private ViewPager mViewPager;
    private CustomTitleView customTitleView;
    private NewMsgAdapter mAdapter;
    private List<NewAnnountFragmentInfo> fragmentInfos;
    private TextView tv_action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newannoun);
        EventBusUtil.register(this);
        initFragmentInfo();
        initTitleView();
        initUI();
    }

    @Override
    protected void onResume() {
//        tv_action.setTextColor(getResources().getColor(R.color.white));
        super.onResume();
    }

    private void initUI() {
        customTitleView = (CustomTitleView) findViewById(R.id.customTitleView);
        customTitleView.setTitleText(getString(R.string.news_announcement));
        customTitleView.setBtnStyle(CustomTitleView.BtnStyle.ONLY_BACK);
        mIndicator = (TabPageIndicator) findViewById(R.id.tpg_indicator);
        mIndicator.setMaxVisiableItemNum(2);
        mViewPager = (ViewPager) findViewById(R.id.vpg_pager);
        mAdapter = new NewMsgAdapter(getSupportFragmentManager(), fragmentInfos);
        mViewPager.setAdapter(mAdapter);
        if (getIntent() == null) {
            return;
        }
        mIndicator.setViewPager(mViewPager, 0);

        if (getIntent().getExtras()!=null){
            mViewPager.setCurrentItem(1);
        }
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

    private void initFragmentInfo() {
        fragmentInfos = new ArrayList<NewAnnountFragmentInfo>();
        NewAnnountFragmentInfo emgcPlan = new NewAnnountFragmentInfo(getString(R.string.announcement), NewsAnnounFragmentType.ANNOUNCEMENT);
        NewAnnountFragmentInfo emgcSuggest = new NewAnnountFragmentInfo(getString(R.string.earthquake), NewsAnnounFragmentType.EARTHQUAKE);
        NewAnnountFragmentInfo eqKnowledge = new NewAnnountFragmentInfo(getString(R.string.expert_opinion), NewsAnnounFragmentType.EXPERTOPINION);
        NewAnnountFragmentInfo taskPlanInfo = new NewAnnountFragmentInfo(getString(R.string.announcement_message), NewsAnnounFragmentType.TASKPLAN);
        if (HostApplication.getApplication().getProjectStyle()== HostApplication.ProjectStyle.PROJECT_WHDZ){
            //武汉地震
            fragmentInfos.add(emgcSuggest);
            fragmentInfos.add(taskPlanInfo);
        } else {
            //其他地震相关的项目
            fragmentInfos.add(emgcPlan);
            fragmentInfos.add(emgcSuggest);
        }
//        fragmentInfos.add(eqKnowledge);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case ResultCode.EARTH_QUAKE_INFO_OK:
                UIEvent uiEvent = new UIEvent(UIEventStatus.EMERGENCY_ANNONOUN_SEARCH,data);
                EventBusUtil.post(uiEvent);

                break;
            case ResultCode.EARTH_QUAKE_INFO_SEARCH_CANCEL:
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }
    }
}
