package com.ecity.cswatersupply.ui.activities.planningtask;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import android.content.Context;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTask;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskLinePart;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPointPart;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPolygonPart;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.PlanTaskUtils;
import com.esri.core.geometry.Polyline;
import com.zzz.ecity.android.applibrary.database.GPSPositionDao;
import com.zzz.ecity.android.applibrary.model.GPSPositionBean;

public class PlanningTaskManager {
    public static PlanningTaskManager instance;
    public int arrviedPointCount = 0;
    public int feedbackedPointsCount = 0;
    public double arrviedLineLength = 0;
    private static GPSPositionDao mGPSPostionDao;
    public static LinkedBlockingQueue<GPSPositionBean> mLinkQueueTrackPositions;

    public int getArrviedPointCount() {
        return arrviedPointCount;
    }

    public void setArrviedPointCount(int arrviedPointCount) {
        this.arrviedPointCount = arrviedPointCount;
    }

    public int getFeedbackedPointsCount() {
        return feedbackedPointsCount;
    }

    public void setFeedbackedPointsCount(int feedbackedPointsCount) {
        this.feedbackedPointsCount = feedbackedPointsCount;
    }

    public double getArrviedLineLength() {
        return arrviedLineLength;
    }

    public void setArrviedLineLength(double arrviedLineLength) {
        this.arrviedLineLength = arrviedLineLength;
    }

    private PlanningTaskManager() {

    }

    public static PlanningTaskManager getInstance() {
        if (null == instance) {
            instance = new PlanningTaskManager();
        }
        mGPSPostionDao = GPSPositionDao.getInstance();
        return instance;
    }

    /**
     * 更新到位状态
     * 
     * @param isArrived
     *            是否到位
     * @param arriveTime
     *            到位时间。若isArrived为false，arriveTime将被设为0。
     * @param pointId
     *            点的id
     * @param taskId
     *            点所属任务的id
     */
    public void updatePointArriveStatus(boolean isArrived, String arriveTime, int gid, int taskId) {
        if (!isArrived) {
            arriveTime = "0";
        }
        if (SessionManager.pointParts == null) {
            return;
        }
        //改变点的状态
        Z3PlanTaskPointPart pointPart = SessionManager.pointParts.get(taskId + "_" + gid);
        
        pointPart.setArrive(isArrived);
        pointPart.setArriveTime(arriveTime);
        SessionManager.pointParts.put(taskId + "_" + gid, pointPart);
        //改变点所属的任务到位点数量
        Z3PlanTask plantask = getPlanTask(taskId);
        if (null == plantask) {
            return;
        } else {
            plantask.setArrviedPointCount(plantask.getArrviedPointCount() + 1);
            SessionManager.allPlanTasks.put(taskId + "", plantask);
        }
    }

    public void updatePointArrivedStatus(String arrivedUpLoadTime, int gid, int taskId) {
        if (SessionManager.pointParts == null) {
            return;
        }
        Z3PlanTaskPointPart pointPart = SessionManager.pointParts.get(taskId + "_" + gid);
        pointPart.setArriveUpLoadTime(arrivedUpLoadTime);

        SessionManager.pointParts.put(taskId + "_" + gid, pointPart);
    }

    public void updateLineArrivedStatus(String arrivedUpLoadTime, int gid, int taskId) {
        if (SessionManager.lineParts == null) {
            return;
        }
        Z3PlanTaskLinePart linePart = SessionManager.lineParts.get(taskId + "_" + gid);
        linePart.setArriveUpLoadTime(arrivedUpLoadTime);
        SessionManager.lineParts.put(taskId + "_" + gid, linePart);
    }

    /**
     * 更新反馈状态
     * 
     * @param isFeedback
     *            是否已反馈
     * @param feedbackTime
     *            反馈时间。若isFeedback为false，feedbackTime将设为0。
     * @param pointId
     *            点的id
     * @param taskId
     *            点所属任务的id
     */
    public void updatePointFeedbackStatus(boolean isFeedback, String feedbackTime, int gid, int taskId) {
        if (!isFeedback) {
            feedbackTime = "0";
        }
        if (SessionManager.pointParts == null) {
            return;
        }
        Z3PlanTaskPointPart pointPart = SessionManager.pointParts.get(taskId + "_" + gid);
        pointPart.setFeedBack(isFeedback);
        pointPart.setFeedbackTime(feedbackTime);
        SessionManager.pointParts.put(taskId + "_" + gid, pointPart);

        //改变点所属的任务反馈点数量
        Z3PlanTask plantask = getPlanTask(taskId);
        if (null == plantask) {
            return;
        } else {
            plantask.setFeedbackedPointCount(plantask.getFeedbackedPointCount() + 1);
            SessionManager.allPlanTasks.put(taskId + "", plantask);
        }
    }

