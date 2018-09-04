package com.ecity.cswatersupply.service;

import android.location.Location;
import android.util.Log;

import com.ecity.cswatersupply.utils.ArriveDetecter;
import com.ecity.cswatersupply.utils.CoordTransfer;
import com.zzz.ecity.android.applibrary.model.GPSPositionBean;
import com.zzz.ecity.android.applibrary.service.APositionBaseReceiver;
import com.zzz.ecity.android.applibrary.service.PositionService;

import java.util.concurrent.LinkedBlockingQueue;

public class PatrolPositionReceiver extends APositionBaseReceiver {
	private final static String  TAG = PatrolPositionReceiver.class.getSimpleName();

	@Override
	protected void onReceiveNewLocation() {
		Log.v(TAG,"onReceiveNewLocation");

		Location newLocation = PositionService.getLastLocation();
		if (null == newLocation) {
			return;
		}
		GPSPositionBean gpsPositionBean = new GPSPositionBean();
		gpsPositionBean.setlat(newLocation.getLatitude());
		gpsPositionBean.setlon(newLocation.getLongitude());
		double[] locationPosition = CoordTransfer.transToLocal(newLocation.getLatitude(), newLocation.getLongitude(), CoordTransfer.ETRANSTYPE.Parameters);
		gpsPositionBean.setx(locationPosition[0]);
		gpsPositionBean.sety(locationPosition[1]);
		if (null == ArriveDetecter.mLinkQueueTrackPositions) {
			ArriveDetecter.mLinkQueueTrackPositions = new LinkedBlockingQueue<GPSPositionBean>();
		}
		ArriveDetecter.mLinkQueueTrackPositions.add(gpsPositionBean);
	}

	@Override
	protected void onFilterNewLocation() {
		Log.v(TAG,"onFilterNewLocation");
	}

	@Override
	protected void onPositionReportSuccess() {
		Log.v(TAG,"onPositionReportSuccess");
	}

	@Override
	protected void onPositionReportFail(String errorMessage) {
		Log.v(TAG,"onPositionReportFail");
	}

	@Override
	public void onGPSPositionBeanBuilderNotFound() {
		Log.v(TAG,"onGPSPositionBeanBuilderNotFound");
	}

	@Override
	protected void onBuildPositionBeanError(String errorMessage) {
		Log.v(TAG,"onBuildPositionBeanError");
	}

	@Override
	protected void onPositionSaveError(String errorMessage) {
		Log.v(TAG,"onPositionSaveError");
	}

}
