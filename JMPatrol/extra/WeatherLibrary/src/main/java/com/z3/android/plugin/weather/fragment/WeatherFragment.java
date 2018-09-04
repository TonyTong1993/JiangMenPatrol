package com.z3.android.plugin.weather.fragment;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.android.weatherdemo.R;
import com.ecity.mobile.android.cityselector.dialog.CitySelectDialog;
import com.ecity.mobile.android.cityselector.dialog.CitySelectDialog.OnCitySelectDialogListener;
import com.z3.android.plugin.weather.PluginMain;
import com.z3.android.plugin.weather.PluginMain.OnNotifyReceivingWeatherInfo;
import com.z3.android.plugin.weather.model.WeatherInfo;
import com.z3.android.plugin.weather.util.PreferencesUtil;

public class WeatherFragment extends Fragment {

	private View mView;
	private Context context;
	private PluginMain pluginMain = null;
	private String currentCity = "";
	private TextView txt_city;
	private TextView txt_date;
	private TextView txt_temperature;
	private TextView txt_wind;
	private ImageView img_weather;
	private TextView txt_switch;
	
	private CitySelectDialog dialog;
	
	private boolean paused = true;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == mView) {
			mView = inflater.inflate(R.layout.fragment_weather, container, false);
			context = getActivity().getApplicationContext();
			initView();
			bindData();
			initListener();
		}
		return mView;
	}
	
	private void initView() {
		this.txt_city=(TextView)mView.findViewById(R.id.txt_city);
		this.txt_temperature=(TextView)mView.findViewById(R.id.txt_temperature);
		this.img_weather=(ImageView)mView.findViewById(R.id.img_weather);
		this.txt_date=(TextView)mView.findViewById(R.id.txt_date);
		this.txt_wind=(TextView)mView.findViewById(R.id.txt_wind);
		this.txt_switch =(TextView)mView.findViewById(R.id.txt_switch);
	}
	
	public void setCityName(String city)
	{
		currentCity = city;
		stopTimerTask();
		if(null != context)
		{
			PreferencesUtil.putString(context, "w_city",""+city);
		}
		setTimerTask();
	}
	private String getCityname()
	{
		return currentCity;
	}
	/**
	 * 事件
	 */
	private void initListener() {
		pluginMain = new PluginMain(context);
		pluginMain.registerOnNotifyReceivingWeatherInfo(notifyReceiving);
		txt_switch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showCitySelectDialog();
			}
		});
	}
	
	/**
	 * 绑定数据
	 */
	private void bindData() {
		String w_city = PreferencesUtil.getString(context, "w_city","");
		String w_weather = PreferencesUtil.getString(context, "w_weather","");
		String w_temperature = PreferencesUtil.getString(context, "w_temperature","");
		String w_date = PreferencesUtil.getString(context, "w_date","");
		String w_wind = PreferencesUtil.getString(context, "w_wind","");
		try {
			txt_city.setText(w_city);
			txt_temperature.setText(w_temperature);
			img_weather.setImageResource(pluginMain.getWeatherIcon(w_weather));
			txt_date.setText(w_date);
			txt_wind.setText(w_wind);
		} catch (Exception e) {
		}
	}


	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onStop() {
		
		if(null != dialog && dialog.isShowing())
			dialog.cancel();
		
		super.onStop();
	}
	
	@Override
	public void onResume() {
		if(null == context)
		{
			try {
				context = getActivity().getApplicationContext();
			} catch (Exception e) {
			}
		}
		paused = false;
		setTimerTask();
		super.onResume();
	}
	
	@Override
	public void onPause() {
		paused = true;
		stopTimerTask();
		super.onPause();
	}
	
	
	private OnNotifyReceivingWeatherInfo notifyReceiving = new OnNotifyReceivingWeatherInfo()
	{
		@Override
		public void onNotifyReceivingWeatherInfo(boolean isSuccess,String msg,WeatherInfo weatherInfo)
		{
			if(isSuccess)
			{
				updateWeatherInfo(weatherInfo);
			}
		}
	};
	
	private void updateWeatherInfo(WeatherInfo weatherInfo)
	{
		if(null == weatherInfo)
			return;

		String weather=weatherInfo.getWeather();
		if(!paused)
		{
			txt_city.setText(weatherInfo.getCityName());
			txt_temperature.setText(weatherInfo.getTemperature());
			img_weather.setImageResource(pluginMain.getWeatherIcon(weather));
			txt_date.setText(weatherInfo.getDate());
			txt_wind.setText(weatherInfo.getWind());
		}
		
		try {
			PreferencesUtil.putString(context, "w_city",""+weatherInfo.getCityName());
			PreferencesUtil.putString(context, "w_weather",""+weather);
			PreferencesUtil.putString(context, "w_temperature",""+weatherInfo.getTemperature());	
			PreferencesUtil.putString(context, "w_date",""+weatherInfo.getDate());	
			PreferencesUtil.putString(context, "w_wind",""+weatherInfo.getWind());	
			
		} catch (Exception e) {
		}
		stopTimerTask();
	}
	
	private Timer timer = new Timer();
	private void stopTimerTask()
	{
		if(timer != null)
			timer.cancel();  
		timer = null;
	}
	
	private void setTimerTask() {
		if(null == timer)
			timer = new Timer();
		
		timer.schedule(new TimerTask() {   
			@Override   
			public void run() {
				timetaskHandler.sendEmptyMessage(0);
			}
		}, 0, 1000*20);}   
	
	private Handler timetaskHandler = new Handler(){
		@Override
        public void handleMessage(Message msg) {
				if(null == pluginMain || pluginMain.isRuning())
					return;
				pluginMain.requestWeatherByCityName(getCityname(),true);
		}
	};
	/***
	 * 选择城市
	 */
	private void showCitySelectDialog()
	{
		if(null != dialog && dialog.isShowing())
		{
			dialog.cancel();
		}
		dialog = new CitySelectDialog(getActivity(), getActivity().getResources().getString(R.string.str_citysel_title), new OnCitySelectDialogListener() {
			
			@Override
			public void back(boolean result, String proviceName, String cityName,
					String districtName, String code) {
				if(result)
				{
					setCityName(cityName);
					if(null != txt_city)
					{
						txt_city.setText(cityName);
					}
				}
			}
		});
		
		dialog.show();
	}
}