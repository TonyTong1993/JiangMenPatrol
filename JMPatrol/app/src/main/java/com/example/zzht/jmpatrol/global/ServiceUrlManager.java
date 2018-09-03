package com.example.zzht.jmpatrol.global;

public class ServiceUrlManager {
    private static ServiceUrlManager instance;

    private ServiceUrlManager() {
    }

    public static ServiceUrlManager getInstance() {
        if (null == instance) {
            synchronized (ServiceUrlManager.class) {
                if (null == instance) {
                    instance = new ServiceUrlManager();
                }
            }
        }
        return instance;
    }

    public static String getDomain() {
        int serverType = SettingsManager.getInstance().getServerType();
        String protocol = "";
        String ip = "";
        String port = "";
        if (0 == serverType) {
            protocol = SettingsManager.getInstance().getProtocolA();
            ip = SettingsManager.getInstance().getServerIPA();
            port = SettingsManager.getInstance().getServerPortA();
        } else if (1 == serverType) {
            protocol = SettingsManager.getInstance().getProtocolB();
            ip = SettingsManager.getInstance().getServerIPB();
            port = SettingsManager.getInstance().getServerPortB();
        } else {
            // no logic to do.
        }

        return protocol + "://" + ip + ":" + port;
    }

    public String getServiceRootUrl() {
        /**
         * 用户包
         */
//        return BuildConfig.SERVICE_URL_ROOT + BuildConfig.VIRTUAL_PATH;


        /* 推广环境默认地址 */
//        return "http://promotion-service-zhyytest.oennso.enn.cn:80/api/v1/sop";

        /* 推广开发环境默认地址 */
        //return "http://promotion-service-dev-zhyytest.oennso.enn.cn:80/api/v1/sop";


//        return "http://service-test-zhyytest.ipaas.enncloud.cn:56512/api/v1/sop";

        /**
         * 可选择ip的测试包
         */
        String virtualPath = SettingsManager.getInstance().getServerVirtualPathA();
        return getDomain() + "/" + virtualPath;

    }

    public String getRootServiceUrl() {
        return getServiceRootUrl();
    }

    /**
     * 登录
     */
    public String getLoginUrl() {
        return getServiceRootUrl() + "/user/login";
    }
}
