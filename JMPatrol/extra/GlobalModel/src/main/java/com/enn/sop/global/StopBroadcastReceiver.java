package com.enn.sop.global;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import com.zzz.ecity.android.applibrary.MyApplication;
import com.zzz.ecity.android.applibrary.utils.ToastUtil;

/**
 * @author yongzhan
 * @date 2017/12/8
 */
public class StopBroadcastReceiver extends BroadcastReceiver {
    private Vibrator mVibrator;
    private Context mContext;

    public StopBroadcastReceiver(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mVibrator = (Vibrator) MyApplication.getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        ToastUtil.showShort("停滞太长时间");
//        Toast.makeText(mContext,"停滞太长时间",Toast.LENGTH_SHORT);
        mVibrator.vibrate(3000);
//        mVibrator.vibrate(VibrationEffect.createWaveform());
//        mVibrator.cancel();
    }
}
