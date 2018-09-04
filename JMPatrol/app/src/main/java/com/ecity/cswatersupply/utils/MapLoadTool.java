package com.ecity.cswatersupply.utils;

import android.app.Activity;
import android.util.Log;

import com.ecity.cswatersupply.network.response.loginresponse.MobileConfig;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.z3map.maploader.AMapViewLoader;
import com.ecity.z3map.maploader.impl.AGSMapViewLoader;
import com.ecity.z3map.maploader.model.EMapLoadType;
import com.esri.android.map.MapView;
import com.z3app.android.util.FileUtil;

public class MapLoadTool {
    private static AMapViewLoader mapViewLoader;
    /**
     * @param activity
     *            窗口变量
     * @param mapView
     *            地图控件
     * @param mapConfig
     *            地图配置
     * @return 取地图配置中的全部信息
     */
    public static void LoadMap(Activity activity, MapView mapView, MobileConfig mapConfig,AMapViewLoader.Z3MaploadCallback callback) {
        try {
            FileUtil.getInstance(activity).getLocalMapDocPath(); // 内部回初始化本地地图路径
        } catch (Exception e) {
            Log.e("MapLoadTool", e.getMessage(), e);
        }

        try {
            mapView.removeAll();
            if ((mapConfig != null) && (!ListUtil.isEmpty(mapConfig.getSourceConfigArrayList()))) {
                loadLayers(activity, mapView, mapConfig,callback);
            }
            
            String url = ServiceUrlManager.getInstance().getSpacialSearchUrl();
            MetaDownloadUtil.LoadMapMetas(url);
        } catch (Exception e) {
            Log.e("MapLoadTool", e.getMessage(), e);
        }
    }

    private static void loadLayers(Activity activity, MapView mapView, MobileConfig mapConfig,AMapViewLoader.Z3MaploadCallback callback) {
        if(null == mapViewLoader) {
            mapViewLoader = new AGSMapViewLoader();
        }

        try {
            mapViewLoader.loadMap(mapConfig.getSourceConfigArrayList(),mapView, mapConfig.getMapLoadType(),callback);
        } catch (Exception e) {
            Log.e("MapLoadTool", e.getMessage(), e);
        }
    }


}
