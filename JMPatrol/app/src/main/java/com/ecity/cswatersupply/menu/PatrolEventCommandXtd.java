package com.ecity.cswatersupply.menu;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.event.EventType;

/***
 * Created by MaoShouBei on 2017/5/16.
 */

public class PatrolEventCommandXtd extends AEventCommandXtd {
    @Override
    protected int getScreenTitleId() {
        return R.string.event_management_type_patrol_tilte;
    }

    @Override
    protected EventType getEventType() {
        return EventType.PATROL;
    }
}
