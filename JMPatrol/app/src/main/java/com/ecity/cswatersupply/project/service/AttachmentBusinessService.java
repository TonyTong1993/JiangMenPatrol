package com.ecity.cswatersupply.project.service;

import com.ecity.cswatersupply.project.model.Attachment;
import com.ecity.cswatersupply.project.model.db.AttachmentBeanXtd;
import com.ecity.cswatersupply.project.model.db.AttachmentDao;
import com.ecity.cswatersupply.utils.DateUtil;

/**
 * Handle logic about Document with {@link AttachmentDao}.
 * @author Ma Qianli
 *
 */
public class AttachmentBusinessService {
    private static AttachmentBusinessService instance;

    private AttachmentBusinessService() {
    }

    static {
        instance = new AttachmentBusinessService();
    }

    public static AttachmentBusinessService getInstance() {
        return instance;
    }

    public Attachment get(String uniqueName) {
        String where = " uniqueName = ?";
        AttachmentBeanXtd bean = AttachmentDao.getInstance().queryOne(where, new String[] { uniqueName });
        if (bean == null) {
            return null;
        }

        return toBusinessModel(bean);
    }

    public void save(Attachment document) {
        AttachmentBeanXtd bean = new AttachmentBeanXtd();
        bean.setUrl(document.getUrl());
        bean.setStatus(AttachmentBeanXtd.STATUS_NEW);
        bean.setFileName(document.getName());
        bean.setUniqueName(document.getUniqueName());

        AttachmentDao.getInstance().save(bean);
    }

    public void markAsDownloading(String uniqueName) {
        updateStatus(uniqueName, AttachmentBeanXtd.STATUS_DOWNLOADING);
    }

    public void markAsNew(String uniqueName) {
        updateStatus(uniqueName, AttachmentBeanXtd.STATUS_NEW);
        saveLocalPath(uniqueName, "");
        updateDownloadTime(uniqueName, null);
    }

    public void markAsDownloaded(String uniqueName) {
        updateStatus(uniqueName, AttachmentBeanXtd.STATUS_DOWNLOADED);
        updateDownloadTime(uniqueName, DateUtil.getCurrentTime());
    }

    public String getLocalPath(String uniqueName) {
        return AttachmentDao.getInstance().getLocalPath(uniqueName);
    }

    public void saveLocalPath(String uniqueName, String localPath) {
        AttachmentDao.getInstance().saveLocalPath(uniqueName, localPath);
    }

    public void updateDownloadTime(String uniqueName, String downloadedOn) {
        AttachmentDao.getInstance().updateDownloadTime(uniqueName, downloadedOn);
    }

    private void updateStatus(String uniqueName, String status) {
        AttachmentDao.getInstance().updateStatus(uniqueName, status);
    }

    private Attachment toBusinessModel(AttachmentBeanXtd bean) {
        if (bean == null) {
            return null;
        }

        Attachment document = new Attachment();
        document.setUrl(bean.getUrl());
        document.setName(bean.getFileName());
        document.setLocalPath(bean.getLocalPath());
        boolean isDownloading = AttachmentBeanXtd.STATUS_DOWNLOADING.equals(bean.getStatus());
        document.setDownloading(isDownloading);
        boolean isDownloaded = AttachmentBeanXtd.STATUS_DOWNLOADED.equals(bean.getStatus());
        document.setDownloaded(isDownloaded);
        document.setUniqueName(bean.getUniqueName());

        return document;
    }
}
