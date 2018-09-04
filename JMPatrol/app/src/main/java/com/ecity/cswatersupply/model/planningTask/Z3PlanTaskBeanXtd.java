package com.ecity.cswatersupply.model.planningTask;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

import com.ecity.android.db.model.ASQLiteBean;
import com.ecity.cswatersupply.adapter.planningtask.PlanTasksAdapter;

public class Z3PlanTaskBeanXtd extends ASQLiteBean {
    private static final long serialVersionUID = 1779157519576694512L;
    // 1 任务id
    private int taskid;
    // 2 计划id
    private int planid;
    // 3 计划名称
    private String planname;
    // 4 计划开始时间
    private String planstart;
    // 5 计划结束时间
    private String planend;
    // 6 任务发布时间
    private String assigntime;
    // 7 任务开始时间
    private String taskstart;
    // 8 任务技术时间
    private String taskend;
    // 9 任务发布的人名
    private String assignername;
    // 10任务执行者
    private String patrolername;
    // 11任务的总长度
    private double linelen;
    // 12任务包括的巡查点
    private int pointcount;
    // 13 计划任务的 巡查周期 例如：按天巡查
    private String plancycle;
    // 14任务的巡检速度
    private int speed;
    // 15 任务类型
    private String tasktype;
    // 16工作开始时间
    private String workstart;
    // 17工作结束时间
    private String workend;
    // 18点类型的任务 jsonString
    private String pointPartsJsonStr;
    // 19线类型的任务 jsonString
    private String linePartsJsonStr;
    // 20区类型的任务 jsonString
    private String polygonPartsJsonStr;
    // 巡查人的用户id
    private int patrolerId;

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public int getPlanid() {
        return planid;
    }

    public void setPlanid(int planid) {
        this.planid = planid;
    }

    public String getPlanname() {
        return planname;
    }

    public void setPlanname(String planname) {
        this.planname = planname;
    }

    public String getPlanstart() {
        return planstart;
    }

    public void setPlanstart(String planstart) {
        this.planstart = planstart;
    }

    public String getPlanend() {
        return planend;
    }

    public void setPlanend(String planend) {
        this.planend = planend;
    }

    public String getAssigntime() {
        return assigntime;
    }

    public void setAssigntime(String assigntime) {
        this.assigntime = assigntime;
    }

    public String getTaskstart() {
        return taskstart;
    }

    public void setTaskstart(String taskstart) {
        this.taskstart = taskstart;
    }

    public String getTaskend() {
        return taskend;
    }

    public void setTaskend(String taskend) {
        this.taskend = taskend;
    }

    public String getAssignername() {
        return assignername;
    }

    public void setAssignername(String assignername) {
        this.assignername = assignername;
    }

    public String getPatrolername() {
        return patrolername;
    }

    public void setPatrolername(String patrolername) {
        this.patrolername = patrolername;
    }

    public double getLinelen() {
        return linelen;
    }

    public void setLinelen(double linelen) {
        this.linelen = linelen;
    }

    public int getPointcount() {
        return pointcount;
    }

    public void setPointcount(int pointcount) {
        this.pointcount = pointcount;
    }

    public String getPlancycle() {
        return plancycle;
    }

