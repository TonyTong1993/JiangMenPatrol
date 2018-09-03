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
        return  "";
    }

    public String getServiceRootUrl() {

        /**
         * 可选择ip的测试包
         */

        return "";

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
