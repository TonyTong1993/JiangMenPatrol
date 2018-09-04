package com.ecity.cswatersupply.model.event;

import java.util.Map;

public class PointLeakageEvent extends Event {
    private static final long serialVersionUID = -4432367945621084864L;

    public static final String ATTR_KEY_DEVICE = "guanlianshebei";
    public static final String ATTR_KEY_LEAK_LEIXING = "loudianleixing";

    public PointLeakageEvent(Map<String, String> dataSource) {
        super(dataSource);
    }

    @Override
    public EventType getType() {
        return EventType.POINT_LEAK;
    }

    @Override
    public String getId() {
        return getAttribute(ATTR_KEY_ID);
    }

    @Override
    public String getReportTime() {
        return null;
    }
}
