package com.ecity.mobile.android.plugins.pipecross.ui;


import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ecity.mobile.android.crossanalysis.R;

public class CrossResultActivity extends Activity implements OnClickListener {
    private PipeCrossView csaview;
    public static float screenWidth;
    public static float screenHeight;
    public static float density;
    public static double qureyX1,qureyY1,qureyX2,qureyY2;
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle=getIntent().getExtras();
        qureyX1=bundle.getDouble("qureyX1");
        qureyY1=bundle.getDouble("qureyY1");
        qureyX2=bundle.getDouble("qureyX2");
        qureyY2=bundle.getDouble("qureyY2");
        // 设置成全屏模式
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        density = dm.density;
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();  
        screenHeight = getWindowManager().getDefaultDisplay().getHeight(); 

        // 加载断面分析视图
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        setContentView(R.layout.activity_cross_result);

        TextView title = (TextView) findViewById(R.id.tv_top_title);
        title.setText("断面图");
        Button btn_left = (Button) findViewById(R.id.btn_title_left);
        btn_left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        csaview = (PipeCrossView) findViewById(R.id.crossanalysisresultview);
        ButtonInit();

    }

    private void ButtonInit() {
        TextView reset = (TextView) findViewById(R.id.button_reset);
        reset.setOnClickListener(this);
        TextView zoomout = (TextView) findViewById(R.id.button_zoomout);
        zoomout.setOnClickListener(this);
        TextView zoomin = (TextView) findViewById(R.id.button_zoomin);
        zoomin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//      switch (id) {
//      // 放大
//      case R.id.button_zoomin:
//          csaview.zoomIn();
//          break;
//      // 缩小
//      case R.id.button_zoomout:
//          csaview.zoomOut();
//          break;
//      // 重置
//      case R.id.button_reset:
//          csaview.reSet();
//          break;
//      default:
//          break;
//      }
        if(id == R.id.button_zoomin)
         {
             csaview.zoomIn();
         }
         else if(id == R.id.button_zoomout)
         {
             csaview.zoomOut();
         }
         else if(id == R.id.button_reset)
         {
             csaview.reSet();
         }
    }


    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return false;
    }
}
