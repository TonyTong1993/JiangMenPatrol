package com.ecity.cswatersupply.project.network.response.adapter;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.project.model.Attachment;
import com.ecity.cswatersupply.project.model.FileModel;
import com.ecity.cswatersupply.project.network.response.GetProjectAttachmentResponse;
import com.ecity.cswatersupply.project.service.AttachmentBusinessService;
import com.z3app.android.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class AttachmentAdapter {
    private static AttachmentAdapter instance;
    private String baseUrl;

    static {
        instance = new AttachmentAdapter();
    }

    public static AttachmentAdapter getInstance() {
        return instance;
    }

    public List<Attachment> adapt(GetProjectAttachmentResponse response) {
        baseUrl = response.getUrl();
        List<FileModel> features = response.getFeatures();
        List<Attachment> attachments = new ArrayList<Attachment>();
        for (FileModel serverModel : features) {
            attachments.add(adapt(serverModel));
        }

        return attachments;
    }

    public Attachment adapt(FileModel serverModel) {
        Attachment attachment = new Attachment();

        String url = baseUrl + serverModel.getFilename();
        attachment.setName(serverModel.getName());
        attachment.setUniqueName(serverModel.getFilename());
        attachment.setUrl(url);
        attachment.setCreatedOn(serverModel.getAdddate());
        //        User user = new User();
        //        user.setId(serverModel.getCreaterid());
        //        user.setRealName(serverModel.getCreater());
        //        attachment.setCreatedBy(user);

        updateDownloadInfo(attachment);

        return attachment;
    }

    private void updateDownloadInfo(Attachment attachment) {
        Attachment tempModel = AttachmentBusinessService.getInstance().get(attachment.getUniqueName());
        if (tempModel != null) {
            if (tempModel.isDownloaded()) {
                String localPath = tempModel.getLocalPath();
//                boolean isExist = FileHelper.getInstance().hasFile(localPath);
                boolean isExist = FileUtil.getInstance(null).hasFile(localPath);
                if (!isExist) { // File may have been deleted.
                    AttachmentBusinessService.getInstance().markAsNew(tempModel.getUniqueName());
                    tempModel.setLocalPath("");
                    tempModel.setDownloading(false);
                    tempModel.setDownloaded(false);
                    tempModel.setDownloadedOn("");
                }
            }

            if (tempModel.isDownloading() && !HostApplication.getApplication().isDownloadingFile(attachment.getUniqueName())) {
                // Handle the case that APP is killed or stopped while the attachment is on downloading. Then launch the APP again.
                AttachmentBusinessService.getInstance().markAsNew(attachment.getUniqueName());
                tempModel = AttachmentBusinessService.getInstance().get(attachment.getUniqueName()); // Get new status.
            }

            attachment.setLocalPath(tempModel.getLocalPath());
            attachment.setDownloading(tempModel.isDownloading());
            attachment.setDownloaded(tempModel.isDownloaded());
            attachment.setDownloadedOn(tempModel.getDownloadedOn());
        } else {
            AttachmentBusinessService.getInstance().save(attachment);
        }
    }
}