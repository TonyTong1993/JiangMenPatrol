package com.ecity.cswatersupply.utils;

import android.content.Context;

import com.ecity.cswatersupply.model.User;
import com.z3app.android.util.PreferencesUtil;
import com.zzz.ecity.android.applibrary.MyApplication;

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


    private static Context context = MyApplication.getApplication();

    private SettingsManager() {
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
        return !AppUtil.isApplicationInBackground(context.getApplicationContext());
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

    public void enableOutOfAreaNotice(boolean isEnabled) {
        PreferencesUtil.putBoolean(context, KEY_OUT_OF_AREA_NOTICE, isEnabled);
    }

    public boolean isOutOfAreaNoticeEnabled() {
        return PreferencesUtil.getBoolean(context, KEY_OUT_OF_AREA_NOTICE, true);
    }

    public boolean setVpnSuccess(boolean b) {
        return PreferencesUtil.putBoolean(context, "KEY_VPN_SUCCESS", b);
    }

    public boolean isVpnSuccess() {
        return PreferencesUtil.getBoolean(context, "KEY_VPN_SUCCESS");
    }

    public boolean setVpnIP(String ip) {
        return PreferencesUtil.putString(context, "KEY_VPN_IP", ip);
    }

    public String getVpnIP() {
        return PreferencesUtil.getString(context, "KEY_VPN_IP", "61.154.12.43");
    }

    public boolean setVpnUserName(String username) {
        return PreferencesUtil.putString(context, "KEY_VPN_USER", username);
    }

    public String getVpnUserName() {
        return PreferencesUtil.getString(context, "KEY_VPN_USER", "众智鸿图01");
    }

    public boolean setVpnUserPsw(String password) {
        return PreferencesUtil.putString(context, "KEY_VPN_PASSWORD", password);
    }

    public String getVpnUserPsw() {
        return PreferencesUtil.getString(context, "KEY_VPN_PASSWORD", "zzht01@zzht");
    }

//    // Target Server begin
//    public static  String TARGET_SERVER_PROTOCOL = "http";
//    public static  String TARGET_SERVER_PRO = "202.168.161.166";//生产环境  联通
//    public static  String TARGET_SERVER_UAT = "113.107.139.145";//测试环境  电信
//    public static  String TARGET_SERVER_DEV = "202.168.161.166";//开发环境  预留
//    //"61.184.160.178";//孝感默认登录ip
//    public static  String TARGET_PRO_PORT = "23689";
//    public static  String TARGET_UAT_PORT = "23689";
//    public static  String TARGET_DEV_PORT = "23689";

//    // Target Server begin
//    "61.184.160.178";//孝感默认登录ip
//    public static  String TARGET_SERVER_PROTOCOL = "http";
//    public static  String TARGET_SERVER_PRO = "61.184.160.178";//生产环境  联通
//    public static  String TARGET_SERVER_UAT = "61.184.160.178";//测试环境  电信
//    public static  String TARGET_SERVER_DEV = "61.184.160.178";//开发环境  预留
//    "9999";//孝感默认登录port
//    public static  String TARGET_PRO_PORT = "9999";
//    public static  String TARGET_UAT_PORT = "9999";
//    public static  String TARGET_DEV_PORT = "9999";

    // Target Server begin
    //"168.1.9.41";//福州默认登录ip
    public static  String TARGET_SERVER_PROTOCOL = "http";
    public static  String TARGET_SERVER_PRO = "192.168.8.161";//生产环境  联通
    public static  String TARGET_SERVER_UAT = "192.168.8.161";//测试环境  电信
    public static  String TARGET_SERVER_DEV = "192.168.8.161";//开发环境  预留
    //"8787";//福州默认登录port
    public static  String TARGET_PRO_PORT = "8080";
    public static  String TARGET_UAT_PORT = "8080";
    public static  String TARGET_DEV_PORT = "8080";
    public static final String TARGET_VIRTUAL_PATH = "";

    public void resetDefaultServerIPPort() {
        TARGET_SERVER_PROTOCOL = "http";
        TARGET_SERVER_PRO = "221.4.189.166";//生产环境  联通
        TARGET_SERVER_UAT = "113.107.139.145";//测试环境  电信
        TARGET_SERVER_DEV = "221.4.189.166";//开发环境  预留
        TARGET_PRO_PORT = "23689";
        TARGET_UAT_PORT = "23689";
        TARGET_DEV_PORT = "23689";
        saveDefaultServerIPPort();
    }

    public void saveDefaultServerIPPort() {
        PreferencesUtil.putString(context, "TARGET_SERVER_PROTOCOL", TARGET_SERVER_PROTOCOL);
        PreferencesUtil.putString(context, "TARGET_SERVER_PRO", TARGET_SERVER_PRO);
        PreferencesUtil.putString(context, "TARGET_SERVER_UAT", TARGET_SERVER_UAT);
        PreferencesUtil.putString(context, "TARGET_SERVER_DEV", TARGET_SERVER_DEV);

        PreferencesUtil.putString(context, "TARGET_PRO_PORT", TARGET_PRO_PORT);
        PreferencesUtil.putString(context, "TARGET_UAT_PORT", TARGET_UAT_PORT);
        PreferencesUtil.putString(context, "TARGET_DEV_PORT", TARGET_DEV_PORT);
    }

    public void loadDefaultServerIPPort() {
        TARGET_SERVER_PROTOCOL = PreferencesUtil.getString(context, "TARGET_SERVER_PROTOCOL", TARGET_SERVER_PROTOCOL);

        TARGET_SERVER_PRO = PreferencesUtil.getString(context, "TARGET_SERVER_PRO", TARGET_SERVER_PRO);
        TARGET_SERVER_UAT = PreferencesUtil.getString(context, "TARGET_SERVER_UAT", TARGET_SERVER_UAT);
        TARGET_SERVER_DEV = PreferencesUtil.getString(context, "TARGET_SERVER_DEV", TARGET_SERVER_DEV);

        TARGET_PRO_PORT = PreferencesUtil.getString(context, "TARGET_PRO_PORT", TARGET_PRO_PORT);
        TARGET_UAT_PORT = PreferencesUtil.getString(context, "TARGET_UAT_PORT", TARGET_UAT_PORT);
        TARGET_DEV_PORT = PreferencesUtil.getString(context, "TARGET_DEV_PORT", TARGET_DEV_PORT);
    }
}
