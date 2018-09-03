package com.zzz.ecity.android.applibrary.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.zzz.ecity.android.applibrary.R;
import com.zzz.ecity.android.applibrary.view.ColorPickerView;
import com.zzz.ecity.android.applibrary.view.ColorPickerView.OnColorChangedListener;

@SuppressLint("DrawAllocation")
public class ColorPickerDialog extends Dialog {
	public interface OnColorPickerDialogListener {
		public void callback(boolean result,int color);
	}
	private int initialColor;
	private OnColorPickerDialogListener listener;
	private Context context;
	public ColorPickerDialog(Context context, int initialColor,
			OnColorPickerDialogListener listener) {
		super(context,R.style.AlertView);
		this.context = context;
		this.initialColor = initialColor;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int radius = 500;
        if(width>height)
        {
        	radius = height/10*6;
        }else
        {
        	radius = width/10*6;
        }
        if(radius<500)
        	radius = 500;
        
		ColorPickerView cpView = new ColorPickerView(this.context,radius,radius,initialColor,onColorChangedListener);
		
        setContentView(cpView);
        //setTitle(title);

	}

	private OnColorChangedListener onColorChangedListener = new OnColorChangedListener(){
    	/**
    	 * 回调函数
    	 * @param color 选中的颜色
    	 */
        public void colorChanged(int color)
        {
        	if(null != listener)
        	{
				listener.callback(true,color);
				ColorPickerDialog.this.dismiss();
        	}
        }
    };
    
	@Override
	public void show() {
		Window mWindow = getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        if(context.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE){//横屏
            lp.width= height/10*8;
        }else{
            lp.width= width/10*8;
        }
        mWindow.setAttributes(lp);
		super.show();
	}
	
	// 返回按钮
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if(null != listener)
				listener.callback(false,0);
			ColorPickerDialog.this.dismiss();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
