package com.ecity.cswatersupply.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.SystemUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

public class DeviceUsageActivity extends BaseActivity {
    private CustomTitleView mViewTitle;
    private TextView mCpuUsage;
    private TextView mMeUsage;
    private TextView mTvBattery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_usage);
        initUI();
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    private void initUI() {
        mViewTitle = (CustomTitleView) findViewById(R.id.view_title_device_usage);
        mTvBattery = (TextView) findViewById(R.id.tv_battery_usage);
        mCpuUsage = (TextView) findViewById(R.id.tv_cpu_usage);
        mMeUsage = (TextView) findViewById(R.id.tv_memory_usage);

        mViewTitle.setTitleText(R.string.my_profile_device_usage);
        mTvBattery.setText(HostApplication.getApplication().getBatteryLevelPercent());
        mCpuUsage.setText(SystemUtil.readUsage());
        mMeUsage.setText(SystemUtil.getTotalMemory(getApplicationContext()));
    }
}
