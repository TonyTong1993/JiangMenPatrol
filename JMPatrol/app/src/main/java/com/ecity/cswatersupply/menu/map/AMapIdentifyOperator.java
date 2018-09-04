package com.ecity.cswatersupply.menu.map;

import java.lang.ref.WeakReference;

import android.os.AsyncTask;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.utils.GraphicFlash;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Point;
import com.esri.core.tasks.ags.identify.IdentifyParameters;
import com.esri.core.tasks.ags.identify.IdentifyResult;
import com.esri.core.tasks.ags.identify.IdentifyTask;

/**
 * @author Administrator
 *
 */
public abstract class AMapIdentifyOperator implements OnSingleTapListener {
    private static final long serialVersionUID = 1912307086231185055L;
    private String mUrl;
    protected WeakReference<IMapOperationContext> mWeakMapFragment;
    protected WeakReference<MapView> mWeakMapView;
    protected WeakReference<GraphicsLayer> mWeakGraphicsLayer;
    protected IdentifyType identifyType;
    protected Point startPoint;
    protected Point endPoint;
    protected boolean isInQuery = false;
    protected GraphicFlash graphicFlash;
    protected MapView mapView;

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getQueryUrl() {
        return mUrl;
    }

    public enum IdentifyType {
        Point, Rect
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public void setInQuery(boolean isInQuery) {
        this.isInQuery = isInQuery;
    }

    protected void handleSingleTap(float px, float py) {

    }

    //坐标转换
    protected void onConvert(double x, double y) {

    }

    //坐标转换完毕后,回调,实现时务毕要调用setIdentifyParameter方法
    protected void onPostConvert(double x, double y) {

    }

    //查询出来结果后，进行处理的回调方法
    protected void onManageResult(IdentifyResult[] result) {

    }

    protected IdentifyParameters getIdentifyParameter(float px, float py) {
        return null;
    }

    public void setMapviewOption(MapView mapView, IMapOperationContext mapFragment, GraphicsLayer layer) {
        if (null == mapView || null == mapFragment || null == layer) {
            return;
        }
        this.mWeakMapFragment = new WeakReference<IMapOperationContext>(mapFragment);
        this.mWeakMapView = new WeakReference<MapView>(mapView);
        this.mWeakGraphicsLayer = new WeakReference<GraphicsLayer>(layer);
    }

    protected void startIdentify(IdentifyParameters identifyParameter) {
        CommonIdentifyTask identifyTask = new CommonIdentifyTask(mUrl);
        if (null != identifyParameter) {
            identifyTask.execute(identifyParameter);
        } else {
            isInQuery = false;
        }
    }

    @Override
    public void onSingleTap(float px, float py) {
        mapView = mWeakMapView.get();
        if (mapView.isLoaded()) {
           handleSingleTap(px, py);
        }
    }

    protected class CommonIdentifyTask extends AsyncTask<IdentifyParameters, Void, IdentifyResult[]> {
        IdentifyTask task;
        String url;

        public CommonIdentifyTask(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            task = new IdentifyTask(url);
        }

        @Override
        protected IdentifyResult[] doInBackground(IdentifyParameters... params) {
            IdentifyResult[] result = null;
            if (null != params && 0 < params.length) {
                IdentifyParameters mParam = params[0];
                try {
                    result = task.execute(mParam);//执行识别任务
                } catch (Exception e) {
                    isInQuery = false;
                    LogUtil.e(this, e);
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(IdentifyResult[] result) {
            IMapOperationContext mapFragment = mWeakMapFragment.get();
            if (null != mapFragment && null != mapFragment.getmMapOperatorTipsView()) {
                mapFragment.getmMapOperatorTipsView().disMissProgressBar();
                mapFragment.getmMapOperatorTipsView().setTipsText("");
                if(mapFragment instanceof MapActivity){
                    mapFragment.getmMapOperatorTipsView().displayButtonClean();
                }
            }
            onManageResult(result);
            if (null != graphicFlash) {
                graphicFlash.stopFlash();
            }
            graphicFlash = null;
        }
    }

     public IMapOperationContext getOperationContext() {
        return mWeakMapFragment.get();
    }

}
