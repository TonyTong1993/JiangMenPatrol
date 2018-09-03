/**************************************************************************************
 * [Project]
 *       MyProgressDialog
 * [Package]
 *       com.lxd.widgets
 * [FileName]
 *       CustomProgressDialog.java
 * [Copyright]
 *       Copyright 2012 LXD All Rights Reserved.
 * [History]
 *       Version          Date              Author                        Record
 *--------------------------------------------------------------------------------------
 *       1.0.0           2012-4-27         lxd (rohsuton@gmail.com)        Create
 **************************************************************************************/

package com.zzz.ecity.android.applibrary.dialog;

import com.zzz.ecity.android.applibrary.R;
import com.zzz.ecity.android.applibrary.view.DonutProgress;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class ProgressDialog extends Dialog {
	private DonutProgress prg_donut;
	private TextView txt_ok_cancel_dialog_cancel;
	private TextView txt_dialog_msg;
	private String title;
	private Context context;
	private OnCancleListener listener;
	private boolean cancelable = false;

	public interface OnCancleListener {
		public void back(boolean result);
	}

	public ProgressDialog(Context context, String title, OnCancleListener listener) {
		super(context, R.style.CustomProgressDialog);
		this.context = context;
		this.title = title;
		this.listener = listener;
	}

	public void setProgress(int progress) {
		if (null != prg_donut) {
			int p = progress%100;
			if(0 == p && progress>0){
				p = 100;
			}
			prg_donut.setProgress(p);
		}
	}

	@Override
	public void setCancelable(boolean flag) {
		// super.setCancelable(flag);
		this.cancelable = flag;
		if(null!= txt_ok_cancel_dialog_cancel){
			txt_ok_cancel_dialog_cancel.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.item_progressdialog);
		prg_donut = (DonutProgress) findViewById(R.id.prg_donut);
		txt_ok_cancel_dialog_cancel =(TextView) findViewById(R.id.txt_ok_cancel_dialog_cancel);
		txt_dialog_msg = (TextView) findViewById(R.id.txt_dialog_msg);
		txt_dialog_msg.setText(title);
		if(!cancelable){
			if(null!= txt_ok_cancel_dialog_cancel){
				txt_ok_cancel_dialog_cancel.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void show() {
		Window mWindow = getWindow();
		WindowManager.LayoutParams lp = mWindow.getAttributes();

		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);

		int width = dm.widthPixels;
		int height = dm.heightPixels;

		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {// 横屏
			lp.width = height / 10 * 8;
		} else {
			lp.width = width / 10 * 8;
		}
		mWindow.setAttributes(lp);

		super.show();
	}

	// 返回按钮
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (!cancelable) {
				return false;
			}

			ProgressDialog.this.dismiss();
			if (null != listener) {
				listener.back(true);
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
