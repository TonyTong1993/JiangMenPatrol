package com.ecity.cswatersupply.menu.map;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.Toast;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.QueryResultShowModel;
import com.ecity.cswatersupply.model.metaconfig.QueryLayerIDs;
import com.ecity.cswatersupply.utils.CustomRenderer;
import com.ecity.cswatersupply.utils.GeometryUtil;
import com.ecity.cswatersupply.utils.LayerTool;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;
import com.esri.core.tasks.ags.identify.IdentifyParameters;
import com.esri.core.tasks.ags.identify.IdentifyResult;

public class RectIdentifyOperatorXtd extends AMapIdentifyOperator {
    private static final long serialVersionUID = 1081381232254065525L;
    private ArrayList<Graphic> graphics = new ArrayList<Graphic>();
    private boolean allowResultActions;
    private List<String> queryLayerNames;
    private List<Integer> queryLayerIds;

    /**
     * @param allowResultActions 是否允许导航到设备和上报事件。
     */
    public RectIdentifyOperatorXtd(boolean allowResultActions) {
        identifyType = IdentifyType.Rect;
        this.allowResultActions = allowResultActions;

    }

    @Override
    protected IdentifyParameters getIdentifyParameter(float px, float py) {

        int radius = 20;
        IdentifyParameters params = null;
        if (null != startPoint && null != endPoint) {
            MapView mapView = mWeakMapView.get();
            double xMin, xMax, yMin, yMax;
            if (startPoint.getX() > endPoint.getX()) {
                xMin = endPoint.getX();
                xMax = startPoint.getX();
            } else {
                xMin = startPoint.getX();
                xMax = endPoint.getX();
            }
            if (startPoint.getY() > endPoint.getY()) {
                yMin = endPoint.getY();
                yMax = startPoint.getY();
            } else {
                yMin = startPoint.getY();
                yMax = endPoint.getY();
            }
            Envelope queryeEnvelope = new Envelope(xMin, yMin, xMax, yMax);
            params = new IdentifyParameters();// 识别任务所需参数对象
            params.setTolerance(radius);
            params.setDPI(96);// 设置地图的DPI
            if (null == getQueryLayerIds()) {
                ToastUtil.showLong(R.string.map_tips_no_rect_layer);
                return null;
            }
            params.setLayers(getQueryLayerIds());// 设置要识别的图层数组
            //            params.setLayers(new int[] { 0, 3 });QueryLayerIDs
            params.setLayerMode(IdentifyParameters.ALL_LAYERS);// 设置识别模式
            params.setGeometry(queryeEnvelope);// 设置识别位置
            params.setSpatialReference(mapView.getSpatialReference());// 设置坐标系
            params.setMapHeight(mapView.getHeight());// 设置地图像素高
            params.setMapWidth(mapView.getWidth());// 设置地图像素宽
            Envelope env = new Envelope();
            mapView.getExtent().queryEnvelope(env);
            params.setMapExtent(env);// 设置当前地图范围
        }

        return params;
    }

    @Override
    protected void handleSingleTap(float px, float py) {
        if (identifyType == IdentifyType.Rect) {
            rectIdentify(px, py);
        }
    }

    private void rectIdentify(float px, float py) {
        if (isInQuery) {
            return;
        }
        IMapOperationContext mapFragment = mWeakMapFragment.get();
        MapView mapView = mWeakMapView.get();
        GraphicsLayer graphicsLayer = mWeakGraphicsLayer.get();
        if (null != mapFragment.getmMapOperatorTipsView()) {
            mapFragment.getmMapOperatorTipsView().setTipsText(R.string.map_tips_rect);
        }
        if (null == mapFragment || null == mapView || null == graphicsLayer) {
            return;
        }
        if (null == startPoint) {
            graphicsLayer.removeAll();
            startPoint = mapView.toMapPoint(px, py);
            Map<String, Object> att = new HashMap<String, Object>();
            att.put("class", 0);
            Graphic gclick = new Graphic(startPoint, CustomRenderer.getInstance().PIPENETPO_POINT_CIRCLE_GREEN(), att, 0);
            graphicsLayer.addGraphic(gclick);
        } else {
            isInQuery = true;
            if (null != mapFragment.getmMapOperatorTipsView()) {
                mapFragment.getmMapOperatorTipsView().disPlayProgressBar();
            }
            endPoint = mapView.toMapPoint(px, py);
            Map<String, Object> att = new HashMap<String, Object>();
            att.put("class", 0);
            Graphic gclick = new Graphic(endPoint, CustomRenderer.getInstance().PIPENETPO_POINT_CIRCLE_GREEN(), att, 0);
            graphicsLayer.addGraphic(gclick);

            double xMin, xMax, yMin, yMax;
            if (startPoint.getX() > endPoint.getX()) {
                xMin = endPoint.getX();
                xMax = startPoint.getX();
            } else {
                xMin = startPoint.getX();
                xMax = endPoint.getX();
            }
            if (startPoint.getY() > endPoint.getY()) {
                yMin = endPoint.getY();
                yMax = startPoint.getY();
            } else {
                yMin = startPoint.getY();
                yMax = endPoint.getY();
            }
            Envelope queryeEnvelope = new Envelope(xMin, yMin, xMax, yMax);

            Map<String, Object> att2 = new HashMap<String, Object>();
            att2.put("class", 1);
            Symbol[] symbols = CustomRenderer.getInstance().generateSymbols();
            Graphic graphic2 = new Graphic(queryeEnvelope, symbols[8], att2, 0);
            graphicsLayer.addGraphic(graphic2);
            startIdentify(getIdentifyParameter(px, py));
            startPoint = null;
            endPoint = null;
        }
    }

