package com.enrique.bluetooth4falcon;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.z3pipe.mobile.android.corssdk.CORSSDK;
import com.z3pipe.mobile.android.corssdk.ICORSSDKCallback;
import com.z3pipe.mobile.android.corssdk.model.Constants;
import com.z3pipe.mobile.android.corssdk.model.PTNLInfo;
import com.z3pipe.mobile.android.corssdk.model.PositionErrorStatistic;
import com.z3pipe.mobile.android.corssdk.model.SatelliteInfo;
import com.zzz.ecity.android.applibrary.view.TitleView;
import com.zzz.ecity.android.applibrary.view.TitleView.BtnStyle;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author zhangfusheng
 * @date 2011-09-21
 */
public class CORSStateActivity extends Activity implements ICORSSDKCallback {
    private static final String TAG = "Bluetooth4falcon";
    // intent request codes
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 1;
    private static final int REQUEST_SET_CORS_PARAMS = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    private static final String PARAMS_LIST = "paramslist";
    private static int CONNECT_CORS_TIMES = 1;
    private static final String CORSSERVICECLASSNAME = "com.ecity.cswatersupply.service.CORSLocationService";
    // Layout Views
    private ListView mListView;
    public static TextView mLat, mLong, mDate, mHdop, mDGPS, mQuality,
            mHorizontalError, mAltitudeError, mCounts, mAltitude, mConnectdevice;
    private BluetoothAdapter mBluetoothAdapter = null;
    private ArrayAdapter<String> mConversationArrayAdapter;
    private TitleView customTitleView;
    private Button popUpBtn, corsParamSet;
    private View GPSOptionView;
    private boolean isCROSSConnected = false;
    private PopWindow popupWindow = null;
    public String[] locationOptions = {"GPS", "CROS"};
    Class<?> corslocationService = null;
    private String[] params;
    private Handler handler;

