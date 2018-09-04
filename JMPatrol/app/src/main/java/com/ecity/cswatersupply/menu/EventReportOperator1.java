package com.ecity.cswatersupply.menu;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.event.Event;
import com.ecity.cswatersupply.network.FileUploader;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;
import com.ecity.cswatersupply.network.request.ReportEventFormParameter;
import com.ecity.cswatersupply.network.request.ReportEventImagesParameter;
import com.ecity.cswatersupply.network.response.eventreport.EventReportResponse;
import com.ecity.cswatersupply.service.ReportEventService;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.task.IExecuteAfterTaskDo;
import com.ecity.cswatersupply.task.ReadCacheTask;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.utils.CacheManager;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.FilesToZip;
import com.ecity.cswatersupply.utils.FilesToZip.EZipType;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.ecity.cswatersupply.utils.InspectItemUtil.ICacheInspectItemsCallback;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.z3app.android.util.FileUtil;
import com.z3app.android.util.StringUtil;

public class EventReportOperator1 extends ACommonReportOperator1 {
    private static final int READY_TO_SUBMIT = 0;
    private final EventReportHandler mEventHandler = new EventReportHandler(this);
    protected List<InspectItem> mInspectItems;
    private boolean hasMedia = false;
    private String mEventTableName;
    private List<String> imagesPaths = null;
    private List<String> audioPaths = null;
    private List<String> videoPaths = null;
    private ReportEventImagesParameter mReportEventImagesParameter;
    private ReportEventImagesParameter mReportEventAudioParameter;
    private ReportEventImagesParameter mReportEventVideoParameter;

    private boolean hasReported = false;
    private List<InspectItem> inspectItems2Upload;

    private IExecuteAfterTaskDo iExecuteAfterTaskDo;
    private AtomicInteger atomicInteger;

    protected String getServiceUrl() {
        return ServiceUrlManager.getInstance().getReportFormEventUrl();
    }

    protected String getReportImageUrl() {
        return ServiceUrlManager.getInstance().getReportImageUrl();
    }

    protected AReportInspectItemParameter getRequestParameter() {
        if (0 != getPlanTaskid()) {
            return new ReportEventFormParameter(getInspectItems2Upload(), mEventTableName, getFunctionKey(), getPlanTaskid());
        } else {
            return new ReportEventFormParameter(getInspectItems2Upload(), mEventTableName, getFunctionKey());
        }
    }

    protected int getRequestId() {
        return ResponseEventStatus.EVENT_REPORT_GET_PARAMS;
    }

    protected void requestDataSource() {
        ReportEventService.getInstance().getEventReportParams(getFunctionKey());
    }

    protected String getFunctionKey() {
        int eventType = getActivity().getIntent().getExtras().getInt(CustomViewInflater.EVENTTYPE);
        return String.valueOf(eventType);
    }

    protected int getPlanTaskid() {
        int planTaskid = getActivity().getIntent().getExtras().getInt(Constants.PLAN_TASK_ID);
        return planTaskid;
    }

    protected void uploadForm(List<String> filePaths, String url, Map<String, String> parameters, Handler handler) {
        FileUploader.execute(filePaths, url, parameters, mEventHandler);
    }

    protected void uploadFiles(List<String> filePaths, String url, Map<String, String> parameters, Handler handler) {
        FileUploader.execute(filePaths, url, parameters, handler);
    }

    @Override
    public List<InspectItem> getDataSource() {
        EventBusUtil.register(this);
        LoadingDialogUtil.show(getActivity(), R.string.event_report_get_params);
        requestDataSource();

        return null;
    }

