package com.z3.android.plugin.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.ecity.android.weatherdemo.R;
import com.z3.android.plugin.weather.model.WeatherInfo;
import com.z3.android.plugin.weather.util.DBHelper;
import com.z3.android.plugin.weather.util.HttpService;

public class PluginMain {
	private Context mContext;
	private Map<String, Integer> mWeatherIcon;// the weather icon
	private boolean isRuning = false;
	
	private List<OnNotifyReceivingWeatherInfo> notifyReceivings = new ArrayList<OnNotifyReceivingWeatherInfo>();

	public interface OnNotifyReceivingWeatherInfo {
		public void onNotifyReceivingWeatherInfo(boolean isSuccess, String msg,
				WeatherInfo weatherInfo);
	}

	public PluginMain(Context mContext) {
		this.mContext = mContext;
		initWeatherIconMap();
	}

	public boolean isRuning() {
		return isRuning;
	}

	public void destroy() {
		if (null != notifyReceivings && notifyReceivings.size() > 0) {
			notifyReceivings.clear();
		}
		if (null != mWeatherIcon) {
			mWeatherIcon.clear();
		}
		mContext = null;
		mWeatherIcon = null;
		notifyReceivings = null;
	}

	/****
	 * 注册一个通知接收者
	 * 
	 * @param listener
	 * @return
	 */
	public boolean registerOnNotifyReceivingWeatherInfo(
			OnNotifyReceivingWeatherInfo listener) {
		boolean isSuccess = false;
		if (null != notifyReceivings && null != listener) {
			try {
				if (!notifyReceivings.contains(listener)) {
					notifyReceivings.add(listener);
					isSuccess = true;
				}
			} catch (Exception e) {
				isSuccess = false;
			}
		}

		return isSuccess;
	}

	/***
	 * 解除注册一个通知接收者
	 * 
	 * @param listener
	 * @return
	 */
	public boolean unRegisterOnNotifyReceivingWeatherInfo(
			OnNotifyReceivingWeatherInfo listener) {
		boolean isSuccess = false;
		if (null != notifyReceivings && null != listener) {
			try {
				if (!notifyReceivings.contains(listener)) {
					notifyReceivings.remove(listener);
					isSuccess = true;
				}
			} catch (Exception e) {
				isSuccess = false;
			}
		}
		return isSuccess;
	}

	/****
	 * 通知天气状态变化
	 * 
	 * @param isSuccess
	 * @param msg
	 * @param weatherInfo
	 */
	private void notifyReceivingWeatherInfo(boolean isSuccess, String msg,
			WeatherInfo weatherInfo) {
		if (null != notifyReceivings && notifyReceivings.size() > 0) {
			for (OnNotifyReceivingWeatherInfo listener : notifyReceivings) {
				listener.onNotifyReceivingWeatherInfo(isSuccess, msg,
						weatherInfo);
			}
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			isRuning = false;
			switch (msg.what) {
			case 0:
				notifyReceivingWeatherInfo(
						false,
						"cityname or citycode Invalid,Unable to get weather information",
						null);
				break;

			case 1:
				if (msg.obj == null
						|| false == (msg.obj instanceof WeatherInfo)) {
					String info = msg.getData().getString("data");
					notifyReceivingWeatherInfo(false,
							"Unable to get weather information :" + info, null);
				} else {
					WeatherInfo weatherInfo = (WeatherInfo) msg.obj;
					notifyReceivingWeatherInfo(true, "", weatherInfo);
				}
				break;

			default:
				break;
			}
		}
	};

	/**
	 * according to the weather conditions to obtain the weather icon
	 * 
	 * @param climate
	 *            the weather conditions
	 * @return the number in R.drawable
	 */
	public int getWeatherIcon(String climate) {
		int weatherRes = R.drawable.ecity_weather_qing;
		if (TextUtils.isEmpty(climate))
			return weatherRes;
		String[] strs = { "晴", "晴" };
		if (climate.contains("转")) {// 天气带转字，取前面那部分
			strs = climate.split("转");
			climate = strs[0];
			if (climate.contains("到")) {// 如果转字前面那部分带到字，则取它的后部分
				strs = climate.split("到");
				climate = strs[1];
			}
		}
		if (mWeatherIcon.containsKey(climate)) {
			weatherRes = mWeatherIcon.get(climate);
		}
		return weatherRes;
	}

