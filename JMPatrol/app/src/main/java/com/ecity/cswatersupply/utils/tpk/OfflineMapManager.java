package com.ecity.cswatersupply.utils.tpk;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.network.FileDownloader;
import com.ecity.cswatersupply.utils.HttpUtils;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.NetWorkHelper;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.z3map.maploader.model.SourceConfig;
import com.z3app.android.util.FileUtil;
import com.z3app.android.util.PreferencesUtil;
import com.z3app.android.util.StringUtil;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OfflineMapManager {
    private static final String KEY_LAYER_NAME = "KEY_LAYER_NAME";
    private static final String KEY_LOCAL_PATH = "KEY_LOCAL_PATH";
    private int currentLayerIndex;
    List<String> tpkUrls = new ArrayList<String>();
    List<String> layerNames = new ArrayList<String>();
    private IOfflineMapResourceCallback callback = new DefaultCallback();
    private String localPathBasic = FileUtil.getInstance(null).getSDPATH() + "//ECity//CSWaterSupply//map//mapSDK//";
    Map<String, String> layerName2TpkUrls = new HashMap<String, String>();
    private Context context = HostApplication.getApplication().getApplicationContext();
    private String targetAbsolutePath;
    private static OfflineMapManager instance;

    public interface IOfflineMapResourceCallback {
        void notifyNoResourceToDownload();

        void notifyNotEnoughDiskSpace();

        void notifyProgressUpdate(String layerName, String tpkUrl, float progress);

        void notifyNotValidNetWork();

        void onSigleComplete(String layerName, String localPath);

        void onComplete(String localPath);

        void onError(String layerName, String tpkUrl, String errorMsg);
    }

    public static OfflineMapManager getInstance(Context context) {
        synchronized (OfflineMapManager.class) {
            if (null == instance) {
                instance = new OfflineMapManager();
            }
        }
        return instance;
    }

    private OfflineMapManager() {
    }

    public void download() {
        deleteInvalidFiles();
        if (!NetWorkHelper.isWifi(HostApplication.getApplication().getApplicationContext())) {
            callback.notifyNotValidNetWork();
        }
        if (!hasNewResources()) {
            callback.notifyNoResourceToDownload();
        }
        if (!hasEnoughSpace()) {
            callback.notifyNotEnoughDiskSpace();
            return;
        }

        EventBusUtil.register(this);
        downloadNextFile();
    }

    public List<MapOfflineBean> getListDataSource() {
        List<MapOfflineBean> resources = new ArrayList<MapOfflineBean>();
        ArrayList<SourceConfig> configs = HostApplication.getApplication().getConfig().getSourceConfigArrayList();
        List<String> tpkUrls = new ArrayList<String>();
        List<String> layerNames = new ArrayList<String>();
        for (SourceConfig confi : configs) {
            if (!StringUtil.isEmpty(confi.tpkUrl)) {
                layerNames.add(confi.serverName);
                tpkUrls.add(confi.tpkUrl);
            }
        }
        for (int i = 0; i < layerNames.size(); i++) {
            String layerName = PreferencesUtil.getString(context, KEY_LAYER_NAME, "");
            String[] names = layerName.split(";");
            if (StringUtil.isBlank(layerName)) {
                layerName2TpkUrls.put(layerNames.get(i), tpkUrls.get(i));
                MapOfflineBean resource = new MapOfflineBean();
                resource.setMapTpkName(layerNames.get(i));
                resource.setTpkUrl(tpkUrls.get(i));
                resource.setProgress(0);
                resources.add(resource);
            } else {
                int j = 0;
                while (j < names.length) {
                    if (!names[j].equals(layerNames.get(i))) {
                        layerName2TpkUrls.put(layerNames.get(i), tpkUrls.get(i));
                        MapOfflineBean resource = new MapOfflineBean();
                        resource.setMapTpkName(layerNames.get(i));
                        resource.setTpkUrl(tpkUrls.get(i));
                        resource.setProgress(0);
                        resources.add(resource);
                    }
                    j++;
                }
            }
        }
        deleteInvalidFiles();

        return resources;
    }

    public String getTpkLocalPath(SourceConfig config) {
        String pathCache = PreferencesUtil.getString(context, KEY_LOCAL_PATH, "");
        String localPath = null;
        if (!StringUtil.isBlank(pathCache) && !StringUtil.isBlank(config.tpkUrl)) {
            String[] paths = pathCache.split(";");
            String urlTpkName = config.tpkUrl.split("/")[config.tpkUrl.split("/").length - 1];
            for (String path : paths) {
                String localTpkName = path.split("/")[path.split("/").length - 1];
                if (urlTpkName.equals(localTpkName)) {
                    localPath = localPathBasic + urlTpkName;
                }
            }
        }

        if(StringUtil.isBlank(localPath)) {
            if(null !=  config && ! StringUtil.isBlank(config.tpkUrl)) {
                String urlTpkName = config.tpkUrl.split("/")[config.tpkUrl.split("/").length - 1];
                localPath = FileUtil.getInstance(null).getLocalMapDocPath() + urlTpkName;
            }
        }

        return localPath;
    }

    private boolean hasNewResources() {
        return !layerName2TpkUrls.isEmpty();
    }

    private boolean hasEnoughSpace() {
        float availableDiskSize = getAvailSpace();
        return (availableDiskSize > requestResourcesSize());
    }

    @SuppressWarnings("deprecation")
    private float getAvailSpace() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        StatFs statFs = new StatFs(path);
        float size = statFs.getBlockSize();
        float blocks = statFs.getAvailableBlocks();
        float result = blocks * size;

        return result;
    }

    private List<String> getValidLayerNames() {
        Set<String> nameSet = layerName2TpkUrls.keySet();
        List<String> names = new ArrayList<String>();
        for (Iterator<String> iterator = nameSet.iterator(); iterator.hasNext();) {
            String name = iterator.next();
            names.add(name);
        }
        return names;
    }

    private float requestResourcesSize() {
        int totalSize = 0;
        Set<String> keys = layerName2TpkUrls.keySet();
        for (String key : keys) {
            String url = layerName2TpkUrls.get(key);
            HttpURLConnection connection = HttpUtils.getConnection(url);
            long tpkSize = (connection == null) ? 0 : connection.getContentLength();
            totalSize += tpkSize;
        }

        return totalSize;
    }

    private void downloadNextFile() {
        String layerName = getValidLayerNames().get(currentLayerIndex);
        String url = layerName2TpkUrls.get(layerName);
        String tpkName = url.split("/")[url.split("/").length - 1];
        targetAbsolutePath = localPathBasic + tpkName;
        FileDownloader.execute(url, targetAbsolutePath, OfflineMapManager.class);
    }

    public void deleteInvalidFiles() {
        deleteInvalidFiles1();
        deleteInvalidFiles2();
        deleteInvalidFiles3();
    }

    //删除缓存里有，但是文件不存在，更新缓存
    private void deleteInvalidFiles1() {
        String layerName = PreferencesUtil.getString(context, KEY_LAYER_NAME, "");
        if (!layerName.isEmpty()) {
            String[] names = layerName.split(";");
            for (int i = 0; i < names.length; i++) {
                String url = layerName2TpkUrls.get(getValidLayerNames().get(i));
                String tpkName = url.split("/")[url.split("/").length - 1];
                targetAbsolutePath = localPathBasic + tpkName;
                File file = new File(targetAbsolutePath);
                if (!names[i].isEmpty() && !file.exists()) {
                    PreferencesUtil.putString(context, KEY_LAYER_NAME, "");
                }
            }
        }
    }

    //删除缓存里没有记录的文件
    private void deleteInvalidFiles2() {
        String layerName = PreferencesUtil.getString(context, KEY_LAYER_NAME, "");
        if (!layerName.isEmpty()) {
            String[] names = layerName.split(";");
            for (int i = 0; i < names.length; i++) {
                String url = layerName2TpkUrls.get(names[i]);
                String tpkName = url.split("/")[url.split("/").length - 1];
                targetAbsolutePath = localPathBasic + tpkName;
                File file = new File(targetAbsolutePath);
                if (names[i].isEmpty() && file.exists()) {
                    FileUtil.deleteFile(file);
                }
            }
        }
    }

    //删除缓存里有、文件有以及服务没有的缓存和文件
    private void deleteInvalidFiles3() {
        ArrayList<SourceConfig> configs = HostApplication.getApplication().getConfig().getSourceConfigArrayList();
        String layerName = PreferencesUtil.getString(context, KEY_LAYER_NAME, "");
        if (!layerName.isEmpty()) {
            String[] names = layerName.split(";");
            for (int i = 0; i < names.length; i++) {
                for (SourceConfig confi : configs) {
                    String url = layerName2TpkUrls.get(names[i]);
                    String tpkName = url.split("/")[url.split("/").length - 1];
                    targetAbsolutePath = localPathBasic + tpkName;
                    File file = new File(targetAbsolutePath);
                    if (confi.serverName.equals(names[i]) && confi.tpkUrl.isEmpty() && file.exists()) {
                        FileUtil.deleteFile(file);
                        PreferencesUtil.putString(context, KEY_LAYER_NAME, "");
                    }
                }
            }
        }
    }

    public void deleteCurrentFile() {
        String layerName = getValidLayerNames().get(currentLayerIndex);
        String url = layerName2TpkUrls.get(layerName);
        String tpkName = url.split("/")[url.split("/").length - 1];
        targetAbsolutePath = localPathBasic + tpkName;
        FileUtil.deleteFile(new File(targetAbsolutePath));
    }

    public void setCallback(IOfflineMapResourceCallback callback) {
        if (callback == null) {
            callback = new DefaultCallback();
        }

        this.callback = callback;
    }

    private class DefaultCallback implements IOfflineMapResourceCallback {

        @Override
        public void notifyNoResourceToDownload() {
        }

        @Override
        public void notifyNotEnoughDiskSpace() {
        }

        @Override
        public void notifyProgressUpdate(String layerName, String tpkUrl, float progress) {
        }

        @Override
        public void onComplete(String localPath) {
        }

        @Override
        public void onError(String layerName, String tpkUrl, String errorMsg) {
        }

        @Override
        public void notifyNotValidNetWork() {

        }

        @Override
        public void onSigleComplete(String layerName, String localPath) {

        }
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isForTarget(this)) {
            return;
        }

        if (!event.isOK()) {
            String layerName = getValidLayerNames().get(currentLayerIndex);
            String url = layerName2TpkUrls.get(layerName);
            callback.onError(layerName, url, event.getMessage());
            deleteCurrentFile();
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.FILE_OPERATION_DOWNLOAD_FINISH:
                handleDownoadFinish();
                break;
            case ResponseEventStatus.FILE_OPERATION_UPDATE_PROGRESS:
                handleDownoadProgressUpdate(event);
                break;
            default:
                break;
        }
    }

    private void handleDownoadProgressUpdate(ResponseEvent event) {
        String layerName = getValidLayerNames().get(currentLayerIndex);
        String url = layerName2TpkUrls.get(layerName);
        float progress = Float.valueOf(event.getMessage());
        callback.notifyProgressUpdate(layerName, url, progress);
        String msg = ResourceUtil.getStringById(R.string.map_offline_download_loading);
        LoadingDialogUtil.updateMessage(msg);
    }

    private void handleDownoadFinish() {
        String layerName = getValidLayerNames().get(currentLayerIndex);
        String url = layerName2TpkUrls.get(layerName);
        String tpkName = url.split("/")[url.split("/").length - 1];
        targetAbsolutePath = localPathBasic + tpkName;
        currentLayerIndex++;
        callback.onSigleComplete(layerName, targetAbsolutePath);
        if (currentLayerIndex == getValidLayerNames().size()) {
            callback.onComplete(targetAbsolutePath);
        } else {
            downloadNextFile();
        }
    }
}