    private static boolean isFisrtInit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);// 禁止横屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cors_main);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        handler = new Handler();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, getString(R.string.str_cors_bluetooth_enabled), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        initView();
        bindListener();
        initData();
    }

    public void initData() {
        try {
            corslocationService = Class.forName(CORSSERVICECLASSNAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    public void onActionButtonClicked(View view) {
        if (null == popupWindow) {
            popupWindow = new PopWindow(this);
        }
        popupWindow.showPopupWindow(view);
    }

    private void initConnectType() {
        isCROSSConnected = CORSSDK.getInstance().isConnected();
        if (!isCROSSConnected) {
            if (isFisrtInit) {
                isCROSSConnected = isServiceRunning(getApplicationContext(), CORSSERVICECLASSNAME);
            }
        }

        isFisrtInit = false;

        if (!isCROSSConnected) {
            customTitleView.setRightActionBtnText(getString(R.string.str_cors_title_rightaction_GPS));
            GPSOptionView.setVisibility(View.VISIBLE);
        } else {
            customTitleView.setRightActionBtnText(getString(R.string.str_cors_title_rightaction_CORS));
        }
    }

    private void initView() {
        try {
            mLat = (TextView) findViewById(R.id.lat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mLat = (TextView) findViewById(R.id.lat);
        mLong = (TextView) findViewById(R.id.longi);
        mDate = (TextView) findViewById(R.id.date);
        mHdop = (TextView) findViewById(R.id.hdop);
        mDGPS = (TextView) findViewById(R.id.dgps);
        GPSOptionView = findViewById(R.id.gps_option_view);
        mQuality = (TextView) findViewById(R.id.quality);
        mHorizontalError = (TextView) findViewById(R.id.h_error);
        mAltitudeError = (TextView) findViewById(R.id.v_error);
        mCounts = (TextView) findViewById(R.id.counts);
        mAltitude = (TextView) findViewById(R.id.altitude);
        customTitleView = (TitleView) findViewById(R.id.customTitleView);
        customTitleView.setBtnStyle(BtnStyle.RIGHT_ACTION);
        customTitleView.setTitleText(getString(R.string.str_cors_title_not_connected));
        initConnectType();
        popUpBtn = (Button) findViewById(R.id.popUpBtn);
        corsParamSet = (Button) findViewById(R.id.corsParamSet);
        mConnectdevice = (TextView) findViewById(R.id.connectdevice);
    }

    private void bindListener() {
        popUpBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent serverIntent = new Intent(CORSStateActivity.this,
                        DeviceListActivity.class);
                startActivityForResult(serverIntent,
                        REQUEST_CONNECT_DEVICE_INSECURE);
            }
        });
        corsParamSet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serverIntent = new Intent(getApplicationContext(),
                        CORSParamsActivity.class);
                startActivityForResult(serverIntent, REQUEST_SET_CORS_PARAMS);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            SetupSeession();
        }
    }

    @Override
    protected void onResume() {
        CORSSDK.getInstance().registerCallBack(CORSStateActivity.this);

        if (CORSSDK.getInstance().isConnected()) {
            setStatus(getString(R.string.str_cors_title_connected));
            if (null != mConnectdevice) {
                mConnectdevice.setText(CORSSDK.getInstance().getConnectedDeviceName());
            }
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        CORSSDK.getInstance().unRegisterCallBack(CORSStateActivity.this);
        super.onPause();
    }

    private void SetupSeession() {
        Log.d(TAG, "SetupSeession()");
        mConversationArrayAdapter = new ArrayAdapter<String>(this,
                R.layout.listview_item_message);
        mListView = (ListView) findViewById(R.id.in);
        mListView.setAdapter(mConversationArrayAdapter);
    }

    private final void setStatus(CharSequence subTitle) {
        customTitleView.setTitleText(String.valueOf(subTitle));
    }

    private final void setStatus(int resId) {
        try {
            customTitleView.setTitleText(getString(resId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    SetupSeession();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.str_cors_bluetooth_enabled,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case REQUEST_SET_CORS_PARAMS:
                if (resultCode == Activity.RESULT_OK) {
                    params = data.getStringArrayExtra(PARAMS_LIST);
                    CORSSDK.getInstance().connectCORSService(params);
                }
                break;
        }
    }

    private void connectDevice(Intent data) {
        // Get the device MAC address
        String address = data.getExtras().getString(
                DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        CORSSDK.getInstance().connectDevice(device);
    }

    @Override
    public void onConnected(String arg0, String arg1) {
        setStatus(getString(R.string.str_cors_title_connected));
        isCROSSConnected = true;
        if (null != mConnectdevice) {
            mConnectdevice.setText(CORSSDK.getInstance().getConnectedDeviceName());
        }
        mConversationArrayAdapter.clear();
        Method startInstance = null;
        try {
            startInstance = corslocationService.getDeclaredMethod("startInstance", Context.class);
            Object[] objects = {getApplicationContext()};
            startInstance.invoke(null, objects);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnecting() {
        setStatus(R.string.str_cors_title_connecting);
    }

    @Override
    public void onNotConnected() {
        isCROSSConnected = false;
        setStatus(R.string.str_cors_title_not_connected);
        Method stopInstance = null;
        try {
            stopInstance = corslocationService.getDeclaredMethod("stopInstance", Context.class);
            Object[] objects = {getApplicationContext()};
            stopInstance.invoke(null, objects);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectedDevice(String arg0) {
    }

    @Override
    public void onMessageCORSLoginFail(String arg0) {
        Toast.makeText(getApplicationContext(), arg0, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onMessageCORSLoginSuccess() {
        if (CONNECT_CORS_TIMES < 2) {
            CORSSDK.getInstance().connectCORSService(params);
            CONNECT_CORS_TIMES++;
        }
        if (CONNECT_CORS_TIMES == 2) {
            Toast.makeText(getApplicationContext(),
                    Constants.MESSAGE_AUTHENCATION_SUCCSESS, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onMessageSwitchStateChanged(String arg0) {
        Intent intent = new Intent(getApplicationContext(),
                CORSParamsActivity.class);
        startActivityForResult(intent, REQUEST_SET_CORS_PARAMS);
    }

    @Override
    public void onMessageToast(String arg0) {
        Toast.makeText(getApplicationContext(), String.valueOf(arg0),
                Toast.LENGTH_SHORT).show();
        setStatus(R.string.str_cors_title_not_connected);
    }

    @Override
    public void onReceiveDateLength(String arg0) {
    }

    @Override
    public void onReceiveLocation(final SatelliteInfo arg0, Location arg1) {
        new Thread() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (null != arg0) {
                            mLat.setText(arg0.getLatitude());// 纬度
                            mLong.setText(arg0.getLongitude());// 经度
                            mDate.setText(arg0.getDate());// 日期
                            mQuality.setText(Constants.getQulityString(arg0.getQuality()));// 定位状态(解质量)
                            mCounts.setText(arg0.getCounts());// 卫星数
                            mHdop.setText(arg0.getHdop());// 水平精度因子
                            mAltitude.setText(arg0.getAltitude());// 椭球高
                            mDGPS.setText(arg0.getDGPS());// 差分龄期
                        }
                    }
                });
            }
        }.start();
    }

    @Override
    public void onReceivePTNLInfo(PTNLInfo arg0) {

    }

    @Override
    public void onReceivePositionErrorStatisticInfo(PositionErrorStatistic arg0) {
        if (null != arg0) {
            mHorizontalError.setText(arg0.getHorizontalError());
            mAltitudeError.setText(arg0.getAltitudeError());
        }
    }


    @Override
    public void onReceiveNMEAData(String nmea) {
        mConversationArrayAdapter.add(nmea);
        int count = mConversationArrayAdapter.getCount();
        if (count > 50) {
            for (int i = 0; i < 20; i++) {
                mConversationArrayAdapter.remove(mConversationArrayAdapter.getItem(i));
            }
        }
    }


    public class PopWindow extends PopupWindow {
        private View conentView;

        public PopWindow(final Activity context) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            conentView = inflater.inflate(R.layout.location_option_list_popupmenu, null);
            int h = context.getWindowManager().getDefaultDisplay().getHeight();
            int w = context.getWindowManager().getDefaultDisplay().getWidth();
            this.setContentView(conentView);
            this.setWidth(260);
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            this.setFocusable(true);
            this.setOutsideTouchable(true);
            this.update();
            ColorDrawable dw = new ColorDrawable(0000000000);
            this.setBackgroundDrawable(dw);
            this.setAnimationStyle(R.style.popup_tophalf_anim);
            ListView listView = (ListView) conentView.findViewById(R.id.ltv_menu);
            LocationOptionAdapter adapter = new LocationOptionAdapter(context);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        public void showPopupWindow(View parent) {
            if (!this.isShowing()) {
                this.showAsDropDown(parent, parent.getLayoutParams().width / 2 + 40, 30);
            } else {
                this.dismiss();
            }
        }
    }

    public class LocationOptionAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mLayoutInflater;

        public LocationOptionAdapter(Context context) {
            this.mContext = context;
            mLayoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return locationOptions.length;
        }

        @Override
        public Object getItem(int position) {

            return locationOptions == null ? null : locationOptions[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            if (convertView == null) {
                holder = new Holder();
                convertView = mLayoutInflater.inflate(R.layout.item_list_popupmenu, null);
                holder.tv_menu = (TextView) convertView.findViewById(R.id.tv_menu);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.tv_menu.setText(locationOptions[position]);
            holder.tv_menu.setOnClickListener(new LocationOptionClickListener(position));
            return convertView;
        }

        private class Holder {
            TextView tv_menu;
        }

        class LocationOptionClickListener implements OnClickListener {
            private int index;

            public LocationOptionClickListener(int index) {
                super();
                this.index = index;
            }

            @Override
            public void onClick(View v) {
                if (index == 0) {
                    CORSSDK.getInstance().stop();
                    CORSSDK.getInstance().unRegisterCallBack(CORSStateActivity.this);
                    customTitleView.setRightActionBtnText(getString(R.string.str_cors_title_rightaction_GPS));
                    GPSOptionView.setVisibility(View.VISIBLE);
                } else {
                    CORSSDK.getInstance().setup(getApplicationContext());
                    CORSSDK.getInstance().registerCallBack(CORSStateActivity.this);
                    customTitleView.setRightActionBtnText(getString(R.string.str_cors_title_rightaction_CORS));
                    GPSOptionView.setVisibility(View.GONE);
                }
                popupWindow.dismiss();
            }
        }
    }

    /**
     * 用来判断服务是否运行.
     *
     * @param
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(500);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equalsIgnoreCase(
                    className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
