package com.ecity.android.map.core.local;

import com.ecity.android.map.core.dbquery.ECityRect;
import com.esri.core.tasks.ags.identify.IdentifyParameters;

import java.util.ArrayList;
import java.util.List;

public class DbQueryParam {

    /**
     * 查询的dno
     */
    public List<Integer> dnos;

    public List<String> layerName;

    public String whereClause;

    public int layerMode;

    public ECityRect rect;

    public DbQueryParam() {
        layerMode = IdentifyParameters.ALL_LAYERS;
        dnos = new ArrayList<Integer>();
        layerName = new ArrayList<String>();
    }

}
