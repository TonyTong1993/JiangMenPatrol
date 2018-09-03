package com.ecity.android.httpforandroid.http;

import java.util.Map;
/***
 * 
 * @author ZiZhengzhuan
 *
 */
public class HttpServiceParameters extends HttpBaseServiceParameters {
	private Map<String, String> paramters = null;
	public HttpServiceParameters(String url) {
		this.url = url;
	}

	public HttpServiceParameters(String url, Map<String, String> paramters) {
		this.url = url;
		this.paramters = paramters;
	}
	@Override
	public Map<String, String> generateRequestParams() {
		return paramters;
	}

	@Override
	public boolean validate() {
		return false;
	}
}
