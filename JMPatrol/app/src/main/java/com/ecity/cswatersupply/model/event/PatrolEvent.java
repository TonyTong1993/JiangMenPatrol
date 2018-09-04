package com.ecity.cswatersupply.model.event;

import java.util.Map;

/***
 * Created by MaoShouBei on 2017/5/23.
 */

public class PatrolEvent extends Event {
    public PatrolEvent(Map<String, String> dataSource) {
        super(dataSource);
    }

    @Override
    public String getId() {
        return getAttribute(ATTR_KEY_ID);
    }

    @Override
    public String getReportTime() {
        return getAttribute(ATTR_KEY_REPORT_TIME);
    }

    @Override
    public EventType getType() {
        return EventType.PATROL;
    }
}
