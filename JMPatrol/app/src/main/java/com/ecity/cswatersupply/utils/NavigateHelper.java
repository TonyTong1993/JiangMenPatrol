package com.ecity.cswatersupply.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.mobile.android.bdlbslibrary.utils.BaiDuUtils;
import com.ecity.mobile.android.bdlbslibrary.utils.LocGeoPoint;
import com.ecity.mobile.android.library.gpsengine.GPSEngine;
import com.zzz.ecity.android.applibrary.service.PositionService;

public class NavigateHelper {

    public static final String BAIDU_PACKAGE_NAME = "com.baidu.BaiduMap";
    public static final String GAODE_PACKAGE_NAME = "com.autonavi.minimap";
    public final static int BAIDU_NAVIGATE_APP = 99;
    public final static int BAIDU_NAVIGATE_WEB = 88;
    public final static int BAIDU_ROUTE_PLAN = 77;

    private Activity activity;
    private NavigationHandler handler;
    private LatLng source;

    private enum ENavigateType {
        BAIDU_NAVIGATE_APP(0),
        BAIDU_NAVIGATE_WEB(1),
        BAIDU_ROUTE_PLAN(2);

        final int type;
        ENavigateType(int type) {
            this.type = type;
        }
    }

    public NavigateHelper(Activity activity) {
        this.activity = activity;
        this.handler = new NavigationHandler();
    }

    /**
     * 安装百度地图app时，跳转到百度app导航；
     * @param targetLonLat 经纬度
     */
    public void startBaiduAppNavigtion(double[] targetLonLat) {
        HostApplication.getApplication().submitExecutorService(new BDNavigationRunnable(handler,targetLonLat,ENavigateType.BAIDU_NAVIGATE_APP));
    }

    /**
     * 安装百度地图app时，跳转到百度路径规划；
     * @param sourceLonLat 起点经纬度
     * @param targetLonLat 终点经纬度
     */
    public void startBaiduAppRoutePlan(double[] sourceLonLat, double[] targetLonLat) {
        HostApplication.getApplication().submitExecutorService(new BDNavigationRunnable(handler,sourceLonLat,targetLonLat,ENavigateType.BAIDU_ROUTE_PLAN));
    }

    /**
     * 没安装百度地图app时，跳转到百度网页导航；
     * @param targetLonLat 经纬度
     */
    public void startBaiduWebNavigtion(double[] targetLonLat) {
        HostApplication.getApplication().submitExecutorService(new BDNavigationRunnable(handler,targetLonLat,ENavigateType.BAIDU_NAVIGATE_WEB));
    }

    /**
     * 安装百度地图app时，跳转到百度网页导航；
     * @param sourceLonLat 起点经纬度
     * @param targetLonLat 终点经纬度
     */
    public void startBaiduWebNavigtion(double[] sourceLonLat, double[] targetLonLat) {
        HostApplication.getApplication().submitExecutorService(new BDNavigationRunnable(handler,sourceLonLat,targetLonLat,ENavigateType.BAIDU_NAVIGATE_WEB));
    }

