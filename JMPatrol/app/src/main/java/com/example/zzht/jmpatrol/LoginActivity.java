package com.example.zzht.jmpatrol;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zzht.jmpatrol.global.ServiceUrlManager;
import com.example.zzht.jmpatrol.global.SettingsManager;
import com.example.zzht.jmpatrol.service.GetLoginRequest;
import com.example.zzht.jmpatrol.utils.SystemUtils;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;
import com.zzz.ecity.android.applibrary.utils.AutomaticUpdate;
import com.zzz.ecity.android.applibrary.utils.ToastUtil;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText txtUserName;
    private EditText txtPassword;
    private CheckBox mCkState;
    private Button imgLogin;
    private TextView version;
    private TextView tvCrashUpload;
    private TextView tvSetting;
    private long[] mHints;
    private AutomaticUpdate automaticUpdate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        initUI();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initUI() {
        txtUserName = findViewById(R.id.et_username);
        txtPassword = findViewById(R.id.et_pwd);
        mCkState = findViewById(R.id.ck_state);
        imgLogin = findViewById(R.id.bt_ok);
        version = findViewById(R.id.tv_version);
        tvCrashUpload = findViewById(R.id.tv_test);
        tvSetting = findViewById(R.id.tv_setting);

        version.setText(SystemUtils.getVersion(this));

    }

    private void initEvent() {
        imgLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_ok:
                ToastUtil.showShort("请输入用户名");
              onLoginClick(view);
                break;
            default:
                ToastUtil.showShort("请输入用户名");
                break;
        }

    }

    private void onLoginClick(View view) {
       String userName = txtUserName.getText().toString().trim();
       String passWord = txtPassword.getText().toString().trim();
       System.out.print("onLoginClick");
        if (StringUtil.isBlank(userName)) {
            ToastUtil.showShort("请输入用户名");
            return;
        } else if (StringUtil.isBlank(passWord)) {
            ToastUtil.showShort("请输入密码");
            return;
        }else if ("://:/".equals(ServiceUrlManager.getInstance().getRootServiceUrl())) {

            SettingsManager.getInstance().setProtocolA("http");
            SettingsManager.getInstance().setServerIPA(SettingsManager.TARGET_SERVER_PROMOTION);
            SettingsManager.getInstance().setServerPortA(SettingsManager.TARGET_PROMOTION_PORT);
            SettingsManager.getInstance().setServerVirtualPathA("api/v1/sop");
            SettingsManager.getInstance().setServerType(0);
            SettingsManager.getInstance().setLastEnvironmentIndex(3);
            SettingsManager.getInstance().saveDefaultServerIPPort();
        }

        new GetLoginRequest(userName,passWord).execute();
    }
}
