package com.ecity.cswatersupply.emergency.view;

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

import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.R;

public class CustomAlertDialog extends Dialog {
    public interface OnCustomDialogListener {
        void back(boolean result);
    }

    public enum DialogStyle {
        OK, OK_CANCEL, YESNO
    }

    private String title;
    private String msg;
    private OnCustomDialogListener customDialogListener;
    private DialogStyle style;
    private Context context;
    private boolean isSystemAlert;
    private String positiveBtnTitle;
    private String negativeBtnTitle;
    private TextView btnOK;
    private TextView btnCancel;

    public CustomAlertDialog(Context context, String title, String msg, OnCustomDialogListener customDialogListener, DialogStyle style) {
        super(context, R.style.CustomProgressDialog);
        this.context = context;
        this.title = title;
        this.msg = msg;
        this.customDialogListener = customDialogListener;
        this.style = style;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initUI();
    }

    @Override
    public void show() {
        Window mWindow = getWindow();
        if (isSystemAlert) {
            mWindow.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        WindowManager.LayoutParams lp = mWindow.getAttributes();

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            lp.width = height / 10 * 8;
        } else {
            lp.width = width / 10 * 8;
        }
        mWindow.setAttributes(lp);

        super.show();
    }

    private void initUI() {
        setContentView(R.layout.item_ok_cancel_dialog);
        ((TextView) findViewById(R.id.tv_title)).setText(title);
        ((TextView) findViewById(R.id.tv_ok_cancel_dialog_Tips)).setText(msg);
        btnOK = (TextView) findViewById(R.id.txt_ok_cancel_dialog_ok);
        btnCancel = (TextView) findViewById(R.id.txt_ok_cancel_dialog_cancel);
        btnOK.setText(positiveBtnTitle);
        btnCancel.setText(negativeBtnTitle);
        ((TextView) findViewById(R.id.tv_ok_cancel_dialog_Tips)).setText(msg);

        if (style == DialogStyle.OK) {
            btnCancel.setVisibility(View.GONE);
            findViewById(R.id.txt_btn_divider).setVisibility(View.GONE);
        }
        btnOK.setOnClickListener(clickOkListener);
        btnCancel.setOnClickListener(clickCancelListener);
    }

    private View.OnClickListener clickOkListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (null != customDialogListener)
                customDialogListener.back(true);
            CustomAlertDialog.this.dismiss();
        }
    };

    private View.OnClickListener clickCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null != customDialogListener)
                customDialogListener.back(false);
            CustomAlertDialog.this.dismiss();
        }
    };

    // 返回按钮
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (null != customDialogListener)
                customDialogListener.back(true);
            CustomAlertDialog.this.dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean isSystemAlert() {
        return isSystemAlert;
    }

    /**
     * Decide if this dialog should be displayed independently from Activity.
     * @param isSystemAlert set true if this dialog is launched from a Service.
     */
    public void setSystemAlert(boolean isSystemAlert) {
        this.isSystemAlert = isSystemAlert;
    }

    public void setPositiveButtonTitle(String title) {
        if (StringUtil.isBlank(title)) {
            return;
        }

        this.positiveBtnTitle = title;
    }

    public void setNegativeButtonTitle(String title) {
        if (StringUtil.isBlank(title)) {
            return;
        }

        this.negativeBtnTitle = title;
    }
}
