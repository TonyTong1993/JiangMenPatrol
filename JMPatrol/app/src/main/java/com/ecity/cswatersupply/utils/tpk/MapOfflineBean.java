package com.ecity.cswatersupply.utils.tpk;

public class MapOfflineBean {
    private String mapTpkName;
    private float progress;
    private String tpkUrl;

    public String getTpkUrl() {
        return tpkUrl;
    }

    public void setTpkUrl(String tpkUrl) {
        this.tpkUrl = tpkUrl;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    private String currentSize;

    public String getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(String currentSize) {
        this.currentSize = currentSize;
    }

    public String getMapTpkName() {
        return mapTpkName;
    }

    public void setMapTpkName(String mapTpkName) {
        this.mapTpkName = mapTpkName;
    }
}
