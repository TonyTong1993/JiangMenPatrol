package com.ecity.cswatersupply.project;

/**
 * 福州外勤模块
 *
 */
public class FuZhouPatrolModuleConfig {
    private static FuZhouPatrolModuleConfig config;
    private boolean isAvailable;

    private FuZhouPatrolModuleConfig() {

    }

    public static void initDefualt() throws Exception {
        init(false);
    }

    public static void init(boolean moduleUseable) throws Exception {
        if (null != config) {
            throw new Exception("ProjectModuleConfig has been initialized！");
        }
        config = new FuZhouPatrolModuleConfig();
        config.setModuleUseable(moduleUseable);
    }

    public static FuZhouPatrolModuleConfig getConfig() throws Exception {
        if (null == config) {
            throw new Exception("ProjectModuleConfig has not been initialized！");
        }

        return config;
    }

    public boolean isModuleUseable() {
        return isAvailable;
    }

    private void setModuleUseable(boolean moduleUseable) {
        this.isAvailable = moduleUseable;
    }
}
