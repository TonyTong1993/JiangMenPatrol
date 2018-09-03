package com.ecity.android.httpexecutor.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author yongzhan
 * @date 2017/12/26
 */
@Entity
public class RequestRecord {
    @Id
    private Long recordId;
    private String id;
    private int userId;
    private String createdOn;
    private String uploadedOn;
    private int failedCount;
    private String uploadParameter;
    private String url;
    private int requestId;
    private boolean isPost;

    @Generated(hash = 388491591)
    public RequestRecord(Long recordId, String id, int userId, String createdOn,
            String uploadedOn, int failedCount, String uploadParameter, String url,
            int requestId, boolean isPost) {
        this.recordId = recordId;
        this.id = id;
        this.userId = userId;
        this.createdOn = createdOn;
        this.uploadedOn = uploadedOn;
        this.failedCount = failedCount;
        this.uploadParameter = uploadParameter;
        this.url = url;
        this.requestId = requestId;
        this.isPost = isPost;
    }
    @Generated(hash = 1183636705)
    public RequestRecord() {
    }
    public Long getRecordId() {
        return this.recordId;
    }
    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getUserId() {
        return this.userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getCreatedOn() {
        return this.createdOn;
    }
    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
    public String getUploadedOn() {
        return this.uploadedOn;
    }
    public void setUploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
    }
    public int getFailedCount() {
        return this.failedCount;
    }
    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }
    public String getUploadParameter() {
        return this.uploadParameter;
    }
    public void setUploadParameter(String uploadParameter) {
        this.uploadParameter = uploadParameter;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public int getRequestId() {
        return this.requestId;
    }
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
    public boolean getIsPost() {
        return this.isPost;
    }
    public void setIsPost(boolean isPost) {
        this.isPost = isPost;
    }
}
