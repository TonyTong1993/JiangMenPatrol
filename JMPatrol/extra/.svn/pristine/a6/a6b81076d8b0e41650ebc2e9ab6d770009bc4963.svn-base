package com.ecity.android.map.layer;

import android.util.Log;

import com.ecity.android.map.core.dbquery.task.DBQueryFutureTask;
import com.ecity.android.map.core.util.CompressUtil;
import com.ecity.android.map.core.util.FileHelper;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.io.EsriSecurityException;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

import net.lingala.zip4j.exception.ZipException;

import org.codehaus.jackson.JsonParser;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

/**
 * 将本地的
 * <p>
 * Created by zhengzhuanzi on 2017/6/20.
 */

public class DBLayer extends GraphicsLayer {
    volatile String D = "";
    private DBLayerServiceInfo layerInfo;
    final transient ConcurrentLinkedQueue<WeakReference<Future<FeatureSet>>> taskQueue = new ConcurrentLinkedQueue();
    private String pkgPath = "";

    public DBLayer() {
        this("");
    }

    public DBLayer(boolean initLayer) {
        this("");
    }

    public DBLayer(long handle) {
        this("");
    }

    public DBLayer(SpatialReference sr, Envelope fullextent) {
        this("");
    }

    public DBLayer(String pkgPath) {
        super(false);
        this.pkgPath = pkgPath;

        try {
            initPkgData();
        } catch (Exception localException1) {
            Log.e("DBLayer", "Failed to initialize the DBLayer.", localException1);
            if ((localException1 instanceof EsriSecurityException))
                changeStatus(OnStatusChangedListener.STATUS.fromInt(((EsriSecurityException) localException1).getCode()));
            else
                changeStatus(OnStatusChangedListener.STATUS.fromInt(-1004));
        }
    }

    @Override
    protected void initLayer() {
        initializeMinMaxScale(this.layerInfo.getMinScale(), this.layerInfo.getMaxScale());
        super.initLayer();
    }

    /**
     * 初始化离线数据包
     * 1、解压文件，验证里面数据是否完整：包含data.db 包含 mapserver.json 两个文件
     * 2、验证mapserver.json文件是否可以打开，文件信息是否完整
     */
    private boolean initPkgData() {

        if (!FileHelper.isFileExists(pkgPath)) {
            Log.e("ArcGIS", "Failed to initialize the ArcGISFeatureLayer. Pakage File Not exists");
            changeStatus(OnStatusChangedListener.STATUS.INITIALIZATION_FAILED);
            return false;
        }

        File file = new File(pkgPath);
        String dest = file.getAbsolutePath();
        File[] unzipFiles = null;
        try {
            int index = dest.lastIndexOf(".");
            dest = dest.substring(0,index);
            unzipFiles = CompressUtil.unzip(pkgPath, dest, null, "UTF-8");
        } catch (ZipException e) {
            e.printStackTrace();
        }

        if (null == unzipFiles) {
            changeStatus(OnStatusChangedListener.STATUS.INITIALIZATION_FAILED);
            return false;
        }

        return false;
    }


    void a(Graphic[] paramArrayOfGraphic) {
        if ((paramArrayOfGraphic == null) || (paramArrayOfGraphic.length == 0)) {
            return;
        }
        Graphic[] arrayOfGraphic1 = paramArrayOfGraphic;

        int[] arrayOfInt = getSelectionIDs();

        if ((getNumberOfGraphics() < 1)) {
            Map localMap = g();

            ArrayList localArrayList = new ArrayList();
            for (Graphic localGraphic : arrayOfGraphic1) {
                Integer localInteger1 = (Integer) localGraphic.getAttributeValue(this.D);
                if (localInteger1 != null) {
                    if ((arrayOfInt == null) || (Arrays.binarySearch(arrayOfInt, localInteger1.intValue()) < 0)) {
                        Integer localInteger2 = (Integer) localMap.get(localInteger1);
                        if (localInteger2 != null) {
                            removeGraphic(localInteger2.intValue());
                        }
                        localArrayList.add(localGraphic);
                    }
                }
            }
            arrayOfGraphic1 = (Graphic[]) localArrayList.toArray(new Graphic[0]);
        }
        addGraphics(arrayOfGraphic1);
        Log.d("ArcGIS.ThreadPool", "<< insertGraphics");
    }

