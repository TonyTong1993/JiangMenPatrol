package com.ecity.cswatersupply.network;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.Toast;

import com.ecity.android.httpforandroid.http.networkstatus.NetWorkUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.map.SearchResult;
import com.ecity.cswatersupply.utils.CoordTransfer;
import com.ecity.mobile.android.bdlbslibrary.LocationPluginMain.OnNotifyAddressInfo;
import com.ecity.mobile.android.bdlbslibrary.model.AddressInfo;
import com.ecity.mobile.android.bdlbslibrary.model.MyPoint;
import com.ecity.mobile.android.bdlbslibrary.utils.BaiDuUtils;
import com.ecity.zzz.pipegps.model.GpsXYZ;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.z3app.android.util.StringUtil;

//地名定位
public class GeoLocatorListener {
    private static LinkedBlockingDeque<SearchResult> safeResult = new LinkedBlockingDeque<SearchResult>();
    private WeakReference<Activity> outer;
    private static ArrayList<SearchResult> searchResults;
    private List<AddressInfo> addrSearchResult;
    private List<AddressInfo> addrSearchResultWGS84;
    private Handler uiHandler;
    boolean isRun = false;

    private GeoLocatorListener() {
        super();
    }

    public GeoLocatorListener(final Activity activity, final Handler uiHandler) {
        this();
        this.uiHandler = uiHandler;
        outer = new WeakReference<Activity>(activity);
    }

    public synchronized void didSearchWithKeywords(String keywords, int pageNum) {

        if (isRun) { 
            return;           
        } 
        
        if (null == getActivity()) {
            hideProgressPopu();          
            return;
        }

        showProgressPopu();        
        if (NetWorkUtil.isNetworkAvailable(getActivity())) {
            searchByInternet(keywords, pageNum);
        }       
    }

    public void showProgressPopu() {
        isRun = true;
    }

    public void hideProgressPopu() {
        isRun = false;
    }

