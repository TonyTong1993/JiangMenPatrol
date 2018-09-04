package com.ecity.cswatersupply.model.planningTask;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class Z3PlanTaskLinePart extends Z3PlanTaskPart implements Serializable {
    private static final long serialVersionUID = -5596077886664709013L;

    private int taskid;
    private String groupid;

    private int gid;
    private String tasktype;
    private int tlineid;
    // 集合数据
    private String geom;
    // 缓冲半径
    private int buffer;
    // 根据buffer生成的缓冲区
    private String geoBuffer;
    // 检查项
//    private String contents;
    // 检查项表单id
    private String etypeid;
    // 属性获取地址
    private String equiporigin;
    //获取属性的id 
    private String equipid;
    //设备类型
    private String type;
    // 巡线绕行区
    private String detours;
    // 线的长度
    private double linelen;
    // 走过的长度
    private double coveredLength;
    // 是否覆盖
    private boolean isCovered;
    // 是否已反馈
    private boolean isFeedBack;
    // 到位时间
    private String arriveTime;
    // 反馈时间
    private String feedbackTime;
    // 上报到位的时间
    private String arriveUpLoadTime;

    private int schemeid;

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
    
    public void setDetours(String detours) {
        this.detours = detours;
    }

    public double getCoveredLength() {
        return coveredLength;
    }

    public String getDetours() {
        return detours;
    }

    public void setCoveredLength(double coveredLength) {
        this.coveredLength = coveredLength;
    }

    public int getGid() {
        return gid;
    }
    
    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
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

    public int getBuffer() {
        return buffer;
    }

    public void setBuffer(int buffer) {
        this.buffer = buffer;
    }

    public String getGeoBuffer() {
        return geoBuffer;
    }

    public void setGeoBuffer(String geoBuffer) {
        this.geoBuffer = geoBuffer;
    }

    public boolean isCovered() {
        return isCovered;
    }

    public void setCovered(boolean isCovered) {
        this.isCovered = isCovered;
    }

    public boolean isFeedBack() {
        return isFeedBack;
    }

    public void setFeedBack(boolean isFeedBack) {
        this.isFeedBack = isFeedBack;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(String feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public String getArriveUpLoadTime() {
        return arriveUpLoadTime;
    }

    public void setArriveUpLoadTime(String arriveUpLoadTime) {
        this.arriveUpLoadTime = arriveUpLoadTime;
    }

    public String getEtypeid() {
        return etypeid;
    }

    public void setEtypeid(String etypeid) {
        this.etypeid = etypeid;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public int getTlineid() {
        return tlineid;
    }

    public void setTlineid(int tlineid) {
        this.tlineid = tlineid;
    }

    public String getEquiporigin() {
        return equiporigin;
    }

    public void setEquiporigin(String equiporigin) {
        this.equiporigin = equiporigin;
    }

    public String getEquipid() {
        return equipid;
    }

    public void setEquipid(String equipid) {
        this.equipid = equipid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLinelen() {
        return linelen;
    }

    public void setLinelen(double linelen) {
        this.linelen = linelen;
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
        map.put("taskid", taskid);
        map.put("gid", gid);
        map.put("tasktype", tasktype);
        map.put("tlineid", tlineid);
        map.put("geom", geom);
        map.put("buffer", buffer);
        map.put("geoBuffer", geoBuffer);
        map.put("detours", detours);
        map.put("coveredLength", coveredLength);
        map.put("etypeid", etypeid);
        map.put("equiporigin", equiporigin);
        map.put("equipid", equipid);
        map.put("type", type);
        map.put("isCovered", isCovered);
        map.put("isFeedBack", isFeedBack);
        map.put("arriveTime", arriveTime);
        map.put("feedbackTime", feedbackTime);
        map.put("arriveUpLoadTime", arriveUpLoadTime);
        map.put("linelen", linelen);
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
                    if (key.equalsIgnoreCase("taskid")) {
                        taskid = json.getInt(key);
                    }else if (key.equalsIgnoreCase("gid")) {
                        gid = json.getInt(key);
                    } else if (key.equalsIgnoreCase("tasktype")) {
                        tasktype = json.getString(key);
                    } else if (key.equalsIgnoreCase("tlineid")) {
                        tlineid = json.getInt("tlineid");
                    } else if (key.equalsIgnoreCase("geom")) {
                        geom = json.getString(key);
                    } else if (key.equalsIgnoreCase("buffer")) {
                        buffer = json.getInt(key);
                    } else if (key.equalsIgnoreCase("geoBuffer")) {
                        geoBuffer = json.getString(key);
                    } else if (key.equalsIgnoreCase("etypeid")) {
                        etypeid = json.getString(key);
                    } else if (key.equalsIgnoreCase("equiporigin")) {
                        equiporigin = json.getString(key);
                    } else if (key.equalsIgnoreCase("equipid")) {
                        equipid = json.getString(key);
                    } else if (key.equalsIgnoreCase("type")) {
                        type = json.getString(key);
                    } else if (key.equalsIgnoreCase("detours")) {
                        detours = json.getString(key);
                    } else if (key.equalsIgnoreCase("coveredLength")) {
                        coveredLength = json.getDouble(key);
                    } else if (key.equalsIgnoreCase("isCovered")) {
                        isCovered = json.getBoolean("isCovered");
                    } else if (key.equalsIgnoreCase("isFeedBack")) {
                        isFeedBack = json.getBoolean("isFeedBack");
                    } else if (key.equalsIgnoreCase("arriveTime")) {
                        arriveTime = json.getString(arriveTime);
                    } else if (key.equalsIgnoreCase("feedbackTime")) {
                        feedbackTime = json.getString(feedbackTime);
                    } else if (key.equalsIgnoreCase("arriveUpLoadTime")) {
                        arriveUpLoadTime = json.getString("arriveUpLoadTime");
                    } else if (key.equalsIgnoreCase("linelen")) {
                        linelen = json.getDouble("linelen");
                    } else if (key.equalsIgnoreCase("schemeid")) {
                        schemeid = json.getInt("schemeid");
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
