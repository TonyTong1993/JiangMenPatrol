package com.ecity.cswatersupply.emergency;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.model.EQMonitorStationModel;
import com.ecity.cswatersupply.emergency.model.EQRefugeInfoModel;
import com.ecity.cswatersupply.emergency.model.EarthQuakeInfoModel;
import com.ecity.cswatersupply.utils.DrawableUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.Symbol;

public class EarthQuakeDisplayUtil {
    private static PictureMarkerSymbol stationSymbol;
    private static PictureMarkerSymbol refugeSymbol;
    private static DecimalFormat df = new DecimalFormat("0.0");

    public static void drawEarthQuakesOnLayer(GraphicsLayer gLayer, List<EarthQuakeInfoModel> dataSource) {
        if (null == gLayer) {
            return;
        }

        if(null == dataSource || dataSource.size() == 0) {
            gLayer.removeAll();
        }

        gLayer.removeAll();
        int size = dataSource.size();
        Graphic[] graphics = new Graphic[size];
        for (int i = 0; i < size; i++) {
            EarthQuakeInfoModel tmp = dataSource.get(i);
            Map<String, Object> attr = new HashMap<String, Object>();
            attr.put("id", String.valueOf(tmp.getId()));
            attr.put("level", df.format(tmp.getML()));
            Point pnt = new Point(tmp.getLongitude(), tmp.getLatitude());
            Symbol symbol = getEQMarkerSymbol(tmp.getML());
            Graphic g = new Graphic(pnt, symbol, attr, 0);
            graphics[i] = g;
        }
        gLayer.addGraphics(graphics);
    }

    public static Graphic drawEarthQuakesOnLayerFromXG(GraphicsLayer gLayer, EarthQuakeInfoModel dataSource) {
        if (null == gLayer || null == dataSource) {
            return null;
        }

        Map<String, Object> attr = new HashMap<String, Object>();
        Point pnt = new Point(dataSource.getLongitude(), dataSource.getLatitude());
        Symbol symbol = getEQMarkerSymbol(dataSource.getML());
        Graphic g = new Graphic(pnt, symbol, attr, 0);
        gLayer.addGraphic(g);
        return g;
    }

    public static void drawEQStationsOnLayer(GraphicsLayer gLayer, List<EQMonitorStationModel> dataSource) {
        if (null == gLayer || null == dataSource || dataSource.size() == 0) {
            return;
        }

        gLayer.removeAll();
        int size = dataSource.size();
        Graphic[] graphics = new Graphic[size];
        for (int i = 0; i < size; i++) {
            Map<String, Object> attr = new HashMap<String, Object>();
            EQMonitorStationModel tmp = dataSource.get(i);
            attr.put("id", String.valueOf(tmp.getId()));
            Graphic g = new Graphic(new Point(tmp.getLongitude(), tmp.getLatitude()), getStationSymbol(), attr, 0);
            graphics[i] = g;
        }

        gLayer.addGraphics(graphics);
    }

    public static void drawEQRefugeOnLayer(GraphicsLayer gLayer, List<EQRefugeInfoModel> dataSource) {
        if (null == gLayer || null == dataSource || dataSource.size() == 0) {
            return;
        }

        gLayer.removeAll();
        int size = dataSource.size();
        Graphic[] graphics = new Graphic[size];
        for (int i = 0; i < size; i++) {
            Map<String, Object> attr = new HashMap<String, Object>();
            EQRefugeInfoModel tmp = dataSource.get(i);
            attr.put("id", String.valueOf(tmp.getId()));
            Graphic g = new Graphic(new Point(tmp.getLongitude(), tmp.getLatitude()), getRefugeSymbol(), attr, 0);
            graphics[i] = g;
        }

        gLayer.addGraphics(graphics);
    }

    private static PictureMarkerSymbol getStationSymbol() {
        if (null != stationSymbol) {
            return stationSymbol;
        }

        try {
            Bitmap bmpMarker = BitmapFactory.decodeResource(HostApplication.getApplication().getResources(), R.drawable.icon_eqstation);
            stationSymbol = new PictureMarkerSymbol(DrawableUtil.createDrawable(bmpMarker, 32, 32));
            stationSymbol.setOffsetY(16);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stationSymbol;
    }

    private static PictureMarkerSymbol getRefugeSymbol() {
        if (null != refugeSymbol) {
            return refugeSymbol;
        }

        try {
            Bitmap bmpMarker = BitmapFactory.decodeResource(HostApplication.getApplication().getResources(), R.drawable.refuge);
            refugeSymbol = new PictureMarkerSymbol(DrawableUtil.createDrawable(bmpMarker, 32, 32));
            refugeSymbol.setOffsetY(16);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return refugeSymbol;
    }

    public static PictureMarkerSymbol buildEQMarkerSymbol(int resourceid) {
        Bitmap bmp = BitmapFactory.decodeResource(HostApplication.getApplication().getResources(), resourceid);
        PictureMarkerSymbol symbol = new PictureMarkerSymbol(DrawableUtil.createDrawable(bmp, 32, 32));
        symbol.setOffsetY(16);
        return symbol;
    }

    public static PictureMarkerSymbol buildEQMarkerSymbol(int resourceid, double eqLevel, int color) {
        Bitmap bmp = BitmapFactory.decodeResource(HostApplication.getApplication().getResources(), resourceid);
        return buildEQMarkerSymbol(bmp, eqLevel, color);
    }

    public static PictureMarkerSymbol buildEQMarkerSymbol(Bitmap bmp, double eqLevel, int color) {
        String sLevel = df.format(eqLevel);
        PictureMarkerSymbol symbol = new PictureMarkerSymbol(DrawableUtil.createDrawable(bmp, 32, 32, color, 12.f, sLevel));
        symbol.setOffsetY(16);
        return symbol;
    }

    public static PictureMarkerSymbol getEQMarkerSymbol(double eqLevel) {
        Bitmap bmp;
        int color = 0;

        if (0.0 <= eqLevel && eqLevel < 2.0) {
            color = Color.parseColor("#dbf103");
            bmp = BitmapFactory.decodeResource(HostApplication.getApplication().getResources(), R.drawable.icon_eqlevel_1);
        } else if (2.0 <= eqLevel && eqLevel < 4.0) {
            color = Color.parseColor("#f1a003");
            bmp = BitmapFactory.decodeResource(HostApplication.getApplication().getResources(), R.drawable.icon_eqlevel_2);
        } else if (4.0 <= eqLevel && eqLevel < 7.0) {
            color = Color.parseColor("#f16503");
            bmp = BitmapFactory.decodeResource(HostApplication.getApplication().getResources(), R.drawable.icon_eqlevel_3);
        } else {
            color = Color.parseColor("#ff0000");
            bmp = BitmapFactory.decodeResource(HostApplication.getApplication().getResources(), R.drawable.icon_eqlevel_4);
        }

        return buildEQMarkerSymbol(bmp, eqLevel, color);
    }


    public static String getEarthQuickLayerName() {
        return HostApplication.getApplication().getResources().getString(R.string.eq_earthquake);
    }

    public static String getStationLayerName() {
        return HostApplication.getApplication().getResources().getString(R.string.eq_station);
    }

    public static String getRefugeLayerName() {
        return "应急避难场所";
    }
}
