package com.ecity.cswatersupply.menu;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.event.EventType;

public class PumpEventCommandXtd extends AEventCommandXtd {

    @Override
    protected int getScreenTitleId() {
        return R.string.event_management_type_pump_tilte;
    }

    @Override
    protected EventType getEventType() {
        return EventType.PUMP;
    }
}