    // 查询出来结果后，进行处理的回调方法
    protected void onManageResult(IdentifyResult[] resultArray) {
        IMapOperationContext mapFragment = getOperationContext();
        MapView mapView = mWeakMapView.get();

        if ((mapFragment == null) || (mapView == null)) {
            return;
        }

        if ((resultArray == null) || (resultArray.length == 0)) {
            isInQuery = false;
            Toast.makeText(mapFragment.getContext(), R.string.map_query_no_device, Toast.LENGTH_SHORT).show();
            return;
        }

        int length = resultArray.length;
        if (length >= 50) {
            isInQuery = false;
            Toast.makeText(mapFragment.getContext(), R.string.map_query_bigger_spare, Toast.LENGTH_LONG).show();
            return;
        }

        graphics.clear();

        GraphicsLayer graphicsLayer = mWeakGraphicsLayer.get();

        if (null != graphicsLayer) {
            for (IdentifyResult result : resultArray) {
                Graphic graphic = parseResultGraphic(result);
                graphics.add(graphic);
                graphicsLayer.addGraphic(graphic);
            }
        }


        QueryResultShowModel queryResult = new QueryResultShowModel();
        queryResult.graphics = graphics;
        mapFragment.showSerachResult(queryResult, allowResultActions);
    }

    private Graphic parseResultGraphic(IdentifyResult result) {
        Geometry geometry = result.getGeometry();
        String typeName = geometry.getType().name();
        Point point = GeometryUtil.GetGeometryCenter(geometry);

        Map<String, Object> attributes = new HashMap<String, Object>(result.getAttributes());
        // 在拉框查询和关联设备功能中，我们在graphic里放了其他属性，但是不需要显示在属性详情界面，这些属性的key都有"hidden_"前缀。
        String hiddenAttrPrefix = HostApplication.getApplication().getString(R.string.map_device_hidden_attr_prefix);
        attributes.put(hiddenAttrPrefix + "layerId", result.getLayerId());
        attributes.put(hiddenAttrPrefix + "location", String.valueOf(point.getX()) + "," + String.valueOf(point.getY()));
        attributes.put(hiddenAttrPrefix + "x", point.getX());
        attributes.put(hiddenAttrPrefix + "y", point.getY());
        attributes.put(hiddenAttrPrefix + "layerName", result.getLayerName());
        attributes.put(hiddenAttrPrefix + "queryUrl", getQueryUrl());
        attributes.put("layerId", result.getLayerId()); // SearchResultsAdapter中，显示设备类型时，需要知道图层id

        Graphic graphic = null;
        if (typeName.equalsIgnoreCase("point")) {
            graphic = new Graphic(geometry, CustomRenderer.getInstance().PIPENETPO_POINT_CIRCLE_RED(), attributes, 0);
        } else if (typeName.equalsIgnoreCase("polyline")) {
            graphic = new Graphic(geometry, CustomRenderer.getInstance().PIPENETPO_LINE_SOLID(), attributes, 0);
        } else if (typeName.equalsIgnoreCase("polygon")) {
            graphic = new Graphic(geometry, CustomRenderer.getInstance().PIPENETPO_POLYGON_FILL(), attributes, 0);
        }
        return graphic;
    }

    private int[] getQueryLayerIds() {
        List<Integer> idList = new ArrayList<Integer>();
		
		if(ListUtil.isEmpty(queryLayerIds) && ListUtil.isEmpty(queryLayerNames)) {
            return QueryLayerIDs.getQueryNetlayerIDs();
        }

        if (!ListUtil.isEmpty(queryLayerIds)) {
            idList = queryLayerIds;
        }

        if (!ListUtil.isEmpty(queryLayerNames)) {
            idList = getQueryLayerIdByName(queryLayerNames);
        }

        if(!ListUtil.isEmpty(idList)) {
            int size = idList.size();
            int[] ids = new int[size];
            for(int i = 0; i < size; i++) {
                ids[i] = idList.get(i);
            }
            return ids;
        }

        return null;
    }

    private List<Integer> getQueryLayerIdByName(List<String> queryLayerNames) {
        int[] allLayerIds = QueryLayerIDs.getQueryNetlayerIDs();
        List<Integer> targetLayerIds = new ArrayList<Integer>();
        for (String layerName : queryLayerNames) {
            for (int layerId : allLayerIds) {
                if ((QueryLayerIDs.getDnamebyLayerId(layerId)).contains(layerName)) {
                    targetLayerIds.add(layerId);
                }
            }
        }
        return targetLayerIds;
    }

    /**
     * 设置要查询的图层名称
     *
     * @param layerNames
     */
    public void setQueryLayerNames(List<String> layerNames) {
        this.queryLayerNames = layerNames;
    }

    /**
     * 设置要查询的图层id
     */
    public void setQueryLayerIds(List<Integer> queryLayerIds) {
        this.queryLayerIds = queryLayerIds;
    }
}
