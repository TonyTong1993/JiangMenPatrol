package com.ecity.cswatersupply.ui.widght;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;

public class MapNavigationView extends FrameLayout {

    public TextView tv_device;
	public TextView tv_address;
	public TextView tv_detail_address;
	private Button btn_report_even;
	private ImageButton ib_show_line;

	public MapNavigationView(Context context) {
		this(context, null);
	}

	public MapNavigationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_map_show_point_detaile, this, true);

		initView();
	}

	private void initView() {
		btn_report_even = (Button) findViewById(R.id.btn_report_event);
		ib_show_line = (ImageButton) findViewById(R.id.ib_show_line);
		tv_device = (TextView) findViewById(R.id.tv_device);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_detail_address = (TextView) findViewById(R.id.tv_detail_address);
	}
	
	public void setDeviceText(String str) {
        this.tv_device.setText(str);
    }

    public void setDeviceText(int resId) {
        this.tv_device.setText(HostApplication.getApplication()
                .getApplicationContext().getResources().getString(resId));
    }

	public void setText(String str) {
		this.tv_address.setText(str);
	}

	public void setText(int resId) {
		this.tv_address.setText(HostApplication.getApplication()
				.getApplicationContext().getResources().getString(resId));
	}
	
	public void setDetailAddressText(String str) {
        this.tv_detail_address.setText(str);
    }

    public void setDetailAddressText(int resId) {
        this.tv_detail_address.setText(HostApplication.getApplication()
                .getApplicationContext().getResources().getString(resId));
    }
    
    public void setDeviceTxtVisibility(int visibility){      
        this.tv_device.setVisibility(visibility);
    }
    
    public void setAddressTxtVisibility(int visibility){      
        this.tv_address.setVisibility(visibility);
    }
    
    public void setDetailAddressTxtVisibility(int visibility){      
        this.tv_detail_address.setVisibility(visibility);
    }
	
	public void setDetailListener(OnClickListener listener){
	    this.tv_device.setOnClickListener(listener);
	}
	
	public void setTextBackground(Drawable resId) {
		this.tv_device.setBackground(resId);
	}

	public void setOnNavigationListener(OnClickListener listener) {
		ib_show_line.setOnClickListener(listener);
	}

	public void setOnReportEventListener(OnClickListener listener) {
		btn_report_even.setOnClickListener(listener);
	}
	
	public void setButtonText(String str){
	    btn_report_even.setText(str);
	}
	
	public void setButtonText(int resId){
        btn_report_even.setText(HostApplication.getApplication()
                .getApplicationContext().getResources().getString(resId));
    }
}
