package com.ecity.cswatersupply.project;

/**
 * 江门工程模块
 *
 */
public class ProjectModuleConfig {
    private static ProjectModuleConfig config;
    private boolean isAvailable;

    private ProjectModuleConfig() {

    }

    public static void initDefualt() throws Exception {
        init(false);
    }

    public static void init(boolean moduleUseable) throws Exception {
        if (null != config) {
            throw new Exception("ProjectModuleConfig has been initialized！");
        }
        config = new ProjectModuleConfig();
        config.setModuleUseable(moduleUseable);
    }

    public static ProjectModuleConfig getConfig() throws Exception {
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
