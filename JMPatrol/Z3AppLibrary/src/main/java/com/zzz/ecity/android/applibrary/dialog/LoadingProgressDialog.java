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

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
/***
 * 无进度等待对话框，
 * @Description
 * @version V2.0 
 * @Author ZiZhengzhuan
 * @CreateDate 2016年1月15日
 * @email
 */
public class LoadingProgressDialog extends Dialog {
	@SuppressWarnings("unused")
	private Context context = null;
	public LoadingProgressDialog(Context context) {
		super(context);
		this.context = context;
	}
	public LoadingProgressDialog(Context context, int theme) {
		super(context, theme);
	}
	public static LoadingProgressDialog createDialog(Context context) {
		LoadingProgressDialog customProgressDialog = new LoadingProgressDialog(context,
				R.style.CustomProgressDialog);
		customProgressDialog.setContentView(R.layout.item_loadingprogressdialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		customProgressDialog.setCanceledOnTouchOutside(false);
		return customProgressDialog;
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		ImageView imageView = (ImageView) this
				.findViewById(R.id.loadingImageView);
		if(imageView==null) {
            return;
        }
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView
				.getBackground();
		animationDrawable.start();
	}
	/***
	 * [Summary] setTitile 设置标题
	 * @param strTitle
	 * @return
	 */
	public LoadingProgressDialog setTitile(String strTitle) {
		
		return this;
	}
	/**
	 * [Summary] setMessage 信息
	 * @param strMessage
	 * @return
	 * 
	 */
	public LoadingProgressDialog setMessage(String strMessage) {
		TextView tvMsg = (TextView) this
				.findViewById(R.id.id_tv_loadingmsg);
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
		return this;
	}
}
