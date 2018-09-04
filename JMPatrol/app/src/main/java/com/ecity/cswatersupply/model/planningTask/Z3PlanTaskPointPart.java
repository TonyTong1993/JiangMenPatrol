package com.ecity.cswatersupply.model.planningTask;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.z3app.android.util.StringUtil;

public class Z3PlanTaskPointPart extends Z3PlanTaskPart implements Serializable {
    private static final long serialVersionUID = 5566555059207167816L;

    private int taskid;
    private String tasktype;
    private String groupid;

    private int gid;
    private int tpointid;
    // 集合数据
    private String geom;
    // 缓冲半径
    private int buffer;
    // 根据buffer生成的缓冲区
    private String geoBuffer;
    // 属性获取地址
    private String equiporigin;
    //获取属性的id 
    private String equipid;
    private String etypeid;
    //设备点类型
    private String type;
    // 是否已经反馈
    private boolean isFeedBack;
    // 是否已经到位
    private boolean isArrive;
    // 检查项
//    private String contents;
    // 到位时间
    private String arriveTime;
    // 反馈时间
    private String feedbackTime;
    // 上报到位的时间
    private String arriveUpLoadTime;
    //设备的属性信息
    private String infoDetails;

    public String getInfoDetails() {
        return infoDetails;
    }

    public void setInfoDetails(String infoDetails) {
        this.infoDetails = infoDetails;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public int getTpointid() {
        return tpointid;
    }

    public void setTpointid(int tpointid) {
        this.tpointid = tpointid;
    }

    public String getEtypeid() {
        return etypeid;
    }

    public void setEtypeid(String etypeid) {
        this.etypeid = etypeid;
    }

    public String getEquipid() {
        return equipid;
    }

    public void setEquipid(String equipid) {
        this.equipid = equipid;
    }

    public String getEquiporigin() {
        return equiporigin;
    }

    public void setEquiporigin(String equiporigin) {
        this.equiporigin = equiporigin;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public String getArriveUpLoadTime() {
        return arriveUpLoadTime;
    }

    public void setArriveUpLoadTime(String arriveUpLoadTime) {
        this.arriveUpLoadTime = arriveUpLoadTime;
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

    public boolean isFeedBack() {
        return isFeedBack;
    }

    public void setFeedBack(boolean isFeedBack) {
        this.isFeedBack = isFeedBack;
    }

    public boolean isArrive() {
        return isArrive;
    }

    public void setArrive(boolean isArrive) {
        this.isArrive = isArrive;
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

    @Override
    public String toString() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskid", taskid);
        map.put("tasktype", tasktype);
        map.put("gid", gid);
        map.put("tpointid", tpointid);
        map.put("geom", geom);
        map.put("buffer", buffer);
        map.put("type", type);
        map.put("etypeid", etypeid);
        map.put("geoBuffer", geoBuffer);
        map.put("equiporigin", equiporigin);
        map.put("equipid", equipid);
        map.put("isFeedBack", isFeedBack);
        map.put("isArrive", isArrive);
        map.put("arriveTime", arriveTime);
        map.put("feedbackTime", feedbackTime);
        map.put("arriveUpLoadTime", arriveUpLoadTime);

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
                    }
                    tasktype = json.optString(key);
                    if (key.equalsIgnoreCase("gid")) {
                        gid = json.getInt(key);
                    } else if (key.equalsIgnoreCase("tpointid")) {
                        tpointid = json.getInt(key);
                    }else if(key.equalsIgnoreCase("type")){
                        type = json.getString("type");
                    }else if (key.equalsIgnoreCase("geom")) {
                        geom = json.getString(key);
                    } else if (key.equalsIgnoreCase("buffer")) {
                        buffer = json.getInt(key);
                    } else if (key.equalsIgnoreCase("etypeid")) {
                        etypeid = json.getString(key);
                    } else if (key.equalsIgnoreCase("equiporigin")) {
                        equiporigin = json.getString("equiporigin");
                    } else if (key.equalsIgnoreCase("equipid")) {
                        equipid = json.getString("equipid");
                    } else if (key.equalsIgnoreCase("geoBuffer")) {
                        geoBuffer = json.getString(key);
                    } else if (key.equalsIgnoreCase("isFeedBack")) {
                        isFeedBack = json.getBoolean(key);
                    } else if (key.equalsIgnoreCase("isArrive")) {
                        isArrive = json.getBoolean(key);
                    } else if (key.equalsIgnoreCase("arriveTime")) {
                        arriveTime = json.getString(key);
                    } else if (key.equalsIgnoreCase("feedbackTime")) {
                        feedbackTime = json.getString(key);
                    } else if (key.equalsIgnoreCase("arriveUpLoadTime")) {
                        arriveUpLoadTime = json.getString(key);
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
    
    public boolean isNotEquipment(){
        return StringUtil.isBlank(type);
    }
}