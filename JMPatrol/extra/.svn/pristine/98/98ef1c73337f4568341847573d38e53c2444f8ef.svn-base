package com.zzz.ecity.android.applibrary.model;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

import com.ecity.android.db.model.ASQLiteBean;
import com.zzz.ecity.android.applibrary.utils.DateUtilExt;

public class GPSPositionBean extends ASQLiteBean {
    private static final long serialVersionUID = -3531614877723677841L;
    //sqlite中的id
    private int id;
    // x坐标
    private double x;
    // y坐标
    private double y;
    // 经度
    private double lon;
    // 纬度
    private double lat;
    // 精度
    private double acu;
    // 电量
    private String battery;
    // 用户id
    private int userid;
    // 速度
    private double speed;
    //上报时间
    private String time;
    //GPS返回的上报时�?
    private long gpsTime;
    //是否已经上报
    private int status = 0;// 1 上报 0 未上�?

    private String tag;

    private String planTaskId;

    private boolean overspeed;

    private boolean repay;

    private boolean nightWatch;

    private boolean inDetourArea;

    public GPSPositionBean() {
        x = 0;
        y = 0;
        lon = 0;
        lat = 0;
        acu = 0;
        battery = "10%";
        userid = 0;
        speed = 0;
        time = DateUtilExt.getCurrentTime();
        status = 0;
        tag = "";
        planTaskId = "";
        overspeed = false;
        repay = false;
        nightWatch = false;
        inDetourArea = false;
    }
    
	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public double getx() {
        return x;
    }

    public void setx(double x) {
        this.x = x;
    }

    public double gety() {
        return y;
    }

    public void sety(double y) {
        this.y = y;
    }

    public double getlat() {
        return lat;
    }

    public void setlat(double lat) {
        this.lat = lat;
    }

    public double getlon() {
        return lon;
    }

    public void setlon(double lon) {
        this.lon = lon;
    }

    public double getacu() {
        return acu;
    }

    public void setacu(double acu) {
        this.acu = acu;
    }

    public String getbattery() {
        return battery;
    }