    /**
     * 更新线反馈状态
     * 
     * @param isFeedback
     *            是否已反馈
     * @param feedbackTime
     *            反馈时间。若isFeedback为false，feedbackTime将设为0。
     * @param pointId
     *            线的id
     * @param taskId
     *            线所属任务的id
     */
    public void updateLineFeedbackStatus(boolean isFeedback, String feedbackTime, int gid, int taskId) {
        if (!isFeedback) {
            feedbackTime = "0";
        }
        if (SessionManager.lineParts == null) {
            return;
        }
        Z3PlanTaskLinePart linePart = SessionManager.lineParts.get(taskId + "_" + gid);
        linePart.setFeedBack(isFeedback);
        linePart.setFeedbackTime(feedbackTime);
        SessionManager.lineParts.put(taskId + "_" + gid, linePart);
    }

    /**
     * 更新线的覆盖长度
     * 
     * @param coveredLength
     *            覆盖长度
     * @param lineId
     *            线的id
     * @param taskId
     *            线所属任务的id
     */
    public void updateLineCoveredLength(boolean isCovered, String arriveTime,double coveredLength, int gid, int taskId) {
        if (!isCovered) {
            arriveTime = "0";
        }
        if (null == SessionManager.lineParts) {
            return;
        }
        Z3PlanTaskLinePart LinePart = SessionManager.lineParts.get(taskId + "_" + gid);
        if(null == LinePart) {
            return;
        }
        LinePart.setCoveredLength(coveredLength);
        LinePart.setArriveTime(arriveTime);
        LinePart.setCovered(isCovered);
        SessionManager.lineParts.put(taskId + "_" + gid, LinePart);

        //改变线所属的任务的覆盖率
        Z3PlanTask plantask = getPlanTask(taskId);
        if (null == plantask) {
            return;
        } else {
            plantask.setArrivedLinelen(getArrivedLineLength(taskId));
            SessionManager.allPlanTasks.put(taskId + "", plantask);
        }
    }

    public Z3PlanTask getPlanTask(int taskId) {
        return SessionManager.allPlanTasks.get(taskId + "");
    }

    public List<Z3PlanTask> getPlanTask() {
        User currentUser = HostApplication.getApplication().getCurrentUser();
        HashMap<String, String> planTaskKeys = SessionManager.planTaskKeys;
        if (null == planTaskKeys || planTaskKeys.size() == 0)
            return null;
        String planTaskPerferenceKeyString = planTaskKeys.get(currentUser.getGid() + Constants.PLANTASK_PER_KEY);
        ArrayList<Z3PlanTask> planTaskList = new ArrayList<Z3PlanTask>();
        List<String> PreferenceNameList = String2List(planTaskPerferenceKeyString);
        if (ListUtil.isEmpty(PreferenceNameList)) {
            return null;
        }
        for (String str : PreferenceNameList) {
            Z3PlanTask planTask = SessionManager.allPlanTasks.get(str.trim());
            planTaskList.add(planTask);
        }

        return planTaskList;
    }

    public List<Z3PlanTask> getPlanTasks(int isContents) {
        if (isContents == 2) {
            return getFlexflowPlanTask();
        } else if (isContents == 1) {
            return getPatrolPlanTask();
        }
        return null;
    }

    public List<Z3PlanTask> getPlanTasks(List<Z3PlanTask> PlanTasks, int isContents) {
        ArrayList<Z3PlanTask> planTaskList = new ArrayList<Z3PlanTask>();
        for (Z3PlanTask plantask : PlanTasks) {
            if (plantask.getIsContents() == isContents) {
                planTaskList.add(plantask);
            }
        }
        return planTaskList;
    }

    public List<Z3PlanTask> getPatrolPlanTask() {
        ArrayList<Z3PlanTask> PatrolplanTaskList = new ArrayList<Z3PlanTask>();
        ArrayList<Z3PlanTask> planTaskList = (ArrayList<Z3PlanTask>) getPlanTask();
        if (ListUtil.isEmpty(planTaskList)) {
            return null;
        }
        for (Z3PlanTask plantask : planTaskList) {
            if (plantask.getIsContents() == 1) {
                PatrolplanTaskList.add(plantask);
            }
        }
        return PatrolplanTaskList;
    }

