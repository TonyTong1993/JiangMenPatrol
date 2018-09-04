package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class QueryNetNodeParameter implements IRequestParameter {
    private String where;
    private boolean returnIdsOnly;
    private boolean returnCountOnly;
    private boolean returnGeometry;

    //    token:
    //        objectIds:
    //        where:[工程编号] like '2009398%'
    //        geometryType:esriGeometryPoint
    //        geometry:
    //        inSR:
    //        spatialRel:esriSpatialRelIntersects
    //        returnIdsOnly:false
    //        returnCountOnly:false
    //        returnGeometry:true
    //        outSR:
    //        outFields:*
    //        f:html
    public QueryNetNodeParameter() {
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("f", "json");
        map.put("where", where);
        map.put("spatialRel", "esriSpatialRelIntersects");
        map.put("returnIdsOnly", String.valueOf(returnIdsOnly));
        map.put("returnCountOnly", String.valueOf(returnCountOnly));
        map.put("returnGeometry", String.valueOf(returnGeometry));
        map.put("returnGeometry", String.valueOf(returnGeometry));
        map.put("pageno", "1");
        map.put("pagesize", "20");

        return map;
    }

    /**
     * 设置查询使用的过滤条件。要用Base64编码。
     * @param where
     */
    public void setWhere(String where) {
        this.where = where;
    }

    public void setReturnIdsOnly(boolean returnIdsOnly) {
        this.returnIdsOnly = returnIdsOnly;
    }

    public void setReturnCountOnly(boolean returnCountOnly) {
        this.returnCountOnly = returnCountOnly;
    }

    public void setReturnGeometry(boolean returnGeometry) {
        this.returnGeometry = returnGeometry;
    }
}
