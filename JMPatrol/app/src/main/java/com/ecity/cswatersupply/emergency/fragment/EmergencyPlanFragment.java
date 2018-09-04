package com.ecity.cswatersupply.emergency.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.emergency.model.EmergencyPlanModel;
import com.ecity.cswatersupply.emergency.network.request.GetQBODatasParameter;
import com.ecity.cswatersupply.emergency.service.KnowledgeBaseNetwork;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;

public class EmergencyPlanFragment extends DownloadBaseFragment {
    private List<EmergencyPlanModel> data;
    private int type;
    private int eventid;
    private boolean isInQuery;

    public EmergencyPlanFragment() {
    }

    public EmergencyPlanFragment(int type, int eventid) {
        this.eventid = eventid;
        this.type = type;
    }

    @Override
    public void onResume() {
        super.onResume();
        isInQuery = true;
        KnowledgeBaseNetwork.getInstance().getEmergencyeRsponse(new GetQBODatasParameter(type));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtil.register(this);
    }

    @Override
    public void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    /**
     * EventBus methods begin.
     */
    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }
        if (eventid == event.getId() && isInQuery) {
            fillData(event);
            isInQuery = false;
        }
    }

    private void fillData(ResponseEvent event) {
        List<EmergencyPlanModel> newData = event.getData();
        if (null == newData || newData.size() < 1) {
            return;
        }

        data = new ArrayList<EmergencyPlanModel>();
        for (int i = 0; i < newData.size(); i++) {
            EmergencyPlanModel tmp = newData.get(i);
            data.add(tmp);
        }

        updateDataList(data);
    }

    public void onEventMainThread(UIEvent event) {
        switch (event.getId()) {
            case UIEventStatus.TOAST:
                if (event.isForTarget(this)) {
                    ToastUtil.showShort(event.getMessage());
                }
                break;
            default:
                break;
        }
    }
    /**
     * EventBus methods end.
     */

}
