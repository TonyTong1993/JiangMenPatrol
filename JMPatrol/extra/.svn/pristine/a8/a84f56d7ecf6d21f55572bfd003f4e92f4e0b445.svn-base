package com.ecity.sangfor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sangfor.ssl.IVpnDelegate;
import com.sangfor.ssl.SFException;
import com.sangfor.ssl.SangforAuth;
import com.sangfor.ssl.common.VpnCommon;
import com.sangfor.ssl.service.setting.SystemConfiguration;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Administrator on 2017/4/6.
 */

public class VpnTestUtil implements IVpnDelegate {
    private static final String TAG = "SFSDK_" + VpnTestUtil.class.getSimpleName();
    private static final String KEY_VPN_USER = "KEY_VPN_USER";
    private static final String KEY_VPN_PASSWORD = "KEY_VPN_PASSWORD";
    private static final String KEY_VPN_IP = "KEY_VPN_IP";

    // View
    private EditText edt_ip = null;
    private EditText edt_user = null;
    private EditText edt_passwd = null;
    private WebView webView = null;
    private NotificationManager manager;

    // 认证所需信息
    private static String VPN_IP = "61.154.12.43"; // VPN设备地址　（也可以使用域名访问）
    private static int VPN_PORT = 443; // vpn设备端口号，一般为443
    // 用户名密码认证；用户名和密码
    private static String USER_NAME = "zouyang";
    private static String USER_PASSWD = "zouyang@com.fzwater";
    // 证书认证；导入证书路径和证书密码（如果服务端没有设置证书认证此处可以不设置）
    private static String CERT_PATH = "";
    private static String CERT_PASSWD = "";
    // 测试内网服务器地址。（在vpn服务器上，配置的内网资源）
    private static String TEST_URL = "http://199.201.75.123";

    private static String SMS_CODE = "";
    private static String RADIUS_CODE = "";//radius认证的

    private final int TEST_URL_TIMEOUT_MILLIS = 8 * 1000;// 测试vpn资源的超时时间
    private int AUTH_MODULE = SangforAuth.AUTH_MODULE_EASYAPP;

    
    public void vpnInit() {
        SangforAuth sfAuth = SangforAuth.getInstance();
        if (sfAuth != null) {
            // 从其它界面回到这个界面时，需重新设置回调，让vpn把回调发送到当前Activity的vpnCallback里面来
            sfAuth.setDelegate(this);
            Log.i(TAG, "set IVpnDelegate");
        }
    }

    private void initVpnModule(Activity context) {
        SangforAuth sfAuth = SangforAuth.getInstance();
        try {
            // SDK模式初始化，easyapp模式或者是l3vpn模式，两种模式区别请参考文档
            sfAuth.init(context.getApplication(), context, this, AUTH_MODULE);//SangforAuth.AUTH_MODULE_L3VPN、SangforAuth.AUTH_MODULE_EASYAPP
            sfAuth.setLoginParam(AUTH_CONNECT_TIME_OUT, String.valueOf(8));
        } catch (SFException e) {
            e.printStackTrace();
        }
    }