    public List<Z3PlanTask> getFlexflowPlanTask() {
        ArrayList<Z3PlanTask> FlexflowplanTaskList = new ArrayList<Z3PlanTask>();
        ArrayList<Z3PlanTask> planTaskList = (ArrayList<Z3PlanTask>) getPlanTask();
        if (ListUtil.isEmpty(planTaskList)) {
            return null;
        }
        for (Z3PlanTask plantask : planTaskList) {
            if (plantask.getIsContents() == 2) {
                FlexflowplanTaskList.add(plantask);
            }
        }
        return FlexflowplanTaskList;
    }

    public Z3PlanTaskPointPart getPlanTaskPointPart(int taskId, int gid) {
        return SessionManager.pointParts.get(taskId + "_" + gid);
    }

    public Z3PlanTaskLinePart getPlanTaskLinePart(int taskId, int gid) {
        return SessionManager.lineParts.get(taskId + "_" + gid);
    }

    public Z3PlanTaskPolygonPart getPlanTaskPolygonPart(int taskId, int gid) {
        return SessionManager.polygonPart.get(taskId + "_" + gid);
    }

    //根据任务id将到位点与反馈点的数量存储在本地缓存
    public void setArrivedAndFeedBackCount(int taskId) {
        arrviedPointCount = 0;
        feedbackedPointsCount = 0;
        User user = HostApplication.getApplication().getCurrentUser();
        HashMap<String, String> planTaskKeys = SessionManager.planTaskKeys;
        if (null == planTaskKeys || planTaskKeys.size() == 0)
            return;
        String pointPartsNameStr = planTaskKeys.get(user.getGid() + Constants.POINTPART_PER_KEY);
        ArrayList<String> pointPartXmlNameList = (ArrayList<String>) String2List(pointPartsNameStr);
        if (ListUtil.isEmpty(pointPartXmlNameList)) {
            return;
        }
        for (int i = 0; i < pointPartXmlNameList.size(); i++) {
            if (pointPartXmlNameList.get(i).contains(taskId + "_")) {
                String key = pointPartXmlNameList.get(i).trim();
                Z3PlanTaskPointPart pointPart = SessionManager.pointParts.get(key);
                if (null != pointPart) {
                    if (pointPart.isArrive()) {
                        arrviedPointCount++;
                    }
                    if (pointPart.isFeedBack()) {
                        feedbackedPointsCount++;
                    }
                }
            }
        }
    }

    /**
     * 根据任务id将覆盖线的长度存储在本地缓存
     * @param taskId  任务id
     */
    public void setArrivedLineLength(int taskId) {
        arrviedLineLength = 0;
        User user = HostApplication.getApplication().getCurrentUser();
        HashMap<String, String> planTaskKeys = SessionManager.planTaskKeys;
        if (null == planTaskKeys || planTaskKeys.size() == 0)
            return;
        String linePartsNameStr = planTaskKeys.get(user.getGid() + Constants.LINEPART_PER_KEY);
        ArrayList<String> linePartXmlNameList = (ArrayList<String>) String2List(linePartsNameStr);
        if (ListUtil.isEmpty(linePartXmlNameList)) {
            return;
        }
        for (int i = 0; i < linePartXmlNameList.size(); i++) {
            if (linePartXmlNameList.get(i).contains(taskId + "_")) {
                String key = linePartXmlNameList.get(i).trim();
                Z3PlanTaskLinePart linePart = SessionManager.lineParts.get(key);
                if (null != linePart&&linePart.isCovered()) {
                    arrviedLineLength += linePart.getCoveredLength();
                }
            }
        }
    }

