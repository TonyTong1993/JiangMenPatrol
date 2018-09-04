package com.z3pipe.mobile.android.corssdk.model;

public class Constants {
	public static boolean DEBUG = true;
	// Message types sent from the TransmissionService Handler
	public static final int MESSAGE_TOAST = 0;
	public static final int MESSAGE_READ = 1;
	public static final int MESSAGE_STATE_CHANGE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_GGA_INFO = 5;
	public static final int MESSAGE_GST_INFO = 6;
	// public static final int MESSAGE_EERORR_INFO = 7;
	public static final int MESSAGE_SWITCH_STATE_CHANGE = 8;
	public static final int MESSAGE_DIFFERENCE = 9;
	public static final int MESSAGE_RETURN_BYTES = 10;

	// Key name received from the TransmissionService Handler
	public static final String TOAST = "toast";
	public static final String DEVICE_NAME = "device_name";
	public static final int GGA = 1;
	public static final int GST = 2;
	public static final int OTHER = 3;
	public static final int LOGIN_INFO = 4;
	public static final int DIFFERENCE_DATA_LENGTH = 5;
	
	/**
	 * location mock provider name
	 */
	public static final String PROVIDER_NAME = "z3pipeGPSProvider";
	

	public static final String MESSAGE_AUTHENCATION_SUCCSESS = "登录差分服务器成功";
	public static final String MESSAGE_UNAUTHORIZED = "该用户未授权，请重新确认";
	
	public static final String getQulityString(EQulityType qulity){
		String qulityStr = "未定位";
		switch (qulity) {
		case NOTPOSITIONED:
			qulityStr = "定未位";
			break;
		case SINGLEPOINT:
			qulityStr = "单点";		
			break;
		case PSEUDORANGE:
			qulityStr = "伪距";
			break;
		case FLOATINGPOINT:
			qulityStr = "浮点";
			break;
		case FIXED:
			qulityStr = "固定";
			break;

		default:
			break;
		}
		
		return qulityStr;
	}
}
