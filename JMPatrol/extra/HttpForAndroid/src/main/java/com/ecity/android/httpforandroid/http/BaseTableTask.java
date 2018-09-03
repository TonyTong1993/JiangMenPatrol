package com.ecity.android.httpforandroid.http;

import java.net.SocketTimeoutException;

import org.json.JSONObject;
public class BaseTableTask extends HttpBaseService<JSONObject> {

	JSONObject jsonObject;

	public BaseTableTask(HttpBaseServiceParameters paramTaskParameters) {
		super(paramTaskParameters);
	}

	public BaseTableTask(HttpBaseServiceParameters paramTaskParameters,
			HttpBaseServiceListener<JSONObject> paramTaskListener) {
		super(paramTaskParameters, paramTaskListener);
	}

	@Override
	public JSONObject execute() {
		try {
			String jsonstr = HttpRequestJsonParse.executeFromMapToStringNew(this.actionInput.getUrl(), this.actionInput.generateRequestParams(), this.actionInput.getRequestType());
			jsonObject = new JSONObject(jsonstr);
		} catch (Exception e) { 
			e.printStackTrace();
			if(e.getMessage().toLowerCase().contains("failed to connect to") 
					|| e.getMessage().toLowerCase().contains("refused")
					|| e.getMessage().toLowerCase().contains("timed out") 
					|| e instanceof SocketTimeoutException)
			{
			}
		}

		return jsonObject;
	}
}
