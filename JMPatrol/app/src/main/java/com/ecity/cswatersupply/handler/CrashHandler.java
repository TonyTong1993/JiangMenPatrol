package com.ecity.cswatersupply.handler;

import android.content.Context;
import android.os.Debug;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.Toast;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler instance;
    private Context mContext;
    static {
        instance = new CrashHandler();
    }

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return instance;
    }

    public void init(Context context) {
        mContext = context;
        Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable e) {
        LogUtil.e(this, e);
        final String msg = e.getLocalizedMessage();

        //用HandlerThread升级Looper线程，执行多条消息
        HandlerThread errorThread = new HandlerThread("error");
        errorThread.start();

        final Handler mHandler = new Handler(errorThread.getLooper());
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (Debug.isDebuggerConnected()) {
                    Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "程序出错啦", Toast.LENGTH_LONG).show();
                }
                mHandler.postDelayed(finishProgram, 3 * 1000);
            }
        });
    }

    Runnable finishProgram = new Runnable() {

        @Override
        public void run() {
            if (mContext != null) {
                HostApplication.getApplication().doOtherthingBeforeExist(mContext);
                HostApplication.getApplication().getAppManager().AppExit(mContext, false);
            } else {
                HostApplication.getApplication().getAppManager().finishAllActivity();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    };
}
