package com.ecity.cswatersupply.menu;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.event.EventType;

public class PunishmentReportCommandXtd extends AEventCommandXtd {

    @Override
    protected int getScreenTitleId() {
        return R.string.punishment_report_title;
    }

    @Override
    protected EventType getEventType() {
        return EventType.PUNISHMENT;
    }
}
