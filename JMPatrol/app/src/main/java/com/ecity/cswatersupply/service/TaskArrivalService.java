package com.ecity.cswatersupply.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.adapter.planningtask.PointPartAttrAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.menu.LinePartInPlaceFeedbackOperator;
import com.ecity.cswatersupply.menu.PointPartInPlaceFeedbackOperator;
import com.ecity.cswatersupply.model.planningTask.ArriveInfo;
import com.ecity.cswatersupply.model.planningTask.ArriveLineInfo;
import com.ecity.cswatersupply.model.planningTask.ArrivePointInfo;
import com.ecity.cswatersupply.model.planningTask.PointPartAttrInfo;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTask;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskLinePart;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPointPart;
import com.ecity.cswatersupply.project.FuZhouPatrolModuleConfig;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.ui.activities.FZFeedBackCustomReportActivity;
import com.ecity.cswatersupply.ui.activities.planningtask.PlanningTaskActivity;
import com.ecity.cswatersupply.ui.activities.planningtask.PlanningTaskManager;
import com.ecity.cswatersupply.utils.ArriveDetecter;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.GeometryUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.esri.core.geometry.Polyline;
import com.z3app.android.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TaskArrivalService {
    private static TaskArrivalService instance;
    private Context context;
    private int arriveStatusCheckInterval;
    private int lineReportInterval;
    private ArrayList<Z3PlanTask> mPlanTaskList;
    private NotificationManager mNotificationManager;
    private ArrivedThread arrivedThread;
    private List<ArrivePointInfo> arrivedPointInfos;
    private List<ArriveLineInfo> arrivedLineInfos;
    //页面已到位的点
    private ArrayList<Z3PlanTaskPointPart> mPlanTaskPointPartList4Arrived;

    static {
        instance = new TaskArrivalService();
    }

    public static TaskArrivalService getInstance() {
        return instance;
    }

    public void start(Context context, int arriveStatusCheckInterval, int lineReportInterval) {
        if (arrivedThread != null) {
            LogUtil.i(this, "Already started. Will not start.");
            return;
        }
        EventBusUtil.register(this);
        this.context = context;
        this.arriveStatusCheckInterval = arriveStatusCheckInterval;
        this.lineReportInterval = lineReportInterval;
        initNotifiManager();
        arrivedThread = new ArrivedThread();
        arrivedThread.start();
    }

    public void stop() {
        EventBusUtil.unregister(this);
        if(null != mPlanTaskPointPartList4Arrived) {
            mPlanTaskPointPartList4Arrived.clear();
        }
        if (arrivedThread == null) {
            return;
        }

        if (!arrivedThread.isStopped()) {
            arrivedThread.setStopped(true);
            arrivedThread.interrupt();
            arrivedThread = null;
        }
    }

    // init mPlanTaskList
    private void initZ3PlanTasks() {
        mPlanTaskList = (ArrayList<Z3PlanTask>) PlanningTaskManager.getInstance().getPlanTask();
    }

    // 初始化通知栏配置
    private void initNotifiManager() {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    // 弹出Notification
    private void showNotification(Z3PlanTaskPointPart pointPart, Z3PlanTask planTask, String tips, int flag) {
        Intent intent = null;
        switch (flag) {
            case 1:
                //需要检查项上报的跳转到检查项上报界面
                intent = new Intent(context, CustomReportActivity1.class);
                Bundle bundle = new Bundle();
                bundle.putInt(CustomViewInflater.REPORT_TITLE, R.string.planningtask_report_check_items);
                bundle.putString(CustomViewInflater.REPORT_COMFROM, PointPartInPlaceFeedbackOperator.class.getName());
                bundle.putSerializable(PointPartInPlaceFeedbackOperator.INTENT_KEY_POINT_PART, pointPart);
                bundle.putSerializable(PointPartInPlaceFeedbackOperator.INTENT_KEY_TASK, planTask);
                bundle.putBoolean(CustomReportActivity1.INTENT_KEY_FROM_TASK_FUNC, true);
                intent.putExtras(bundle);
                try {
                    if (FuZhouPatrolModuleConfig.getConfig().isModuleUseable()) {
                        bundle.putBoolean(FZFeedBackCustomReportActivity.INTENT_KEY_FROM_TASK_FUNC, true);
                        intent = new Intent(context, FZFeedBackCustomReportActivity.class);
                        intent.putExtras(bundle);
                    } else {
                        bundle.putBoolean(CustomReportActivity1.INTENT_KEY_FROM_TASK_FUNC, true);
                        intent = new Intent(context, CustomReportActivity1.class);
                        intent.putExtras(bundle);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case 2:
                intent = new Intent(context, PlanningTaskActivity.class);
                intent.putExtra("isContents", 2);
                intent.putExtra("type", HostApplication.getApplication().getApplicationContext().getResources().getString(R.string.planningtask_type_p));
                SessionManager.isContent = 2;
                break;
            case 3:
                intent = new Intent(context, PlanningTaskActivity.class);
                intent.putExtra("isContents", 1);
                intent.putExtra("type", HostApplication.getApplication().getApplicationContext().getResources().getString(R.string.planningtask_type_p));
                SessionManager.isContent = 1;
                break;
            default:
                break;
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, pointPart.getGid(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String notificationTitle = context.getResources().getString(R.string.app_name_cswatersupply);
//        String notificationContent = context.getResources().getString(R.string.planningtask_notification_message);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.logo_main);
        builder.setTicker(notificationTitle);
        builder.setContentTitle(notificationTitle);
        builder.setContentText(tips);
        builder.setWhen(System.currentTimeMillis()); //发送时间
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentIntent(pendingIntent);
        mNotificationManager.notify(pointPart.getGid(),  builder.build());
        deletePointPart(pointPart);
    }

    //2017/3/15改
    private void handleNotificationTips(Z3PlanTaskPointPart pointPart){
        getAttrsListForGeometry(pointPart.getEquiporigin(),pointPart.getEquipid());
    }

    private void getAttrsListForGeometry(String equiporigin, String objectIds) {
        // 得到Geometry属性集合
        PlanningTaskService.getInstance().getAttrsForGeometry(equiporigin + "/query", objectIds,"pointservice");
    }

    private void showNotification(Z3PlanTaskLinePart linePart, int planTaskid) {
        //需要检查项上报的跳转到检查项上报界面
        Intent intent = new Intent(context, CustomReportActivity1.class);
        Bundle bundle = new Bundle();
        bundle.putInt(CustomViewInflater.REPORT_TITLE, R.string.planningtask_report_check_items);
        bundle.putString(CustomViewInflater.REPORT_COMFROM, LinePartInPlaceFeedbackOperator.class.getName());
        bundle.putSerializable(LinePartInPlaceFeedbackOperator.INTENT_KEY_LINE_PART, linePart);
        bundle.putInt(LinePartInPlaceFeedbackOperator.INTENT_KEY_TASK_ID, planTaskid);
        bundle.putBoolean(CustomReportActivity1.INTENT_KEY_FROM_TASK_FUNC, true);
        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, linePart.getGid(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String notificationTitle = context.getResources().getString(R.string.app_name_cswatersupply);
        String notificationContent = context.getResources().getString(R.string.planningtask_notification_message);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.logo_main);
        builder.setTicker(notificationTitle);
        builder.setContentTitle(notificationTitle);
        builder.setContentText(notificationContent);
        builder.setWhen(System.currentTimeMillis()); //发送时间
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentIntent(pendingIntent);
        mNotificationManager.notify(linePart.getGid(),  builder.build());
    }

    class ArrivedThread extends Thread {
        private volatile boolean isStopped = false;
        private int checkTimes;

        @Override
        public void run() {
            while (!isStopped && !isInterrupted()) {
                try {
                    doTask();
                    sleep(TaskArrivalService.this.arriveStatusCheckInterval);
                } catch (InterruptedException e) {
                    LogUtil.e(TaskArrivalService.this, e);
                }
            }
        }

        public boolean isStopped() {
            return isStopped;
        }

        public void setStopped(boolean isStopped) {
            this.isStopped = isStopped;
        }

        private synchronized void doTask() {
            initZ3PlanTasks();

            if (!ListUtil.isEmpty(mPlanTaskList)) {
                if (null == arrivedPointInfos) {
                    arrivedPointInfos = new ArrayList<ArrivePointInfo>();
                }
                if (null == arrivedLineInfos) {
                    arrivedLineInfos = new ArrayList<ArriveLineInfo>();
                }
                arrivedPointInfos.clear();
                arrivedLineInfos.clear();
                for (int i = 0; i < mPlanTaskList.size(); i++) {
                    if (isStopped()) {
                        return;
                    }

                    ArriveDetecter arrivalDetecter = new ArriveDetecter(mPlanTaskList.get(i));
                    List<ArriveInfo> arriveInfos = arrivalDetecter.startTask();
                    if (!ListUtil.isEmpty(arriveInfos)) {
                        for (int j = 0; j < arriveInfos.size(); j++) {
                            if (isStopped()) {
                                return;
                            }

                            ArriveInfo arriveInfo = arriveInfos.get(j);
                            Object z3PlanTaskPart = arriveInfo.getmZ3PlanTaskPart();
                            int planTaskid = arriveInfo.getPlanTaskId();
                            Z3PlanTask plantask = PlanningTaskManager.getInstance().getPlanTask(planTaskid);

                            if (z3PlanTaskPart instanceof Z3PlanTaskPointPart) {
                                Z3PlanTaskPointPart pointPart = (Z3PlanTaskPointPart) z3PlanTaskPart;
                                if (!pointPart.isArrive()) {
                                    continue;
                                }
                                int pointid = pointPart.getGid();
                                if (pointPart.isArrive()) {
                                    String currentTime = DateUtil.getCurrentTime();
                                    PlanningTaskManager.getInstance().updatePointArriveStatus(true, currentTime, pointid, planTaskid);

                                    ArrivePointInfo arrivedPointInfo = new ArrivePointInfo(pointPart.getTaskid(), pointPart.getGid(), pointPart.isArrive(), currentTime,
                                            pointPart.getTasktype());
                                    arrivedPointInfos.add(arrivedPointInfo);
                                    if (StringUtil.isBlank(pointPart.getType())) {
                                        return;
                                    }
                                    addArrviedPointPart(pointPart);
                                    handleNotificationTips(pointPart);
                                }
                            }

                            if (z3PlanTaskPart instanceof Z3PlanTaskLinePart) {
                                if (arriveInfo.getmGeometry() == null) {
                                    continue;
                                }
                                Z3PlanTaskLinePart linePart = (Z3PlanTaskLinePart) z3PlanTaskPart;
                                if(linePart.isCovered()) {
                                    String currentTime = DateUtil.getCurrentTime();
                                    // 覆盖线
                                    Polyline geo = (Polyline) arriveInfo.getmGeometry();
                                    double coveredLength = GeometryUtil.calculateLength(geo, true);
                                    PlanningTaskManager.getInstance().updateLineCoveredLength(true, currentTime, coveredLength,linePart.getGid(), planTaskid);

                                    ArriveLineInfo arrivedLineInfo = new ArriveLineInfo(linePart.getTaskid(), linePart.getGid(), linePart.isCovered(), currentTime,
                                            linePart.getCoveredLength(), linePart.getTasktype(), "line");
                                    arrivedLineInfos.add(arrivedLineInfo);
                                }
                               //点击之后 判断点属于巡检任务还是养护任务
                                if (plantask == null || plantask.getIsContents() == 0) {
                                    return;
                                }
                                if (2 == plantask.getIsContents()) {
                                    showNotification(linePart, planTaskid);
                                }

                            }
                        }
                        // 界面刷新
                        UIEvent event = new UIEvent(UIEventStatus.PLANNINGTASK_POINT_PART_NOTIFICATION);
                        event.setData(arriveInfos);
                        EventBusUtil.post(event);
                    }
                    Polyline trackLine = arrivalDetecter.getTrackPolyline();
                    if (null == trackLine) {
                        continue;
                    }
                    // 地图人员轨迹刷新
                    UIEvent event = new UIEvent(UIEventStatus.PLANNINGTASK_TRACKLINE_NOTIFICATION);
                    event.setData(trackLine);
                    EventBusUtil.post(event);
                }
            }
            repoterArrivedPointPart(arrivedPointInfos);
            checkTimes ++;
            if (checkTimes > (lineReportInterval / arriveStatusCheckInterval)) {
                repoterArrivedLinePart(arrivedLineInfos);
                checkTimes = 0;
            }
        }
    }

    private void repoterArrivedPointPart(List<ArrivePointInfo> infos) {
        // 上报到位
        if (ListUtil.isEmpty(infos)) {
            return;
        }
        PlanningTaskService.getInstance().repoterArrivedPointPart(ServiceUrlManager.getInstance().getInPlaceReportUrl(), infos);
    }

    public void repoterArrivedLinePart(List<ArriveLineInfo> infos) {
        // 上报线的到位
        if (ListUtil.isEmpty(infos)) {
            return;
        }
        PlanningTaskService.getInstance().repoterArrivedLinePart(ServiceUrlManager.getInstance().getInPlaceReportUrl(), infos);
    }

    private void handleGetPointPartAttrs(ResponseEvent event) {
        String eventObject = event.getData();
        PointPartAttrInfo info = PointPartAttrAdapter.getInstance().getPointPartAttrAdapter(eventObject);
        if (info == null || info.getAttrList() == null || info.getAttrList().size() == 0) {
            return;
        }
        List<PointPartAttrInfo.Attr> infoList = info.getAttrList().get(0);
        if (null != infoList && infoList.size() > 0) {
            StringBuffer tips = new StringBuffer();

            String devName = context.getResources().getString(R.string.planningtask_esrifieldtype_disp_name);
            String devCode = context.getResources().getString(R.string.planningtask_esrifieldtype_disp_code);

            String pumpName = "";
            String pumpCode = "";
            tips.append(context.getResources().getString(R.string.planningtask_notification_message));
            for (int i = 0; i < infoList.size(); i++) {
                PointPartAttrInfo.Attr attr = infoList.get(i);
                //zzz 2017-06-01 到位的通知是本地发的，暂时没修改 泵房名称  消火栓编号
                if (attr.getAttrKey().equalsIgnoreCase(context.getResources().getString(R.string.planningtask_esrifieldtype_name))) {
                    pumpName = attr.getAttrValue();
                } else if (attr.getAttrKey().equalsIgnoreCase(context.getResources().getString(R.string.planningtask_esrifieldtype_id))) {
                    devCode = context.getResources().getString(R.string.planningtask_esrifieldtype_id);
                    devName = "";
                    pumpName = "";
                    pumpCode = attr.getAttrValue();
                    break;
                } else if (attr.getAttrKey().equalsIgnoreCase(context.getResources().getString(R.string.planningtask_esrifieldtype_code))) {
                    pumpCode = attr.getAttrValue();
                }

                if (!StringUtil.isEmpty(pumpCode) && !StringUtil.isEmpty(pumpName)) {
                   break;
                }
            }

            if(!StringUtil.isEmpty(pumpName) && !StringUtil.isEmpty(pumpCode) ) {
                tips.append("[");
            }


            if (!StringUtil.isEmpty(pumpName)) {
                tips.append(devName);
                tips.append(": ");
                tips.append(pumpName);
            }

            if (!StringUtil.isEmpty(pumpCode)) {
                tips.append(" ");
                tips.append(devCode);
                tips.append(": ");
                tips.append(pumpCode);
            }

            if(!StringUtil.isEmpty(pumpName) && !StringUtil.isEmpty(pumpCode) ) {
                tips.append("]");
            }

            Z3PlanTaskPointPart currentPintPart = getPointPart4NotificationByGid(info);
            if (null == currentPintPart){
                return;
            }
            Z3PlanTask currentTak =  PlanningTaskManager.getInstance().getPlanTask(currentPintPart.getTaskid());
            if (null == currentTak){
                return;
            }
            if (!("0").equalsIgnoreCase(currentPintPart.getType())) {
                // 弹出提示 点击跳转到上报检查项界面
                showNotification(currentPintPart, currentTak, tips.toString(),1);
            } else {
                //点击之后 判断点属于巡检任务还是养护任务
                if (currentTak == null || currentTak.getIsContents() == 0) {
                    return;
                }
                int isContents = currentTak.getIsContents();
                if (isContents == 2) {
                    //养护任务列表
                    showNotification(currentPintPart, currentTak, tips.toString(), 2);
                } else if (isContents == 1) {
                    //巡检任务列表
                    showNotification(currentPintPart, currentTak, tips.toString(), 3);
                }
            }
        }
    }

    private Z3PlanTaskPointPart getPointPart4NotificationByGid(PointPartAttrInfo info){
        Z3PlanTaskPointPart pointPart  = null;
        String gid = null;
        List<PointPartAttrInfo.Attr> infoList = info.getAttrList().get(0);
        if (null != infoList && infoList.size() > 0) {
            for (int i = 0; i < infoList.size(); i++) {
                PointPartAttrInfo.Attr attr = infoList.get(i);
                //泵房名称  消火栓编号
                if (attr.getAttrKey().equalsIgnoreCase("gid") ) {
                    gid = attr.getAttrValue();
                    break;
                }
            }
        }
        if (null != gid){
            for (Z3PlanTaskPointPart pointPart1 : mPlanTaskPointPartList4Arrived){
                if (gid.equalsIgnoreCase(String.valueOf(pointPart1.getEquipid()))){
                    pointPart = pointPart1;
                    break;
                }
            }
        }
        return pointPart;
    }

    private String[] getXY4PointPart(String geometry){
        String[] xy = new String[2];
        try {
            JSONObject jsonObject = new JSONObject(geometry);
            String x = jsonObject.optString("x");
            String y = jsonObject.optString("y");
            xy[0] = x;
            xy[1] = y;
        }catch (JSONException e) {
            LogUtil.e(this, e);
        }
        return  xy;
    }

    //将所有的本次登录已到位的点全部集合
    private void addArrviedPointPart(Z3PlanTaskPointPart pointPart){
         if (null == mPlanTaskPointPartList4Arrived){
             mPlanTaskPointPartList4Arrived = new ArrayList<Z3PlanTaskPointPart>();
         }
         mPlanTaskPointPartList4Arrived.add(pointPart);
    }

    private void deletePointPart(Z3PlanTaskPointPart pointPart){
        if (!ListUtil.isEmpty(mPlanTaskPointPartList4Arrived)){
             for (Z3PlanTaskPointPart pointPart1 : mPlanTaskPointPartList4Arrived){
                 if (pointPart1.getGid()==pointPart.getGid()){
                     mPlanTaskPointPartList4Arrived.remove(pointPart);
                     break;
                 }
             }
        }
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.PLANNING_POINT_ARRIVED:
                handleRepoterArrivedPoint(event);
                break;
            case ResponseEventStatus.PLANNING_LINE_ARRIVED:
                handleRepoterArrivedLine(event);
                break;
            case ResponseEventStatus.PLANNING_ATTRS_POINTSERVICE:
                // 得到属性集合
                handleGetPointPartAttrs(event);
                break;
            default:
                break;
        }
    }

    private void handleRepoterArrivedLine(ResponseEvent event) {
        // 返回数据待定 应返回是否上报成功
        for (int i = 0; i < arrivedLineInfos.size(); i++) {
            String arrivedUpLoadTime = DateUtil.getCurrentTime();
            PlanningTaskManager.getInstance().updateLineArrivedStatus(arrivedUpLoadTime, arrivedLineInfos.get(i).getGid(), arrivedLineInfos.get(i).getTaskid());
        }
    }

    // 接受返回数据 并更新数据库数据
    private void handleRepoterArrivedPoint(ResponseEvent event) {
        // 返回数据待定 应返回是否上报成功
        for (int i = 0; i < arrivedPointInfos.size(); i++) {
            String arrivedUpLoadTime = DateUtil.getCurrentTime();
            PlanningTaskManager.getInstance().updatePointArrivedStatus(arrivedUpLoadTime, arrivedPointInfos.get(i).getGid(), arrivedPointInfos.get(i).getTaskid());
        }
    }

}
