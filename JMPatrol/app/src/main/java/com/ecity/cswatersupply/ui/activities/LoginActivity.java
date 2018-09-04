package com.ecity.cswatersupply.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.config.MobileConfigParser;
import com.ecity.cswatersupply.emergency.EQModuleConfig;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.menu.AMenu.EMenuType;
import com.ecity.cswatersupply.menu.AppMenu;
import com.ecity.cswatersupply.menu.MenuFactory;
import com.ecity.cswatersupply.model.AppVerionInfo;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.response.loginresponse.LoginResponse;
import com.ecity.cswatersupply.network.response.loginresponse.LoginTokenResponse;
import com.ecity.cswatersupply.network.response.loginresponse.MobileConfig;
import com.ecity.cswatersupply.project.FuZhouPatrolModuleConfig;
import com.ecity.cswatersupply.project.ProjectModuleConfig;
import com.ecity.cswatersupply.service.AppStatusService;
import com.ecity.cswatersupply.service.AppVersionCheckRunnable;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.service.UserService;
import com.ecity.cswatersupply.task.TransparamsLoader;
import com.ecity.cswatersupply.utils.AppUtil;
import com.ecity.cswatersupply.utils.ArriveDetecter;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.RestoreManager;
import com.ecity.cswatersupply.utils.SettingsManager;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.utils.AutomaticUpdate;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LoginActivity extends Activity {
    public static final String INTENT_KEY_USER_NAME = "INTENT_KEY_USER_NAME";

    private LinearLayout layoutLoginSetting;
    private EditText etUsername;
    private EditText etPassword;
    private ImageView imgClearInput;
    private TextView tvVersion;
    private TextView tvForgetPwd;
    private String username;
    private String password;
    private Button btnLogin;
    private ImageButton lineimg;

    private CheckBox cbAutoLogin;
    private CheckBox cbRememberPassword;
    private boolean doesAutoLogin;
    private boolean doesRememberPassword;

    private long mHints[];
    private User currentUser;
    private boolean checkingAppUpdate = false;
    private AutomaticUpdate automaticUpdate = null;
    private AppVersionCheckHandler appVersionCheckHandler;

    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTheme(android.R.style.Theme_Holo_Light);
        initUI();
        LoginSettingActivity.initDefaultServerSettings();
        EventBusUtil.register(this);
        new Handler(getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                autoLogin();
            }
        }, 1000);
    }

    private void autoLogin() {
        if (doesAutoLogin) {
            onLoginBtnClicked(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLastUserInfo();
        checkAppUpdate();
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        LoadingDialogUtil.dismiss();
        super.onDestroy();
    }

    private void checkAppUpdate() {
        if (checkingAppUpdate) {
            LoadingDialogUtil.show(this, R.string.checking_new_version);
            return;
        }
        LoadingDialogUtil.show(this, R.string.checking_new_version);
        AppUtil.checkAppUpdate(appVersionCheckHandler);
    }

    public void onClearInputClicked(View view) {
        etUsername.setText("");
        etPassword.setText("");
//        imgClearInput.setVisibility(View.GONE);
    }

    public void onLoginSettingBtnClicked(View view) {
        Intent intent = new Intent(LoginActivity.this, LoginSettingActivity.class);
        startActivity(intent);
    }

    public void onLoginBtnClicked(View view) {
        username = etUsername.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if (isUserInfoEmpty()) {
            return;
        }
        if (checkingAppUpdate) {
            Toast.makeText(this, getString(R.string.checking_new_version), Toast.LENGTH_SHORT).show();
            return;
        }
        startLogin(username, password);
    }

    public void onDisplaySettingButton(View view) {
        System.arraycopy(mHints, 1, mHints, 0, mHints.length - 1);
        mHints[mHints.length - 1] = SystemClock.uptimeMillis();
        if (SystemClock.uptimeMillis() - mHints[0] <= 1000) {
            lineimg.setVisibility(View.VISIBLE);
            layoutLoginSetting.setVisibility(View.VISIBLE);
        }
    }

    private void loadLastUserInfo() {
        String lastUser = SettingsManager.getInstance().getLastUser();
        String lastPwd = SettingsManager.getInstance().getLastPWD();

        etUsername.setText(lastUser);
        etPassword.setText(lastPwd);
    }

    private void initUI() {
        layoutLoginSetting = (LinearLayout) findViewById(R.id.layout_settings);
        etUsername = (EditText) findViewById(R.id.et_username);
        etUsername.setText("admin");
        etPassword = (EditText) findViewById(R.id.et_password);
        etPassword.setText("123456");
        loadLastUserInfo();
        tvVersion = (TextView) findViewById(R.id.tv_version);
//        imgClearInput = (ImageView) this.findViewById(R.id.img_clear_input);
        cbRememberPassword = (CheckBox) findViewById(R.id.cb_remember_password);
        cbRememberPassword.setOnCheckedChangeListener(rememberPasswordCheckedChangListener);
        cbRememberPassword.setChecked(SettingsManager.getInstance().getRememberPassword());
//        cbAutoLogin = (CheckBox) findViewById(R.id.cb_auto_login);
//        cbAutoLogin.setOnCheckedChangeListener(autoLoginCheckedChangListener);
//        cbAutoLogin.setChecked(SettingsManager.getInstance().getAutoLogin());

        String version = AppUtil.getVersion(this);
        if (StringUtil.isBlank(version)) {
            throw new RuntimeException("Application version is unknown.");
        }
        tvVersion.setText("v" + version);
        UserInfoTextWatcher textWatcher = new UserInfoTextWatcher();
        etUsername.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);
        lineimg = (ImageButton) findViewById(R.id.lineimg);

        tvForgetPwd = (TextView) findViewById(R.id.id_forget_pwd);
        Drawable drawable = getResources().getDrawable(R.drawable.icon_forget_pwd);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvForgetPwd.setCompoundDrawables(drawable, null, null, null);


        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setEnabled(true); // Disable btnLogin before checking new
        // version.
        mHints = new long[5];// click the version TextView five times;
        appVersionCheckHandler = new AppVersionCheckHandler(this);
    }

    private OnCheckedChangeListener rememberPasswordCheckedChangListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!isChecked) {
                cbAutoLogin.setChecked(false);
            }
            doesRememberPassword = isChecked;
        }
    };

    private OnCheckedChangeListener autoLoginCheckedChangListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                cbRememberPassword.setChecked(true);
            }
            doesAutoLogin = isChecked;
        }
    };


    private void startLogin(String username, String password) {
        LoadingDialogUtil.show(this, R.string.login_get_user_info);
        User user = new User();
        user.setLoginName(username);
        user.setPassword(password);
        UserService.getInstance().login(username, password);
    }

    private void getUserInfo(String token) {
        LoadingDialogUtil.show(this, R.string.login_logging_in);
        UserService.getInstance().getUserInfoByToken(token);
    }

    private class UserInfoTextWatcher implements TextWatcher {
        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            etUsername.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        etPassword.setText("");
                    }
                    return false;
                }
            });
            if (txtUserNameEditTextFieldIsValid()) {
//                imgClearInput.setVisibility(View.VISIBLE);
            }

            btnLogin.setEnabled(txtUserNameEditTextFieldIsValid() && txtPasswordEditTextFieldIsValid());
        }

        private boolean txtUserNameEditTextFieldIsValid() {
            return !TextUtils.isEmpty(etUsername.getText());
        }

        private boolean txtPasswordEditTextFieldIsValid() {
            return !TextUtils.isEmpty(etPassword.getText());
        }

    }

    private boolean isUserInfoEmpty() {
        if (StringUtil.isEmpty(username)) {
            ToastUtil.showShort(R.string.login_username_empty);
            return true;
        }

        if (StringUtil.isEmpty(password)) {
            ToastUtil.showShort(R.string.login_password_empty);
            return true;
        }

        return false;
    }

    private void handleUserInfo(LoginResponse response) {
        currentUser = response.getUser();
        currentUser.setPassword(password);
        checkLeaderInfo(currentUser);
        HostApplication.getApplication().setCurrentUser(currentUser);
        SettingsManager.getInstance().setLastUser(currentUser, doesRememberPassword);
        RestoreManager.getInstance().saveLastUser(currentUser);
    }

    private void checkLeaderInfo(User user) {
        String id = user.getId();
        String leaderId = user.getLeader();
        if (id.equals(leaderId)) {
            user.setLeader(true);
        } else {
            user.setLeader(false);
        }
    }

    // 对比服务返回的按钮名字跟XML中配置的按钮，取相同的
    private ArrayList<AppMenu> getAvailableMenus(List<AppMenu> appMenus) {
        ArrayList<AppMenu> availableMenus = new ArrayList<AppMenu>();
        if (appMenus != null && appMenus.size() > 0) {
            UserService.getInstance().setDynamicTabMenus(MenuFactory.getDynamicTabs(appMenus));
            HashMap<String, AppMenu> xmlMenus = MenuFactory.getMenuMapByTab(ResourceUtil.getStringById(R.string.menu_main));

            for (int i = 0; i < appMenus.size(); i++) {
                AppMenu serverAppMenu = appMenus.get(i);
                Iterator<String> keyIterator = xmlMenus.keySet().iterator();
                while (keyIterator.hasNext()) {
                    String xmlMenuName = keyIterator.next();
                    if (xmlMenuName.equalsIgnoreCase(serverAppMenu.getName())) {
                        AppMenu availableMenu = xmlMenus.get(xmlMenuName);
                        availableMenu.setGid(serverAppMenu.getGid());
                        availableMenu.setType(EMenuType.getTypeByValue(serverAppMenu.getConfigUrl()));
                        availableMenu.setIconURL(serverAppMenu.getIconURL());
                        availableMenu.setUrl(serverAppMenu.getUrl());
                        availableMenus.add(availableMenu);
                    }
                }
            }
        }
        return availableMenus;
    }

    private void gotoMainScreen() {
        overridePendingTransition(R.anim.fade, R.anim.hold);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private  void  handleGetToken(ResponseEvent event) {
        LoadingDialogUtil.updateMessage(R.string.login_getting_init_paramters);
        LoginTokenResponse response = event.getData();
        getUserInfo(response.getToken());
    }
    private void handleLogin(ResponseEvent event) {
        LoadingDialogUtil.updateMessage(R.string.login_getting_init_paramters);
        LoginResponse response = event.getData();
        handleCheckBoxStatus();
        handleUserInfo(response);
        handleUserMenus(response);
        handleServerTime(response);
        handleMobileConfig(response);
    }

    private void handleWatch(ResponseEvent event) {
        Map<String, String> result = new HashMap<String, String>();
        result = event.getData();
        currentUser.setWatch(result.get("isWatch").equals("true"));
        currentUser.setCanSign(result.get("canSign").equals("true"));
        currentUser.setSignOutTime("");
        if (result.get("isWatch").equals("true")) {
            currentUser.setSignOutTime(result.get("signOutTime"));
        }
        gotoMainScreen();
    }

    private void handleCheckBoxStatus() {
        SettingsManager.getInstance().setRememberPassword(doesRememberPassword);
        SettingsManager.getInstance().setAutoLogin(doesAutoLogin);
    }

    private void handleGPSHistoryPosition() {
        HostApplication.getApplication().submitExecutorService(new LoadGPSPositionThread(HostApplication.getApplication().getCurrentUser().getId(), DateUtil.getCurrentDate()));
    }

    private void handleMobileConfig(LoginResponse response) {
        ArrayList<MobileConfig> configUrls = response.getMobileConfigs();
        for (MobileConfig mobileConfig : configUrls) {
            if (mobileConfig.getKey().equalsIgnoreCase("MobileMap")) {
                String mapConfigUrl = mobileConfig.getValue();
                UserService.getInstance().getMobileConfigFile(mapConfigUrl);
            } else if (mobileConfig.getKey().equalsIgnoreCase("TransParams")) {
                String transParamsUrl = mobileConfig.getValue();
                TransparamsLoader.getInstance().loadFromWeb(transParamsUrl);
            }
        }
    }

    private void handleServerTime(LoginResponse response) {
        String serverTime = response.getTime();
        if (serverTime == null || serverTime.isEmpty()) {
            ToastUtil.showShort(R.string.login_get_server_time_error);
        }
    }

    private void handleUserMenus(LoginResponse response) {
        ArrayList<AppMenu> userMenus = getAvailableMenus(response.getMenus());
        UserService.getInstance().setAvailableMenus(userMenus);
        RestoreManager.getInstance().saveAvailableMenus(userMenus);
    }

    private void handleGetMobileConfigFile(ResponseEvent event) {
        MobileConfig config = MobileConfigParser.fromXmlInputStream(event.getData());
        RestoreManager.getInstance().saveMobileMapConfig(config);
        HostApplication.getApplication().setConfig(config);
        handleGPSHistoryPosition();
        // informNewVersion(config.getVersionCode());
        try {
            if (EQModuleConfig.getConfig().isModuleUseable() || ProjectModuleConfig.getConfig().isModuleUseable() || FuZhouPatrolModuleConfig.getConfig().isModuleUseable()) {
                gotoMainScreen();
            } else {
                UserService.getInstance().getWatchState(currentUser);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private class LoadGPSPositionThread extends Thread {
        private String userid;
        private String date;

        public LoadGPSPositionThread(String userid, String date) {
            this.userid = userid;
            this.date = date;
        }

        @Override
        public void run() {
            ArriveDetecter arriveDetecter = new ArriveDetecter();
            arriveDetecter.loadGPSPositionFromDbByUserid(userid, date);
        }
    }

    /***
     * APP版本信息获取完成，进行本地比对
     * @param appVerionInfo
     */
    private void responseAppVersionCheck(AppVerionInfo appVerionInfo) {
        if (null == appVerionInfo || !appVerionInfo.isSuccess()) {
            Toast.makeText(this, getString(R.string.check_new_version_fail), Toast.LENGTH_SHORT).show();
            return;
        }
        automaticUpdate = new AutomaticUpdate(this);
        String pakagePath = ServiceUrlManager.getAPPUrl();
        String apk = pakagePath + appVerionInfo.getPakage();
        if (automaticUpdate.isUpdate(appVerionInfo.getVersionCode())) {
            automaticUpdate.checkUpdate(apk, appVerionInfo.getVersionCode(), appVerionInfo.getType(), appVerionInfo.getDescription());
        } else {
            //Toast.makeText(this, getString(R.string.str_soft_update_no), Toast.LENGTH_SHORT).show();
        }
    }

    private static class AppVersionCheckHandler extends Handler {
        private WeakReference<LoginActivity> loginActivity;

        public AppVersionCheckHandler(LoginActivity activity) {
            loginActivity = new WeakReference<LoginActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginActivity activity = loginActivity.get();
            LoadingDialogUtil.dismiss();
            if (null == activity) {
                return;
            }
            activity.btnLogin.setEnabled(true);
            if (msg.what == AppVersionCheckRunnable.CHECKAPPUPDATE_COMPLETE) {
                AppVerionInfo appVerionInfo = null;
                try {
                    appVerionInfo = (AppVerionInfo) msg.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                activity.responseAppVersionCheck(appVerionInfo);
                activity.checkingAppUpdate = false;
            }
        }
    }

    /**
     * EventBus methods begin.
     */
    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            if (event.getId() == ResponseEventStatus.LoginEvent || event.getId() == ResponseEventStatus.ERROR) {
                try {
                    AppStatusService.changeServer();
                } catch (Exception e) {
                    LogUtil.i("LoginEvent", "changeServer error" + e.getMessage());
                }
            }
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.LoginEvent:
                handleLogin(event);
                break;
            case ResponseEventStatus.WatchEvent:
                handleWatch(event);
                break;
            case ResponseEventStatus.GET_MAPCONFIG_EVENT:
                handleGetMobileConfigFile(event);
                break;
            case ResponseEventStatus.LoginGetTokenEvent:
                handleGetToken(event);
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(UIEvent event) {
        switch (event.getId()) {
            case UIEventStatus.TOAST:
                if (event.isForTarget(this)) {
                    ToastUtil.showShort(event.getMessage());
                }
                break;
            default:
                break;
        }
    }
    /**
     * EventBus methods end.
     */
}