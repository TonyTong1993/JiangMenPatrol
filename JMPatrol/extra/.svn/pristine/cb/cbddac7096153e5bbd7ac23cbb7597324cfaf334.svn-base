package com.ecity.z3map.maploader.model;

import java.io.Serializable;

/**
 * Created by zhengzhuanzi on 2017/6/15.
 */

public class SourceConfig  implements Serializable {
    public SourceConfig(){
        dispRect = new ECityRect(0,0,0,0);
        dispMinScale = 5000000;
        dispMaxScale = 5;
    }

    public String name;
    /**
     * tpkUrl
     */
    public String tpkUrl;

    /**
     * layerUrl
     */
    public String URL;

    /**
     * 手机本地源路径
     */
    public String sourceLocalPath;

    /***
     * 源类型，矢量，瓦片，google，或者其他
     */
    public ESourceType sourceType;

    /***
     * 是否可见
     */
    public String isVisible;

    /***
     * 源ID
     */
    public String sourceID;

    /***
     * 服务名称，一个源对应一个服务
     */
    public String serverName;

    /**
     * 源描述
     */
    public String description;

    /***
     * 显示最大比例尺，当比例尺大于这个值时不显示
     */
    public double dispMaxScale;
    /***
     * 显示最小比例尺，当比例尺小于这个值时不显示
     */
    public double dispMinScale;
    /***
     * 显示范围
     */
    public ECityRect dispRect;

    @Override
    public boolean equals(Object o) {
        if(null != o  && o instanceof  SourceConfig) {
            SourceConfig src = (SourceConfig)o;

            if(! isStringEquals( src.name,this.name)) {
                return false;
            }
            if(! isStringEquals( src.tpkUrl,this.tpkUrl)) {
                return false;
            }
            if(! isStringEquals( src.URL,this.URL)) {
                return false;
            }
            if(! isStringEquals( src.sourceLocalPath,this.sourceLocalPath)) {
                return false;
            }

            return true;
        }

        return false;
    }

    private boolean isStringEquals(String str1,String str2){
        if(null == str1 ) {
           return null == str2;
        }

        if(null == str2 ) {
            return false;
        }

        return str1.equalsIgnoreCase(str2);
    }
}
