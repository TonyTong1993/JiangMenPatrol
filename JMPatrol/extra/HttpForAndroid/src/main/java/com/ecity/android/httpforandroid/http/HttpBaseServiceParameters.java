package com.ecity.android.httpforandroid.http;

import java.util.Map;
/***
 * 
 * @author ZiZhengzhuan
 *
 */
public abstract class HttpBaseServiceParameters {
	protected String url;
	private HttpRequestType requestType = HttpRequestType.AUTO;
	
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/***
	 * 网络请求类型 
	 * @return GET、POST、AUTO
	 */
	public HttpRequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(HttpRequestType requestType) {
		this.requestType = requestType;
	}
	
	protected Map<String, String> requestParams;

	public Map<String, String> getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(Map<String, String> requestParams) {
		this.requestParams = requestParams;
	}

	public abstract Map<String, String> generateRequestParams() throws Exception;

	public abstract boolean validate();

	@Override
	public int hashCode() {
		int j = 1;
		j = 31 * j + (this.url == null ? 0 : this.url.hashCode());
		return j;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		HttpBaseServiceParameters localTaskParameters = (HttpBaseServiceParameters) obj;
		if (this.url == null) {
			if (localTaskParameters.url != null) {
				return false;
			}
		} else if (!this.url.equals(localTaskParameters.url)) {
			return false;
		}
		return true;
	}
}
