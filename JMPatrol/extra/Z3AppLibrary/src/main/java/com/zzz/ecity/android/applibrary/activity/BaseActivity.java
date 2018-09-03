package com.zzz.ecity.android.applibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.z3app.android.app.Z3Application;
import com.zzz.ecity.android.applibrary.dialog.AlertView;
import com.zzz.ecity.android.applibrary.dialog.AlertView.AlertStyle;
import com.zzz.ecity.android.applibrary.dialog.LoadingProgressDialog;

public abstract class BaseActivity extends Activity {

	private LoadingProgressDialog loadingDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		notifiyApplicationActivityCreating();
		onPreOnCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		
	    if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0)
	    {
	    	finish();
	    	return;
	    }
	    
	    getZ3Application().getAppManager().addActivity(this);
		initActivity();
		onAfterOnCreate(savedInstanceState);
		notifiyApplicationActivityCreated();
	}
	
	@Override
	protected void onDestroy() {
		if(null != loadingDialog)
		{
			loadingDialog.cancel();
			loadingDialog.dismiss();
		}
		loadingDialog = null;
		super.onDestroy();
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
	}

	@Override
	public void finish() {
		getZ3Application().getAppManager().removeActivity(this);
		super.finish();
	}
	/***
	 * *
	 * Description:<br/>
	 * @return
	 * @version V1.0
	 */
	public Z3Application getZ3Application()
	{
		return Z3Application.getApplication();
	}
	/***
	 * *
	 * Description:<br/>
	 * @version V1.0
	 */
	private void initActivity() {

	}
	/***
	 * creat LoadingDialog
	 */
	public LoadingProgressDialog creatLoadingDialog(String msg,boolean cancelable,boolean canceledOnTouchOutside)
	{
		LoadingProgressDialog dialog = LoadingProgressDialog.createDialog(this);
		dialog.setMessage(msg);
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
		return dialog;
	}
	/**
	 * *
	 * Description:<br/> show LoadingDialog
	 * @param msg
	 * @param cancelable
	 * @param canceledOnTouchOutside
	 * @version V1.0
	 */
	final protected void showLoadingDialog(String msg,boolean cancelable,boolean canceledOnTouchOutside)
	{
		if(null == loadingDialog) {
            loadingDialog = creatLoadingDialog(msg, cancelable, canceledOnTouchOutside);
        } else
		{
			loadingDialog.setMessage(msg);
			loadingDialog.setCancelable(cancelable);
			loadingDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
		}
		loadingDialog.show();
	}
	final protected void dismissLoadingDialog()
	{
		if(null != loadingDialog) {
            loadingDialog.hide();
        }
	}
	
	/***
	 * show Toast Message
	 * @param msg
	 */
	final protected void showToastMessage(String msg)
	{
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	/***
	 * show Alert Dialog
	 */
	final protected void showAlertDialog(String title,String msg,AlertStyle style)
	{
		AlertView dialog = new AlertView(this, title,
				msg, new AlertView.OnAlertViewListener() {
					@Override
					public void back(boolean result) {
						
					}
				}, style);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	/***
	 * *
	 * Description:<br/>
	 * @version V1.0
	 */
	private void notifiyApplicationActivityCreating() {

	}
	/***
	 * *
	 * Description:<br/>
	 * @version V1.0
	 */
	private void notifiyApplicationActivityCreated() {
		
	}
	/***
	 * *
	 * Description:<br/>
	 * @param savedInstanceState
	 * @version V1.0
	 */
	protected void onPreOnCreate(Bundle savedInstanceState) {
		
	}
	/**
	 * *
	 * Description:<br/>
	 * @param savedInstanceState
	 * @version V1.0
	 */
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		
	}
	/***
	 * *
	 * Description:<br/>
	 * @param isBackground
	 * @version V1.0
	 */
	public void exitApp(Boolean isBackground) {
		getZ3Application().exitApp(isBackground);
	}
	/***
	 * *
	 * Description:<br/>
	 * @version V1.0
	 */
	public void exitApp() {
		getZ3Application().exitApp(false);
	}
	/***
	 * *
	 * Description:<br/>
	 * @version V1.0
	 */
	public void exitAppToBackground() {
		getZ3Application().exitApp(true);
	}
}