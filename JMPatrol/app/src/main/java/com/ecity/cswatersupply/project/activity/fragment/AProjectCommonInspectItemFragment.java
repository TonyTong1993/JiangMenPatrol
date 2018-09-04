package com.ecity.cswatersupply.project.activity.fragment;

public abstract class AProjectCommonInspectItemFragment extends ACommonInspectItemFragment {
    protected int position;
    protected int requestId;
    protected String proType;

    public AProjectCommonInspectItemFragment() {
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        if (recordId == null) {
            recordId = "";
        }
        this.recordId = recordId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getProType() {
        return proType;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }
}