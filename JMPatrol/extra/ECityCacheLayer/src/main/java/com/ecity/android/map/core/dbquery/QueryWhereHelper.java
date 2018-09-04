package com.ecity.android.map.core.dbquery;

public class QueryWhereHelper {

    public static String getNodeRectString(ECityRect rct){
        if (rct == null) {
            return null;
        }
        String selectWhere = " x >= "+( rct.xMin)+
                " and x <= "+( rct.xMax)+
                " and y >= "+( rct.yMin)+
                " and y <= "+( rct.yMax);
        
        
        
        return selectWhere;
        
    }
    
    public static String getLineRectString(ECityRect rct){
        if (rct == null) {
            return null;
        }
        String selectWhere = " xmin <= "+( rct.xMax)+
                " and xmax >= "+( rct.xMin)+
                " and ymin <= "+( rct.yMax)+
                " and ymax >= "+( rct.yMin);
        
        
        
        return selectWhere;
    }
}
