package com.ecity.cswatersupply.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.service.UserService;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;
import com.zzz.ecity.android.applibrary.dialog.AlertView;
import com.zzz.ecity.android.applibrary.dialog.AlertView.OnAlertViewListener;

public class ChangePasswordActivity extends BaseActivity {
    private static final String _TYPE = "json";

    private EditText etOldPassword;
    private EditText etNewPassword;
    private EditText etNewPassworAgain;
    private Button tvDone;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_password);
        initView();
        setOnClickListener();
        EventBusUtil.register(this);
    }

    private void initView() {
        user = HostApplication.getApplication().getCurrentUser();
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.change_password);
        TextView username = (TextView) findViewById(R.id.tv_login_name_value);
        username.setText(user.getLoginName());
        etOldPassword = (EditText) findViewById(R.id.et_old_password);
        etNewPassword = (EditText) findViewById(R.id.et_new_password);
        etNewPassworAgain = (EditText) findViewById(R.id.et_new_password_again);
        tvDone = (Button) findViewById(R.id.btn_change_password);
    }

    public void onBackButtonClicked(View v) {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    private void setOnClickListener() {
        tvDone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final String oldPassword = etOldPassword.getText().toString().trim();
                final String newPassword = etNewPassword.getText().toString().trim();
                final String newPassworAgain = etNewPassworAgain.getText().toString().trim();
                if (oldPassword.isEmpty() || newPassword.isEmpty() || newPassworAgain.isEmpty()) {
                    Toast.makeText(ChangePasswordActivity.this, R.string.login_password_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newPassword.equals(newPassworAgain)) {
                    Toast.makeText(ChangePasswordActivity.this, R.string.login_password_unlike, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!oldPassword.equals(user.getPassword())) {
                    Toast.makeText(ChangePasswordActivity.this, R.string.change_password_error, Toast.LENGTH_SHORT).show();
                } else {
                    String title = getString(R.string.dialog_title_prompt);
                    String msg = getString(R.string.change_password_make_sure);
                    AlertView dialog = new AlertView(ChangePasswordActivity.this, title, msg, new OnAlertViewListener() {
                        @Override
                        public void back(boolean result) {
                            if (result) {
                                requestChangePassword(oldPassword, newPassword);
                            } else {
                                //no logic
                            }
                        }
                    }, AlertView.AlertStyle.YESNO);
                    dialog.show();
                }
            }
        });
    }

    private void requestChangePassword(String oldPassword, String newPassword) {
        UserService.getInstance().changePassword(_TYPE, user, oldPassword, newPassword);
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.USER_CHANGE_PASSWORD:
                handleChangePasswordResponse(event);
                break;
            default:
                break;
        }
    }

    private void handleChangePasswordResponse(ResponseEvent event) {
        Toast.makeText(ChangePasswordActivity.this, R.string.change_password_success, Toast.LENGTH_SHORT).show();
        String newPassword = etNewPassword.getText().toString().trim();
        HostApplication.getApplication().getCurrentUser().setPassword(newPassword);
        finish();
    }
}
