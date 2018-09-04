package com.ecity.cswatersupply.adapter.planningtask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.adapter.checkitem.InspectItemAdapter;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.Content;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTask;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskLinePart;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPointPart;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPolygonPart;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.activities.planningtask.PlanningTaskManager;
import com.ecity.cswatersupply.utils.JsonUtil;
import com.esri.core.geometry.Polyline;

public class PlanTasksAdapter {
    private static PlanTasksAdapter instance;
    private static List<String> planTaskPerferenceKeyList;
    private static List<String> pointPartPerferenceKeyList;
    private static List<String> linePartPerferenceKeyList;
    private static List<String> gonPartPerferenceKeyList;
    private String currentUserId;

    private PlanTasksAdapter() {
    }

    static {
        instance = new PlanTasksAdapter();
    }

    public static PlanTasksAdapter getInstance() {
        return instance;
    }

    private void initData() {
        currentUserId = HostApplication.getApplication().getCurrentUser().getGid();
        if (SessionManager.planTaskKeys == null) {
            SessionManager.planTaskKeys = new HashMap<String, String>();
        } else {
            SessionManager.planTaskKeys.clear();
        }

        if (SessionManager.allPlanTasks == null) {
            SessionManager.allPlanTasks = new HashMap<String, Z3PlanTask>();
        } else {
            SessionManager.allPlanTasks.clear();
        }

        if (SessionManager.pointParts == null) {
            SessionManager.pointParts = new HashMap<String, Z3PlanTaskPointPart>();
        } else {
            SessionManager.pointParts.clear();
        }

        if (SessionManager.lineParts == null) {
            SessionManager.lineParts = new HashMap<String, Z3PlanTaskLinePart>();
        } else {
            SessionManager.lineParts.clear();
        }

        if (SessionManager.polygonPart == null) {
            SessionManager.polygonPart = new HashMap<String, Z3PlanTaskPolygonPart>();
        } else {
            SessionManager.polygonPart.clear();
        }

        if (planTaskPerferenceKeyList == null) {
            planTaskPerferenceKeyList = new CopyOnWriteArrayList<String>();
        } else {
            planTaskPerferenceKeyList.clear();
        }

        if (pointPartPerferenceKeyList == null) {
            pointPartPerferenceKeyList = new CopyOnWriteArrayList<String>();
        } else {
            pointPartPerferenceKeyList.clear();
        }

        if (linePartPerferenceKeyList == null) {
            linePartPerferenceKeyList = new CopyOnWriteArrayList<String>();
        } else {
            linePartPerferenceKeyList.clear();
        }

        if (gonPartPerferenceKeyList == null) {
            gonPartPerferenceKeyList = new CopyOnWriteArrayList<String>();
        } else {
            gonPartPerferenceKeyList.clear();
        }
    }

    // 按照新的字段结构解析计划任务数据 
    public void getPlanTasksAdapter(final String planTasksJson) {
        initData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject mPlanTasksJson = JsonUtil.getJsonObject(planTasksJson);
                JSONArray jsonArray = mPlanTasksJson.optJSONArray("total");
                if (mPlanTasksJson == null||jsonArray == null||mPlanTasksJson.optInt("rows") == 0) {
                    UIEvent event = new UIEvent(UIEventStatus.PLANNINGTASK_NO_DATA);
                    EventBusUtil.post(event);
                    return;
                }

                final ArrayList<Z3PlanTask> planTaskList = new ArrayList<Z3PlanTask>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    Z3PlanTask plantask = adaptePlanTask(jsonArray.opt(i).toString());
                    PlanningTaskManager.getInstance().setArrivedAndFeedBackCount(plantask.getTaskid());
                    PlanningTaskManager.getInstance().setArrivedLineLength(plantask.getTaskid());
                    // 本地缓存
                    plantask.setArrviedPointCount(PlanningTaskManager.getInstance().getArrviedPointCount());
                    plantask.setFeedbackedPointCount(PlanningTaskManager.getInstance().getFeedbackedPointsCount());

                    plantask.setArrivedLinelen(PlanningTaskManager.getInstance().getArrviedLineLength());
                    SessionManager.allPlanTasks.put(plantask.getTaskid() + "", plantask);

                    planTaskList.add(plantask);
                }
                SessionManager.planTaskKeys.put(currentUserId + Constants.PLANTASK_PER_KEY, planTaskPerferenceKeyList.toString());

