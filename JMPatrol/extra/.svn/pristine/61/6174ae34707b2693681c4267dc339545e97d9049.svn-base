/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dtr.zxing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dtr.zxing.R;
import com.dtr.zxing.camera.CameraManager;
import com.dtr.zxing.decode.DecodeThread;
import com.dtr.zxing.utils.BeepManager;
import com.dtr.zxing.utils.CaptureActivityHandler;
import com.dtr.zxing.utils.InactivityTimer;
import com.dtr.zxing.utils.StatusBarCompat;
import com.google.zxing.Result;

import java.io.IOException;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback {

    public static final int REQUEST_CODE_SCAN_BARCODE = 0;

    public static final String INTENT_KEY_BARCODE = "INTENT_KEY_BARCODE";

    public static final String INTENT_KEY_RESULT_ACTIVITY_CLASS = "INTENT_KEY_RESULT_ACTIVITY_CLASS";

    /**
     * 如果要想在二维码扫描页面增加手动输入功能，请在打开 CaptureActivity的intent中putExtra此项，至于对应的extra类型不限制，不为空即可。
     */
    public static final String INTENT_KEY_SELFINPUT ="INTENT_KEY_SELFINPUT";

    public static final int REQUEST_CODE_SELF_INPUT = 1;

    public static final String INTENT_KEY_OPEN_LIGHT="INTENT_KEY_DEFAULT_OPEN_LIGHT";

    private static final String TAG = CaptureActivity.class.getSimpleName();

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;

    private SurfaceView scanPreview = null;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;
    private Rect mCropRect = null;
    private String resultActivityClsName;
    private boolean isHasSurface = false;



    private View btnClose;
    private ImageView imgInput;
    private ImageView imgLight;
    private TextView  tvLight;

    private ViewGroup layout_input;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Object selfInput = null;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            selfInput = bundle.get(INTENT_KEY_SELFINPUT);
        }


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarUpperAPI21();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setStatusBarUpperAPI19();
        }
        StatusBarCompat.translucentStatusBar(this,true);

        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);

        initOperateView(selfInput != null);
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);
        resultActivityClsName = getIntent().getStringExtra(INTENT_KEY_RESULT_ACTIVITY_CLASS);
        if (TextUtils.isEmpty(resultActivityClsName)) {
            resultActivityClsName = "";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(scanPreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            scanPreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     * 
     * @param rawResult
     *            The contents of the barcode.
     * 
     * @param bundle
     *            The extras
     */
    public void handleDecode_bak(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();

        bundle.putInt("width", mCropRect.width());
        bundle.putInt("height", mCropRect.height());
        bundle.putString("result", rawResult.getText());

        Class<?> cls = ResultActivity.class;
        try {
            cls = Class.forName(resultActivityClsName);
            if (!cls.isAssignableFrom(Activity.class)) { // Make sure the it is a Activity, otherwise App will crash when calling startActivity.
                cls = ResultActivity.class;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(CaptureActivity.this, cls).putExtras(bundle));
    }

    public void handleDecode(Result rawResult, Bundle bundle) {
        String result = rawResult.getText();
        Intent intent = new Intent();
        intent.putExtra(INTENT_KEY_BARCODE, result);
        setResult(REQUEST_CODE_SCAN_BARCODE, intent);
        finish();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("相机打开出错，请稍后重试");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    private void initOperateView(boolean selfInput){



        btnClose = findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        layout_input = (ViewGroup) findViewById(R.id.layout_input);
        imgInput = (ImageView) findViewById(R.id.btn_input);
        imgInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开手动输入窗口
                Intent intent = new Intent(CaptureActivity.this,SelfInputActivity.class);
                intent.putExtra(INTENT_KEY_OPEN_LIGHT,cameraManager.isCameralightOn());
                startActivityForResult(intent,REQUEST_CODE_SELF_INPUT);
            }
        });
        if (!selfInput){
            layout_input.setVisibility(View.GONE);
        }


         imgLight = (ImageView) findViewById(R.id.btn_light);
         imgLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraManager.isCameralightOn()){
                    //当前闪光灯打开了。点击button是关闭。
                    cameraManager.closeCameraLight();
                    tvLight.setText(R.string.light_off);
                    imgLight.setImageResource(R.drawable.css_light_off);
                }else {
                    cameraManager.openCameraLight();
                    tvLight.setText(R.string.light_on);
                    imgLight.setImageResource(R.drawable.css_light_on);
                }
            }
        });
         tvLight = (TextView) findViewById(R.id.label_light);

    }



    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

//    private int getStatusBarHeight() {
//        try {
//            Class<?> c = Class.forName("com.android.internal.R$dimen");
//            Object obj = c.newInstance();
//            Field field = c.getField("status_bar_height");
//            int x = Integer.parseInt(field.get(obj).toString());
//            return getResources().getDimensionPixelSize(x);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }


    private void setStatusBarUpperAPI21() {
        Window window = getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }
    }

    // 4.4 - 5.0版本
    private void setStatusBarUpperAPI19() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View statusBarView = mContentView.getChildAt(0);
        //移除假的 View
        if (statusBarView != null && statusBarView.getLayoutParams() != null &&
                statusBarView.getLayoutParams().height == getStatusBarHeight()) {
            mContentView.removeView(statusBarView);
        }
        //不预留空间
        if (mContentView.getChildAt(0) != null) {
            ViewCompat.setFitsSystemWindows(mContentView.getChildAt(0), false);
        }
    }

    private int getStatusBarHeight(){
        int result = 0;
        int resId = getResources().getIdentifier("status_bar_height","dimen","android");
        if(resId>0){
            result = getResources().getDimensionPixelSize(resId);
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELF_INPUT){
            if (resultCode == REQUEST_CODE_SELF_INPUT){

                if (data !=null){
                    String result = data.getStringExtra(INTENT_KEY_BARCODE);
                    Intent intent = new Intent();
                    intent.putExtra(INTENT_KEY_BARCODE, result);
                    setResult(REQUEST_CODE_SCAN_BARCODE, intent);
                }

                finish();

            }
        }
    }
}