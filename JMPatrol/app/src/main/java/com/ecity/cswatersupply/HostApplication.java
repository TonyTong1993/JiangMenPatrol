package com.ecity.cswatersupply;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.db.PlanTaskDao;
import com.ecity.cswatersupply.db.PlanTaskLinePartDao;
import com.ecity.cswatersupply.db.PlanTaskPointPartDao;
import com.ecity.cswatersupply.emergency.EQModuleConfig;
import com.ecity.cswatersupply.handler.CrashHandler;
import com.ecity.cswatersupply.menu.MenuFactory;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.response.loginresponse.MobileConfig;
import com.ecity.cswatersupply.project.FuZhouPatrolModuleConfig;
import com.ecity.cswatersupply.project.ProjectModuleConfig;
import com.ecity.cswatersupply.project.model.db.AttachmentDao;
import com.ecity.cswatersupply.service.CORSLocationService;
import com.ecity.cswatersupply.utils.AppUtil;
import com.ecity.cswatersupply.utils.FileCleaner;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.PropertyChangeManager;
import com.ecity.cswatersupply.utils.SettingsManager;
import com.ecity.cswatersupply.xg.XGPushUtil;
import com.ecity.cswatersupply.xg.util.NotificationFunctionMappingFactory;
import com.ecity.cswatersupply.xg.util.NotificationUtil;
import com.ecity.mobile.android.bdlbslibrary.LocationPluginMain;
import com.squareup.leakcanary.watcher.RefWatcher;
import com.z3app.android.util.FileUtil;
import com.zzz.ecity.android.applibrary.MyApplication;
import com.zzz.ecity.android.applibrary.database.GPSPositionDao;
import com.zzz.ecity.android.applibrary.database.TablesBuilder;
import com.zzz.ecity.android.applibrary.service.PositionService;

import java.util.List;

import static com.ecity.cswatersupply.Constants.TARGET_SERVER_NONE;
import static com.ecity.cswatersupply.Constants.TARGET_SERVER_NORMAL;

public class HostApplication extends MyApplication {
    private static final int MAX_DIR_SIZE_MB = 100;// 文件路径所占最大空间
    private static final int MAX_FILE_EXSIST_DAY = 20;// 单个文件存在的最长时间（天）
    private static final int MAX_SINGLE_FILE_SIZE_MB = 2 * MAX_DIR_SIZE_MB / MAX_FILE_EXSIST_DAY;// 单个文件最大空间
    public static boolean vpnTest = false;//从属性来看 方法vpnTestInit 是一段废代码 @hxx

    private static HostApplication application;
    private static User currentUser;
    private MobileConfig config;
    private LocationPluginMain mLocationPluginMain = null;
    private List<String> downloadingFiles;

    public ProjectStyle projectStyle;

