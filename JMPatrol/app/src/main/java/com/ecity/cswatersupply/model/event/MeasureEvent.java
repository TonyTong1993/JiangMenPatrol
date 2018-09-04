package com.ecity.cswatersupply.model.event;

import java.util.Map;

public class MeasureEvent extends Event {
    private static final long serialVersionUID = -3393456062159888943L;
    public static final String ATTR_KEY_MEASURE_TYPE = "measuetype";
    public static final String ATTR_KEY_AUDIO = "luyinxinxi";

    public MeasureEvent(Map<String, String> dataSource) {
        super(dataSource);
    }

    @Override
    public EventType getType() {
        return EventType.CONSTRUCTION;
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
