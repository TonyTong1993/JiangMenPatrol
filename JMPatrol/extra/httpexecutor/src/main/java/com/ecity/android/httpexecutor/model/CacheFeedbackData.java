package com.ecity.android.httpexecutor.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;

/**
 * @author xiaobei
 * @date 2018/5/23
 */
@Entity
public class CacheFeedbackData {
    private static final long serialVersionUID = -3531614877723677841L;
    //sqlite中的id
    @Id
    private Long recordId;
    private int failCount;
    // 电量
    private String battery;
    // 用户id
    private int userid;
    //上报时间
    private String time;
    //是否已经上报
    private int status = 0;// 1 上报 0 未上报
    //表单是否已经上报
    private int tableStatus = 0;// 1 上报 0 未上报
    //附件是否已经上报
    private int fileStatus = 0;// 1 上报 0 未上报
    //未上报成功的操作
    private String tag;
    //未上报成功的url
    private String url;
    //未上报成功的参数
    private String parameter;
    //关联附件的resourceId
    private String resourceId;
    //未上报成功的附件url
    private String fileUrl;
    //未上报成功的附件参数
    private String fileParameter;
    //未上报成功的附件路径
    private String filePath;
    //存放文件的数据库表名
    private String tableName;
    //上报的类型 0：工单 1：应急 2：事件或者任务
    private int reportType;
    private int tableType;
    private String column;

    @Keep
    public CacheFeedbackData(Long id, int failCount, String battery, int userid, String time, int status, int tableStatus, int fileStatus, String tag, String url, String parameter, String resourceId, String fileUrl, String fileParameter, String filePath, String tableName, int reportType, int tableType, String column) {
        this.recordId = id;
        this.failCount = failCount;
        this.battery = battery;
        this.userid = userid;
        this.time = time;
        this.status = status;
        this.tableStatus = tableStatus;
        this.fileStatus = fileStatus;
        this.tag = tag;
        this.url = url;
        this.parameter = parameter;
        this.resourceId = resourceId;
        this.fileUrl = fileUrl;
        this.fileParameter = fileParameter;
        this.filePath = filePath;
        this.tableName = tableName;
        this.reportType = reportType;
        this.tableType = tableType;
        this.column = column;
    }

    @Generated(hash = 1228804185)
    public CacheFeedbackData() {
    }

    public Long getId() {
        return recordId;
    }

    public void setId(Long id) {
        this.recordId = id;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(int tableStatus) {
        this.tableStatus = tableStatus;
    }

    public int getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(int fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileParameter() {
        return fileParameter;
    }

    public void setFileParameter(String fileParameter) {
        this.fileParameter = fileParameter;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getRecordId() {
        return this.recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getReportType() {
        return reportType;
    }

    public void setReportType(int reportType) {
        this.reportType = reportType;
    }

    public int getTableType() {
        return tableType;
    }

    public void setTableType(int tableType) {
        this.tableType = tableType;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
}
