
package com.ecity.cswatersupply.emergency.menu;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.emergency.network.request.ReportUnUsualFormParameter;
import com.ecity.cswatersupply.emergency.service.EmergencyService;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.menu.EventReportOperator1;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;
import com.ecity.cswatersupply.network.response.eventreport.EventReportResponse;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.task.IExecuteAfterTaskDo;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;

/**
 * 异常速报
 * @author Gxx 2016-12-07
 */
public class UnUsualReportOperater extends EventReportOperator1 {
    public static final String EARTHQUAKEID = "EARTHQUAKEID";
    private List<InspectItem> mInspectItems;
    private String tableCode;
    private IExecuteAfterTaskDo iExecuteAfterTaskDo;
    private String gid = "";

    @Override
    public List<InspectItem> getDataSource() {
        return super.getDataSource();
    }

    @Override
    public void submit2Server(List<InspectItem> mInspectItems) {
        super.submit2Server(mInspectItems);
    }

    @Override
    public void notifyBackEvent(final CustomReportActivity1 activity) {
        super.notifyBackEvent(activity);
    }

    @Override
    protected String getServiceUrl() {
        return ServiceUrlManager.getInstance().getUnUsualReportFormEventUrl();
    }

    @Override
    protected String getReportImageUrl() {
        return ServiceUrlManager.getInstance().getUnUsualReportFileUrl();
    }

    @Override
    protected AReportInspectItemParameter getRequestParameter() {
        ReportUnUsualFormParameter parameter = new ReportUnUsualFormParameter(mInspectItems);

        if(getActivity().getIntent().hasExtra(EARTHQUAKEID)){
            String eqid = getActivity().getIntent().getStringExtra(EARTHQUAKEID);
            parameter.setEqid(eqid);
        }
        parameter.setCode(getFunctionKey());
        return parameter;
    }

    @Override
    protected String getFunctionKey() {
        tableCode = getActivity().getIntent().getExtras().getString(CustomViewInflater.EVENTTYPE);
        return tableCode;
    }

    @Override
    protected void requestDataSource() {
        EmergencyService.getInstance().getUnUsualReportForm(getFunctionKey());
    }

    @Override
    protected int getRequestId() {
        return ResponseEventStatus.EMERGENCY_GET_UNUSUAL_REPORT_FORM;
    }

    @Override
    protected String getUniqueCacheKey() {
        return HostApplication.getApplication().getCurrentUser().getGid() + tableCode;
    }

    @Override
    public String parserMediaGID(JSONObject jsonObject){
        if(null == jsonObject){
            return null;
        }
        gid = jsonObject.optString("gid");
        return gid;
    }

    @Override
    protected void fillFileUploadSimpleParameter(Map<String, String> param){
        if(null == param){
            return;
        }
        param.put("code", getFunctionKey());
        param.put("gid", gid);
    }

    public void onEventMainThread(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        if (!event.isOK()) {
            ToastUtil.showLong(event.getMessage());
            return;
        }

        if (event.getId() == getRequestId()) {
            handleGetUnUsualReportForm(event);
        }
    }

    private void handleGetUnUsualReportForm(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        EventReportResponse response = event.getData();
        mInspectItems = response.getItems();
        iExecuteAfterTaskDo = new EventReportExecuteAfterTaskDo(mInspectItems);
        readCachedValue(iExecuteAfterTaskDo);
    }
}
