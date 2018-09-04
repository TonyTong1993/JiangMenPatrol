package com.ecity.cswatersupply.workorder.presenter;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrderBtnModel;

import java.util.List;

/**
 * Created by Gxx on 2017/4/12.
 */

public class WorkOrderDataSourceProvider extends AFromDataSourceProvider{
    public  WorkOrderBtnModel taskBtn;
    private List<InspectItem> mInspectItems;

    @Override
    public void requestDataSource() {
        EventBusUtil.register(this);
        LoadingDialogUtil.show(getActivity(), R.string.event_report_get_params);
        WorkOrderService.instance.getTaskFormData(taskBtn);
    }

    public void setTaskBtn(WorkOrderBtnModel taskBtn) {
        this.taskBtn = taskBtn;
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        if (ResponseEventStatus.FORM_GET_FORM_INSPECTITEMS == event.getId()) {
            handleWorkOrderInspectItems(event);
        }
    }

    private void handleWorkOrderInspectItems(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        mInspectItems = event.getData();
        EventBusUtil.unregister(this);

        UIEvent uiEvent = new UIEvent(UIEventStatus.GET_FORM_INSPECTITEM, mInspectItems);
        EventBusUtil.post(uiEvent);
    }

}
