package com.ecity.cswatersupply.workorder.presenter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.WorkOrderDispatchTableAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.FileUploader;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.task.IExecuteAfterTaskDo;
import com.ecity.cswatersupply.task.ReadCacheTask;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.ui.inpsectitem.ContactInspectItemViewXtd;
import com.ecity.cswatersupply.utils.CacheManager;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.FilesToZip;
import com.ecity.cswatersupply.utils.FilesToZip.EZipType;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.ecity.cswatersupply.utils.InspectItemUtil.ICacheInspectItemsCallback;
import com.ecity.cswatersupply.utils.JsonUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.workorder.model.ListModel;
import com.ecity.cswatersupply.workorder.model.UnFinishWorkOrderInfo;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.z3app.android.util.FileUtil;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.ecity.cswatersupply.R.string;

/**
 * 工单检查项操作类，使用时用intent传入以下数据： 1、当前工单(CustomReportActivity.REPORT_COMFROM);
 * 2、title名(CustomReportActivity.REPORT_TITLE); 3、cacheKey(KEY_CACHE);
 * 4、EventBus回调id(KEY_EVENTID). 如果需要可以传：
 * 1、底部按钮模式(CustomReportActivity.BOTTOM_TOOLBAR_MODE
 * ，可选值：CustomReportActivity.SINGLE_BTN/CustomReportActivity.TWO_BTNS)；
 * 2、底部按钮文字(单按钮：CustomReportActivity.BOTTOM_SINLEBTN_TXT；
 * 双按钮：CustomReportActivity
 * .BOTTOM_TWOBTN_NEGATIVE_TXT、CustomReportActivity.BOTTOM_TWOBTN_POSITIVE_TXT)；
 * 3、回到工单界面弹出的成功提示文字(KEY_REPORT_SUCCESS_MSG) 示例
 * {@AuditAsk4HelpWorkOrderCommandXtd}
 * 
 * @author gaokai
 */
public class WorkOrderOperator extends AWorkOrderOperator implements IExecuteAfterTaskDo {
    public static final String KEY_CACHE = "CACHE";
    public static final String KEY_EVENTID = "EVENTID";
    public static final String KEY_AUDIT = "AUDIT";
    public static final String KEY_SUB_WORK_FLOW = "goSubWorkFlow";
    public static final String KEY_REPORT_SUCCESS_MSG = "successMsg";

    private int eventId;
    private String isAgree;
    private String cacheKey;
    private String successMsg;
    private List<String> audioPaths;
    private List<String> imagesPaths;
    private boolean hasReported = false;
    private WorkOrder currentWorkOrder;
    private static final int READY_TO_SUBMIT = 0x00000;
    private WorkOrderReportHandler mHandler = new WorkOrderReportHandler(this);
    private List<InspectItem> inspectItems = Collections.synchronizedList(new ArrayList<InspectItem>());
    private JSONObject uploadFormResult;
//    private ListViewForScrollView tableList;
    private ListView tableList;
    private List<UnFinishWorkOrderInfo> infoList;
    private WorkOrderDispatchTableAdapter adapter;