    private boolean getValueFromView() {
        VPN_IP = edt_ip.getText().toString();

        USER_NAME = edt_user.getText().toString().trim();
        USER_PASSWD = edt_passwd.getText().toString().trim();

        return true;
    }

//    @Override
//    public void onClick(View v) {
//        if (!getValueFromView()) {
//            return;
//        }
//
//        switch (v.getId()) {
//            case R.id.btn_login:
//                if (SangforAuth.getInstance().vpnQueryStatus() != IVpnDelegate.VPN_STATUS_AUTH_OK) {
//                    initSslVpn();
//
//                    PreferencesUtil.putString(this, KEY_VPN_USER, USER_NAME);
//                    PreferencesUtil.putString(this, KEY_VPN_PASSWORD, USER_PASSWD);
//                    PreferencesUtil.putString(this, KEY_VPN_IP, VPN_IP);
//                } else {
//                    ToastUtil.show("VPN正常运行中,注销后才能重新登录", 0);
//                }
//
//                break;
//            case R.id.btn_logout:
//                com.sangfor.bugreport.logger.Log.info("TAG", "Do vpnLogout");
//                // vpnLogout是异步接口，在vpnCallback里面等到回调后表示从服务端成功注销用户
//                if (SangforAuth.getInstance().vpnLogout() == false) {
//                    com.sangfor.ssl.service.utils.logger.Log.error("TAG", "call vpnLogout failed, please see Logcat log");
//                } else {
//                    // 调用vpnLogout成功后会在vpnCallback方法中收到结果
//                }
//                break;
////            case R.id.btn_send_sms:
////                doVpnLogin(IVpnDelegate.AUTH_TYPE_SMS);
////                break;
////            case R.id.btn_send_challenge:
////                doVpnLogin(IVpnDelegate.AUTH_TYPE_RADIUS);
////                break;
////            case R.id.btn_reget_sms:
////                doVpnLogin(IVpnDelegate.AUTH_TYPE_SMS1);
////                break;
////            case R.id.btn_test_http:
////                new TestThread().start();// 将测试结果写到logcat中去，需要分析日志
////                //new TestHttpsThread().start();//这个是测试下载文件
////                loadPage(TEST_URL);// 将测试结果展示到界面上，直观展示
////                break;
//            default:
//                Log.w(TAG, "onClick no process");
//        }
//    }

    private void loadPage(String testUrl) {
        LoadPageByWebView(testUrl);
    }

