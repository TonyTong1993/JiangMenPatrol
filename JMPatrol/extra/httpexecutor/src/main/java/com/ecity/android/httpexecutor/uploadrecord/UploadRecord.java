package com.ecity.android.httpexecutor.uploadrecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by jonathanma on 20/4/2017.
 */

public class UploadRecord {
    private String id;
    private int userId;
    private String createdOn;
    private String uploadedOn;
    private int failedCount;
    private Map<String, String> uploadParameter;
    private String url;
    private int requestId;
    private boolean isPost;

    public UploadRecord(String id, int userId, String createdOn, String uploadedOn, Map<String, String> uploadParameter, int failedCount, String url, int requestId, boolean isPost) {
        this.id = id;
        this.userId = userId;
        this.createdOn = createdOn;
        this.uploadedOn = uploadedOn;
        this.uploadParameter = uploadParameter;
        this.failedCount = failedCount;
        this.url = url;
        this.requestId = requestId;
        this.isPost = isPost;
    }

    public String getPersistenceInfo() {
        JSONObject json = new JSONObject();

        try {
            json.put("id", id);
            json.put("userId", userId);
            json.put("createdOn", createdOn);
            json.put("uploadedOn", uploadedOn);
            json.put("failedCount", failedCount);
            json.put("uploadParams", new JSONObject(uploadParameter));
            json.put("url", url);
            json.put("requestId", requestId);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return json.toString();
    }

    public String getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getUploadedOn() {
        return uploadedOn;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public Map<String, String> getUploadParameter() {
        return uploadParameter;
    }

    public String getUrl() {
        return url;
    }

    public int getRequestId() {
        return requestId;
    }

    public boolean isPost() {
        return isPost;
    }
}