    public void setbattery(String battery) {
        this.battery = battery;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * @return 上报时间
     */
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(long gpsTime) {
        this.gpsTime = gpsTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPlanTaskId() {
        return planTaskId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setPlanTaskId(String planTaskId) {
        this.planTaskId = planTaskId;
    }

    public boolean isOverspeed() {
        return overspeed;
    }

    /**
     * 1 true; 0 false
     * @param repay
     */
    public void setOverspeed(int overspeed) {
        if (1 == overspeed) {
            this.overspeed = true;
        } else {
            this.overspeed = false;
        }
    }

    public boolean isRepay() {
        return repay;
    }

    /**
     * 1 true; 0 false
     * @param repay
     */
    public void setRepay(int repay) {
        if (1 == repay) {
            this.repay = true;
        } else {
            this.repay = false;
        }
    }

    public boolean isNightWatch() {
        return nightWatch;
    }

    /**
     * 1 true; 0 false
     * @param repay
     */
    public void setNightWatch(int nightWatch) {
        if (1 == nightWatch) {
            this.nightWatch = true;
        } else {
            this.nightWatch = false;
        }
    }

    public boolean isInDetourArea() {
        return inDetourArea;
    }

    /**
     * 1 true; 0 false
     * @param repay
     */
    public void setInDetourArea(int inDetourArea) {
        if (1 == inDetourArea) {
            this.inDetourArea = true;
        } else {
            this.inDetourArea = false;
        }
        ;
    }

    @Override
    public void buildFromCursor(Cursor cursor) {
        this.id = cursor.getInt(0);//1 实际上为 _rid
        this.x = cursor.getDouble(2);
        this.y = cursor.getDouble(3);
        this.lon = cursor.getDouble(4);
        this.lat = cursor.getDouble(5);
        this.acu = cursor.getDouble(6);
        this.battery = cursor.getString(7);
        this.userid = cursor.getInt(8);
        this.speed = cursor.getDouble(9);
        this.time = cursor.getString(10);
        this.gpsTime = Long.valueOf(cursor.getString(11));
        this.status = cursor.getInt(12);
        this.tag = cursor.getString(13);
        this.planTaskId = cursor.getString(14);
        this.overspeed = cursor.getInt(15) == 1 ? true : false;
        this.repay = cursor.getInt(16) == 1 ? true : false;
        this.nightWatch = cursor.getInt(17) == 1 ? true : false;
        this.inDetourArea = cursor.getInt(18) == 1 ? true : false;
    }

    @Override
    public boolean buildFromJson(JSONObject jsonObj) {
        boolean flag = false;
        if (null == jsonObj) {
            return flag;
        }
        try {
            @SuppressWarnings("unchecked")
            Iterator<String> iterator = jsonObj.keys();
            while (iterator.hasNext()) {
                String key = iterator.next().toString();
                if (key.equalsIgnoreCase("id")) {
                    this.id = Integer.parseInt(jsonObj.getString(key));
                } else if (key.equalsIgnoreCase("x")) {
                    this.x = jsonObj.getDouble(key);
                } else if (key.equalsIgnoreCase("y")) {
                    this.y = jsonObj.getDouble(key);
                } else if (key.equalsIgnoreCase("lon")) {
                    this.lon = jsonObj.getDouble(key);
                } else if (key.equalsIgnoreCase("lat")) {
                    this.lat = jsonObj.getDouble(key);
                } else if (key.equalsIgnoreCase("acu")) {
                    this.acu = jsonObj.getDouble(key);
                } else if (key.equalsIgnoreCase("battery")) {
                    this.battery = jsonObj.getString(key);
                } else if (key.equalsIgnoreCase("userid")) {
                    this.userid = jsonObj.getInt(key);
                } else if (key.equalsIgnoreCase("speed")) {
                    this.speed = jsonObj.getDouble(key);
                } else if (key.equalsIgnoreCase("time")) {
                    this.time = jsonObj.getString(key);
                } else if (key.equalsIgnoreCase("gpsTime")) {
                    this.gpsTime = Long.valueOf(jsonObj.getString(key));
                } else if (key.equalsIgnoreCase("status")) {
                    this.status = jsonObj.getInt(key);
                } else if (key.equalsIgnoreCase("tag")) {
                    this.tag = jsonObj.getString(key);
                } else if (key.equalsIgnoreCase("planTaskId")) {
                    this.planTaskId = jsonObj.getString(key);
                } else if (key.equalsIgnoreCase("overspeed")) {
                    this.overspeed = jsonObj.getInt(key) == 1 ? true : false;
                } else if (key.equalsIgnoreCase("repay")) {
                    this.repay = jsonObj.getInt(key) == 1 ? true : false;
                } else if (key.equalsIgnoreCase("nightWatch")) {
                    this.nightWatch = jsonObj.getInt(key) == 1 ? true : false;
                } else if (key.equalsIgnoreCase("inDetourArea")) {
                    this.inDetourArea = jsonObj.getInt(key) == 1 ? true : false;
                }
            }
            flag = true;
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }

        return flag;
    }

    @Override
    public void copyTo(ASQLiteBean bean) {
        if (bean == null || !(bean instanceof GPSPositionBean)) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public ASQLiteBean createInstance() {
        // TODO Auto-generated method stub
        return new GPSPositionBean();
    }

    @Override
    public ContentValues generateContentValues() {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("id", this.id);
        localContentValues.put("x", this.x);
        localContentValues.put("y", this.y);
        localContentValues.put("lon", this.lon);
        localContentValues.put("lat", this.lat);
        localContentValues.put("acu", this.acu);
        localContentValues.put("battery", (null == this.battery) ? "" : this.battery);
        localContentValues.put("userid", this.userid);
        localContentValues.put("speed", this.speed);
        localContentValues.put("time", (null == this.time) ? "" : this.time);
        localContentValues.put("gpsTime", this.gpsTime);
        localContentValues.put("status", this.status);
        localContentValues.put("tag", (null == this.tag) ? "" : this.tag);
        localContentValues.put("planTaskId", (null == this.planTaskId) ? "" : this.planTaskId);
        localContentValues.put("overspeed", this.overspeed ? 1 : 0);
        localContentValues.put("repay", this.repay ? 1 : 0);
        localContentValues.put("nightWatch", this.nightWatch ? 1 : 0);
        localContentValues.put("inDetourArea", this.inDetourArea ? 1 : 0);

        return localContentValues;
    }

    @Override
    public boolean isInvalid() {
        return true;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(toMap());
        } catch (Exception e) {
            // TODO: handle exception
        }
        return jsonObj;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> jsonMap = new Hashtable<String, Object>();
        jsonMap.put("id", this.id);
        jsonMap.put("x", this.x);
        jsonMap.put("y", this.y);
        jsonMap.put("lon", this.lon);
        jsonMap.put("lat", this.lat);
        jsonMap.put("acu", this.acu);
        jsonMap.put("battery", (null == this.battery) ? "" : this.battery);
        jsonMap.put("userid", this.userid);
        jsonMap.put("speed", this.speed);
        jsonMap.put("time", (null == this.time) ? "" : this.time);
        jsonMap.put("gpsTime", this.gpsTime);
        jsonMap.put("status", this.status);
        jsonMap.put("tag", this.tag);
        jsonMap.put("planTaskId", this.planTaskId);
        jsonMap.put("overspeed", overspeed ? 1 : 0);
        jsonMap.put("repay", repay ? 1 : 0);
        jsonMap.put("nightWatch", nightWatch ? 1 : 0);
        jsonMap.put("inDetourArea", inDetourArea ? 1 : 0);

        return jsonMap;
    }
}
