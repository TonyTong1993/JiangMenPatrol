package com.ecity.android.httpforandroid.http;

import java.net.SocketTimeoutException;

import org.json.JSONObject;
/***
 * 
 * @Description
 * @version V2.0 
 * @Author ZiZhengzhuan
 * @CreateDate 2016年1月15日
 * @email
 */
public class BaseStringTask extends HttpBaseService<String> {

	public BaseStringTask(HttpBaseServiceParameters paramTaskParameters) {
		super(paramTaskParameters);
	}

	public BaseStringTask(HttpBaseServiceParameters paramTaskParameters, HttpBaseServiceListener<String> paramTaskListener) {
		super(paramTaskParameters, paramTaskListener);
	}

	@Override
	public String execute() {
		try {
			String jsonstr = HttpRequestJsonParse.executeFromMapToStringNew(this.actionInput.getUrl(), this.actionInput.generateRequestParams(),this.actionInput.getRequestType());
			JSONObject obj = new JSONObject(jsonstr);
			if(obj!=null)
			{
				String flag = String.valueOf(obj.getBoolean("success"));
				String msg = obj.getString("msg");
				if(flag == null) {
                    flag = "false";
                }
				if(msg==null) {
                    msg = " ";
                }
				String result = flag+ ":" +msg;
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getMessage().toLowerCase().contains("failed to connect to") 
					|| e.getMessage().toLowerCase().contains("refused")
					|| e.getMessage().toLowerCase().contains("timed out")
					|| e instanceof SocketTimeoutException)
			{
			}
		}
		return "";
	}
}
