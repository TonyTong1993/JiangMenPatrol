package com.ecity.mobile.android.bdlbslibrary;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.ecity.mobile.android.bdlbslibrary.model.AddressInfo;
import com.ecity.mobile.android.bdlbslibrary.model.MyPoint;
import com.ecity.mobile.android.bdlbslibrary.utils.BaiDuUtils;
import com.ecity.mobile.android.bdlbslibrary.utils.LocGeoPoint;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

import java.util.ArrayList;
import java.util.List;

public class BDGeoLocator {
    private SDKReceiver mReceiver;
    private PoiSearch mPoiSearch = null;
    private Context context;
    private LocationClient mLocationClient;
    private MyLocationListener myLocationListener;
    private MySuggestListener mySuggestListener;
    private SuggestionSearch mSuggestionSearch = null;
    private AddressInfo addressInfo;
    private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private MyReverseGeoCodeListener myReverseGeoCodeListener;
    private MyPOIListener mPOIListener;
    private List<String> listInfo = null;
    private LocGeoPoint myPoint;
    private static AddressInfo mLastAddressInfo;

    public BDGeoLocator() {

    }

    public BDGeoLocator(Application mContext) {
        this.context = mContext;
        // 注册 SDK 广播监听者
        SDKInitializer.initialize(context.getApplicationContext());
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        context.registerReceiver(mReceiver, iFilter);

        mPOIListener = new MyPOIListener(null);
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(mPOIListener);
    }

    public void destory() {
        destroyPoiSearch();
        stopBDLocation();
        destroyAddressInfoByKey();
        destroyAddressInfoByXY();
        destroySuggestion();
        // 取消监听 SDK 广播
        context.unregisterReceiver(mReceiver);
    }

    /**
     * @version V1.0
     * @Description 用于回调的接口
     * @Author SSA
     * @CreateDate 2015年7月13日
     * @email
     */
    public interface OnNotifyAddressInfo {
        void getAddressInfo(boolean isSuccess, int type, String message, List<AddressInfo> addressInfos);
    }

    /**
     * * 注册定位监听
     *
     * @Description
     * @version V1.0
     */
    private void registerLocation() {
        mLocationClient = new LocationClient(context);
        initLocation();
        myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
    }

    /**
     * * 注册（反）地理编码监听
     *
     * @Description
     * @version V1.0
     */
    private void registerGeo(OnNotifyAddressInfo onNotifyAddressInfo) {
        myReverseGeoCodeListener = new MyReverseGeoCodeListener(onNotifyAddressInfo);
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(myReverseGeoCodeListener);
    }


    private void registerPOI(OnNotifyAddressInfo onNotifyAddressInfo) {
        if (null == mPOIListener) {
            mPOIListener = new MyPOIListener(onNotifyAddressInfo);
        }
        mPOIListener.setOnNotifyAddressInfo(onNotifyAddressInfo);
    }

    public void getPOIAddressInfoByKey(String city, String geoCodeKey, OnNotifyAddressInfo onNotifyAddressInfo, int pageNum, int pageCapacity) {
        registerPOI(onNotifyAddressInfo);

        // 设置检索参数  
        PoiCitySearchOption citySearchOption = new PoiCitySearchOption();
        citySearchOption.city(city);// 城市
        citySearchOption.keyword(geoCodeKey);// 关键字
        citySearchOption.pageCapacity(pageCapacity);// 默认每页10条
        citySearchOption.pageNum(pageNum);// 分页编号
        // 发起检索请求
        mPoiSearch.searchInCity(citySearchOption);
    }

    public void destroyPoiSearch() {
        if (null != mPoiSearch) {
            mPoiSearch.destroy();
        }
    }

    /**
     * * 注册在线建议查询监听
     *
     * @Description
     * @version V1.0
     */
    private void registerSuggestion(OnNotifyAddressInfo onNotifyAddressInfo) {
        mySuggestListener = new MySuggestListener(onNotifyAddressInfo);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(mySuggestListener);

    }

    /**
     * * 开启百度定位
     *
     * @Description
     * @version V1.0
     */
    public void startBDLocation() {
        registerLocation();
        mLocationClient.start();
    }

    /**
     * * 停止百度定位
     *
     * @Description
     * @version V1.0
     */
    public void stopBDLocation() {
        if (null != mLocationClient) {
            mLocationClient.stop();
        }

    }

