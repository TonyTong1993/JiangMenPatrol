package com.ecity.cswatersupply.model.event;

import java.util.Map;

import com.ecity.cswatersupply.model.AModel;

public abstract class Event extends AModel {
    private static final long serialVersionUID = -9049133767791797526L;

    public static final int STATUS_UNPROCESSING = 0;
    public static final int STATUS_PROCESSED = 1;
    public static final int STATUS_END = 2;
    public static final String ATTR_KEY_ID = "eventid";
    public static final String ATTR_KEY_REPORTER = "shangbaoren";
    public static final String ATTR_KEY_DESCRIPTION = "wentimiaoshu";
    public static final String ATTR_KEY_ADDRESS = "fashenweizhi";
    public static final String ATTR_KEY_LATITUDE = "lat";
    public static final String ATTR_KEY_LONGITUDE = "lon";
    public static final String ATTR_KEY_X = "x";
    public static final String ATTR_KEY_Y = "y";
    public static final String ATTR_KEY_CODE = "shijianbianhao";
    public static final String ATTR_KEY_STATE = "chulizhuangtai";
    public static final String ATTR_KEY_REPORT_TIME = "shangbaoshijian";
    public static final String ATTR_KEY_EVENTTYPE = "eventtype";
    public static final String ATTR_KEY_USERID = "userid";
    public static final String ATTR_KEY_PHOTOS = "zhaopian";
    public static final String ATTR_KEY_CAN_EDIT = "canedit";

    protected final Map<String, String> dataSource;

    public abstract String getId();

    public abstract String getReportTime();

    public abstract EventType getType();

    public Event(Map<String, String> dataSource) {
        this.dataSource = dataSource;
    }

    public String getAttribute(String key) {
        return dataSource.get(key);
    }
}
