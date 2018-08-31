package com.zzz.ecity.android.applibrary.service;

import com.zzz.ecity.android.applibrary.model.EPositionMessageType;
import com.zzz.ecity.android.applibrary.model.PositionConfig;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class APositionBaseReceiver extends BroadcastReceiver {

	@Override
	public final void onReceive(Context context, Intent intent) {
		String msgType = intent
				.getStringExtra(PositionConfig.ACTION_POSITION_MSG_TYPE);
		String msg = intent
				.getStringExtra(PositionConfig.ACTION_POSITION_MSG_CONTENT);

		switch (EPositionMessageType.getTypeByValue(msgType)) {
		case RECEIVENEWLOCATION:
			onReceiveNewLocation();
			break;
		case FILTERNEWLOCATION:
			onFilterNewLocation();
			break;
		case POSITIONREPORTSUCCESS:
			onPositionReportSuccess();
			break;
		case POSITIONREPORTFAIL:
			onPositionReportFail(msg);
			break;

		case POSITIONBEANBUILDERNOTFOUND:
			onGPSPositionBeanBuilderNotFound();
			break;
		case BUILDPOSITIONBEANERROR:
			onBuildPositionBeanError(msg);
			break;
		case POSITIONBEANSAVEFAIL:
			onPositionSaveError(msg);
			break;
		default:
			break;
		}
	}

	/**
	 * 接收到新的GPS坐标
	 */
	protected abstract void onReceiveNewLocation();

	/***
	 * 有新的坐标被筛选
	 */
	protected abstract void onFilterNewLocation();

	/**
	 * 坐标上报成功
	 */
	protected abstract void onPositionReportSuccess();

	/***
	 * 坐标上报失败
	 * 
	 * @param errorMessage
	 */
	protected abstract void onPositionReportFail(String errorMessage);

	/**
	 * GPS坐标构建器未创建
	 */
	public abstract void onGPSPositionBeanBuilderNotFound();

	/***
	 * 构建上报坐标失败
	 * 
	 * @param errorMessage
	 */
	protected abstract void onBuildPositionBeanError(String errorMessage);
	
	/***
	 * 保存坐标失败
	 * 
	 * @param errorMessage
	 */
	protected abstract void onPositionSaveError(String errorMessage);
}
