package com.dtr.zxing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dtr.zxing.R;
import com.dtr.zxing.camera.CameraManager;
import com.dtr.zxing.decode.DecodeThread;
import com.dtr.zxing.utils.CaptureActivityHandler;

import java.io.IOException;

import static com.dtr.zxing.activity.CaptureActivity.INTENT_KEY_BARCODE;
import static com.dtr.zxing.activity.CaptureActivity.INTENT_KEY_OPEN_LIGHT;

/**
 * Created by Administrator on 2018/1/11.
 */

public class SelfInputActivity extends Activity{



    private View closeView;

    private EditText editText;

    private View confirmView;

    private View btn_light;

    private CameraManager cameraManager;

    private boolean openLightAtFirst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selfinput);

        closeView = findViewById(R.id.btn_back);
        editText = (EditText) findViewById(R.id.edit_input);
        confirmView = findViewById(R.id.btn_ok);
        btn_light = findViewById(R.id.btn_light);

        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                String result = editText.getText().toString();
                intent.putExtra(INTENT_KEY_BARCODE, result);
                setResult(CaptureActivity.REQUEST_CODE_SELF_INPUT,intent);
                finish();
            }
        });

        btn_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraManager == null){
                    cameraManager = new CameraManager(getApplication());
                }

                if (cameraManager.isCameralightOn()){
                    cameraManager.closeCameraLight();
                }else {
                    cameraManager.openCameraLight();
                }
            }
        });

        Intent intent = getIntent();
        openLightAtFirst = intent.getBooleanExtra(INTENT_KEY_OPEN_LIGHT,false);

    }


    @Override
    protected void onPause() {

        cameraManager.closeDriver();
        super.onPause();
    }

    @Override
    protected void onResume() {

        cameraManager = new CameraManager(getApplication());

        if (cameraManager.isOpen()) {
            return;
        }
        try {
            cameraManager.openDriver(null);
            if (openLightAtFirst){
                cameraManager.openCameraLight();
                openLightAtFirst = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            displayFrameworkBugMessageAndExit();
        }

        super.onResume();
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
}
