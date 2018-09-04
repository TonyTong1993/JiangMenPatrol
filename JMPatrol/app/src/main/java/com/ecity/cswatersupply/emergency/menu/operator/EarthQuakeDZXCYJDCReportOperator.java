package com.ecity.cswatersupply.emergency.menu.operator;

import android.os.Handler;
import android.os.Message;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.emergency.model.EarthQuakeQuickReportModel;
import com.ecity.cswatersupply.emergency.model.EmergencyPlanModel;
import com.ecity.cswatersupply.emergency.network.request.GetEQUpdateZQSBInspectInfoParameter;
import com.ecity.cswatersupply.emergency.network.request.GetEQXCYJDCInspectInfoParameter;
import com.ecity.cswatersupply.emergency.network.request.GetEarthQuakeInspectFileParameter;
import com.ecity.cswatersupply.emergency.service.DownloadService;
import com.ecity.cswatersupply.emergency.service.EmergencyService;
import com.ecity.cswatersupply.emergency.utils.InspectFileManager;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.menu.EventReportOperator1;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.FileUploader;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.ClearUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.z3app.android.util.StringUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 地震现场应急调查上报
 *
 * @author ml
 */
public class EarthQuakeDZXCYJDCReportOperator extends EventReportOperator1 {
    private static final int READY_TO_SUBMIT = 0;
    private EQEventReportHandler mEQEventHandler = new EQEventReportHandler(this);
    private List<InspectItem> mInspectChildsItems;
    private InspectItem mInspectParentItem;
    private GetEarthQuakeInspectFileParameter inspectFileParameter;
    private EarthQuakeQuickReportModel model;
    public boolean hasImage = false;
    boolean flagNewOrUpdate;
    private List<InspectItem> inspectItems2Upload;
    //包含所有的媒体文件的真实路径
    private List<String> mediaPaths = new ArrayList<String>();
    private EmergencyPlanModel emergency;
    private String new_or_update;

    @Override
    public List<InspectItem> getDataSource() {
        new_or_update = getActivity().getIntent().getStringExtra("new_or_update");
        CustomTitleView viewById = (CustomTitleView) getActivity().findViewById(R.id.view_title_report_event);
        viewById.setBtnStyle(CustomTitleView.BtnStyle.ONLY_BACK);

        EventBusUtil.register(this);
        LoadingDialogUtil.show(getActivity(), R.string.event_report_get_params);
        model = (EarthQuakeQuickReportModel) getActivity().getIntent().getSerializableExtra("report_item");
        if (!StringUtil.isEmpty(new_or_update)) {
            if (new_or_update.equalsIgnoreCase("new")) {
                EmergencyService.getInstance().getEQQuickReportInspectInfos(ServiceUrlManager.getEQQuickReportInspectInfoUrl(), new GetEQXCYJDCInspectInfoParameter(), getRequestId());
            } else if (new_or_update.equalsIgnoreCase("update")) {
                EmergencyService.getInstance().getEQQuickReportInspectInfosForUpdate(ServiceUrlManager.getEQXCDCUpdateInspectInfoUrl(), new GetEQUpdateZQSBInspectInfoParameter(model.getGid()),
                        getRequestId());
            }
        }
        return null;
    }

    @Override
    public void submit2Server(List<InspectItem> datas) {
        LoadingDialogUtil.show(getActivity(), R.string.str_emergency_ziping);
        LoadingDialogUtil.setCancelable(false);
        inspectItems2Upload = datas;
        if (new_or_update.equalsIgnoreCase("new")) {
            getAllMediaPath(inspectItems2Upload, null, false);
        } else {
            getAllMediaPath(inspectItems2Upload, InspectFileManager.getInstance().getSrcPath4Update(), false);
        }
        new MyZipThread().start();
    }

