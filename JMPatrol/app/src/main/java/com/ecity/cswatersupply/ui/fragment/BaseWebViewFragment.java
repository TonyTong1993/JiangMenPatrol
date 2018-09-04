package com.ecity.cswatersupply.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.widght.MyWebView;

/**
 * 基类webViewFragment,实现公用webview加载不同的网页.
 *
 * @author wangfeng
 * @date 2017/3/17
 */

public class BaseWebViewFragment extends Fragment {

	private View      view;
	private View      ll_title_bar;
	private Button    btnBack;
	private TextView  tvTitle;
	private Button    btnMore;
	private MyWebView webView;
	private String    menuName;
	private String    loadUrl;
	private String    titleType;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_webview, container, false);
		initData();
		initUI(view);
		initListener();
		return view;
	}

	private void initData() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			menuName = bundle.getString(Constants.MENUNAME);
			loadUrl = bundle.getString(Constants.URL);
			titleType = bundle.getString(Constants.TITLETYPE);
		}
	}

	private void initUI(View view) {
		webView = (MyWebView) view.findViewById(R.id.webview);
		ll_title_bar = view.findViewById(R.id.ll_title);
		btnBack = (Button) view.findViewById(R.id.btn_title_left);
		//默认返回不显示
		btnBack.setVisibility(View.GONE);
		tvTitle = (TextView) view.findViewById(R.id.tv_top_title);
		btnMore = (Button) view.findViewById(R.id.btn_title_right);

		WebChromeClient wvcc = new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				setTitleType(titleType, title);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
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
		if (titleType.equalsIgnoreCase(Constants.TYPE_NO_TITLE)) {
		} else if (titleType.equalsIgnoreCase(Constants.TYPE_MENU_TITLE)) {
			ll_title_bar.setVisibility(View.VISIBLE);
			tvTitle.setText(menuName);
		} else if (titleType.equalsIgnoreCase(Constants.TYPE_WEB_TITLE)) {
			ll_title_bar.setVisibility(View.VISIBLE);
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
			isShowBackButton();
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
	}

	private View.OnClickListener MyOnclickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.btn_title_left:
					if (null != webView) {
						if (webView.canGoBack()) {
							webView.goBack();
						}
					}
					break;
			}
		}
	};

	public void isShowBackButton() {
		String currentUrl = webView.getUrl();
		if (!StringUtil.isEmpty(currentUrl)) {
			if (currentUrl.equals(loadUrl)) {//第一个网页
				//返回按钮隐藏
				btnBack.setVisibility(View.GONE);
			} else {
				//返回按钮显示
				btnBack.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 重新加载url
	 */
	public void reLoadUrl(){
		if(null!=webView){
			webView.loadUrl(webView.getUrl());
		}
	}
}

