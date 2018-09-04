package com.ecity.cswatersupply.model.checkitem;

import java.io.Serializable;

public class PatrolPosition implements Serializable {
    private static final long serialVersionUID = 4257362740562620576L;

    public PatrolPosition() {
        super();
    }

    public PatrolPosition(boolean isLatLon, double x, double y, String placeName) {
        this.isLatLon = isLatLon;
        this.x = x;
        this.y = y;
        this.placeName = placeName;
    }

    public boolean isLatLon = false;
    public double x = 0.0;// may be a latitude
    public double y = 0.0;// may be a longitude
    public String placeName = "";
}
