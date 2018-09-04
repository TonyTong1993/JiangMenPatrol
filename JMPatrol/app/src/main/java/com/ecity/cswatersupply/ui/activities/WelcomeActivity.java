package com.ecity.cswatersupply.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.service.AppStatusService;
import com.ecity.cswatersupply.utils.ResourceUtil;

/**
 * the page of welcome
 * @author SunShan'ai
 */
public class WelcomeActivity extends Activity {

    boolean isFirstIn = false;
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    private static final long SPLASH_DELAY_MILLIS = 3000;
    private static final String SHAREDPREFERENCES_NAME = "first_pref";

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (!isGPSOPen()) {
            showDailog();
        }
    }

    private void showDailog() {
        AlertDialog.Builder builder = new Builder(WelcomeActivity.this);
        builder.setMessage(ResourceUtil.getStringById(R.string.gps_state_didnot_set));
        builder.setTitle(ResourceUtil.getStringById(R.string.gps_state_notice));
        builder.setPositiveButton(ResourceUtil.getStringById(R.string.gps_state_set_commit), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                openSettingGPS();
            }
        });
        builder.setNegativeButton(ResourceUtil.getStringById(R.string.gps_state_set_cancel), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void initDatas() {
        SharedPreferences preferences = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        isFirstIn = preferences.getBoolean("isFirstIn", true);
        if (!isFirstIn) {
            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtil.v(this, "Welcome to CSWaterSupply");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if (isLaunchAppNotFromDesktop()) {
            WelcomeActivity.this.finish();
            return;
        }
        initDatas();
        AppStatusService.startInstance(HostApplication.getApplication());
    }

    private void goHome() {
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        WelcomeActivity.this.startActivity(intent);
        WelcomeActivity.this.finish();
    }

    private void goGuide() {
        Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
        WelcomeActivity.this.startActivity(intent);
        WelcomeActivity.this.finish();
    }

    private final boolean isGPSOPen() {
        boolean gps = false;
        boolean exceptionFlg = false;

        try {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）  
            gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）  
            boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            gps = gps | network;
        } catch (Exception e) {
            exceptionFlg = true;
        }

        return !(exceptionFlg || !gps);

    }

    private final void openSettingGPS() {
        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, 0);
    }

    /**
     * 当程序在由第三方工具完成安装且直接打开，然后切换到后台运行时，再重新从桌面进入程序时不再进入欢迎页和登录页
     */
    private boolean isLaunchAppNotFromDesktop() {
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    return true;
                }
            }
        }
        return false;
    }
}