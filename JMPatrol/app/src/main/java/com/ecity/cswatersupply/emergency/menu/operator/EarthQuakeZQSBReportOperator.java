package com.ecity.cswatersupply.emergency.menu.operator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Handler;
import android.os.Message;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.model.EarthQuakeQuickReportModel;
import com.ecity.cswatersupply.emergency.model.EmergencyPlanModel;
import com.ecity.cswatersupply.emergency.network.request.GetEQReportZQSBInspectFileParameter;
import com.ecity.cswatersupply.emergency.network.request.GetEQReportZQSBInspectInfoParameter;
import com.ecity.cswatersupply.emergency.network.request.GetEQUpdateZQSBInspectFileParameter;
import com.ecity.cswatersupply.emergency.network.request.GetEQUpdateZQSBInspectInfoParameter;
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
 * 灾情速报
 *
 * @author ml
 */
public class EarthQuakeZQSBReportOperator extends EventReportOperator1 {
    private static final int READY_TO_SUBMIT = 0;
    //上报新的还是更新已有的灾情
    public static final String REPORT_TYPE = "REPORT_TYPE";
    public static final String REPORT_TYPE_NEW = "REPORT_TYPE_NEW";
    public static final String REPORT_TYPE_UPDATE = "REPORT_TYPE_UPDATE";

    private List<InspectItem> mInspectChildsItems;
    private InspectItem mInspectParentItem;
    private List<InspectItem> inspectItems2Upload;
    private List<String> mediaPaths = new ArrayList<String>();
    private EventReportHandler mReportHandler = new EventReportHandler(this);
    private GetEQReportZQSBInspectFileParameter reportZQSBInspectFileParameter;
    private GetEQUpdateZQSBInspectFileParameter updateZQSBInspectFileParameter;
    //地震信息id
    private int eqid;
    public boolean hasImage = false;
    private EarthQuakeQuickReportModel model;
    private String report_type;
    //用于处理zip包下载完成后的操作
    private EmergencyDownloadReceiver mReceiver;
    private EmergencyPlanModel emergency;

