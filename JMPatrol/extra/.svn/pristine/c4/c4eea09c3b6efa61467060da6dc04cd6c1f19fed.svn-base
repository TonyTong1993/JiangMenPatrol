package com.ecity.z3map.maploader;

/**
 * Created by zhengzhuanzi on 2017/6/15.
 */

public class ModuleSetting {
    private static ModuleSetting config;
    /***
     * 模块是否可用标记
     */
    private boolean moduleUseable;

    private double maxResolution = 100000;
    private double minResolution = 0.00000022351741790859856;

    private ModuleSetting(){

    }

    public static void initDefualt(){
        init(false);
    }

    public static void init(boolean moduleUseable) {
        if(null != config){
           return;
        }
        config = new ModuleSetting();
        config.setModuleUseable(moduleUseable);
    }

    public static ModuleSetting getModuleSetting() {
        if(null == config){
            ModuleSetting.init(true);
        }

        return config;
    }

    public boolean isModuleUseable() {
        return moduleUseable;
    }

    private void setModuleUseable(boolean moduleUseable) {
        this.moduleUseable = moduleUseable;
    }

    public double getMinResolution() {
        return minResolution;
    }

    public void setMinResolution(double minResolution) {
        this.minResolution = minResolution;
    }

    public double getMaxResolution() {
        return maxResolution;
    }

    public void setMaxResolution(double maxResolution) {
        this.maxResolution = maxResolution;
    }
}