    private void getAllMediaPath(List<InspectItem> datas, String dirPath, boolean isUnzip) {
        for (InspectItem item : datas) {
            if (item.getType() == EInspectItemType.GROUP) {
                getAllMediaPath(item.getChilds(), dirPath, isUnzip);
            } else {
                if ((item.getType() == EInspectItemType.IMAGE) && !StringUtil.isBlank(item.getValue())) {
                    hasImage = true;
                    String[] strs = null;
                    if (new_or_update.equalsIgnoreCase("new")) {
                        strs = item.getValue().split(Constants.IMAGE_SPLIT_STRING);
                        reSetMediaPath(item, strs);
                        mediaPaths.addAll(Arrays.asList(strs));
                    } else {
                        if (isUnzip) {
                            strs = item.getValue().split(",");
                            reSetMediaPath(item, strs, dirPath, isUnzip);
                        } else {
                            strs = item.getValue().split(Constants.IMAGE_SPLIT_STRING);
                            reSetMediaPath(item, strs, dirPath, isUnzip);
                            mediaPaths.addAll(Arrays.asList(strs));
                        }
                    }
                    continue;
                } else if ((item.getType() == EInspectItemType.AUDIO) && !StringUtil.isBlank(item.getValue())) {
                    hasImage = true;
                    String[] strs = item.getValue().split(",");
                    if (new_or_update.equalsIgnoreCase("new")) {
                        reSetMediaPath(item, strs);
                    } else {
                        reSetMediaPath(item, strs, dirPath, isUnzip);
                    }
                    mediaPaths.addAll(Arrays.asList(strs));
                    continue;
                } else if ((item.getType() == EInspectItemType.VIDEO) && !StringUtil.isBlank(item.getValue())) {
                    hasImage = true;
                    String[] strs = item.getValue().split(",");
                    if (new_or_update.equalsIgnoreCase("new")) {
                        reSetMediaPath(item, strs);
                    } else {
                        reSetMediaPath(item, strs, dirPath, isUnzip);
                    }
                    mediaPaths.addAll(Arrays.asList(strs));
                    continue;
                }
            }
        }
    }