    public List<Z3PlanTaskPointPart> getPointParts(int Flag, int taskId) {
        User user = HostApplication.getApplication().getCurrentUser();
        
        HashMap<String, String> planTaskKeys = SessionManager.planTaskKeys;
        if (null == planTaskKeys || planTaskKeys.size() == 0)
            return null;
        String pointPartsNameStr = planTaskKeys.get(user.getGid() + Constants.POINTPART_PER_KEY);
        ArrayList<String> pointPartXmlNameList = (ArrayList<String>) String2List(pointPartsNameStr);
        if (ListUtil.isEmpty(pointPartXmlNameList)) {
            return null;
        }
        List<Z3PlanTaskPointPart> pointPartList = new ArrayList<Z3PlanTaskPointPart>();
        List<Z3PlanTaskPointPart> pointPartListForReturn = new ArrayList<Z3PlanTaskPointPart>();
        for (int i = 0; i < pointPartXmlNameList.size(); i++) {
            if (pointPartXmlNameList.get(i).contains(taskId + "_")) {
                String key = pointPartXmlNameList.get(i).trim();
                pointPartList.add(SessionManager.pointParts.get(key));
            }
        }
        if (ListUtil.isEmpty(pointPartList)) {
            return null;
        }
        switch (Flag) {
            case Constants.PLANTASK_GETPOINT_ID_ALL:
                pointPartListForReturn = pointPartList;
                break;
            case Constants.PLANTASK_GETPOINT_ID_ARRIVED:
                for (Z3PlanTaskPointPart pointPart : pointPartList) {
                    if (null != pointPart && pointPart.isArrive()) {
                        pointPartListForReturn.add(pointPart);
                    }
                }
                break;
            case Constants.PLANTASK_GETPOINT_ID_FEEDBACK:
                for (Z3PlanTaskPointPart pointPart : pointPartList) {
                    if (null != pointPart && pointPart.isFeedBack()) {
                        pointPartListForReturn.add(pointPart);
                    }
                }
                break;
            case Constants.PLANTASK_GETPOINT_ID_NOT_FEEDBACK:
                for (Z3PlanTaskPointPart pointPart : pointPartList) {
                    if (null != pointPart && pointPart.isArrive() && !pointPart.isFeedBack()) {
                        pointPartListForReturn.add(pointPart);
                    }
                }
                break;
            case Constants.PLANTASK_GETPOINT_ID_UNARRIVED:
                for (Z3PlanTaskPointPart pointPart : pointPartList) {
                    if (null != pointPart && !pointPart.isArrive()) {
                        pointPartListForReturn.add(pointPart);
                    }
                }
                break;
            default:
                break;
        }

        return pointPartListForReturn;
    }

    public List<Z3PlanTaskLinePart> getLines(int taskId) {
        User user = HostApplication.getApplication().getCurrentUser();
        HashMap<String, String> planTaskKeys = SessionManager.planTaskKeys;
        if (null == planTaskKeys || planTaskKeys.size() == 0)
            return null;
        String LinePartsNameStr = planTaskKeys.get(user.getGid() + Constants.LINEPART_PER_KEY);
        ArrayList<String> LinePartsXmlNameList = (ArrayList<String>) String2List(LinePartsNameStr);
        if (ListUtil.isEmpty(LinePartsXmlNameList)) {
            return null;
        }
        List<Z3PlanTaskLinePart> LinePartsList = new ArrayList<Z3PlanTaskLinePart>();
        for (int i = 0; i < LinePartsXmlNameList.size(); i++) {
            String key = LinePartsXmlNameList.get(i).trim();
            if (LinePartsXmlNameList.get(i).contains(taskId + "_")) {
                LinePartsList.add(SessionManager.lineParts.get(key));
            }
        }
        return LinePartsList;
    }

    public List<Z3PlanTaskPolygonPart> getPolygon(int taskId) {
        User user = HostApplication.getApplication().getCurrentUser();
        HashMap<String, String> planTaskKeys = SessionManager.planTaskKeys;
        if (null == planTaskKeys || planTaskKeys.size() == 0)
            return null;
        String polygonPartNameStr = planTaskKeys.get(user.getGid() + Constants.GONPART_PER_KEY);
        ArrayList<String> polygonPartXmlNameList = (ArrayList<String>) String2List(polygonPartNameStr);
        if (ListUtil.isEmpty(polygonPartXmlNameList)) {
            return null;
        }
        List<Z3PlanTaskPolygonPart> polygonPartList = new ArrayList<Z3PlanTaskPolygonPart>();
        for (int i = 0; i < polygonPartXmlNameList.size(); i++) {
            if (polygonPartXmlNameList.get(i).contains(taskId + "_")) {
                polygonPartList.add(SessionManager.polygonPart.get(polygonPartXmlNameList.get(i).trim()));
            }
        }

        return polygonPartList;
    }

    /**
     * 
     * 
     * @param [xx,yy,zz]格式字符串
     */
    public List<String> String2List(String preference) {
        if (StringUtil.isEmpty(preference)) {
            return null;
        }
        List<String> PreferenceNameList = new ArrayList<String>();
        String s = preference.substring(1, preference.length() - 1);
        if (s.contains(",")) {
            String[] ss = s.split(",");
            for (int i = 0; i < ss.length; i++) {
                String newName = ss[i];
                if (PreferenceNameList.contains(newName) == false) {
                    PreferenceNameList.add(ss[i]);
                }
            }
        } else {
            PreferenceNameList.add(s);
        }

        return PreferenceNameList;
    }

