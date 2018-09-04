package com.ecity.mobile.android.cityselector.dialog;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ecity.mobile.android.cityselector.CitySelectorInitializer;
import com.ecity.mobile.android.cityselector.R;

public class CitySelectDialog extends Dialog {
	public interface OnCitySelectDialogListener {
		public void back(boolean result,String proviceName,String cityName,String districtName,String code);
	}

	
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	
	private String title;
	private OnCitySelectDialogListener listener;
	private Context context;
	public CitySelectDialog(Context context, String title,
			OnCitySelectDialogListener customDialogListener) {
		super(context,R.style.CitySelector);
		this.context = context;
		this.title = title;
		this.listener = customDialogListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.panel_selector);
		((TextView) findViewById(R.id.tv_title)).setText(title);
		TextView okBtn = (TextView) findViewById(R.id.txt_ok_cancel_dialog_ok);
		TextView cancelBtn = (TextView) findViewById(R.id.txt_ok_cancel_dialog_cancel);
		
		mViewProvince = (WheelView) findViewById(R.id.id_province);
		mViewCity = (WheelView) findViewById(R.id.id_city);
		mViewDistrict = (WheelView) findViewById(R.id.id_district);
		
		setUpListener();
		setUpData();
		
		okBtn.setOnClickListener(clickOkListener);
		cancelBtn.setOnClickListener(clickCancelListener);
	}

	@Override
	public void show() {
		Window mWindow = getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        if(context.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE){//横屏
            lp.width= height/10*8;
        }else{
            lp.width= width/10*8;
        }
        mWindow.setAttributes(lp);
        
		super.show();
	}
	
	private View.OnClickListener clickOkListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if(null != listener)
				listener.back(true,CitySelectorInitializer.getInstance().mCurrentProviceName,CitySelectorInitializer.getInstance().mCurrentCityName,CitySelectorInitializer.getInstance().mCurrentDistrictName,CitySelectorInitializer.getInstance().mCurrentZipCode);
			CitySelectorInitializer.getInstance().save();
			CitySelectDialog.this.dismiss();
		}
	};
	private View.OnClickListener clickCancelListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if(null != listener)
				listener.back(false,CitySelectorInitializer.getInstance().mCurrentProviceName,CitySelectorInitializer.getInstance().mCurrentCityName,CitySelectorInitializer.getInstance().mCurrentDistrictName,CitySelectorInitializer.getInstance().mCurrentZipCode);
			CitySelectDialog.this.dismiss();
		}
	};

	// 返回按钮
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if(null != listener)
				listener.back(false,CitySelectorInitializer.getInstance().mCurrentProviceName,CitySelectorInitializer.getInstance().mCurrentCityName,CitySelectorInitializer.getInstance().mCurrentDistrictName,CitySelectorInitializer.getInstance().mCurrentZipCode);
			CitySelectDialog.this.dismiss();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	private void setUpListener() {
    	mViewProvince.addChangingListener(onWheelChangedListener);
    	mViewCity.addChangingListener(onWheelChangedListener);
    	mViewDistrict.addChangingListener(onWheelChangedListener);
    }
	
	private void setUpData() {
		CitySelectorInitializer.getInstance().initialize(context);
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context, CitySelectorInitializer.getInstance().mProvinceDatas));
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	private OnWheelChangedListener onWheelChangedListener = new OnWheelChangedListener()
	{
		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			if (wheel == mViewProvince) {
				updateCities();
			} else if (wheel == mViewCity) {
				updateAreas();
			} else if (wheel == mViewDistrict) {
				CitySelectorInitializer.getInstance().mCurrentDistrictName = CitySelectorInitializer.getInstance().mDistrictDatasMap.get(CitySelectorInitializer.getInstance().mCurrentCityName)[newValue];
				CitySelectorInitializer.getInstance().mCurrentZipCode = CitySelectorInitializer.getInstance().mZipcodeDatasMap.get(CitySelectorInitializer.getInstance().mCurrentDistrictName);
			}
		}
	};
	private void updateAreas() {
		
		if(null == CitySelectorInitializer.getInstance().mProvinceDatas)
			return;
		if(null == CitySelectorInitializer.getInstance().mCitisDatasMap)
			return;
		
		int pCurrent = mViewCity.getCurrentItem();
		CitySelectorInitializer.getInstance().mCurrentCityName = CitySelectorInitializer.getInstance().mCitisDatasMap.get(CitySelectorInitializer.getInstance().mCurrentProviceName)[pCurrent];
		String[] areas = CitySelectorInitializer.getInstance().mDistrictDatasMap.get(CitySelectorInitializer.getInstance().mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
		mViewDistrict.setCurrentItem(0);
	}
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		
		if(null == CitySelectorInitializer.getInstance().mProvinceDatas)
			return;
		CitySelectorInitializer.getInstance().mCurrentProviceName = CitySelectorInitializer.getInstance().mProvinceDatas[pCurrent];
		String[] cities = CitySelectorInitializer.getInstance().mCitisDatasMap.get(CitySelectorInitializer.getInstance().mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}
}