    @Override
    public void submit2Server(List<InspectItem> datas) {
        inspectItems2Upload = datas;

        for (InspectItem item : datas) {
            if ((item.getType() == EInspectItemType.IMAGE) && !StringUtil.isBlank(item.getValue())) {
                hasMedia = true;
                String[] strs = item.getValue().split(Constants.IMAGE_SPLIT_STRING);
                imagesPaths = Arrays.asList(strs);
                continue;
            }
            if ((item.getType() == EInspectItemType.AUDIO) && !StringUtil.isBlank(item.getValue())) {
                hasMedia = true;
                String[] strs = item.getValue().split(",");
                audioPaths = Arrays.asList(strs);
                continue;
            }
            if ((item.getType() == EInspectItemType.VIDEO) && !StringUtil.isBlank(item.getValue())) {
                hasMedia = true;
                String[] strs = item.getValue().split(",");
                videoPaths = Arrays.asList(strs);
                continue;
            }
        }
        if (getActivity() != null) {
            LoadingDialogUtil.show(getActivity(), R.string.uploading_now);
            LoadingDialogUtil.setCancelable(false);
        }
        AReportInspectItemParameter parameter = getRequestParameter();
        uploadForm(new ArrayList<String>(), getServiceUrl(), parameter.toMap(), mEventHandler);
    }

    @Override
    public void notifyBackEvent(final CustomReportActivity1 activity) {
        final List<InspectItem> allInspectItems = InspectItemUtil.mergeAllItemsInline(getInspectItems2Upload());
        if (!hasReported && InspectItemUtil.hasValue(allInspectItems)) {
            InspectItemUtil.confirmCacheItems(activity, allInspectItems, new ICacheInspectItemsCallback() {
                @Override
                public String getCacheKey() {
                    return getUniqueCacheKey();
                }

                @Override
                public void onCacheDone(boolean isSaved) {
                    finishActivity(activity);
                }
            });
        } else {
            CacheManager.deleteCache(activity, getUniqueCacheKey());
            finishActivity(activity);
        }
    }

    @Override
    public void notifyBackEventWhenFinishReport(final CustomReportActivity1 activity) {
        EventBusUtil.post(new UIEvent(UIEventStatus.EVENT_REPORT_FINISHED));
        notifyBackEvent(activity);
    }

    public String parserMediaGID(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }

