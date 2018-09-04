package com.ecity.cswatersupply.workorder.menu;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.menu.AMenuCommand;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderState;

/**
 * 取消延期、取消协助、取消转办和取消退单的操作的基类
 * @author jonathanma
 *
 */
public abstract class ACancelSubProcessWorkOrderCommandXtd extends AMenuCommand {

    /**
     * 获取网络请求ID
     * @return 网络请求ID
     */
    protected abstract int getRequestId();

    /**
     * 获取工单的状态。如“延期申请”、“转办申请”。
     * @return 工单状态。如“延期申请”、“转办申请”。
     */
    protected abstract WorkOrderState getWorkOrderSubState();

    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean executeWithExtra(View v, Bundle data) {
        if (data == null) {
            return false;
        }

        WorkOrder currentWorkOrder = (WorkOrder) data.get(WorkOrder.KEY_SERIAL);
        Map<String, String> params = new HashMap<String, String>();
        params.put("assignee", HostApplication.getApplication().getCurrentUser().getGid());
        params.put("processInstanceId", currentWorkOrder.getAttributes().get(WorkOrder.KEY_ID));
        JSONObject json = new JSONObject();
        try {
            json.putOpt(WorkOrder.KEY_SUB_STATE, getWorkOrderSubState().value);
        } catch (JSONException e) {
            LogUtil.e("ACancelSubProcessWorkOrderCommandXtd", e);
        }
        params.put("variables", json.toString());
        WorkOrderService.instance.handleWorkOrder(ServiceUrlManager.getInstance().getCancelWorkOrderSubProcessApplicationUrl(), getRequestId(), params);
        
        return true;
    }

    @Override
    public boolean execute() {
        return false;
    }
}
