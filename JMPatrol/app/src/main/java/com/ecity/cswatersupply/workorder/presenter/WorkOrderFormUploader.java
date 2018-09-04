package com.ecity.cswatersupply.workorder.presenter;

import android.os.Handler;
import android.os.Message;

import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.FileUploader;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;
import com.ecity.cswatersupply.network.request.ReportFormParameter;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.utils.FilesToZip;
import com.ecity.cswatersupply.utils.JsonUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrderBtnModel;
import com.z3app.android.util.FileUtil;
import com.z3app.android.util.StringUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gxx on 2017/4/12.
 */

public class WorkOrderFormUploader extends AFromUploader {

    public WorkOrderBtnModel taskBtn;
    private List<InspectItem> inspectItems;

    private static final int COMPRESS_FILE_DONE = 0;
    private Map<String, String> inspectItemName2ZipPath = new HashMap<String, String>();
    private Handler handler = new WorkOrderFormUploader.FormReportHandler(this);
    private List<InspectItem> mediaItems;
    private int currentUploadingItemIndex = -1;
    private List<InspectItem> uploadedMediaItems = new ArrayList<InspectItem>();
    private String gid;
    private String tableName;

    @Override
    public void submit(List<InspectItem> datas) {
        this.inspectItems = datas;
        findMediaItems(datas);
        mediaItems.removeAll(uploadedMediaItems);
        uploadedMediaItems.clear();
        currentUploadingItemIndex = -1;

        if (StringUtil.isEmpty(gid)) {
            LoadingDialogUtil.show(getActivity(), R.string.uploading_now);
            AReportInspectItemParameter parameter = getRequestParameter();
            FileUploader.execute(new ArrayList<String>(), getSubmitTaskFormUrl(), parameter.toMap(), handler);
        } else {
            // 再次上传上传失败的文件
            uploadNextInspectMedia();
        }
    }

    public void setTaskBtn(WorkOrderBtnModel taskBtn) {
        this.taskBtn = taskBtn;
    }

    private String getSubmitTaskFormUrl() {
        return ServiceUrlManager.getInstance().submitTaskFormDataUrl();
    }

    private String getSubmitFormFileUrl() {
        return ServiceUrlManager.getInstance().submitTaskFileUrl();
    }

    private AReportInspectItemParameter getRequestParameter() {
        return new ReportFormParameter(inspectItems, taskBtn);
    }

    private void findMediaItems(List<InspectItem> items) {
        mediaItems = new ArrayList<InspectItem>();
        List<EInspectItemType> mediaTypes = Arrays.asList(EInspectItemType.IMAGE, EInspectItemType.AUDIO, EInspectItemType.VIDEO);
        for (InspectItem item : items) {
            if (mediaTypes.contains(item.getType()) && !StringUtil.isBlank(item.getValue())) {
                mediaItems.add(item);
            }
        }
    }

    private void zipFiles() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                zipFiles(mediaItems);
            }
        }).start();
    }

    private void zipFiles(List<InspectItem> items) {
        for (InspectItem item : items) {
            String[] values = null;
            FilesToZip.EZipType zipType = null;
            if (item.getType() == EInspectItemType.IMAGE) {
                zipType = FilesToZip.EZipType.IMAGE;
                values = item.getValue().split(Constants.IMAGE_SPLIT_STRING);
            } else if (item.getType() == EInspectItemType.AUDIO) {
                zipType = FilesToZip.EZipType.AUDIO;
                values = item.getValue().split(",");
            } else if (item.getType() == EInspectItemType.VIDEO) {
                zipType = FilesToZip.EZipType.VIDEO;
                values = item.getValue().split(",");
            }

            if (com.zzz.ecity.android.applibrary.utils.ListUtil.isEmpty(values) || (zipType == null)) {
                continue;
            }

            FilesToZip.getInstance().setZipType(zipType);
            List<String> paths = Arrays.asList(values);
            String zipPath = FilesToZip.getInstance().zip(paths, item.getName());
            inspectItemName2ZipPath.put(item.getName(), zipPath);
        }

        Message msg = Message.obtain();
        if (inspectItemName2ZipPath.size() > 0) {
            msg.what = COMPRESS_FILE_DONE;
            msg.obj = inspectItemName2ZipPath;
        } else {
            msg.what = FileUploader.UPLOAD_ALL_FILES_DONE;
        }
        handler.sendMessage(msg);
    }

    private void clearUploadedItemMediaPath() {
        InspectItem item = mediaItems.get(currentUploadingItemIndex);
        inspectItemName2ZipPath.remove(item.getName());
        uploadedMediaItems.add(item);
    }

    private void uploadNextInspectMedia() {
        currentUploadingItemIndex++;
        if (currentUploadingItemIndex >= mediaItems.size()) {
            return;
        }

        InspectItem item = mediaItems.get(currentUploadingItemIndex);

        String temp = ResourceUtil.getStringById(R.string.uploading_file);
        String message = String.format(temp,item.getAlias());
        LoadingDialogUtil.updateMessage(message);

        String zipPath = inspectItemName2ZipPath.get(item.getName());
        List<String> paths = Arrays.asList(zipPath);
        String url = getSubmitFormFileUrl();
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("gid", gid);
        parameters.put("tablename", tableName);
        parameters.put("field", item.getName());
        FileUploader.execute(paths, url, parameters, handler);
    }

    private static class FormReportHandler extends Handler {
        private final WorkOrderFormUploader uploader;

        public FormReportHandler(WorkOrderFormUploader uploader) {
            this.uploader = uploader;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FileUploader.UPLOAD_ALL_FORMS_DONE:
                    processSubmitFormResponse(msg);
                    break;
                case COMPRESS_FILE_DONE:
                    uploader.uploadNextInspectMedia();
                    break;
                case FileUploader.UPLOAD_SINGLE_FILE_DONE:
                    File file = new File((String) msg.obj);
                    FileUtil.deleteFile(file);
                    uploader.clearUploadedItemMediaPath();
                    uploader.uploadNextInspectMedia();
                    break;
                case FileUploader.UPLOAD_SINGLE_FILE_FAIL:
                    //no need handle
                    break;
                case FileUploader.UPLOAD_ALL_FILES_DONE:
                    notifySubmitSuccess();
                    break;
                case FileUploader.UPLOAD_FAIL:
                    LoadingDialogUtil.dismiss();
                    ToastUtil.showLong(R.string.upload_file_fail);
                    break;
                default:
                    break;
            }
        }

        private void processSubmitFormResponse(Message msg) {
            String resultInfo = (String) msg.obj;
            JSONObject jsonObject = JsonUtil.getJsonObject(resultInfo);
            if (jsonObject == null) {
                ToastUtil.showShort(R.string.upload_fail);
                return;
            }

            boolean isSuccess = jsonObject.optBoolean("isSuccess");
            if (!isSuccess) {
                isSuccess = jsonObject.optBoolean("success");
            }

            if (!isSuccess) {
                ToastUtil.showShort(R.string.upload_fail);
                return;
            }

            if (ListUtil.isEmpty(uploader.mediaItems)) {
                notifySubmitSuccess();
                return;
            }

            uploader.gid = jsonObject.optString("gid");
            uploader.tableName = jsonObject.optString("tablename");
            LoadingDialogUtil.updateMessage(R.string.compress_file_now);
            uploader.zipFiles();
        }

        private void notifySubmitSuccess() {
            ToastUtil.showShort(R.string.upload_success);
            LoadingDialogUtil.dismiss();
            uploader.getActivity().finish();
        }
    }
}
