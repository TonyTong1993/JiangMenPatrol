package com.ecity.cswatersupply.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.esri.core.symbol.PictureMarkerSymbol;

public class BusDisplayUtil {
    private static Bitmap userMarkerOffline;
    private static Bitmap userMarkerOnline;
    private static Bitmap userMarkerHighlight;
    private static int iconSize = 40;
    private static float textSize = 16.f;
    private static int color = Color.BLACK;
    private static int hightColor = Color.GREEN;

    public static PictureMarkerSymbol getBusSymbol(String name, boolean isOnline) {
        Bitmap bmpMarker = null;
        if (null == name) {
            name = "";
        }

        if (isOnline) {
            bmpMarker = getUserMarkerOnline();
        } else {
            bmpMarker = getUserMarkerOffline();
        }

        BitmapDrawable drawable = DrawableUtil.createDrawableForText(bmpMarker, iconSize, color, textSize, name);
        PictureMarkerSymbol symbol = new PictureMarkerSymbol(drawable);
        symbol.setOffsetY(drawable.getBounds().height() / 2);
        return symbol;
    }

    public static PictureMarkerSymbol getUserHighLightSymbol(String name) {
        Bitmap bmpMarker = null;
        if (null == name) {
            name = "";
        }

        bmpMarker = getUserMarkerHighLight();
        BitmapDrawable drawable = DrawableUtil.createDrawableForText(bmpMarker, iconSize, hightColor, textSize, name);
        PictureMarkerSymbol symbol = new PictureMarkerSymbol(drawable);
        symbol.setOffsetY(drawable.getBounds().height() / 2);
        return symbol;
    }

    private static Bitmap getUserMarkerOffline() {
        if (null == userMarkerOffline) {
            try {
                userMarkerOffline = BitmapFactory.decodeResource(HostApplication.getApplication().getResources(), R.drawable.patrol_bus_offline);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userMarkerOffline;
    }

    private static Bitmap getUserMarkerOnline() {
        if (null == userMarkerOnline) {
            try {
                userMarkerOnline = BitmapFactory.decodeResource(HostApplication.getApplication().getResources(), R.drawable.patrol_bus_online);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userMarkerOnline;
    }

    private static Bitmap getUserMarkerHighLight() {
        if (null == userMarkerHighlight) {
            try {
                userMarkerHighlight = BitmapFactory.decodeResource(HostApplication.getApplication().getResources(), R.drawable.patrol_bus_highlight);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userMarkerHighlight;
    }
}
