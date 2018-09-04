package com.ecity.cswatersupply.network.request;

import java.util.List;
import java.util.Map;

import com.ecity.cswatersupply.model.checkitem.InspectItem;
/**
 * 
 * @author lotus
 *
 */
public class ReportTaskFormParameter extends AReportInspectItemParameter {
    private List<InspectItem> items;

    public ReportTaskFormParameter(List<InspectItem> items) {
        this.items = items;
    }

    @Override
    protected List<InspectItem> getInspectItems() {
        return items;
    }
    @Override
    protected String getInspectItemsKey() {
        return "properties";
    }

    @Override
    protected void fillSimpleFields(Map<String, String> map) {
        // no other fields need to report
    }
}
