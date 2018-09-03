package com.ecity.android.httpforandroid.http;

import org.codehaus.jackson.JsonParser;

public class HttpStringService extends HttpBaseService<String> {

	public HttpStringService(HttpBaseServiceParameters paramTaskParameters) {
		super(paramTaskParameters);
	}

	public HttpStringService(HttpBaseServiceParameters paramTaskParameters, HttpBaseServiceListener<String> paramTaskListener) {
		super(paramTaskParameters, paramTaskListener);
	}

	@Override
	public String execute() {
		try {
			JsonParser jsonParser = HttpRequestJsonParse
					.executeFromMap(this.actionInput.getUrl(), this.actionInput.generateRequestParams());

			return jsonParser.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
