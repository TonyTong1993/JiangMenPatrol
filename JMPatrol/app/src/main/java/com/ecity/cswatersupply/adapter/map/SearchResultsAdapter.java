package com.ecity.cswatersupply.adapter.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.planningtask.PointPartAttrAdapter;
import com.ecity.cswatersupply.menu.map.GraphicAttrListViewOperator;
import com.ecity.cswatersupply.menu.map.IMapOperationContext;
import com.ecity.cswatersupply.model.metaconfig.QueryLayerIDs;
import com.ecity.cswatersupply.model.planningTask.PointPartAttrInfo;
import com.ecity.cswatersupply.project.FuZhouPatrolModuleConfig;
import com.ecity.cswatersupply.ui.activities.ReportEventTypeSelectActivity;
import com.ecity.cswatersupply.ui.activities.planningtask.TaskAttrListsActivity;
import com.ecity.cswatersupply.utils.CoordTransfer;
import com.ecity.cswatersupply.utils.CustomRenderer;
import com.ecity.cswatersupply.utils.GeometryUtil;
import com.ecity.cswatersupply.utils.GraphicFlash;
import com.ecity.cswatersupply.utils.LayerTool;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.mobile.android.bdlbslibrary.utils.BaiDuUtils;
import com.ecity.mobile.android.bdlbslibrary.utils.LocGeoPoint;
import com.ecity.mobile.android.library.gpsengine.GPSEngine;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;
import com.z3app.android.util.StringUtil;