    /**
     * *根据经纬度查地址
     *
     * @return
     * @throws Exception
     * @Description
     * @version V1.0
     */
    public void startGetAddressInfoByXY(double lng, double lat, OnNotifyAddressInfo onNotifyAddressInfo) throws Exception {

        registerGeo(onNotifyAddressInfo);
        myPoint = new LocGeoPoint(lng, lat, 0.0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*
				 * Message msg=myHandler.obtainMessage(); msg.what=2;
				 * myHandler.sendMessage(msg);
				 */
                try {
                    LocGeoPoint pRet = BaiDuUtils.getBaiduGpsPointByGps(myPoint);
                    LatLng ptCenter = new LatLng(pRet.y, pRet.x);
                    // LatLng ptCenter = new LatLng(lat, lng);
                    // 反Geo搜索
                    mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    /**
     * * 释放
     *
     * @Description
     * @version V1.0
     */
    public void destroyAddressInfoByXY() {
        if (null != mSearch) {
            mSearch.destroy();
        }
    }

    /**
     * * 发起在线建议查询
     *
     * @param keyword
     * @param city
     * @Description
     * @version V1.0
     */
    public void startSuggestion(String keyword, String city, OnNotifyAddressInfo onNotifyAddressInfo) {
        registerSuggestion(onNotifyAddressInfo);
        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(keyword).city(city));

    }

    /**
     * * 释放
     *
     * @Description
     * @version V1.0
     */
    public void destroySuggestion() {
        if (null != mSuggestionSearch) {
            mSuggestionSearch.destroy();
        }
    }

    /**
     * * 开启地理编码查询
     *
     * @Description
     * @version V1.0
     */
    public void getAddressInfoByKey(String city, String geoCodeKey, OnNotifyAddressInfo onNotifyAddressInfo) {
        registerGeo(onNotifyAddressInfo);
        mSearch.geocode(new GeoCodeOption().city(city).address(geoCodeKey));
    }

    /***
     * * 释放
     *
     * @Description
     * @version V1.0
     */
    public void destroyAddressInfoByKey() {
        if (null != mSearch) {
            mSearch.destroy();
        }
    }

    /**
     * * 初始化百度定位参数
     *
     * @Description
     * @version V1.0
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 是否打开GPS
        option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(2000);// 设置发起定位请求的间隔时间为2000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
    }

    public AddressInfo getLastAddressInfo() {
        return mLastAddressInfo;
    }

    /**
     * @version V1.0
     * @Description 实现定位回调监听
     * @Author SSA
     * @CreateDate 2015年7月10日
     * @email
     */
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation arg0) {
            if (null != arg0) {
                addressInfo = new AddressInfo();
                addressInfo.setLatitude(arg0.getLatitude());
                addressInfo.setLongitude(arg0.getLongitude());
                addressInfo.setRadius(arg0.getRadius());
                addressInfo.setAddrStr(arg0.getAddrStr());
                addressInfo.setProvince(arg0.getProvince());
                addressInfo.setGetCity(arg0.getCity());
                addressInfo.setDistrict(arg0.getDistrict());
                addressInfo.setDirection(arg0.getDirection());
                didCoordTrans(new LocGeoPoint(arg0.getLongitude(), arg0.getLatitude(), 0));
                mLastAddressInfo = addressInfo.copy();
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    public static Location fixeLocation = null;
    ;

    private void didCoordTrans(final LocGeoPoint point) {
        new Thread() {
            public void run() {
                MyPoint inPoint = new MyPoint(point.x, point.y, point.z);
                MyPoint outPoint = null;
                try {
                    outPoint = BaiDuUtils.getGpsPointByBaiduGps(inPoint);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if (null != outPoint) {
                    try {
                        fixeLocation = new Location("WGS84");
                        fixeLocation.setLatitude(outPoint.y);
                        fixeLocation.setLongitude(outPoint.x);
                    } catch (Exception e) {
                    }
                }
            }

            ;
        }.start();
    }

    /**
     * @version V1.0
     * @Description（反）地理编码监听
     * @Author SSA
     * @CreateDate 2015年7月15日
     * @email
     */
    private class MyReverseGeoCodeListener implements OnGetGeoCoderResultListener {
        private OnNotifyAddressInfo onNotifyAddressInfo;

        public MyReverseGeoCodeListener(OnNotifyAddressInfo onNotifyAddressInfo) {
            this.onNotifyAddressInfo = onNotifyAddressInfo;
        }

        @Override
        public void onGetGeoCodeResult(GeoCodeResult arg0) {
            if (arg0 == null || arg0.error != SearchResult.ERRORNO.NO_ERROR) {
                notifyResult(false, "fail to geocode", null);
                return;
            }

            addressInfo = new AddressInfo();
            // final MyPoint myPoint = new MyPoint(114.405767,30.51198,0.0);
            final MyPoint myPoint = new MyPoint(arg0.getLocation().longitude, arg0.getLocation().latitude, 0.0);

            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        MyPoint mRet = BaiDuUtils.getGpsPointByBaiduGps(myPoint);
                        addressInfo.setLongitude(mRet.x);
                        addressInfo.setLatitude(mRet.y);
                        List<AddressInfo> results = new ArrayList<AddressInfo>();
                        results.add(addressInfo);
                        notifyResult(true, "", results);
                    } catch (Exception e) {
                        e.printStackTrace();
                        notifyResult(false, e.getMessage(), null);
                    }
                }
            }).start();

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {

            if (arg0 == null || arg0.error != SearchResult.ERRORNO.NO_ERROR || ListUtil.isEmpty(arg0.getPoiList())) {
                notifyResult(false, "fail to reverser geocode", null);
                return;
            }
            PoiInfo info = arg0.getPoiList().get(0);
            addressInfo = new AddressInfo();
            addressInfo.setAddrStr(info.address + "(" + info.name + ")");
            List<AddressInfo> results = new ArrayList<AddressInfo>();
            results.add(addressInfo);
            notifyResult(true, "", results);
        }

        final private void notifyResult(boolean success, String msg, List<AddressInfo> addressInfo) {
            if (null != onNotifyAddressInfo) {
                onNotifyAddressInfo.getAddressInfo(success, 0, "fail to geocode", addressInfo);
            }
        }
    }

    /**
     * @version V1.0
     * @Description POI监听
     * @Author SSA
     * @CreateDate 2015年7月15日
     * @email
     */
    private class MyPOIListener implements OnGetPoiSearchResultListener {
        private OnNotifyAddressInfo onNotifyAddressInfo;

        public MyPOIListener(OnNotifyAddressInfo onNotifyAddressInfo) {
            this.onNotifyAddressInfo = onNotifyAddressInfo;
        }

        public void setOnNotifyAddressInfo(OnNotifyAddressInfo onNotifyAddressInfo) {
            this.onNotifyAddressInfo = onNotifyAddressInfo;
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult arg0) {
        }

        @Override
        public void onGetPoiResult(PoiResult arg0) {
            if (arg0.error == SearchResult.ERRORNO.NO_ERROR) {
                List<AddressInfo> listAddress = new ArrayList<AddressInfo>();
                for (PoiInfo pi : arg0.getAllPoi()) {
                    AddressInfo address = new AddressInfo();
                    address.setLongitude(pi.location.longitude);
                    address.setLatitude(pi.location.latitude);
                    address.setAddrStr(pi.address);
                    address.setTitle(pi.name);
                    address.setCity(pi.city);
                    listAddress.add(address);
                }
                notifyResult(true, "", listAddress);
            } else {
                notifyResult(false, "fail to search poi", null);
                return;
            }
        }

        final private void notifyResult(boolean success, String msg, List<AddressInfo> addressInfo) {
            if (null != onNotifyAddressInfo) {
                onNotifyAddressInfo.getAddressInfo(success, 0, "fail to geocode", addressInfo);
            }
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult arg0) {
            // TODO Auto-generated method stub

        }
    }

    /**
     * @version V1.0
     * @Description 建议地点监听
     * @Author SSA
     * @CreateDate 2015年7月15日
     * @email
     */
    private class MySuggestListener implements OnGetSuggestionResultListener {
        private OnNotifyAddressInfo onNotifyAddressInfo;

        public MySuggestListener(OnNotifyAddressInfo onNotifyAddressInfo) {
            this.onNotifyAddressInfo = onNotifyAddressInfo;
        }

        @Override
        public void onGetSuggestionResult(SuggestionResult arg0) {
            // TODO Auto-generated method stub
            if (arg0 == null || arg0.getAllSuggestions() == null) {
                notifyResult(false, "fail to suggest", null);
                return;
            }

            listInfo = new ArrayList<String>();
            for (SuggestionResult.SuggestionInfo info : arg0.getAllSuggestions()) {
                if (info.key != null) listInfo.add(info.key);
            }
            addressInfo = new AddressInfo();
            addressInfo.setListAddressInfo(listInfo);

            List<AddressInfo> results = new ArrayList<AddressInfo>();
            results.add(addressInfo);

            notifyResult(true, "", results);
        }

        final private void notifyResult(boolean success, String msg, List<AddressInfo> addressInfo) {
            if (null != onNotifyAddressInfo) {
                onNotifyAddressInfo.getAddressInfo(success, 0, "fail to geocode", addressInfo);
            }
        }

    }

    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            Log.d("bdmap", "action: " + s);
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                //Toast.makeText(context,"key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置",Toast.LENGTH_LONG).show();
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                //Toast.makeText(context,"网络出错",Toast.LENGTH_LONG).show();
            }
        }
    }
}
