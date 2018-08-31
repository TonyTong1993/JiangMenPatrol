package com.ecity.android.httpforandroid.http;

import java.util.Map;

public class BaseTaskParameters extends HttpBaseServiceParameters {

	public BaseTaskParameters(String url) {
		this.url = url;
	}

	public BaseTaskParameters(String url, Map<String, String> paramters) {
		this.url = url;
		this.requestParams = paramters;
	}

	@Override
	public Map<String, String> generateRequestParams() {
		return this.requestParams;
	}

	@Override
	public boolean validate() {
		return true;
	}
}