public class SearchResultsAdapter extends PagerAdapter implements OnPageChangeListener {
    public final static int GOTO_BAIDU_SHOW_DIALOG = 0;
    public final static int GOTO_BAIDU_SUCCESS_DIALOG = 1;
    public final static int GOTO_BAIDU_ERROR_DIALOG = 2;
    private ArrayList<Graphic> graphics;
    private int mChildCount = 0;
    // 管网查询结果
    private IMapOperationContext mMapFragment = null;
    private MapView mapView = null;
    private int selectedPipeGid = -1;
    private GraphicsLayer annimationLayer = null;
    private GraphicsLayer graphicsLayer = null;
    private LayoutInflater inflater = null;
    private GraphicFlash graphicFlash = null;
    MyHandler handler = new MyHandler();
    private boolean showActionButtons;

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int flag = msg.what;
            switch (flag) {
                case GOTO_BAIDU_SHOW_DIALOG:
                    LoadingDialogUtil.show(mMapFragment.getContext(), mMapFragment.getContext().getResources().getString(R.string.map_to_baidu));
                    break;
                case GOTO_BAIDU_SUCCESS_DIALOG:
                    LoadingDialogUtil.dismiss();
                    break;
                case GOTO_BAIDU_ERROR_DIALOG:
                    LoadingDialogUtil.dismiss();
                    ToastUtil.showShort(mMapFragment.getContext().getResources().getString(R.string.map_to_baidu_error));
                    break;
                default:
                    break;
            }
        }
    }

    public SearchResultsAdapter(ArrayList<Graphic> searchResults, IMapOperationContext mMapFragment) {
        this(searchResults, mMapFragment, true);
    }

    public SearchResultsAdapter(ArrayList<Graphic> searchResults, IMapOperationContext mMapFragment, boolean showActionButtons) {
        super();
        graphics = searchResults;
        this.mMapFragment = mMapFragment;
        mapView = mMapFragment.getMapView();
        this.inflater = LayoutInflater.from(mMapFragment.getContext());
        this.showActionButtons = showActionButtons;
        annimationLayer = LayerTool.getAnimationLayer(mapView);
        graphicsLayer = LayerTool.getPatrolPlanGraphicsLayer(mapView);
        selectedPipeGid = -1;
    }

    private View buildViewForGraphic(int index, Graphic graphic) {
        Map<String, Object> attibutes = graphic.getAttributes();
        View view = inflater.inflate(R.layout.view_searchresult, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv1 = (TextView) view.findViewById(R.id.tv_1);
        TextView tv2 = (TextView) view.findViewById(R.id.tv_2);
        LinearLayout ll_detail = (LinearLayout) view.findViewById(R.id.ll_detail);
        FrameLayout fl_go = (FrameLayout) view.findViewById(R.id.frame_go);
        FrameLayout fl_edit = (FrameLayout) view.findViewById(R.id.frame_edit);
        View horizontalDivider = view.findViewById(R.id.view_horizontal_divider);

        final String type = getDname(attibutes);
        fl_go.setOnClickListener(new GoOnClickListener(graphic, type));
        fl_edit.setOnClickListener(new EditOnClickListener(graphic, type));
        ll_detail.setOnClickListener(new DetailOnClickListener(graphic, type));

        tv_title.setText(index + 1 + "、" + type);
        String text1 = getfirstValue(attibutes, type);
        String text2 = getSecondValue(attibutes, type);
        tv1.setText(text1);
        tv2.setText(text2);
        if (!showActionButtons) {
            fl_go.setVisibility(View.GONE);
            fl_edit.setVisibility(View.GONE);
            horizontalDivider.setVisibility(View.GONE);
        }

        try {
            if (FuZhouPatrolModuleConfig.getConfig().isModuleUseable()) {
                fl_edit.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public void updateData(ArrayList<Graphic> searchResults) {
        graphics = searchResults;
        selectedPipeGid = -1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = null;
        if (null != graphics) {
            view = buildViewForGraphic(position, graphics.get(position));
            showLocator(graphics, 0);
        }
        container.addView(view);

        return view;
    }

    private String getDname(Map<String, Object> attibutes) {
        String dname = "";
        int layerId = -1;
        try {
            layerId = (Integer) attibutes.get("layerId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            layerId = (Integer) attibutes.get("<图层ID>");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (-1 != layerId) {
            dname = QueryLayerIDs.getDnamebyLayerId(layerId);
        }

        if (TextUtils.isEmpty(dname)) {
            dname = "未知类型";
        }
        return dname;

    }

    private String getfirstValue(Map<String, Object> attibutes, String type) {
        String result = "";
        if (attibutes.containsKey("阀门种类")) {
            result += "阀门种类：" + attibutes.get("阀门种类") ;
            result += " ";
        }

        if (attibutes.containsKey("材质")) {
            if(null != attibutes.get("材质") && !StringUtil.isBlank(attibutes.get("材质").toString())) {
                result += "材质：" + attibutes.get("材质").toString();
                result += " ";
            }
        }

        if (attibutes.containsKey("所在道路")) {
            if(null != attibutes.get("所在道路") && !StringUtil.isBlank(attibutes.get("所在道路").toString())) {
                result = attibutes.get("所在道路").toString();
                result += " ";
            }
        }

        if (attibutes.containsKey("道路名")) {
            result += "道路名：" + attibutes.get("道路名");
            result += " ";
        }

        if (attibutes.containsKey("pdperson")) {
            result += "派单人员：" + attibutes.get("pdperson");
            result += " ";
        } else if (attibutes.containsKey("REPORTER")) {
            result += "记者：" + attibutes.get("REPORTER");
            result += " ";
        } else if (attibutes.containsKey("测点性质")) {
            result += "测点性质：" + attibutes.get("测点性质");
            result += " ";
        }

        return result;
    }

    private String getSecondValue(Map<String, Object> attibutes, String type) {
        String result = "";
        if (attibutes.containsKey("埋深")) {
            result += "埋深：" + attibutes.get("埋深");
            result += " ";
        } else if (attibutes.containsKey("管径")) {
            result += "管径：" + attibutes.get("管径");
            result += " ";
        }

        if (attibutes.containsKey("建设年代")) {
            result += "建设年代：" + attibutes.get("建设年代");
            result += " ";
        } else if (attibutes.containsKey("department")) {
            result = "处理部门：" + attibutes.get("department");
        } else if (attibutes.containsKey("TEL")) {
            result = "电话：" + attibutes.get("TEL");
        }

        return result;
    }

    @Override
    public int getCount() {
        return null == graphics ? 0 : graphics.size();
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub
        /*
         * if (currentPosition == 0) { if (arg2 == 0) {
         * ToastUtils.show("已经是第一页了"); } } else if (currentPosition == maxPos -
         * 1) { if (arg2 == 0) { ToastUtils.show("已经是最后一页了"); } }
         */

    }

    @Override
    public void onPageSelected(int position) {
        showLocator(graphics, position);
    }

    /**
     * 此方法描述的是：Tip标志点
     * 
     * @author: wangliu94@163.com
     * @version: 2015年1月18日 下午9:50:56
     */

    private void showLocator(ArrayList<Graphic> results, int index) {
        try {
            Graphic g = null;

            if (null != results) {
                g = results.get(index);
                Point point = GeometryUtil.GetGeometryCenter(g.getGeometry());

                if(mMapFragment.getMapView().getScale() > 1000 ) {
                    //比例尺太小时看不清楚，因此放大到1：500
                    mMapFragment.getMapView().zoomToScale(point, 500);
                } else {
                    mMapFragment.getMapView().centerAt(point, true);
                }

                Graphic g_pipe = getSelectedGraphic(g);
                Graphic g_pipeNew = getHighlightGraphic(g);

                if (selectedPipeGid != -1) {
                    graphicsLayer.updateGraphic(selectedPipeGid, g_pipe);
                } else {
                    selectedPipeGid = graphicsLayer.addGraphic(g_pipe);
                }

                if (null != graphicFlash) {
                    graphicFlash.stopFlash();
                    graphicFlash = null;
                }
                graphicFlash = new GraphicFlash(g_pipe, g_pipeNew, annimationLayer);
                graphicFlash.startFlash(400);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 此方法描述的是： 标记当前选中的点、线、区
     * 
     * @author: wangliu94@163.com
     * @version: 2015年1月18日 下午11:18:44
     */

    private Graphic getSelectedGraphic(Graphic graphic) {
        Graphic g = null;
        try {
            if (null != graphic) {
                Geometry geometry = graphic.getGeometry();
                String type = geometry.getType().name();
                Map<String, Object> attribues = graphic.getAttributes();
                Symbol symbol = null;
                if (type.equalsIgnoreCase("polyline")) {
                    symbol = CustomRenderer.getInstance().PIPENETPO_LINE_SOLID_SELECTED();
                } else if (type.equalsIgnoreCase("polygon")) {
                    symbol = CustomRenderer.getInstance().PIPENETPO_POLYGON_FILL();
                } else if (type.equalsIgnoreCase("point")) {
                    symbol = CustomRenderer.getInstance().PIPENETPO_POINT_CIRCLE_GREEN();
                }
                g = new Graphic(geometry, symbol, attribues, 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return g;

    }

    private Graphic getHighlightGraphic(Graphic graphic) {
        Graphic g = null;
        try {
            if (null != graphic) {
                Geometry geometry = graphic.getGeometry();
                String type = geometry.getType().name();
                Symbol symbol = null;
                if (type.equalsIgnoreCase("polyline")) {
                    symbol = CustomRenderer.getInstance().PIPENETPO_LINE_SOLID_RED();
                } else if (type.equalsIgnoreCase("polygon")) {
                    symbol = CustomRenderer.getInstance().PIPENETPO_POLYGON_RED_FILL();
                } else if (type.equalsIgnoreCase("point")) {
                    symbol = CustomRenderer.getInstance().PIPENETPO_POINT_CIRCLE_RED(20);
                }
                g = new Graphic(geometry, symbol, null, 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return g;

    }

    class GoOnClickListener implements OnClickListener {
        private Graphic graphic;
        private String type;

        GoOnClickListener(Graphic graphic, String type) {
            this.graphic = graphic;
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Location locationStart = GPSEngine.getInstance().getLastLocation();
                        if (null == locationStart) {
                            Toast.makeText(mMapFragment.getContext(), "获取当前位置失败，建议先定位当前位置", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // 将GPS设备采集的原始GPS坐标转换成百度坐标
                        LocGeoPoint inPoint = new LocGeoPoint(locationStart.getLongitude(), locationStart.getLatitude(), 0);
                        LocGeoPoint outPoint = BaiDuUtils.getBaiduGpsPointByGps(inPoint);
                        LatLng start = new LatLng(outPoint.y, outPoint.x);
                        Point point = GeometryUtil.GetGeometryCenter(graphic.getGeometry());

                        // 本地转WGS84（GPS使用的坐标系）
                        double[] endPoints = CoordTransfer.transToLatlon(point.getX(), point.getY());
                        if (null != endPoints) {
                            LocGeoPoint endPoint = new LocGeoPoint(endPoints[0], endPoints[1], 0);
                            LocGeoPoint outPoint2 = BaiDuUtils.getBaiduGpsPointByGps(endPoint);
                            LatLng end = new LatLng(outPoint2.y, outPoint2.x);
                            startNavi(start.latitude, start.longitude, end.latitude, end.longitude);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * 开始导航
     * 
     * @param
     */
    public void startNavi(double mLat1, double mLon1, double mLat2, double mLon2) {
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(mLat2, mLon2);

        // 构建 导航参数
        NaviParaOption para = new NaviParaOption().startPoint(pt1).endPoint(pt2).startName("从这里开始").endName("到这里结束");
        try {
            Message msg = new Message();
            msg.what = 0;
            handler.sendMessage(msg);
            boolean isshow = BaiduMapNavigation.openBaiduMapNavi(para, mMapFragment.getContext());
            if (isshow) {
                Message dMsg = new Message();
                msg.what = 1;
                handler.sendMessage(dMsg);
            } else {
                Message eMsg = new Message();
                msg.what = 2;
                handler.sendMessage(eMsg);
            }

        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(mMapFragment.getContext());
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

    class EditOnClickListener implements OnClickListener {
        private Graphic graphic;
        private String type;

        EditOnClickListener(Graphic graphic, String type) {
            this.graphic = graphic;
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            try {
                Bundle bundle = new Bundle();
                bundle.putSerializable("device", graphic);
                UIHelper.startActivityWithExtra(ReportEventTypeSelectActivity.class,bundle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class DetailOnClickListener implements OnClickListener {
        private Graphic graphic;
        private String type;

        DetailOnClickListener(Graphic graphic, String type) {
            this.graphic = graphic;
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            try {
                if (null == graphic) {
                    ToastUtil.showLong(mMapFragment.getContext().getResources().getString(R.string.map_query_error));
                    return;
                }
                PointPartAttrInfo info = PointPartAttrAdapter.getInstance().getGraphicAttrAdapter(graphic);
                if (info == null || info.getAttrList() == null || info.getAttrList().size() == 0) {
                    ToastUtil.showLong(mMapFragment.getContext().getResources().getString(R.string.map_query_attr_error));
                    return;
                }

                Intent attrIntent = new Intent(mMapFragment.getContext(), TaskAttrListsActivity.class);
                Bundle attrBundle = new Bundle();
                attrBundle.putString(TaskAttrListsActivity.ATTR_LISTVIEW_COMEFROM, GraphicAttrListViewOperator.class.getName());
                attrBundle.putInt(TaskAttrListsActivity.ATTR_TITLE, R.string.map_query_details);
                attrBundle.putSerializable(TaskAttrListsActivity.ATTRS_LIST, (Serializable) info.getAttrList().get(0));
                attrIntent.putExtras(attrBundle);
                mMapFragment.getContext().startActivity(attrIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}