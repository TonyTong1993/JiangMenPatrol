package com.ecity.cswatersupply.model;

/**
 * @author Derek
 * 空间比较关系
 */
public class SpatialRelationship {
    /**
     * 精确相交查询
     */
    public static String SpatialRelOverlap = "SpatialRelOverlap";

    /**
     * 模糊相交查询
     */
    public static String SpatialRelMbrOverlap = "SpatialRelMbrOverlap";

    /**
     * 包含查询
     */
    public static String SpatialRelContain = "SpatialRelContain";

    public class SpatialSearchType {
        public static final String POINT = "point";
        public static final String RECT = "rect";
        public static final String POLYGON = "polygon";
    }
}
