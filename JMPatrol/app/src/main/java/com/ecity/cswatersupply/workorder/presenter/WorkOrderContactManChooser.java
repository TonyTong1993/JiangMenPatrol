package com.ecity.cswatersupply.workorder.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ecity.android.contactmanchooser.AContactManCore;
import com.ecity.android.contactmanchooser.model.ContactMan;
import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.workorder.network.ContactManResponse;
import com.z3app.android.util.StringUtil;

/**
 * 获取工单联系人操作类
 * 
 * @author gaokai
 *
 */
public class WorkOrderContactManChooser extends AContactManCore {
    private static ArrayList<ContactMan> mContaceMen;
    /**
     * 默认从服务端加载可选项，用户从这些选项中选择。但是完工上报不从服务端加载，获取检查项时，服务已经返回了可选项。
     * 目前只有完工上报中，loadContactsFromServer为false。
     */
    private static boolean loadContactsFromServer = true;

    public static void setContactMen(ArrayList<ContactMan> mContaceMen) {
        WorkOrderContactManChooser.mContaceMen = mContaceMen;
    }

    @Override
    public List<ContactMan> getContactMen(Map<String, String> params,String requestType) {
        EventBusUtil.register(this);
        LoadingDialogUtil.show(getActivity(), R.string.please_wait);
        if (!StringUtil.isBlank(requestType)){
            WorkOrderService.instance.getEvent2TaskContactMan();
            return null;
        } else {
            if (loadContactsFromServer || ListUtil.isEmpty(mContaceMen)) {
                WorkOrderService.instance.getContactMan(params);
                return null;
            } else {
                LoadingDialogUtil.dismiss();
                return mContaceMen;
            }
        }
    }

    private void handleContactMan(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        if (event.getStatus() == ResponseEventStatus.ERROR) {
            getActivity().setErrorLayout();
        }
        ContactManResponse response = event.getData();
        getActivity().updateContactMenList(response.getPatrolManList());
        // 以后加缓存本地
    }

    @Override
    public void beforeFinishActivity() {
        EventBusUtil.unregister(this);
        LoadingDialogUtil.dismiss();
    }
    /**
     * Event bus begin
     */
    public void onEventMainThread(ResponseEvent event) {
        if (getActivity().isDestroyed()) {
            return;
        }
        switch (event.getId()) {
        case ResponseEventStatus.WORKORDER_GET_CONTACT_MAN:
            handleContactMan(event);
            break;
        }
    }

    public static boolean isLoadContactsFromServer() {
        return loadContactsFromServer;
    }

    public static void setLoadContactsFromServer(boolean loadContactsFromServer) {
        WorkOrderContactManChooser.loadContactsFromServer = loadContactsFromServer;
    }
}
