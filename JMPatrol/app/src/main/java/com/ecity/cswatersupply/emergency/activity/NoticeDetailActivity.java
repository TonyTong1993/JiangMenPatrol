package com.ecity.cswatersupply.emergency.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.fragment.AnnouncementFragment;
import com.ecity.cswatersupply.emergency.service.EmergencyService;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.project.service.ProjectService;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/4/18.
 */

public class NoticeDetailActivity extends BaseActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_web);
        EventBusUtil.register(this);
        initView();
        requestNoticeContent();
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    public void onBackButtonClicked(View view) {
        this.finish();
    }

    private void initView() {
        CustomTitleView title = (CustomTitleView) findViewById(R.id.view_title_web);
        title.setTitleText("公告详情");

        ProgressBar progress = (ProgressBar) findViewById(R.id.h_progress);
        progress.setVisibility(View.GONE);
        webView = (WebView) findViewById(R.id.detail_web);
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setTextSize(WebSettings.TextSize.LARGEST);
    }

    private void requestNoticeContent() {
        String gid = getIntent().getStringExtra(AnnouncementFragment.INTENT_KEY_GID);
        EmergencyService.getInstance().getNoticeContent(gid);
    }

    private void handleGetNoticeContent(ResponseEvent event) {
        JSONObject data = event.getData();
        String url = data.optString("returnDic");
        webView.loadUrl(url);
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.EMERGENCY_GET_NOTICE_Content:
                LoadingDialogUtil.dismiss();
                handleGetNoticeContent(event);
                break;

            default:
                break;
        }
    }
}