    private void searchByInternet(String keywords, int pageNum) {
        try {
            if (null == HostApplication.getApplication().getLocationPluginMain()) {
                hideProgressPopu();
                Toast.makeText(getActivity(), HostApplication.getApplication().getString(R.string.init_search_error), Toast.LENGTH_LONG).show();
                return;
            }
        } catch (Exception e) {
        }

        if (null == addrSearchResult) {
            addrSearchResult = new ArrayList<AddressInfo>();
        }
        addrSearchResult.clear();

        if (StringUtil.isEmpty(keywords)) {
            hideProgressPopu();
            return;
        }
        String cityName = "";
        String keyName = "";
        String split = ",";

        if (keywords.contains(",")) {
            split = ",";
        } else if (keywords.contains("，")) {
            split = "，";
        } else if (keywords.contains(";")) {
            split = ";";
        } else if (keywords.contains("；")) {
            split = "；";
        } else if (keywords.contains(":")) {
            split = ":";
        } else if (keywords.contains("：")) {
            split = "：";
        }

        String[] strs = keywords.split(split);

        try {
            if (strs.length > 1) {
                cityName = strs[0];
                for (int i = 1; i < strs.length; i++) {
                    keyName += strs[i];
                }
            } else if (null != HostApplication.getApplication()
                    .getLocationPluginMain().getLastAddressInfo()) {
                cityName = HostApplication.getApplication()
                        .getLocationPluginMain().getLastAddressInfo().getCity();
                keyName = keywords;
            } else {
                cityName = null;
                keyName = keywords;
            }
        } catch (Exception e) {
        }

        if (StringUtil.isEmpty(cityName)) {
            hideProgressPopu();
            Toast.makeText(getActivity(),"未定位城市信息，请用\"城市名+(,或;或:)+关键字\"格式输入查询！", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            HostApplication.getApplication().getLocationPluginMain()
                    .getPOIAddressInfoByKey(cityName, keyName,
                            new OnNotifyAddressInfo() {
                                @Override
                                public void getAddressInfo(boolean isSuccess,int type, String message,
                                        List<AddressInfo> addressInfos) {
                                    if (isSuccess) {
                                        if (null != addressInfos) {
                                            for (AddressInfo addressInfo : addressInfos) {
                                                addrSearchResult.add(addressInfo);
                                            }

                                            didCoordTrans();
                                        }
                                    } else {
                                        if (null != uiHandler) {
                                            uiHandler.sendEmptyMessage(1);
                                        }
//                                        Toast.makeText(getActivity(),"没有相关的地名及道路！",Toast.LENGTH_LONG).show();
                                        hideProgressPopu();
                                    }
                                }
                            }, pageNum);           
        } catch (Exception e) {
        }      
    }

    private void didCoordTrans() {
        if (null == addrSearchResult || addrSearchResult.size() < 1) {
            hideProgressPopu();
            return;
        }

        if (null == addrSearchResultWGS84) {
            addrSearchResultWGS84 = new ArrayList<AddressInfo>();
        }
        addrSearchResultWGS84.clear();
        new Thread(runnable).start();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (null != addrSearchResult) {
                for (AddressInfo addressInfo : addrSearchResult) {
                    MyPoint tpoint = null;
                    try {
                        tpoint = BaiDuUtils.getGpsPointByBaiduGps(new MyPoint(addressInfo.getLongitude(), addressInfo.getLatitude(), 0));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    if (null != tpoint) {
                        AddressInfo newAddressInfo = addressInfo.copy();
                        newAddressInfo.setLongitude(tpoint.x);
                        newAddressInfo.setLatitude(tpoint.y);
                        addrSearchResultWGS84.add(newAddressInfo);
                    }
                }
                organizResult();
            }
        }
    };

    private void organizResult() {
        hideProgressPopu();
        if (null == addrSearchResultWGS84 || addrSearchResultWGS84.size() < 1) {
            return;
        }
        safeResult.clear();
        /**
         * 用于设置图形属性在外部范围是0-9，使用时要改为1-10 而且这个图形在点的时候才能显示
         */
        int pointIndex = 0;
        for (AddressInfo addressInfo : addrSearchResultWGS84) {
            SearchResult tempResult = new SearchResult();
            // 一个结果的标题名称
            Map<String, Object> hashMap = new HashMap<String, Object>();

            // hashMap.put("", addressInfo.getTitle()); // 22.570766845861925, 113.07130995575653
            hashMap.put("城市", addressInfo.getCity());
            hashMap.put("地名", addressInfo.getAddrStr());
            hashMap.put("名称", addressInfo.getTitle());
            String acu = "";
            DecimalFormat df = new DecimalFormat("#.0");
            acu = df.format(addressInfo.getRadius());
            hashMap.put("精度", acu);

            tempResult.titleName = addressInfo.getTitle();
            tempResult.address = addressInfo.getAddrStr();
            
            GpsXYZ xy = transToLocalPosition(addressInfo.getLongitude(),addressInfo.getLatitude());
            
            Point AGSpt = new Point(xy.getX(), xy.getY());
            Graphic AGSpgt = null;  // 56677.311728418535, 25377.863534319316
            try {
                AGSpgt = new Graphic(AGSpt,
                        PIPENETPO_POINT_PICTURE((pointIndex % 10 + 1)),
                        hashMap, null);
                // 添加到查询结果集
                tempResult.graphic = AGSpgt;
                tempResult.centerPoint = AGSpt;
                safeResult.add(tempResult);
                // 下一条记录显示的标示符
                pointIndex++;
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            }
        }
        if (null != uiHandler) {
            uiHandler.sendEmptyMessage(0);
        }

    }

    public PictureMarkerSymbol PIPENETPO_POINT_PICTURE(int index)
            throws IOException {
        PictureMarkerSymbol POINT_PICTURE = null;
        try {
            POINT_PICTURE = new PictureMarkerSymbol(
                    getDrawableByName("icon_mark" + String.valueOf(index)));
            POINT_PICTURE.setOffsetY(16);
            POINT_PICTURE.setAngle(0);
        } catch (Exception e) {

        }
        return POINT_PICTURE;
    }

    private Drawable getDrawableByName(String name) {
        int id = -1;
        Resources res = HostApplication.getApplication().getResources();
        try {
            id = res.getIdentifier(name, "drawable", HostApplication
                    .getApplication().getPackageName());

        } catch (Exception e) {
        }
        return res.getDrawable(id);
    }

    private GpsXYZ transToLocalPosition(double longitude, double latitude) {
        GpsXYZ xy = new GpsXYZ();

        double[] txy = CoordTransfer.transToLocal(latitude, longitude);
        xy.setX(txy[0]);
        xy.setY(txy[1]);
        return xy;
    }

    // 弹出地名、道路名查询窗口
    public synchronized static List<SearchResult> getLastSearchResults() {

        if (null == searchResults) {
            searchResults = new ArrayList<SearchResult>();
        }

        int size = safeResult.size();
        if (size < 1) {
            return searchResults;
        }
        searchResults.clear();

        for (int i = 0; i < size; i++) {
            try {
                searchResults.add(safeResult.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return searchResults;
    }

    private Activity getActivity() {
        if (null == outer) {
            return null;
        }
        return outer.get();
    }
}