    public void setPlancycle(String plancycle) {
        this.plancycle = plancycle;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public String getWorkstart() {
        return workstart;
    }

    public void setWorkstart(String workstart) {
        this.workstart = workstart;
    }

    public String getWorkend() {
        return workend;
    }

    public void setWorkend(String workend) {
        this.workend = workend;
    }

    public String getPointPartsJsonStr() {
        return pointPartsJsonStr;
    }

    public void setPointPartsJsonStr(String pointPartsJsonStr) {
        this.pointPartsJsonStr = pointPartsJsonStr;
    }

    public String getLinePartsJsonStr() {
        return linePartsJsonStr;
    }

    public void setLinePartsJsonStr(String linePartsJsonStr) {
        this.linePartsJsonStr = linePartsJsonStr;
    }

    public String getPolygonPartsJsonStr() {
        return polygonPartsJsonStr;
    }

    public void setPolygonPartsJsonStr(String polygonPartsJsonStr) {
        this.polygonPartsJsonStr = polygonPartsJsonStr;
    }

    public int getPatrolerId() {
        return patrolerId;
    }

    public void setPatrolerId(int patrolerId) {
        this.patrolerId = patrolerId;
    }

    @Override
    public void buildFromCursor(Cursor cursor) {
        String[] columnNames = cursor.getColumnNames();
        if (null == columnNames || columnNames.length < 1)
            return;

        int index = 0;
        for (String columnName : columnNames) {
            index = cursor.getColumnIndex(columnName);
            if (columnName.equalsIgnoreCase("taskid")) {
                taskid = cursor.getInt(index);
            } else if (columnName.equalsIgnoreCase("planid")) {
                planid = cursor.getInt(index);
            } else if (columnName.equalsIgnoreCase("planname")) {
                planname = cursor.getString(index);
            } else if (columnName.equalsIgnoreCase("planstart")) {
                planstart = cursor.getString(index);
            } else if (columnName.equalsIgnoreCase("planend")) {
                planend = cursor.getString(index);
            } else if (columnName.equalsIgnoreCase("assigntime")) {
                assigntime = cursor.getString(index);
            } else if (columnName.equalsIgnoreCase("taskstart")) {
                taskstart = cursor.getString(index);
            } else if (columnName.equalsIgnoreCase("taskend")) {
                taskend = cursor.getString(index);
            } else if (columnName.equalsIgnoreCase("assignername")) {
                assignername = cursor.getString(index);
            } else if (columnName.equalsIgnoreCase("patrolername")) {
                patrolername = cursor.getString(index);
            } else if (columnName.equalsIgnoreCase("linelen")) {
                linelen = cursor.getDouble(index);
            } else if (columnName.equalsIgnoreCase("pointcount")) {
                pointcount = cursor.getInt(index);
            } else if (columnName.equalsIgnoreCase("plancycle")) {
                plancycle = cursor.getString(index);
            } else if (columnName.equalsIgnoreCase("speed")) {
                speed = cursor.getInt(index);
            } else if (columnName.equalsIgnoreCase("tasktype")) {
                tasktype = cursor.getString(index);
            } else if (columnName.equalsIgnoreCase("workstart")) {
                workstart = cursor.getString(index);
            } else if (columnName.equalsIgnoreCase("workend")) {
                workend = cursor.getString(index);
            } else if (columnName.equalsIgnoreCase("pointPartsJsonStr")) {
                pointPartsJsonStr = cursor.getString(index);
            } else if (columnName.equalsIgnoreCase("linePartsJsonStr")) {
                linePartsJsonStr = cursor.getString(index);
            } else if (columnName.equalsIgnoreCase("polygonPartsJsonStr")) {
                polygonPartsJsonStr = cursor.getString(index);
            } else if (columnName.equalsIgnoreCase("patrolerId")) {
                patrolerId = cursor.getInt(index);
            }
        }
    }

    @Override
    public boolean buildFromJson(JSONObject js) {
        if (null == js)
            return false;
        boolean flag = false;
        try {
            Iterator<String> iterator = js.keys();
            while (iterator.hasNext()) {
                String key = iterator.next().toString();
                if (key.equalsIgnoreCase("taskid")) {
                    taskid = js.getInt(key);
                } else if (key.equalsIgnoreCase("planid")) {
                    planid = js.getInt(key);
                } else if (key.equalsIgnoreCase("planname")) {
                    planname = js.getString(key);
                } else if (key.equalsIgnoreCase("planstart")) {
                    planstart = js.getString(key);
                } else if (key.equalsIgnoreCase("planend")) {
                    planend = js.getString(key);
                } else if (key.equalsIgnoreCase("assigntime")) {
                    assigntime = js.getString(key);
                } else if (key.equalsIgnoreCase("taskstart")) {
                    taskstart = js.getString(key);
                } else if (key.equalsIgnoreCase("taskend")) {
                    taskend = js.getString(key);
                } else if (key.equalsIgnoreCase("assignername")) {
                    assignername = js.getString(key);
                } else if (key.equalsIgnoreCase("patrolername")) {
                    patrolername = js.getString(key);
                } else if (key.equalsIgnoreCase("linelen")) {
                    linelen = js.getDouble(key);
                } else if (key.equalsIgnoreCase("pointcount")) {
                    pointcount = js.getInt(key);
                } else if (key.equalsIgnoreCase("plancycle")) {
                    plancycle = js.getString(key);
                } else if (key.equalsIgnoreCase("speed")) {
                    speed = js.getInt(key);
                } else if (key.equalsIgnoreCase("tasktype")) {
                    tasktype = js.getString(key);
                } else if (key.equalsIgnoreCase("workstart")) {
                    workstart = js.getString(key);
                } else if (key.equalsIgnoreCase("workend")) {
                    workend = js.getString(key);
                } else if (key.equalsIgnoreCase("points")) {
                    pointPartsJsonStr = js.getJSONArray(key).toString();
                } else if (key.equalsIgnoreCase("lines")) {
                    linePartsJsonStr = js.getJSONArray(key).toString();
                } else if (key.equalsIgnoreCase("areas")) {
                    polygonPartsJsonStr = js.getJSONArray(key).toString();
                } else if (key.equalsIgnoreCase("patrolerId")) {
                    patrolerId = js.getInt(key);
                }
            }
            flag = true;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public void copyTo(ASQLiteBean arg0) {
        if (arg0 == null || !(arg0 instanceof Z3PlanTaskBeanXtd)) {
            throw new IllegalArgumentException();
        }
        ((Z3PlanTaskBeanXtd) arg0).setPlanid(planid);
        ((Z3PlanTaskBeanXtd) arg0).setTaskid(taskid);
        ((Z3PlanTaskBeanXtd) arg0).setPlanname(planname);
        ((Z3PlanTaskBeanXtd) arg0).setPlanstart(planstart);
        ((Z3PlanTaskBeanXtd) arg0).setPlanend(planend);
        ((Z3PlanTaskBeanXtd) arg0).setAssigntime(assigntime);
        ((Z3PlanTaskBeanXtd) arg0).setTaskstart(taskstart);
        ((Z3PlanTaskBeanXtd) arg0).setTaskend(taskend);
        ((Z3PlanTaskBeanXtd) arg0).setAssignername(assignername);
        ((Z3PlanTaskBeanXtd) arg0).setPatrolername(patrolername);
        ((Z3PlanTaskBeanXtd) arg0).setLinelen(linelen);
        ((Z3PlanTaskBeanXtd) arg0).setPointcount(pointcount);
        ((Z3PlanTaskBeanXtd) arg0).setPlancycle(plancycle);
        ((Z3PlanTaskBeanXtd) arg0).setSpeed(speed);
        ((Z3PlanTaskBeanXtd) arg0).setTasktype(tasktype);
        ((Z3PlanTaskBeanXtd) arg0).setWorkstart(workstart);
        ((Z3PlanTaskBeanXtd) arg0).setWorkend(workend);
        ((Z3PlanTaskBeanXtd) arg0).setPointPartsJsonStr(pointPartsJsonStr);
        ((Z3PlanTaskBeanXtd) arg0).setLinePartsJsonStr(linePartsJsonStr);
        ((Z3PlanTaskBeanXtd) arg0).setPolygonPartsJsonStr(polygonPartsJsonStr);
        ((Z3PlanTaskBeanXtd) arg0).setPatrolerId(patrolerId);
    }

    @Override
    public ASQLiteBean createInstance() {
        return new Z3PlanTaskBeanXtd();
    }

    @Override
    public ContentValues generateContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("planid", this.planid);
        contentValues.put("taskid", this.taskid);
        contentValues.put("planname", (null == this.planname) ? "" : this.planname);
        contentValues.put("planstart", (null == this.planstart) ? "" : this.planstart);
        contentValues.put("planend", (null == this.planend) ? "" : this.planend);
        contentValues.put("assigntime", (null == this.assigntime) ? "" : this.assigntime);
        contentValues.put("taskstart", (null == this.taskstart) ? "" : this.taskstart);
        contentValues.put("taskend", (null == this.taskend) ? "" : this.taskend);
        contentValues.put("assignername", (null == this.assignername) ? "" : this.assignername);
        contentValues.put("patrolername", (null == this.patrolername) ? "" : this.patrolername);
        contentValues.put("linelen", (0.0 == this.linelen) ? 0.0 : this.linelen);
        contentValues.put("pointcount", (0 == this.pointcount) ? 0 : this.pointcount);
        contentValues.put("plancycle", (null == this.plancycle) ? "" : this.plancycle);
        contentValues.put("speed", (0 == this.speed) ? 0 : this.speed);
        contentValues.put("tasktype", (null == this.tasktype) ? "" : this.tasktype);
        contentValues.put("workstart", (null == this.workstart) ? "" : this.workstart);
        contentValues.put("workend", (null == this.workend) ? "" : this.workend);
        contentValues.put("pointPartsJsonStr", (null == this.pointPartsJsonStr) ? "[]" : this.pointPartsJsonStr);
        contentValues.put("linePartsJsonStr", (null == this.linePartsJsonStr) ? "[]" : this.linePartsJsonStr);
        contentValues.put("polygonPartsJsonStr", (null == this.polygonPartsJsonStr) ? "[]" : this.polygonPartsJsonStr);
        contentValues.put("patrolerId", this.patrolerId);

        return contentValues;
    }

    @Override
    public boolean isInvalid() {
        return false;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(toMap());
        } catch (Exception e) {
        }
        return jsonObj;
    }

    @Override
    public Map toMap() {
        Map<String, Object> jsonMap = new Hashtable<String, Object>();
        jsonMap.put("planid", this.planid);
        jsonMap.put("taskid", this.taskid);
        jsonMap.put("planname", (null == this.planname) ? "" : this.planname);
        jsonMap.put("planstart", (null == this.planstart) ? "" : this.planstart);
        jsonMap.put("planend", (null == this.planend) ? "" : this.planend);
        jsonMap.put("assigntime", (null == this.assigntime) ? "" : this.assigntime);
        jsonMap.put("taskstart", (null == this.taskstart) ? "" : this.taskstart);
        jsonMap.put("assignername", (null == this.assignername) ? "" : this.assignername);
        jsonMap.put("patrolername", (null == this.patrolername) ? "" : this.patrolername);
        jsonMap.put("linelen", (0.0 == this.linelen) ? 0.0 : this.linelen);
        jsonMap.put("pointcount", (0 == this.pointcount) ? 0 : this.pointcount);
        jsonMap.put("plancycle", (null == this.plancycle) ? "" : this.plancycle);
        jsonMap.put("speed", (0 == this.speed) ? 0 : this.speed);
        jsonMap.put("tasktype", (null == this.tasktype) ? "" : this.tasktype);
        jsonMap.put("workstart", (null == this.workstart) ? "" : this.workstart);
        jsonMap.put("workend", (null == this.workend) ? "" : this.workend);
        jsonMap.put("points", getGeoJsonArray(this.pointPartsJsonStr));
        jsonMap.put("lines", getGeoJsonArray(this.linePartsJsonStr));
        jsonMap.put("areas", getGeoJsonArray(this.polygonPartsJsonStr));
        jsonMap.put("patrolerId", this.patrolerId);

        return jsonMap;
    }

    public JSONArray getGeoJsonArray(String geoStr) {
        if ("[]".equalsIgnoreCase(geoStr)) {
            return new JSONArray();
        } else {
            try {
                return new JSONArray(geoStr);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return new JSONArray();
    }

    @Override
    public String toString() {
        String result = "";
        try {
            result = toJson().toString();
        } catch (Exception e) {
            result = super.toString();
        }
        return result;
    }

    // 依据Z3PlanTaskBeanXtd 生成 Z3PlanTask对象
    public Z3PlanTask getZ3PlanTask() {
        Z3PlanTask plantask = PlanTasksAdapter.getInstance().adaptePlanTask(toString());
        return plantask;
    }
}
