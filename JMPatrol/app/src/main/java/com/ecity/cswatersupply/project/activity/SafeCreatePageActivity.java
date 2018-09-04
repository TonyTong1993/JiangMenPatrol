package com.ecity.cswatersupply.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.checkitem.InspectItemAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.FileUploader;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;
import com.ecity.cswatersupply.project.network.request.PostSafeCreatPageParameter;
import com.ecity.cswatersupply.project.network.response.SafeProjectListModel;
import com.ecity.cswatersupply.project.service.ProjectService;
import com.ecity.cswatersupply.project.service.ProjectServiceUrlManager;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.FilesToZip;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.z3app.android.util.FileUtil;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/31.
 */

public class SafeCreatePageActivity extends BaseActivity {

    private LinearLayout layoutContainer;
    private Button btn_submit;
    private List<InspectItem> inspectItems;
    private List<String> attachmentPathList;
    private String zipPath;
    private static final int READY_TO_SUBMIT = 0x00000;
    private AttachUploadHandler mHandler = new AttachUploadHandler();
    private JSONObject uploadFormResult;
    private SafeProjectListModel projectModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_project_info);
        EventBusUtil.register(this);
        initData();
        initView();
        requestCreatePage();
        setOnClickListener();

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (CustomViewInflater.pendingViewInflater != null) {
            CustomViewInflater.pendingViewInflater.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initData() {
        attachmentPathList = new ArrayList<>();
        projectModel = (SafeProjectListModel) getIntent().getSerializableExtra(SafeManageEventListActivity.SELECT_PROJECT);
    }

    private void initView() {

        layoutContainer = (LinearLayout) findViewById(R.id.ll_container);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setVisibility(View.VISIBLE);
    }

    private void requestCreatePage() {
        User currentUser = HostApplication.getApplication().getCurrentUser();
        ProjectService.getInstance().getCreatePage(currentUser);
    }

    private void handleGetSafeCreatePageResponse(ResponseEvent event) {
        JSONObject json = event.getData();
        inspectItems = InspectItemAdapter.adaptItems(json);
        CustomViewInflater customViewInflater = new CustomViewInflater(this);
        for (int i = 0; i < inspectItems.size(); i++) {
            layoutContainer.addView(customViewInflater.inflate(inspectItems.get(i)));
        }
    }


    private void setOnClickListener() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<InspectItem> infos = InspectItemUtil.mergeAllItemsInline(SafeCreatePageActivity.this.inspectItems);
                for (InspectItem info : infos) {
                    if (info.getType() == EInspectItemType.ATTACHMENT_UPLOAD) {
                        String[] strs = info.getValue().split(Constants.IMAGE_SPLIT_STRING);
                        attachmentPathList = Arrays.asList(strs);
                    }
                }

                new MyZipThread(attachmentPathList, "").start();
            }
        });
    }

    private void uploadForm() {
        LoadingDialogUtil.show(this, "正在提交表单");
        ProjectService.getInstance().postCommonCheck(ProjectServiceUrlManager.getInstance().saveEventInfo(), ResponseEventStatus.PROJECT_SAFE_CREATE_PAGE_SUBMIT_DONE, getParameter());
    }

    private void handleReportTableDone(ResponseEvent event) {
        if (event.getStatus() == ResponseEventStatus.ERROR) {
            ToastUtil.showShort(event.getMessage());
            LoadingDialogUtil.dismiss();
            return;
        }

        uploadFormResult = event.getData();

        if (!ListUtil.isEmpty(attachmentPathList)) {
            new MyZipThread(attachmentPathList, "").start();
        } else {
            this.finish();
        }
    }

    private void uploadFiles() {
        if (!StringUtil.isEmpty(zipPath)) {
            LoadingDialogUtil.updateMessage(R.string.workorder_report_images);
            List<String> uploadPaths = new ArrayList<String>();
            uploadPaths.add(zipPath);
            Map<String, String> params = new HashMap<>();
            FileUploader.execute(uploadPaths, ProjectServiceUrlManager.getInstance().uploadFile(), params, mHandler);
        } else {
//            operator.afterReport(operator, operator.getActivity(), null);
            uploadForm();
        }
    }

    private void uploadForm1() {
        LoadingDialogUtil.show(this, "正在提交表单");
        ProjectService.getInstance().postCommonCheck(ProjectServiceUrlManager.getInstance().saveEventInfo(), ResponseEventStatus.PROJECT_SAFE_CREATE_PAGE_SUBMIT_DONE, getParameter());
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
            zipPath = FilesToZip.getInstance().zip(filePaths, id);
            Message msg = Message.obtain();
            msg.what = READY_TO_SUBMIT;
            mHandler.sendMessage(msg);
        }
    }

    private AReportInspectItemParameter getParameter() {
        for (InspectItem item : inspectItems) {
            if (item.getType() == EInspectItemType.ATTACHMENT_UPLOAD) {
                item.setValue(newGuidStr);
                break;
            }
        }

        return new PostSafeCreatPageParameter(inspectItems, projectModel.getGid(), null);
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.PROJECT_SAFE_CREATE_PAGE_INFO:
                LoadingDialogUtil.dismiss();
                handleGetSafeCreatePageResponse(event);
                break;
            case ResponseEventStatus.PROJECT_SAFE_CREATE_PAGE_SUBMIT_DONE:
                LoadingDialogUtil.dismiss();
                ToastUtil.show("上传表单成功", 0);
                this.finish();
                break;
            default:
                break;
        }
    }

    String newGuidStr = "";

    class AttachUploadHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case READY_TO_SUBMIT:
                    uploadFiles();
                    break;
                case FileUploader.UPLOAD_SINGLE_FILE_DONE:
                    File file = new File(zipPath);
                    FileUtil.deleteFile(file);//删除已经上传完成的zip包

                    JSONObject jsonObj = (JSONObject) msg.obj;

                    JSONArray jsonArray = jsonObj.optJSONArray("files");

                    String guidStr = "";
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = (JSONObject) jsonArray.get(i);
                            String attchid = obj.optString("attchid");
                            guidStr += "," + attchid;
                        }

                        newGuidStr = guidStr.substring(1);


                        uploadForm();
                    } catch (Exception e) {

                    }


                    break;
                case FileUploader.UPLOAD_ALL_FILES_DONE:
                    break;
                case FileUploader.UPLOAD_FAIL:
                    break;
                default:
                    break;
            }
        }
    }

}
