package com.ecity.cswatersupply.utils;

import java.util.List;

import android.content.Context;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.menu.AppMenu;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.response.loginresponse.MobileConfig;
import com.z3app.android.util.PreferencesUtil;

/**
 * Used for restoring context when receiving a notification while APP is NOT
 * running.
 * 
 * @author sunshanai@126.com
 *
 */
public class RestoreManager {
    private static final String KEY_LAST_USER_JSON = "KEY_LAST_USER_JSON";
    private static final String KEY_AVAILABLE_MENUS = "KEY_AVAILABLE_MENUS";
    private static final String KEY_MOBILE_MAP_CONFIG = "KEY_MOBILE_MAP_CONFIG";

    private static RestoreManager instance;
    private static Context context = HostApplication.getApplication();

    private RestoreManager() {
    }

    public static RestoreManager getInstance() {
        if (instance == null) {
            instance = new RestoreManager();
        }

        return instance;
    }

    public void saveLastUser(User user) {
        try {
            PreferencesUtil.putString(context, KEY_LAST_USER_JSON, GsonUtil.toJson(user));
        } catch (Exception e) {
            LogUtil.e(this, e);
        }
    }

    public User restoreLastUser() {
        String json = PreferencesUtil.getString(context, KEY_LAST_USER_JSON, "");
        try {
            return GsonUtil.toObject(json, User.class);
        } catch (Exception e) {
            LogUtil.e(this, e);
            return null;
        }
    }

    public void saveAvailableMenus(List<AppMenu> menuNames) {
        try {
            PreferencesUtil.putString(context, KEY_AVAILABLE_MENUS, GsonUtil.toJson(menuNames));
        } catch (Exception e) {
            LogUtil.e(this, e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<AppMenu> restoreAvailableMenus() {
        String json = PreferencesUtil.getString(context, KEY_AVAILABLE_MENUS, "");
        try {
            return GsonUtil.toObject(json, List.class);
        } catch (Exception e) {
            LogUtil.e(this, e);
            return null;
        }
    }

    public void saveMobileMapConfig(MobileConfig config) {
        try {
            PreferencesUtil.putString(context, KEY_MOBILE_MAP_CONFIG, GsonUtil.toJson(config));
        } catch (Exception e) {
            LogUtil.e(this, e);
        }
    }

    public MobileConfig restoreMobileMapConfig() {
        String json = PreferencesUtil.getString(context, KEY_MOBILE_MAP_CONFIG);
        try {
            return GsonUtil.toObject(json, MobileConfig.class);
        } catch (Exception e) {
            LogUtil.e(this, e);
            return null;
        }
    }
}
