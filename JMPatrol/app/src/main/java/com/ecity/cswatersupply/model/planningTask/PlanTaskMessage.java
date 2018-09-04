package com.ecity.cswatersupply.model.planningTask;

public class PlanTaskMessage {
    //通知类型
    public static final String NOTIFY_TYPE = "NOTIFY_TYPE";
    //点到位
    public static final String IS_POINT_ARRIVED = "IS_POINT_ARRIVED";
    //线到位
    public static final String IS_LINE_ARRIVED = "IS_LINE_ARRIVED";
    //区到位
    public static final String IS_POLYGON_ARRIVED = "IS_POLYGON_ARRIVED";
    //出圈
    public static final String IS_OUT_OF_RANGE = "IS_OUT_OF_RANGE";
    //超速
    public static final String IS_OVER_SPEED = "IS_OVER_SPEED";
    //广播的action
    public static final String BROADCOAST_ACTION = "com.ecity.notifyarrived";
}
