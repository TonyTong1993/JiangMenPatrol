package com.ecity.cswatersupply.emergency.activity;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class DetailWebActivity extends Activity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_web);
        Intent intent = getIntent();
        initUI(intent);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initUI(Intent intent) {
        CustomTitleView viewTitle = (CustomTitleView) findViewById(R.id.view_title_web);
        viewTitle.setTitleText("内容详情");
        viewTitle.setBtnStyle(BtnStyle.ONLY_BACK);
        progressBar = (ProgressBar) findViewById(R.id.h_progress);
        WebView webView = (WebView) findViewById(R.id.detail_web);
        WebSettings webSet = webView.getSettings();
        //支持javascript
        webSet.setJavaScriptEnabled(true);
        // 设置可以支持缩放 
        webSet.setSupportZoom(true);
        // 设置出现缩放工具 
        webSet.setBuiltInZoomControls(true);
        //扩大比例的缩放
        webSet.setUseWideViewPort(true);
        //自适应屏幕
        webSet.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        webSet.setLoadWithOverviewMode(true);
        String url = intent.getStringExtra("url");
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView wv, int newProgress) {
                super.onProgressChanged(wv, newProgress);
                if (null != progressBar) {
                    if (newProgress == 100) {
                        progressBar.setProgress(100);
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);

                            }
                        }, 500);
                    } else {
                        if (progressBar.getVisibility() == View.GONE) {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                        progressBar.setProgress(newProgress);
                    }
                }

            }
        });
        webView.loadUrl("http://www.csi.ac.cn/" + url);

    }

    public void onBackButtonClicked(View v) {
        finish();
    }
}
