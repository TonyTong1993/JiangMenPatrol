package com.ecity.cswatersupply.model.planningTask;

import java.util.HashMap;

/**
 * 控制图层的显示
 * @author ml
 *
 */
public class LegendControl {
    //Graphics 的uid
    private HashMap<String, Integer> gidMap;
    //是否显示
    private boolean isVisible;
    //图层key 
    private String key;
    public LegendControl(HashMap<String, Integer> gidMap, boolean isVisible, String key) {
        super();
        this.gidMap = gidMap;
        this.isVisible = isVisible;
        this.key = key;
    }
    public HashMap<String, Integer> getGidMap() {
        return gidMap;
    }
    public void setGidMap(HashMap<String, Integer> gidMap) {
        this.gidMap = gidMap;
    }
    public boolean isVisible() {
        return isVisible;
    }
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    } 
}
