package com.ecity.android.map.core.dbquery;

import com.ecity.android.map.core.local.DbQueryParam;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.tasks.ags.identify.IdentifyParameters;

public class DbQueryOrganizer {

    /**
     * 组织查询语句
     * @param param
     * @return
     */
    public static DbQueryParam organzeIdentifyClause(IdentifyParameters param){
        
        DbQueryParam dbQueryParam = new DbQueryParam();
        
        int layerMode = param.getLayerMode();
        
        dbQueryParam.layerMode = layerMode;
        
        if (IdentifyParameters.ALL_LAYERS == layerMode) {
             
        }
        else {
            int[] layers = param.getLayers(); //识别的layer
            if (layers!= null) {
                for (int i = 0; i < layers.length; i++) {
                    dbQueryParam.dnos.add(layers[i]);
                }
            }
            
        }
        
        
        Geometry geometry = param.getGeometry(); //识别的位置
        Envelope envelope = null;
        if (geometry instanceof Envelope) {
             envelope = (Envelope) geometry;
             
        }
        else {
            //暂时先不支持吧，先仅仅支持查询矩形范围。
            System.out.println("not support query unrect yet,query all map");
            envelope = param.getMapExtent();
        }
        dbQueryParam. rect = new ECityRect(envelope);
 
        return dbQueryParam;
    }
    
    
    
    private static String getWhereStringByIds(int[]layers){
 
        
        String string = null;
        
        if (layers != null&& layers.length > 0) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < layers.length; i++) {
                int layer = layers[i];
                if(buffer.length() > 0){
                    buffer .append(",") ;
                }
                buffer.append(layer);
                string = buffer.toString();
            }
        }
        if (string != null) {
            string = "  dno in (" + string + ") ";
        }
        return string;
    }
    
    private static String getRangeString(Envelope envelope){
        if (envelope == null) {
            return null;
        }
        String selectWhere = " xmax >= "+( envelope.getXMin())+
                " and xmin <= "+( envelope.getXMax())+
                " and ymax >= "+( envelope.getYMin())+
                " and ymin <= "+( envelope.getYMax());
        
        
        
        return selectWhere;
    }
}
