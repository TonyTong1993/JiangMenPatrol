package com.ecity.cswatersupply.ui.widght;

import android.app.Activity;

import com.ecity.cswatersupply.utils.GISMarkerUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.MarkerSymbol;
import com.z3app.android.util.StringUtil;

/**
 * A class to show an image on map, with a text below the image.
 * 
 * @author Ma Qianli
 *
 */
public class MapPointMarker {
    private Activity activity;
    private Point mapPoint;
    private int drawableId;
    private String markerText;
    private int textColorResourceId;

    /**
     * @param activity
     * @param mapPoint The mapPoint to add marker.
     * @param drawableId drawable resource id. -1 if no need to add image
     * @param markerText text of marker. Blank string means no image to add.
     * @param textColorResourceId
     */
    public MapPointMarker(Activity activity, Point mapPoint, int drawableId, String markerText, int textColorResourceId) {
        this.activity = activity;
        this.mapPoint = mapPoint;
        this.drawableId = drawableId;
        this.markerText = markerText;
        this.textColorResourceId = textColorResourceId;
    }

    /**
     * @param activity
     * @param mapPoint The mapPoint to add marker.
     * @param drawableId drawable resource id. -1 if no need to add image
     * @param textResourceId text resource id of marker. Blank string means no image to add.
     * @param textColorResourceId
     */
    public MapPointMarker(Activity activity, Point mapPoint, int drawableId, int textResourceId, int colorResourceId) {
        this.activity = activity;
        this.mapPoint = mapPoint;
        this.drawableId = drawableId;
        this.textColorResourceId = colorResourceId;
        if (textResourceId != -1) {
            this.markerText = activity.getString(textResourceId);
        }
    }

    /**
     * Add MapPointMarker in the given layer.
     * @param layer the layer to add marker on
     * @return int[] { imgMakerId, textMakerId }. Default value of array elements is -1 if no corresponding marker is added.
     */
    public int[] addOnLayer(GraphicsLayer layer) {
        int imgMakerId = addImageMarker(layer);
        int textMakerId = addTextMarker(layer);

        return new int[] { imgMakerId, textMakerId };
    }

    private int addImageMarker(GraphicsLayer layer) {
        if (drawableId == -1) {
            return -1;
        }

        MarkerSymbol symbol = GISMarkerUtil.getLocationImageMarker(activity, drawableId);
        Graphic graphic = new Graphic(mapPoint, symbol);

        return layer.addGraphic(graphic);
    }

    private int addTextMarker(GraphicsLayer layer) {
        if (StringUtil.isBlank(markerText)) {
            return -1;
        }

        MarkerSymbol symbol = GISMarkerUtil.getTextMarker(activity, markerText, 0, textColorResourceId, drawableId);
        Graphic graphic = new Graphic(mapPoint, symbol);

        return layer.addGraphic(graphic);
    }
}
