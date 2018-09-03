package com.zzz.ecity.android.applibrary.model;


import android.app.Notification;

/***
 * 轨迹点上报配置
 * @author ZiZhengzhuan
 *
 */
public class PositionConfig {
	public static final String ACTION_POSITION_NAME = "com.zzz.ecity.android.applibrary.service.GPSPositionBroadcastReceiver";
	public static final String ACTION_POSITION_MSG_TYPE = "msg";
	public static final String ACTION_POSITION_MSG_CONTENT = "content";
    public static int MAX_ACCURACY = 200; //最大精度，200米
    public static int MAX_DISTANCE = 50; //最大距离间隔 米
    public static int INVALIDATE_BUFFER = 500;//无效缓冲半径 米
    public static int MIN_OVERSPEEDPOINTS_NUM = 5;
    public static int POINTS_PER_MINUTE = 5;
    public static int MIN_REPORT_INTERVAL = 20;
    public static int REPORT_INTERVAL = 30;// every 30 seconds report the
	private static String reportUserId;                          // positions.
	private static String reportServerURL;                       // 上传地址
    public static final int MAX_REPORT_INTERVAL = 300;
    private static String reportPositionBeanBuilderClassName;
    private static String loationProducerClassName;
    
	private static boolean isLogLocation = false;
    private static boolean isSimulateLocation = false;

	private static Notification.Builder locationNotificationBuilder;

    public static String getReportUserId() {
		return reportUserId;
	}
    
	public static void setReportUserId(String reportUserId) {
		PositionConfig.reportUserId = reportUserId;
	}
	/***
	 * 轨迹上报地址，轨迹上报仅仅支持POST方式
	 * @return
	 */
	public static String getReportServerURL() {
		return reportServerURL;
	}

	public static void setReportServerURL(String reportServerURL) {
		PositionConfig.reportServerURL = reportServerURL;
	}
	/**
	 * 轨迹点构建器完整类名，需要继承AReportPositionBeanBuilder类
	 * @return
	 */
	public static String getReportPositionBeanBuilderClassName() {
		return reportPositionBeanBuilderClassName;
	}

	public static void setReportPositionBeanBuilderClassName(
			String reportPositionBeanBuilderClassName) {
		PositionConfig.reportPositionBeanBuilderClassName = reportPositionBeanBuilderClassName;
	}
	
	/**
	 * 模拟坐标生成器完整类名，需要继承ALoationProducer类
	 * @return
	 */
	public static String getLoationProducerClassName() {
		return loationProducerClassName;
	}
	/**
	 * 模拟坐标生成器完整类名，需要继承ALoationProducer类
	 */
	public static void setLoationProducerClassName(String loationProducerClassName) {
		PositionConfig.loationProducerClassName = loationProducerClassName;
	}

	/***
	 * 是否记录坐标到文件
	 * @return
	 */
	public static boolean isLogLocation() {
		return isLogLocation;
	}

	public static void setLogLocation(boolean isLogLocation) {
		PositionConfig.isLogLocation = isLogLocation;
	}
	/***
	 * 是否模拟轨迹，如果模拟轨迹，则需要提供坐标生成器
	 * @return
	 */
	public static boolean isSimulateLocation() {
		return isSimulateLocation;
	}

	public static void setSimulateLocation(boolean isSimulateLocation) {
		PositionConfig.isSimulateLocation = isSimulateLocation;
	}

	/**
	 * 定位状态通知中心设置
	 * @return
     */
	public static Notification.Builder getLocationNotificationBuilder() {
		return locationNotificationBuilder;
	}

	/***
	 * 定位状态通知中心设置
	 * @param locationNotificationBuilder
     */
	public static void setLocationNotificationBuilder(Notification.Builder locationNotificationBuilder) {
		PositionConfig.locationNotificationBuilder = locationNotificationBuilder;
	}
}
