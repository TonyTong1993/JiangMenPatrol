package com.ecity.cswatersupply.network.request;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
/***
 * 获取巡检员轨迹
 * @author ZiZhengzhuan
 *
 */
public class GetPatrolManTrackParameter implements IRequestParameter {
    private String idlist;
    private String sttime;
    private String endtime;
    private double xmin;
    private double xmax;
    private double ymin;
    private double ymax;
    private DecimalFormat df = new DecimalFormat("0.000000");
    
    public String getIdlist() {
        return idlist;
    }

    public void setIdlist(String idlist) {
        this.idlist = idlist;
    }

    public String getSttime() {
        return sttime;
    }

    public void setSttime(String sttime) {
        this.sttime = sttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }


    public GetPatrolManTrackParameter() {
        
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("idlist", idlist);
        map.put("sttime", sttime);
        map.put("endtime", endtime);
        map.put("xmin", df.format(xmin));
        map.put("xmax", df.format(xmax));
        map.put("ymin", df.format(ymin));
        map.put("ymax", df.format(ymax));
        map.put("f", "json");
        return map;
    }
}
