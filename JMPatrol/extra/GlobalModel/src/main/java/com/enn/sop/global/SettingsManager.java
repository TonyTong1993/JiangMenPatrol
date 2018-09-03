package com.enn.sop.global;

import android.content.Context;

import com.enn.sop.model.user.User;
import com.z3app.android.util.PreferencesUtil;
import com.zzz.ecity.android.applibrary.MyApplication;


/**
 * @author clown
 * @date 2017/10/26
 */

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

    private static Context context = MyApplication.getApplication();

    // Target Server begin
    public static String TARGET_SERVER_PROTOCOL = "http";
    /**
     * SOP pass平台测试环境
     */
    public static String TARGET_SERVER_PRO = "service-test-zhyytest.ipaas.enncloud.cn";
    /**
     * SOP内网测试环境
     */
    public static String TARGET_SERVER_UAT = "10.39.3.117";
    /**
     * SOP内网开发环境
     */
    public static String TARGET_SERVER_DEV = "10.39.3.54";
    /**
     * 应急开发环境
     */
    public static String TARGET_SERVER_EMM_DEV = "sop-dongguan-zhyytest.oennso.enn.cn";
    /**
     * 推广环境
     */
    public static String TARGET_SERVER_PROMOTION = "promotion-service-zhyytest.oennso.enn.cn";

    /**
     * 推广演示
     */
    public static String TARGET_SERVER_PROMOTION_DEMO = "promotion-service-zhyytest.ipaas.enncloud.cn";


    public static String TARGET_PRO_PORT = "56512";
    public static String TARGET_UAT_PORT = "8081";
    public static String TARGET_DEV_PORT = "8081";
    public static String TARGET_DEV_EMM_PORT = "18317";

    public static String TARGET_PROMOTION_PORT = "80";
    public static String TARGET_PROMOTION_DEMO_PORT = "10297";


    public static final String TARGET_VIRTUAL_PATH = "api/v1/sop";

    // iCome begin
    public static final String APP_SCHEME = "sop";
    public static final String ICOME_VALIDATION_URL = "http://icome.enn.cn/snsapi/open/validateUser.json?source=vyh8B8tgQ7IvfSC7&message=";
    // iCome end

    private SettingsManager() {
        TARGET_SERVER_PROTOCOL = "http";

        TARGET_SERVER_PRO = "service-test-zhyytest.ipaas.enncloud.cn";
//        TARGET_SERVER_EMM_DEV = "sop-dongguan-zhyytest.ipaas.enncloud.cn";
//        TARGET_SERVER_EMM_DEV = "service-emer-test-zhyytest.ipaas.enncloud.cn";
        TARGET_SERVER_EMM_DEV = "sop-dongguan-zhyytest.oennso.enn.cn";
        TARGET_SERVER_UAT = "10.39.3.117";
        TARGET_SERVER_DEV = "10.39.3.54";
        TARGET_SERVER_PROMOTION = "promotion-service-zhyytest.oennso.enn.cn";
        TARGET_SERVER_PROMOTION_DEMO = "promotion-demo-service-zhyytest.ipaas.enncloud.cn";

        TARGET_PRO_PORT = "56512";
        TARGET_UAT_PORT = "8081";
        TARGET_DEV_PORT = "8081";
        TARGET_DEV_EMM_PORT = "18317";
        TARGET_PROMOTION_PORT = "80";
        TARGET_PROMOTION_DEMO_PORT = "10297";

        loadDefaultServerIPPort();
    }

    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }

        return instance;
    }

    public boolean isXGPushRegistered(String loginName) {
        return PreferencesUtil.getBoolean(context, KEY_XG_PUSH_REGISTERED, false);
    }

    public void setXGPushRegistered(boolean isRegistered) {
        PreferencesUtil.putBoolean(context, KEY_XG_PUSH_REGISTERED, isRegistered);
    }

    public boolean isNotificationRingEnabled() {
        return true;
    }

    public boolean isNotificationVibrateEnabled() {
        return true;
    }

    public boolean isNotificationDialogEnabled(Context context) {
        //return !AppUtil.isApplicationInBackground(context.getApplicationContext());
        return false;
    }

    public void setLastUser(User user, boolean doesRememberPassword) {
        updateXGRegisterStatus(user);
        PreferencesUtil.putString(context, KEY_LAST_USER, user.getLoginName());
        if (doesRememberPassword) {
            PreferencesUtil.putString(context, KEY_LAST_PWD, user.getPassword());
        } else {
            PreferencesUtil.putString(context, KEY_LAST_PWD, "");
        }
    }

    private void updateXGRegisterStatus(User user) {
        boolean isRegistered = PreferencesUtil.getBoolean(context, KEY_XG_PUSH_REGISTERED, false);
        boolean isSameUser = getLastUser().equals(user.getLoginName());

        isRegistered = isRegistered && isSameUser;
        setXGPushRegistered(isRegistered);
    }

    public int getReportInterval() {
        return PreferencesUtil.getInt(context, KEY_REPORT_INTERVAL, 25);
    }

    public void setReportInterval(int interval) {
        PreferencesUtil.putInt(context, KEY_REPORT_INTERVAL, interval);
    }

    public String getLastUser() {
        return PreferencesUtil.getString(context, KEY_LAST_USER, "");
    }

    public void cleanUser() {
        PreferencesUtil.putString(context, KEY_LAST_USER, null);
    }

    public String getLastPWD() {
        return PreferencesUtil.getString(context, KEY_LAST_PWD);
    }

    public void cleanPWD() {
        PreferencesUtil.putString(context, KEY_LAST_PWD, null);
    }

    public int getServerType() {
        return PreferencesUtil.getInt(context, KEY_SERVER_TYPE, -1);
    }

    public void setServerType(int type) {
        PreferencesUtil.putInt(context, KEY_SERVER_TYPE, type);
    }

    public void setProtocolA(String protocol) {
        PreferencesUtil.putString(context, KEY_PROTOCOL_A, protocol);
    }

    public String getProtocolA() {
        return PreferencesUtil.getString(context, KEY_PROTOCOL_A, "");
    }

    public void setServerIPA(String ip) {
        PreferencesUtil.putString(context, KEY_SERVER_IP_A, ip);
    }

    public String getServerIPA() {
        return PreferencesUtil.getString(context, KEY_SERVER_IP_A, "");
    }

    public void setServerPortA(String port) {
        PreferencesUtil.putString(context, KEY_SERVER_PORT_A, port);
    }

    public String getServerPortA() {
        return PreferencesUtil.getString(context, KEY_SERVER_PORT_A, "");
    }

    public void setServerVirtualPathA(String path) {
        PreferencesUtil.putString(context, KEY_SERVER_VIRTUAL_PATH_A, path);
    }

    public String getServerVirtualPathA() {
        return PreferencesUtil.getString(context, KEY_SERVER_VIRTUAL_PATH_A, "");
    }

    public void setLastEnvironmentIndex(int lastIndex) {
        PreferencesUtil.putInt(context, LAST_ENVIRONMENT_INDEX, lastIndex);
    }

    public int getLastEnvironmentIndex() {
        return PreferencesUtil.getInt(context, LAST_ENVIRONMENT_INDEX, 0);
    }

    public void setProtocolB(String protocol) {
        PreferencesUtil.putString(context, KEY_PROTOCOL_B, protocol);
    }

    public String getProtocolB() {
        return PreferencesUtil.getString(context, KEY_PROTOCOL_B, "");
    }

    public void setServerIPB(String ip) {
        PreferencesUtil.putString(context, KEY_SERVER_IP_B, ip);
    }

    public String getServerIPB() {
        return PreferencesUtil.getString(context, KEY_SERVER_IP_B, "");
    }

    public void setServerPortB(String port) {
        PreferencesUtil.putString(context, KEY_SERVER_PORT_B, port);
    }

    public String getServerPortB() {
        return PreferencesUtil.getString(context, KEY_SERVER_PORT_B, "");
    }

    public void setServerVirtualPathB(String path) {
        PreferencesUtil.putString(context, KEY_SERVER_VIRTUAL_PATH_B, path);
    }

    public String getServerVirtualPathB() {
        return PreferencesUtil.getString(context, KEY_SERVER_VIRTUAL_PATH_B, "");
    }

    public void setServerTypeSetStatus(boolean isSet) {
        PreferencesUtil.putBoolean(context, KEY_IS_SERVER_TYPE_SET, isSet);
    }

    public boolean isServerTypeSet() {
        return PreferencesUtil.getBoolean(context, KEY_IS_SERVER_TYPE_SET);
    }

    /**
     * @return true, to receive notification even if App is not running <br>
     * false, do not receive notification after logout
     */
    public boolean isReceiveNotificationAfterLogout() {
        return true;
    }

    public void setRememberPassword(boolean b) {
        PreferencesUtil.putBoolean(context, KEY_REMEMBER_PASSWORD, b);
    }

    public boolean getRememberPassword() {
        return PreferencesUtil.getBoolean(context, KEY_REMEMBER_PASSWORD);
    }

    public void setAutoLogin(boolean b) {
        PreferencesUtil.putBoolean(context, KEY_AUTO_LOGIN, b);
    }

    public boolean getAutoLogin() {
        return PreferencesUtil.getBoolean(context, KEY_AUTO_LOGIN);
    }

    public void enableGPSCheckNotice(boolean isEnabled) {
        PreferencesUtil.putBoolean(context, KEY_GPS_CHECK_NOTICE, isEnabled);
    }

    public boolean isGPSCheckNoticeEnabled() {
        return PreferencesUtil.getBoolean(context, KEY_GPS_CHECK_NOTICE, true);
    }

    public void setGPSTipsSpan(long tipsSpan) {
        PreferencesUtil.putLong(context, KEY_NO_GPS_TIPS_SPAN, tipsSpan);
    }

    public void setTaskArriveTipsSpan(long tipsSpan) {
        PreferencesUtil.putLong(context, KEY_TASK_ARRIVE_TIPS_SPAN, tipsSpan);
    }

    public void enableTaskArriveNotice(boolean isEnabled) {
        PreferencesUtil.putBoolean(context, KEY_TASK_ARRIVE_NOTICE, isEnabled);
    }

    public boolean isTaskArriveNoticeEnabled() {
        return PreferencesUtil.getBoolean(context, KEY_TASK_ARRIVE_NOTICE, true);
    }

    public void enableHiddenTroubleReport(boolean isEnabled) {
        PreferencesUtil.putBoolean(context, KEY_HIDDEN_TROUBLE_REPORT, isEnabled);
    }

    public boolean isHiddenTroubleReport() {
        return PreferencesUtil.getBoolean(context, KEY_HIDDEN_TROUBLE_REPORT, true);
    }

    public void setStartManualCheckArrive(boolean isManualCheck){
        PreferencesUtil.putBoolean(context, KEY_MANUAL_CHECK_ARRIVE, isManualCheck);
    }

    public boolean isStartManualCheckArrive() {
        return PreferencesUtil.getBoolean(context, KEY_MANUAL_CHECK_ARRIVE, false);
    }

    public long getGPSTipsSpan() {
        return PreferencesUtil.getLong(context, KEY_NO_GPS_TIPS_SPAN);
    }

    public long getTaskArriveTipsSpan() {
        return PreferencesUtil.getLong(context, KEY_TASK_ARRIVE_TIPS_SPAN);
    }

    /**
     * 设置照片的保存天数
     * @param days
     */
    public void setImageKeepDays(int days) {
        PreferencesUtil.putInt(context, KEY_IMAGE_KEEP_DAYS, days);
    }

    /**
     * 获取照片的保存天数
     * @return
     */
    public int getImageKeepDays() {
        return PreferencesUtil.getInt(context, KEY_IMAGE_KEEP_DAYS);
    }

    public void setIsAlreadyReportOutOfCircle(String key, boolean isAlreadyReport) {
        PreferencesUtil.putBoolean(context, key, isAlreadyReport);
    }

    public boolean getIsAlreadyReportOutOfCircle(String key) {
        return PreferencesUtil.getBoolean(context, key, false);
    }

    public void resetDefaultServerIPPort() {
        TARGET_SERVER_PROTOCOL = "http";

        TARGET_SERVER_PRO = "backend-service-test-zhyytest.ipaas.enncloud.cn";
        TARGET_SERVER_EMM_DEV = "sop-dongguan-zhyytest.ipaas.enncloud.cn";
//        TARGET_SERVER_EMM_DEV = "service-emer-test-zhyytest.ipaas.enncloud.cn";
        TARGET_SERVER_UAT = "10.39.3.117";
        TARGET_SERVER_DEV = "10.39.3.54";

        TARGET_PRO_PORT = "37270";
        TARGET_UAT_PORT = "8081";
        TARGET_DEV_PORT = "8081";
        TARGET_DEV_EMM_PORT = "18317";

        saveDefaultServerIPPort();
    }

    public void saveDefaultServerIPPort() {

        PreferencesUtil.putString(context, "TARGET_SERVER_PROTOCOL", TARGET_SERVER_PROTOCOL);
        PreferencesUtil.putString(context, "TARGET_SERVER_PRO", TARGET_SERVER_PRO);
        PreferencesUtil.putString(context, "TARGET_SERVER_UAT", TARGET_SERVER_UAT);
        PreferencesUtil.putString(context, "TARGET_SERVER_DEV", TARGET_SERVER_DEV);
        PreferencesUtil.putString(context, "TARGET_SERVER_EMM_DEV", TARGET_SERVER_EMM_DEV);
        PreferencesUtil.putString(context, "TARGET_SERVER_PROMOTION", TARGET_SERVER_PROMOTION);
        PreferencesUtil.putString(context, "TARGET_SERVER_PROMOTION_DEMO", TARGET_SERVER_PROMOTION_DEMO);

        PreferencesUtil.putString(context, "TARGET_PRO_PORT", TARGET_PRO_PORT);
        PreferencesUtil.putString(context, "TARGET_UAT_PORT", TARGET_UAT_PORT);
        PreferencesUtil.putString(context, "TARGET_DEV_PORT", TARGET_DEV_PORT);
        PreferencesUtil.putString(context, "TARGET_DEV_EMM_PORT", TARGET_DEV_EMM_PORT);
        PreferencesUtil.putString(context, "TARGET_PROMOTION_PORT", TARGET_PROMOTION_PORT);
        PreferencesUtil.putString(context, "TARGET_PROMOTION_DEMO_PORT", TARGET_PROMOTION_DEMO_PORT);

    }

    public void loadDefaultServerIPPort() {
        TARGET_SERVER_PROTOCOL = PreferencesUtil.getString(context, "TARGET_SERVER_PROTOCOL", TARGET_SERVER_PROTOCOL);

        TARGET_SERVER_PRO = PreferencesUtil.getString(context, "TARGET_SERVER_PRO", TARGET_SERVER_PRO);
        TARGET_SERVER_UAT = PreferencesUtil.getString(context, "TARGET_SERVER_UAT", TARGET_SERVER_UAT);
        TARGET_SERVER_DEV = PreferencesUtil.getString(context, "TARGET_SERVER_DEV", TARGET_SERVER_DEV);
        TARGET_SERVER_EMM_DEV = PreferencesUtil.getString(context, "TARGET_SERVER_EMM_DEV", TARGET_SERVER_EMM_DEV);
        TARGET_SERVER_PROMOTION = PreferencesUtil.getString(context, "TARGET_SERVER_PROMOTION", TARGET_SERVER_PROMOTION);
        TARGET_SERVER_PROMOTION_DEMO = PreferencesUtil.getString(context, "TARGET_SERVER_PROMOTION_DEMO", TARGET_SERVER_PROMOTION_DEMO);

        TARGET_PRO_PORT = PreferencesUtil.getString(context, "TARGET_PRO_PORT", TARGET_PRO_PORT);
        TARGET_UAT_PORT = PreferencesUtil.getString(context, "TARGET_UAT_PORT", TARGET_UAT_PORT);
        TARGET_DEV_PORT = PreferencesUtil.getString(context, "TARGET_DEV_PORT", TARGET_DEV_PORT);
        TARGET_DEV_EMM_PORT = PreferencesUtil.getString(context, "TARGET_DEV_EMM_PORT", TARGET_DEV_EMM_PORT);
        TARGET_PROMOTION_PORT = PreferencesUtil.getString(context, "TARGET_PROMOTION_PORT", TARGET_PROMOTION_PORT);
        TARGET_PROMOTION_DEMO_PORT = PreferencesUtil.getString(context, "TARGET_PROMOTION_DEMO_PORT", TARGET_PROMOTION_DEMO_PORT);
    }
}
