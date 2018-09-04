package com.ecity.cswatersupply.network.response.eventreport;

import java.util.List;

import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.response.AServerResponse;
import com.google.gson.annotations.SerializedName;

public class EventReportResponse extends AServerResponse {
    private String tableName;
    @SerializedName("params")
    private List<InspectItem> inspectItems;

    public List<InspectItem> getItems() {
        return inspectItems;
    }

    public void setInspectItems(List<InspectItem> inspectItems) {
        this.inspectItems = inspectItems;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
