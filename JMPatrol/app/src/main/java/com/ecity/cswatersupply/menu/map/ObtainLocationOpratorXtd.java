package com.ecity.cswatersupply.menu.map;

import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.model.ResultCode;
import com.ecity.cswatersupply.model.checkitem.PatrolPosition;
import com.ecity.cswatersupply.project.FuZhouPatrolModuleConfig;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.ui.widght.AddressIcomView;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView.BtnStyle;
import com.ecity.cswatersupply.ui.widght.MapNavigationView;
import com.ecity.cswatersupply.utils.CoordTransfer;
import com.ecity.cswatersupply.utils.CurrentLocationManager;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.mobile.android.bdlbslibrary.BDGeoLocator;
import com.ecity.mobile.android.bdlbslibrary.BDGeoLocator.OnNotifyAddressInfo;
import com.ecity.mobile.android.bdlbslibrary.model.AddressInfo;
import com.ecity.zzz.pipegps.util.TypeFormat;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnPanListener;
import com.esri.core.geometry.Point;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.service.PositionService;

public class ObtainLocationOpratorXtd extends AMapViewOperator {
    private WeakReference<IMapOperationContext> mOutersActivity;
    private WeakReference<MapView> mOutersMapView;
    private MapNavigationView ptMapNavigationView;
    private MapActivityTitleView titleView;
    private static MapActivity activity;
    private AddressIcomView addressView;
    private String locationLatLon = "";
    private static BDGeoLocator mBDGeoLocator;
    public static PatrolPosition mPatrolPosition;

    @Override
    public void setMapviewOption(MapView mapView, IMapOperationContext mapActivity) {
        if (null == mapView) {
            return;
        }
        super.setMapviewOption(mapView, mapActivity);

        if (null == mapActivity) {
            return;
        }

        mOutersActivity = new WeakReference<IMapOperationContext>(mapActivity);
        mOutersMapView = new WeakReference<MapView>(mapView);
        activity = (MapActivity) mOutersActivity.get();

        addressView = activity.getAddressView();
        addressView.setVisibility(View.VISIBLE);
        titleView = activity.getTitleView();
        titleView.setBtnStyle(BtnStyle.ONLY_BACK);
        ptMapNavigationView = activity.getPatrolMapNavigationView();
        ptMapNavigationView.setButtonText(R.string.address_commit);
        mBDGeoLocator = new BDGeoLocator(HostApplication.getApplication());
        initCurrentLocation();
        mapView.setOnPanListener(new MyOnPanListener());
    }

    private void initCurrentLocation() {
        MapView mapView = mOutersMapView.get();
        if (null == activity || null == mapView) {
            return;
        }
        ptMapNavigationView.setText(HostApplication.getApplication().getApplicationContext().getResources().getString(R.string.event_report_hint_address_loading));
        ptMapNavigationView.setVisibility(View.VISIBLE);
        ptMapNavigationView.setDeviceTxtVisibility(View.GONE);
        ptMapNavigationView.setAddressTxtVisibility(View.VISIBLE);
        ptMapNavigationView.setDetailAddressTxtVisibility(View.GONE);
        double[] localPoint = null;
        Location currentLocation = null;
        try {
            if (FuZhouPatrolModuleConfig.getConfig().isModuleUseable()) {
                currentLocation = CurrentLocationManager.getLocation();
            } else {
                currentLocation = PositionService.getLastLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (currentLocation != null) {
            localPoint = CoordTransfer.transToLocal(currentLocation.getLatitude(), currentLocation.getLongitude());
            mPatrolPosition = new PatrolPosition(true, localPoint[0], localPoint[1], "");
            locationLatLon = TypeFormat.FormatDouble(localPoint[0]) + "," + TypeFormat.FormatDouble(localPoint[1]);
        } else {
            ToastUtil.showShort(ResourceUtil.getStringById(R.string.event_reprot_hint_address_no_current_location));
            return;
        }
        try {
            double[] lngLat = null;
            lngLat = CoordTransfer.transToLatlon(mPatrolPosition.x, mPatrolPosition.y);
            if (null != lngLat && 2 == lngLat.length) {
                mBDGeoLocator.startGetAddressInfoByXY(lngLat[0], lngLat[1], new MyOnNotifyAddressInfoCallback());
            }
        } catch (Exception e) {
            LogUtil.e(activity, e);
        }
        Point mapPoint = new Point(localPoint[0], localPoint[1]);
        if (mapPoint != null) {
            mapView.centerAt(mapPoint, false);
        }
        ptMapNavigationView.setOnReportEventListener(new CommitClickListener());
    }

    @Override
    public void notifyBackEvent(IMapOperationContext mapActivity) {
        if (null != mapActivity) {
            mapActivity.finish();
        }
    }

    private class CommitClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra(Constants.FORM_MAP_GET_CURRENT_POSITION, locationLatLon);
            activity.getContext().setResult(ResultCode.GETLOCATION, intent);
            activity.getContext().finish();
        }
    }

