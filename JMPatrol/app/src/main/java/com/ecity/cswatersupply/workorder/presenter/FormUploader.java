package com.ecity.cswatersupply.workorder.presenter;

import android.os.Handler;
import android.os.Message;

import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.menu.ACommonReportOperator1;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.FileUploader;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.utils.FilesToZip;
import com.ecity.cswatersupply.utils.JsonUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.z3app.android.util.FileUtil;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormUploader extends ACommonReportOperator1 {
    private static final int COMPRESS_FILE_DONE = 1000;
    private Map<String, String> inspectItemName2ZipPath = new HashMap<String, String>();
    private Handler handler = new EventReportHandler(this);
    private List<InspectItem> mediaItems;
    private int currentUploadingItemIndex = -1;
    private List<InspectItem> uploadedMediaItems = new ArrayList<InspectItem>();
    private String formId;
    private String tableName;
    private int failedMediaItemCount;

    @Override
    public List<InspectItem> getDataSource() {
        return new ArrayList<InspectItem>();
    }

    @Override
    public void submit2Server(List<InspectItem> datas) {
        findMediaItems(datas);
        mediaItems.removeAll(uploadedMediaItems);
        uploadedMediaItems.clear();
        currentUploadingItemIndex = -1;

        if (StringUtil.isEmpty(formId)) {
            //    upload form
        } else { // retry uploading media files
            uploadNextInspectMedia();
        }
    }

    @Override
    public void notifyBackEvent(CustomReportActivity1 activity) {
        if (null != activity) {
            activity.finish();
        }
    }

    private void notifySubmitSuccess() {
        ToastUtil.showShort(R.string.upload_success);
        LoadingDialogUtil.dismiss();
        getActivity().finish();
    }

    private void zipFiles() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                zipFiles(mediaItems);
            }
        }).start();
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

    private void zipFiles(List<InspectItem> items) {
        for (InspectItem item : items) {
            String[] values = item.getValue().split(Constants.IMAGE_SPLIT_STRING);
            if (ListUtil.isEmpty(values)) {
                continue;
            }

            List<String> paths = Arrays.asList(values);
            FilesToZip.EZipType zipType = null;
            if (item.getType() == EInspectItemType.IMAGE) {
                zipType = FilesToZip.EZipType.IMAGE;
            } else if (item.getType() == EInspectItemType.AUDIO) {
                zipType = FilesToZip.EZipType.AUDIO;
            } else if (item.getType() == EInspectItemType.VIDEO) {
                zipType = FilesToZip.EZipType.VIDEO;
            }

            if (zipType == null) {
                continue;
            }

            FilesToZip.getInstance().setZipType(zipType);
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
        LoadingDialogUtil.updateMessage("正在上传" + item.getAlias() + "项附件");
        String zipPath = inspectItemName2ZipPath.get(item.getName());
        List<String> paths = Arrays.asList(zipPath);
        String url = null;
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("gid", formId);
        parameters.put("tablename", tableName);
        parameters.put("field", item.getName());
        FileUploader.execute(paths, url, parameters, handler);
    }

    private static class EventReportHandler extends Handler {
        private final FormUploader uploader;

        public EventReportHandler(FormUploader uploader) {
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
                    uploader.failedMediaItemCount++;
                    break;
                case FileUploader.UPLOAD_ALL_FILES_DONE:
                    uploader.notifySubmitSuccess();
                    break;
                case FileUploader.UPLOAD_FAIL:
                    LoadingDialogUtil.dismiss();
                    ToastUtil.showLong("上传附件失败，请重试");
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
                uploader.notifySubmitSuccess();
                return;
            }

            uploader.formId = jsonObject.optString("");
            uploader.tableName = jsonObject.optString("");
            LoadingDialogUtil.updateMessage("正在压缩");
            uploader.zipFiles();
        }
    }
}
