package com.ecity.cswatersupply.utils;

import android.content.Intent;
import android.location.Location;

import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.planningTask.ArriveInfo;
import com.ecity.cswatersupply.model.planningTask.PlanTaskMessage;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTask;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskLinePart;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPointPart;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPolygonPart;
import com.ecity.cswatersupply.ui.activities.planningtask.PlanningTaskManager;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.zzz.ecity.android.applibrary.database.GPSPositionDao;
import com.zzz.ecity.android.applibrary.model.GPSPositionBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class ArriveDetecter {
    public static LinkedBlockingQueue<GPSPositionBean> mLinkQueueTrackPositions;
    private Polyline mTrackPolyline;// 轨迹线
    private Polyline mCoverPolyline;// 覆盖长度
    private Polyline mEffectivePolyline;// 有效长度
    private Z3PlanTask mZ3PlanTask;

    private GPSPositionDao mGPSPostionDao;
    private SpatialReference mSpatialReference;
    private List<ArriveInfo> mArriveInfos;

    public List<ArriveInfo> getmArriveInfos() {
        return mArriveInfos;
    }

    public void setmArriveInfos(List<ArriveInfo> mArriveInfos) {
        this.mArriveInfos = mArriveInfos;
    }

    public ArriveDetecter() {
        this.mGPSPostionDao = GPSPositionDao.getInstance();
    }

    public ArriveDetecter(Z3PlanTask z3PlanTask) {
        this.mGPSPostionDao = GPSPositionDao.getInstance();
        this.mSpatialReference = SpatialReference.create(102113);
        this.mArriveInfos = new ArrayList<ArriveInfo>();
        this.mZ3PlanTask = z3PlanTask;
    }

    /*
     * public static ArriveDetecter getInstance() { if (null == instance) {
     * synchronized (ArriveDetecter.class) { if (null == instance) { instance =
     * new ArriveDetecter(); } } }
     * 
     * return instance; }
     */

    public List<ArriveInfo> startTask() {
        buildTrackLine();
        calculatePointArrived();
        calculateCoverPolyline();

        return mArriveInfos;
    }

    public Polyline getCoverPolylines() {
        return mCoverPolyline;
    }

    public Polyline getEffectivePolylines() {
        return mEffectivePolyline;
    }

    public Polyline getTrackPolyline() {
        return mTrackPolyline;
    }

    /**
     * 从sqlite数据库中加载坐标点
     */
    public void loadGPSPositionFromDbByUserid(String userid, String date) {
        String where = "userid = " + userid + " and time like '" + date + "%'";
        List<GPSPositionBean> result = mGPSPostionDao.queryList(where);
        if (ListUtil.isEmpty(result)) {
            return;
        }
        if (null == mLinkQueueTrackPositions) {
            mLinkQueueTrackPositions = new LinkedBlockingQueue<GPSPositionBean>();
        }
        mLinkQueueTrackPositions.clear();

        for (int i = 0; i < result.size(); i++) {
            mLinkQueueTrackPositions.add(result.get(i));
        }
    }

    public void loadGPSPositionFromDbByStatus() {

        String where = "status = 0";
        List<GPSPositionBean> result = mGPSPostionDao.queryList(where);
        if (ListUtil.isEmpty(result)) {
            return;
        }
        if (null == mLinkQueueTrackPositions) {
            mLinkQueueTrackPositions = new LinkedBlockingQueue<GPSPositionBean>();
        }
        mLinkQueueTrackPositions.clear();

        for (int i = 0; i < result.size(); i++) {
            mLinkQueueTrackPositions.add(result.get(i));
        }

    }

    private void buildTrackLine() {
        if (null != mLinkQueueTrackPositions && mLinkQueueTrackPositions.size() > 0) {
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
    }

    /**
     * 计算覆盖线（计划线与轨迹缓冲区相交得到的线）
     */
    public Polyline calculateCoverPolyline() {
        if (null == mZ3PlanTask || null == mTrackPolyline) {
            return null;
        }

        boolean isNightTask = mZ3PlanTask.isNightTask();
        //1
        ArrayList<Z3PlanTaskLinePart> z3PlanTaskLineParts = (ArrayList<Z3PlanTaskLinePart>) PlanningTaskManager.getInstance().getLines(mZ3PlanTask.getTaskid());
        if (ListUtil.isEmpty(z3PlanTaskLineParts)) {
            return null;
        }
        for (Z3PlanTaskLinePart z3PlanTaskLinePart : z3PlanTaskLineParts) {
            int buffer = z3PlanTaskLinePart.getBuffer();
            if (isNightTask && buffer <= 50) {
                buffer = 50;
            } else {
                if (buffer < 30) {
                    buffer = 30;
                }
            }
            Polygon bufferForPolyline = PlanTaskUtils.getBufferForPolyline(mTrackPolyline, mSpatialReference, buffer);
            //2
            Polyline planPolyline = (Polyline) PlanTaskUtils.buildGeometryFromJSON(z3PlanTaskLinePart.getGeom());

            if (isIntersect(planPolyline, bufferForPolyline)) {
                mCoverPolyline = PlanTaskUtils.getCoverPolyline(bufferForPolyline, planPolyline, mSpatialReference);
                z3PlanTaskLinePart.setCovered(true);
                notification(PlanTaskMessage.IS_LINE_ARRIVED);
            }
            ArriveInfo arriveInfo = new ArriveInfo();
            arriveInfo.setmGeometry(mCoverPolyline);
            arriveInfo.setmZ3PlanTaskPart(z3PlanTaskLinePart);
            arriveInfo.setPlanTaskId(mZ3PlanTask.getTaskid());
            mArriveInfos.add(arriveInfo);
            mCoverPolyline = null;
        }

        return mCoverPolyline;
    }

    /**
     * 计算有效轨迹（轨迹线与计划线缓冲区相交的线）
     */
    public Polyline calculateEffectivePolyLine() {
        if (null == mZ3PlanTask || null == mTrackPolyline) {
            return null;
        }

        ArrayList<Z3PlanTaskLinePart> z3PlanTaskLineParts = (ArrayList<Z3PlanTaskLinePart>) PlanningTaskManager.getInstance().getLines(mZ3PlanTask.getTaskid());
        for (Z3PlanTaskLinePart z3PlanTaskLinePart : z3PlanTaskLineParts) {
            //3
            Polygon bufferForPolyline = (Polygon) PlanTaskUtils.buildGeometryFromJSON(z3PlanTaskLinePart.getGeoBuffer());
            if (null == bufferForPolyline || bufferForPolyline.isEmpty() || null == mTrackPolyline || mTrackPolyline.isEmpty()) {
                break;
            }
            if (isIntersect(mTrackPolyline, bufferForPolyline)) {
                mEffectivePolyline = PlanTaskUtils.getCoverPolyline(bufferForPolyline, mTrackPolyline, mSpatialReference);
                break;
            }
        }

        return mEffectivePolyline;
    }

    /**
     * 判断是否超速，只有在不出圈的情况下才会存在超速问题。<br>
     * 基于judgeOverSpeed(GPSPositionBean bean)做的重载。
     * 
     * @return
     */
    /**
     * 判断是否超速，只有在不出圈的情况下才会存在超速问题。<br>
     * 基于judgeOverSpeed(GPSPositionBean bean)做的重载。
     * 
     * @param speed 速度
     * @param isOutOfRange 是否出圈。由judgeOutOfRange计算。
     * @param isInDetourArea 是否在绕行区。由judgeInDetourArea计算。
     * @return
     */
    public boolean judgeOverSpeed(double speed, boolean isOutOfRange, boolean isInDetourArea) {
        if (mZ3PlanTask == null) {
            return false;
        }

        double planSpeed = Double.valueOf(mZ3PlanTask.getSpeed() * 1000d) / 3.6;// km/h 换算成 m/s
        double realSpeed = speed * 1000d;

        return ((realSpeed - planSpeed > 0) && !isOutOfRange && !isInDetourArea);
    }

    /**
     * 是否出圈(线任务，区任务)
     * 
     * @param point
     *            Point
     * @return
     */
    public boolean judgeOutOfRange(Point point) {
        if (null == mZ3PlanTask || null == point) {
            return false;
        }

        boolean flag = false;

        ArrayList<Z3PlanTaskLinePart> z3PlanTaskLineParts = (ArrayList<Z3PlanTaskLinePart>) PlanningTaskManager.getInstance().getLines(mZ3PlanTask.getTaskid());
        if (ListUtil.isEmpty(z3PlanTaskLineParts)) {
            return false;
        }
        for (int i = 0; i < z3PlanTaskLineParts.size(); i++) {
            Z3PlanTaskLinePart linePart = z3PlanTaskLineParts.get(i);
            Polygon buffer = PlanTaskUtils.buildPolygonFromString(linePart.getGeoBuffer());
            if (null == buffer) {
                return false;
            }
            flag = !isPointInPolygon(point, buffer);
            if (flag) {
                notification(PlanTaskMessage.IS_OUT_OF_RANGE);
                return flag;
            }
        }

        ArrayList<Z3PlanTaskPolygonPart> z3PolygonParts = (ArrayList<Z3PlanTaskPolygonPart>) PlanningTaskManager.getInstance().getPolygon(mZ3PlanTask.getTaskid());
        if (ListUtil.isEmpty(z3PolygonParts)) {
            return false;
        }
        for (int i = 0; i < z3PolygonParts.size(); i++) {
            Z3PlanTaskPolygonPart pointPart = z3PolygonParts.get(i);
            Polygon polygon = PlanTaskUtils.buildPolygonFromString(pointPart.getGeom());
            if (null == polygon) {
                return false;
            }
            flag = !isPointInPolygon(point, polygon);
            if (flag) {
                notification(PlanTaskMessage.IS_OUT_OF_RANGE);
                return flag;
            }
        }

        return flag;
    }

    private void calculatePointArrived() {
        if (null == mZ3PlanTask || null == mTrackPolyline || mTrackPolyline.isEmpty()) {
            return;
        }
        ArrayList<Z3PlanTaskPointPart> z3PointParts = (ArrayList<Z3PlanTaskPointPart>) PlanningTaskManager.getInstance().getPointParts(Constants.PLANTASK_GETPOINT_ID_UNARRIVED,
                mZ3PlanTask.getTaskid());
        if (ListUtil.isEmpty(z3PointParts)) {
            return;
        }
        for (int i = 0; i < z3PointParts.size(); i++) {

            Z3PlanTaskPointPart pointPart = z3PointParts.get(i);
            int buffer = pointPart.getBuffer();
            Polygon bufferForPoint = PlanTaskUtils.getBufferForPoint((Point) PlanTaskUtils.buildGeometryFromJSON(pointPart.getGeom()), mSpatialReference, buffer);
            boolean isIntersect = PlanTaskUtils.isIntersect(bufferForPoint, mTrackPolyline, mSpatialReference);
            if (isIntersect) {
                pointPart.setArrive(true);
                notification(PlanTaskMessage.IS_POINT_ARRIVED);
            }

            ArriveInfo arriveInfo = new ArriveInfo();
            arriveInfo.setmZ3PlanTaskPart(pointPart);
            arriveInfo.setPlanTaskId(mZ3PlanTask.getTaskid());
            mArriveInfos.add(arriveInfo);
        }
    }

    /**
     * 判断点是否到位（根据Z3PlanTaskPointPart）
     * 
     * @param bean
     *            GPSPositionBean
     * @return
     */
    public boolean judgePointArrived(GPSPositionBean bean) {
        if (null == mZ3PlanTask || null == bean) {
            return false;
        }
        double[] mercatorxy = null;
        Point point = new Point();
        try {
            mercatorxy = CoordTransfer.transToLocal(bean.getlat(), bean.getlon());

            if (null != mercatorxy && 2 == mercatorxy.length) {
                point.setX(mercatorxy[0]);
                point.setY(mercatorxy[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            point = null;
        }
        if (null == point || point.isEmpty()) {
            return false;
        }
        boolean flag = false;

        ArrayList<Z3PlanTaskPointPart> z3PointParts = (ArrayList<Z3PlanTaskPointPart>) PlanningTaskManager.getInstance().getPointParts(Constants.PLANTASK_GETPOINT_ID_ALL,
                mZ3PlanTask.getTaskid());
        if (ListUtil.isEmpty(z3PointParts)) {
            return false;
        }
        for (int i = 0; i < z3PointParts.size(); i++) {

            Z3PlanTaskPointPart pointPart = z3PointParts.get(i);
            Polygon geoBuffer = PlanTaskUtils.buildPolygonFromString(pointPart.getGeoBuffer());

            flag = isPointInPolygon(point, geoBuffer);
            if (flag) {
                pointPart.setArrive(true);
                notification(PlanTaskMessage.IS_POINT_ARRIVED);
                return flag;
            } else {
                pointPart.setArrive(false);
            }

            ArriveInfo arriveInfo = new ArriveInfo();
            arriveInfo.setmGeometry(point);
            arriveInfo.setmZ3PlanTaskPart(pointPart);
            arriveInfo.setPlanTaskId(mZ3PlanTask.getTaskid());
            mArriveInfos.add(arriveInfo);
        }

        /*
         * ArrayList<Z3PlanTaskPolygonPart> z3PolygonParts =
         * SessionManager.CurrentPlanTask.getPolygonParts(); if
         * (ListUtil.isEmpty(z3PolygonParts)) { return false; } for (int i = 0;
         * i < z3PolygonParts.size(); i++) { Z3PlanTaskPolygonPart pointPart =
         * z3PolygonParts.get(i); Polygon polygon = pointPart.getGeom(); if
         * (null == polygon) { return false; } flag = isPointInPolygon(point,
         * polygon); if (flag) { pointPart.setArrived(true);
         * notification(PlanTaskMessage.IS_ARRIVED); return flag; } }
         */

        return flag;
    }

    /**
     * 判断点是否到位(根据Z3PlanTaskPointPart)
     * 
     * @param location
     *            Location
     * @return
     */
    public boolean judgePointArrivedByLocation(Location location) {
        if (null == mZ3PlanTask || null == location) {
            return false;
        }
        GPSPositionBean bean = new GPSPositionBean();
        bean.setlat(location.getLatitude());
        bean.setlon(location.getLongitude());

        return judgePointArrived(bean);
    }

    /**
     * 判断区是否到位（根据Z3PlanTaskPolygonPart）
     * 
     * @param bean
     *            GPSPositionBean
     * @return
     */
    public boolean judgePolygonArrived(GPSPositionBean bean) {
        if (null == mZ3PlanTask || null == bean) {
            return false;
        }
        double[] mercatorxy = null;
        Point point = new Point();
        try {
            mercatorxy = CoordTransfer.transToLocal(bean.getlat(), bean.getlon());

            if (null != mercatorxy && 2 == mercatorxy.length) {
                point.setX(mercatorxy[0]);
                point.setY(mercatorxy[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            point = null;
        }
        if (null == point || point.isEmpty()) {
            return false;
        }
        boolean flag = false;

        ArrayList<Z3PlanTaskPolygonPart> z3PolygonParts = (ArrayList<Z3PlanTaskPolygonPart>) PlanningTaskManager.getInstance().getPolygon(mZ3PlanTask.getTaskid());
        if (ListUtil.isEmpty(z3PolygonParts)) {
            return false;
        }
        for (int i = 0; i < z3PolygonParts.size(); i++) {
            Z3PlanTaskPolygonPart pointPart = z3PolygonParts.get(i);
            Polygon polygon = PlanTaskUtils.buildPolygonFromString(pointPart.getGeom());
            if (null == polygon) {
                return false;
            }
            flag = isPointInPolygon(point, polygon);
            if (flag) {
                pointPart.setArrived(true);
                notification(PlanTaskMessage.IS_POLYGON_ARRIVED);
                return flag;
            } else {
                pointPart.setArrived(false);
            }
            ArriveInfo arriveInfo = new ArriveInfo();
            arriveInfo.setmGeometry(point);
            arriveInfo.setmZ3PlanTaskPart(pointPart);
            arriveInfo.setPlanTaskId(mZ3PlanTask.getTaskid());
            mArriveInfos.add(arriveInfo);
        }

        return flag;
    }

    /**
     * 判断是否在绕行区（线任务才有）
     * 
     * @param Point
     * @return
     */
    public boolean judgeInDetourArea(Point point) {
        if (null == point) {
            return true;
        }

        return judgeInDetourAreaByPoint(point);
    }

    private boolean judgeInDetourAreaByPoint(Point point) {
        if (null == mZ3PlanTask || null == point || point.isEmpty()) {
            return true;
        }

        boolean isInDetourArea = false;
        ArrayList<Z3PlanTaskLinePart> z3PlanTaskLinePart = (ArrayList<Z3PlanTaskLinePart>) PlanningTaskManager.getInstance().getLines(mZ3PlanTask.getTaskid());
        if (null != z3PlanTaskLinePart) {

            for (Z3PlanTaskLinePart z3LinePart : z3PlanTaskLinePart) {
                //                ArrayList<Polygon> z3DetourPolygon =z3LinePart.getDetours();
                //                if (null != z3DetourPolygon) {
                //                    for (Polygon detour : z3DetourPolygon) {
                //                        if (null != detour && !detour.isEmpty()) {
                //                            // isInDetourArea =
                //                            // PlanTaskUtils.isPointInPolygon(detour, point,
                //                            // mSpatialReference);
                //                            isInDetourArea = isPointInPolygon(point, detour);
                //                        }
                //                        if (isInDetourArea) {
                //                            break;
                //                        }
                //                    }
                //                }
                if (isInDetourArea) {
                    break;
                }
                // 判断是否在缓冲区外，缓冲区外，也是属于在绕行区内
                if (!isPointInPolygon(point, PlanTaskUtils.buildPolygonFromString(z3LinePart.getGeoBuffer()))) {
                    isInDetourArea = true;
                    break;
                }
            }
        }

        return isInDetourArea;
    }

    private boolean isPointInPolygon(Point pnt, Polygon ploygon) {
        if (null == ploygon || ploygon.isEmpty() || null == pnt || pnt.isEmpty())
            return false;

        boolean result = false;
        try {
            result = PlanTaskUtils.isPointInPolygon(ploygon, pnt, mSpatialReference);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    private boolean isIntersect(Polyline polyline, Polygon polygon) {
        if (null == polygon || polygon.isEmpty() || null == polyline || polyline.isEmpty())
            return false;

        boolean result = false;
        try {
            result = PlanTaskUtils.isIntersect(polygon, polyline, mSpatialReference);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    private void notification(String type) {
        try {
            Intent intent = new Intent();
            intent.putExtra(PlanTaskMessage.NOTIFY_TYPE, type);
            intent.setAction(PlanTaskMessage.BROADCOAST_ACTION);
            HostApplication.getApplication().sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
