package com.ecity.cswatersupply.model;

public class WorkOrderOperationLogBean implements Comparable<WorkOrderOperationLogBean> {
    private String nodeid;
    private String username;
    private String processtime;
    private String description;
    private String dealusername;

    public WorkOrderOperationLogBean(String nodeid, String username, String processtime, String description, String dealusername) {
        super();
        this.nodeid = nodeid;
        this.username = username;
        this.processtime = processtime;
        this.description = description;
        this.dealusername = dealusername;
    }

    public String getNodeid() {
        return nodeid;
    }

    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProcesstime() {
        return processtime;
    }

    public void setProcesstime(String processtime) {
        this.processtime = processtime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDealusername() {
        return dealusername;
    }

    public void setDealusername(String dealusername) {
        this.dealusername = dealusername;
    }

    @Override
    public int compareTo(WorkOrderOperationLogBean another) {
        int result = processtime.compareTo(another.processtime);

        if (result < 0)
            return -1;
        if (result > 0)
            return 1;

        return 0;
    }
}