    public double getArrivedLineLength(int taskId) {
        double length = 0;
        List<Z3PlanTaskLinePart> lines = PlanningTaskManager.getInstance().getLines(taskId);
        if (ListUtil.isEmpty(lines)) {
            return length;
        }
        for (Z3PlanTaskLinePart linePart : lines) {
            if (null != linePart) {
                length = length + linePart.getCoveredLength();
            }
        }

        return length;
    }

    public String getArrivedLineRate(Context mContext, Z3PlanTask mPlanningtask) {
        double arrivedLength = getArrivedLineLength(mPlanningtask.getTaskid());
        double taskLength = mPlanningtask.getLinelen();
        return getRate(mContext, arrivedLength, taskLength);
    }

    public String getRate(Context mContext, Object numOne, Object numtwo) {
        String rate;
        DecimalFormat df = new DecimalFormat(mContext.getString(R.string.planningtask_pettern));
        double temp = Double.parseDouble(numOne.toString())/ Double.parseDouble(numtwo.toString());
        if(Math.abs(temp) > 1.0 && Math.abs(temp) < 1.1)  {
            temp = 1.0;
        }
        rate = df.format(temp * 100);
        if (rate.contains(".")) {
            int leng = rate.indexOf(".");
            rate = rate.substring(0, leng - 1);
        }
        return rate;
    }

    public void putTask2List(Z3PlanTask currentPlanTask) {
        SessionManager.allPlanTasks.put(currentPlanTask.getTaskid() + "", currentPlanTask);
        
    }
    
    /**
     *  从数据库获取该时间段内的轨迹信息
     * @param
     * @return
     */
    public Polyline buildOldTrackLineByClock(Z3PlanTask task) {
        Polyline mTrackPolyline = null;
        try {
            loadGPSPositionFromDbByTask(task);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != mLinkQueueTrackPositions&& mLinkQueueTrackPositions.size() > 0) {
            List<GPSPositionBean> rackPositions = new ArrayList<GPSPositionBean>();
            Iterator<GPSPositionBean> iterator = mLinkQueueTrackPositions.iterator();
            while (iterator.hasNext()) {
                GPSPositionBean gpsPositionBean = iterator.next();
                rackPositions.add(gpsPositionBean);
            }

            if (!ListUtil.isEmpty(rackPositions)) {
                mTrackPolyline = PlanTaskUtils.buildPolylineFromGPSPositionListForXY(rackPositions);
            }
        }
        return mTrackPolyline;
    }
    /**
     * 从sqlite数据库中加载莫一段时间内的坐标点
     */
    public void loadGPSPositionFromDbByTask(Z3PlanTask task) {
        if (StringUtil.isBlank(task.getWorkstart())||StringUtil.isBlank(task.getWorkend())) {
            return ;
        }
        if (null == mLinkQueueTrackPositions) {
            mLinkQueueTrackPositions = new LinkedBlockingQueue<GPSPositionBean>();
        }
        mLinkQueueTrackPositions.clear();
        String userid = HostApplication.getApplication().getCurrentUser().getId();
        String date = DateUtil.getCurrentDate();
        String startTime = date + " " + task.getWorkstart();
        String endTime = date + " " + task.getWorkend();
        String where = "userid = " + userid + " and time between '" + startTime + "' and '" + endTime + "'";
        List<GPSPositionBean> result = mGPSPostionDao.queryList(where);
        if (ListUtil.isEmpty(result)) {
            return;
        }

        for (int i = 0; i < result.size(); i++) {
            mLinkQueueTrackPositions.add(result.get(i));
        }
    }
    /**
     * 保存点设备属性值
     * @param infoDetails
     * @param pointPart
     */
    public void updatePointInfoDetails(String infoDetails,Z3PlanTaskPointPart mpointPart){
        if(StringUtil.isEmpty(infoDetails)||null == mpointPart){
            return;
        }
        // Z3PlanTaskPointPart pointPart = SessionManager.pointParts.get(mpointPart.getTaskid() + "_" + mpointPart.getGid());

        mpointPart.setInfoDetails(infoDetails);
        SessionManager.pointParts.put(mpointPart.getTaskid() + "_" + mpointPart.getGid(), mpointPart);
    }

    public String getInfoDetaisByPointPart(Z3PlanTaskPointPart mpointPart){
        String info = null;
        if(null == mpointPart){
            return info;
        }
        Z3PlanTaskPointPart pointPart = SessionManager.pointParts.get(mpointPart.getTaskid() + "_" + mpointPart.getGid());
        if (null != pointPart) {
            info = pointPart.getInfoDetails();
        }
        return info;
    }

}
