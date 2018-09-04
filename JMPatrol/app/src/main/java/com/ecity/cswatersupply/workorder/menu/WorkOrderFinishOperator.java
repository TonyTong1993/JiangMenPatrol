package com.ecity.cswatersupply.workorder.menu;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.FileUploader;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.task.IExecuteAfterTaskDo;
import com.ecity.cswatersupply.task.ReadCacheTask;
import com.ecity.cswatersupply.ui.activities.WorkOrderDetailFragmentActivity;
import com.ecity.cswatersupply.ui.inpsectitem.ContactInspectItemViewXtd;
import com.ecity.cswatersupply.ui.inpsectitem.PumpInspectItemViewXtd;
import com.ecity.cswatersupply.utils.CacheManager;
import com.ecity.cswatersupply.utils.FilesToZip;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.ecity.cswatersupply.utils.InspectItemUtil.ICacheInspectItemsCallback;
import com.ecity.cswatersupply.utils.JsonUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.WorkOrderFinishActivity;
import com.ecity.cswatersupply.workorder.fragment.WorkOrderFinishFragmentMaterielInfo;
import com.ecity.cswatersupply.workorder.model.ListModel;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.z3app.android.util.FileUtil;
import com.z3app.android.util.StringUtil;

public class WorkOrderFinishOperator extends AWorkOrderFinishOperator implements IExecuteAfterTaskDo {
    public static final String KEY_CACHE = "CACHE";
    public static final String KEY_EVENTID = "EVENTID";
    public static final String KEY_AUDIT = "AUDIT";
    public static final String KEY_REPORT_SUCCESS_MSG = "successMsg";

    private int eventId;
    private static WorkOrder currentWorkOrder;
    private static final int READY_TO_SUBMIT = 0x00000;

    private String cacheKey;
    private String successMsg;
    private String mZipImagesPath;
    private List<String> imagesPaths;
    private boolean hasReported = false;
    private WorkOrderFinishReportHandler mHandler = new WorkOrderFinishReportHandler(this);
    private List<InspectItem> inspectItems = Collections.synchronizedList(new ArrayList<InspectItem>());
    private JSONObject uploadFormResult;

    private IExecuteAfterTaskDo iExecuteAfterTaskDo = new IExecuteAfterTaskDo() {

        @Override
        public void executeTaskError() {
        }

        @Override
        public void executeOnTaskSuccess(Serializable result) {
            String cacheStr = (String) result;
            InspectItemUtil.restoreInspectItemsFromCache(cacheStr, inspectItems);
        }

        @Override
        public void executeOnTaskFinish() {
            if(ListUtil.isEmpty(inspectItems) || ListUtil.isEmpty(inspectItems.get(0).getChilds())) {
                LoadingDialogUtil.dismiss();
                return;
            }
            InspectItem inspectItem = inspectItems.get(0).getChilds().get(0);
            filiterUnVisibleInspectItem(inspectItem);
            LoadingDialogUtil.dismiss();
        }
    };

    @Override
    public List<InspectItem> getDataSource() {
        EventBusUtil.register(this);
        this.cacheKey = getActivity().getIntent().getExtras().getString(KEY_CACHE);
        this.eventId = (Integer) getActivity().getIntent().getExtras().getSerializable(KEY_EVENTID);
        int msgId = getActivity().getIntent().getExtras().getInt(KEY_REPORT_SUCCESS_MSG);
        this.successMsg = ResourceUtil.getStringById(msgId);
        currentWorkOrder = (WorkOrder) getActivity().getIntent().getExtras().getSerializable(WorkOrder.KEY_SERIAL);
        this.gid = currentWorkOrder.getAttributes().get(WorkOrder.KEY_ID);
        LoadingDialogUtil.show(getActivity(), ResourceUtil.getStringById(R.string.page_loading));

        goWorkOrderMainWorkFlow();

        return null;
    }

