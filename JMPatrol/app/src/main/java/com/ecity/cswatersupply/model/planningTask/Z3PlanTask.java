package com.ecity.cswatersupply.model.planningTask;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.ecity.cswatersupply.model.AModel;
import com.esri.core.geometry.Geometry;

public class Z3PlanTask extends AModel implements Serializable {

    private static final long serialVersionUID = -6014631151856554386L;
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
    //已到位点数量
    private int arrviedPointCount;
    //已反馈点数量
    private int feedbackedPointCount;
    //已到位的线长度
    private double arrivedLinelen;
    //已到位的线百分率
    private String rate;
    // 13 计划任务的 巡查周期 例如：按天巡查
    private String plancycle;
    // 14任务的巡检速度
    private int speed;
    // 15 任务类型
    private String tasktype;
    // 16工作开始时间 (未确定开始时间和结束时间的范围是否包含多天还是只是一天的工作开始结束时间)
    private String workstart;
    // 17工作结束时间 (未确定开始时间和结束时间的范围是否包含多天还是只是一天的工作开始结束时间)
    private String workend;
    // 巡查人的用户id
    private int patrolerId;
    //区分任务的类型  是否需要上传检查项
    private int isContents;
    //区分施工场地巡检与线巡检  tasksubtype为“施工场地”为施工场地巡检
    private String tasksubtype;
    //事件上报个数
    private int eventnum;
    //任务在一段时间内走过的线路
    private Geometry trackLinegeom;
    //检查项组id
    private String groupid;
    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
    
    public Geometry getTrackLinegeom() {
        return trackLinegeom;
    }
    public void setTrackLinegeom(Geometry trackLinegeom) {
        this.trackLinegeom = trackLinegeom;
    }
    public int getIsContents() {
        return isContents;
    }

    public void setIsContents(int isContents) {
        this.isContents = isContents;
    }

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

    public boolean isNightTask() {
        return !"白天".equalsIgnoreCase(this.tasktype);
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

    public int getPatrolerId() {
        return patrolerId;
    }

    public void setPatrolerId(int patrolerId) {
        this.patrolerId = patrolerId;
    }
    
    public int getArrviedPointCount() {
        return arrviedPointCount;
    }

    public void setArrviedPointCount(int arrviedPointCount) {
        this.arrviedPointCount = arrviedPointCount;
    }

    public int getFeedbackedPointCount() {
        return feedbackedPointCount;
    }

    public void setFeedbackedPointCount(int feedbackedPointCount) {
        this.feedbackedPointCount = feedbackedPointCount;
    }

    public double getArrivedLinelen() {
        return arrivedLinelen;
    }

    public void setArrivedLinelen(double arrivedLinelen) {
        this.arrivedLinelen = arrivedLinelen;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTasksubtype() {
        return tasksubtype;
    }

    public void setTasksubtype(String tasksubtype) {
        this.tasksubtype = tasksubtype;
    }

    public int getEventnum() {
        return eventnum;
    }

    public void setEventnum(int eventnum) {
        this.eventnum = eventnum;
    }

    @Override
    public String toString() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskid", taskid);
        map.put("planid", planid);
        map.put("planname", planname);
        map.put("planstart", planstart);
        map.put("planend", planend);
        map.put("assigntime", assigntime);
        map.put("taskstart", taskstart);
        map.put("taskend", taskend);
        map.put("assignername", assignername);
        map.put("patrolername", patrolername);
        map.put("linelen", linelen);
        map.put("pointcount", pointcount);
        map.put("plancycle", plancycle);
        map.put("speed", speed);
        map.put("tasktype", tasktype);
        map.put("workstart", workstart);
        map.put("workend", workend);
        map.put("patrolerId", patrolerId);
        map.put("isContents", isContents);
        map.put("arrviedPointCount", arrviedPointCount);
        map.put("feedbackedPointCount", feedbackedPointCount);
        map.put("arrivedLinelen", arrivedLinelen);
        map.put("rate", rate);
        map.put("tasksubtype", tasksubtype);
        map.put("eventnum", eventnum);
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
                    } else if (key.equalsIgnoreCase("planid")) {
                        planid = json.getInt(key);
                    } else if (key.equalsIgnoreCase("planname")) {
                        planname = json.getString(key);
                    } else if (key.equalsIgnoreCase("planstart")) {
                        planstart = json.getString(key);
                    } else if (key.equalsIgnoreCase("planend")) {
                        planend = json.getString(key);
                    } else if (key.equalsIgnoreCase("assigntime")) {
                        assigntime = json.getString(key);
                    } else if (key.equalsIgnoreCase("taskstart")) {
                        taskstart = json.getString(key);
                    } else if (key.equalsIgnoreCase("taskend")) {
                        taskend = json.getString(key);
                    } else if (key.equalsIgnoreCase("assignername")) {
                        assignername = json.getString(key);
                    } else if (key.equalsIgnoreCase("patrolername")) {
                        patrolername = json.getString(key);
                    } else if (key.equalsIgnoreCase("linelen")) {
                        linelen = json.getInt(key);
                    } else if (key.equalsIgnoreCase("pointcount")) {
                        pointcount = json.getInt(key);
                    } else if (key.equalsIgnoreCase("plancycle")) {
                        plancycle = json.getString(key);
                    } else if (key.equalsIgnoreCase("speed")) {
                        speed = json.getInt(key);
                    } else if (key.equalsIgnoreCase("tasktype")) {
                        tasktype = json.getString(key);
                    } else if (key.equalsIgnoreCase("workstart")) {
                        workstart = json.getString(key);
                    } else if (key.equalsIgnoreCase("workend")) {
                        workend = json.getString(key);
                    } else if (key.equalsIgnoreCase("patrolerId")) {
                        patrolerId = json.getInt(key);
                    } else if (key.equalsIgnoreCase("isContents")) {
                        isContents = json.getInt(key);
                    }else if(key.equalsIgnoreCase("arrviedPointCount")){
                        arrviedPointCount = json.getInt(key);
                    }else if(key.equalsIgnoreCase("feedbackedPointCount")){
                        feedbackedPointCount = json.getInt(key);
                    }else if(key.equalsIgnoreCase("arrivedLinelen")){
                        arrivedLinelen = json.getDouble(key);
                    }else if(key.equalsIgnoreCase("rate")){
                        rate = json.getString(key);
                    }else if(key.equalsIgnoreCase("tasksubtype")) {
                        tasksubtype = json.getString("tasksubtype");
                    }else if(key.equalsIgnoreCase("eventnum")) {
                        eventnum = json.getInt("eventnum");
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
