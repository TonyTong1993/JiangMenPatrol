package com.ecity.cswatersupply.ui.activities;

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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.menu.EmptyOpratorXtd;
import com.ecity.cswatersupply.menu.map.AMapViewOperator;
import com.ecity.cswatersupply.menu.map.IMapOperationContext;
import com.ecity.cswatersupply.menu.map.IdentifyResultsController;
import com.ecity.cswatersupply.menu.map.MapMeasure;
import com.ecity.cswatersupply.menu.map.MapMeasure.MeasureMode;
import com.ecity.cswatersupply.menu.map.MapOperationActivityXtd;
import com.ecity.cswatersupply.model.AddressInfoModel;
import com.ecity.cswatersupply.model.NaviModel;
import com.ecity.cswatersupply.model.QueryResultShowModel;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTask;
import com.ecity.cswatersupply.network.response.loginresponse.MobileConfig;
import com.ecity.cswatersupply.ui.widght.AddressIcomView;
import com.ecity.cswatersupply.ui.widght.CustomPopupToolView;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView.BtnStyle;
import com.ecity.cswatersupply.ui.widght.MapLegendView;
import com.ecity.cswatersupply.ui.widght.MapNavigationFlexflowView;
import com.ecity.cswatersupply.ui.widght.MapNavigationView;
import com.ecity.cswatersupply.ui.widght.MapOperatorTipsView;
import com.ecity.cswatersupply.ui.widght.NavigateActionSheet;
import com.ecity.cswatersupply.utils.AppUtil;
import com.ecity.cswatersupply.utils.CoordTransfer;
import com.ecity.cswatersupply.utils.CurrentLocationManager;
import com.ecity.cswatersupply.utils.DrawableUtil;
import com.ecity.cswatersupply.utils.LayerTool;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.MapLoadTool;
import com.ecity.cswatersupply.utils.NavigateHelper;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.RestoreManager;
import com.ecity.z3map.maploader.AMapViewLoader;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Grid.GridType;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.z3app.android.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MapActivity extends MapOperationActivityXtd implements IMapOperationContext {
    public final static int BAIDU_NAVIGATE_APP = 99;
    public final static int BAIDU_NAVIGATE_WEB = 88;
    public final static int RESULT_CODE = 5;
    public final static int QUERY_ADDRASS = 6;
    public static final String SEARCH_RESULTS = "SearchResults";
    public static final String TASKFLAG = "taskflag";
    public static final String MAP_OPERATOR = "com.ecity.mapoperator";
    public static final String DEVICE_OPERATOR = "com.ecity.deviceoperator";
    public static final String DEVICE_VALVE_OPERATOR = "com.ecity.valveoperator";
    public static final String MAP_TITLE = "map_title";
    public static final String LOCATION_OPERATOR = "com.ecity.locationoperator";
    public static final String EVENT_LLOCATION_LAT = "event_location_lat";
    public static final String EVENT_LLOCATION_LON = "event_location_lon";
    public static final String BAIDU_PACKAGE_NAME = "com.baidu.BaiduMap";
    public static final String GAODE_PACKAGE_NAME = "com.autonavi.minimap";
    public static final String RIGHTBUTTONACTION = "com.ecity.right.button.action";
    private AMapViewOperator mapViewOperator;
    private Intent userIntent;
    private MapActivityTitleView viewTitle;
    private MapOperatorTipsView mMapOperatorTipsView;
    private MapLegendView mMapLegendView;
    private MapView mMapView;
    private MobileConfig mMobileConfig;
    private ImageView mIvMainGps;
    private ImageView mIvZoomIn;
    private ImageView mIvZoomOut;
    private ImageView mIvZoomFull;
    private RelativeLayout mRlMapMenuMeasure;
    private RelativeLayout mRlMapMenuQuery;
    private RelativeLayout mRlMapMenuClear;
    private RelativeLayout mRlMapMenuMore;
    private MapMeasure measureTool;
    private ViewPager mResultsViewpager;
    private FrameLayout mtoobar;
    private PictureMarkerSymbol myLocationPointSymbol;
    private double[] targetLonLat = new double[2];
    //    private Z3PlanTaskPointPart curentPointPart;
    private IdentifyResultsController queryResultsControler;
    private View layoutZoomControl;
    private MapNavigationView patrolMapNavigationView;
    private AddressIcomView addressView;
    private Button report;
    //判断是否已经通知地图加载，防止多次加载
    private boolean isMapLoadNotifyed = false;
    private MapNavigationFlexflowView flexflowMapNavigationView;
    private int addGraphicID = 0;
    private NavigateHelper navigateHelper;
    private String rightActionFlag;
    private TextView achor;

    // GPS图标在图层上的唯一id
    private int mGPSLocateGID = -1;
    //GPS位置绘制所使用的图层
    private GraphicsLayer mGPSGraphicsLayer;
    //定时任务执行服务
    private ScheduledExecutorService mBackgroundService;
    //定时任务是否停止标志

    private boolean isTimetaskStop = true;
    private Location location;
    private Handler locationHandler;
    private DefaultZ3MaploadCallback mMaploadCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initUI();
        initMapView();
        initMapExtent();
        initMapviewOperator(getIntent());
    }

    @Override
    protected void onResume() {
        if (SessionManager.isActivityMapNeedReload) {
            SessionManager.isActivityMapNeedReload = false;
            initMapView();
        }
        if (null != mMapView) {
            mMapView.unpause();
            if (mMapView.isLoaded()) {
                mMapView.setExtent(SessionManager.lastInitExtent);
            }
        }
        super.onResume();
        mGPSLocateGID = -1;
        mGPSGraphicsLayer = LayerTool.getGPSGraphicsLayer(mMapView);
        mGPSGraphicsLayer.removeAll();
        startTimerTask();
    }

    @Override
    protected void onPause() {
        if (null != mMapView && mMapView.isLoaded()) {
            if (SessionManager.lastInitExtent != null) {
                SessionManager.lastInitExtent = new Envelope();
            }
            mMapView.getExtent().queryEnvelope(SessionManager.lastInitExtent);
            mMapView.pause();
        }
        super.onPause();
        stopTimerTask();
        mMapView.pause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mapViewOperator.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (resultCode) {
            case RESULT_CODE:
                Point point = null;
                Bundle bundle = data.getExtras();
//                isDisplaySearchResult = true;
                if (bundle.containsKey(SEARCH_RESULTS)) {
                    AddressInfoModel searchResult = (AddressInfoModel) bundle.getSerializable(SEARCH_RESULTS);
                    String type = searchResult.getType();
                    if (ResourceUtil.getStringById(R.string.map_address).equals(type)) {
                        point = searchResult.getPoint();
                    } else {
                        point = new Point(searchResult.getX(), searchResult.getY());
                    }
                }
                mMapView.centerAt(point, true);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mMapView.isRecycled()) {
            mMapView.recycle();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            notifyBackEvent();
        }

        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        isMapLoadNotifyed = false;
        Z3PlanTask plantask = (Z3PlanTask) intent.getSerializableExtra("planning_tasks");
        SessionManager.currentTaskIntMapOpretor = plantask;
        super.onNewIntent(intent);
        mapViewOperator.notifyMapLoaded();
    }

    public ViewPager getResultsViewpager() {
        return mResultsViewpager;
    }

    public void setTargetLonLat(double[] LonLat) {
        int size = LonLat.length;
        for (int i = 0; i < size; i++) {
            this.targetLonLat[i] = LonLat[i];
        }
    }

    public MapOperatorTipsView getmMapOperatorTipsView() {
        return mMapOperatorTipsView;
    }

    public MapNavigationView getPatrolMapNavigationView() {
        return patrolMapNavigationView;
    }

    public MapNavigationFlexflowView getFlexflowMapNavigationView() {
        return flexflowMapNavigationView;
    }

    public MapLegendView getmMapLegendView() {
        return mMapLegendView;
    }

    private void notifyBackEvent() {
        if (null != measureTool) {
            measureTool.stop();
        }
        measureTool = null;
        if (null != mapViewOperator) {
            mapViewOperator.notifyBackEvent(this);
        } else {
            finish();
        }
    }

    private void notifyActionEvent() {
        if (null != measureTool) {
            measureTool.stop();
        }

        measureTool = null;
        if (null != mapViewOperator) {
            mapViewOperator.notifyActionEvent(this);
        }

    }

    private void notifySearchActionEvent() {
        if (null != measureTool) {
            measureTool.stop();
        }

        measureTool = null;
        if (null != mapViewOperator) {
            mapViewOperator.notifySearchBtnOnClicked(this);
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

    private void initUI() {
        viewTitle = (MapActivityTitleView) findViewById(R.id.view_title_mapactivity);
        viewTitle.setBtnStyle(BtnStyle.RIGHT_ACTION);
//        viewTitle.setSearchBtnOnClickedListener(new MapQueryAddressListener());

        mMapLegendView = (MapLegendView) findViewById(R.id.map_legend_view);

        layoutZoomControl = findViewById(R.id.rl_map_zoom_control);
        mMapOperatorTipsView = (MapOperatorTipsView) findViewById(R.id.map_tips_view);

        mIvMainGps = (ImageView) findViewById(R.id.iv_main_gps);
        mIvZoomIn = (ImageView) findViewById(R.id.iv_main_zoomin);
        mIvZoomOut = (ImageView) findViewById(R.id.iv_main_zoomout);
        mIvZoomFull = (ImageView) findViewById(R.id.iv_main_zoomfull);
        report = (Button) findViewById(R.id.report);
        achor = (TextView) findViewById(R.id.pop_achor);

        mRlMapMenuMeasure = (RelativeLayout) findViewById(R.id.layout_measure_length);
        mRlMapMenuQuery = (RelativeLayout) findViewById(R.id.layout_query_point);
        mRlMapMenuClear = (RelativeLayout) findViewById(R.id.layout_mainmenu_clearmap);
        mRlMapMenuMore = (RelativeLayout) findViewById(R.id.layout_mainmenu_more);
        patrolMapNavigationView = (MapNavigationView) findViewById(R.id.view_map_show_point_detaile);
        flexflowMapNavigationView = (MapNavigationFlexflowView) findViewById(R.id.view_map_show_point_flexflow_detaile);

        mRlMapMenuMeasure.setOnClickListener(new MyMapMenuClickListener());
        mRlMapMenuQuery.setOnClickListener(new MyMapMenuClickListener());
        mRlMapMenuClear.setOnClickListener(new MyMapMenuClickListener());
        mRlMapMenuMore.setOnClickListener(new MyMapMenuClickListener());
        mtoobar = (FrameLayout) findViewById(R.id.fl_toobar);
        mMapView = (MapView) findViewById(R.id.map);

        mIvMainGps.setOnClickListener(new MyZoomOnClickListener(mMapView));
        mIvZoomIn.setOnClickListener(new MyZoomOnClickListener(mMapView));
        mIvZoomOut.setOnClickListener(new MyZoomOnClickListener(mMapView));
        mIvZoomFull.setOnClickListener(new MyZoomOnClickListener(mMapView));
        mResultsViewpager = (ViewPager) findViewById(R.id.results_viewpager);

        addressView = new AddressIcomView(HostApplication.getApplication());
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        getWindow().addContentView(addressView, params);
        addressView.setVisibility(View.GONE);
        navigateHelper = new NavigateHelper(this);
        viewTitle.setOnActionButtonClickedListener(new OnClickListener() {
            // 在布局文件中指定android:onClick="onActionButtonClickedForMap"，在有的手机上不管用。因此显示的设置监听器
            @Override
            public void onClick(View v) {
                notifyActionEvent();
            }
        });

        viewTitle.setSearchBtnOnClickedListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                notifySearchActionEvent();
            }
        });

        locationHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(null != msg.obj && msg.obj instanceof  Point) {
                    updateCurrentLocation((Point)msg.obj);
                }
            }
        };
    }

    public AddressIcomView getAddressView() {
        return addressView;
    }

    public void setAddressView(AddressIcomView addressView) {
        this.addressView = addressView;
    }

    private void initMapView() {
        mMapView.getGrid().setVisibility(false);
        mMapView.setBackgroundColor(Color.argb(0, 255, 255, 255));
        mMapView.setMapBackground(Color.WHITE, Color.argb(0, 255, 255, 255), 0, 0);
        mMapView.getGrid().setType(GridType.NONE);
        mMobileConfig = RestoreManager.getInstance().restoreMobileMapConfig();

        if (!ListUtil.isEmpty(mMobileConfig.getSourceConfigArrayList())) {
            if (null == mMaploadCallback) {
                mMaploadCallback = new DefaultZ3MaploadCallback();
            }
            MapLoadTool.LoadMap(this, mMapView, mMobileConfig,mMaploadCallback);
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

    // 地图查找坐标地址功能
    private class MapQueryAddressListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            cleanMapView();
//            Intent intent = new Intent(getActivity(), MapPoiSearchActivity.class);
//            startActivityForResult(intent, QUERY_CODE);
            Intent intent = new Intent(MapActivity.this, MapAddressSearchActivity.class);
            startActivityForResult(intent, QUERY_ADDRASS);
        }
    }


    private void getPlanningData() {
        Z3PlanTask plantask = (Z3PlanTask) getIntent().getSerializableExtra("planning_tasks");
        SessionManager.currentTaskIntMapOpretor = plantask;
    }

    /**
     * 初始化地图操作
     */
    private void initMapviewOperator(Intent intent) {
        getPlanningData();

        userIntent = intent;
        Bitmap bmpMarker = BitmapFactory.decodeResource(HostApplication.getApplication().getResources(), R.drawable.icon_locationme);
        Drawable drawable = DrawableUtil.createDrawable(bmpMarker, 32, 32);
        myLocationPointSymbol = new PictureMarkerSymbol(drawable);
        myLocationPointSymbol.setOffsetY(drawable.getBounds().height() / 2);

        if (null == mMapView) {
            return;
        }
        String className = null;
        if (null != intent) {
            Bundle bundle = intent.getExtras();
            if (null == bundle) {
                return;
            }
            try {
                if (bundle.containsKey(MAP_OPERATOR)) {
                    className = bundle.getString(MAP_OPERATOR);
                    viewTitle.setTitleText(bundle.getString(MAP_TITLE));
                    rightActionFlag = bundle.getString(RIGHTBUTTONACTION);
                } else if (bundle.containsKey(DEVICE_OPERATOR)) {
                    className = bundle.getString(DEVICE_OPERATOR);
                    viewTitle.setTitleText(bundle.getString(MAP_TITLE));
                } else if (bundle.containsKey(LOCATION_OPERATOR)) {
                    className = bundle.getString(LOCATION_OPERATOR);
                    viewTitle.setTitleText(bundle.getString(MAP_TITLE));
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
            viewTitle.setTitleText(R.string.map_title);
            mapViewOperator = new EmptyOpratorXtd();
            mapViewOperator.setMapviewOption(mMapView, this);
            mtoobar.setVisibility(View.VISIBLE);
        } else {
            try {
                mapViewOperator = (AMapViewOperator) Class.forName(className).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setMapViewOperator(mapViewOperator);
    }

    public Intent getUserIntent() {
        if (null == userIntent) {
            return getIntent();
        }
        return userIntent;
    }

    public void setMapViewOperator(AMapViewOperator mapViewOperator) {
        this.mapViewOperator = mapViewOperator;
        if (null != this.mapViewOperator) {
            this.mapViewOperator.setMapviewOption(mMapView, this);
        }
    }

    public void cleanMapView() {
        mGPSLocateGID = -1;
        if (null == mMapView || !mMapView.isLoaded()) {
            return;
        }

        try {
            if (mMapView.getCallout().isShowing()) {
                mMapView.getCallout().hide();
            }
        } catch (Exception e) {
        }

        GraphicsLayer graphicsLayer = null;
        try {
            graphicsLayer = LayerTool.getGraphicsLayer(mMapView);
        } catch (Exception e) {
        }

        if (null != graphicsLayer) {
            graphicsLayer.removeAll();
        }

        GraphicsLayer gpsGraphicsLayer = null;
        try {
            gpsGraphicsLayer = LayerTool.getGPSGraphicsLayer(mMapView);
        } catch (Exception e) {
        }

        if (null != gpsGraphicsLayer) {
            gpsGraphicsLayer.removeAll();
        }

        GraphicsLayer animationLayer = null;
        try {
            animationLayer = LayerTool.getAnimationLayer(mMapView);
        } catch (Exception e) {
        }

        if (null != animationLayer) {
            animationLayer.removeAll();
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

    private class DefaultZ3MaploadCallback implements AMapViewLoader.Z3MaploadCallback {

        @Override
        public void onStatusChanged(Object arg0, OnStatusChangedListener.STATUS arg1) {
            if (arg1 == OnStatusChangedListener.STATUS.LAYER_LOADED) {
                if (isMapLoaded()) {
                    notifyMapLoaded();
                }
            }
        }
    }

    public class GoOnClickListener implements OnClickListener {

        Context context = MapActivity.this;

        @Override
        public void onClick(View v) {
            if (AppUtil.isAvilible(context, BAIDU_PACKAGE_NAME) && AppUtil.isAvilible(context, GAODE_PACKAGE_NAME)) {//传入指定应用包名
                showNaviSelectDialog(context);
            } else if (AppUtil.isAvilible(context, GAODE_PACKAGE_NAME)) {
                //调用高德导航
                navigateHelper.startMiniNavigtion(targetLonLat);
            } else if (AppUtil.isAvilible(context, BAIDU_PACKAGE_NAME)) {
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
            Toast.makeText(this, R.string.no_location, Toast.LENGTH_LONG).show();
            return;
        }

        double[] xy = CoordTransfer.transToLocal(location.getLatitude(), location.getLongitude());
        Point point = new Point();
        if (null != xy && 2 == xy.length) {
            point.setX(xy[0]);
            point.setY(xy[1]);
        } else {
            Toast.makeText(this, R.string.no_location, Toast.LENGTH_LONG).show();
            return;
        }
        mapView.centerAt(point, true);

        updateCurrentLocation(point);
    }

    private class MyMapMenuClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_measure_length:
                    measureLength();
                    break;
                case R.id.layout_query_point:
                    break;
                case R.id.layout_mainmenu_clearmap:
                    cleanMapView();
                    break;
                case R.id.layout_mainmenu_more:
                    CustomPopupToolView view = new CustomPopupToolView(mMapView, MapActivity.this);
                    view.show(v, MapActivity.this);
                    break;

                default:
                    break;
            }
        }
    }

    public void measureArea() {

        if (null == mMapView || !mMapView.isLoaded()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.map_not_loaded), Toast.LENGTH_SHORT).show();
        }

        if (measureTool == null) {
            GraphicsLayer glayer = null;
            try {
                glayer = LayerTool.getGraphicsLayer(mMapView);
            } catch (Exception e) {
            }
            measureTool = new MapMeasure(this, mMapView, glayer, MeasureMode.Area);
        }

        mMapView.getCallout().hide();
        measureTool.setMeasureMode(MeasureMode.Area);
        measureTool.start();
    }

    public void measureLength() {
        if (null == mMapView || !mMapView.isLoaded()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.map_not_loaded), Toast.LENGTH_SHORT).show();
        }

        if (measureTool == null) {
            GraphicsLayer glayer = null;
            try {
                glayer = LayerTool.getGraphicsLayer(mMapView);
            } catch (Exception e) {
            }
            measureTool = new MapMeasure(this, mMapView, glayer, MeasureMode.Length);
        }

        mMapView.getCallout().hide();
        measureTool.setMeasureMode(MeasureMode.Length);
        measureTool.start();
    }


    public void onBackButtonClicked(View view) {
        notifyBackEvent();
    }

    public void onActionButtonClickedForMap(View view) {
        notifyActionEvent();
    }

    public void onSearchBtnClicked(View view) {
        notifySearchActionEvent();
    }

    public void showSerachResult(final QueryResultShowModel queryResult, boolean showActionButtons) {
        queryResultsControler = new IdentifyResultsController(this, showActionButtons);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layoutZoomControl.setVisibility(View.INVISIBLE);
                queryResultsControler.showIdentifyResults(queryResult.graphics);
            }
        });
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public MapView getMapView() {
        return mMapView;
    }

    @Override
    public View getRootView() {
        return ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
    }

    @Override
    public View getAchor() {
        return achor;
    }

    public MapActivityTitleView getTitleView() {
        return viewTitle;
    }

    public IdentifyResultsController getIdentifyResultController() {
        return queryResultsControler;
    }

    //MapActivity界面的事件上报按钮
    public Button getReport() {
        return report;
    }

    @Override
    public View getmTitleView() {
        return null;
    }

    @Override
    public View getmRlMapZoomView() {
        return layoutZoomControl;
    }

    @Override
    public View getMapLocationLayout() {
        return null;
    }

    @Override
    public OnClickListener getNaviClickListener() {
        return new GoOnClickListener();
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

    /**
     * 更新地图上的GPS标记位置
     */
    private void updateCurrentLocation(Point point) {

        if (mGPSGraphicsLayer == null || point == null) {
            return;
        }

        try{
            if(null == mGPSGraphicsLayer.getGraphic(mGPSLocateGID)){
                mGPSLocateGID = -1;
            }
        } catch (Exception e){
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
