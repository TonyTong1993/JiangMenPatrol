package com.ecity.cswatersupply.project.activity.fragment;


public abstract class ACommonListInspectFragment extends ACommonListFragment {

    protected String projectId;
    protected String recordId;
    protected int position;
    protected int requestId;

    public ACommonListInspectFragment() {
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
}