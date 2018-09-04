package com.ecity.cswatersupply.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.TextSymbol;
import com.z3app.android.util.StringUtil;

/**
 * Add markers on map.
 * @author Ma Qianli
 *
 */
public class GISMarkerUtil {

    public static PictureMarkerSymbol getLocationImageMarker(Context context, int drawableId) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        //        int w = drawable.getIntrinsicWidth() / 2;
        int h = drawable.getIntrinsicHeight() / 2;

        PictureMarkerSymbol marker = new PictureMarkerSymbol(drawable);

        marker.setOffsetY(h);
        //        marker.setOffsetX(w);
        marker.setAngle(0);

        return marker;
    }

    /**
     * @param context
     * @param text
     * @param fontSize default to 15 if <= 0
     * @return
     */
    public static TextSymbol getTextMarker(Context context, String text, int fontSize, int fontColor) {
        if (StringUtil.isBlank(text)) {
            text = "";
        }

        if (fontSize <= 0) {
            fontSize = 15;
        }

        TextSymbol marker = new TextSymbol(fontSize, text, fontColor);

        return marker;
    }

    /**
     * Get a {@link TextSymbol} to show around the drawable of which the id is companionDrawableId.
     * @param context
     * @param text text of the symbol. Suggested length is 4 characters. Suitable place is not supported for a long text.
     * @param fontSize
     * @param companionDrawableId This TextSymbol is show around the drawable, at a suitable place with an offset value basing on the drawable's size. 
     * @return
     */
    public static TextSymbol getTextMarker(Context context, String text, int fontSize, int fontColor, int companionDrawableId) {
        if (StringUtil.isBlank(text)) {
            text = "";
        }

        TextSymbol marker = getTextMarker(context, text, fontSize, fontColor);

        Drawable drawable = context.getResources().getDrawable(companionDrawableId);
        float yOffset = -(drawable.getIntrinsicHeight() / 2);
        float xOffset = 0;
        if (text.length() >= 15) {
            xOffset = -4 * marker.getWidth(); // Adjust horizontal offset.
        } else if (text.length() >= 4) {
            xOffset = -2 * marker.getWidth(); // Adjust horizontal offset.
        }
        marker.setOffsetY(yOffset);
        marker.setOffsetX(xOffset);

        return marker;
    }
}