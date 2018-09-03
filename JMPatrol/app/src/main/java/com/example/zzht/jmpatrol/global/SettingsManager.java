package com.example.zzht.jmpatrol.global;

import android.content.Context;

public class SettingsManager {
    private static SettingsManager instance;
    private static final String KEY_SERVER_TYPE = "KEY_SERVER_TYPE";
    private static final String KEY_LAST_USER = "KEY_LAST_USER";
    private static final String KEY_LAST_PWD = "KEY_LAST_PWD";
    private static final String KEY_PROTOCOL_A = "KEY_PROTOCOL_A";
    private static final String KEY_SERVER_IP_A = "KEY_SERVER_IP_A";
    private static final String KEY_SERVER_PORT_A = "KEY_SERVER_PORT_A";
    private static final String KEY_SERVER_VIRTUAL_PATH_A = "KEY_SERVER_VIRTUAL_PATH_A";
    private static final String LAST_ENVIRONMENT_INDEX = "LAST_ENVIRONMENT_INDEX";
    private static final String KEY_PROTOCOL_B = "KEY_PROTOCOL_B";
    private static final String KEY_SERVER_IP_B = "KEY_SERVER_IP_B";
    private static final String KEY_SERVER_PORT_B = "KEY_SERVER_PORT_B";
    private static final String KEY_SERVER_VIRTUAL_PATH_B = "KEY_SERVER_VIRTUAL_PATH_B";
    private static final String KEY_REMEMBER_PASSWORD = "KEY_REMEMBER_PASSWORD";
    private static final String KEY_AUTO_LOGIN = "KEY_AUTO_LOGIN";

    private static final String KEY_XG_PUSH_REGISTERED = "KEY_XG_PUSH_REGISTERED";
    private static final String KEY_REPORT_INTERVAL = "KEY_REPORT_INTERVAL";
    private static final String KEY_OUT_OF_AREA_NOTICE = "KEY_OUT_OF_AREA_NOTICE";
    private static final String KEY_IS_SERVER_TYPE_SET = "KEY_IS_SERVER_TYPE_SET";
    private static final String KEY_NO_GPS_TIPS_SPAN = "KEY_NO_GPS_TIPS_SPAN";
    private static final String KEY_GPS_CHECK_NOTICE = "KEY_GPS_CHECK_NOTICE";
    private static final String KEY_TASK_ARRIVE_NOTICE = "KEY_TASK_ARRIVE_NOTICE";

    private static final String KEY_TASK_ARRIVE_TIPS_SPAN = "KEY_TASK_ARRIVE_TIPS_SPAN";
    private static final String KEY_IMAGE_KEEP_DAYS = "KEY_IMAGE_KEEP_DAYS";

    private static final String KEY_HIDDEN_TROUBLE_REPORT = "KEY_HIDDEN_TROUBLE_REPORT";

    private static final String KEY_MANUAL_CHECK_ARRIVE = "KEY_MANUAL_CHECK_ARRIVE";

    private static Context context = HostApplication.getApplication();

    // Target Server begin
    public static String TARGET_SERVER_PROTOCOL = "http";
    /**
     * jiangmen pass平台测试环境
     */
    public static String TARGET_SERVER_PRO = "";
    /**
     * jiangmen内网测试环境
     */
    public static String TARGET_SERVER_UAT = "1192.168.8.161";
    /**
     * jiangmen内网开发环境
     */
    public static String TARGET_SERVER_DEV = "192.168.8.161";


    public static String TARGET_PRO_PORT = "8080";
    public static String TARGET_UAT_PORT = "8080";
    public static String TARGET_DEV_PORT = "8080";
    public static String TARGET_DEV_EMM_PORT = "8080";

    public static String TARGET_PROMOTION_PORT = "8080";
    public static String TARGET_PROMOTION_DEMO_PORT = "8080";


    public static final String TARGET_VIRTUAL__PATH = "api/v1.1/omsService";

    private SettingsManager() {
        TARGET_SERVER_PROTOCOL = "http";

        TARGET_SERVER_PRO = "";
        TARGET_SERVER_UAT = "192.168.8.161";
        TARGET_SERVER_DEV = "192.168.8.161";

        TARGET_PRO_PORT = "8080";
        TARGET_UAT_PORT = "8080";
        TARGET_DEV_PORT = "8080";
        TARGET_DEV_EMM_PORT = "8080";
        TARGET_PROMOTION_PORT = "8080";
        TARGET_PROMOTION_DEMO_PORT = "8080";

    }

    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }

        return instance;
    }


}
