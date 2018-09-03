package com.example.zzht.jmpatrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.example.zzht.jmpatrol.constants.Constants;
import com.example.zzht.jmpatrol.event.ResponseEventStatus;
import com.example.zzht.jmpatrol.global.HostApplication;
import com.example.zzht.jmpatrol.global.SettingsManager;
import com.example.zzht.jmpatrol.utils.LoadingDialogUtil;
import com.example.zzht.jmpatrol.utils.StringUtil;
import com.example.zzht.jmpatrol.view.CustomTitleView;
import com.zzz.ecity.android.applibrary.dialog.AlertView;
import com.zzz.ecity.android.applibrary.view.CustomSpinner;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * Author by zzht, Email tongwanhua1993@163.com, Date on 2018/9/3.
 * PS: Not easy to write code, please indicate.
 */
public class LoginSettingActivity extends Activity {
    private CustomTitleView mViewTitle;

    private RelativeLayout mRlContainer;
    private LinearLayout mLlSpinnerEnvironment;
    private FrameLayout mFlSplitline;
    private TextView mTvEnvironment;
    private ArrayList<String> mEnvironments;
    private CustomSpinner mCustomSpinner;
    private MyEnvironmentOnSpinnerListener mEnvironmentSpinnerListener;
    private String mProtocolA;
    private String mIpAddressA;
    private String mPortA;
    private String mVirtualPathA;
    private String mProtocolB;
    private String mIpAddressB;
    private String mPortB;
    private String mVirtualPathB;