    class InitSslVpnTask extends AsyncTask<Void, Void, Boolean> {
        InetAddress m_iAddr = null;

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                m_iAddr = InetAddress.getByName(VPN_IP);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            SangforAuth sfAuth = SangforAuth.getInstance();
            String strHost = "";
            if (m_iAddr != null) {
                strHost = m_iAddr.getHostAddress();
            }
            if (TextUtils.isEmpty(strHost)) {
                Log.i(TAG, "解析VPN服务器域名失败");
                strHost = "0.0.0.0";
            }
            Log.i(TAG, "vpn server ip is: " + strHost);
            long host = VpnCommon.ipToLong(strHost);
            if (sfAuth.vpnInit(host, VPN_PORT) == false) {
                Log.d(TAG, "vpn init fail, errno is " + sfAuth.vpnGeterr());
                return;
            }
        }
    }

    /**
     * 开始初始化VPN，该初始化为异步接口，后续动作通过回调函数vpncallback通知结果
     *
     * @return 成功返回true，失败返回false，一般情况下返回true
     */
    private boolean initSslVpn() {
        InitSslVpnTask initSslVpnTask = new InitSslVpnTask();
        initSslVpnTask.execute();
        return true;
    }

    /**
     * 处理认证，通过传入认证类型（需要的话可以改变该接口传入一个hashmap的参数用户传入认证参数）.
     * <p>
     * 也可以一次性把认证参数设入，这样就如果认证参数全满足的话就可以一次性认证通过，可见下面屏蔽代码
     *
     * @param authType 认证类型
     * @throws SFException
     */
    private void doVpnLogin(int authType) {
        Log.d(TAG, "doVpnLogin authType " + authType);
        boolean ret = false;
        SangforAuth sfAuth = SangforAuth.getInstance();
        /*
         * // session共享登陆：主APP封装时走原认证流程，子APP认证时使用TWFID（SessionId）认证方式 boolean isMainApp = true; //子APP,isMainApp = false; if(!isMainApp){ authType =
		 * IVpnDelegate.AUTH_TYPE_TWFID; }
		 */
        switch (authType) {
            case IVpnDelegate.AUTH_TYPE_CERTIFICATE:
                if (CERT_PATH.isEmpty()) {
//                    Toast.makeText(this, "vpn证书路径不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String certPath = CERT_PATH;
                String certPasswd = CERT_PASSWD;
                sfAuth.setLoginParam(IVpnDelegate.CERT_PASSWORD, certPasswd);
                sfAuth.setLoginParam(IVpnDelegate.CERT_P12_FILE_NAME, certPath);
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_CERTIFICATE);
                break;
            case IVpnDelegate.AUTH_TYPE_PASSWORD:
                if (edt_user.getText().toString().isEmpty()) {
//                    Toast.makeText(this, "vpn用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String user = edt_user.getText().toString();
                String passwd = edt_passwd.getText().toString();
                sfAuth.setLoginParam(IVpnDelegate.PASSWORD_AUTH_USERNAME, user);
                sfAuth.setLoginParam(IVpnDelegate.PASSWORD_AUTH_PASSWORD, passwd);

                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_PASSWORD);
                break;
            case IVpnDelegate.AUTH_TYPE_SMS:
                // 进行短信认证
                if (SMS_CODE.isEmpty()) {
//                    Toast.makeText(this, "vpn短信验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String smsCode = SMS_CODE;
                sfAuth.setLoginParam(IVpnDelegate.SMS_AUTH_CODE, smsCode);
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_SMS);
                break;
            case IVpnDelegate.AUTH_TYPE_SMS1://重新获取短信
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_SMS1);
                break;
            case IVpnDelegate.AUTH_TYPE_TOKEN:
                if (RADIUS_CODE.isEmpty()) {
//                    Toast.makeText(this, "vpn 动态口令不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String token = RADIUS_CODE;
                Log.e(TAG, "TOKEN_AUTH_CODE = " + token);
                sfAuth.setLoginParam(IVpnDelegate.TOKEN_AUTH_CODE, token);
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_TOKEN);
                break;
            case IVpnDelegate.AUTH_TYPE_RADIUS:
                if (RADIUS_CODE.isEmpty()) {
//                    Toast.makeText(this, "vpn radius 挑战验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 进行挑战认证
                String challenge = RADIUS_CODE;
                sfAuth.setLoginParam(IVpnDelegate.CHALLENGE_AUTH_REPLY, challenge);
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_RADIUS);
                break;
            case IVpnDelegate.AUTH_TYPE_TWFID:
                // session共享登陆--子APP登陆：子APP使用TWFID登陆。这两个APP就共享VPN隧道，占用同一个授权。
                String twfid = "11438E3C617095C50D28BA337133872730CBAB0D64F98B53F5105221B9D937E8";
                if (twfid != null && !twfid.equals("")) {
                    Log.i(TAG, "do TWFID Auth, TwfId:" + twfid);
                    sfAuth.setLoginParam(IVpnDelegate.TWF_AUTH_TWFID, twfid);
                    ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_TWFID);
                } else {
                    Log.e(TAG, "You hasn't written TwfId");
//                    Toast.makeText(this, "You hasn't written TwfId", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Log.w(TAG, "default authType " + authType);
                break;
        }

        if (ret == true) {
            Log.i(TAG, "success to call login method");
        } else {
            Log.i(TAG, "fail to call login method");
        }

    }

    /*
     * l3vpn模式（SangforAuth.AUTH_MODULE_L3VPN）必须重写这个函数： 注意：当前Activity的launchMode不能设置为 singleInstance，否则L3VPN服务启动会失败。 原因：
	 * L3VPN模式需要通过startActivityForResult向系统申请使用L3VPN权限，{@link VpnService#prepare} 但startActivityForResult有限制： You cannot use startActivityForResult()
	 * if the activity being started is not running in the same task as the activity that starts it. This means that neither of the activities can
	 * have launchMode="singleInstance" 也就是说当前Activity的launchMode不能设置为 singleInstance
	 *
	 * EASYAPP模式 (SangforAuth.AUTH_MODULE_EASYAPP）： 请忽略。
	 */
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        SangforAuth.getInstance().onActivityResult(requestCode, resultCode);
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    private void displayToast(String str) {
//        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    /*
     * VPN 初始化和认证过程的回调结果通知
     *
     * @see com.sangfor.ssl.IVpnDelegate#vpnCallback(int, int)
     */
    @Override
    public void vpnCallback(int vpnResult, int authType) {
        SangforAuth sfAuth = SangforAuth.getInstance();

        switch (vpnResult) {
            case IVpnDelegate.RESULT_VPN_INIT_FAIL:
                /**
                 * 初始化vpn失败
                 */
                Log.i(TAG, "RESULT_VPN_INIT_FAIL, error is " + sfAuth.vpnGeterr());
                displayToast(sfAuth.vpnGeterr());
                break;

            case IVpnDelegate.RESULT_VPN_INIT_SUCCESS:
                /**
                 * 初始化vpn成功，接下来就需要开始认证工作了
                 */
                Log.i(TAG, "RESULT_VPN_INIT_SUCCESS, current vpn status is " + sfAuth.vpnQueryStatus());
//                displayToast("RESULT_VPN_INIT_SUCCESS, current vpn status is " + sfAuth.vpnQueryStatus());
                Log.i(TAG, "vpnResult============" + vpnResult + "\nauthType ============" + authType);
                sfAuth.setLoginParam(IVpnDelegate.AUTH_DEVICE_LANGUAGE, "zh_CN");// zh_CN or en_US
                // 设置后台不自动登陆,true为off,即取消自动登陆.默认为false,后台自动登陆.
                // sfAuth.setLoginParam(AUTO_LOGIN_OFF_KEY, "true");
                // 初始化成功，进行认证操作　（此处采用“用户名密码”认证）
                doVpnLogin(IVpnDelegate.AUTH_TYPE_PASSWORD);
                break;

            case IVpnDelegate.RESULT_VPN_AUTH_FAIL:
                /**
                 * 认证失败，有可能是传入参数有误，具体信息可通过sfAuth.vpnGeterr()获取
                 */
                String errString = sfAuth.vpnGeterr();
                Log.i(TAG, errString);
                displayToast("" + errString);

                if (errString.contains("psw_errorCode")) {
                    String pwdMsg = sfAuth.getPasswordSafePolicyPrompt(errString);
                    displayToast(pwdMsg);
                }

                break;

            case IVpnDelegate.RESULT_VPN_AUTH_SUCCESS:
//                displayNotification();

                /**
                 * 认证成功，认证成功有两种情况，一种是认证通过，可以使用sslvpn功能了，
                 *
                 * 另一种是 前一个认证（如：用户名密码认证）通过，但需要继续认证（如：需要继续证书认证）
                 */
                if (authType == IVpnDelegate.AUTH_TYPE_NONE) {

				/*
                 * // session共享登陆--主APP保存：认证成功 保存TWFID（SessionId），供子APP使用 String twfid = sfAuth.getTwfid(); Log.i(TAG, "twfid = "+twfid);
				 */
                    Log.i(TAG, "welcome to sangfor sslvpn!");
                    displayToast("认证成功!");

                    // 若为L3vpn流程，认证成功后会自动开启l3vpn服务，需等l3vpn服务开启完成后再访问资源
                    if (SangforAuth.getInstance().getModuleUsed() == SangforAuth.AUTH_MODULE_EASYAPP) {
                        // EasyApp流程，认证流程结束，可访问资源。
                        doResourceRequest();
                    }
                } else if (authType == IVpnDelegate.VPN_TUNNEL_OK) {
                    // l3vpn流程，l3vpn服务通道建立成功，可访问资源
                    Log.i(TAG, "L3VPN tunnel OK!");
                    displayToast("L3VPN 认证成功!");
                    doResourceRequest();
                } else {
                    Log.i(TAG, "auth success, and need next auth, next auth type is " + authType);
                    displayToast("auth success, and need next auth, next auth type is " + authType);

                    if (authType == IVpnDelegate.AUTH_TYPE_SMS) {
                        // 下一次认证为短信认证，获取相关的信息
                        String phoneNum = SangforAuth.getInstance().getSmsPhoneNum();
                        String countDown = SangforAuth.getInstance().getSmsCountDown();
                        String toastStrsg = "sms code send to [" + phoneNum + "]\n" + "reget code count down [" + countDown + "]\n";
//                        Toast.makeText(this, toastStrsg, Toast.LENGTH_SHORT).show();
                    } else if (authType == IVpnDelegate.AUTH_TYPE_RADIUS) {
//                        Toast.makeText(this, "start radius challenge auth", Toast.LENGTH_SHORT).show();
                    } else if (authType == IVpnDelegate.AUTH_TYPE_TOKEN) {
                        String tokenCode = RADIUS_CODE;
                        if (TextUtils.isEmpty(tokenCode)) {
                            displayToast("need input tokencode");
                            break;
                        }
                        sfAuth.setLoginParam(IVpnDelegate.TOKEN_AUTH_CODE, tokenCode);
                    } else {
                        doVpnLogin(authType);
                    }
                }
                break;
            case IVpnDelegate.RESULT_VPN_AUTH_CANCEL:
                Log.i(TAG, "RESULT_VPN_AUTH_CANCEL");
//                displayToast("RESULT_VPN_AUTH_CANCEL");
                break;


            case IVpnDelegate.RESULT_VPN_AUTH_LOGOUT:
                /**
                 * 主动注销（自己主动调用logout接口）
                 */
                Log.i(TAG, "RESULT_VPN_AUTH_LOGOUT");
//                ToastUtil.show("注销vpn成功", 0);
//                finish();
                try {
                    manager.cancel(1);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                break;
            case IVpnDelegate.RESULT_VPN_L3VPN_FAIL:
                /**
                 * L3vpn启动失败，有可能是没有l3vpn资源，具体信息可通过sfAuth.vpnGeterr()获取
                 */
                Log.i(TAG, "RESULT_VPN_L3VPN_FAIL, error is " + sfAuth.vpnGeterr());
                displayToast(sfAuth.vpnGeterr());
                break;
            case IVpnDelegate.RESULT_VPN_L3VPN_SUCCESS:
                /**
                 * L3vpn启动成功
                 */
                Log.i(TAG, "RESULT_VPN_L3VPN_SUCCESS ===== " + SystemConfiguration.getInstance().getSessionId());
                break;
            case IVpnDelegate.RESULT_VPN_L3VPN_RELOGIN:
                /**
                 * L3vpn服务端注销虚拟IP,一般是私有帐号在其他设备同时登录造成的
                 */
                Log.i(TAG, "relogin now");
                displayToast("relogin now");
                break;
            default:
                /**
                 * 其它情况，不会发生，如果到该分支说明代码逻辑有误
                 */
                Log.i(TAG, "default result, vpn result is " + vpnResult);
                displayToast("" + vpnResult);
                break;
        }
    }

    private void doResourceRequest() {
        // 认证结束，可访问资源。
    }

    /**
     * 认证过程若需要图形校验码，则回调通告图形校验码位图，
     *
     * @param data 图形校验码位图
     */
    @Override
    public void vpnRndCodeCallback(byte[] data) {
        Log.d(TAG, "vpnRndCodeCallback data: " + Boolean.toString(data == null));
        if (data != null) {
            Log.i(TAG, "vpnRndCodeCallback RndCo we not support RndCode now");
        }
    }

    @Override
    public void reloginCallback(int status, int result) {
        switch (status) {

            case IVpnDelegate.VPN_START_RELOGIN:
                Log.e(TAG, "relogin callback start relogin start ...");
                break;
            case IVpnDelegate.VPN_END_RELOGIN:
                Log.e(TAG, "relogin callback end relogin ...");

                if (result == IVpnDelegate.VPN_RELOGIN_SUCCESS) {
                    Log.e(TAG, "relogin callback, relogin success!");
                    displayToast("重新登录成功!");
                } else {
                    Log.e(TAG, "relogin callback, relogin failed");
                    displayToast("重新登录成功!");

                }
                break;
        }

    }

    /**
     * 测试资源，打开浏览器
     */
    private void luanchWebBrowser(String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

//            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            displayToast("Luanch Web Browser is error. " + url);
        }
    }

    /**
     * 测试HTTP/HTTPS资源
     */
    private class TestThread extends Thread {
        @Override
        public void run() {
            SangforAuth sfAuth = SangforAuth.getInstance();
            Log.i(TAG, "vpn status ===================== " + sfAuth.vpnQueryStatus());

            String urlStr = TEST_URL;
            Log.i(TAG, "url =======" + urlStr);

            try {
                if (!urlStr.isEmpty()) {
                    SentHttpAndHttpsPost(urlStr);
                } else {
                    Log.i(TAG, "url is empty!");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    /** 测试HTTPS资源 */
//    private class TestHttpsThread extends Thread {
//        @Override
//        public void run() {
//            String path = "/sdcard/tmp/Test.apk";
//            Log.i("download", path);
//            // String url = "https://" + "200.200.75.38" + "/com/" +
//            // "EasyConnectPhone.apk";
//            String url = TEST_URL;
//            AsyncTask task = new DownloadApkTask(url, path);
//            task.execute((Void[]) null);
//        }
//    }

    public static class CrashHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {

            // 打印未捕获的异常堆栈
            Log.d(TAG, "UnHandledException: ");
            ex.printStackTrace();
        }
    }

    private void SentHttpAndHttpsPost(final String url) {
        if (url == null || url.isEmpty()) {
            Log.e(TAG, "SentHttpAndHttpsPost url param error");
            return;
        }

        try {
            if (!getHostByName(url)) {
                return;
            }
            Log.i(TAG, "test url= " + url);
            if (url.toLowerCase().contains("https://".toLowerCase())) {
                sendHttpsPost(url);
            } else {
                sendHttpPost(url);
            }
        } catch (Exception e) {
            Log.i(TAG, "sendHttpsPost throw exception");
            e.printStackTrace();
        }
        return;
    }

    // 测试dns解析
    public class DNSLookupThread extends Thread {
        private InetAddress addr;
        private String hostname;

        public DNSLookupThread(String hostname) {
            this.hostname = hostname;
        }

        public void run() {
            try {
                InetAddress add = InetAddress.getByName(hostname);
                addr = add;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        public synchronized String getIP() {
            if (null != this.addr) {
                return addr.getHostAddress();
            }
            return null;
        }
    }

    // 设置dns解析超时方法
    public boolean getHostByName(String urlStr) throws Exception {
        if (urlStr == null || urlStr.isEmpty()) {
            return false;
        }
        URL url = new URL(urlStr);
        String ipAddress = url.getHost();

        DNSLookupThread dnsThread = new DNSLookupThread(ipAddress);
        dnsThread.start();
        dnsThread.join(TEST_URL_TIMEOUT_MILLIS);
        String ipStr = dnsThread.getIP();
        if (ipStr == null) {
            Log.e(TAG, "host=" + ipAddress + " DNSLookup failed!");
            return false;
        } else {
            Log.i(TAG, "host=" + ipAddress + " ip=" + ipStr);
            return true;
        }
    }

    class MyX509TrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
            // No need to implement.
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
            // No need to implement.
        }
    }

    class MyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    @SuppressLint("TrulyRandom")
    private void sendHttpsPost(String url) throws Exception {
        // Create a trust manager that does not validate certificate chains
        if (url == null || url.isEmpty()) {
            Log.e(TAG, "sendHttpsPost url is wrong");
            return;
        }
        TrustManager[] trustAllCerts = new TrustManager[]{new MyX509TrustManager()};
        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new MyHostnameVerifier();
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        URL obj = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(TEST_URL_TIMEOUT_MILLIS); // set timeout to 5
        // seconds
        conn.setReadTimeout(TEST_URL_TIMEOUT_MILLIS);
        conn.setRequestProperty("Content-type", "application/x-java-serialized-object");
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        String urlParameters = obj.getQuery();
        if (urlParameters == null) {
            urlParameters = "";
        }
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = conn.getResponseCode();
        Log.i(TAG, "Sending 'POST' request to URL : " + url);
        Log.i(TAG, url + " Post parameters = " + urlParameters);
        Log.i(TAG, url + " Response Code = " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String result = response.toString();
        Log.i(TAG, url + " response = " + result);
    }

    // HTTP POST request
    private void sendHttpPost(String url) throws Exception {
        if (url == null || url.isEmpty()) {
            Log.i(TAG, "sendHttpPost url is wrong");
            return;
        }

        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(TEST_URL_TIMEOUT_MILLIS);
        conn.setReadTimeout(TEST_URL_TIMEOUT_MILLIS);
        conn.setRequestProperty("Content-type", "application/x-java-serialized-object");
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        String urlParameters = obj.getQuery();
        if (urlParameters == null) {
            urlParameters = "";
        }

        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = conn.getResponseCode();
        Log.i(TAG, "Sending 'POST' request to URL : " + url);
        Log.i(TAG, url + " Post parameters = " + urlParameters);
        Log.i(TAG, url + " Response Code = " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String result = response.toString();
        Log.i(TAG, url + " response = " + result);

    }

    @SuppressLint("SetJavaScriptEnabled")
    public void LoadPageByWebView(String url) {
        if (url == null || url.equals("")) {
            Log.i(TAG, "LoadPageByWebView url is wrong!");
            return;
        }

        if (webView == null) {
//            webView = (WebView) findViewById(R.id.webView);
        }

        webView.setWebViewClient(new MyWebViewClient());

        WebSettings webSettings = webView.getSettings();
        // 不使用缓存，只从网络获取数据。
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        // 开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        // 设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
        webSettings.setUseWideViewPort(true);
        // 设置默认加载的可视范围是大视野范围
        webSettings.setLoadWithOverviewMode(true);
        // 网页中包含JavaScript内容需调用以下方法，参数为true
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(url);
    }

    private class MyWebViewClient extends WebViewClient {

        public MyWebViewClient() {
        }

        @Override
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.i(TAG, "onPageStarted url = " + url);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // super.onReceivedSslError(view, handler,
            // error);//这样会调用到handler.cancel()
            handler.proceed();// 忽略证书错误
        }
    }

//    @Override
//    // 用户点击回退按钮时需要处理WebView的回退
//    public void onBackPressed() {
//        if (webView != null && webView.isFocused() && webView.canGoBack()) {
//            webView.goBack();
//        } else {
//            super.onBackPressed();
//            finish();
//        }
//    }

//    private void displayNotification() {
//        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Notification.Builder builder = new Notification.Builder(this);
//        builder.setContentText("vpn正常运行中");
//        builder.setContentTitle("福州工程");
//        builder.setSmallIcon(R.drawable.logo_main);
//        builder.setWhen(System.currentTimeMillis());
//        Intent notificationIntent = new Intent();
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//        builder.setContentIntent(null);
//        Notification notification = builder.build();
//        manager.notify(1, notification);
//
////        manager.cancel(1);
    }

    //	// ///////////////////android 6.0 动态权限begin///////////////////////////
    //	private static final int PERMISSION_REQUEST_CODE = 1;
    //	private static final String[] ALL_PERMISSIONS_WE_NEED = { Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE,
    //			Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, };
    //
    //	private List<String> mPermissionsNotGranted;
    //
    //	/**
    //	 * 是否需要申请权限
    //	 *
    //	 * @return false表示不需要申请，true表示需要申请
    //	 */
    //	@SuppressLint("NewApi")
    //	private boolean shouldRequestPermissions() {
    //		// Android 6.0以下没有运行时权限
    //		if (Build.VERSION.SDK_INT < 23) {
    //			return false;
    //		}
    //
    //		ApplicationInfo ai = getApplicationInfo();
    //		// targetSdkVersion < 23 不会启用运行时权限
    //		if (ai.targetSdkVersion < 23) {
    //			return false;
    //		}
    //
    //		if (mPermissionsNotGranted == null) {
    //			mPermissionsNotGranted = new ArrayList<String>();
    //		}
    //		mPermissionsNotGranted.clear();
    //		for (int i = 0; i < ALL_PERMISSIONS_WE_NEED.length; ++i) {
    //			// Your targetSdkVersion needs to be 23 or higher
    //			if (checkSelfPermission(ALL_PERMISSIONS_WE_NEED[i]) != PackageManager.PERMISSION_GRANTED) {
    //				mPermissionsNotGranted.add(ALL_PERMISSIONS_WE_NEED[i]);
    //			}
    //		}
    //
    //		if (mPermissionsNotGranted.isEmpty()) {
    //			return false; // 如果已经获得权限，则不必继续申请
    //		}
    //
    //		return true; // 需要申请权限
    //	}
    //
    //	/**
    //	 * 通知用户要进行权限申请
    //	 */
    //	private void showRequestPermissionsReasonDialog() {
    //		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    //		builder.setTitle("需要授权");
    //		builder.setMessage("VPN需要相关权限，请在授权页面进行授权");
    //		builder.setPositiveButton("申请权限", new DialogInterface.OnClickListener() {
    //			@Override
    //			public void onClick(DialogInterface dialog, int which) {
    //				dialog.dismiss();
    //				startRequestPermissions();// 开始申请权限
    //			}
    //		});
    //		builder.setCancelable(false);
    //		builder.show();
    //	}
    //
    //	@SuppressLint("NewApi")
    //	private void startRequestPermissions() {
    //		String[] permissions = new String[mPermissionsNotGranted.size()];
    //		mPermissionsNotGranted.toArray(permissions);
    //		requestPermissions(permissions, PERMISSION_REQUEST_CODE);
    //	}
    //
    //	/**
    //	 * 通知用户权限申请失败
    //	 */
    //	private void showRequestPermissionFailedDialog() {
    //		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    //		builder.setTitle("需要授权").setMessage("VPN申请权限失败，请前往系统设置的“权限”页面进行授权").setPositiveButton("前往权限页面", new DialogInterface.OnClickListener() {
    //
    //			@Override
    //			public void onClick(DialogInterface dialog, int which) {
    //				gotoAppPermissionManageActivity();
    //				dialog.dismiss();
    //				exitApp();
    //			}
    //		}).setNegativeButton("退出", new DialogInterface.OnClickListener() {
    //
    //			@Override
    //			public void onClick(DialogInterface dialog, int which) {
    //				dialog.dismiss();
    //				exitApp();
    //			}
    //		}).setCancelable(false).show();
    //	}
    //
    //	/**
    //	 * 跳转到系统的权限配置页面
    //	 */
    //	private void gotoAppPermissionManageActivity() {
    //		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    //		intent.setData(Uri.parse("package:" + getPackageName()));
    //		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //		startActivity(intent);
    //	}
    //
    //	private void exitApp() {
    //		System.exit(0);
    //	}
    //
    //	/**
    //	 * 权限申请结果回调函数
    //	 */
    //	@SuppressLint("NewApi")
    //	@Override
    //	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    //		switch (requestCode) {
    //		case PERMISSION_REQUEST_CODE: {
    //			boolean allPermissionsGranted = true;
    //			for (int i = 0; i < grantResults.length; ++i) {
    //				if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
    //					allPermissionsGranted = false;
    //					break;
    //				}
    //			}
    //			if (!allPermissionsGranted) {
    //				Toast.makeText(this, "Permission Request Failed", Toast.LENGTH_SHORT).show();
    //				showRequestPermissionFailedDialog();
    //			} else {
    //				Toast.makeText(this, "Permission Request Success", Toast.LENGTH_SHORT).show();
    //				initVpnModule();
    //			}
    //			break;
    //		}
    //		default: {
    //			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    //			return;
    //		}
    //
    //		}
    //	}
    //	// ///////////////////android 6.0 动态权限end///////////////////////////
//}