    @Override
    public List<InspectItem> getDataSource() {
        EventBusUtil.register(this);
        LoadingDialogUtil.show(getActivity(), R.string.event_report_get_params);
        report_type = getActivity().getIntent().getStringExtra(REPORT_TYPE);
        if (!StringUtil.isEmpty(report_type)) {
            if (report_type.equalsIgnoreCase(REPORT_TYPE_NEW)) {
                eqid = getActivity().getIntent().getIntExtra("gid", 0);
                EmergencyService.getInstance().getEQQuickReportInspectInfos(ServiceUrlManager.getEQQuickReportInspectInfoUrl(), new GetEQReportZQSBInspectInfoParameter(),
                        getRequestId());
            } else if (report_type.equalsIgnoreCase(REPORT_TYPE_UPDATE)) {
                model = (EarthQuakeQuickReportModel) getActivity().getIntent().getSerializableExtra("report_item");
                eqid = model.getEarthQuakeId();
                EmergencyService.getInstance().getEQQuickReportInspectInfosForUpdate(ServiceUrlManager.getEQUpdateInspectInfoUrl(), new GetEQUpdateZQSBInspectInfoParameter(model.getGid()),
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
        if (report_type.equalsIgnoreCase(REPORT_TYPE_NEW)) {
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
                    if (report_type.equalsIgnoreCase(REPORT_TYPE_NEW)) {
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
                    if (report_type.equalsIgnoreCase(REPORT_TYPE_NEW)) {
                        reSetMediaPath(item, strs);
                    } else {
                        reSetMediaPath(item, strs, dirPath, isUnzip);
                    }
                    mediaPaths.addAll(Arrays.asList(strs));
                    continue;
                } else if ((item.getType() == EInspectItemType.VIDEO) && !StringUtil.isBlank(item.getValue())) {
                    hasImage = true;
                    String[] strs = item.getValue().split(",");
                    if (report_type.equalsIgnoreCase(REPORT_TYPE_NEW)) {
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

    private class MyZipThread extends Thread {
        @Override
        public void run() {
            Message msg = Message.obtain();
            InspectItem resItem = null;
            if (report_type.equalsIgnoreCase(REPORT_TYPE_NEW)) {
                resItem = InspectFileManager.readInspectItemFromFile(InspectFileManager.getInstance().getInspectPath());
            } else {
                resItem = InspectFileManager.readInspectItemFromFile(InspectFileManager.getInstance().getInspectPath4Update());
            }

            if (null == resItem) {
                msg.what = FileUploader.UPLOAD_FAIL;
                msg.obj = HostApplication.getApplication().getResources().getString(R.string.str_emergency_getinspect_error);
            } else {
                if (hasImage) {
                    if (moveMediaFile(report_type.equalsIgnoreCase(REPORT_TYPE_NEW))) {
                        //上报之前  先更新数据
                        if (report_type.equalsIgnoreCase(REPORT_TYPE_UPDATE)) {
                            inspectItems2Upload.remove(0);
                        }
                        mInspectParentItem.setChilds(inspectItems2Upload);
                        boolean isZipSuccess = false;
                        if (report_type.equalsIgnoreCase(REPORT_TYPE_NEW)) {
                            InspectFileManager.wirteInspectIntoFile(mInspectParentItem, InspectFileManager.getInstance().getInspectPath());
                            isZipSuccess = InspectFileManager.zipInspectFile(InspectFileManager.getInstance().getSrcPath(), InspectFileManager.getInstance().getZipPath());
                        } else if (report_type.equalsIgnoreCase(REPORT_TYPE_UPDATE)) {
                            InspectFileManager.wirteInspectIntoFile(mInspectParentItem, InspectFileManager.getInstance().getInspectPath4Update());
                            isZipSuccess = InspectFileManager.zipInspectFile(InspectFileManager.getInstance().getSrcPath4Update(), InspectFileManager.getInstance()
                                    .getZipPath());
                        }
                        if (isZipSuccess) {
                            msg.what = READY_TO_SUBMIT;
                            msg.obj = InspectFileManager.getInstance().getZipPath();
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
                    if (report_type.equalsIgnoreCase(REPORT_TYPE_UPDATE)) {
                        inspectItems2Upload.remove(0);
                    }
                    mInspectParentItem.setChilds(inspectItems2Upload);
                    boolean isZipSuccess = false;
                    if (report_type.equalsIgnoreCase(REPORT_TYPE_NEW)) {
                        InspectFileManager.wirteInspectIntoFile(mInspectParentItem, InspectFileManager.getInstance().getInspectPath());
                        isZipSuccess = InspectFileManager.zipInspectFile(InspectFileManager.getInstance().getSrcPath(), InspectFileManager.getInstance().getZipPath());
                    } else if (report_type.equalsIgnoreCase(REPORT_TYPE_UPDATE)) {
                        InspectFileManager.wirteInspectIntoFile(mInspectParentItem, InspectFileManager.getInstance().getInspectPath4Update());
                        isZipSuccess = InspectFileManager.zipInspectFile(InspectFileManager.getInstance().getSrcPath4Update(), InspectFileManager.getInstance()
                                .getZipPath());
                    }
                    if (isZipSuccess) {
                        msg.what = READY_TO_SUBMIT;
                        msg.obj = InspectFileManager.getInstance().getZipPath();
                    } else {
                        msg.what = FileUploader.UPLOAD_FAIL;
                        msg.obj = HostApplication.getApplication().getResources().getString(R.string.str_emergency_zip_errer);
                    }
                }
            }
            mReportHandler.sendMessage(msg);
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
                sb.append(mStr).append(",");
            } else {
                sb.append(mStr);
            }
        }
        item.setValue(sb.toString());
    }

    /**
     * @param item
     * @param strs
     * @param dirPath  ZIP解压后文件的存放路径
     * @param isUnZip  是否是从ZIP解压出来的InspectItem
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

    /**
     * 对新的速报  移动媒体文件到指定目录
     *
     * @return
     */
    private boolean moveMediaFile(boolean flag) {
        for (int i = 0; i < mediaPaths.size(); i++) {
            String path = mediaPaths.get(i);
            File srcFile = new File(path);
            String mediaFileName = "";
            if(!path.contains("/")){
                mediaFileName = path;
            }else{
                mediaFileName = path.substring(path.lastIndexOf("/"));
            }
            File tarFile = null;
            if (flag){
                tarFile = new File(InspectFileManager.getInstance().getSrcPath() + mediaFileName);
            }else{
                tarFile = new File(InspectFileManager.getInstance().getSrcPath4Update() + mediaFileName);
            }
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

    private class EventReportHandler extends Handler {
        private WeakReference<EarthQuakeZQSBReportOperator> operator;

        public EventReportHandler(EarthQuakeZQSBReportOperator operator) {
            this.operator = new WeakReference<EarthQuakeZQSBReportOperator>(operator);
        }

        @Override
        public void handleMessage(Message msg) {
            EarthQuakeZQSBReportOperator operator = this.operator.get();
            if (null == operator) {
                return;
            }
            CustomReportActivity1 activity = operator.getActivity();
            if (null != activity) {
                switch (msg.what) {
                    case READY_TO_SUBMIT:
                        LoadingDialogUtil.show(getActivity(), R.string.uploading_now);
                        String zipPath = (String) msg.obj;
                        List<String> paths = new ArrayList<String>();
                        paths.add(zipPath);

                        if (!StringUtil.isEmpty(report_type)) {
                            if (report_type.equalsIgnoreCase(REPORT_TYPE_NEW)) {
                                reportZQSBInspectFileParameter = new GetEQReportZQSBInspectFileParameter(String.valueOf(eqid));
                                FileUploader.execute(paths, getServiceUrl(), operator.reportZQSBInspectFileParameter.toMap(), operator.mReportHandler);
                            } else if (report_type.equalsIgnoreCase(REPORT_TYPE_UPDATE)) {
                                updateZQSBInspectFileParameter = new GetEQUpdateZQSBInspectFileParameter(model.getGid());
                                FileUploader.execute(paths, getServiceUrl(), operator.updateZQSBInspectFileParameter.toMap(), operator.mReportHandler);
                            }
                        }
                        break;
                    case FileUploader.UPLOAD_ALL_FILES_DONE:
                        if (null != activity) {
                            LoadingDialogUtil.setCancelable(true);
                            LoadingDialogUtil.dismiss();
                            InspectFileManager.deleteInspectFile();
                            ToastUtil.showLong(activity.getResources().getString(R.string.upload_success));
                            operator.notifyBackEvent(activity);
                        }
                        break;
                    case FileUploader.UPLOAD_FAIL:
                        if (null != activity) {
                            LoadingDialogUtil.setCancelable(true);
                            LoadingDialogUtil.dismiss();
                            InspectFileManager.deleteInspectFile();
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
    public void notifyBackEvent(CustomReportActivity1 activity) {
        if (null != activity) {
            EventBusUtil.unregister(this);
            unRegister();
            activity.finish();
        }
    }

    protected String getServiceUrl() {
        if (report_type.equalsIgnoreCase(REPORT_TYPE_NEW)) {
            return ServiceUrlManager.reportEQZqsb();
        } else if (report_type.equalsIgnoreCase(REPORT_TYPE_UPDATE)) {
            return ServiceUrlManager.updateEQZqsb();
        }
        return null;
    }

    protected int getRequestId() {
        if (report_type.equalsIgnoreCase(REPORT_TYPE_NEW)) {
            return ResponseEventStatus.EMERGENCY_GET_EQQUICKREPORT_NEW_INSPECT_INFO;
        } else if (report_type.equalsIgnoreCase(REPORT_TYPE_UPDATE)) {
            return ResponseEventStatus.EMERGENCY_GET_EQQUICKREPORT_UPDATE_INSPECT_INFO;
        }
        return 0;
    }

    /**
     * 对新建灾情速报表单处理
     *
     * @param event
     */
    private void handleGetEventReportParams(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        mInspectChildsItems = new ArrayList<InspectItem>();
        mInspectParentItem = event.getData();
        //将检查项写入文件
        InspectFileManager.initInspectFile(eqid);
        if (null != mInspectParentItem && (!ListUtil.isEmpty(mInspectParentItem.getChilds()))) {
            InspectFileManager.wirteInspectIntoFile(mInspectParentItem, InspectFileManager.getInstance().getInspectPath());
            if (report_type.equalsIgnoreCase(REPORT_TYPE_UPDATE)) {
                getBaseModelValue(model);
            }
            for (InspectItem item : mInspectParentItem.getChilds()) {
                item.setVisible(true);
                mInspectChildsItems.add(item);
            }
            getActivity().fillDatas4Earthquake(mInspectChildsItems);
        } else {
            ToastUtil.showLong(getActivity().getResources().getString(R.string.str_emergency_get_inspect_error));
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
        InspectFileManager.initInspectFile(eqid);
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

//            zipUrl = new String("http://192.168.8.206:8888/map/czdz/zqsb/20170105151701921.zip");
            String filename = zipUrl.substring(zipUrl.lastIndexOf("/") + 1, zipUrl.length());
            String filePath = zipUrl.substring(0, zipUrl.lastIndexOf("/"));
            InspectFileManager.initFilePath4UpdateInspect(model.getGid());
            InspectFileManager.getInstance().setZipPath4Update(filename);
            //下载压缩包
            emergency = new EmergencyPlanModel();
            emergency.setSavePath(InspectFileManager.getInstance().getSrcPath4Update());
            emergency.setDoc(filename);
            emergency.setUrl(filePath + "/" + filename);
            DownloadService.intentDownload(getActivity(), filePath, emergency);
            LoadingDialogUtil.show(getActivity(),"正在处理上报数据，请稍后……");
        }
    }

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


    public void onEventMainThread(UIEvent event){
       switch (event.getId()){
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
            case ResponseEventStatus.EMERGENCY_GET_EQQUICKREPORT_NEW_INSPECT_INFO:
                handleGetEventReportParams(event);
                break;
            case ResponseEventStatus.EMERGENCY_GET_EQQUICKREPORT_UPDATE_INSPECT_INFO:
                handleGetEventUpdateParams(event);
                break;
            default:
                break;
        }
    }

    public List<InspectItem> getmInspectItems() {
        return mInspectChildsItems;
    }

    //注册广播
    private void register() {
        mReceiver = new EmergencyDownloadReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.ACTION_DOWNLOAD_BROAD_CAST);
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, intentFilter);

    }

    //反注册广播
    private void unRegister() {
        if (mReceiver != null) {
//            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
        }
    }

    //文件下载广播接收
    private class EmergencyDownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action == null || !action.equals(DownloadService.ACTION_DOWNLOAD_BROAD_CAST)) {
                return;
            }

            final EmergencyPlanModel tmpInfo = (EmergencyPlanModel) intent.getSerializableExtra(DownloadService.EXTRA_DETAIL_INFO);
            if (tmpInfo == null) {
                return;
            }
            final int status = tmpInfo.getStatus();
            switch (status) {
                case EmergencyPlanModel.STATUS_CONNECTING:
                    emergency.setStatus(EmergencyPlanModel.STATUS_CONNECTING);
                    updateStatus(emergency.getStatus());
                    break;

                case EmergencyPlanModel.STATUS_DOWNLOADING:
                    emergency.setStatus(EmergencyPlanModel.STATUS_DOWNLOADING);
                    emergency.setProgress(tmpInfo.getProgress());
                    emergency.setDownloadPerSize(tmpInfo.getDownloadPerSize());
                    updateStatus(emergency.getStatus());
                    break;
                case EmergencyPlanModel.STATUS_COMPLETE:
                    emergency.setStatus(EmergencyPlanModel.STATUS_COMPLETE);
                    emergency.setProgress(tmpInfo.getProgress());
                    emergency.setDownloadPerSize(tmpInfo.getDownloadPerSize());
                    updateStatus(emergency.getStatus());
                    break;
                case EmergencyPlanModel.STATUS_PAUSED:
                    emergency.setStatus(EmergencyPlanModel.STATUS_PAUSED);
                    updateStatus(emergency.getStatus());
                    break;
                case EmergencyPlanModel.STATUS_NOT_DOWNLOAD:
                    emergency.setStatus(EmergencyPlanModel.STATUS_NOT_DOWNLOAD);
                    emergency.setProgress(tmpInfo.getProgress());
                    emergency.setDownloadPerSize(tmpInfo.getDownloadPerSize());
                    updateStatus(emergency.getStatus());
                    break;
                case EmergencyPlanModel.STATUS_DOWNLOAD_ERROR:
                    emergency.setStatus(EmergencyPlanModel.STATUS_DOWNLOAD_ERROR);
                    emergency.setDownloadPerSize("");
                    updateStatus(emergency.getStatus());
                    break;
            }
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
                        if (report_type.equalsIgnoreCase(REPORT_TYPE_UPDATE)) {
                            getBaseModelValue(model);
                        }
                        for (InspectItem item : mInspectParentItem.getChilds()) {
                            item.setVisible(true);
                            mInspectChildsItems.add(item);
                        }
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
}
