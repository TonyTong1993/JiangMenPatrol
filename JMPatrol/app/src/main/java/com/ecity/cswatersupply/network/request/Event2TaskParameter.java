package com.ecity.cswatersupply.network.request;

import java.util.List;
import java.util.Map;

import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.event.Event;

public class Event2TaskParameter extends AReportInspectItemParameter {
    private User reporter;
    private Event event;
    private List<InspectItem> items;

    public Event2TaskParameter(User reporter, Event event, List<InspectItem> items) {
        super();
        this.reporter = reporter;
        this.event = event;
        this.items = items;
    }

    @Override
    protected void fillSimpleFields(Map<String, String> map) {
        map.put("eventid", event.getId());
        map.put("createrid", reporter.getGid());
    }

    @Override
    protected String getInspectItemsKey() {
        return "properties";
    }

    @Override
    protected List<InspectItem> getInspectItems() {
        return items;
    }
}
