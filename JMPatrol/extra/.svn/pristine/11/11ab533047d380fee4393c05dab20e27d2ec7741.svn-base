package com.z3pipe.mobile.android.corssdk;

import com.z3pipe.mobile.android.corssdk.model.PTNLInfo;
import com.z3pipe.mobile.android.corssdk.model.PositionErrorStatistic;
import com.z3pipe.mobile.android.corssdk.model.SatelliteInfo;

import android.location.Location;

public interface ICORSSDKCallback {
	public void onConnected(String deviceName,String deviceAddress);
	public void onConnecting();
	public void onNotConnected();
	public void onConnectedDevice(String connectedDeviceName);
	public void onReceiveLocation(SatelliteInfo satelliteInfo,Location location);
	public void onReceivePositionErrorStatisticInfo(PositionErrorStatistic info);
	public void onReceivePTNLInfo(PTNLInfo ptnlInfo);
	public void onMessageToast(String message);
	public void onMessageSwitchStateChanged(String message);
	public void onMessageCORSLoginFail(String message);
	public void onMessageCORSLoginSuccess();
	public void onReceiveDateLength(String dataLength);
	public void onReceiveNMEAData(String nmea);
}