    void b(Graphic[] paramArrayOfGraphic) {
        if ((paramArrayOfGraphic == null) || (paramArrayOfGraphic.length == 0)) {
            return;
        }
        Graphic[] arrayOfGraphic1 = paramArrayOfGraphic;

        int[] arrayOfInt = getSelectionIDs();

        if ((getNumberOfGraphics() > 0)) {
            Map localMap = g();

            ArrayList localArrayList = new ArrayList();
            for (Graphic localGraphic : arrayOfGraphic1) {
                Integer localInteger1 = (Integer) localGraphic.getAttributeValue(this.D);
                if (localInteger1 != null) {
                    if ((arrayOfInt == null) || (Arrays.binarySearch(arrayOfInt, localInteger1.intValue()) < 0)) {
                        Integer localInteger2 = (Integer) localMap.get(localInteger1);
                        if (localInteger2 != null) {
                            removeGraphic(localInteger2.intValue());

                            localArrayList.add(localGraphic);
                        }
                    }
                }
            }

            arrayOfGraphic1 = (Graphic[]) localArrayList.toArray(new Graphic[0]);
            addGraphics(arrayOfGraphic1);
        }

        Log.d("ArcGIS.ThreadPool", "<< insertGraphics");
    }

    private Map<Integer, Integer> g() {

        if (layerInfo != null) {
            setFullExtent(layerInfo.getFullEnvelope());
            setDefaultSpatialReference(SpatialReference.create(layerInfo.getWkid()));
        }

        HashMap localHashMap = new HashMap();

        if ((getNumberOfGraphics() > 0)) {
            int[] arrayOfInt1 = getGraphicIDs();

            if (arrayOfInt1 != null) {
                for (int k : arrayOfInt1) {
                    Graphic localGraphic = getGraphic(k);
                    if (localGraphic != null) {
                        Integer localInteger = (Integer) localGraphic.getAttributeValue(this.D);
                        if (localInteger != null) {
                            localHashMap.put(localInteger, Integer.valueOf(k));
                        }
                    }
                }
            }
        }

        return localHashMap;
    }

    private void dd() {
        Geometry.Type type = Geometry.Type.POLYGON;
        if (getRenderer() == null) {
            if (Geometry.Type.POLYGON == type) {
                SimpleFillSymbol localSimpleFillSymbol = new SimpleFillSymbol(1677721600);
                localSimpleFillSymbol.setOutline(new SimpleLineSymbol(-16777216, 2.0F));
                setRenderer(new SimpleRenderer(localSimpleFillSymbol));
            } else if (Geometry.Type.POLYLINE == type) {
                setRenderer(new SimpleRenderer(new SimpleLineSymbol(-65536, 1.0F)));
            } else if (Geometry.Type.POINT == type) {
                setRenderer(new SimpleRenderer(new SimpleMarkerSymbol(-16776961, 5, SimpleMarkerSymbol.STYLE.SQUARE)));
            }
        }
    }


    private void query() {
        try {
            DBQueryFutureTask task = new DBQueryFutureTask(null);
            getServiceExecutor().submit(task);
        } catch (RejectedExecutionException localRejectedExecutionException) {
        }
    }


    void a(List<Integer> paramList) {
        if (paramList.isEmpty()) {
            return;
        }

        Map localMap = g();

        for (Integer localInteger1 : paramList) {
            Integer localInteger2 = (Integer) localMap.get(localInteger1);
            if (localInteger2 != null)
                removeGraphic(localInteger2.intValue());
        }
    }

    final int[] c(Graphic[] paramArrayOfGraphic) {
        int[] arrayOfInt = null;
        if ((paramArrayOfGraphic != null) && (paramArrayOfGraphic.length > 0)) {
            arrayOfInt = new int[paramArrayOfGraphic.length];
            for (int i = 0; i < paramArrayOfGraphic.length; i++) {
                Integer localInteger = (Integer) paramArrayOfGraphic[i].getAttributeValue(this.D);
                arrayOfInt[i] = (localInteger != null ? localInteger.intValue() : -1);
            }
        }
        return arrayOfInt;
    }
}
