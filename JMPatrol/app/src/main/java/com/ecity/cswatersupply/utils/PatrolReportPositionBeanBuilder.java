package com.ecity.cswatersupply.utils;

import android.location.Location;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTask;
import com.esri.core.geometry.Point;
import com.z3app.android.util.DateUtil;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.MyApplication;
import com.zzz.ecity.android.applibrary.model.GPSPositionBean;
import com.zzz.ecity.android.applibrary.service.AReportPositionBeanBuilder;
import com.zzz.ecity.android.applibrary.utils.DateUtilExt;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

public class PatrolReportPositionBeanBuilder extends AReportPositionBeanBuilder {

    @Override
    public GPSPositionBean buildCustomGPSPositionBean(Location location) {

        if (location == null || HostApplication.getApplication() == null || HostApplication.getApplication().getCurrentUser() == null) {
            return null;
        }

        GPSPositionBean bean = convertLocation2PositionBean(location);
        if (!ListUtil.isEmpty(SessionManager.CurrentPlanTasks)) {
            boolean inDetourArea = false;
            boolean isOverspeed = false;
            boolean isOutOfRange = false;
            boolean isNightTask = false;
            int planTaskId = -1;

            for (Z3PlanTask planTask : SessionManager.CurrentPlanTasks) {
                ArriveDetecter arriveDetecter = new ArriveDetecter(planTask);
                Point point = new Point(bean.getx(), bean.gety());
                inDetourArea = arriveDetecter.judgeInDetourArea(point);
                isOutOfRange = arriveDetecter.judgeOutOfRange(point);
                isOverspeed = arriveDetecter.judgeOverSpeed(bean.getSpeed(), isOutOfRange, inDetourArea);
                if (!isOutOfRange) {
                    planTaskId = planTask.getPlanid();
                    isNightTask = planTask.isNightTask();
                    break;
                }
            }

            bean.setPlanTaskId(String.valueOf(planTaskId));
            bean.setInDetourArea(inDetourArea ? 1 : 0);
            bean.setOverspeed(isOverspeed ? 1 : 0);
            bean.setNightWatch(isNightTask ? 1 : 0);
            bean.setTag("0");
        }

        return bean;
    }

    private GPSPositionBean convertLocation2PositionBean(Location location) {
        GPSPositionBean bean = new GPSPositionBean();
        bean.setUserid(Integer.valueOf(HostApplication.getApplication().getCurrentUser().getId()));
        double[] mercatorxy = null;
        try {
            mercatorxy = CoordTransfer.transToLocal(location.getLatitude(), location.getLongitude());
            if (null != mercatorxy && 2 == mercatorxy.length) {
                bean.setx(mercatorxy[0]);
                bean.sety(mercatorxy[1]);
            } else {
                bean.setx(0.0);
                bean.sety(0.0);
            }
        } catch (Exception e) {
            LogUtil.e(this, e);
        }

        bean.setlat(location.getLatitude());
        bean.setlon(location.getLongitude());
        bean.setacu(location.getAccuracy());
        bean.setbattery(MyApplication.getApplication().getBatteryLevelPercent());
        bean.setSpeed(location.getSpeed());

        String time = DateUtil.changeLongToString(location.getTime());
        if (StringUtil.isEmpty(time) || time.contains("1970"))
            time = DateUtilExt.getOffsetToServiceTime();

        if (StringUtil.isEmpty(time)) {
            time = DateUtil.getDateEN();
        }

        if (time.contains(".")) {
            String strs[] = time.split("\\.");
            time = strs[0];
        }
        bean.setTime(time);
        bean.setRepay(0);
        bean.setStatus(0);

        return bean;
    }
}
