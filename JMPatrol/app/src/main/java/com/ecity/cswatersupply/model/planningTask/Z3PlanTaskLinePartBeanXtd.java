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

public class Z3PlanTaskLinePartBeanXtd extends ASQLiteBean {
    private static final long serialVersionUID = -2312805550067927427L;

    private int taskId;
    private int gid;
    private String geomJson;
    private int buffer;
    private String detourJson;
    private float coveredLength;

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
            } else if ("detourJson".equalsIgnoreCase(name)) {
                this.detourJson = cursor.getString(index);
            } else if ("coveredLength".equalsIgnoreCase(name)) {
                this.coveredLength = cursor.getFloat(index);
            } else {
                // no logic
            }
        }
    }

    @Override
    public boolean buildFromJson(JSONObject json) {
        try {
            this.geomJson = json.getJSONObject("geometry").toString();

            JSONObject attributes = json.getJSONObject("attributes");
            Iterator<String> pIterator = attributes.keys();
            while (pIterator.hasNext()) {
                String key = pIterator.next();
                if (key.equalsIgnoreCase("BUFFER")) {
                    this.buffer = attributes.getInt("BUFFER");
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
        return new Z3PlanTaskLinePartBeanXtd();
    }

    @Override
    public ContentValues generateContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("taskId", taskId);
        contentValues.put("gid", gid);
        contentValues.put("geomJson", geomJson);
        contentValues.put("buffer", buffer);
        contentValues.put("detourJson", detourJson);
        contentValues.put("coveredLength", coveredLength);

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
        jsonMap.put("detourJson", detourJson);
        jsonMap.put("coveredLength", coveredLength);

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

    public String getDetourJson() {
        return detourJson;
    }

    public void setDetourJson(String detourJson) {
        this.detourJson = detourJson;
    }

    public float getCoveredLength() {
        return coveredLength;
    }

    public void setCoveredLength(float coveredLength) {
        this.coveredLength = coveredLength;
    }
}
