package com.ecity.cswatersupply.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.emergency.EQModuleConfig;
import com.ecity.cswatersupply.emergency.EmergencyRefreshHelper;
import com.ecity.cswatersupply.emergency.menu.UnUsualReportOperater;
import com.ecity.cswatersupply.menu.map.AMapMenu;
import com.ecity.cswatersupply.menu.map.AMapViewOperator;
import com.ecity.cswatersupply.menu.map.CustomPopupMenu;
import com.ecity.cswatersupply.menu.map.CustomPopupMenu.OnMapMenuClickListener;
import com.ecity.cswatersupply.menu.map.EmptyOpratorXtd;
import com.ecity.cswatersupply.menu.map.IMapOperationContext;
import com.ecity.cswatersupply.menu.map.IdentifyResultsController;
import com.ecity.cswatersupply.menu.map.LayerControlOperatorXtd;
import com.ecity.cswatersupply.model.AddressInfoModel;
import com.ecity.cswatersupply.model.NaviModel;
import com.ecity.cswatersupply.model.QueryResultShowModel;
import com.ecity.cswatersupply.model.map.SearchResult;
import com.ecity.cswatersupply.network.response.loginresponse.MobileConfig;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.ui.activities.MapAddressSearchActivity;
import com.ecity.cswatersupply.ui.activities.ReportEventTypeSelectActivity;
import com.ecity.cswatersupply.ui.dialog.ConditionQueryDialog;
import com.ecity.cswatersupply.ui.widght.MapFragmentTitleView;
import com.ecity.cswatersupply.ui.widght.MapNavigationView;
import com.ecity.cswatersupply.ui.widght.MapOperatorTipsView;
import com.ecity.cswatersupply.ui.widght.NavigateActionSheet;
import com.ecity.cswatersupply.utils.AnalyzeMapMenuXML;
import com.ecity.cswatersupply.utils.AppUtil;
import com.ecity.cswatersupply.utils.CoordTransfer;
import com.ecity.cswatersupply.utils.CurrentLocationManager;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.DrawableUtil;
import com.ecity.cswatersupply.utils.LayerTool;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.MapLoadTool;
import com.ecity.cswatersupply.utils.NavigateHelper;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.RestoreManager;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.mobile.android.library.gpsengine.GPSEngine;
import com.ecity.z3map.maploader.AMapViewLoader;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Grid.GridType;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.shizhefei.fragment.LazyFragment;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.service.PositionService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MapMainTabFragment extends LazyFragment implements IMapOperationContext {

    public static final String MAP_OPERATOR = "com.ecity.mapoperator";
    public static final String DEVICE_OPERATOR = "com.ecity.deviceoperator";
    public static final String SEARCH_RESULT_CURRENT_POSITION = "SearchResultCurrentPosition";
    public static final String SEARCH_RESULTS = "SearchResults";
    public static final String BAIDU_PACKAGE_NAME = "com.baidu.BaiduMap";
    public static final String GAODE_PACKAGE_NAME = "com.autonavi.minimap";
    public final static int QUERY_CODE = 5;
    public final static int RESULT_CODE = 6;
    private MapFragmentTitleView fragmentViewTitle = null;
    private MapView mMapView;
    private MobileConfig mMobileConfig;
    private ImageView mIvMainGps;
    private ImageView mIvZoomIn;
    private ImageView mIvZoomOut;
    private ImageView mIvZoomFull;
    private ImageView reportBtn;
    private AMapViewOperator mapViewOperator;
    private PictureMarkerSymbol myLocationPointSymbol;
    private MapOperatorTipsView mMapOperatorTipsView;
    private ViewPager mResultsViewpager;
    private RelativeLayout mRlMapZoomView;
    private IdentifyResultsController queryResultsControler;
    private ImageButton layerControlBut;
    private Bundle bundle = null;
    private MapNavigationView navigationView;
    private Geometry curentPointPartGeometry;
    private MapOperatorClickListener operatorClickListener;
    private boolean isDisplaySearchResult;
    private MapNavigationListener mNavigationListener;
    private SearchResult result;
    private AddressInfoModel searchResult;
    private Location location;
    private LinearLayout mapLocationLayout;
    private NavigateHelper navigateHelper;
    private double[] targetLonLat = new double[2];
    private TextView achor;
    private boolean isMapLoadNotifyed;
    private Handler locationHandler;

    // GPS图标在图层上的唯一id
    private int mGPSLocateGID = -1;
    //GPS位置绘制所使用的图层
    private GraphicsLayer mGPSGraphicsLayer;
    //定时任务执行服务
    private ScheduledExecutorService mBackgroundService;
    //定时任务是否停止标志
    private boolean isTimetaskStop = true;

    //地名定位绘制图层
    private GraphicsLayer mAnimationGraphicsLayer;
    private TextView calloutTextView;

    private DefaultZ3MaploadCallback mMaploadCallback;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_map, null);
        initView(view);
        setContentView(view);
        initMapView();
        initMapExtent();
        isDisplaySearchResult = false;
        initMapviewOperator();
    }

    @Override
    protected void onDestroyViewLazy() {
        EmergencyRefreshHelper.getInstance().onPauseLazy();
        super.onDestroyViewLazy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (SessionManager.isFragmentMapNeedReload) {
                SessionManager.isFragmentMapNeedReload = false;
                initMapView();
            }
            try {
                EQModuleConfig config = EQModuleConfig.getConfig();
                if (config.isModuleUseable() || config.isCZModuleUseable()) {
                    EmergencyRefreshHelper.getInstance().onResumeLazy();
                    EmergencyRefreshHelper.getInstance().initMapViewListener();
                }
            } catch (Exception e) {
                LogUtil.e(this, e);
            }

            mGPSLocateGID = -1;
            mGPSGraphicsLayer = LayerTool.getGPSGraphicsLayer(mMapView);
            mGPSGraphicsLayer.removeAll();

            showMyLocation(mMapView);

            if (mMapView.isLoaded()) {
                mMapView.setExtent(SessionManager.lastInitExtent);
            }
            startTimerTask();
        } else {
            if (null == SessionManager.lastInitExtent) {
                SessionManager.lastInitExtent = new Envelope();
            }
            if ((null != mMapView) && mMapView.isLoaded()) {
                mMapView.getExtent().queryEnvelope(SessionManager.lastInitExtent);
            }

            stopTimerTask();
        }
    }

    public void initView(View view) {
        fragmentViewTitle = (MapFragmentTitleView) view.findViewById(R.id.view_title_map);
        fragmentViewTitle.setTitleText(R.string.fragment_map_title);
        operatorClickListener = new MapOperatorClickListener();
        fragmentViewTitle.setOnOperatorListener(operatorClickListener);
        fragmentViewTitle.setOnQueryAddressListener(new MapQueryAddressListener(true));
        mMapOperatorTipsView = (MapOperatorTipsView) view.findViewById(R.id.map_tips_view);
        mMapOperatorTipsView.registerCleanListener(new MyBtnCleanMapClickListener());
        Bitmap bmpMarker = BitmapFactory.decodeResource(HostApplication.getApplication().getResources(), R.drawable.icon_locationme);
        Drawable drawable = DrawableUtil.createDrawable(bmpMarker, 32, 32);
        myLocationPointSymbol = new PictureMarkerSymbol(drawable);
        myLocationPointSymbol.setOffsetY(drawable.getBounds().height() / 2);

        mIvMainGps = (ImageView) view.findViewById(R.id.iv_main_gps);
        mIvZoomIn = (ImageView) view.findViewById(R.id.iv_main_zoomin);
        mIvZoomOut = (ImageView) view.findViewById(R.id.iv_main_zoomout);
        mIvZoomFull = (ImageView) view.findViewById(R.id.iv_main_zoomfull);
        reportBtn = (ImageView) view.findViewById(R.id.report);
        mMapView = (MapView) view.findViewById(R.id.map);
        mapLocationLayout = (LinearLayout) view.findViewById(R.id.location_layout);
        achor = (TextView) view.findViewById(R.id.pop_achor);

        try {
            if (EQModuleConfig.getConfig().isModuleUseable()) {
                reportBtn.setVisibility(View.VISIBLE);
                reportBtn.setOnClickListener(new UnUsualReportClickListener());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mIvMainGps.setOnClickListener(new MyZoomOnClickListener(mMapView));
        mIvZoomIn.setOnClickListener(new MyZoomOnClickListener(mMapView));
        mIvZoomOut.setOnClickListener(new MyZoomOnClickListener(mMapView));
        mIvZoomFull.setOnClickListener(new MyZoomOnClickListener(mMapView));
        mResultsViewpager = (ViewPager) view.findViewById(R.id.results_viewpager);
        mRlMapZoomView = (RelativeLayout) view.findViewById(R.id.rl_map_zoom_control);
        layerControlBut = (ImageButton) view.findViewById(R.id.control_layer);
        layerControlBut.setOnClickListener(new LayerControlClickListener());

        navigateHelper = new NavigateHelper(getActivity());
        navigationView = (MapNavigationView) view.findViewById(R.id.map_search_point);
        navigationView.setOnReportEventListener(new SearchPointReportClickListener());
        navigationView.setOnNavigationListener(new MapNavigationListener());

        locationHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (null != msg.obj && msg.obj instanceof Point) {
                    updateCurrentLocation((Point) msg.obj);
                }
            }
        };
    }

    private void initMapView() {
        mMapView.getGrid().setVisibility(false);
        mMapView.setBackgroundColor(Color.argb(0, 255, 255, 255));
        mMapView.setMapBackground(Color.WHITE, Color.argb(0, 255, 255, 255), 0, 0);
        mMapView.getGrid().setType(GridType.NONE);
        mMobileConfig = RestoreManager.getInstance().restoreMobileMapConfig();
        mMaploadCallback = new DefaultZ3MaploadCallback();

        if (!ListUtil.isEmpty(mMobileConfig.getSourceConfigArrayList())) {
            if (null == mMaploadCallback) {
                mMaploadCallback = new DefaultZ3MaploadCallback();
            }
            MapLoadTool.LoadMap(getActivity(), mMapView, mMobileConfig, mMaploadCallback);
            EQModuleConfig config;
            try {
                config = EQModuleConfig.getConfig();
                if (config.isModuleUseable() || config.isCZModuleUseable()) {
                    EmergencyRefreshHelper.getInstance().initGraphicsLayer(mMapView, this);
                }
            } catch (Exception e) {
                LogUtil.e(this, e);
            }
        }
    }

    private class DefaultZ3MaploadCallback implements AMapViewLoader.Z3MaploadCallback {

        @Override
        public void onStatusChanged(Object arg0, OnStatusChangedListener.STATUS arg1) {
            if (arg1 == OnStatusChangedListener.STATUS.LAYER_LOADED) {
                if (isMapLoaded()) {
                    if (SessionManager.lastInitExtent != null) {
                        mMapView.setExtent(SessionManager.lastInitExtent);
                    }

                    if (null != mapViewOperator) {
                        notifyMapLoaded();
                    }
                }
            }
        }
    }

    /***
     * 初始化地图范围，如果最近一次启动地图的范围为空则使用默认配置的范围
     */
    private void initMapExtent() {
        if (SessionManager.lastInitExtent != null) {
            mMapView.setExtent(SessionManager.lastInitExtent);
        } else {
            MobileConfig mobileConfig = HostApplication.getApplication().getConfig();
            if ((mobileConfig != null) && (mobileConfig.getInitExtent() != null)) {
                mMapView.setExtent(mobileConfig.getInitExtent());
            }
        }
    }

    /**
     * 初始化地图操作
     */
    private void initMapviewOperator() {
        if (null == mMapView) {
            return;
        }
        String className = null;
        if (bundle == null) {
            className = "";
        } else {
            try {
                if (bundle.containsKey(MAP_OPERATOR)) {
                    className = bundle.getString(MAP_OPERATOR);
                } else if (bundle.containsKey(DEVICE_OPERATOR)) {
                    className = bundle.getString(DEVICE_OPERATOR);
                } else {
                    // no logic to do.
                }
            } catch (Exception e) {
                e.printStackTrace();
                className = "";
            }
        }
        if (StringUtil.isBlank(className)) {
            // 地图浏览 显示工具栏
            mapViewOperator = new EmptyOpratorXtd();
            mapViewOperator.setMapviewOption(mMapView, this);
        } else {
            try {
                if (isDisplaySearchResult) {
                    mapViewOperator = (AMapViewOperator) Class.forName(className).newInstance();
                } else {
                    mapViewOperator = new EmptyOpratorXtd();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setMapViewOperator(mapViewOperator);
    }

    public void setMapViewOperator(AMapViewOperator mapViewOperator) {
        this.mapViewOperator = mapViewOperator;
        if (null != this.mapViewOperator) {
            this.mapViewOperator.setMapviewOption(mMapView, this);
            this.mapViewOperator.notifyMapLoaded();
        }
    }

    public SearchResult getResult() {
        return result;
    }

    public MapNavigationListener getmNavigationListener() {
        return mNavigationListener;
    }

    public MapOperatorClickListener getOperatorClickListener() {
        return operatorClickListener;
    }

    public MapFragmentTitleView getFragmentViewTitle() {
        return fragmentViewTitle;
    }

    public ViewPager getmResultsViewpager() {
        return mResultsViewpager;
    }

    public MapOperatorTipsView getmMapOperatorTipsView() {
        return mMapOperatorTipsView;
    }

    public MapNavigationView getNavigationView() {
        return navigationView;
    }

    public MapView getMapView() {
        return mMapView;
    }

    public LinearLayout getMapLocationLayout() {
        return mapLocationLayout;
    }

    public void onActionButtonClicked(View v) {
    }

    private class LayerControlClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            LayerControlOperatorXtd operator = new LayerControlOperatorXtd();
            operator.execute(mMapView, MapMainTabFragment.this);
        }
    }

    private class MyBtnCleanMapClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            cleanMapView();
            v.setVisibility(View.GONE);
        }
    }

    private synchronized boolean isMapLoaded() {
        if (null != mMapView) {
            return mMapView.isLoaded();
        }

        return false;
    }

    private synchronized void notifyMapLoaded() {
        if (isMapLoadNotifyed) {
            return;
        }
        if (null != this.mapViewOperator) {
            this.mapViewOperator.setMapviewOption(mMapView, this);
            mapViewOperator.notifyMapLoaded();
            isMapLoadNotifyed = true;
        }
    }

    // 生成地图工具popuwindow
    private class MapOperatorClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            List<AMapMenu> menus = AnalyzeMapMenuXML.analyzeMenuXML(getActivity());
            CustomPopupMenu moreBtnPopup = new CustomPopupMenu(getActivity(), menus, AMapMenu.class);
            int itemCount = menus.size();
            int padding = ResourceUtil.getDimensionPixelSizeById(R.dimen.lv_item_padding_up_down_level_1);
            int width = ResourceUtil.getDimensionPixelSizeById(R.dimen.popmenu_item_w) + (padding << 1);
            int height = ResourceUtil.getDimensionPixelSizeById(R.dimen.popmenu_item_h) * itemCount + (itemCount + 1) * padding;
            moreBtnPopup.initPopup(width, LayoutParams.WRAP_CONTENT);
            int[] location = new int[2];
            v.getLocationOnScreen(location);
            // 在屏幕上半边
            if (moreBtnPopup.atTopHalfOfScreen(location)) {
                moreBtnPopup.setAnimation(R.style.popup_tophalf_anim);
                moreBtnPopup.showAtLocation(v, (location[0] + v.getWidth() / 2) - width / 2, location[1] + v.getHeight() + 5, Gravity.NO_GRAVITY);
            } else {
                // 在屏幕下半边
                moreBtnPopup.setAnimation(R.style.popup_bottomhalf_anim);
                moreBtnPopup.showAtLocation(v, (location[0] + v.getWidth() / 2) - width / 2, location[1] - height - 5, Gravity.NO_GRAVITY);
            }
            moreBtnPopup.setOnActionItemClickListener(new OnMapMenuClickListener() {

                @Override
                public void onMenuItemClick(AMapMenu menu, int pos) {

                    navigationView.setVisibility(View.GONE);
                    menu.getMapMenuCommand().execute(mMapView, MapMainTabFragment.this);
                }
            });
        }
    }

    // 清除界面
    public void cleanMapView() {

        if (null == mMapView || !mMapView.isLoaded()) {
            return;
        }

        try {
            if (mMapView.getCallout().isShowing()) {
                mMapView.getCallout().hide();
            }
        } catch (Exception e) {
        }

        try {
            mGPSLocateGID = -1;
            mGPSGraphicsLayer.removeAll();
        } catch (Exception e) {
        }

        if (null != mAnimationGraphicsLayer) {
            mAnimationGraphicsLayer.removeAll();
        } else {
            try {
                mAnimationGraphicsLayer = LayerTool.getAnimationLayer(mMapView);
                mAnimationGraphicsLayer.removeAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        GraphicsLayer patrolPlanGraphicsLayer = null;
        try {
            patrolPlanGraphicsLayer = LayerTool.getPatrolPlanGraphicsLayer(mMapView);
        } catch (Exception e) {
        }

        if (null != patrolPlanGraphicsLayer) {
            patrolPlanGraphicsLayer.removeAll();
        }

        if (null != queryResultsControler) {
            queryResultsControler.clearAllResults();
        }
        queryResultsControler = null;

        if (null != mResultsViewpager) {
            mResultsViewpager.removeAllViews();
            mResultsViewpager.setVisibility(View.GONE);
        }

        if (null != mMapOperatorTipsView) {
            mMapOperatorTipsView.disMissProgressBar();
            mMapOperatorTipsView.dismissButtonClean();
            mMapOperatorTipsView.setTipsText("");
        }
    }

    public void showSerachResult(final QueryResultShowModel queryResult, boolean showActionButtons) {
        queryResultsControler = new IdentifyResultsController(this, showActionButtons);

        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRlMapZoomView.setVisibility(View.INVISIBLE);
                queryResultsControler.showIdentifyResults(queryResult.graphics);
            }
        });
    }

    // 地图查找坐标地址功能
    public class MapQueryAddressListener implements OnClickListener {
        private boolean isAddressQuery;
        private boolean isDeviceQuery;

        public MapQueryAddressListener(boolean isAddressQuery) {
            this.isAddressQuery = isAddressQuery;
        }

        public MapQueryAddressListener(boolean isAddressQuery, boolean isDeviceQuery) {
            this.isAddressQuery = isAddressQuery;
            this.isDeviceQuery = isDeviceQuery;
        }

        @Override
        public void onClick(View v) {
            cleanMapView();
//            Intent intent = new Intent(getActivity(), MapPoiSearchActivity.class);
//            startActivityForResult(intent, QUERY_CODE);
            if (isAddressQuery) {
                Intent intent = new Intent(getActivity(), MapAddressSearchActivity.class);
                startActivityForResult(intent, QUERY_CODE);
            } else {
                if (isDeviceQuery) {
                    ConditionQueryDialog dialog = new ConditionQueryDialog(getActivity(), mMapView, MapMainTabFragment.this, true);
                    dialog.show();
                } else {
                    ConditionQueryDialog dialog = new ConditionQueryDialog(getActivity(), mMapView, MapMainTabFragment.this, false);
                    dialog.show();
                }
            }
        }
    }

    private class UnUsualReportClickListener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            location = GPSEngine.getInstance().getLastLocation();
            if (null == location) {
                Toast.makeText(getActivity(), R.string.no_location, Toast.LENGTH_LONG).show();
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putDouble(Constants.EARTH_QUAKE_QUICK_REPORT_LONGITUDE, location.getLongitude());
            bundle.putDouble(Constants.EARTH_QUAKE_QUICK_REPORT_LATITUDE, location.getLatitude());
            bundle.putInt(CustomViewInflater.REPORT_TITLE, R.string.str_emergency_unusual_report);
            bundle.putString(CustomViewInflater.REPORT_COMFROM, UnUsualReportOperater.class.getName());
            bundle.putString(CustomViewInflater.EVENTTYPE, "0003");
            UIHelper.startActivityWithExtra(CustomReportActivity1.class, bundle);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (intent == null) {
            return;
        }
        switch (resultCode) {
            case RESULT_CODE:
//                bundle = intent.getExtras();
//                isDisplaySearchResult = true;
//                if (bundle.containsKey(SEARCH_RESULTS)) {
//                    result = (SearchResult) bundle.getSerializable(SEARCH_RESULTS);
//                    curentPointPartGeometry = result.centerPoint;
//                    if (null == curentPointPartGeometry) {
//                        return;
//                    }
//                    Point point = GeometryUtil.GetGeometryCenter(curentPointPartGeometry);
//                    targetLonLat = CoordTransfer.transToLatlon(point.getX(), point.getY()); 
//                }
//                initMapviewOperator();
                Point point = null;
                bundle = intent.getExtras();
                isDisplaySearchResult = true;
                if (bundle.containsKey(SEARCH_RESULTS)) {
                    searchResult = (AddressInfoModel) bundle.getSerializable(SEARCH_RESULTS);
                    String type = searchResult.getType();
                    if (ResourceUtil.getStringById(R.string.map_address).equals(type)) {
                        point = searchResult.getPoint();
                    } else {
                        point = new Point(searchResult.getX(), searchResult.getY());
                    }
                }

                Graphic graphic = new Graphic(point, myLocationPointSymbol, null, null);

                if (null == mAnimationGraphicsLayer) {
                    try {
                        mAnimationGraphicsLayer = LayerTool.getAnimationLayer(mMapView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (!StringUtil.isBlank(searchResult.getName())) {
                    if (null == calloutTextView) {
                        calloutTextView = new TextView(getContext());
                    }

                    calloutTextView.setText(searchResult.getName());
                    mMapView.getCallout().setOffset(0, 48);
                    mMapView.getCallout().setMaxHeight(200);
                    mMapView.getCallout().setMaxWidth(500);
                    mMapView.getCallout().animatedShow(point, calloutTextView);
                }

                mMapView.centerAt(point, true);

                if (mAnimationGraphicsLayer != null) {
                    mAnimationGraphicsLayer.removeAll();
                    mAnimationGraphicsLayer.addGraphic(graphic);
                }

                break;
            default:
                break;
        }
    }

    public class SearchPointReportClickListener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            UIHelper.startActivityWithoutExtra(ReportEventTypeSelectActivity.class);
        }
    }

    public class MapNavigationListener implements OnClickListener {

        Activity activity = getActivity();

        @Override
        public void onClick(View v) {
            if (AppUtil.isAvilible(activity, BAIDU_PACKAGE_NAME) && AppUtil.isAvilible(activity, GAODE_PACKAGE_NAME)) {//传入指定应用包名
                showNaviSelectDialog(activity);
            } else if (AppUtil.isAvilible(activity, GAODE_PACKAGE_NAME)) {
                //调用高德导航
                navigateHelper.startMiniNavigtion(targetLonLat);
            } else if (AppUtil.isAvilible(activity, BAIDU_PACKAGE_NAME)) {
                navigateHelper.startBaiduAppNavigtion(targetLonLat);
            } else {
                //跳转到百度网页导航
                navigateHelper.startBaiduWebNavigtion(targetLonLat);
            }
        }

        private void showNaviSelectDialog(Context context) {
            List<NaviModel> naviApps = buildSheetMenus();
            NavigateActionSheet.show(context, context.getString(R.string.action_menu_title), naviApps, new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            navigateHelper.startBaiduAppNavigtion(targetLonLat);
                            break;
                        case 1:
                            navigateHelper.startMiniNavigtion(targetLonLat);
                            break;
                        default:
                            break;
                    }
                }
            });
        }

        private List<NaviModel> buildSheetMenus() {
            List<NaviModel> naviApps = new ArrayList<NaviModel>();
            NaviModel baidu = new NaviModel();
            baidu.setIcon(AppUtil.getAppIcon(BAIDU_PACKAGE_NAME));
            baidu.setName(ResourceUtil.getStringById(R.string.map_navigtion_baidu));
            naviApps.add(baidu);
            NaviModel gaode = new NaviModel();
            gaode.setIcon(AppUtil.getAppIcon(GAODE_PACKAGE_NAME));
            gaode.setName(ResourceUtil.getStringById(R.string.map_navigtion_gaode));
            naviApps.add(gaode);
            return naviApps;
        }
    }

    private class MyZoomOnClickListener implements OnClickListener {
        private MapView mapView;

        public MyZoomOnClickListener(MapView mapView) {
            this.mapView = mapView;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_main_gps:
                    showMyLocation(mapView);
                    break;
                case R.id.iv_main_zoomin:
                    this.mapView.zoomin();
                    break;
                case R.id.iv_main_zoomout:
                    this.mapView.zoomout();
                    break;
                case R.id.iv_main_zoomfull:
                    this.mapView.setExtent(HostApplication.getApplication().getConfig().getInitExtent());
                    break;
                default:
                    break;
            }
        }
    }

    private void showMyLocation(MapView mapView) {
        location = CurrentLocationManager.getLocation();
        if (location == null) {
            Toast.makeText(getActivity(), R.string.no_location, Toast.LENGTH_LONG).show();
            return;
        }

        double[] xy = CoordTransfer.transToLocal(location.getLatitude(), location.getLongitude());
        Point point = new Point();
        if (null != xy && 2 == xy.length) {
            point.setX(xy[0]);
            point.setY(xy[1]);
        } else {
            Toast.makeText(getActivity(), R.string.no_location, Toast.LENGTH_LONG).show();
            return;
        }
        mapView.centerAt(point, true);

        updateCurrentLocation(point);
    }

    /**
     * 更新地图上的GPS标记位置
     */
    private void updateCurrentLocation(Point point) {

        if (mGPSGraphicsLayer == null || point == null) {
            return;
        }

        try {
            if (null == mGPSGraphicsLayer.getGraphic(mGPSLocateGID)) {
                mGPSLocateGID = -1;
            }
        } catch (Exception e) {
            mGPSLocateGID = -1;
        }

        if (-1 == mGPSLocateGID) {
            Graphic graphic = new Graphic(point, myLocationPointSymbol, null, null);
            mGPSGraphicsLayer.removeAll();
            mGPSLocateGID = mGPSGraphicsLayer.addGraphic(graphic);
        } else {
            try {
                mGPSGraphicsLayer.updateGraphic(mGPSLocateGID, point);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Activity getContext() {
        return getActivity();
    }

    @Override
    public void finish() {
    }

    @Override
    public View getRootView() {
        return ((ViewGroup) getContext().findViewById(android.R.id.content)).getChildAt(0);
    }

    public View getAchor() {
        return achor;
    }

    @Override
    public Intent getUserIntent() {
        return null;
    }

    @Override
    public View getmTitleView() {
        return this.fragmentViewTitle;
    }

    @Override
    public View getmRlMapZoomView() {
        return mRlMapZoomView;
    }

    @Override
    public void setTargetLonLat(double[] LonLat) {
        int size = LonLat.length;
        for (int i = 0; i < size; i++) {
            this.targetLonLat[i] = LonLat[i];
        }
    }

    @Override
    public OnClickListener getNaviClickListener() {
        return new MapNavigationListener();
    }

    public MapQueryAddressListener getQueryClickListener(boolean queryAddress, boolean queryGX) {
        return new MapQueryAddressListener(queryAddress, queryGX);
    }

    public MapQueryAddressListener getQueryClickListener(boolean queryAddress) {
        return new MapQueryAddressListener(queryAddress);
    }

    private void startTimerTask() {
        mBackgroundService = Executors.newSingleThreadScheduledExecutor();
        mBackgroundService.scheduleAtFixedRate(new TimerIncreasedRunnable(), 0, 1000 * 3, TimeUnit.MILLISECONDS);
        isTimetaskStop = false;
    }

    private void stopTimerTask() {
        isTimetaskStop = true;
        if (null != mBackgroundService) {
            mBackgroundService.shutdownNow();
        }
    }

    public class TimerIncreasedRunnable implements Runnable {
        @Override
        public void run() {
            try {
                if (isTimetaskStop) {
                    return;
                }

                location = CurrentLocationManager.getLocation();
                if (location == null) {
                    return;
                }

                double[] xy = CoordTransfer.transToLocal(location.getLatitude(), location.getLongitude());
                Point point = new Point();
                if (null != xy && 2 == xy.length) {
                    point.setX(xy[0]);
                    point.setY(xy[1]);
                } else {
                    return;
                }

                Message msg = Message.obtain();
                msg.obj = point;
                locationHandler.sendMessage(msg);

            } catch (Throwable t) {
                Log.e("LocationService", t.getMessage());
            }
        }
    }

}
