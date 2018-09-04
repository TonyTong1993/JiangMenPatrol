package com.ecity.cswatersupply.ui.activities;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.AppVerionInfo;
import com.ecity.cswatersupply.service.AppVersionCheckRunnable;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.AppUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.SettingsManager;
import com.ecity.cswatersupply.utils.UIHelper;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;
import com.zzz.ecity.android.applibrary.utils.AutomaticUpdate;

public class SystemSettingsActivity extends BaseActivity {
    private CustomTitleView mViewTitle;
    private RelativeLayout clearMemory;
    private RelativeLayout checkUpdate;
    private ToggleButton switchOutOfAreaNotice;
    private String imagePath = Environment.getExternalStorageDirectory().getPath() + "/ECity/CSWaterSupply/Media/";
    private String audioFile = Environment.getExternalStorageDirectory().getPath() + "/Z3SDK/cswatersupply/media/";
    private File images;
    private File audio;
    private TextView appVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_settings);
        initUI();
        images = new File(imagePath);
        audio = new File(audioFile);
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    private void initUI() {
        mViewTitle = (CustomTitleView) findViewById(R.id.view_title_system_settings);
        switchOutOfAreaNotice = (ToggleButton) findViewById(R.id.switch_out_of_area_notice);
        mViewTitle.setTitleText(R.string.my_profile_system_settings);

        if (SettingsManager.getInstance().isOutOfAreaNoticeEnabled()) {
            switchOutOfAreaNotice.setBackgroundResource(R.drawable.switch_on);
        }

        switchOutOfAreaNotice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setBackgroundResource(isChecked ? R.drawable.switch_on : R.drawable.switch_off);
                SettingsManager.getInstance().enableOutOfAreaNotice(isChecked);
            }
        });

        clearMemory = (RelativeLayout) findViewById(R.id.rl_clear_memory);
        clearMemory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.startActivityWithoutExtra(MemoryClearActivity.class);
            }
        });

        appVersion = (TextView) findViewById(R.id.tv_app_version);
        appVersion.setText(AppUtil.getVersion(HostApplication.getApplication().getApplicationContext()));
        checkUpdate = (RelativeLayout) findViewById(R.id.rl_check_update);
        checkUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUpdateVersion();
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void checkUpdateVersion() {
        LoadingDialogUtil.show(this, R.string.checking_new_version);
        AppUtil.checkAppUpdate(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == AppVersionCheckRunnable.CHECKAPPUPDATE_COMPLETE) {
                    AppVerionInfo appVerionInfo = null;
                    try {
                        appVerionInfo = (AppVerionInfo) msg.obj;
                        if (null == appVerionInfo || !appVerionInfo.isSuccess()) {
                            Toast.makeText(SystemSettingsActivity.this, getString(R.string.check_new_version_fail), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        AutomaticUpdate automaticUpdate = new AutomaticUpdate(SystemSettingsActivity.this);
                        String pakagePath = ServiceUrlManager.getAPPUrl();
                        String apk = pakagePath+appVerionInfo.getPakage();
                        if(automaticUpdate.isUpdate(appVerionInfo.getVersionCode())){
                            automaticUpdate.checkUpdate(apk,appVerionInfo.getVersionCode(),appVerionInfo.getType(),appVerionInfo.getDescription());
                        } else {
                            Toast.makeText(SystemSettingsActivity.this, getString(R.string.str_soft_update_no), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                super.handleMessage(msg);
            }
        });
    }

}
