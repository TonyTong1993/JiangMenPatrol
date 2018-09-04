package com.ecity.cswatersupply.model.event;

import java.util.Map;

public class RepairementEvent extends Event {
    private static final long serialVersionUID = -9029735983565980197L;

    public static final String ATTR_KEY_SELECTED_TYPE = "shijianleixing";

    public RepairementEvent(Map<String, String> dataSource) {
        super(dataSource);
    }

    @Override
    public EventType getType() {
        return EventType.REPAIRE;
    }

    @Override
    public String getId() {
        return getAttribute(ATTR_KEY_ID);
    }

    @Override
    public String getReportTime() {
        return getAttribute(ATTR_KEY_REPORT_TIME);
    }
}
