package com.ecity.cswatersupply.emergency;

import android.app.Application;

import com.aspsine.multithreaddownload.DownloadConfiguration;
import com.aspsine.multithreaddownload.DownloadManager;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.emergency.test.FromPackageTest;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class EQModuleConfig {
    public static final String EQLAYERNAME = "earthquakeLayer";
    public static final String STATIONLAYERNAME = "eqStationLayer";
    public static final String REFUGENAME = "eqRefugeLayer";
    private static EQModuleConfig config;
    /***
     * 模块是否可用标记
     */
    private boolean moduleUseable;
    private boolean czModleUseable;
    
    private EQModuleConfig(){
        
    }
    
    public static void initDefualt() throws Exception{
        init(false,false);
    }


    public static void init(boolean moduleUseable,boolean czModleUseable) throws Exception{
        if(null != config){
            throw new Exception("EQModuleConfig has been initialized！");
        }
        FromPackageTest.test();
        config = new EQModuleConfig();
        config.setModuleUseable(moduleUseable);
        config.setCzModleUseable(czModleUseable);
        initDownloader(HostApplication.getApplication());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(HostApplication.getApplication()).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
    }
    
    private static void initDownloader(Application application) {
        DownloadConfiguration configuration = new DownloadConfiguration();
        configuration.setMaxThreadNum(10);
        configuration.setThreadNum(3);
        DownloadManager.getInstance().init(application, configuration);
    }

    public static EQModuleConfig getConfig() throws Exception{
        if(null == config){
            throw new Exception("EQModuleConfig has not been initialized！");
        }
        
        return config;
    }

    public boolean isModuleUseable() {
        return moduleUseable;
    }

    public boolean isCZModuleUseable(){
        return czModleUseable;
    }

    private void setModuleUseable(boolean moduleUseable) {
        this.moduleUseable = moduleUseable;
    }

    public void setCzModleUseable(boolean czModleUseable) {
        this.czModleUseable = czModleUseable;
    }
}
