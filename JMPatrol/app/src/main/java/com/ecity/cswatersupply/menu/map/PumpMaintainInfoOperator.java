package com.ecity.cswatersupply.menu.map;

import android.os.Bundle;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.menu.ACommonReportOperator1;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.response.eventreport.EventReportResponse;
import com.ecity.cswatersupply.service.ReportEventService;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrder;

import java.util.List;

/***
 * Created by MaoShouBei on 2017/5/19.
 */

public class PumpMaintainInfoOperator extends ACommonReportOperator1 {
    private String processInstanceId;

    @Override
    public List<InspectItem> getDataSource() {
        getDataFromIntent();
        requestDataSource();
        return null;
    }

    @Override
    public void notifyBackEvent(CustomReportActivity1 activity) {
        if (null != activity) {
            EventBusUtil.unregister(this);
            activity.finish();
        }
    }

    private void getDataFromIntent() {
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        processInstanceId = bundle.getString(WorkOrder.KEY_ID);
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.EVENT_GET_PUMP_MAINTAIN_INSPECT_ITEM:
                LoadingDialogUtil.dismiss();
                EventReportResponse response = event.getData();
                List<InspectItem> items = response.getItems();
                setRadioTextItemUnEditable(items);
                getActivity().fillDatas(items);
                break;
            default:
                break;
        }
    }

    private void requestDataSource() {
        EventBusUtil.register(this);
        LoadingDialogUtil.show(getActivity(), R.string.event_report_get_params);
        String url = ServiceUrlManager.getInstance().getFormContent();
        ReportEventService.getInstance().getPumpMaintainInspectItemData(url, processInstanceId);
    }

    private void setRadioTextItemUnEditable(List<InspectItem> items) {
        for (InspectItem item : items) {
            if (item.getType() == EInspectItemType.GROUP) {
                setRadioTextItemUnEditable(item.getChilds());
            } else {
                if (item.getType() == EInspectItemType.RADIOTXT && item.isEdit()) {
                    item.setEdit(false);
                }
            }
        }
    }
}