    public void setPatrolPosition(String location) {
        if (StringUtil.isBlank(location)) {
            return;
        }

        String[] xy = location.split(",");
        if (null != xy && 2 == xy.length) {
            if (null == mPatrolPosition) {
                mPatrolPosition = new PatrolPosition();
            }
            mPatrolPosition.x = Double.valueOf(xy[0]);
            mPatrolPosition.y = Double.valueOf(xy[1]);
        }
    }

    private class MyOnNotifyAddressInfoCallback implements OnNotifyAddressInfo {
        @Override
        public void getAddressInfo(boolean arg0, int arg1, String arg2, List<AddressInfo> arg3) {
            if (!ListUtil.isEmpty(arg3)&&arg3.get(0).getAddrStr()!=null) {
                ptMapNavigationView.setText(arg3.get(0).getAddrStr());
            } else {
                ToastUtil.showMessage(activity, R.string.event_report_hint_address_loading_fail, 0);
            }
        }
    }

    private class MyOnPanListener implements OnPanListener {
        private static final long serialVersionUID = 3717646053374788733L;

        @Override
        public void prePointerUp(float arg0, float arg1, float arg2, float arg3) {

            MapView mapView = mOutersMapView.get();
            if (null == activity || null == mapView) {
                return;
            }
            ptMapNavigationView.setText(HostApplication.getApplication().getApplicationContext().getResources().getString(R.string.event_report_hint_address_loading));
            WindowManager wm = (WindowManager) activity.getContext().getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            int height = wm.getDefaultDisplay().getHeight();
            float x = width / 2;
            float y = height / 2;
            Point mapPoint = mapView.toMapPoint(x, y);
            if (null != mapPoint && !mapPoint.isEmpty()) {
                locationLatLon = TypeFormat.FormatDouble(mapPoint.getX()) + "," + TypeFormat.FormatDouble(mapPoint.getY());
            }
            ptMapNavigationView.setVisibility(View.VISIBLE);
            ptMapNavigationView.setDeviceTxtVisibility(View.GONE);
            ptMapNavigationView.setAddressTxtVisibility(View.VISIBLE);
            ptMapNavigationView.setDetailAddressTxtVisibility(View.GONE);
            setPatrolPosition(locationLatLon);
            try {
                double[] lngLat = null;
                lngLat = CoordTransfer.transToLatlon(mPatrolPosition.x, mPatrolPosition.y);
                if (null != lngLat && 2 == lngLat.length) {
                    mBDGeoLocator.startGetAddressInfoByXY(lngLat[0], lngLat[1], new MyOnNotifyAddressInfoCallback());
                }
            } catch (Exception e) {
                LogUtil.e(activity, e);
            }
            ptMapNavigationView.setOnReportEventListener(new CommitClickListener());
        }

        @Override
        public void prePointerMove(float arg0, float arg1, float arg2, float arg3) {
        }

        @Override
        public void postPointerUp(float arg0, float arg1, float arg2, float arg3) {
        }

        @Override
        public void postPointerMove(float arg0, float arg1, float arg2, float arg3) {
        }
    }
}
