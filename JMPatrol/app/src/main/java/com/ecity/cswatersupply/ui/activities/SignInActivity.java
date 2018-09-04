package com.ecity.cswatersupply.ui.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.service.UserService;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog.DatetimePickerCallback;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

public class SignInActivity extends BaseActivity {
    public static final int SIGN_IN_REQUEST_CODE = 100;
    public static final int SIGN_IN_RESULT_CODE = 101;
    public static final String SIGN_IN_RESULT_STRING = "SIGN_IN_RESULT_STRING";

    private CustomTitleView mViewTitle;
    private Button btnStartTime;
    private Button btnEndTime;

    private DatetimePickerDialog timeDialog;
    final String KEY_SIGN_IN = "KEY_SIGN_IN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        EventBusUtil.register(this);
        initUI();
        initData();
        setListener();
    }

    private void initUI() {
        mViewTitle = (CustomTitleView) findViewById(R.id.view_title_sign_in);
        mViewTitle.setTitleText(R.string.my_profile_sign_in);
        mViewTitle.setBtnStyle(BtnStyle.RIGHT_ACTION);
        TextView tvAction = (TextView) mViewTitle.findViewById(R.id.tv_action);
        tvAction.setText(R.string.submit_sign_in);

        btnStartTime = (Button) findViewById(R.id.btn_sign_start_time);
        btnEndTime = (Button) findViewById(R.id.btn_sign_end_time);
    }

    //提交签到方法
    public void onActionButtonClicked(View view) {
        signIn();
    }

    private void signIn() {
        //获得截止时间
        if (btnEndTime.getText().length() != 0) {
            String endTime = btnEndTime.getText().toString();
            String startTime = btnStartTime.getText().toString();
            User currentUser = HostApplication.getApplication().getCurrentUser();
            UserService.getInstance().signIn(currentUser, endTime, startTime);
        } else {
            ToastUtil.showShort(getString(R.string.user_sign_out_end_time_choose));
        }
    }

    private void initData() {
        btnStartTime.setText(DateUtil.getCurrentTime());
        btnEndTime.setHint(R.string.hint_choose);
    }

    private void setListener() {
        btnStartTime.setOnClickListener(new MyBtnSelectOnClickListener(btnStartTime, true));
        btnEndTime.setOnClickListener(new MyBtnSelectOnClickListener(btnEndTime, false));
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    private class MyBtnSelectOnClickListener implements OnClickListener {
        private Button btn;
        private boolean isStartTime;

        public MyBtnSelectOnClickListener(Button btn, boolean isStartTime) {
            this.btn = btn;
            this.isStartTime = isStartTime;
        }

        @Override
        public void onClick(View v) {
            timeDialog = new DatetimePickerDialog(SignInActivity.this, AlertDialog.THEME_HOLO_LIGHT, new MyDateTimeCallBack(btn, isStartTime));
            LayoutParams attributes = timeDialog.getWindow().getAttributes();
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            attributes.width = (int) (metrics.widthPixels * 0.9);
            attributes.height = (int) (metrics.heightPixels * 0.6);
            attributes.flags = LayoutParams.FLAG_DIM_BEHIND;
            attributes.dimAmount = 0.5f;
            timeDialog.getWindow().setAttributes(attributes);
            timeDialog.show();
        }
    }

    private class MyDateTimeCallBack implements DatetimePickerCallback {
        private Button btn;
        private boolean isStartTime;

        public MyDateTimeCallBack(Button btn, boolean isStartTime) {
            this.btn = btn;
            this.isStartTime = isStartTime;
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        public void OnOK(String input) {
            if (!isStartTime) {
                if (input.compareTo(btnStartTime.getText().toString()) <= 0) {
                    ToastUtil.showShort(getString(R.string.user_sign_out_invalid_end_time));
                    return;
                }
            } else {
                if (input.compareTo(DateUtil.getCurrentTime()) <= 0) {
                    btn.setText(getString(R.string.please_select));
                    ToastUtil.showShort(getString(R.string.user_sign_in_invalid));
                    return;
                }
            }
            btn.setText(input);
        }
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.SIGN_IN_EVENT:
                switchSignState();
                ToastUtil.showShort(getString(R.string.success_submit_sign_in));
                break;
            default:
                break;
        }
    }

    private void switchSignState() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(SIGN_IN_RESULT_STRING, btnEndTime.getText().toString());
        intent.putExtras(bundle);
        setResult(SIGN_IN_RESULT_CODE, intent);
        finish();
    }
}
