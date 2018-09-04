package com.ecity.mobile.android.crossanalysis;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class CrossAnalysisResultActivity extends Activity implements OnClickListener{	
	private       CrossAnalysisResultView csaview;
	public static float                   screenWidth;
	public static float                   screenHeight;
	public static float                   density ;

	private Button btn_title_left,btn_title_right;
	private TextView tv_top_title;

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
		      //从布局加载断面分析视图

	      //设置成全屏模式 	     
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        DisplayMetrics dm = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(dm);
	        density =  dm.density;
	        screenWidth  = getWindowManager().getDefaultDisplay()
					.getWidth(); // 屏幕宽（像素，如：480px）
	        screenHeight = getWindowManager().getDefaultDisplay() 
					.getHeight(); // 屏幕高（像素，如： 800px )	       

	      //加载断面分析视图	 
	        setContentView(R.layout.activity_crossanalysisresult);
	        csaview = (CrossAnalysisResultView)findViewById(R.id.crossanalysisresultview);
	        ButtonInit();
	        
			tv_top_title = (TextView) findViewById(R.id.tv_top_title);
			btn_title_left = (Button) findViewById(R.id.btn_title_left);
			btn_title_left.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {

					finish();
				}
			});
			
//			btn_title_right = (Button) findViewById(R.id.btn_title_right);
//			btn_title_right.setVisibility(View.GONE);
	        tv_top_title.setText("断面分析结果");
	 }

	private void ButtonInit(){
        TextView querybtn = (TextView)findViewById(R.id.button_qurey);	      
        querybtn.setOnClickListener(this);
        TextView reset = (TextView)findViewById(R.id.button_reset);	      
        reset.setOnClickListener(this);
        TextView zoomout = (TextView)findViewById(R.id.button_zoomout);   
        zoomout.setOnClickListener(this);
	    TextView zoomin = (TextView)findViewById(R.id.button_zoomin);
	    zoomin.setOnClickListener(this);
	 }
	 
	 @Override
	public void onClick(View v){
		 int id = v.getId();
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
		 else if(id == R.id.button_qurey)
		 {
			 QueryDialog();
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
	
	private void QueryDialog(){
			CrossAnalysisAlertDialog csa_alertdialog = new CrossAnalysisAlertDialog(CrossAnalysisResultActivity.this, csaview, "查询管线信息");
			csa_alertdialog.setCanceledOnTouchOutside(true);//单击对话框外边消失
			csa_alertdialog.show();			
		}		
	public boolean onKeyDown(int keyCode,KeyEvent event){
		if (keyCode==KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0)
		{
			finish();
			return true;
		}
		return false;
		}
}
