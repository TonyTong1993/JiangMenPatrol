package com.ecity.android.httpforandroid.http;

import java.net.SocketTimeoutException;

import org.json.JSONObject;

import android.util.Log;
/***
 * 
 * @Description
 * @version V1.0 
 * @Author ZiZhengzhuan
 * @CreateDate 2016年1月15日
 * @email
 */
public class HttpTableService extends HttpBaseService<JSONObject> {
	JSONObject jsonObject;
	/***
	 * 
	 * @param paramTaskParameters
	 */
	public HttpTableService(HttpBaseServiceParameters paramTaskParameters) {
		super(paramTaskParameters);
	}
	/***
	 * 
	 * @param paramTaskParameters
	 * @param paramTaskListener
	 */
	public HttpTableService(HttpBaseServiceParameters paramTaskParameters,
			HttpBaseServiceListener<JSONObject> paramTaskListener) {
		super(paramTaskParameters, paramTaskListener);
	}

	@Override
	public JSONObject execute() {
		try {
			String json=null;
			json = HttpRequestJsonParse.executeFromMapToStringNew(this.actionInput.getUrl(), this.actionInput.generateRequestParams(),this.actionInput.getRequestType());
			jsonObject = new JSONObject(json);
		} catch (Exception e) {
			Log.e("BaseTableTask", e.getMessage());
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
