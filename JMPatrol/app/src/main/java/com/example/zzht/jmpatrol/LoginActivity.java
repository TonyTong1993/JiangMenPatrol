package com.example.zzht.jmpatrol;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText txtUserName;
    private EditText txtPassword;
    private CheckBox mCkState;
    private Button imgLogin;
    private TextView version;
    private TextView tvCrashUpload;
    private long[] mHints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTheme(android.R.style.Theme_Holo_Light);
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

    }

    private void initEvent() {
        imgLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_ok:
              onLoginClick(view);
                break;
            default:
                break;
        }

    }

    private void onLoginClick(View view) {
       String userName = txtUserName.getText().toString().trim();
       String passWord = txtPassword.getText().toString().trim();
       System.out.print("onLoginClick");
    }
}
