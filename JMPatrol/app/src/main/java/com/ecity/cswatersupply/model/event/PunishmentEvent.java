package com.ecity.cswatersupply.model.event;

import java.util.Map;

public class PunishmentEvent extends Event {
    private static final long serialVersionUID = -9029735983565980197L;
    public static final int STATUS_REPORTING = 0;
    public static final int STATUS_PRINTING = 1;
    public static final int STATUS_END = 2;

    public static final String ATTR_KEY_ID = "eventid";
    public static final String ATTR_KEY_PHONE = "PHONE";
    public static final String ATTR_KEY_CREATED_BY = "PERSON";
    public static final String ATTR_KEY_CODE = "PUNISHNUM";
    public static final String ATTR_KEY_CREATED_TIME = "PUNISHTIME";
    public static final String ATTR_KEY_ADDRESS = "PUNISHADDR";
    public static final String ATTR_KEY_ACCEPTER = "ACCEPTER";
    public static final String ATTR_KEY_REASON = "PUNISHITEM";
    public static final String ATTR_KEY_STATE = "PUNISHSTATE";
    public static final String ATTR_KEY_LATITUDE = "lat";
    public static final String ATTR_KEY_LONGITUDE = "lon";

    public PunishmentEvent(Map<String, String> dataSource) {
        super(dataSource);
    }

    @Override
    public EventType getType() {
        return EventType.PUNISHMENT;
    }

    @Override
    public String getId() {
        return getAttribute(ATTR_KEY_ID);
    }

    @Override
    public String getReportTime() {
        return getAttribute(ATTR_KEY_CREATED_TIME);
    }
}

/*
    {
      "eventid": "201608111557051980992717",
      "PHONE": "hdhdh",
      "PUNISHAMOUNT": "",
      "PUNISHMARK": "uuuu",
      "PERSON": "蓬江东区班长",
      "userid": "8",
      "PUNISHTIME": "2016-08-11 15:57:05",
      "PUNISHADDR": "广东省中山市中山市市辖区古神公路",
      "ACCEPTER": "hhdhdh",
      "PUNISHNUM": "xxxxvvvv",
      "PUNISHITEM": "私接盗用水",
      "PUNISHSTATE": "2",
      "SEQUENCENUM": "",
      "lat": "22.37684446911515",
      "lon": "113.31174951645835",
      "eventtype": "2",
      "pz": "1470902226800_0_0.jpg"???
    },


 * */