    /**
     * @param item
     * @param strs
     * @param dirPath ZIP解压后文件的存放路径
     * @param isUnZip 是否是从ZIP解压出来的InspectItem
     */
    private void reSetMediaPath(InspectItem item, String[] strs, String dirPath, boolean isUnZip) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strs.length; i++) {
            String mStr = null;
            if (isUnZip) {
                mStr = dirPath + "/" + strs[i];
                if (i != strs.length - 1) {
                    if (item.getType() == EInspectItemType.IMAGE) {
                        sb.append(mStr).append(Constants.IMAGE_SPLIT_STRING);
                    } else {
                        sb.append(mStr).append(",");
                    }
                } else {
                    sb.append(mStr);
                }
            } else {
                mStr = strs[i].substring(strs[i].lastIndexOf("/") + 1);
                if (i != strs.length - 1) {
                    sb.append(mStr).append(",");
                } else {
                    sb.append(mStr);
                }
            }
        }
        item.setValue(sb.toString());
    }

    private class MyZipThread extends Thread {
        @Override
        public void run() {
            Message msg = Message.obtain();
            InspectItem resItem = null;
            if (new_or_update.equals("new")) {
                resItem = InspectFileManager.readInspectItemFromFile(InspectFileManager.getInstance().getXcdcJsonFilePath());
            } else {
                resItem = InspectFileManager.readInspectItemFromFile(InspectFileManager.getInstance().getInspectPath4Update());
            }
            if (null == resItem) {
                msg.what = FileUploader.UPLOAD_FAIL;
                msg.obj = HostApplication.getApplication().getResources().getString(R.string.str_emergency_getinspect_error);
            } else {
                for (InspectItem i : inspectItems2Upload) {
                    if (i.getType() == EInspectItemType.GEOMETRY) {
                        i.getValue().replace(Constants.IMAGE_SPLIT_STRING, "");
                    }
                }
                if (hasImage) {
                    //复制文件到指点目录下 
                    if (moveMediaFileforXCDC()) {
                        //上报之前  先更新数据
                        mInspectParentItem.setChilds(inspectItems2Upload);
                        InspectFileManager.wirteInspectIntoFile(mInspectParentItem, InspectFileManager.getInstance().getXcdcJsonFilePath());
                        boolean isZipSuccess = InspectFileManager.zipInspectFile(InspectFileManager.getInstance().getXcdcFilePath(), InspectFileManager.getInstance().getXcdcZipFilePath());
                        if (isZipSuccess) {
                            msg.what = READY_TO_SUBMIT;
                            msg.obj = InspectFileManager.getInstance().getXcdcZipFilePath();
                        } else {
                            msg.what = FileUploader.UPLOAD_FAIL;
                            msg.obj = HostApplication.getApplication().getResources().getString(R.string.str_emergency_zip_errer);
                        }
                    } else {
                        msg.what = FileUploader.UPLOAD_FAIL;
                        msg.obj = HostApplication.getApplication().getResources().getString(R.string.str_emergency_zip_errer);
                    }
                } else {
                    //上报之前  先更新数据
                    mInspectParentItem.setChilds(inspectItems2Upload);
                    InspectFileManager.wirteInspectIntoFile(mInspectParentItem, InspectFileManager.getInstance().getXcdcJsonFilePath());
                    boolean isZipSuccess = InspectFileManager.zipInspectFile(InspectFileManager.getInstance().getXcdcFilePath(), InspectFileManager.getInstance().getXcdcZipFilePath());
                    if (isZipSuccess) {
                        msg.what = READY_TO_SUBMIT;
                        msg.obj = InspectFileManager.getInstance().getXcdcZipFilePath();
                    } else {
                        msg.what = FileUploader.UPLOAD_FAIL;
                        msg.obj = HostApplication.getApplication().getResources().getString(R.string.str_emergency_zip_errer);
                    }
                }
            }
            mEQEventHandler.sendMessage(msg);
        }
    }

    /**
     * 将媒体文件的路径全部换成原路径内的最后名字
     *
     * @param item
     * @param strs
     */
    private void reSetMediaPath(InspectItem item, String[] strs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strs.length; i++) {
            String mStr = strs[i].substring(strs[i].lastIndexOf("/") + 1);
            if (i != strs.length - 1) {
                sb.append(mStr).append(Constants.IMAGE_SPLIT_STRING);
            } else {
                sb.append(mStr);
            }
        }
        item.setValue(sb.toString());
    }

    /**
     * 移动媒体文件到指定目录(现场调查)
     *
     * @return
     */
    private boolean moveMediaFileforXCDC() {
        InspectFileManager.deleteXCDCSrc();
        for (int i = 0; i < mediaPaths.size(); i++) {
            String path = mediaPaths.get(i);
            File srcFile = new File(path);
            if (!path.contains("/")) {
                continue;
            }
            String mediaFileName = path.substring(path.lastIndexOf("/"));
            File tarFile = new File(InspectFileManager.getInstance().getXcdcFilePath() + mediaFileName);
            try {
                boolean isMovedSuccess = ClearUtil.moveFile(srcFile, tarFile);
                if (isMovedSuccess) {
                    if (i != mediaPaths.size() - 1) {
                        continue;
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            } catch (Exception e) {
                LogUtil.e(this, e);
                return false;
            }
        }

        return false;
    }

    private class EQEventReportHandler extends Handler {
        private WeakReference<EarthQuakeDZXCYJDCReportOperator> operator;

        public EQEventReportHandler(EarthQuakeDZXCYJDCReportOperator operator) {
            this.operator = new WeakReference<EarthQuakeDZXCYJDCReportOperator>(operator);
        }

        @Override
        public void handleMessage(Message msg) {
            EarthQuakeDZXCYJDCReportOperator operator = this.operator.get();
            if (null == operator) {
                return;
            }
            CustomReportActivity1 activity = operator.getActivity();
            if (null != activity) {
                switch (msg.what) {
                    case READY_TO_SUBMIT:
                        LoadingDialogUtil.show(getActivity(), R.string.uploading_now);
                        String zipPath = (String) msg.obj;
                        inspectFileParameter = new GetEarthQuakeInspectFileParameter(model, inspectItems2Upload);
                        List<String> paths = new ArrayList<>();
                        paths.add(zipPath);
                        FileUploader.execute(paths, getServiceUrl(), operator.inspectFileParameter.toMap(), operator.mEQEventHandler);
                        //处理上报表单，将gid存首选项
//                        SettingsManager.getInstance().setXCDCItemForGid(String.valueOf(model.getGid()), InspectFileManager.getInstance().getXcdcJsonFilePath());

                        break;
                    case FileUploader.UPLOAD_ALL_FILES_DONE:
                        if (null != activity) {
                            LoadingDialogUtil.setCancelable(true);
                            LoadingDialogUtil.dismiss();
                            ToastUtil.showLong(activity.getResources().getString(R.string.upload_success));
                            operator.notifyBackEvent(activity);
                        }
                        break;
                    case FileUploader.UPLOAD_FAIL:
                        if (null != activity) {
                            LoadingDialogUtil.setCancelable(true);
                            LoadingDialogUtil.dismiss();
                            ToastUtil.showLong((String) msg.obj);
                            operator.notifyBackEvent(activity);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected String getServiceUrl() {
        if (new_or_update.equals("new")) {
            flagNewOrUpdate = true;
            return ServiceUrlManager.reportEQXCDCDetail();
        } else if (new_or_update.equals("update")) {
            flagNewOrUpdate = false;
            return ServiceUrlManager.updateEQXCDCDetail();
        } else {
            return "";
        }
    }

    @Override
    protected String getReportImageUrl() {
        return ServiceUrlManager.reportExceptionFile();
    }

    public void fileUpLoading(EarthQuakeDZXCYJDCReportOperator operator) {
        List<String> paths = new ArrayList<String>();
        paths.add(InspectFileManager.getInstance().getZipPath());
        FileUploader.execute(paths, getReportImageUrl(), operator.inspectFileParameter.toMap(), operator.mEQEventHandler);
    }

    @Override
    public void notifyBackEvent(CustomReportActivity1 activity) {
        if (null != activity) {
            EventBusUtil.unregister(this);
            activity.finish();
        }
    }

    protected int getRequestId() {
        String str = getActivity().getIntent().getStringExtra("new_or_update");
        if (str.equals("new")) {
            return ResponseEventStatus.EMERGENCY_GET_EQQXCYJDC_INSPECT_INFO;
        } else {
            return ResponseEventStatus.EMERGENCY_GET_EQQXCYJDC_INSPECT_INFO_UPDATE;
        }

    }

    private void handleGetEventReportParams(ResponseEvent event) {
        SessionManager.isImageNotEdit = false;
        LoadingDialogUtil.dismiss();
        mInspectChildsItems = new ArrayList<InspectItem>();
        //服务获取
        InspectFileManager.initInspectFile(model);

        mInspectParentItem = event.getData();
        if (null != mInspectParentItem && (!ListUtil.isEmpty(mInspectParentItem.getChilds()))) {
            InspectFileManager.wirteInspectIntoFile(mInspectParentItem, InspectFileManager.getInstance().getXcdcJsonFilePath());
//            getBaseModelValue(model);
            for (InspectItem item : mInspectParentItem.getChilds()) {
                item.setVisible(true);
                mInspectChildsItems.add(item);
            }
            SessionManager.currentInspectItems = mInspectChildsItems;
            getActivity().fillDatas4Earthquake(mInspectChildsItems);
        } else {
            ToastUtil.showLong(getActivity().getResources().getString(R.string.str_emergency_get_inspect_error));
        }
    }

//    private void handleClickedItem(EarthQuakeQuickReportModel model) {
//        String srcJsonPath = SettingsManager.getInstance().getXCDCItemForGid(String.valueOf(model.getGid()));
//        if (srcJsonPath != null) {
//            InspectItem inspectItem = InspectFileManager.readInspectItemFromFile(srcJsonPath);
//            reSetMediaPathXCDC(inspectItem.getChilds());
//        }
//    }


    /**
     * 将基础信息打包放到检查项组内
     *
     * @param model
     */
    private void getBaseModelValue(EarthQuakeQuickReportModel model) {
        StringBuffer sb = new StringBuffer();
        sb.append(getActivity().getResources().getString(R.string.earthquake_name) + "  " + model.getEarthQuakeName());
        sb.append("\n");
        sb.append(getActivity().getResources().getString(R.string.earthquake_id) + "  " + model.getEarthQuakeId());
        sb.append("\n");
        sb.append(getActivity().getResources().getString(R.string.survey_person) + "  " + model.getSurveyPerson());
        sb.toString();
        InspectItem modelItem = new InspectItem();
        modelItem.setAlias(getActivity().getResources().getString(R.string.earthquake_base_info));
        modelItem.setName(getActivity().getResources().getString(R.string.earthquake_base_info));
        modelItem.setDefaultValue(sb.toString());
        modelItem.setVisible(true);
        modelItem.setType(EInspectItemType.TEXTEXT);
        modelItem.setEdit(false);
        mInspectChildsItems.add(modelItem);
    }

    public void onEventMainThread(UIEvent event) {
        switch (event.getId()) {
            case UIEventStatus.EMERGENCY_DOWN_DONE:
                EmergencyPlanModel data = event.getData();
                updateStatus(data.getStatus());
        }
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.EMERGENCY_GET_EQQXCYJDC_INSPECT_INFO:

                handleGetEventReportParams(event);
                break;
            case ResponseEventStatus.EMERGENCY_GET_EQQXCYJDC_INSPECT_INFO_UPDATE:

                handleGetEventUpdateParams(event);
                break;
            case ResponseEventStatus.EMERGENCY_EARTHQUAKE_XCDC_REPORT:
                break;
            default:
                break;
        }
    }

    public static boolean isChinese(char c) {
        boolean ischinese = false;
        if (c >= 19968 && c <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
            ischinese = true;
        }
        return ischinese;
    }


    private void handleGetEventUpdateParams(ResponseEvent event) {
        InspectFileManager.initInspectFile(model);
        String zipUrlResponse = event.getData();
        String zipUrl = "";
        for (int j = 0; j < zipUrlResponse.length(); j++) {
            if (isChinese(zipUrlResponse.charAt(j)))
                try {
                    zipUrl = zipUrl + URLEncoder.encode(zipUrlResponse.substring(j, j + 1), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            else {
                zipUrl = zipUrl + zipUrlResponse.substring(j, j + 1);
            }
        }

        zipUrl = zipUrl.replaceAll(" ", "%20");


        if (StringUtil.isEmpty(zipUrl)) {
            ToastUtil.showLong(getActivity().getResources().getString(R.string.str_emergency_get_inspect_error));
        } else {
            String filename = zipUrl.substring(zipUrl.lastIndexOf("/") + 1, zipUrl.length());
            String filePath = zipUrl.substring(0, zipUrl.lastIndexOf("/"));
            InspectFileManager.initFilePath4UpdateInspectXC(model.getGid());
            InspectFileManager.getInstance().setZipPath4Update(filename);
            //下载压缩包
            emergency = new EmergencyPlanModel();
            emergency.setSavePath(InspectFileManager.getInstance().getSrcPath4Update());
            emergency.setDoc(filename);
            emergency.setUrl(filePath + "/" + filename);
            DownloadService.intentDownload(getActivity(), filePath, emergency);
            LoadingDialogUtil.show(getActivity(), "正在处理上报数据，请稍候……");
        }
    }

    private void updateStatus(int status) {
        switch (status) {
            case EmergencyPlanModel.STATUS_NOT_DOWNLOAD:
                LoadingDialogUtil.show(getActivity(), R.string.str_emergency_download_file_ready);
                break;
            case EmergencyPlanModel.STATUS_CONNECTING:
                LoadingDialogUtil.show(getActivity(), R.string.str_emergency_download_file_connection);
                break;
            case EmergencyPlanModel.STATUS_CONNECT_ERROR:
                LoadingDialogUtil.show(getActivity(), R.string.str_emergency_download_file_connection_error);
                break;
            case EmergencyPlanModel.STATUS_DOWNLOADING:
                LoadingDialogUtil.show(getActivity(), R.string.str_emergency_download_file_downloading);
                break;
            case EmergencyPlanModel.STATUS_PAUSED:
                break;
            case EmergencyPlanModel.STATUS_DOWNLOAD_ERROR:
                LoadingDialogUtil.show(getActivity(), R.string.str_emergency_download_file_download_error);
                break;
            case EmergencyPlanModel.STATUS_COMPLETE:
                LoadingDialogUtil.show(getActivity(), R.string.str_emergency_download_file_download_finish);
                String zipPath4Update = InspectFileManager.getInstance().getZipPath4Update();
                String srcPath4Update = InspectFileManager.getInstance().getSrcPath4Update();
                String inspectPath4Update = InspectFileManager.getInstance().getInspectPath4Update();
                //解压zip文件
                boolean isUnZipSuccess = InspectFileManager.unZipInspectFile(zipPath4Update, srcPath4Update);
                if (isUnZipSuccess) {
                    //解压成功之后删除zip包
                    InspectFileManager.deleteFile(zipPath4Update);
                    mInspectParentItem = InspectFileManager.readInspectItemFromFile(inspectPath4Update);
                    mInspectChildsItems = new ArrayList<InspectItem>();
                    if (null != mInspectParentItem && (!ListUtil.isEmpty(mInspectParentItem.getChilds()))) {
                        getAllMediaPath(mInspectParentItem.getChilds(), srcPath4Update, true);
                        InspectFileManager.wirteInspectIntoFile(mInspectParentItem, inspectPath4Update);
                        handleInspectEdit(mInspectParentItem.getChilds());
                        getActivity().fillDatas4Earthquake(mInspectChildsItems);
                    }
                } else {
                    ToastUtil.showLong(getActivity().getResources().getString(R.string.str_emergency_download_file_un_zip_error));
                }
                break;
            default:
                break;
        }
        LoadingDialogUtil.dismiss();
    }

    private void handleInspectEdit(List<InspectItem> mInspectList) {
        for (InspectItem item : mInspectList) {
            item.setVisible(true);

            if (SessionManager.isImageNotEdit) {
                if (item.getType() == EInspectItemType.GROUP) {
                    handleGroup(item);
                }

//                if (item.getType() != EInspectItemType.IMAGE && item.getType() != EInspectItemType.VIDEO) {
//                    item.setEdit(false);
//                }
                item.setEdit(false);
            }

            mInspectChildsItems.add(item);
        }
    }

    private void handleGroup(InspectItem item) {
        for (InspectItem item1 : item.getChilds()) {
//            if (i.getType() != EInspectItemType.IMAGE && i.getType() != EInspectItemType.VIDEO) {
//                i.setEdit(false);
//            }
            item1.setEdit(false);
        }
    }

    public List<InspectItem> getmInspectItems() {
        return mInspectChildsItems;
    }
}
