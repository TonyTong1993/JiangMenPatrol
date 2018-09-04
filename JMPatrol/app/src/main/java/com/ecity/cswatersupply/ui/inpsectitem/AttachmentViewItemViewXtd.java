package com.ecity.cswatersupply.ui.inpsectitem;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.project.adapter.AttachmentListAdapter;
import com.ecity.cswatersupply.project.model.Attachment;
import com.ecity.cswatersupply.project.network.response.GetProjectAttachmentResponse;
import com.ecity.cswatersupply.project.network.response.adapter.AttachmentAdapter;
import com.ecity.cswatersupply.project.service.AttachmentBusinessService;
import com.ecity.cswatersupply.project.service.ProjectService;
import com.ecity.cswatersupply.ui.widght.ListViewForScrollView;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;

public class AttachmentViewItemViewXtd extends ABaseInspectItemView {
    private TextView tv_item_value;
    private ListViewForScrollView lvAddachment;
    private String guid;
    private AttachmentListAdapter attachmentListAdapter;
    private List<Attachment> list;

    private boolean finishOneTime = true;
    private boolean nonOneTime = true;

    @Override
    protected void setup(View contentView) {
        EventBusUtil.register(this);
        lvAddachment = (ListViewForScrollView) contentView.findViewById(R.id.lv_attachments);
        tv_item_value = (TextView) contentView.findViewById(R.id.tv_item_value);
        guid = mInspectItem.getValue();

        attachmentListAdapter = new AttachmentListAdapter(context);

        setOnClickListener();
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_attachment_select;
    }

    private void setOnClickListener() {
        tv_item_value.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LoadingDialogUtil.show(context, "正在查询相关附件");
                ProjectService.getInstance().getAttachment(guid);
                nonOneTime = true;
            }
        });
    }

    private void handleGetProjectAttachment(ResponseEvent event) {
        GetProjectAttachmentResponse data = event.getData();
        if (!ListUtil.isEmpty(data.getFeatures())) {
            if (!data.getFeatures().get(0).getGuid().equals(guid)) {
                return;
            }
        } else {
            if (nonOneTime) {
                ToastUtil.show("未查到相关附件", 0);
                nonOneTime = false;
            }
            return;
        }

        lvAddachment.setAdapter(attachmentListAdapter);
        list = AttachmentAdapter.getInstance().adapt(data);
        attachmentListAdapter.setList(list);
    }

    private void handleDownloadFileDoneEvent(ResponseEvent event) {
        String url = event.getData();
        int lastIndex = url.lastIndexOf("/");
        String uniqueName = url.substring(lastIndex + 1);
        markDocumentAsDownloaded(uniqueName);
        HostApplication.getApplication().removeDownloadingFile(uniqueName);
    }

    private void markDocumentAsDownloaded(String uniqueName) {
        if (ListUtil.isEmpty(list)) {
            return;
        }
        for (Attachment document : list) {
            if (uniqueName.equals(document.getName())) {
                document.setDownloading(false);
                document.setDownloaded(true);
                AttachmentBusinessService.getInstance().markAsDownloaded(document.getUniqueName());
            }
        }

        if (finishOneTime) {
            attachmentListAdapter.setList(list);
            ToastUtil.show("请确保手机上有能够打开该资料的应用软件", 0);
            finishOneTime = false;
        }
    }

    private void markDocumentAsNew(String url) {
        for (Attachment document : list) {
            if (url.equals(document.getUrl())) {
                document.setDownloading(false);
                document.setDownloaded(false);
                AttachmentBusinessService.getInstance().markAsNew(document.getUniqueName());
                HostApplication.getApplication().removeDownloadingFile(document.getUniqueName());
            }
        }

        attachmentListAdapter.setList(list);
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());

            if (event.getId() == ResponseEventStatus.FILE_OPERATION_DOWNLOAD_FINISH) {
                String url = event.getData();
                markDocumentAsNew(url);
            }
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.PROJECT_GET_PROJECT_ATTACHMENT:
                LoadingDialogUtil.dismiss();
                handleGetProjectAttachment(event);
                break;
            case ResponseEventStatus.FILE_OPERATION_DOWNLOAD_FINISH:
            case ResponseEventStatus.FILE_OPERATION_UPDATE_PROGRESS:
                handleDownloadFileDoneEvent(event);
                break;
            default:
                break;
        }
    }

}
