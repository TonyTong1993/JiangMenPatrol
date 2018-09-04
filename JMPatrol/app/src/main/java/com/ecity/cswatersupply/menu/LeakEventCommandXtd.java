package com.ecity.cswatersupply.menu;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.event.EventType;

public class LeakEventCommandXtd extends AEventCommandXtd {

    @Override
    protected int getScreenTitleId() {
        return R.string.event_management_type_leak_tilte;
    }

    @Override
    protected EventType getEventType() {
        return EventType.POINT_LEAK;
    }
}