	/**
	 * get the code of city
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getCityCode(String cityName) throws Exception {
		String cityCode = null;
		if (null == cityName || cityName.length() == 0)
			throw new Exception("cityname is empty");
		if (null == mContext)
			throw new Exception("mContext is null");

		DBHelper dbHelper = new DBHelper(mContext);

		String sql = "select number from city where city=?";
		cityName = cityName.replace("市", "").trim();
		cityCode = dbHelper.rawQuery(sql, new String[] { cityName });
		dbHelper.closeConnection();
		return cityCode;
	}

	/**
	 * get the name of city
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getCityName(String cityCode) throws Exception {
		String cityName = null;
		if (null == cityCode || cityCode.length() == 0)
			throw new Exception("cityCode is empty");
		if (null == mContext)
			throw new Exception("mContext is null");

		DBHelper dbHelper = new DBHelper(mContext);
		String sql = "select city from city where number=?";
		cityName = dbHelper.rawQuery(sql, new String[] { cityCode });
		dbHelper.closeConnection();
		return cityName;
	}

	/***
	 * 根据城市名请求天气
	 * 
	 * @param cityName
	 */
	public void requestWeatherByCityName(String cityName, boolean isSina) {
		String cityCode = null;
		try {
			cityCode = getCityCode(cityName);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		cityName = cityName.replace("市", "").trim();
		if(isSina)
		{
			QuerySinaAsyncTask asyncTask = new QuerySinaAsyncTask(cityName);
			 asyncTask.execute(cityName);
		}else
		{
			requestWeatherByCityCode(cityCode);
		}

		isRuning = true;
	}

	/***
	 * 根据城市编码请求天气 cityCode
	 * 
	 * @param cityCode
	 */
	public void requestWeatherByCityCode(String cityCode) {
		String cityName = null;
		try {
			cityName = getCityName(cityCode);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		cityName = cityName.replace("市", "").trim();
		RequestWeatherRunnable rub = new RequestWeatherRunnable(cityCode);
		new Thread(rub).start();
		isRuning = true;
	}

	/**
	 * An inner class to start another thread
	 * 
	 * @author SunShanai
	 * @version: In March 17, 2015 10:27:45 p.m.
	 */
	class RequestWeatherRunnable implements Runnable {
		private String cityCode = null;

		public RequestWeatherRunnable(String cityCode) {
			this.cityCode = cityCode;
		}

		@Override
		public void run() {
			String weatherUrl = null;
			if (null == cityCode || cityCode.length() == 0) {
				weatherUrl = null;
			} else {
				weatherUrl = "http://m.weather.com.cn/data/" + cityCode
						+ ".html";
			}
			
			if (null == weatherUrl || weatherUrl.length() == 0) {
				handler.sendEmptyMessage(0);
			} else {
				String weatherJson = null;
				String ErrMsg = "";
				boolean errflg = false;
				try {
					weatherJson = queryStringForGet(weatherUrl);

					JSONObject jsonObject = null;
					try {
						jsonObject = new JSONObject(weatherJson);
					} catch (JSONException e1) {
						
						e1.printStackTrace();
						ErrMsg += e1.getMessage();
						errflg = true;
					} catch (Exception e) {
						e.printStackTrace();
						ErrMsg += e.getMessage();
						errflg = true;
					}
					JSONObject weatherObject = null;
					WeatherInfo weatherInfo = new WeatherInfo();
					if (!errflg) {
						weatherInfo.setAllInfo(weatherJson);
						try {
							weatherObject = jsonObject
									.getJSONObject("weatherinfo");
							weatherInfo.setCityName(weatherObject
									.getString("city"));
							weatherInfo.setDate(weatherObject
									.getString("date_y"));
							weatherInfo.setFchh("今日"
									+ weatherObject.getString("fchh") + "时发布");
							weatherInfo
									.setWeek(weatherObject.getString("week"));
							weatherInfo.setTemperature(weatherObject
									.getString("temp1"));
							weatherInfo.setWeather(weatherObject
									.getString("weather1"));
							weatherInfo.setWind(weatherObject
									.getString("wind1"));

						} catch (Exception e) {
							e.printStackTrace();
							ErrMsg += e.getMessage();
							errflg = true;
						}
					}

					Message msg = new Message();
					if (errflg)
						msg.what = 0;
					else
						msg.what = 1;
					msg.obj = weatherInfo;
					Bundle bundle = new Bundle();
					bundle.putString("data", ErrMsg);
					msg.setData(bundle);
					handler.sendMessage(msg);

				} catch (IOException e1) {
					
					e1.printStackTrace();
					Message msg = new Message();
					msg.what = 0;
					Bundle bundle = new Bundle();
					bundle.putString("data", e1.getMessage());
					msg.setData(bundle);
					handler.sendMessage(msg);
				}
			}
		}
	}

	/**
	 * initialize the weather icon
	 */
	private void initWeatherIconMap() {
		mWeatherIcon = new HashMap<String, Integer>();
		mWeatherIcon.put("暴雪", R.drawable.ecity_weather_baoxue);
		mWeatherIcon.put("暴雨", R.drawable.ecity_weather_baoyu);
		mWeatherIcon.put("大暴雨", R.drawable.ecity_weather_dabaoyu);
		mWeatherIcon.put("大雪", R.drawable.ecity_weather_daxue);
		mWeatherIcon.put("大雨", R.drawable.ecity_weather_dayu);

		mWeatherIcon.put("多云", R.drawable.ecity_weather_duoyun);
		mWeatherIcon.put("雷阵雨", R.drawable.ecity_weather_leizhenyu);
		mWeatherIcon.put("雷阵雨冰雹", R.drawable.ecity_weather_leizhenyubingbao);
		mWeatherIcon.put("晴", R.drawable.ecity_weather_qing);
		mWeatherIcon.put("沙尘暴", R.drawable.ecity_weather_shachenbao);

		mWeatherIcon.put("特大暴雨", R.drawable.ecity_weather_tedabaoyu);
		mWeatherIcon.put("雾", R.drawable.ecity_weather_wu);
		mWeatherIcon.put("小雪", R.drawable.ecity_weather_xiaoxue);
		mWeatherIcon.put("小雨", R.drawable.ecity_weather_xiaoyu);
		mWeatherIcon.put("阴", R.drawable.ecity_weather_yin);

		mWeatherIcon.put("雨夹雪", R.drawable.ecity_weather_yujiaxue);
		mWeatherIcon.put("阵雪", R.drawable.ecity_weather_zhenxue);
		mWeatherIcon.put("阵雨", R.drawable.ecity_weather_zhenyu);
		mWeatherIcon.put("中雪", R.drawable.ecity_weather_zhongxue);
		mWeatherIcon.put("中雨", R.drawable.ecity_weather_zhongyu);
	}

	
	private class QuerySinaAsyncTask extends AsyncTask<Object, Object, Object> {
		private String cityName;
		public QuerySinaAsyncTask(String cityName)
		{
			this.cityName = cityName;
		}
		@Override
		protected void onPostExecute(Object result) {
			String ErrMsg = "";
			boolean errflg = false;
			WeatherInfo weatherInfo = null;

			if(result!=null){
				String weatherResult = (String)result;
				if(weatherResult.split(";").length>1){
					String a  = weatherResult.split(";")[1];
					if(a.split("=").length>1){
						String b = a.split("=")[1];
						String c = b.substring(1,b.length()-1);
						String[] resultArr = c.split("\\}");
						if(resultArr.length>0){
							weatherInfo = todayParse(cityName,resultArr[0]);
							if(null == weatherInfo)
							{
								ErrMsg = "查询天气失败，解析结果为空！";
								errflg = true;
							}
							else
								errflg = false;
						}
						
					}else{
						ErrMsg = "查询天气失败";
						errflg = true;
					}
				}else{
					ErrMsg = "查询天气失败";
					errflg = true;
				}
			}else{
				ErrMsg = "查询天气失败";
				errflg = true;
			}
			
			Message msg = new Message();
			if (errflg)
				msg.what = 0;
			else
				msg.what = 1;
			msg.obj = weatherInfo;
			Bundle bundle = new Bundle();
			bundle.putString("data", ErrMsg);
			msg.setData(bundle);
			handler.sendMessage(msg);
			
			super.onPostExecute(result);			
		}
			
		@Override
		protected Object doInBackground(Object... params) {
			return HttpService.getWeather(cityName);
		}
	}
	
	
	private WeatherInfo todayParse(String cityName,String weather){
		WeatherInfo weatherInfo = null;
		String temp = weather.replace("'", "");
		String[] tempArr = temp.split(",");
		String wd="";
		String tq="";
		String fx="";
		if(tempArr.length>0){
			for(int i=0;i<tempArr.length;i++){
				if(tempArr[i].indexOf("t1:")!=-1){
					wd=tempArr[i].substring(3,tempArr[i].length())+"℃";
				}else if(tempArr[i].indexOf("t2:")!=-1){
					wd=wd+"~"+tempArr[i].substring(3,tempArr[i].length())+"℃";
				}else if(tempArr[i].indexOf("d1:")!=-1){
					fx=tempArr[i].substring(3,tempArr[i].length());
				}else if(tempArr[i].indexOf("s1:")!=-1){
					tq=tempArr[i].substring(4,tempArr[i].length());
				}
			}
			
			weatherInfo = new WeatherInfo();
			weatherInfo.setCityName(cityName);
			weatherInfo.setDate(getCurrentTime());
			weatherInfo.setFchh("");
			weatherInfo.setWeek(getWeek(System.currentTimeMillis()));
			weatherInfo.setTemperature(wd);
			weatherInfo.setWeather(tq);
			weatherInfo.setWind(fx);
		}
		return weatherInfo;
	}
	
	/***
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrentTime(){

		Timestamp stamp1 = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeStr = sdf.format(stamp1); 
		return timeStr;
	}
	
	public static String getWeek(long time){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(new Date(time));
		
		String Week = "星期";
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			Week += "天";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			Week += "一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			Week += "二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			Week += "三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			Week += "四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			Week += "五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			Week += "六";
		}
		return Week;
	}
	
	/**
	 * access to the specified URL data
	 * 
	 * @param url
	 *            API address
	 * @return String
	 * @throws IOException
	 */
	private String queryStringForGet(String url) throws IOException {
		if (null == url || url.length() < 1)
			return null;

		URL url1 = null;
		try {
			url1 = new URL(url);
		} catch (MalformedURLException e) {
			
			throw new IOException(e.getMessage());
		}

		HttpURLConnection connect = null;
		connect = (HttpURLConnection) url1.openConnection();
		connect.setConnectTimeout(15 * 1000);// 连接超时15秒
		connect.setReadTimeout(60 * 1000);// 读取数据超时1分钟

		connect.setRequestProperty("content-type", "text/plain; charset=utf-8");

		if (connect.getResponseCode() != 200) {
			throw new IOException(connect.getResponseMessage());
		}

		// 将返回的值存入到String中
		BufferedReader brd = null;
		try {
			brd = new BufferedReader(new InputStreamReader(
					connect.getInputStream(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		StringBuilder sb = new StringBuilder();
		String line;
		try {
			while ((line = brd.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		try {
			brd.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		connect.disconnect();
		String result = new String(sb.toString());
		if (result.startsWith("\"") && result.endsWith("\"")
				&& result.length() > 1
				&& (result.charAt(1) == '[' || result.charAt(1) == '{')) {
			result = result.substring(1, result.length() - 1);
		}
		result = result.replace("\\", "");
		return result;
	}
}

