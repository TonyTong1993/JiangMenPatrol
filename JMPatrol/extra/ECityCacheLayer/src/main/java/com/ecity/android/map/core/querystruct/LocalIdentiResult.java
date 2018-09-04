package com.ecity.android.map.core.querystruct;

import com.esri.core.geometry.Geometry;
import com.esri.core.map.Graphic;

import java.util.Map;

public class LocalIdentiResult extends IdentiResult {

    private Graphic graphic;

    public LocalIdentiResult() {
        this.bLocal = true;
    }

    @Override
    public int getLayerId() {
        return Integer.parseInt(graphic.getAttributes().get("layerId").toString());
    }

    @Override
    public Geometry getGeometry() {
        return graphic.getGeometry();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return graphic.getAttributes();
    }

    public void setGraphic(Graphic graphic) {
        this.graphic = graphic;
    }
}
