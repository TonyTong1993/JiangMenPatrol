package com.ecity.cswatersupply.emergency.menu.operator;

import java.lang.ref.WeakReference;
import java.util.List;

import android.os.Bundle;

import com.ecity.cswatersupply.emergency.activity.EarthQuakeBaseReportActivity;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;

public abstract class AEarthQuakeCommonReportOperator {
	private WeakReference<EarthQuakeBaseReportActivity> mActivity;

	public final void setCustomActivity(EarthQuakeBaseReportActivity activity) {
		mActivity = new WeakReference<EarthQuakeBaseReportActivity>(activity);
	}

	public EarthQuakeBaseReportActivity getActivity() {
		if (null == mActivity) {
			return null;
		}
		EarthQuakeBaseReportActivity activity = mActivity.get();
		return activity;
	}

	public List<InspectItem> getDataSource() {
		return null;
	}

	public void submit2Server(List<InspectItem> datas) {

	}

	/**
	 * 用于判断的页面
	 */
	public void submit2Server(List<InspectItem> datas, boolean isAgree) {

	}

	public void notifyBackEvent(EarthQuakeBaseReportActivity activity) {

	}

	public void nofityBackEventWhenFinishReport(EarthQuakeBaseReportActivity activity) {

	}

	/**
	 * 自定义实现点击事件
	 * 
	 * @param itemName
	 *            区分item的key
	 * @param eInspectItemType
	 * @param data
	 *            回调数据包
	 */
	public void onItemClicked(String itemName,
			EInspectItemType eInspectItemType, Bundle data) {

	}
}
