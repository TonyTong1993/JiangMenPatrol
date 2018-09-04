package com.z3pipe.mobile.android.corssdk;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.LinkedBlockingQueue;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.z3pipe.mobile.android.corssdk.model.Constants;
import com.z3pipe.mobile.android.corssdk.model.EQulityType;
import com.z3pipe.mobile.android.corssdk.model.MsgInfo;
import com.z3pipe.mobile.android.corssdk.model.PTNLInfo;
import com.z3pipe.mobile.android.corssdk.model.PositionErrorStatistic;
import com.z3pipe.mobile.android.corssdk.model.SatelliteInfo;
import com.z3pipe.mobile.android.corssdk.net.NetWorkService;
import com.z3pipe.mobile.android.corssdk.util.NMEAParser;

public class CORSSDK {

	private static CORSSDK instance = null;
	private BluetoothSessionService mBluetoothSessionService = null;
	private String mConnectedDeviceName = null;
	private String mConnectedDeviceAddress = null;
	private String[] obj = null;
	private MsgInfo msgInfo = null;
	private OutputStream mOutputStream = null;
	private BluetoothSocket mSocket = null;
	private NetWorkService mNetWorkService;
	private MsgHandler mHandler = null;
	private long sizes = 0;
	private boolean connected = false;
	private LocationSimulateThread locationSimulateThread;
	private LinkedBlockingQueue<ICORSSDKCallback> listeners;
	private CORSSDK(){
		 msgInfo = new MsgInfo();
		 listeners = new LinkedBlockingQueue<ICORSSDKCallback>();
	}
	
