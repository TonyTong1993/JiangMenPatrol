package com.ecity.cswatersupply.network.request;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.event.Event;

public class Event2WorkOrderParameter extends AReportInspectItemParameter {
    private User reporter;
    private Event event;
    private List<InspectItem> items;

    public Event2WorkOrderParameter(User reporter, Event event, List<InspectItem> items) {
        super();
        this.reporter = reporter;
        this.event = event;
        this.items = items;
    }

    @Override
    protected void fillSimpleFields(Map<String, String> map) {
        JSONObject userInfo = new JSONObject();
        try {
            userInfo.put("groupId", reporter.getGroupId());
            userInfo.put("gid", reporter.getGid());
            userInfo.put("username",reporter.getLoginName());
        } catch (JSONException e) {
            LogUtil.e(this, e);
        }
        map.put("eventId", event.getId());
        map.put("reporttime", event.getReportTime());
        map.put("reporter", reporter.getGid());
        map.put("userInfo", userInfo.toString());
        map.put("processDefinitionKey", "WorkOrder");
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