    // 在自己的Application中添加如下代码
    public static RefWatcher getRefWatcher(Context context) {
        HostApplication application = (HostApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    // 在自己的Application中添加如下代码
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        application = this;
        super.onCreate();
        SDKInitializer.initialize(this);
        CrashHandler.getInstance().init(this);// 这儿引起了内存泄漏，因为内部用了对话框，只能传ActivityContext
        // 在自己的Application中添加如下代码
        refWatcher = RefWatcher.DISABLED; // 在调试模式，使用 refWatcher = LeakCanary.install(this);
        setUpLocationPlugin();
        initLogger();
        initResources();
        initModuleConfig();
        if (!SettingsManager.getInstance().isServerTypeSet()) {
            LogUtil.i(this, "Will change server type.");
            SettingsManager.getInstance().setServerType(TARGET_SERVER_NONE);
            SettingsManager.getInstance().setLastEnvironmentIndex(1);
            SettingsManager.getInstance().setServerTypeSetStatus(true);
        } else {
            LogUtil.i(this, "No need to change server type.");
        }
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /***
     * 初始化模块配置，如果有些模块不需要，可以移除代码或者在此配置是否使用
     */
    private void initModuleConfig() {
        try {
            EQModuleConfig.init(false, false);//均1false2true常州地震，第1true2false武汉地震
            FuZhouPatrolModuleConfig.init(false);
            ProjectModuleConfig.init(false);
            vpnTestInit(false);
            setCurrentProject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCurrentProject() {
        try {
            if (EQModuleConfig.getConfig().isCZModuleUseable()) {
                projectStyle = ProjectStyle.PROJECT_CZDZ;
            } else if (EQModuleConfig.getConfig().isModuleUseable()) {
                projectStyle = ProjectStyle.PROJECT_WHDZ;
            }else if (FuZhouPatrolModuleConfig.getConfig().isModuleUseable()){
                projectStyle = ProjectStyle.PROJECT_FZXJ;
            }else{
                projectStyle = ProjectStyle.PROJECT_JMWQ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ProjectStyle getProjectStyle() {
        return projectStyle;
    }


    public enum ProjectStyle {
        PROJECT_WHDZ, PROJECT_CZDZ, PROJECT_FZXJ, PROJECT_JMGC,PROJECT_JMWQ,
    }

    /***
     * 初始化数据库
     *
     * @return
     */

    @Override
    public void customDatabaseInit() {
        TablesBuilder.analyzeTablesXML(getResources(), R.xml.tables);

        try {
            getDatabase(GPSPositionDao.TB_NAME).createTable(TablesBuilder.getTableByName(GPSPositionDao.TB_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            getDatabase(PlanTaskDao.TB_NAME).createTable(TablesBuilder.getTableByName(PlanTaskDao.TB_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            getDatabase(PlanTaskLinePartDao.TB_NAME).createTable(TablesBuilder.getTableByName(PlanTaskLinePartDao.TB_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            getDatabase(PlanTaskPointPartDao.TB_NAME).createTable(TablesBuilder.getTableByName(PlanTaskPointPartDao.TB_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            getDatabase(AttachmentDao.TABLE_NAME).createTable(TablesBuilder.getTableByName(AttachmentDao.TABLE_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLowMemory() {
        LogUtil.w(this, "Low Memory. memoryUsage=" + AppUtil.getMemoryUsage() + "M");
        super.onLowMemory();

        //TODO
    }

    public static HostApplication getApplication() {
        return application;
    }

    private void setUpLocationPlugin() {
        mLocationPluginMain = new LocationPluginMain(getApplication());
        mLocationPluginMain.startBDLocation();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        HostApplication.currentUser = currentUser;
    }

    public LocationPluginMain getLocationPluginMain() {
        return mLocationPluginMain;
    }

    public void setLocationPluginMain(LocationPluginMain mLocationPluginMain) {
        this.mLocationPluginMain = mLocationPluginMain;
    }

    private void initResources() {
        MenuFactory.analyzeTablesXML(getResources());
        NotificationFunctionMappingFactory.parseConfigXml(this);
    }

    public MobileConfig getConfig() {
        return config;
    }

    public void setConfig(MobileConfig config) {
        this.config = config;
    }

    private void initLogger() {
        final String logPath = FileUtil.getInstance(null).getSDPATH() + "//ECity//CSWaterSupply//Log//";
        LogUtil.init(logPath, Constants.LOG_NAME, Constants.LOG_SUFFIX, LogUtil.LOG_LEVEL_INFO);
        LogUtil.v(this, "Logger has been initialised.");
        FileCleaner cleaner = new FileCleaner(logPath, MAX_DIR_SIZE_MB, MAX_FILE_EXSIST_DAY, MAX_SINGLE_FILE_SIZE_MB);
        cleaner.work();
    }

    /*//    public Database getZ3Database() {
    //        return mDatabase;
    //    }
    */
    public void doOtherthingBeforeExist(Context context) {
        XGPushUtil.unregisterPush(context);
        PropertyChangeManager.getInstance().removeAllListeners();
        try {
            if (FuZhouPatrolModuleConfig.getConfig().isModuleUseable()) {
                CORSLocationService.stopInstance(context);
            } else {
                PositionService.stopInstance(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        NotificationUtil.clearNotification(context);
    }

    public void addDownloadingFile(String uniqueId) {
        downloadingFiles = ListUtil.getSafeList(downloadingFiles);
        downloadingFiles.add(uniqueId);
    }

    public void removeDownloadingFile(String uniqueId) {
        downloadingFiles = ListUtil.getSafeList(downloadingFiles);
        downloadingFiles.remove(uniqueId);
    }

    /**
     * @param uniqueId
     * @return
     */
    public boolean isDownloadingFile(String uniqueId) {
        downloadingFiles = ListUtil.getSafeList(downloadingFiles);

        return downloadingFiles.contains(uniqueId);
    }

    private void vpnTestInit(boolean flag) {
        vpnTest = flag;
    }
}