        return jsonObject.optString("ids");
    }

    /**
     * 读取缓存的页面信息
     */
    protected void readCachedValue(IExecuteAfterTaskDo iExecuteAfterTaskDo) {
        new ReadCacheTask(getActivity(), iExecuteAfterTaskDo).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getUniqueCacheKey());
    }

    protected String getUniqueCacheKey() {
        return HostApplication.getApplication().getCurrentUser().getGid() + mEventTableName;
    }

    private void finishActivity(CustomReportActivity1 activity) {
        LoadingDialogUtil.dismiss();
        EventBusUtil.unregister(this);
        activity.finish();
    }

    private static class EventReportHandler extends Handler {
        private WeakReference<EventReportOperator1> operator;

        public EventReportHandler(EventReportOperator1 operator) {
            this.operator = new WeakReference<EventReportOperator1>(operator);
        }

        @Override
        public void handleMessage(Message msg) {
            EventReportOperator1 operator = this.operator.get();
            if (null == operator) {
                return;
            }
            CustomReportActivity1 activity = operator.getActivity();
            if (null != activity) {
                switch (msg.what) {
                    case READY_TO_SUBMIT:
                        if (operator.hasMedia && !StringUtil.isEmpty(msg.obj.toString()) && null != operator.mReportEventImagesParameter) {
                            Map<String, List<String>> uploadPaths = new HashMap<String, List<String>>();
                            uploadPaths = (Map<String, List<String>>) msg.obj;
                            operator.fileUpLoading(uploadPaths, operator);
                        }
                        break;
                    case FileUploader.UPLOAD_ALL_FORMS_DONE:
                        if (operator.hasMedia || !ListUtil.isEmpty(operator.imagesPaths)) {
                            try {
                                String resultInfo = (String) msg.obj;
                                JSONObject jsonObject = new JSONObject(resultInfo);
                                String ids = operator.parserMediaGID(jsonObject);
                                operator.mReportEventImagesParameter = new ReportEventImagesParameter(operator.getInspectItemName(EInspectItemType.IMAGE), ids);
                                operator.mReportEventAudioParameter = new ReportEventImagesParameter(operator.getInspectItemName(EInspectItemType.AUDIO), ids);
                                operator.mReportEventVideoParameter = new ReportEventImagesParameter(operator.getInspectItemName(EInspectItemType.VIDEO), ids);
                                operator.new MyZipThread().start();
                            } catch (Exception e) {
                                Log.e("EventReportOperator", e.getMessage());
                            }
                        } else {
                            LoadingDialogUtil.setCancelable(true);
                            LoadingDialogUtil.dismiss();
                            boolean isSuccess = false;
                            String errorMsg = "";
                            try {
                                String resultInfo = (String) msg.obj;
                                JSONObject jsonObject = new JSONObject(resultInfo);
                                if (jsonObject.has("isSuccess")) {
                                    isSuccess = jsonObject.optBoolean("isSuccess");
                                } else if (jsonObject.has("success")) {
                                    isSuccess = jsonObject.optBoolean("success");
                                }

                                errorMsg = jsonObject.optString("message");
                            } catch (JSONException e) {
                                Log.e("EventReportOperator", e.getMessage());
                            }
                            if (isSuccess) {
                                operator.hasReported = true;
                                Toast.makeText(activity, activity.getString(R.string.upload_success), Toast.LENGTH_SHORT).show();
                                operator.notifyBackEventWhenFinishReport(activity);
                            } else {
                                Toast.makeText(activity, activity.getString(R.string.upload_fail)+"("+errorMsg+")", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case FileUploader.UPLOAD_SINGLE_FILE_DONE:
                        File file = new File((String) msg.obj);
                        FileUtil.deleteFile(file);
                        break;
                    case FileUploader.UPLOAD_ALL_FILES_DONE:
                        if (null != operator.atomicInteger && 0 >= operator.atomicInteger.decrementAndGet()) {
                            LoadingDialogUtil.setCancelable(true);
                            LoadingDialogUtil.dismiss();

                            if (null != activity) {
                                operator.hasReported = true;
                                Toast.makeText(activity, (String) msg.obj, Toast.LENGTH_SHORT).show();
                                operator.notifyBackEventWhenFinishReport(activity);
                            }
                        }
                        break;
                    case FileUploader.UPLOAD_FAIL:
                        if (null != operator.atomicInteger && 0 >= operator.atomicInteger.decrementAndGet()) {
                            LoadingDialogUtil.setCancelable(true);
                            LoadingDialogUtil.dismiss();
                        } else {
                            LoadingDialogUtil.setCancelable(true);
                            LoadingDialogUtil.dismiss();
                        }

                        if (null != activity) {
                            Toast.makeText(activity, (String) msg.obj, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private class MyZipThread extends Thread {

        @Override
        public void run() {
            atomicInteger = new AtomicInteger(0);
            Map<String, List<String>> zipPathMap = new HashMap<String, List<String>>();
            List<String> zipImagePaths = new ArrayList<String>(3);
            List<String> zipAudioPaths = new ArrayList<String>(3);
            List<String> zipVideoPaths = new ArrayList<String>(3);
            if (imagesPaths != null && imagesPaths.size() > 0) {
                FilesToZip.getInstance().setZipType(EZipType.IMAGE);
                zipImagePaths.add(FilesToZip.getInstance().zip(imagesPaths, "0"));
                atomicInteger.incrementAndGet();
            }
            if (audioPaths != null && audioPaths.size() > 0) {
                FilesToZip.getInstance().setZipType(EZipType.AUDIO);
                zipAudioPaths.add(FilesToZip.getInstance().zip(audioPaths, "1"));
                atomicInteger.incrementAndGet();
            }
            if (videoPaths != null && videoPaths.size() > 0) {
                FilesToZip.getInstance().setZipType(EZipType.VIDEO);
                zipVideoPaths.add(FilesToZip.getInstance().zip(videoPaths, "2"));
                atomicInteger.incrementAndGet();
            }
            zipPathMap.put("imagesPaths", zipImagePaths);
            zipPathMap.put("audioPaths", zipAudioPaths);
            zipPathMap.put("videoPaths", zipVideoPaths);
            Message msg = Message.obtain();
            if (zipPathMap.size() > 0) {
                msg.what = READY_TO_SUBMIT;
                msg.obj = zipPathMap;
            } else {
                msg.what = FileUploader.UPLOAD_ALL_FILES_DONE;
            }
            mEventHandler.sendMessage(msg);
        }
    }

    private void handleGetEventReportParams(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        EventReportResponse response = event.getData();
        mEventTableName = response.getTableName();
        mInspectItems = response.getItems();
        iExecuteAfterTaskDo = new EventReportExecuteAfterTaskDo(mInspectItems);
        readCachedValue(iExecuteAfterTaskDo);
    }

    protected void fileUpLoading(Map<String, List<String>> uploadPaths, EventReportOperator1 operator) {
        Map<String, String> param = null;
        if (!uploadPaths.get("imagesPaths").isEmpty()) {
            param = operator.getReportEventImagesParameter().toMap();
            fillFileUploadSimpleParameter(param);
            uploadFiles(uploadPaths.get("imagesPaths"), getReportImageUrl(), param, operator.getEventHandler());
        }
        if (!uploadPaths.get("audioPaths").isEmpty()) {
            param = operator.getReportEventAudioParameter().toMap();
            fillFileUploadSimpleParameter(param);
            uploadFiles(uploadPaths.get("audioPaths"), getReportImageUrl(), param, operator.getEventHandler());
        }

        if (!uploadPaths.get("videoPaths").isEmpty()) {
            param = operator.getReportEventVideoParameter().toMap();
            fillFileUploadSimpleParameter(param);
            uploadFiles(uploadPaths.get("videoPaths"), getReportImageUrl(), param, operator.getEventHandler());
        }
    }

    /***
     * 自定义文件上传参数填充
     * @param param
     */
    protected void fillFileUploadSimpleParameter(Map<String, String> param) {

    }

    private String getInspectItemName(EInspectItemType targetType) {
        for (InspectItem item : getInspectItems2Upload()) {
            if (item.getType().name().equalsIgnoreCase(targetType.name())) {
                return item.getName();
            }
        }

        return null;
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        if (event.getId() == getRequestId()) {
            handleGetEventReportParams(event);
        }
    }

    public final ReportEventImagesParameter getReportEventImagesParameter() {
        return mReportEventImagesParameter;
    }

    public final ReportEventImagesParameter getReportEventAudioParameter() {
        return mReportEventAudioParameter;
    }

    public final ReportEventImagesParameter getReportEventVideoParameter() {
        return mReportEventVideoParameter;
    }

    public final EventReportHandler getEventHandler() {
        return mEventHandler;
    }

    protected List<InspectItem> getInspectItems() {
        return mInspectItems;
    }

    private List<InspectItem> getInspectItems2Upload() {
        return inspectItems2Upload;
    }

    protected class EventReportExecuteAfterTaskDo implements IExecuteAfterTaskDo {
        private List<InspectItem> mInspectItems;

        public EventReportExecuteAfterTaskDo(List<InspectItem> mInspectItems) {
            this.mInspectItems = mInspectItems;
        }

        @Override
        public void executeTaskError() {
        }

        @Override
        public void executeOnTaskSuccess(Serializable result) {
            String cacheStr = (String) result;
            InspectItemUtil.restoreInspectItemsFromCache(cacheStr, mInspectItems);
        }

        @Override
        public void executeOnTaskFinish() {
            getActivity().fillDatas(mInspectItems);
            LoadingDialogUtil.dismiss();
        }
    }
}
