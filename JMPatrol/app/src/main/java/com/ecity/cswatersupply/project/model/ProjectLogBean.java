package com.ecity.cswatersupply.project.model;

public class ProjectLogBean implements Comparable<ProjectLogBean> {
    private String nodeName;
    private String username;
    private String processtime;
    private String remark;

    public ProjectLogBean(String nodeName, String username, String processtime, String remark) {
        super();
        this.nodeName = nodeName;
        this.username = username;
        this.processtime = processtime;
        this.remark = remark;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public int compareTo(ProjectLogBean another) {
        int result = processtime.compareTo(another.processtime);

        if (result < 0)
            return -1;
        if (result > 0)
            return 1;

        return 0;
    }
}
