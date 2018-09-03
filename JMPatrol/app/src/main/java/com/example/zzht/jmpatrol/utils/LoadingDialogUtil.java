package com.example.zzht.jmpatrol.utils;

import android.app.Activity;

import com.example.zzht.jmpatrol.global.HostApplication;
import com.zzz.ecity.android.applibrary.dialog.LoadingProgressDialog;

import java.lang.ref.WeakReference;

/**
 * @author clown
 * @date 2017/10/26
 */
public class LoadingDialogUtil {
    private static LoadingProgressDialog dialog;

    public static final void show(Activity activity, String message) {
        Activity mActivity = new WeakReference<Activity>(activity).get();
        if (dialog == null) {
            dialog = LoadingProgressDialog.createDialog(mActivity);
            dialog.setMessage(message);
            dialog.setCancelable(true);
            dialog.show();
        } else {
            updateMessage(message);
        }
    }

    public static final void show(Activity activity, int messageId) {
        Activity mActivity = new WeakReference<Activity>(activity).get();
        String message = activity.getString(messageId);
        show(mActivity, message);
    }

    public static final boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        }
        return false;
    }

    public static final void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public static final void updateMessage(String message) {
        if (dialog != null) {
            dialog.setMessage(message);
        }
    }

    public static final void updateMessage(int messageId) {
        String message = HostApplication.getApplication().getString(messageId);
        updateMessage(message);
    }

    /**
     * 设置此项,使其按手机返回键时不会消失
     */
    public static final void setCancelable(boolean isCancel) {
        if (null != dialog) {
            dialog.setCancelable(isCancel);
        }
    }
}
