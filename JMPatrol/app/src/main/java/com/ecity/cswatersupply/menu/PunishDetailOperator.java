package com.ecity.cswatersupply.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.response.eventreport.EventReportResponse;
import com.ecity.cswatersupply.service.ReportEventService;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;

public class PunishDetailOperator extends ACommonReportOperator1 {

    protected Map<String, String> getFunctionKey() {
        Map<String, String> map = new HashMap<String, String>();
        int eventType = getActivity().getIntent().getExtras().getInt(CustomViewInflater.EVENTTYPE);
        String eventId = getActivity().getIntent().getExtras().getString(CustomViewInflater.EVENTID);
        map.put("eventtype", String.valueOf(eventType));
        map.put("eventid", eventId);
        return map;
    }

    @Override
    public void notifyBackEvent(CustomReportActivity1 activity) {
        if (null != activity) {
            EventBusUtil.unregister(this);
            activity.finish();
        }
    }

    public List<InspectItem> getDataSource() {
        EventBusUtil.register(this);
        LoadingDialogUtil.show(getActivity(), R.string.event_report_get_params);
        ReportEventService.getInstance().getPunishmentDetails(String.valueOf(getFunctionKey().get("eventid")), String.valueOf(getFunctionKey().get("eventtype")));
        return null;
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.PUNISHMENT_DETAILS:
                LoadingDialogUtil.dismiss();
                EventReportResponse response = event.getData();
                List<InspectItem> items = response.getItems();
                getActivity().fillDatas(items);
                break;
            default:
                break;
        }
    }
}
