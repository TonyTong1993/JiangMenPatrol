package com.ecity.cswatersupply.model.event;

import java.util.Map;

public class PumpEvent extends Event {
    private static final long serialVersionUID = -3393456062159888943L;
    public static final String ATTR_KEY_PUMP_NAME = "bengfangmingcheng";
    public static final String ATTR_KEY_PUMP_TYPE = "shijianleixing";
    public static final String ATTR_KEY_AUDIO = "luyinxinxi";

    public PumpEvent(Map<String, String> dataSource) {
        super(dataSource);
    }

    @Override
    public EventType getType() {
        return EventType.PUMP;
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
