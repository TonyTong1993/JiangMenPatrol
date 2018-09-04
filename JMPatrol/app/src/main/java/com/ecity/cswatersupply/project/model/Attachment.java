package com.ecity.cswatersupply.project.model;

import com.ecity.cswatersupply.model.User;

public class Attachment implements Comparable<Attachment> {

    private String id;
    private String name;
    private String uniqueName;
    private String url;
    private User createdBy;
    private String createdOn;
    private String downloadedOn;
    private boolean isDownloaded;
    private boolean isDownloading;
    private String localPath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return local file path if this file has been downloaded, otherwise url in server. 
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url local file path if this file has been downloaded, otherwise url in server.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean isDownloaded) {
        this.isDownloaded = isDownloaded;
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(boolean isDownloading) {
        this.isDownloading = isDownloading;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getDownloadedOn() {
        return downloadedOn;
    }

    public void setDownloadedOn(String downloadedOn) {
        this.downloadedOn = downloadedOn;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    @Override
    public int compareTo(Attachment another) {
        if (another == null) {
            return 1;
        }

        if (getCreatedOn() == null) {
            return -1;
        }

        return getCreatedOn().compareToIgnoreCase(another.getCreatedOn());
    }
}
