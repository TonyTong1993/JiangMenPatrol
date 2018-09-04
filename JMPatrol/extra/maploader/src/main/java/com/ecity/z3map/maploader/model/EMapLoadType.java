package com.ecity.z3map.maploader.model;

/**
 * Created by zhengzhuanzi on 2017/6/16.
 */

public enum EMapLoadType {
    /**
     * 正常
     */
    NORMAL("NORMAL"),
    /**
     * 比例尺
     */
    SCALE("SCALE"),
    /***
     * envelope 区域
     */
    ENVELOPE("ENVELOPE"),
    /**
     * 比例尺和范围都考虑
     */
    BOTH("BOTH");

    String value;
    EMapLoadType(String v) {
        if( null != v ) {
            value = v.toUpperCase();
        }
    }

    String value() {
        return value;
    }
}
