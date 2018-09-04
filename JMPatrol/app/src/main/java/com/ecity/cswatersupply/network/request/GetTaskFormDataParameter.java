package com.ecity.cswatersupply.network.request;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.workorder.model.WorkOrderBtnModel;

import java.util.HashMap;
import java.util.Map;

public class GetTaskFormDataParameter implements IRequestParameter {
    private WorkOrderBtnModel taskBtn;


    public GetTaskFormDataParameter(WorkOrderBtnModel taskBtn) {
        this.taskBtn = taskBtn;
    }

    @Override
    public Map<String, String> toMap() {
        User currentUser = HostApplication.getApplication().getCurrentUser();
        Map<String, String> map = new HashMap<String, String>();

        map.put("taskId", taskBtn.getTaskId());
        map.put("taskType", taskBtn.getTaskType());
        map.put("taskCode", taskBtn.getTaskCode());
        map.put("userid", currentUser.getId());

        return map;
    }

}
