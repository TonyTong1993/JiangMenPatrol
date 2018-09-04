package com.ecity.cswatersupply.menu.map;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.QueryResultShowModel;
import com.ecity.cswatersupply.service.NetService;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.utils.CustomRenderer;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConditionAndCodeQueryOperator {

    private String mUrl;
    private boolean isInQuery = false;
    private IMapOperationContext fragment;
    protected WeakReference<MapView> mWeakMapView;
    protected GraphicsLayer graphicsLayer;
    private String querycondition;
    private ArrayList<Graphic> graphicsList = new ArrayList<Graphic>();
    private int layerId;

    public ConditionAndCodeQueryOperator(String mQuerycondition) {
        this.querycondition = mQuerycondition;
    }

    public void setLayerId(int layerId) {
        this.layerId = layerId;
        if(-1 == layerId) {
            ToastUtil.showLong("查询图层为空");
            return;
        }
        this.mUrl = ServiceUrlManager.getInstance().getSpacialSearchUrl() + "/" + layerId;
    }

    public void setMapviewOption(MapView mapView, IMapOperationContext fragment) {
        if (null == mapView || null == fragment) {
            return;
        }
        this.fragment = fragment;
        mWeakMapView = new WeakReference<MapView>(mapView);
    }

    public void startQuery() {
        if (isInQuery) {
            return;
        }

        startQuery(mUrl, querycondition);
    }

    private void startQuery(String url, String where) {
        EventBusUtil.register(this);
        NetService.getInstance().queryNode(url, where, true);
    }

    public void onEventMainThread(ResponseEvent event) {
        if (event.getId() != ResponseEventStatus.NET_SERVER_QUERY_NODE) {
            return;
        }

        EventBusUtil.unregister(this);
        JSONObject data = event.getData();
        FeatureSet featureSet = parseQueryResult(data);
        onManageResult(featureSet);
        isInQuery = false;
    }

    private Graphic[] parsePoints(JSONArray serverData, int layerId) {
        if (serverData == null) {
            return null;
        }

        Graphic[] graphics = new Graphic[serverData.length()];
        for (int i = 0; i < serverData.length(); i++) {
            JSONObject jsonObj = serverData.optJSONObject(i);
            Point point = new Point();
            JSONObject geometry = jsonObj.optJSONObject("geometry");
            point.setX(geometry.optDouble("x"));
            point.setY(geometry.optDouble("y"));
            JSONObject attObj = jsonObj.optJSONObject("attributes");
            Map<String, Object> attributes = json2Map(attObj);
            attributes.put("<图层ID>", layerId);
            graphics[i] = new Graphic(point, null, attributes, 0);
        }

        return graphics;
    }

    private Graphic[] parseLines(JSONArray serverData, int layerId) {
        if (serverData == null) {
            return null;
        }

        Graphic[] graphics = new Graphic[serverData.length()];
        for (int k = 0; k < serverData.length(); k++) {
            JSONObject jsonObj = serverData.optJSONObject(k);
            JSONObject geometry = jsonObj.optJSONObject("geometry");
            JSONArray paths = geometry.optJSONArray("paths");
            Polyline polyline = new Polyline();
            for (int i = 0; i < paths.length(); i++) {
                JSONArray path = paths.optJSONArray(i);
                for (int j = 0; j < path.length(); j++) {
                    JSONArray elm = path.optJSONArray(j);
                    double x = 0.0, y = 0.0;
                    x = elm.optDouble(0);
                    y = elm.optDouble(1);
                    if (j == 0) {
                        polyline.startPath(x, y);
                    } else {
                        polyline.lineTo(x, y);
                    }
                }
            }
            JSONObject attObj = jsonObj.optJSONObject("attributes");
            Map<String, Object> attributes = json2Map(attObj);
            attributes.put("<图层ID>", layerId);
            graphics[k] = new Graphic(polyline, null, attributes, 0);
        }

        return graphics;
    }

    private FeatureSet parseQueryResult(JSONObject serverData) {
        FeatureSet fes = new FeatureSet();
        try {
            if (serverData.optString("geometryType").equalsIgnoreCase("esriGeometryPoint")) {
                Graphic[] graphics = parsePoints(serverData.optJSONArray("features"), layerId);
                fes.setGraphics(graphics);
            } else if (serverData.optString("geometryType").equalsIgnoreCase("esriGeometryPolyline")) {
                Graphic[] graphics = parseLines(serverData.optJSONArray("features"), layerId);
                fes.setGraphics(graphics);
            } else {
                // we do not need to handle other types(for example, PolyLine) in this function
            }
            JSONObject spatialReferenceJson = serverData.optJSONObject("spatialReference");
            JsonParser parser = new JsonFactory().createJsonParser(spatialReferenceJson.toString());
            SpatialReference spatialReference = SpatialReference.fromJson(parser);
            fes.setSpatialReference(spatialReference);
        } catch (Exception e) {
            LogUtil.e(this, e);
        }

        return fes;
    }

    private Map<String, Object> json2Map(JSONObject json) {
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<String> rawtypes = json.keys();
        while (rawtypes.hasNext()) {
            String key = rawtypes.next().toString();
            String value = json.optString(key);
            if (value == null) {
                map.put(key, "");
            } else {
                map.put(key, value.trim());
            }
        }

        return map;
    }

    private void onManageResult(FeatureSet result) {
        if (null == result) {
            ToastUtil.showLong(R.string.no_results);
            return;
        }

        Graphic[] graphics = result.getGraphics();
        if (graphics.length == 0) {
            ToastUtil.showLong(ResourceUtil.getStringById(R.string.no_results));
            return;
        }

        for (int j = 0; j < graphics.length; j++) {
            Map<String, Object> attr = graphics[j].getAttributes();
            attr.put("<图层ID>", layerId);
            Geometry geometry = graphics[j].getGeometry();
            Graphic graphic = new Graphic(geometry, CustomRenderer.getInstance().PIPENETPO_POINT_CIRCLE_RED(), attr, 0);
            graphicsList.add(graphic);
        }
        QueryResultShowModel queryResult = new QueryResultShowModel();
        queryResult.graphics = graphicsList;
        fragment.showSerachResult(queryResult, false);
    }
}
