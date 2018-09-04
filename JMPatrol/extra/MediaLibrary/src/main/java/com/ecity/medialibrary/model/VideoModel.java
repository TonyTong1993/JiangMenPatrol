package com.ecity.medialibrary.model;

import java.io.Serializable;

import com.z3app.android.util.StringUtil;

public class VideoModel implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final String TYPE_PHOTO = "photo";
    public static final String TYPE_ZIP = "zip";
    public static final String TYPE_VIDEO = "video";

    private String path;
    private String type;
    private String description;
    private String uploadedOn;
    private boolean isReadOnly;
    private boolean isPrivate;

    public VideoModel() {
    }

    public VideoModel(String path, String type) {
        this.path = path;
        if (!StringUtil.isBlank(path)) {
            this.type = path.endsWith(".mp4") ? TYPE_VIDEO : TYPE_PHOTO;
        }

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}
