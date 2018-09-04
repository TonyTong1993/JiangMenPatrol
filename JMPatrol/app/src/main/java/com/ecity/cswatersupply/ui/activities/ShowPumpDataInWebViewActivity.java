package com.ecity.cswatersupply.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.widght.MyWebView;

import java.util.Stack;

/***  
 * Created by MaoShouBei on 2017/5/15.
 */

public class ShowPumpDataInWebViewActivity extends Activity {

    private View ll_title_bar;
    private Button btnBack;
    private TextView tvTitle;
    private SwipeRefreshLayout swipeLayout;
    private ProgressBar horizontalProgress;
    private MyWebView webView;
    private String menuName;
    private String loadUrl;
    private String titleType;
    private Stack<String> mUrlHistory = new Stack<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_punmp_data_in_webview);
        initData();
        initUI();
        initListener();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            menuName = bundle.getString(PumpDetailActivity.WEB_VIEW_TITLE_NAME);
            loadUrl = bundle.getString(PumpDetailActivity.PUMP_URL);
            titleType = bundle.getString(PumpDetailActivity.WEB_VIEW_TITLE_TYPE);
        }
    }

    private void initUI() {
        webView = (MyWebView) findViewById(R.id.wv_show_pump_data);
        ll_title_bar = findViewById(R.id.ll_title);
        btnBack = (Button) findViewById(R.id.btn_title_left);
        tvTitle = (TextView) findViewById(R.id.tv_top_title);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        horizontalProgress = (ProgressBar) findViewById(R.id.progress_bar);

        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitleType(titleType, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (null != horizontalProgress) {
                    if (newProgress == 100) {
                        horizontalProgress.setProgress(100);
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                // 进度条消失
                                horizontalProgress.setVisibility(View.GONE);
                                // 停止刷新
                                swipeLayout.setRefreshing(false);
                            }
                        }, 500);
                    } else {
                        if (horizontalProgress.getVisibility() == View.GONE) {
                            horizontalProgress.setVisibility(View.VISIBLE);
                        }
                        horizontalProgress.setProgress(newProgress);
                    }
                }
            }
        };

        if (null != webView) {
            // 设置setWebChromeClient对象
            webView.setWebChromeClient(wvcc);
            webView.loadUrl(loadUrl);
            webView.setWebViewClient(new MyWebViewClient());
        }
    }

    private void setTitleType(String titleType, String title) {
        if (titleType.equalsIgnoreCase("type_no_title")) {
            ll_title_bar.setVisibility(View.GONE);
        } else if (titleType.equalsIgnoreCase("type_menu_title")) {
            tvTitle.setText(menuName);
        } else if (titleType.equalsIgnoreCase("type_web_title")) {
            tvTitle.setText(title);
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // false表示加载的url始终由当前webView处理，不需要过问程序的ActivityManager
            if (URLUtil.isNetworkUrl(url)) {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (mUrlHistory.size() == 0 || URLUtil.isNetworkUrl(url) && !mUrlHistory.get(mUrlHistory.size() - 1).equals(url)) {
                mUrlHistory.push(url);
            }
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // 网页错误执行 eg找不到网页
            Log.e("WebViewError", "onReceivedError");
        }

    }

    private void initListener() {
        btnBack.setOnClickListener(MyOnclickListener);

        // 设置下拉刷新距离
        swipeLayout.setDistanceToTriggerSync(250);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 重新刷新页面
                if (webView.getScrollY() == 0) {
                    webView.loadUrl(webView.getUrl());
                }
            }
        });
    }

    private View.OnClickListener MyOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_title_left:
                    if (null != webView) {
                        if (mUrlHistory.size() != 0) {
                            if (mUrlHistory.size() == 1) {
                                webView.clearCache(true);
                                finish();
                                return;
                            }
                            mUrlHistory.pop();
                            webView.loadUrl(mUrlHistory.peek());
                        } else {
                            finish();
                            webView.clearCache(true);
                        }
                    }
                    break;
            }
        }
    };
}
