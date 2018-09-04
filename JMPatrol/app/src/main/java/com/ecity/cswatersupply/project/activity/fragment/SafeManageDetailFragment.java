package com.ecity.cswatersupply.project.activity.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.checkitem.InspectItemAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.FileUploader;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;
import com.ecity.cswatersupply.project.activity.SafeManageDetailActivity;
import com.ecity.cswatersupply.project.network.request.PostSafeCreatPageParameter;
import com.ecity.cswatersupply.project.network.response.SafeDetailStepModel;
import com.ecity.cswatersupply.project.service.ProjectService;
import com.ecity.cswatersupply.project.service.ProjectServiceUrlManager;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.FilesToZip;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.shizhefei.fragment.LazyFragment;
import com.z3app.android.util.FileUtil;
import com.z3app.android.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/26.
 */

public class SafeManageDetailFragment extends LazyFragment {

    private CustomViewInflater customViewInflater;
    private LinearLayout layoutContainer;
    private int requestId;
    private String step;
    private SafeDetailStepModel stepdata;
    private boolean isInQuery;
    private List<InspectItem> inspectItems;
    private InspectItem itemBackExplain;
    private List<String> attachmentPathList;
    private String zipPath;
    private static final int READY_TO_SUBMIT = 0x00000;
    private AttachUploadHandler mHandler = new AttachUploadHandler();
    private String newGuidStr;


    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_project_info);
        EventBusUtil.register(this);
        initData();
        initView();
    }

    public SafeManageDetailFragment() {
    }

    public SafeManageDetailFragment(int eventId, String step, SafeDetailStepModel stepdata) {
        this.requestId = eventId;
        this.step = step;
        this.stepdata = stepdata;
    }

    @Override
    protected void onDestroyViewLazy() {
        EventBusUtil.unregister(this);
        super.onDestroyViewLazy();
    }

    private void initData() {
        isInQuery = true;
        User currentUser = HostApplication.getApplication().getCurrentUser();
        SafeManageDetailActivity activity = (SafeManageDetailActivity) getActivity();
        ProjectService.getInstance().getEventInfo(requestId, step, activity.getModel(), currentUser, stepdata);
    }

    int displayViewId;
    View inflate;
    View checkItemView;
    boolean isDisplayTabView;

    private void handleGetSafeDetailInfoResponse(ResponseEvent event) {
        JSONObject json = event.getData();
        inspectItems = InspectItemAdapter.adaptItems(json);
        customViewInflater = new CustomViewInflater(getActivity());
        for (int i = 0; i < inspectItems.size(); i++) {
            layoutContainer.addView(customViewInflater.inflate(inspectItems.get(i)));
            if (inspectItems.get(i).getType().equals(EInspectItemType.RADIO)) {
                inflate = customViewInflater.inflate(inspectItems.get(i));
            }
            if (inspectItems.get(i).getName().equals("checkopinion")) {
                checkItemView = customViewInflater.inflate(inspectItems.get(i));
            }
        }

        if (event.getId() == ResponseEventStatus.PROJECT_SAFE_MANAGE_GET_DETAIL_INFO2) {
            itemBackExplain = new InspectItem();
            itemBackExplain.setAlias("驳回意见");
            itemBackExplain.setType(EInspectItemType.TEXTEXT);
            itemBackExplain.setVisible(true);
            itemBackExplain.setEdit(true);
            itemBackExplain.setName("checkopinion1");
            View view = customViewInflater.inflate(itemBackExplain);
            displayViewId = view.getId();
            layoutContainer.addView(view);

            selectYesDisplay();

            isDisplayTabView = true;
        }


        isButtonView();
    }

    private void isButtonView() {
        Button btn_submit = (Button) findViewById(R.id.btn_submit);

        for (InspectItem item : inspectItems) {
            if (item.isEdit()) {
                btn_submit.setVisibility(View.VISIBLE);
                break;
            }
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestId == ResponseEventStatus.PROJECT_SAFE_MANAGE_GET_DETAIL_INFO2) {
                    handleValueForItemOpinion2();
                } else {
                    ToastUtil.show("第三页", 0);
                }
                clickSubmitButton();
            }
        });
    }

    private void clickSubmitButton() {
        List<InspectItem> infos = InspectItemUtil.mergeAllItemsInline(inspectItems);
        for (InspectItem info : infos) {
            if (info.getType() == EInspectItemType.ATTACHMENT_UPLOAD) {
                String[] strs = info.getValue().split(Constants.IMAGE_SPLIT_STRING);
                attachmentPathList = Arrays.asList(strs);
            }
        }

        new MyZipThread(attachmentPathList, "").start();
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

    private void uploadFiles() {
        if (!StringUtil.isEmpty(zipPath)) {
            LoadingDialogUtil.updateMessage(R.string.workorder_report_images);
            List<String> uploadPaths = new ArrayList<String>();
            uploadPaths.add(zipPath);
            Map<String, String> params = new HashMap<>();
            FileUploader.execute(uploadPaths, ProjectServiceUrlManager.getInstance().uploadFile(), params, mHandler);
        } else {
            uploadForm();
        }
    }

    private void uploadForm() {
        LoadingDialogUtil.show(getActivity(), "正在提交表单");
        ProjectService.getInstance().postCommonCheck(getPostUrl(), ResponseEventStatus.PROJECT_SAFE_CREATE_PAGE_SUBMIT_DONE, getParameter());
    }

    private String getPostUrl() {
        if (requestId == ResponseEventStatus.PROJECT_SAFE_MANAGE_GET_DETAIL_INFO2) {
            return ProjectServiceUrlManager.getInstance().saveCheckOrZgInfo();
        } else {
            return ProjectServiceUrlManager.getInstance().saveZgFeedbak();
        }
    }

    private AReportInspectItemParameter getParameter() {
        for (InspectItem item : inspectItems) {
            if (item.getType() == EInspectItemType.ATTACHMENT_UPLOAD) {
                item.setValue(newGuidStr);
                break;
            }
        }

        SafeManageDetailActivity activity = (SafeManageDetailActivity) getActivity();
        return new PostSafeCreatPageParameter(inspectItems, activity.getModel().getProid(),activity.getModel().getGid());
    }

    //处理第二tab页审核信息的赋值
    private void handleValueForItemOpinion2() {
        String value = itemBackExplain.getValue();
        if (!isDisplay) {
            for (InspectItem item : inspectItems) {
                if (item.getName().equals("checkopinion")) {
                    item.setValue(value);
                    break;
                }
            }
        } else {
            LinearLayout childAt = (LinearLayout) layoutContainer.getChildAt(3);
            LinearLayout childAt1 = (LinearLayout) childAt.getChildAt(0);
            EditText editValue = (EditText) childAt1.getChildAt(1);
            for (InspectItem item : inspectItems) {
                if (item.getName().equals("checkopinion")) {
                    item.setValue(editValue.getText().toString());
                    break;
                }
            }
        }
    }

    private void initView() {
        layoutContainer = (LinearLayout) findViewById(R.id.ll_container);
    }

    boolean isDisplay = true;

    private void handleSelectPeopleIsDisplay(UIEvent event) {
        isDisplay = event.getData();
        if (isDisplay) {
            selectYesDisplay();
        } else {
            selectNoDisplay();
        }
    }

    private void selectYesDisplay() {
        int j = layoutContainer.getChildCount() - 1;
        for (int k = 1; k < j; k++) {
            View childAt1 = layoutContainer.getChildAt(k);
            childAt1.setVisibility(View.VISIBLE);
        }
        int i = layoutContainer.getChildCount() - 1;
        View childAt = layoutContainer.getChildAt(i);
        childAt.setVisibility(View.GONE);
    }

    private void selectNoDisplay() {
        int j = layoutContainer.getChildCount() - 1;
        for (int k = 1; k < j; k++) {
            View childAt1 = layoutContainer.getChildAt(k);
            if (inflate.getId() != childAt1.getId()) {
                childAt1.setVisibility(View.GONE);
            }
        }
        int i = layoutContainer.getChildCount() - 1;
        View childAt = layoutContainer.getChildAt(i);
        childAt.setVisibility(View.VISIBLE);
    }

    public void onEventMainThread(UIEvent event) {
        if (isDisplayTabView) {
            switch (event.getId()) {
                case UIEventStatus.PROJECT_SELECT_PEOPLE_DISPLAY:
                    handleSelectPeopleIsDisplay(event);
                    break;
                default:
                    break;
            }
        }
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        LoadingDialogUtil.dismiss();
        if ((event.getId() == requestId) && isInQuery) {
            isInQuery = false;
            if (event.isOK()) {
                handleGetSafeDetailInfoResponse(event);
            } else {
                ToastUtil.showLong(event.getMessage());
            }
        }
    }

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