                UIEvent event = new UIEvent(UIEventStatus.PLANNINGTASK_ADAPTER_TASK);
                event.setData(planTaskList);
                EventBusUtil.post(event);
            }
        }).start();
    }

    // 按照新的字段结构解析计划任务数据
    public ArrayList<Z3PlanTask> getPlanTasksAdapterInMainActivity(String planTasksJson) {
        initData();
        ArrayList<Z3PlanTask> planTaskList = null;
        JSONObject mPlanTasksJson;
        try {
            mPlanTasksJson = new JSONObject(planTasksJson);
            int rows = mPlanTasksJson.getInt("rows");
            if (rows > 0) {
                planTaskList = new ArrayList<Z3PlanTask>();
                JSONArray jsonArray = mPlanTasksJson.getJSONArray("total");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Z3PlanTask plantask = adaptePlanTask(jsonArray.get(i).toString());
                    PlanningTaskManager.getInstance().setArrivedAndFeedBackCount(plantask.getTaskid());
                    PlanningTaskManager.getInstance().setArrivedLineLength(plantask.getTaskid());
                    // 本地缓存
                    plantask.setArrviedPointCount(PlanningTaskManager.getInstance().getArrviedPointCount());
                    plantask.setFeedbackedPointCount(PlanningTaskManager.getInstance().getFeedbackedPointsCount());
                    plantask.setArrivedLinelen(PlanningTaskManager.getInstance().getArrviedLineLength());

                    SessionManager.allPlanTasks.put(plantask.getTaskid() + "", plantask);
                    planTaskList.add(plantask);
                }
                SessionManager.planTaskKeys.put(currentUserId + Constants.PLANTASK_PER_KEY, planTaskPerferenceKeyList.toString());
            }
        } catch (JSONException e) {
            LogUtil.e(this, e);
        }
        return planTaskList;
    }

    // 获取一个Z3PlanTask
    public Z3PlanTask adaptePlanTask(String planTaskStr) {
        Z3PlanTask plantask = new Z3PlanTask();
        User currentUser = HostApplication.getApplication().getCurrentUser();
        plantask.setPatrolerId(Integer.valueOf(currentUser.getId()));
        try {
            JSONObject plantaskJson = new JSONObject(planTaskStr);
            plantask.setTaskid(plantaskJson.optInt("taskid"));
            plantask.setPlanid(plantaskJson.optInt("planid"));
            plantask.setGroupid(plantaskJson.optString("groupid"));
            plantask.setPlanname(plantaskJson.optString("planname"));
            plantask.setPlanstart(plantaskJson.optString("planstart"));
            plantask.setPlanend(plantaskJson.optString("planend"));
            plantask.setAssigntime(plantaskJson.optString("assigntime"));
            plantask.setIsContents(plantaskJson.optBoolean("isContents") ? 2 : 1);
            plantask.setAssignername(plantaskJson.optString("assignername"));
            plantask.setTaskstart(plantaskJson.optString("taskstart"));
            plantask.setTaskend(plantaskJson.optString("taskend"));
            plantask.setPlancycle(plantaskJson.optString("plancycle"));
            plantask.setSpeed(plantaskJson.optInt("speed"));
            plantask.setTasktype(plantaskJson.optString("tasktype"));
            plantask.setWorkstart(plantaskJson.optString("workstart"));
            plantask.setWorkend(plantaskJson.optString("workend"));
            Polyline TrackLineInClock = PlanningTaskManager.instance.buildOldTrackLineByClock(plantask);
            plantask.setTrackLinegeom(TrackLineInClock);
            plantask.setPatrolername(plantaskJson.optString("patrolername"));
            plantask.setLinelen(plantaskJson.optDouble("linelen"));
            plantask.setPointcount(plantaskJson.optInt("pointcount"));
            plantask.setTasksubtype(plantaskJson.optString("tasksubtype"));
            plantask.setEventnum(plantaskJson.optInt("eventnum"));
            JSONArray pointArray = plantaskJson.optJSONArray("points");
            if (null != pointArray && pointArray.length() > 0) {
                getZ3PlanTaskPointPart(pointArray.toString(), plantask);
                SessionManager.isPointTask.put(plantask.getTaskid()+"", true);
                getZ3PlanTaskPointPart(pointArray.toString(), plantask);
                SessionManager.planTaskKeys.put(currentUserId + Constants.POINTPART_PER_KEY, pointPartPerferenceKeyList.toString());
            }
            JSONArray polylineArray = plantaskJson.optJSONArray("lines");
            if (null != polylineArray && polylineArray.length() > 0) {
                SessionManager.isPointTask.put(plantask.getTaskid()+"", false);
                getZ3PlanTaskLinePart(polylineArray.toString(), plantask);
                SessionManager.planTaskKeys.put(currentUserId + Constants.LINEPART_PER_KEY, linePartPerferenceKeyList.toString());
            }
            JSONArray polygonArray = plantaskJson.optJSONArray("areas");
            if (null != polygonArray && polygonArray.length() > 0) {
                SessionManager.isPointTask.put(plantask.getTaskid()+"", false);
                getZ3PlanTaskPolygonPart(polygonArray.toString(), plantask.getTaskid());
                SessionManager.planTaskKeys.put(currentUserId + Constants.GONPART_PER_KEY, gonPartPerferenceKeyList.toString());
            }
        } catch (JSONException e) {
            LogUtil.e(this, e);
        }
        String tagerName = plantask.getTaskid() + "";
        if (!planTaskPerferenceKeyList.contains(tagerName)) {
            planTaskPerferenceKeyList.add(tagerName);
        }
        return plantask;
    }

    // 获取pointpart
    public ArrayList<Z3PlanTaskPointPart> getZ3PlanTaskPointPart(String pointStr, Z3PlanTask task) {
        if (null == pointStr || ("").equalsIgnoreCase(pointStr) || pointStr.equalsIgnoreCase("[]")) {
            return null;
        }
        try {
            // 解析点类型的任务
            JSONArray pointArray = new JSONArray(pointStr);
            if (pointArray.length() > 0) {
                ArrayList<Z3PlanTaskPointPart> pointparts = new ArrayList<Z3PlanTaskPointPart>();
                for (int p = 0; p < pointArray.length(); p++) {
                    Z3PlanTaskPointPart pointpart = new Z3PlanTaskPointPart();
                    // geo data
                    JSONObject pointJs = (JSONObject) pointArray.get(p);
                    pointpart = adaptePointPart(pointJs, task);
                    pointparts.add(pointpart);
                }
                return pointparts;
            }
        } catch (JSONException e) {
            LogUtil.e(this, e);
        }

        return null;
    }

    // 解析每个点
    public Z3PlanTaskPointPart adaptePointPart(JSONObject pointJs, Z3PlanTask task) {
        int gid = 0;
        Z3PlanTaskPointPart pointpart = new Z3PlanTaskPointPart();
        String pGeometry = pointJs.optJSONObject("geometry").toString();
        pointpart.setGeom(pGeometry);
        pointpart.setTaskid(task.getTaskid());
        pointpart.setTasktype(task.getTasktype());
        pointpart.setGroupid(task.getGroupid());
        JSONObject attributes = pointJs.optJSONObject("attributes");
        pointpart.setGid(attributes.optInt("gid"));
        gid = attributes.optInt("gid");
        pointpart.setTpointid(attributes.optInt("tpointid"));
        pointpart.setBuffer(attributes.optInt("buffer"));
        pointpart.setType(attributes.optString("type"));
        pointpart.setEtypeid(attributes.optString("etypeid"));

        String equiporigin = attributes.optString("equiporigin");
        //TODO:服务端应该对此处缺陷进行支持，后续要删除此段代码
        //zzz 2017-06-05 如果配置的地址为192.168网段，则进行一次替换
        if(!StringUtil.isBlank(equiporigin)){
            if(equiporigin.startsWith("http://192.168.") || equiporigin.startsWith("https://192.168.")) {
                String subStr = "";

                if(equiporigin.contains("ServiceEngine")) {
                    subStr = equiporigin.substring(equiporigin.indexOf("ServiceEngine"));
                } else if (equiporigin.contains("arcgis")) {
                    subStr = equiporigin.substring(equiporigin.indexOf("arcgis"));
                }

                String newIpPort = "http://202.168.161.166:24380/";
                if(!StringUtil.isBlank(ServiceUrlManager.getInstance().getPatrolDeviceQueryIpPort())) {
                    newIpPort = ServiceUrlManager.getInstance().getPatrolDeviceQueryIpPort();
                }

                equiporigin = newIpPort + subStr;
            }
        }
        pointpart.setEquiporigin(equiporigin);
        pointpart.setEquipid(attributes.optString("equipid"));
        pointpart.setFeedBack(attributes.optBoolean("isFeedback"));
        pointpart.setArrive(attributes.optBoolean("isArrive"));
        SessionManager.pointParts.put(task.getTaskid() + "_" + gid, pointpart);
        String tagerName = task.getTaskid() + "_" + gid;
        if (!pointPartPerferenceKeyList.contains(tagerName)) {
            pointPartPerferenceKeyList.add(tagerName);
        }
        return pointpart;
    }

    // 获取线类型
    public ArrayList<Z3PlanTaskLinePart> getZ3PlanTaskLinePart(String lineStr, Z3PlanTask task) {
        if (null == lineStr || ("").equals(lineStr) || lineStr.equalsIgnoreCase("[]")) {
            return null;
        }
        try {
            // 解析线类型的任务
            JSONArray polylineArray = new JSONArray(lineStr);
            if (polylineArray.length() > 0) {
                ArrayList<Z3PlanTaskLinePart> lineparts = new ArrayList<Z3PlanTaskLinePart>();
                for (int j = 0; j < polylineArray.length(); j++) {
                    Z3PlanTaskLinePart linepart = new Z3PlanTaskLinePart();
                    linepart.setTaskid(task.getTaskid());
                    linepart.setTasktype(task.getTasktype());
                    linepart.setGroupid(task.getGroupid());
                    JSONObject polylineJs = (JSONObject) polylineArray.get(j);
                    String lGeometry = polylineJs.optJSONObject("geometry").toString();
                    linepart.setGeom(lGeometry);
                    JSONObject attributes = polylineJs.optJSONObject("attributes");
                    linepart.setGid(attributes.optInt("gid"));
                    linepart.setBuffer(attributes.optInt("buffer"));
                    linepart.setEtypeid(attributes.optString("etypeid"));
                    linepart.setTlineid(attributes.optInt("tlineid"));
                    linepart.setEquiporigin(attributes.optString("equiporigin"));
                    linepart.setEquipid(attributes.optString("equipid"));
                    linepart.setFeedBack(attributes.optBoolean("isFeedback"));
                    linepart.setCovered(attributes.optBoolean("isArrive"));
                    linepart.setArriveTime(attributes.optString("arriveTime"));
                    linepart.setLinelen(attributes.optDouble("linelen"));
                    linepart.setCoveredLength(attributes.optDouble("arrivelen"));
                    linepart.setType(attributes.optString("type"));
                    linepart.setSchemeid(attributes.optInt("schemeid"));
                    SessionManager.lineParts.put(task.getTaskid() + "_" + linepart.getGid(), linepart);
                    String tagerName = task.getTaskid() + "_" + linepart.getGid();
                    if (!linePartPerferenceKeyList.contains(tagerName)) {
                        linePartPerferenceKeyList.add(tagerName);
                    }
                    lineparts.add(linepart);
                }
                return lineparts;
            }
        } catch (Exception e) {
            LogUtil.e(this, e);
        }
        return null;
    }

    // 获取区类型
    public ArrayList<Z3PlanTaskPolygonPart> getZ3PlanTaskPolygonPart(String polygonStr, int taskid) {
        if (null == polygonStr || ("").equalsIgnoreCase(polygonStr) || polygonStr.equalsIgnoreCase("[]")) {
            return null;
        }
        try {
            JSONArray polygonArray = new JSONArray(polygonStr);
            if (polygonArray.length() > 0) {
                ArrayList<Z3PlanTaskPolygonPart> polygonParts = new ArrayList<Z3PlanTaskPolygonPart>();

                for (int j = 0; j < polygonArray.length(); j++) {
                    Z3PlanTaskPolygonPart polygonPart = new Z3PlanTaskPolygonPart();
                    JSONObject polygonJs = (JSONObject) polygonArray.get(j);
                    String gGeometry = polygonJs.optJSONObject("geometry").toString();
                    polygonPart.setGeom(gGeometry);
                    JSONObject attributes = polygonJs.optJSONObject("attributes");
                    polygonPart.setGid(attributes.optInt("gid"));
                    polygonPart.setSchemeid(attributes.optInt("schemeid"));
                    polygonParts.add(polygonPart);

                    SessionManager.polygonPart.put(taskid + "_" + polygonPart.getGid(), polygonPart);
                    String tagerName = taskid + "_" + polygonPart.getGid();
                    if (!gonPartPerferenceKeyList.contains(tagerName)) {
                        gonPartPerferenceKeyList.add(tagerName);
                    }
                }
                return polygonParts;
            }
        } catch (Exception e) {
            LogUtil.e(this, e);
        }

        return null;
    }

    public ArrayList<InspectItem> adaptInspectItems(String contents) {
        ArrayList<InspectItem> inspectItems = new ArrayList<InspectItem>();
        JSONArray serverItems = null;
        try {
            JSONObject json = new JSONObject(contents);
            String items = json.optString("params");
            serverItems = new JSONArray(items);
            if (null == serverItems || serverItems.length() <= 0) {
                return null;
            }
            for (int n = 0; n < serverItems.length(); n++) {
                JSONObject serverItem = serverItems.optJSONObject(n);
                InspectItem inspectItem = InspectItemAdapter.adaptItem(serverItem);
                inspectItems.add(inspectItem);
            }
        } catch (JSONException e) {
            LogUtil.e(this, e);
        }
        return inspectItems;
    }

    public ArrayList<Content> adaptContents(String contentsStr) {
        ArrayList<Content> contentsList = new ArrayList<Content>();
        JSONArray contentsItems = null;
        try {
            JSONObject contentsJson = new JSONObject(contentsStr);
            String contents = contentsJson.optString("contents");
            contentsItems = new JSONArray(contents);
            if (null == contentsItems || contentsItems.length() <= 0) {
                return null;
            }
            for (int i = 0; i < contentsItems.length(); i++) {
                JSONObject serverItem = contentsItems.optJSONObject(i);
                Content content = new Content();
                String etypeid = serverItem.optString("etypeid");
                String groupid = serverItem.optString("groupid");
                content.setEtypeid(etypeid);
                content.setGroupid(groupid);
                JSONObject contentJson = serverItem.optJSONObject("content");
                String tableName = contentJson.optString("tableName");
                content.setTableName(tableName);
                JSONArray serverItems = null;
                ArrayList<InspectItem> inspectItems = null;
                String items = contentJson.optString("params");
                inspectItems = new ArrayList<InspectItem>();
                serverItems = new JSONArray(items);
                if (null == serverItems || serverItems.length() <= 0) {
                    return null;
                }
                for (int n = 0; n < serverItems.length(); n++) {
                    JSONObject cItem = serverItems.optJSONObject(n);
                    //判断是否是group 或者是检查项
                    InspectItem inspectItem = InspectItemAdapter.adaptItem(cItem);
                    inspectItems.add(inspectItem);
                }
                content.setInspectItems(inspectItems);
                contentsList.add(content);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentsList;
    }
}