	public static CORSSDK getInstance() {
		if (null == instance) {
			try {
				Thread.sleep(300);
				synchronized (CORSSDK.class) {
					if (null == instance) {
						instance = new CORSSDK();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return instance;
	}
	
	public boolean registerCallBack(ICORSSDKCallback listener){
		if(null == listener){
			return false;
		}
		
		if(listeners.contains(listener)){
			return false;
		}
		
		return listeners.add(listener);
	}
	
	public boolean unRegisterCallBack(ICORSSDKCallback listener){
		if(null == listener){
			return false;
		}
		
		if(!listeners.contains(listener)){
			return false;
		}
		
		return listeners.remove(listener);
	}
	
	public void setup(final Context context){
		if( null == context ){
			return;
		}
		
		if( null != mBluetoothSessionService ){
			Log.e("CORSSDK", "CORSSDK already setup");
			return;
		}
		
		mHandler = new MsgHandler();
		mBluetoothSessionService = new BluetoothSessionService(context, mHandler);
	}
	
	public void connectDevice(BluetoothDevice device) {
		if( null == device || null == mBluetoothSessionService ){
			return;
		}
		
		// Attempt to connect to the device
		mBluetoothSessionService.connect(device);
	}
	
	public void connectCORSService(final String[] params){
		if(null == params){
			return;
		}
		
		if (mNetWorkService != null) {
			mNetWorkService = null;
		}
		
		mNetWorkService = new NetWorkService(params, mHandler);
		mNetWorkService.connect2server4data();
	}
	
	public void stop(){
		if (mBluetoothSessionService != null) {
			mBluetoothSessionService.stop();
		}
		
		if (mNetWorkService != null) {
			mNetWorkService.cancleAll();
			mNetWorkService = null;
		}
		
		if (mSocket != null) {
			try {
				mSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		mBluetoothSessionService = null;
	}
	
	public BluetoothSessionService getBluetoothSessionService() {
		return mBluetoothSessionService;
	}
	
	public MsgInfo getMsgInfo() {
		return msgInfo;
	}

	public void setMsgInfo(MsgInfo msgInfo) {
		this.msgInfo = msgInfo;
	}

	public OutputStream getOutputStream() {
		return mOutputStream;
	}

	public void setOutputStream(OutputStream mOutputStream) {
		this.mOutputStream = mOutputStream;
	}

	public BluetoothSocket getSocket() {
		return mSocket;
	}

	public void setSocket(BluetoothSocket mSocket) {
		this.mSocket = mSocket;
	}

	public NetWorkService getNetWorkService() {
		return mNetWorkService;
	}
	
	public boolean isConnected(){
		return connected;
	}
	
	public String getConnectedDeviceName() {
		return mConnectedDeviceName;
	}

	public String getConnectedDeviceAddress() {
		return mConnectedDeviceAddress;
	}
	
	private static class MsgHandler extends Handler {
		MsgHandler() {
		}
		@Override
		public void handleMessage(Message msg) {
			getInstance().handleMessage(msg);
		}
	}

	private void handleMessage(android.os.Message msg) {
		switch (msg.what) {
		case Constants.MESSAGE_STATE_CHANGE:
			switch (msg.arg1) {
			case BluetoothSessionService.STATE_CONNECTED:
				onConnected(mConnectedDeviceName,mConnectedDeviceAddress);
				break;

			case BluetoothSessionService.STATE_CONNECTING:
				onConnecting();
				break;

			case BluetoothSessionService.STATE_NONE:
				onNotConnected();
				break;
			default:
				break;
			}
			break;

		case Constants.MESSAGE_DEVICE_NAME:
			mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
			onConnectedDevice(mConnectedDeviceName);
			break;

		case Constants.MESSAGE_READ:
			switch (msg.arg1) {
			case Constants.GGA:
				try {
					onReceiveNMEAData(String.valueOf(msg.obj));
					String[] metaData = ((String) msg.obj).split("\r\n");
					for (String objData : metaData) {
						String sentence = objData.substring(1, 6);
						if(sentence.endsWith("GGA")){
							SatelliteInfo satelliteInfo = new SatelliteInfo();
							//case "GPGGA":
							//case "GNGGA":
							//case "GLGGA":
							//case "GAGGA":
							//case "GBGGA":
							obj = NMEAParser.ParseGGA(objData);
							satelliteInfo.setLatitude(NMEAParser.getLatitude(obj[2]));
							satelliteInfo.setLongitude(NMEAParser
									.getLongitude(obj[4]));// 经度
							satelliteInfo.setDate(NMEAParser.getDate(obj[1]));
							satelliteInfo.setQuality(NMEAParser.getQuality(obj[6]));
							satelliteInfo.setCounts(NMEAParser.getNumber(obj[7]));
							satelliteInfo.setHdop(obj[8]);
							satelliteInfo.setAltitude(NMEAParser.getAltitude(obj[9], obj[11]));
							satelliteInfo.setDGPS(NMEAParser.getDgps(obj[13]));
							

							Location location = setLocation(Constants.PROVIDER_NAME,
									NMEAParser.parseLatitude(obj[2]),
									NMEAParser.parseLongitude(obj[4]),
									NMEAParser.string2Double(NMEAParser.getAltitude(obj[9], obj[11])), 0,
									0, 5);
							onReceiveLocation(satelliteInfo,location);
						} else if(sentence.endsWith("GST")){
							//case "GPGST":
							//case "GNGST":
							//case "GLGST":
							//case "GAGST":
							//case "GBGST":
							obj = NMEAParser.ParseGST(objData);
							PositionErrorStatistic gst = new PositionErrorStatistic();
							gst.setHorizontalError(NMEAParser.getHorizontalError(obj[6], obj[7]));
							gst.setAltitudeError(obj[8].substring(0,obj[8].indexOf("*")));
							onReceivePositionErrorStatisticInfo(gst);
						} else if(sentence.equalsIgnoreCase("PTNL,")){
							obj = NMEAParser.ParseBPQ(objData);
							if (NMEAParser.string2Double(obj[2]) != 0
									&& NMEAParser.string2Double(obj[4]) != 0) {
								try {
									PTNLInfo ptnlInfo = new PTNLInfo();
									ptnlInfo.setBaseStationHeight(obj[8].substring(3));
									ptnlInfo.setBaseStationLat(obj[4]);
									ptnlInfo.setBaseStationLong(obj[6]);
									
									ptnlInfo.setLongitude(obj[4]);
									ptnlInfo.setLatitude(obj[2]);
									onReceivePTNLInfo(ptnlInfo);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
					break;
				} catch (Exception ex) {
					ex.printStackTrace();
					onMessageToast(ex.getMessage());
				}
			}
			break;

		case Constants.MESSAGE_TOAST:
			onMessageToast(msg.getData().getString(Constants.TOAST));
			break;

		case Constants.MESSAGE_SWITCH_STATE_CHANGE:
			onMessageSwitchStateChanged("");
			break;
		case Constants.MESSAGE_DIFFERENCE:
			switch (msg.arg1) {
			case Constants.LOGIN_INFO:
				if (msg.arg2 == 200) {
					onMessageCORSLoginSuccess();
				} else {
					onMessageCORSLoginFail(Constants.MESSAGE_UNAUTHORIZED);
				}
				break;
			case Constants.DIFFERENCE_DATA_LENGTH:
				sizes += msg.arg2;
				onReceiveDateLength(String.valueOf(sizes) + " bytes");
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}
	
	private Location setLocation(String provider, double lati, double longi,
			double altitude, float bearing, float speed, int accuracy) {
		Location location = new Location(provider);
		location.setLatitude(lati);
		location.setLongitude(longi);
		location.setAltitude(altitude);
		location.setBearing(bearing);
		location.setSpeed(speed);
		location.setAccuracy(accuracy);
		location.setTime(System.currentTimeMillis());
		try {
			location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return location;
	}
	
	private void onConnected(String deviceName,String deviceAddress){
		connected = true;
		if( listeners == null || listeners.isEmpty()){
			return;
		}
		
		for (ICORSSDKCallback listener : listeners) {
			listener.onConnected(deviceName,deviceAddress);
		}
	}
	
	private void onConnecting(){
		connected = false;
		if(listeners == null || listeners.isEmpty()){
			return;
		}
		
		for (ICORSSDKCallback listener : listeners) {
			listener.onConnecting();
		}
	}
	
	private void onNotConnected(){
		connected = false;
		if(listeners == null|| listeners.isEmpty()){
			return;
		}
		
		for (ICORSSDKCallback listener : listeners) {
			listener.onNotConnected();
		}
	}
	
	private void onConnectedDevice(String connectedDeviceName){
		if(listeners == null|| listeners.isEmpty()){
			return;
		}
		
		for (ICORSSDKCallback listener : listeners) {
			listener.onConnectedDevice(connectedDeviceName);
		}
	}
	
	private void onReceiveLocation(SatelliteInfo satelliteInfo,Location location){
		if(listeners == null|| listeners.isEmpty()){
			return;
		}
		
		for (ICORSSDKCallback listener : listeners) {
			listener.onReceiveLocation(satelliteInfo,location);
		}
	}
	
	private void onReceivePositionErrorStatisticInfo(PositionErrorStatistic info){
		if(listeners == null|| listeners.isEmpty()){
			return;
		}
		
		for (ICORSSDKCallback listener : listeners) {
			listener.onReceivePositionErrorStatisticInfo(info);
		}
	}
	
	private void onReceivePTNLInfo(PTNLInfo ptnlInfo){
		if(listeners == null|| listeners.isEmpty()){
			return;
		}
		
		for (ICORSSDKCallback listener : listeners) {
			listener.onReceivePTNLInfo(ptnlInfo);
		}
	}
	
	private void onMessageToast(String message){
		if(listeners == null|| listeners.isEmpty()){
			return;
		}
		
		for (ICORSSDKCallback listener : listeners) {
			listener.onMessageToast(message);
		}
	}
	
	private void onMessageSwitchStateChanged(String message){
		if(listeners == null|| listeners.isEmpty()){
			return;
		}
		
		for (ICORSSDKCallback listener : listeners) {
			listener.onMessageSwitchStateChanged(message);
		}
	}
	
	private void onMessageCORSLoginFail(String message){
		if( listeners == null|| listeners.isEmpty()){
			return;
		}
		
		for (ICORSSDKCallback listener : listeners) {
			listener.onMessageCORSLoginFail(message);
		}
	}
	
	private void onMessageCORSLoginSuccess(){
		if( listeners == null || listeners.isEmpty()){
			return;
		}
		
		for (ICORSSDKCallback listener : listeners) {
			listener.onMessageCORSLoginSuccess();
		}
	}
	
	private void onReceiveDateLength(String dataLength){
		if(listeners == null || listeners.isEmpty()){
			return;
		}
		
		for (ICORSSDKCallback listener : listeners) {
			listener.onReceiveDateLength(dataLength);
		}
	}
	
	private void onReceiveNMEAData(String nmea){
		if( listeners == null|| listeners.isEmpty()){
			return;
		}
		
		for ( ICORSSDKCallback listener : listeners) {
			if( null != listener) {
				listener.onReceiveNMEAData(nmea);
			}
		}
	}
	
	//
	public void startSimulateLocation() {
		if(null != locationSimulateThread) {
			return;
		}
		locationSimulateThread = new LocationSimulateThread();
		locationSimulateThread.start();
	}
	
	public void stopSimulateLocation() {		
		try {
			locationSimulateThread.cancel();
			Thread.sleep(2000);
			locationSimulateThread.interrupt();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		locationSimulateThread = null;
	}
	
	private class LocationSimulateThread extends Thread {
		private boolean stopSimulate = false;
		public LocationSimulateThread() {
		}

		@Override
		public void run() {
			try {
				
				while(!stopSimulate){
					SatelliteInfo satelliteInfo = new SatelliteInfo();
					double lat = 26.05426 + Math.random();
					double lon = 119.30948 + Math.random();
					
					satelliteInfo.setLatitude(String.valueOf(lat));
					satelliteInfo.setLongitude(String.valueOf(lon));// 经度
					satelliteInfo.setDate("2017-06-17 00:00:00");
					satelliteInfo.setQuality(EQulityType.FIXED);
					satelliteInfo.setCounts("7");
					satelliteInfo.setHdop("1");
					satelliteInfo.setAltitude("1");
					satelliteInfo.setDGPS("1");
					

					Location location = setLocation(Constants.PROVIDER_NAME,
							lat,
							lon,
							10, 0,
							0, 5);
					onReceiveLocation(satelliteInfo,location);
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public void cancel() {
			stopSimulate = true;
		}
	}
}