    /**
     * 安装高德地图app时，跳转到高德app导航；
     * @param targetLonLat 经纬度
     */
    public void startMiniNavigtion(double[] targetLonLat) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Uri uri = Uri.parse("androidamap://navi?sourceApplication=appname&lat="+ targetLonLat[1] + "&lon=" + targetLonLat[0] + "&dev=1&style=2");
        intent.setData(uri); 
        activity.startActivity(intent);
    }

    /**
     * 跳转到高德地图路径规划
     * @param sourceLonLat 起点经纬度
     * @param targetLonLat 终点经纬度
     */
    public void startMiniRoutePlan(double[] sourceLonLat, double[] targetLonLat) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Uri uri = Uri.parse("androidamap://route?sourceApplication=softname&slat=" + sourceLonLat[1] + "&slon=" + sourceLonLat[0] + "&dlat=" + targetLonLat[1] + "&dlon=" + targetLonLat[0] + "&dev=0&t=2");
        intent.setData(uri); 
        activity.startActivity(intent);
    }

    private class BDNavigationRunnable implements Runnable{
        private Handler naviHandler;
        private double[] sourceLonLat = new double[2];
        private double[] targetLonLat = new double[2];
        private ENavigateType type;

        public BDNavigationRunnable(Handler naviHandler,double[] targetLonLat, ENavigateType type) {
            super();
            this.naviHandler = naviHandler;
            this.targetLonLat = targetLonLat;
            this.type = type;
        }

        public BDNavigationRunnable(Handler naviHandler, double[] sourceLonLat, double[] targetLonLat, ENavigateType type) {
            this(naviHandler,targetLonLat,type);
            this.sourceLonLat = sourceLonLat;
        }

        @Override
        public void run() {
            if((0 == sourceLonLat[0]) || (0 == sourceLonLat[1])) {
                Location locationStart = PositionService.getLastLocation();
                source = getBaiduLatLng(locationStart.getLongitude(), locationStart.getLatitude());
            } else {
                source = getBaiduLatLng(sourceLonLat[0], sourceLonLat[1]);
            }

            Message msg = Message.obtain();
            msg.obj = getBaiduLatLng(targetLonLat[0], targetLonLat[1]);
            switch (type) {
                case BAIDU_NAVIGATE_APP:
                    msg.what = BAIDU_NAVIGATE_APP;
                    break;
                case BAIDU_NAVIGATE_WEB:
                    msg.what = BAIDU_NAVIGATE_WEB;
                    break;
                case BAIDU_ROUTE_PLAN:
                    msg.what = BAIDU_ROUTE_PLAN;
                    break;
                default:
                    break;
            }
            if(null != naviHandler){
                naviHandler.sendMessage(msg);
            }
        }

        private LatLng getBaiduLatLng(double lon, double lat) {
            LatLng end = null;
              try {
                  LocGeoPoint inPoint2 = new LocGeoPoint(lon, lat, 0);
                  LocGeoPoint outPoint2 = BaiDuUtils.getBaiduGpsPointByGps(inPoint2);
                  end = new LatLng(outPoint2.y, outPoint2.x);
              } catch (Exception e) {
                  e.printStackTrace();
              }
              return end;
        }
    }

    public class NavigationHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int flag = msg.what;

            if (null == activity) {
                return;
            }
            
            switch (flag) {
                case BAIDU_NAVIGATE_APP:
                    navigateByApp(activity, (LatLng)msg.obj);
                    break;
                case BAIDU_NAVIGATE_WEB:
                    navigateByWeb((LatLng)msg.obj);
                    break;
                case BAIDU_ROUTE_PLAN:
                    routePlanByApp(activity, (LatLng)msg.obj);
                    break;
                default:
                    break;
            }
        }
 
        private void routePlanByApp(Context context,LatLng target){
            if(null == target || null == context) {
                return;
            }
            Intent intent = new Intent();
            intent.setData(Uri.parse("baidumap://map/direction?origin=" + source.latitude + "," + source.longitude + "&destination=" + target.latitude + "," + target.longitude + "&mode=driving"));
            context.startActivity(intent);
        }

        private void navigateByApp(Context context,LatLng target){
            if(null == target || null == context) {
                return;
            }
            
            Intent intent = new Intent();
            intent.setData(Uri.parse("baidumap://map/navi?location=" + target.latitude + "," + target.longitude));
            context.startActivity(intent);
        }

        private void navigateByWeb(LatLng target){
            if(null == target || null == source) {
               return;
            }
            startNavi(source, target);
        }
    }

    /**
     * 开始导航
     * @param
     */
    private void startNavi(LatLng pt1, LatLng pt2) {
        // 构建 导航参数
        NaviParaOption para = new NaviParaOption().startPoint(pt1).endPoint(pt2).startName("从这里开始").endName("到这里结束");
        try {
            BaiduMapNavigation.openBaiduMapNavi(para, activity);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }
}
