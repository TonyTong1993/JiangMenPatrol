package com.ecity.cswatersupply.model.planningTask;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

import com.ecity.android.db.model.ASQLiteBean;
import com.ecity.android.log.LogUtil;

public class Z3PlanTaskPointPartBeanXtd extends ASQLiteBean {
    private static final long serialVersionUID = -5357599407657145009L;

    private int taskId;
    private int gid;
    private String geomJson;
    // 缓冲半径
    private int buffer;
    // 是否已经反馈
    private boolean isFeedBack; // 1 YES, 0 NO
    // 是否已经到位
    private boolean isArrived; // 1 YES, 0 NO
    // 到位时间
    private long arriveTime;
    // 反馈时间
    private long feedbackTime;

    @Override
    public void buildFromCursor(Cursor cursor) {
        int index = 0;

        String[] columnNames = cursor.getColumnNames();
        for (String name : columnNames) {
            index = cursor.getColumnIndex(name);

            if ("taskId".equalsIgnoreCase(name)) {
                this.taskId = cursor.getInt(index);
            } else if ("gid".equalsIgnoreCase(name)) {
                this.gid = cursor.getInt(index);
            } else if ("geomJson".equalsIgnoreCase(name)) {
                this.geomJson = cursor.getString(index);
            } else if ("buffer".equalsIgnoreCase(name)) {
                this.buffer = cursor.getInt(index);
            } else if ("isFeedBack".equalsIgnoreCase(name)) {
                this.isFeedBack = (cursor.getInt(index) == 1);
            } else if ("isArrived".equalsIgnoreCase(name)) {
                this.isArrived = (cursor.getInt(index) == 1);
            } else if ("arriveTime".equalsIgnoreCase(name)) {
                this.arriveTime = cursor.getLong(index);
            } else if ("feedbackTime".equalsIgnoreCase(name)) {
                this.feedbackTime = cursor.getLong(index);
            }
        }
    }

    @Override
    public boolean buildFromJson(JSONObject pointJs) {
        try {
            this.geomJson = pointJs.getJSONObject("geometry").toString();
            JSONObject attributes = pointJs.getJSONObject("attributes");
            Iterator<String> iterator = attributes.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (key.equalsIgnoreCase("BUFFER")) {
                    this.buffer = attributes.getInt(key);
                } else if (key.equalsIgnoreCase("GID")) {
                    this.gid = attributes.getInt("GID");
                }
            }
        } catch (JSONException e) {
            LogUtil.e(this, e);
            return false;
        }

        return true;
    }

    @Override
    public void copyTo(ASQLiteBean bean) {
    }

    @Override
    public ASQLiteBean createInstance() {
        return new Z3PlanTaskPointPartBeanXtd();
    }

    @Override
    public ContentValues generateContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("taskId", taskId);
        contentValues.put("gid", gid);
        contentValues.put("geomJson", geomJson);
        contentValues.put("buffer", buffer);
        contentValues.put("isFeedBack", isFeedBack ? 1 : 0);
        contentValues.put("isArrived", isArrived ? 1 : 0);
        contentValues.put("arriveTime", arriveTime);
        contentValues.put("feedbackTime", feedbackTime);

        return contentValues;
    }

    @Override
    public boolean isInvalid() {
        return true;
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject(toMap());
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> jsonMap = new Hashtable<String, Object>();
        jsonMap.put("taskId", taskId);
        jsonMap.put("gid", gid);
        jsonMap.put("geomJson", geomJson);
        jsonMap.put("buffer", buffer);
        jsonMap.put("isFeedBack", isFeedBack ? 1 : 0);
        jsonMap.put("isArrived", isArrived ? 1 : 0);
        jsonMap.put("arriveTime", arriveTime);
        jsonMap.put("feedbackTime", feedbackTime);

        return jsonMap;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getGeomJson() {
        return geomJson;
    }

    public void setGeomJson(String geomJson) {
        this.geomJson = geomJson;
    }

    public int getBuffer() {
        return buffer;
    }

    public void setBuffer(int buffer) {
        this.buffer = buffer;
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

    public long getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(long arriveTime) {
        this.arriveTime = arriveTime;
    }

    public long getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(long feedbackTime) {
        this.feedbackTime = feedbackTime;
    }
}
