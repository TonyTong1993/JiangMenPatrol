package com.ecity.cswatersupply.ui.widght;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecity.cswatersupply.R;

public class MapNavigationFlexflowView extends RelativeLayout {

	public TextView tv_flexflow;
	public Button btn_flexflowreport_backup;
	public Button btn_flexflowreport_event;
	public ImageButton ib_flexflow_show_line;

	public MapNavigationFlexflowView(Context context) {
		this(context, null);
	}

	public MapNavigationFlexflowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(
				R.layout.view_map_show_point_flexflow_detaile, this, true);

		initView();
	}

	private void initView() {
		
	    tv_flexflow = (TextView) findViewById(R.id.tv_flexflow);
		btn_flexflowreport_backup = (Button) findViewById(R.id.btn_flexflowreport_backup);
		btn_flexflowreport_event = (Button) findViewById(R.id.btn_flexflowreport_event);
		ib_flexflow_show_line = (ImageButton) findViewById(R.id.ib_flexflow_show_line);
	}

	public void setStatusText(String str) {
		this.tv_flexflow.setText(str);
	}
	
	public void setStatusBackground(Drawable resId){
	    this.tv_flexflow.setBackground(resId);
	}
	
	public void setBackUpBtnEnable(boolean b){
		btn_flexflowreport_backup.setEnabled(b);		
	}

	public void setOnNavigationListener(OnClickListener listener) {
		ib_flexflow_show_line.setOnClickListener(listener);
	}

	public void setOnReportEventListener(OnClickListener listener) {
		btn_flexflowreport_event.setOnClickListener(listener);
	}
	
	public void setOnBackUpListener(OnClickListener listener) {
		btn_flexflowreport_backup.setOnClickListener(listener);
	}
	
	public void setOnDetailsListener(OnClickListener listener) {
	    tv_flexflow.setOnClickListener(listener);
	}
}
