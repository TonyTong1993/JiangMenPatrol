package com.ecity.cswatersupply.model.planningTask;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class Z3PlanTaskPolygonPart extends Z3PlanTaskPart implements Serializable {
    private static final long serialVersionUID = -1663972577013430600L;

    private int gid;
    // 集合数据
    private String geom;
    // 是否已经反馈
    private boolean isFeedBack;
    // 是否已经到位
    private boolean isArrived;
    // 检查项
    private String contents;

    private int schemeid;

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public boolean isFeedBack() {
        return isFeedBack;
    }

    public void setFeedBack(boolean isFeedBack) {
        this.isFeedBack = isFeedBack;
    }

    public boolean isArrived() {
        return isArrived;
    }

    public void setArrived(boolean isArrived) {
        this.isArrived = isArrived;
    }

    public int getSchemeid() {
        return schemeid;
    }

    public void setSchemeid(int schemeid) {
        this.schemeid = schemeid;
    }

    @Override
    public String toString() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("gid", gid);
        map.put("geom", geom);
        map.put("isFeedBack", isFeedBack);
        map.put("isArrived", isArrived);
        map.put("contents", contents);
        map.put("schemeid", schemeid);

        JSONObject jsonObject = new JSONObject(map);
        return jsonObject.toString();
    }

    public boolean buildFromJson(String jsonString) {
        if (null == jsonString || jsonString.isEmpty())
            return false;
        boolean flg = false;
        try {
            JSONObject json = new JSONObject(jsonString);

            try {
                @SuppressWarnings("unchecked")
                Iterator<String> iterator = json.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    if (key.equalsIgnoreCase("gid")) {
                        gid = json.getInt(key);
                    } else if (key.equalsIgnoreCase("geom")) {
                        geom = json.getString(key);
                    } else if (key.equalsIgnoreCase("isFeedBack")) {
                        isFeedBack = json.getBoolean(key);
                    } else if (key.equalsIgnoreCase("isArrived")) {
                        isArrived = json.getBoolean(key);
                    } else if (key.equalsIgnoreCase("contents")) {
                        contents = json.getString(key);
                    } else if (key.equalsIgnoreCase("schemeid")) {
                        schemeid = json.getInt(key);
                    }
                }
                flg = true;
            } catch (Exception e) {
                flg = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            flg = false;
        }
        return flg;
    }
}