    private IExecuteAfterTaskDo iExecuteAfterTaskDo = new IExecuteAfterTaskDo() {

        @Override
        public void executeTaskError() {
        }

        @Override
        public void executeOnTaskSuccess(Serializable result) {// Question: 这里是做什么？
            String cacheStr = (String) result;
            InspectItemUtil.restoreInspectItemsFromCache(cacheStr, inspectItems);
        }

        @Override
        public void executeOnTaskFinish() {
            getActivity().fillDatas(inspectItems);
            User currentUser = HostApplication.getApplication().getCurrentUser();

            //判断是否是工单分派界面
            String mTitle = getActivity().getCustomReportActivityTitle();
            LinearLayout container = getActivity().getLinearLayout();
            if(ResourceUtil.getStringById(R.string.title_workorder_dispatch).equals(mTitle)) {
                container.setVisibility(View.VISIBLE);
                LoadingDialogUtil.show(getActivity(), R.string.page_loading);
                WorkOrderService.instance.getUnfinishedWorkOrderInfos(currentUser.getId(), currentUser.getGroupId());
            } else {
                container.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public List<InspectItem> getDataSource() {
        EventBusUtil.register(this);
        this.cacheKey = getActivity().getIntent().getExtras().getString(KEY_CACHE);
        this.eventId = getActivity().getIntent().getExtras().getInt(KEY_EVENTID);
        int msgId = getActivity().getIntent().getExtras().getInt(KEY_REPORT_SUCCESS_MSG);
        this.successMsg = ResourceUtil.getStringById(msgId);
        currentWorkOrder = (WorkOrder) getActivity().getIntent().getExtras().getSerializable(WorkOrder.KEY_SERIAL);
        this.gid = currentWorkOrder.getAttribute(WorkOrder.KEY_ID);
        String currentWorkOrderButtonKey = getActivity().getIntent().getStringExtra(WorkOrder.INTENT_KEY_CLICKED_BUTTON);
        CustomViewInflater.setCurrentWorkOrderButtonKey(currentWorkOrderButtonKey);
        LoadingDialogUtil.show(getActivity(), ResourceUtil.getStringById(R.string.page_loading));
        // 审核跟主流程一个服务接口，但是参数不同
        if (getActivity().getIntent().getExtras().getBoolean(WorkOrderOperator.KEY_AUDIT, false)) {
            goWorkOrderAudit();
        }
        // 如果是分支节点（进度、完工、协助、转办、延期、退回等），要走分支流程
        else if (getActivity().getIntent().getExtras().getBoolean(WorkOrderOperator.KEY_SUB_WORK_FLOW, false)) {
            goWorkOrderSubWorkFlow();
        }
        // 主流程
        else {
            goWorkOrderMainWorkFlow();
        }
        return null;
    }

    private void goWorkOrderAudit() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("assignee", HostApplication.getApplication().getCurrentUser().getId());
        params.put(WorkOrder.KEY_ID, currentWorkOrder.getAttribute(WorkOrder.KEY_ID));
        params.put(WorkOrder.KEY_SUB_STATE, getActivity().getIntent().getExtras().getString(WorkOrder.KEY_SUB_STATE));
        WorkOrderService.instance.getMainWorkFlowFormData(params);
    }

    private void goWorkOrderMainWorkFlow() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(WorkOrder.KEY_ID, currentWorkOrder.getAttribute(WorkOrder.KEY_ID));
        WorkOrderService.instance.getMainWorkFlowFormData(params);
    }

    private void goWorkOrderSubWorkFlow() {
        String subState = getActivity().getIntent().getExtras().getString(WorkOrder.KEY_SUB_STATE);
        if (subState == null) {
            LoadingDialogUtil.dismiss();
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("state", subState);
        WorkOrderService.instance.getSubWorkFlowFormData(params);
    }

    @Override
    public void submit2Server(List<InspectItem> datas, boolean isAgree) {
        this.isAgree = isAgree == true ? "1" : "0";
        submit2Server(datas);
    }

    @Override
    public void submit2Server(List<InspectItem> datas) {
        LoadingDialogUtil.show(getActivity(), ResourceUtil.getStringById(string.please_wait));

        Iterator<InspectItem> iterator = datas.iterator();

        while (iterator.hasNext()) {
            InspectItem item = iterator.next();
            if (!item.isEdit()) {
                continue;
            }

            if (item.getType() == EInspectItemType.IMAGE && !StringUtil.isBlank(item.getValue())) {
                String[] strs = item.getValue().split(Constants.IMAGE_SPLIT_STRING);
                imagesPaths = Arrays.asList(strs);
                break;
            } else if (item.getType() == EInspectItemType.GEOMETRY && checkPosition(item)) {
                ToastUtil.showShort(string.not_in_right_position);
            } else if (item.getType() == EInspectItemType.AUDIO && !StringUtil.isBlank(item.getValue())) {
                String[] strs = item.getValue().split(",");
                audioPaths = Arrays.asList(strs);
            }
        }
        LoadingDialogUtil.show(getActivity(), string.uploading_now);
        LoadingDialogUtil.setCancelable(false);

        if (isMaintainSigned(uploadFormResult)) {
            doZipJob();
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        JSONObject json = null;
        String url = null;
        // 审核流程
        if (getActivity().getIntent().getExtras().getBoolean(WorkOrderOperator.KEY_AUDIT, false)) {
            json = buildAuditJson(datas);
            params.put("properties", json.toString());
            params.put("assignee", HostApplication.getApplication().getCurrentUser().getId());
            params.put(WorkOrder.KEY_ID, currentWorkOrder.getAttribute(WorkOrder.KEY_ID));
            url = ServiceUrlManager.getInstance().getSubmitFormDataUrl();
        }
        // 分支流程走专门服务
        else if (getActivity().getIntent().getExtras().getBoolean(WorkOrderOperator.KEY_SUB_WORK_FLOW, false)) {
            json = buildSubWorkFlowJson(datas);
            params.put("variables", json.toString());
            params.put("state", getActivity().getIntent().getExtras().getString(WorkOrder.KEY_SUB_STATE));
            url = ServiceUrlManager.getInstance().getStartSubWorkFlowUrl();
        }
        // 主流程
        else {
            json = buildMainWorkFlowJson(datas);
            params.put("properties", json.toString());
            params.put(WorkOrder.KEY_ID, currentWorkOrder.getAttribute(WorkOrder.KEY_ID));
            url = ServiceUrlManager.getInstance().getSubmitFormDataUrl();
        }
        WorkOrderService.instance.handleWorkOrder(url, ResponseEventStatus.WORKORDER_REPORT_TABLE, params);
    }

    private JSONObject buildAuditJson(List<InspectItem> datas) {
        JSONObject json = new JSONObject();
        try {
            json.putOpt(WorkOrder.KEY_SUB_STATE, getActivity().getIntent().getExtras().getString(WorkOrder.KEY_SUB_STATE));
            json.putOpt("isApproval", isAgree);
        } catch (JSONException e) {
            LogUtil.e("WorkOrderOperator", e);
        }
        putOptInspectItemData(json, datas);
        return json;
    }

    private boolean checkPosition(InspectItem item) {
        return false;
    }

    /**
     * 构造分支流程json
     */
    private JSONObject buildSubWorkFlowJson(List<InspectItem> datas) {
        JSONObject json = new JSONObject();
        try {
            json.putOpt(WorkOrder.KEY_MAIN_MAN, HostApplication.getApplication().getCurrentUser().getId());
            json.putOpt("groupmonitor", HostApplication.getApplication().getCurrentUser().getLeader());
            json.putOpt("workorderid", currentWorkOrder.getAttribute(WorkOrder.KEY_CODE));
            json.putOpt("parentid", currentWorkOrder.getAttribute(WorkOrder.KEY_ID));
        } catch (JSONException e) {
            LogUtil.e("WorkOrderOperator", e);
        }
        putOptInspectItemData(json, datas);
        return json;
    }

    /**
     * 构造主流程json
     */
    private JSONObject buildMainWorkFlowJson(List<InspectItem> datas) {
        JSONObject json = new JSONObject();
        try {
            json.putOpt("userid", HostApplication.getApplication().getCurrentUser().getId());
            json.putOpt(WorkOrder.KEY_STATE, currentWorkOrder.getAttribute(WorkOrder.KEY_STATE));
        } catch (JSONException e) {
            LogUtil.e("WorkOrderOperator", e);
        }
        putOptInspectItemData(json, datas);
        return json;
    }

    /**
     * 创建表单数据json
     */
    private void putOptInspectItemData(JSONObject json, List<InspectItem> datas) {
        for (InspectItem inspectItem : datas) {
            if (!inspectItem.isEdit()) {
                continue;
            }

            String key = inspectItem.getName();
            String value = "";
            if (inspectItem.getType() == EInspectItemType.IMAGE) {
                value = "PlaceHolderForImageItem"; // 对于图片类型的项，工作流需要将该项的key上传，值可以是任何非空的字符串
            } else if ((inspectItem.getType() == EInspectItemType.CONTACTMEN_MULTIPLE) || inspectItem.getType() == EInspectItemType.CONTACTMEN_SINGLE|| inspectItem.getType() == EInspectItemType.ORG || inspectItem.getType() == EInspectItemType.ORGM) {
                value = getContactValue(inspectItem);
            } else {
                value = inspectItem.getValue();
            }

            try {
                json.putOpt(key, value);
            } catch (JSONException e) {
                LogUtil.e(this, e);
            }
        }
    }

    public void notifyBackEvent(final CustomReportActivity1 activity) {
        final List<InspectItem> allInspectItems = InspectItemUtil.mergeAllItemsInline(inspectItems);
        if (!hasReported && InspectItemUtil.hasValue(allInspectItems)) {
            InspectItemUtil.confirmCacheItems(activity, allInspectItems, new ICacheInspectItemsCallback() {

                @Override
                public String getCacheKey() {
                    return getUniqueCacheKey();
                }

                @Override
                public void onCacheDone(boolean isSaved) {
                    FinishActivity(activity);
                }
            });
        } else {
            CacheManager.deleteCache(activity, getUniqueCacheKey());
            FinishActivity(activity);
        }
    }

    private void FinishActivity(CustomReportActivity1 activity) {
        LoadingDialogUtil.dismiss();
        EventBusUtil.unregister(this);
        activity.finish();
    }

    @SuppressLint("NewApi")
    private void handleInspectItems(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        if (event.getStatus() == ResponseEventStatus.ERROR) {
            setIExecutoAfterTaskDo(this);
            LoadingDialogUtil.updateMessage(ResourceUtil.getStringById(string.cache_loading));
            readCacheData();
            return;
        }

        this.inspectItems = event.getData();
        readCachedValue();
        // 把检查项缓存到本地
        ListModel<InspectItem> inspectItemList = new ListModel<InspectItem>();
        inspectItemList.setDatas(inspectItems);
        saveCacheData(inspectItemList);
    }

    /**
     * 读取缓存的页面信息
     */
    private void readCachedValue() {
        // 自建corePoolSize为5的线程池来调度线程任务
        // 避免大量AsyncTask造成的等待
        new ReadCacheTask(getActivity(), iExecuteAfterTaskDo).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getUniqueCacheKey());
    }

    private void afterReport(WorkOrderOperator operator, CustomReportActivity1 activity, Message msg) {
        LoadingDialogUtil.setCancelable(true);
        LoadingDialogUtil.dismiss();
        // 上报成功，删除页面缓存
        CacheManager.deleteCache(activity, getUniqueCacheKey());
        EventBusUtil.post(new UIEvent(operator.eventId, UIEventStatus.OK, operator.gid, successMsg, null));
        EventBusUtil.post(new UIEvent(UIEventStatus.WORKORDER_OPERATE_REPORT,currentWorkOrder.getAttributes().get(WorkOrder.KEY_ID)));
        operator.hasReported = true;
        operator.notifyBackEvent(activity);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void executeOnTaskSuccess(Serializable result) {
        ListModel<InspectItem> aModelList = (ListModel<InspectItem>) result;
        this.inspectItems = aModelList.getDatas() == null ? new ArrayList<InspectItem>() : aModelList.getDatas();
        readCachedValue();
    }

    @Override
    public void executeTaskError() {
        ToastUtil.showShort(string.read_cache_error);
    }

    @Override
    public void executeOnTaskFinish() {
        LoadingDialogUtil.dismiss();
    }

    @Override
    public String getCacheKey() {
        if (this.cacheKey == null) {
            this.cacheKey = Constants.INSPECT_DELAYAPPLY; // 默认延期申请页面布局
        }
        return this.cacheKey;
    }

    private String getUniqueCacheKey() {
        return HostApplication.getApplication().getCurrentUser().getGid() + "_" + gid + "_" + cacheKey;
    }

    private static class WorkOrderReportHandler extends Handler {
        private WeakReference<WorkOrderOperator> operator;

        public WorkOrderReportHandler(WorkOrderOperator operator) {
            this.operator = new WeakReference<WorkOrderOperator>(operator);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            WorkOrderOperator operator = this.operator.get();
            if (null == operator) {
                return;
            }
            CustomReportActivity1 activity = operator.getActivity();
            if (null == activity) {
                return;
            }
            switch (msg.what) {
                case READY_TO_SUBMIT:
                    operator.uploadAttachments(operator, (ArrayList<String>) msg.obj);
                    break;
                case FileUploader.UPLOAD_SINGLE_FILE_DONE:
                    File file = new File((String) msg.obj);
                    FileUtil.deleteFile(file);
                    break;
                case FileUploader.UPLOAD_ALL_FILES_DONE:
                    operator.afterReport(operator, activity, msg);
                    break;
                case FileUploader.UPLOAD_FAIL:
                    LoadingDialogUtil.setCancelable(true);
                    LoadingDialogUtil.dismiss();
                    operator.isMaintainSigned(operator.uploadFormResult);
                    Toast.makeText(activity, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }
        }
    }

    private void uploadAttachments(WorkOrderOperator operator, List<String> uploadPaths) {
        LoadingDialogUtil.updateMessage(string.workorder_report_images);
        Map<String, String> params = JsonUtil.json2Map(uploadFormResult);
        params.put("workorderid", currentWorkOrder.getAttribute(WorkOrder.KEY_ID));
        FileUploader.execute(uploadPaths, ServiceUrlManager.getInstance().getWorkOrderReportAttachmentsUrl(), params, operator.mHandler);
    }

    private class MyZipThread extends Thread {

        @Override
        public void run() {
            ArrayList<String> zipPaths = new ArrayList<String>(3);
            if (imagesPaths != null && imagesPaths.size() > 0) {
                zipPaths.add(FilesToZip.getInstance().zip(imagesPaths, "0"));
            }
            if (audioPaths != null && audioPaths.size() > 0) {
                FilesToZip.getInstance().setZipType(EZipType.AUDIO);
                zipPaths.add(FilesToZip.getInstance().zip(audioPaths, "1"));
            }
            FilesToZip.getInstance().setZipType(EZipType.IMAGE);
            Message msg = Message.obtain();
            if (zipPaths.size() > 0) {
                msg.what = READY_TO_SUBMIT;
                msg.obj = zipPaths;
            } else {
                msg.what = FileUploader.UPLOAD_ALL_FILES_DONE;
            }
            mHandler.sendMessage(msg);
        }
    }

    private void handleReportTableDone(ResponseEvent event) {
        if (event.getStatus() == ResponseEventStatus.ERROR) {
            ToastUtil.showShort(event.getMessage());
            LoadingDialogUtil.dismiss();
            return;
        }

        uploadFormResult = event.getData();
        doZipJob();
    }

    private void doZipJob() {
        new MyZipThread().start();
    }

    /**
     * Event bus begin
     */
    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.GET_WORKORDER_INSPECT_ITEMS:
                handleInspectItems(event);
                break;
            case ResponseEventStatus.WORKORDER_REPORT_TABLE:
                handleReportTableDone(event);
                break;
            case ResponseEventStatus.WORKORDER_GET_UNFINISHED_INFOS:
                handleGetUnfinishedInfos(event);
                break;
            default:
                break;
        }
    }

    private void handleGetUnfinishedInfos(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        if (event.getStatus() == ResponseEventStatus.ERROR) {
            ToastUtil.showShort(event.getMessage());
            return;
        }

        infoList = event.getData();
        if(ListUtil.isEmpty(infoList)) {
            ToastUtil.showShort(string.no_data);
            return;
        }
        List<UnFinishWorkOrderInfo> temp = new ArrayList<UnFinishWorkOrderInfo>();
        for(UnFinishWorkOrderInfo info : infoList) {
            if(info.getTodayWorkOrderNumber().equals("0") && info.getWaitProcessWorkOrderNumber().equals("0")) {
                temp.add(info);
            }
        }
        infoList.removeAll(temp);
        temp.clear();
        if(ListUtil.isEmpty(infoList)) {
            getActivity().getTableTitleLayout().setVisibility(View.GONE);
        } else {
            getActivity().getTableTitleLayout().setVisibility(View.VISIBLE);
        }
        tableList = getActivity().getListView();
        adapter = new WorkOrderDispatchTableAdapter(getActivity(), infoList);
        tableList.setAdapter(adapter);
    }

    /**
     * Event bus end
     */

    public static String getContactValue(InspectItem item) {
        if (item.getType() == EInspectItemType.CONTACTMEN_SINGLE || item.getType() == EInspectItemType.ORG) {
            return getSingleContactValue(item.getValue());
        }

        JSONArray jsonArray = JsonUtil.getJsonArray(item.getValue());
        if (jsonArray == null) {
            return "";
        }

        JSONArray values = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            String tmpValue = getSingleContactValue(jsonArray.optString(i));
            values.put(tmpValue);
        }

        return values.toString();
    }

    private static String getSingleContactValue(String oldValue) {
        String[] array = oldValue.split(ContactInspectItemViewXtd.CONTACT_USER_INFO_SEPARATOR);
        return (array.length > 1) ? array[0] : oldValue;
    }

    private boolean isMaintainSigned(JSONObject jsonObject) {
        if (null != jsonObject) {
            Map<String, String> params = JsonUtil.json2Map(jsonObject);
            Iterator i = params.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry entry = (java.util.Map.Entry) i.next();
                if (entry.getValue().equals("WORKORDER_MAINTAIN")) {
                    return true;
                }
            }
        }
        return false;
    }
}
