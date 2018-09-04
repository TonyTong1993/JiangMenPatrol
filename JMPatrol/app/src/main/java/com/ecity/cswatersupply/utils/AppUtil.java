package com.ecity.cswatersupply.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.service.AppVersionCheckRunnable;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.zzz.ecity.android.applibrary.MyApplication;

public class AppUtil {
    public static String getVersion(Context context) {
        String version = "";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(context, e);
        }

        return version;
    }

    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static void deleteShortcut() {
        Context context = MyApplication.getApplication();
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        String title = null;
        try {
            final PackageManager pm = context.getPackageManager();
            title = pm.getApplicationLabel(pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)).toString();
        } catch (Exception e) {
        }
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        Intent shortcutIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        context.sendBroadcast(shortcut);
    }

    public static void addShortcut() {
        Context context = MyApplication.getApplication();
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        Intent shortcutIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        String title = null;
        try {
            final PackageManager pm = context.getPackageManager();
            title = pm.getApplicationLabel(pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)).toString();
        } catch (Exception e) {
        }
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        shortcut.putExtra("duplicate", false);
        Parcelable iconResource = Intent.ShortcutIconResource.fromContext(context, R.drawable.ic_launcher);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);

        context.sendBroadcast(shortcut);
    }

    public static boolean hasShortcut(String name, String authority) {
        Context context = MyApplication.getApplication();
        String url = "";
        if (authority == null) {
            if (android.os.Build.VERSION.SDK_INT < 8) {
                url = "content://com.android.launcher.settings/favorites?notify=true";
            } else {
                url = "content://com.android.launcher2.settings/favorites?notify=true";
            }
        } else
            url = "content://" + authority + "/favorites?notify=true";
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Uri.parse(url), null, "title=?", new String[] { name }, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }

    public static boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) MyApplication.getApplication().getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）  
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isGPSAllowed() {
        Context context = MyApplication.getApplication();
        boolean isGPSAllowed = false;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）  
        isGPSAllowed = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）  
        //boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);  

        return isGPSAllowed;
    }

    public static String getAuthorityFromPermission(String permission) {
        Context context = MyApplication.getApplication();
        if (permission == null) {
            return null;
        }
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
        if (packs != null) {
            for (PackageInfo pack : packs) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        if (permission.equals(provider.readPermission)) {
                            return provider.authority;
                        }
                        if (permission.equals(provider.writePermission)) {
                            return provider.authority;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * @return memory usage of current application (in M)
     */
    public static float getMemoryUsage() {
        long byteSize = android.os.Debug.getNativeHeapAllocatedSize();
        float allocatedMemorySize = byteSize / (1024 * 1024);

        return allocatedMemorySize;
    }
    /***
     * check app version update
     * @param uiHandler notify check finish
     */
    public synchronized static void checkAppUpdate(final Handler uiHandler){
        AppVersionCheckRunnable runnable = new AppVersionCheckRunnable(uiHandler, ServiceUrlManager.getAppVersionCheckUrl());
        HostApplication.getApplication().submitExecutorService(runnable);
    }

    /** 检查手机上是否安装了指定包名的软件
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName){
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if(packageInfos != null){
            for(int i = 0; i < packageInfos.size(); i++){
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /***
     * 根据包名启动app
     * @param packagename 
     */
    public static void doStartApplicationWithPackageName(String packagename) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等  
        PackageInfo packageinfo = null;  
        try {  
            packageinfo = HostApplication.getApplication().getPackageManager().getPackageInfo(packagename, 0);
        } catch (NameNotFoundException e) {  
            e.printStackTrace();  
        }  
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent  
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历  
        List<ResolveInfo> resolveinfoList = HostApplication.getApplication().getPackageManager().queryIntentActivities(resolveIntent, 0);  
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname  
            String packageName = resolveinfo.activityInfo.packageName;  
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent  
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径  
            ComponentName cn = new ComponentName(packageName, className);  
            intent.setComponent(cn);
            UIHelper.startActivityWithItent(intent);
        }  
    } 

    /**
     *  @Description    : 这个包名的程序是否在运行
     *  @param context 上下文
     *  @param packageName 判断程序的包名
     *  必须加载的权限    <uses-permission android:name="android.permission.GET_TASKS"> 
     *  @return         : boolean
     */
    public static boolean isRunningApp(String packageName) {
        ActivityManager activityManager = (ActivityManager) HostApplication.getApplication().getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
             if (appProcess.processName.equals(packageName)) {
                 return true;
               }
        }
        return false;
    }

    /**
     * 根据app包名返回app的图标
     * @return
     */
    public static Drawable getAppIcon(String pkgName){
        Drawable drawable = null;
        PackageManager pm = HostApplication.getApplication().getPackageManager();  
        List<PackageInfo>  packgeInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);   
        for(PackageInfo packgeInfo : packgeInfos){
            if(packgeInfo.packageName.equalsIgnoreCase(pkgName)){
                drawable = packgeInfo.applicationInfo.loadIcon(pm); 
                break;
            }
        }
        
        return drawable;
    }

}
