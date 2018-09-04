package com.ecity.cswatersupply.project.network.request;

import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;

import java.util.List;
import java.util.Map;

/**
 * 江门工程上报用的参数基类。
 * @author jonathanma
 *
 */
public abstract class AProjectBaseSubmitParameter extends AReportInspectItemParameter {
    private List<InspectItem> items;

    public AProjectBaseSubmitParameter(List<InspectItem> items) {
        this.items = items;
    }

    @Override
    protected void fillSimpleFields(Map<String, String> map) {

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
