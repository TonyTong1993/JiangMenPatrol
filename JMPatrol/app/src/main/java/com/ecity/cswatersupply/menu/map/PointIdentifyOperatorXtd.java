package com.ecity.cswatersupply.menu.map;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.model.metaconfig.QueryLayerIDs;
import com.ecity.cswatersupply.utils.CustomRenderer;
import com.ecity.cswatersupply.utils.GraphicFlash;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.ags.identify.IdentifyParameters;
import com.esri.core.tasks.ags.identify.IdentifyResult;

public class PointIdentifyOperatorXtd extends AMapIdentifyOperator {
    private static final long serialVersionUID = 1081381232254065525L;
    private boolean allowResultActions;
    
    /**
     * @param allowResultActions 是否允许导航到设备和上报事件。
     */
    public PointIdentifyOperatorXtd(boolean allowResultActions) {
        identifyType = IdentifyType.Point;
        this.allowResultActions = allowResultActions;
    }

    @Override
    protected void handleSingleTap(float px, float py) {
        if(identifyType == IdentifyType.Point){
            pointIdentify(px, py);
        }
    }

    private void pointIdentify(float px, float py) {
        if (isInQuery) {
            return;
        }

        isInQuery = true;
        IMapOperationContext mapFragment = mWeakMapFragment.get();
        MapView mapView = mWeakMapView.get();
        GraphicsLayer graphicsLayer = mWeakGraphicsLayer.get();
        if (null == mapFragment || null == mapView || null == graphicsLayer) {
            return;
        }
        if (null != graphicFlash) {
            graphicFlash.stopFlash();
            graphicFlash = null;
        }
        startPoint = mapView.toMapPoint(px, py);
        Map<String, Object> att = new HashMap<String, Object>();
        att.put("class", 0);
        Graphic gclick = new Graphic(startPoint, CustomRenderer.getInstance().PIPENETPO_POINT_CIRCLE_GREEN(), att, 0);
        Graphic gclick2 = new Graphic(startPoint, CustomRenderer.getInstance().PIPENETPO_POINT_CIRCLE_RED(), att, 0);
        graphicFlash = new GraphicFlash(gclick, gclick2, graphicsLayer);
        graphicFlash.startFlash(500);

        startIdentify(getIdentifyParameter(px, py));
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
            params.setLayers(QueryLayerIDs.getQueryNetlayerIDs());// 设置要识别的图层数组
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

    // 查询出来结果后，进行处理的回调方法
    protected void onManageResult(IdentifyResult[] resultArray) {

    }

    private Graphic parseResultGraphic(IdentifyResult result) {
        return null;
    }

}
