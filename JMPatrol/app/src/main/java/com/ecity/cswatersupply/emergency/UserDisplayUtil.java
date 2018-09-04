package com.ecity.cswatersupply.emergency;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.DrawableUtil;
import com.esri.core.symbol.PictureMarkerSymbol;

public class UserDisplayUtil {
    private static Bitmap userMarkerOffline;
    private static Bitmap userMarkerOnline;
    private static Bitmap userMarkerHighlight;
    
    public static PictureMarkerSymbol getUserSymbol(String name,boolean isOnline){
        Bitmap bmpMarker = null;
        int iconSize = 64;
        float textSize = 12.f;
        int color = 0;
        
        if(null == name){
            name = "";
        } else {
            if(name.length()>3){
                name = name.substring(0,3);
                name+="*";
            }
        }
        
        if(isOnline){
            bmpMarker = getUserMarkerOnline();
            color = Color.parseColor("ff0000");
        } else {
            bmpMarker = getUserMarkerOffline();
            color = Color.GRAY;
        }

        PictureMarkerSymbol symbol = new PictureMarkerSymbol(DrawableUtil.createDrawable(bmpMarker,iconSize,iconSize,color,textSize,name));
        symbol.setOffsetY(iconSize/2);
        return symbol;
    }

    public static PictureMarkerSymbol getUserHighLightSymbol(String name){
        Bitmap bmpMarker = null;
        int iconSize = 64;
        float textSize = 12.f;
        int color = 0;
        
        if(null == name){
            name = "";
        } else {
            if(name.length()>3){
                name = name.substring(0,3);
                name+="*";
            }
        }
        bmpMarker = getUserMarkerHighLight();
        color = Color.parseColor("#00ff00");
        PictureMarkerSymbol symbol = new PictureMarkerSymbol(DrawableUtil.createDrawable(bmpMarker,iconSize,iconSize,color,textSize,name));
        symbol.setOffsetY(iconSize/2);
        return symbol;
    }

    private static Bitmap getUserMarkerOffline(){
        if(null == userMarkerOffline){
            try {
                userMarkerOffline = BitmapFactory.decodeResource(HostApplication.getApplication().getResources(), R.drawable.icon_marker_gray_x64); 
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return userMarkerOffline;
    }
    
    private static Bitmap getUserMarkerOnline(){
        if(null == userMarkerOnline){
            try {
                userMarkerOnline = BitmapFactory.decodeResource(HostApplication.getApplication().getResources(), R.drawable.icon_marker_red_x64); 
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return userMarkerOnline;
    }

    private static Bitmap getUserMarkerHighLight(){
        if(null == userMarkerHighlight){
            try {
                userMarkerHighlight = BitmapFactory.decodeResource(HostApplication.getApplication().getResources(), R.drawable.icon_marker_green_x64); 
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return userMarkerHighlight;
    }
}
