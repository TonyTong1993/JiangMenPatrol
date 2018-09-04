package com.ecity.z3map.maploader.model;

/**
 * Created by zhengzhuanzi on 2017/6/15.
 */

public enum ESourceType {
    /**
     * ArcGIS 本地瓦片数据，即标准的TPK文件
     */
    ARCGISLOCALTILEDLAYER("ARCGISLOCALTILEDLAYER"),
    /**
     * arcgis 标准在线瓦片服务
     */
    ARCGISTILEDMAPSERVICELAYER("ARCGISTILEDMAPSERVICELAYER"),
    /***
     * arcgis 在线要素服务
     */
    ARCGISFEATURELAYER("ARCGISFEATURELAYER"),
    /**
     * arcgis 在线矢量服务
     */
    ARCGISDYNAMICMAPSERVICELAYER("ARCGISDYNAMICMAPSERVICELAYER"),
    /***
     * arcgis 影响图片服务
     */
    ARCGISIMAGESERVICELAYER("ARCGISIMAGESERVICELAYER"),
    /***
     * 支持缓存的标准在线瓦片服务
     */
    ECITYTILEDMAPSERVICELAYER("ECITYTILEDMAPSERVICELAYER"),
    /***
     * 标准wmts格式的服务
     */
    WMTSLAYER("WMTSLAYER"),
    /***
     * 本地DB格式的图层
     */
    DBLAYER("DBLAYER");

    String value;
    ESourceType(String v) {
        if( null != v ) {
            this.value = v.toUpperCase();
        }
    }

    public String value() {
        return value;
    }
}
