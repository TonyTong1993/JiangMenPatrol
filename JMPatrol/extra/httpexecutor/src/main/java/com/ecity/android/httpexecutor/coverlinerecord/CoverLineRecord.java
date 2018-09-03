package com.ecity.android.httpexecutor.coverlinerecord;

import java.util.Map;

/**
 * @author xiaobei
 * @date 2018/7/23
 */
public class CoverLineRecord {
    private String id;
    private int userId;
    private String createdOn;
    private String uploadedOn;
    private int failedCount;
    private Map<String, String> uploadParameter;
    private String url;
    private int requestId;
    private boolean isPost;
    private String uuid;
    private int lineId;

    public CoverLineRecord(String id, int userId, String createdOn, String uploadedOn, int failedCount, Map<String, String> uploadParameter, String url, int requestId, boolean isPost, String uuid, int lineId) {
        this.id = id;
        this.userId = userId;
        this.createdOn = createdOn;
        this.uploadedOn = uploadedOn;
        this.failedCount = failedCount;
        this.uploadParameter = uploadParameter;
        this.url = url;
        this.requestId = requestId;
        this.isPost = isPost;
        this.uuid = uuid;
        this.lineId = lineId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

    public Map<String, String> getUploadParameter() {
        return uploadParameter;
    }

    public void setUploadParameter(Map<String, String> uploadParameter) {
        this.uploadParameter = uploadParameter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean post) {
        isPost = post;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }
}