    private void goWorkOrderMainWorkFlow() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(WorkOrder.KEY_ID, currentWorkOrder.getAttributes().get(WorkOrder.KEY_ID));
        WorkOrderService.instance.getMainWorkFlowFormData(params);
    }

    @Override
    public void submit2Server(List<InspectItem> datas) {
        List<InspectItem> allInfos = InspectItemUtil.mergeAllItemsInline(datas);

        if (InspectItemUtil.hasEmptyItem(allInfos)) {
            Toast.makeText(getActivity(), R.string.is_null_prompt, Toast.LENGTH_SHORT).show();
            return;
        }

        LoadingDialogUtil.show(getActivity(), ResourceUtil.getStringById(R.string.please_wait));
        for (InspectItem item : allInfos) {
            if (item.getType() == EInspectItemType.IMAGE && !StringUtil.isBlank(item.getValue())) {
                String[] strs = item.getValue().split(Constants.IMAGE_SPLIT_STRING);
                imagesPaths = Arrays.asList(strs);
                break;
            } else if (item.getType() == EInspectItemType.GEOMETRY && checkPosition(item)) {
                ToastUtil.showShort(R.string.not_in_right_position);
            }
        }
        LoadingDialogUtil.show(getActivity(), R.string.uploading_now);
        LoadingDialogUtil.setCancelable(false);

        Map<String, String> params = new HashMap<String, String>();
        JSONObject json = buildInspectItemJson(allInfos);
        params.put("properties", json.toString());
        params.put(WorkOrder.KEY_ID, currentWorkOrder.getAttribute(WorkOrder.KEY_ID));
        String url = ServiceUrlManager.getInstance().submitWorkOrderInspectItemsUrl();

        WorkOrderService.instance.handleWorkOrder(url, ResponseEventStatus.WORKORDER_REPORT_TABLE, params);
    }

    private boolean checkPosition(InspectItem item) {
        return false;
    }

    private JSONObject buildInspectItemJson(List<InspectItem> datas) {
        JSONObject json = new JSONObject();
        try {
            json.putOpt("userid", HostApplication.getApplication().getCurrentUser().getId());
            json.putOpt(WorkOrder.KEY_STATE, currentWorkOrder.getAttributes().get(WorkOrder.KEY_STATE));
        } catch (JSONException e) {
            LogUtil.e(this, e);
        }

        for (InspectItem inspectItem : datas) {
            if (!inspectItem.isEdit() || !inspectItem.isVisible()) {
                continue;
            }

            String key = inspectItem.getName();
            String value = "";
            if (inspectItem.getType() == EInspectItemType.IMAGE) {
                value = "PlaceHolderForImageItem"; // 对于图片类型的项，工作流需要将该项的key上传，值可以是任何非空的字符串
            } else if ((inspectItem.getType() == EInspectItemType.CONTACTMEN_MULTIPLE) || inspectItem.getType() == EInspectItemType.CONTACTMEN_SINGLE) {
                value = getContactValue(inspectItem);
            } else if (inspectItem.getType() == EInspectItemType.NAMEGEOM) {
                String[] pumpValues = PumpInspectItemViewXtd.constructPumpParams(json, inspectItem.getValue());
                if(null != pumpValues && pumpValues.length > 0 ) {
                    value =pumpValues[0];
                }
            } else {
                value = inspectItem.getValue();
            }

            try {
                json.putOpt(key, value);
            } catch (JSONException e) {
                LogUtil.e(this, e);
            }
        }

        return json;
    }

    @Override
    public void notifyBackEvent(final WorkOrderFinishActivity activity) {
        WorkOrderFinishFragmentMaterielInfo.getInstance().addMaterialJsonToInspectItems();

        final List<InspectItem> allInspectItems = InspectItemUtil.mergeAllItemsInline(inspectItems);
        final String uniqueCacheKey = gid + cacheKey;
        if (!hasReported && InspectItemUtil.hasValue(allInspectItems)) {
            InspectItemUtil.confirmCacheItems(activity, allInspectItems, new ICacheInspectItemsCallback() {

                @Override
                public String getCacheKey() {
                    return uniqueCacheKey;
                }

                @Override
                public void onCacheDone(boolean isSaved) {
                    finishActivity(activity);
                }
            });
        } else {
            CacheManager.deleteCache(activity, uniqueCacheKey);
            finishActivity(activity);
        }
    }

    private void finishActivity(WorkOrderFinishActivity activity) {
        LoadingDialogUtil.dismiss();
        EventBusUtil.unregister(this);
        activity.finish();
    }

    @SuppressLint("NewApi")
    private void handleInspectItems(ResponseEvent event) {
        if (event.getStatus() == ResponseEventStatus.ERROR) {
            // 网络获取出错时，从缓存读取
            if (getActivity().isDestroyed()) {
                return;
            }
            setIExecutoAfterTaskDo(this);
            LoadingDialogUtil.updateMessage(ResourceUtil.getStringById(R.string.cache_loading));
            readCacheData();
            return;
        }
        this.inspectItems = event.getData();
        readCachedValue();
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
        new ReadCacheTask(getActivity(), iExecuteAfterTaskDo).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, gid + cacheKey);
    }

    private void afterReport(WorkOrderFinishOperator operator, WorkOrderFinishActivity activity, Message msg) {
        LoadingDialogUtil.setCancelable(true);
        LoadingDialogUtil.dismiss();
        if (msg != null) {
            Toast.makeText(activity, (String) msg.obj, Toast.LENGTH_SHORT).show();
        }

        EventBusUtil.post(new UIEvent(operator.eventId, UIEventStatus.OK, operator.gid, successMsg, null));
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
        ToastUtil.showShort(R.string.read_cache_error);
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

    private static class WorkOrderFinishReportHandler extends Handler {
        private WeakReference<WorkOrderFinishOperator> operator;

        public WorkOrderFinishReportHandler(WorkOrderFinishOperator operator) {
            this.operator = new WeakReference<WorkOrderFinishOperator>(operator);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            WorkOrderFinishOperator operator = this.operator.get();
            if (null == operator) {
                return;
            }
            WorkOrderFinishActivity activity = operator.getActivity();
            if (null == activity) {
                return;
            }
            switch (msg.what) {
                case READY_TO_SUBMIT:
                    operator.uploadImages(operator);
                    break;
                case FileUploader.UPLOAD_SINGLE_FILE_DONE:
                    File file = new File((String) msg.obj);
                    FileUtil.deleteFile(file);
                    break;
                case FileUploader.UPLOAD_ALL_FILES_DONE:
                    if (null != activity) {
                        operator.afterReport(operator, activity, msg);
                    }
                    break;
                case FileUploader.UPLOAD_FAIL:
                    if (null != activity) {
                        LoadingDialogUtil.setCancelable(true);
                        LoadingDialogUtil.dismiss();
                        Toast.makeText(activity, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;

            }
        }
    }

    private void uploadImages(WorkOrderFinishOperator operator) {
        if (!StringUtil.isEmpty(operator.mZipImagesPath)) {
            LoadingDialogUtil.updateMessage(R.string.workorder_report_images);
            List<String> uploadPaths = new ArrayList<String>();
            uploadPaths.add(operator.mZipImagesPath);
            Map<String, String> params = JsonUtil.json2Map(uploadFormResult);
            params.put("workorderid", currentWorkOrder.getAttribute(WorkOrder.KEY_ID));
            FileUploader.execute(uploadPaths, ServiceUrlManager.getInstance().getWorkOrderReportAttachmentsUrl(), params, operator.mHandler);
        } else {
            operator.afterReport(operator, operator.getActivity(), null);
        }
    }

    private class MyZipThread extends Thread {
        private List<String> filePaths;
        private String id;

        public MyZipThread(List<String> filePaths, String id) {
            this.filePaths = filePaths;
            this.id = id;
        }

        @Override
        public void run() {
            mZipImagesPath = FilesToZip.getInstance().zip(filePaths, id);
            Message msg = Message.obtain();
            msg.what = READY_TO_SUBMIT;
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
        if (imagesPaths != null) {
            new MyZipThread(imagesPaths, "").start();
        } else {
            afterReport(this, getActivity(), null);
        }
    }

    @Override
    public void onItemClicked(String itemName, EInspectItemType eInspectItemType, Bundle data) {
        if (eInspectItemType == EInspectItemType.WORKORDER_CODE) {
            Bundle workOrder = new Bundle();
            workOrder.putSerializable(WorkOrder.KEY_SERIAL, currentWorkOrder);
            UIHelper.startActivityWithExtra(WorkOrderDetailFragmentActivity.class, workOrder);
        }
        super.onItemClicked(itemName, eInspectItemType, data);
    }

    /**
     * Event bus begin
     */
    public void onEventMainThread(ResponseEvent event) {
        switch (event.getId()) {
            case ResponseEventStatus.GET_WORKORDER_INSPECT_ITEMS:
                handleInspectItems(event);
                break;
            case ResponseEventStatus.WORKORDER_REPORT_TABLE:
                handleReportTableDone(event);
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(UIEvent event) {
        if (event.getId() == UIEventStatus.INSPECT_ITEM_VALUE_CHANGED) {
            InspectItem inspectItem = event.getData();
            filiterUnVisibleInspectItem(inspectItem);
        }
    }

    public void filiterUnVisibleInspectItem(InspectItem inspectItem) {
        if(null == inspectItem || !inspectItem.getName().equals(WorkOrder.KEY_REPORTFINISH_DEAL_TYPE)) {
            return;
        }
        boolean visible = false;
        boolean isPumpInspectVisible = false;
        String value = inspectItem.getValue();
        if(StringUtil.isBlank(value) || value.length() < 9) {
            return;
        }
        value = inspectItem.getValue().substring(0,8);
        /**
         * "name": 20001000,alias": "管网维修"
         * "name": 20007000,alias": "消防管理"
         * "name": 20003000,alias": "水质问题",
         * "name": 20002000,alias": "用水问题"
         * "name": 20008000,alias": "管网停水",
         */
        /*管网维修*//*消防管理*//*用水问题*//*水质问题*//*管网停水*/
        visible = value.equals("20001000")/*管网维修*/
                || value.equals("20007000")/*消防管理*/
                || value.equals("20002000")/*用水问题*/
                || value.equals("20003000")/*水质问题*/
                || value.equals("20008000");

        if (value.equals("20006000")) {
            isPumpInspectVisible = true;
        }

        if(ListUtil.isEmpty(inspectItems) || ListUtil.isEmpty(inspectItems.get(0).getChilds())) {
            return;
        }

        List<InspectItem> items = inspectItems.get(0).getChilds();
        for(InspectItem item : items) {
            if(item.getName().equals(WorkOrder.KEY_REPORTFINISH_DEAL_BGFFL) || item.getName().equals(WorkOrder.KEY_REPORTFINISH_DEAL_BGFKJ)) {
                item.setVisible(visible);
            }
            if (item.getName().equals(WorkOrder.KEY_PUMP_NAME)) {
                item.setVisible(isPumpInspectVisible);
            }
        }
        inspectItems.get(0).setChilds(items);
        getActivity().fillDatas(inspectItems);
    }

    /**
     * Event bus end
     */

    private String getContactValue(InspectItem item) {
        if (item.getType() == EInspectItemType.CONTACTMEN_SINGLE) {
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

    private String getSingleContactValue(String oldValue) {
        String[] array = oldValue.split(ContactInspectItemViewXtd.CONTACT_USER_INFO_SEPARATOR);
        return (array.length > 1) ? array[0] : oldValue;
    }
}
