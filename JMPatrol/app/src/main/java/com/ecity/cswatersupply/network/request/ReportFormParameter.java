package com.ecity.cswatersupply.network.request;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.workorder.model.WorkOrderBtnModel;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportFormParameter extends AReportInspectItemParameter {
    private List<InspectItem> items;
    private WorkOrderBtnModel taskBtn;

    public ReportFormParameter(List<InspectItem> items,WorkOrderBtnModel taskBtn) {
        this.items = items;
        this.taskBtn = taskBtn;
    }

    @Override
    protected void fillSimpleFields(Map<String, String> map) {

        User currentUser = HostApplication.getApplication().getCurrentUser();
        map.put("taskId", taskBtn.getTaskId());
        map.put("taskType", taskBtn.getTaskType());
        map.put("taskCode", taskBtn.getTaskCode());
        map.put("properties", getInspectItemsKey());
        map.put("userid", currentUser.getId());
        map.put("user", currentUser.getTrueName());
    }

    @Override
    protected String getInspectItemsKey() {
        List<InspectItem> inspectItems = getInspectItems();
        Map<String, String> parameterMap = new HashMap<String, String>();
        for (int i = 0; i < inspectItems.size(); i++) {
            if (inspectItems.get(i).getType().name().equalsIgnoreCase(EInspectItemType.IMAGE.name())) {
                continue;
            }
            if (inspectItems.get(i).getType().name().equalsIgnoreCase(EInspectItemType.AUDIO.name())) {
                continue;
            }
            if (inspectItems.get(i).getType().name().equalsIgnoreCase(EInspectItemType.VIDEO.name())) {
                continue;
            }
            if (items.get(i).getType().name().equalsIgnoreCase(EInspectItemType.GEOMETRY.name())) {
                String[] loaction = items.get(i).getValue().split(";");
                parameterMap.put(inspectItems.get(i).getName(), loaction[1]);
                continue;
            }
            parameterMap.put(inspectItems.get(i).getName(), inspectItems.get(i).getValue());
        }
        Gson gson = new Gson();
        String parameters = gson.toJson(parameterMap);
        return parameters;
    }

    @Override
    protected List<InspectItem> getInspectItems() {
        return items;
    }
}