    private int lastEnvironmentIndex;
    private int mServerType;
    private Button mBtnNormal;
    private Button mBtnCustom;
    private EditText mEtServerIP;
    private EditText mEtServerPort;
    private EditText mEtServerVirtualPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        initDataSource();
        initUI();
        EventBusUtil.register(this);
    }

    private void initDataSource() {
        mServerType = SettingsManager.getInstance().getServerType();
        mProtocolA = SettingsManager.getInstance().getProtocolA();
        mIpAddressA = SettingsManager.getInstance().getServerIPA();
        mPortA = SettingsManager.getInstance().getServerPortA();
        mVirtualPathA = SettingsManager.getInstance().getServerVirtualPathA();
        lastEnvironmentIndex = SettingsManager.getInstance().getLastEnvironmentIndex();

        mProtocolB = mProtocolA;
        mIpAddressB = SettingsManager.getInstance().getServerIPB();
        mPortB = SettingsManager.getInstance().getServerPortB();
        mVirtualPathB = SettingsManager.getInstance().getServerVirtualPathB();


        if( (Constants.TARGET_SERVER_NORMAL != mServerType && Constants.TARGET_SERVER_CUSTOM != mServerType ) || StringUtil.isBlank(mIpAddressA)){//如果常规服务类型设置为空就进行默认设置
            if(StringUtil.isBlank(mIpAddressB)){//如果自定义类型也为空就默认使用常规类型 为电信
                initDefaultServerSettings();
                loadDefaultServerSettings();
                mServerType = Constants.TARGET_SERVER_NORMAL;
            } else {
                mServerType = Constants.TARGET_SERVER_CUSTOM;
            }
        } else {
            if( Constants.TARGET_SERVER_NORMAL == mServerType) {//常规
            } else  if ( Constants.TARGET_SERVER_CUSTOM == mServerType) {//自定义
                //no logic
            } else {
                //no logic
            }
        }

        String[] types = getResources().getStringArray(R.array.target_server);
        mEnvironments = new ArrayList<String>();
        for (String type : types) {
            mEnvironments.add(type);
        }
    }

    private void loadDefaultServerSettings(){
        mServerType = SettingsManager.getInstance().getServerType();
        mProtocolA = SettingsManager.getInstance().getProtocolA();
        mIpAddressA = SettingsManager.getInstance().getServerIPA();
        mPortA = SettingsManager.getInstance().getServerPortA();
        mVirtualPathA = SettingsManager.getInstance().getServerVirtualPathA();
        lastEnvironmentIndex = 1;
    }

    public static void initDefaultServerSettings() {
        if (StringUtil.isBlank(SettingsManager.getInstance().getServerIPA())) {
            SettingsManager.getInstance().setProtocolA(SettingsManager.TARGET_SERVER_PROTOCOL);
            SettingsManager.getInstance().setServerIPA(SettingsManager.TARGET_SERVER_UAT);
            SettingsManager.getInstance().setLastEnvironmentIndex(1);
        }
        if (StringUtil.isBlank(SettingsManager.getInstance().getServerPortA())) {
            SettingsManager.getInstance().setServerPortA(SettingsManager.TARGET_UAT_PORT);
        }
        if (StringUtil.isBlank(SettingsManager.getInstance().getServerVirtualPathA())) {
            SettingsManager.getInstance().setServerVirtualPathA(SettingsManager.TARGET_VIRTUAL_PATH);
        }
        if (SettingsManager.getInstance().getServerType() < 0) {
            SettingsManager.getInstance().setServerType(Constants.TARGET_SERVER_NORMAL);
        }
        //=========================================================================================
        //设置 默认自定义数据
        if (StringUtil.isBlank(SettingsManager.getInstance().getServerIPB())) {
            SettingsManager.getInstance().setProtocolB(SettingsManager.TARGET_SERVER_PROTOCOL);
            SettingsManager.getInstance().setServerIPB(SettingsManager.TARGET_SERVER_UAT);
        }
        if (StringUtil.isBlank(SettingsManager.getInstance().getServerPortB())) {
            SettingsManager.getInstance().setServerPortB(SettingsManager.TARGET_DEV_PORT);
        }
        if (StringUtil.isBlank(SettingsManager.getInstance().getServerVirtualPathB())) {
            SettingsManager.getInstance().setServerVirtualPathB(SettingsManager.TARGET_VIRTUAL_PATH);
        }
    }

    private void initUI() {
        setContentView(R.layout.activity_setting);

        mRlContainer = (RelativeLayout) findViewById(R.id.rl_container);
        mBtnNormal = (Button) findViewById(R.id.btn_normal);
        mBtnCustom = (Button) findViewById(R.id.btn_custom);
        mFlSplitline = (FrameLayout) findViewById(R.id.fl_splitline);
        mEtServerIP = (EditText) findViewById(R.id.et_server);
        mEtServerPort = (EditText) findViewById(R.id.et_port);
        mEtServerVirtualPath = (EditText) findViewById(R.id.et_virtualPath);

        mViewTitle = (CustomTitleView) findViewById(R.id.customTitleView1);
        mViewTitle.setTitleText(R.string.setting_login_setting);
        mViewTitle.setBtnStyle(CustomTitleView.BtnStyle.RIGHT_ACTION);
        mViewTitle.setRightActionBtnText(R.string.save);

        mLlSpinnerEnvironment = (LinearLayout) findViewById(R.id.ll_spinner_environment);
        mTvEnvironment = (TextView) findViewById(R.id.tv_environment);
        mEnvironmentSpinnerListener = new MyEnvironmentOnSpinnerListener();
        mCustomSpinner = new CustomSpinner(this, mLlSpinnerEnvironment, mTvEnvironment, mEnvironmentSpinnerListener, mEnvironments, lastEnvironmentIndex);
        refreshEditTextView();
        if (Constants.TARGET_SERVER_CUSTOM == mServerType ) {
            mBtnCustom.performClick();
        }

        Button bt_vpn = (Button) findViewById(R.id.btn_vpn_test);
        if (HostApplication.getApplication().vpnTest) {
            bt_vpn.setVisibility(View.VISIBLE);
            bt_vpn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginSettingActivity.this, VpnTestViewActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            bt_vpn.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新编辑框
     */
    private void refreshEditTextView(){
        if (Constants.TARGET_SERVER_NORMAL == mServerType) {
            mEtServerIP.setText(mIpAddressA);
            mEtServerPort.setText(mPortA);
            mEtServerVirtualPath.setText(mVirtualPathA);
            mCustomSpinner.setSelection(lastEnvironmentIndex);
        } else {
            mEtServerIP.setText(mIpAddressB);
            mEtServerPort.setText(mPortB);
            mEtServerVirtualPath.setText(mVirtualPathB);
        }
    }

    public void onBackButtonClicked(View v) {
        if (isSettingInfoChanged()) {
            return;
        }
        finish();
    }

    public void onActionButtonClicked(View v) {
        if (isSettingInfoEmpty()) {
            Toast.makeText(this, R.string.is_null_prompt, Toast.LENGTH_SHORT).show();
            return;
        }
        if (0 == mServerType) {
            savePreferencesA();
        } else if (1 == mServerType) {
            savePreferencesB();
        } else {
            // no logic to do.
        }

        finish();
    }

    public void onNormalClicked(View v) {
        mServerType = 0;
        mFlSplitline.setVisibility(View.VISIBLE);
        mBtnNormal.setBackgroundResource(R.color.blue_normal);
        mBtnCustom.setBackgroundResource(R.color.transparent);
        if (mRlContainer.getVisibility() != View.VISIBLE) {
            mRlContainer.setVisibility(View.VISIBLE);
        }
        refreshEditTextView();
    }

    public void onCustomClicked(View v) {
        mServerType = 1;
        mFlSplitline.setVisibility(View.GONE);
        mBtnCustom.setBackgroundResource(R.color.blue_normal);
        mBtnNormal.setBackgroundResource(R.color.transparent);
        if (null != mEtServerVirtualPath && !StringUtil.isBlank(mVirtualPathA)) {
            mEtServerVirtualPath.setText(mVirtualPathA);
        }
        if (mRlContainer.getVisibility() == View.VISIBLE) {
            mRlContainer.setVisibility(View.GONE);
        }
        if (null != mCustomSpinner) {
            mCustomSpinner.setSelection(lastEnvironmentIndex);
        }
        refreshEditTextView();
    }

    public void onTestConnectionBtnClicked(View view) {
        if (isSettingInfoEmpty()) {
            return;
        }

        LoadingDialogUtil.show(this, R.string.setting_testing_connetion);
        new Thread(testConnectionTask).start();
    }

    Runnable testConnectionTask = new Runnable() {
        public void run() {
            ResponseEvent event = new ResponseEvent(ResponseEventStatus.TestConnectionEvent);
            try {
                String testUrl = "";
                if (Constants.TARGET_SERVER_NORMAL == mServerType) {
                    testUrl = getTestURLA();
                } else if (Constants.TARGET_SERVER_CUSTOM == mServerType) {
                    testUrl = getTestURLB();
                } else {
                    //no logic to do.
                }
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(testUrl).openConnection();
                httpURLConnection.setConnectTimeout(10 * 1000);
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    event.setStatus(ResponseEventStatus.OK);
                } else {
                    event.setStatus(ResponseEventStatus.ERROR);
                }
            } catch (Exception e) {
                event.setStatus(ResponseEventStatus.ERROR);
            }

            EventBusUtil.post(event);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackButtonClicked(null);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * EventBus methods begin.
     */
    public void onEventMainThread(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        String msg = "";
        if (event.getStatus() == ResponseEventStatus.OK) {
            msg = getResources().getString(R.string.setting_test_connection_success);
        } else {
            msg = getResources().getString(R.string.setting_test_connection_failed);
        }
        ToastUtil.showShort(msg);
    }

    public void onEventMainThread(UIEvent event) {
        if (event.isToastForTarget(this)) {
            ToastUtil.showShort(event.getMessage());
        }
    }

    /**
     * EventBus methods end.
     */

    private String getTestURLA() {
        mIpAddressA = mEtServerIP.getText().toString();
        mPortA = mEtServerPort.getText().toString();
        mVirtualPathA = mEtServerVirtualPath.getText().toString();

        String url = mProtocolA + "://" + mIpAddressA + ":" + mPortA + "/" + mVirtualPathA + "/rest/userService/login";
        return url;
    }

    private String getTestURLB() {
        String url = mProtocolB + "://" + mIpAddressB + ":" + mPortB + "/" + mVirtualPathB + "/rest/userService/login";
        return url;
    }

    private String getIpAddress(int position) {
        String ip = "";
        switch (position) {
            case 0:
                ip = SettingsManager.TARGET_SERVER_PRO;
                break;
            case 1:
                ip = SettingsManager.TARGET_SERVER_UAT;
                break;
            case 2:
                ip = SettingsManager.TARGET_SERVER_DEV;
                break;
            default:
                break;
        }

        return ip;
    }

    private String getPort(int position) {
        String port = "";
        switch (position) {
            case 0:
                port = SettingsManager.TARGET_PRO_PORT;
                break;
            case 1:
                port = SettingsManager.TARGET_UAT_PORT;
                break;
            case 2:
                port = SettingsManager.TARGET_DEV_PORT;
                break;
            default:
                break;
        }

        return port;
    }

    private boolean isSettingInfoChanged() {
        String savedIPA = SettingsManager.getInstance().getServerIPA();
        String savedPortA = SettingsManager.getInstance().getServerPortA();
        String savedVirtualPathA = SettingsManager.getInstance().getServerVirtualPathA();

        String savedIPB = SettingsManager.getInstance().getServerIPB();
        String savedPortB = SettingsManager.getInstance().getServerPortB();
        String savedVirtualPathB = SettingsManager.getInstance().getServerVirtualPathB();

        int savedServerType = SettingsManager.getInstance().getServerType();
        if (Constants.TARGET_SERVER_CUSTOM  == savedServerType) {
            mIpAddressB = mEtServerIP.getText().toString();
            mPortB = mEtServerPort.getText().toString();
            mVirtualPathB = mEtServerVirtualPath.getText().toString();
        }else{
            mIpAddressA = mEtServerIP.getText().toString();
            mPortA = mEtServerPort.getText().toString();
            mVirtualPathA = mEtServerVirtualPath.getText().toString();
        }

        if (Constants.TARGET_SERVER_NORMAL == mServerType) {
            if (!savedIPA.equalsIgnoreCase(mIpAddressA) || !savedPortA.equalsIgnoreCase(mPortA) || !savedVirtualPathA.equalsIgnoreCase(mVirtualPathA)
                    || savedServerType != mServerType) {
                String title = getString(R.string.dialog_title_prompt);
                String msg = getString(R.string.setting_dialog_msg_server_info_changed);
                AlertView dialog = new AlertView(this, title, msg, new AlertView.OnAlertViewListener() {
                    @Override
                    public void back(boolean isPositiveBtnClicked) {
                        if (isPositiveBtnClicked) {
                            savePreferencesA();
                            finish();
                        } else {
                            finish();
                        }
                    }
                }, AlertView.AlertStyle.YESNO);
                dialog.show();

                return true;
            }
        } else if (Constants.TARGET_SERVER_CUSTOM  == mServerType) {
            if (!savedIPB.equalsIgnoreCase(mIpAddressB) || !savedPortB.equalsIgnoreCase(mPortB) || !savedVirtualPathB.equalsIgnoreCase(mVirtualPathB)
                    || savedServerType != mServerType) {
                String title = getString(R.string.dialog_title_prompt);
                String msg = getString(R.string.setting_dialog_msg_server_info_changed);
                AlertView dialog = new AlertView(this, title, msg, new OnAlertViewListener() {
                    @Override
                    public void back(boolean isPositiveBtnClicked) {
                        if (isPositiveBtnClicked) {
                            savePreferencesB();
                            finish();
                        } else {
                            finish();
                        }
                    }
                }, AlertView.AlertStyle.YESNO);
                dialog.show();

                return true;
            }
        } else {
            // no logic to do.
        }

        return false;
    }

    private boolean isSettingInfoEmpty() {
        String msg = getResources().getString(R.string.setting_server_info_empty);
        if (0 == mServerType) {
            if (StringUtil.isEmpty(mIpAddressA)) {
                switch (lastEnvironmentIndex) {
                    case 0:
                        msg = String.format(msg, getResources().getString(R.string.setting_target_server_pro));
                        break;
                    case 1:
                        msg = String.format(msg, getResources().getString(R.string.setting_target_server_uat));
                        break;
                    case 2:
                        msg = String.format(msg, getResources().getString(R.string.setting_target_server_dev));
                        break;
                    default:
                        break;
                }
                EventBusUtil.post(new UIEvent(UIEventStatus.TOAST, msg, this));
                return true;
            }
        }

        if (Constants.TARGET_SERVER_CUSTOM  == mServerType) {
            if (StringUtil.isBlank(mEtServerIP.getText().toString()) || StringUtil.isBlank(mEtServerPort.getText().toString())
                    || StringUtil.isBlank(mEtServerVirtualPath.getText().toString())) {
                return true;
            } else {
                mIpAddressB = mEtServerIP.getText().toString();
                mPortB = mEtServerPort.getText().toString();
                mVirtualPathB = mEtServerVirtualPath.getText().toString();
            }
        }else{
            mIpAddressA = mEtServerIP.getText().toString();
            mPortA = mEtServerPort.getText().toString();
            mVirtualPathA = mEtServerVirtualPath.getText().toString();
        }

        return false;
    }

    private void savePreferencesA() {
        SettingsManager.getInstance().setProtocolA(mProtocolA);
        SettingsManager.getInstance().setServerIPA(mIpAddressA);
        SettingsManager.getInstance().setServerPortA(mPortA);
        SettingsManager.getInstance().setServerVirtualPathA(mVirtualPathA);
        SettingsManager.getInstance().setServerType(mServerType);
        SettingsManager.getInstance().setLastEnvironmentIndex(lastEnvironmentIndex);

        if (0 == lastEnvironmentIndex) {
            SettingsManager.TARGET_SERVER_PRO = mIpAddressA;
            SettingsManager.TARGET_PRO_PORT = mPortA;
        } else {
            SettingsManager.TARGET_SERVER_UAT = mIpAddressA;
            SettingsManager.TARGET_UAT_PORT = mPortA;
        }

        SettingsManager.getInstance().saveDefaultServerIPPort();
    }

    private void savePreferencesB() {
        SettingsManager.getInstance().setProtocolB(mProtocolB);
        SettingsManager.getInstance().setServerIPB(mIpAddressB);
        SettingsManager.getInstance().setServerPortB(mPortB);
        SettingsManager.getInstance().setServerVirtualPathB(mVirtualPathB);
        SettingsManager.getInstance().setServerType(mServerType);
    }

    private class MyEnvironmentOnSpinnerListener implements CustomSpinner.OnSpinnerListener {

        @Override
        public void back(int position) {
            lastEnvironmentIndex = position;
            mIpAddressA = getIpAddress(position);
            mPortA = getPort(position);
            mVirtualPathA = SettingsManager.TARGET_VIRTUAL_PATH;
            refreshEditTextView();
        }
    }
}
