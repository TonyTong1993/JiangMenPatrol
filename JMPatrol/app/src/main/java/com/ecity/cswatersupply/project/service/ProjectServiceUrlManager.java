package com.ecity.cswatersupply.project.service;

import com.ecity.cswatersupply.utils.SettingsManager;

public class ProjectServiceUrlManager {

    private static ProjectServiceUrlManager instance;

    private ProjectServiceUrlManager() {
    }

    public static ProjectServiceUrlManager getInstance() {
        if (null == instance) {
            synchronized (ProjectServiceUrlManager.class) {
                if (null == instance) {
                    instance = new ProjectServiceUrlManager();
                }
            }
        }

        return instance;
    }

    /**
     * 获取消息的url
     *
     * @return
     */
    public String getUserMsg() {
        return getProjectServerNonFlowUrl() + "/getUserMsg";
    }

    /**
     * 删除消息的url
     *
     * @return
     */
    public String deleteUserMsg() {
        return getProjectServerNonFlowUrl() + "/deleteUserMsg";
    }

    /**
     * 获取项目列表信息的url
     *
     * @return
     */
    public String getProjectsUrl() {
        return getProjectServerRootUrl() + "/getProListFormat";
    }

    /**
     * 获取项目总览信息的url
     *
     * @return
     */
    public String queryPro4Analysis() {
        return getProjectServerRootUrl() + "/queryPro4Analysis";
    }

    public String getSummaryProCount4AnalysisUrl() {
        return getProjectServerRootUrl() + "/querySummaryProCount4Analysis";
    }

    /**
     * 获取项目附件信息
     *
     * @return
     */
    public String getAttachment() {
        return getProjectJmpServerRootUrl() + "/getAttachment";
    }

    /**
     * 获取工程一张图的项目信息
     *
     * @return
     */
    public String getProjectForMap() {
        return getProjectJmpServerRootUrl() + "/getProjectForMap";
    }

    /**
     * 获取江门工程，负责人汇总统计的url
     *
     * @return
     */
    public String getFzrStastictis() {
        return getProjectJmpServerRootUrl() + "/getFzrStatistics";
    }

    /**
     * 获取勘察设计技术部查看工程部委托信息
     *
     * @return
     */
    public String getDesignLook() {
        return getProjectJmpServerRootUrl() + "/designLook";
    }

    /**
     * 获取勘察设计项目列表的url
     *
     * @return
     */
    public String getKcsjProList() {
        return getProjectJmpServerRootUrl() + "/getKcsjProList";
    }

    /**
     * 勘察设计审核的url
     *
     * @return
     */
    public String checkKcsj() {
        return getProjectServerRootUrl() + "/checkKcsj";
    }

    /**
     * 获取勘察设计延期时间轴的url
     *
     * @return
     */
    public String getKcsjCheckLog() {
        return getProjectServerRootUrl() + "/getKcsjCheckLog";
    }

    /**
     * 设计部退回工程部勘察设计的委托
     *
     * @return
     */
    public String backDesignDelegate() {
        return getProjectServerRootUrl() + "/backDesignDelegate";
    }

    /**
     * 获取开工项目列表的url
     *
     * @return
     */
    public String getKgProList() {
        return getProjectJmpServerRootUrl() + "/getKgProList";
    }

    /**
     * 获取开工详情的url
     *
     * @return
     */
    public String getProKgInfo() {
        return getProjectServerRootUrl() + "/getProKgInfo";
    }

    /**
     * 获取开工审核提交
     *
     * @return
     */
    public String checkProKg() {
        return getProjectServerRootUrl() + "/checkProKg";
    }

    /**
     * 获取资金支付项目列表的url
     *
     * @return
     */
    public String getPayProList() {
        return getProjectServerNonFlowUrl() + "/getPayProList";
    }

    /**
     * 获取资金支付详情
     *
     * @return
     */
    public String getPayInfo() {
        return getProjectServerRootUrl() + "/getPayInfo";
    }

    /**
     * 获取资金支付历史记录
     *
     * @return
     */
    public String getPayHistory() {
        return getProjectServerRootUrl() + "/getPayHistory";
    }

    /**
     * 获取资金支付审核
     *
     * @return
     */
    public String checkProPay() {
        return getProjectServerRootUrl() + "/checkProPay";
    }

    /**
     * 获取试压试验列表
     *
     * @return
     */
    public String getSyProList() {
        return getProjectJmpServerRootUrl() + "/getSyProList";
    }

    /**
     * 获取试压试验详情
     *
     * @return
     */
    public String getSyCheckDetail() {
        return getProjectServerRootUrl() + "/getSyCheckDetail";
    }

    /**
     * 获取试压试验审核
     *
     * @return
     */
    public String checkSy() {
        return getProjectServerRootUrl() + "/checkSy";
    }

    /**
     * 获取接水列表
     *
     * @return
     */
    public String getJsProList() {
        return getProjectJmpServerRootUrl() + "/getJsProList";
    }

    /**
     * 获取接水历史记录
     *
     * @return
     */
    public String getJsHsList() {
        return getProjectServerRootUrl() + "/getJsHsList";
    }

    /**
     * 获取试压历史记录
     *
     * @return
     */
    public String getSyHistory() {
        return getProjectServerRootUrl() + "/getSyHistory";
    }

    /**
     * 获取接水详情
     *
     * @return
     */
    public String getJsCheckInfo() {
        return getProjectServerRootUrl() + "/getJsCheckInfo";
    }

    /**
     * 获取接水审核
     *
     * @return
     */
    public String checkWater() {
        return getProjectServerRootUrl() + "/checkWater";
    }

    /**
     * 获取竣工验收项目列表的url
     *
     * @return
     */
    public String getJGProList() {
        return getProjectJmpServerRootUrl() + "/getJGProList";
    }

    /**
     * 获取竣工验收详情
     *
     * @return
     */
    public String getProJgysInfo() {
        return getProjectServerRootUrl() + "/getProJgysInfo";
    }

    /**
     * 获取竣工验收审核提交
     *
     * @return
     */
    public String checkProJgys() {
        return getProjectServerRootUrl() + "/checkProJgys";
    }

    /**
     * 获取项目总览信息各类数量的url
     *
     * @return
     */

    public String queryProCount4Analysis() {
        return getProjectServerRootUrl() + "/queryProCount4Analysis";
    }

    public String getProBaseInfo() {
        return getProjectServerRootUrl() + "/getProBaseInfo";
    }

    public String getProBaseWorkload() {
        return getProjectServerRootUrl() + "/getProBaseWorkload";
    }

    public String getProBaseProgress() {
        return getProjectServerRootUrl() + "/getProBaseProgress";
    }

    public String getProBasePay() {
        return getProjectServerRootUrl() + "/getProBasePay";
    }

    public String getProBaseChage() {
        return getProjectServerRootUrl() + "/getProBaseChage";
    }

    private static String getProjectJmpServerRootUrl() {
        return getServiceRootUrl() + "/rest/services/JmpServer";
    }

    /***
     * 江门工程普通内容返回日志信息查询的url
     * @param logType
     * @return
     */
    public String getProjectsLogBackUrl(String logType) {
        return getProjectJmpServerRootUrl() + "/getProModLog/" + logType;
    }

    /***
     * 江门工程安全管理内容返回日志信息查询的url
     * @param
     * @return
     */
    public String getSafeEventLogBackUrl() {
        return getProjectJmSafeServerUrl() + "/queryEventLog";
    }

    /**
     * 获取勘查设计详情的url
     *
     * @param functionIndex 0：项目基本信息，1：委托信息，2：延期信息，3：提交信息
     * @return
     */
    public String getKanChaSheJiDetailUrl(int functionIndex) {
        return getProjectServerRootUrl() + "/getKcsjPages/" + String.valueOf(functionIndex);
    }

    /**
     * 获取工作量项目列表的url
     *
     * @return
     */
    public String getWorkloadProjectsUrl() {
        return getProjectServerRootUrl() + "/queryWorkloadProjects";
    }

    /**
     * 获取工作量申请详情的url
     *
     * @return
     */
    public String getWorkloadDetailUrl() {
        return getProjectServerRootUrl() + "/queryWorkloadDetail";
    }

    /**
     * 审核工作量的url
     *
     * @return
     */
    public String getCheckWorkloadUrl() {
        return getProjectServerRootUrl() + "/checkWorkload";
    }

    /**
     * 获取工作量申请记录的url
     *
     * @return
     */
    public String getWorkloadRecordsUrl() {
        return getProjectServerRootUrl() + "/queryWorkloadRecords";
    }

    /**
     * 获取水表报装统计信息的url
     *
     * @return
     */
    public String statProStatusCountByTime() {
        return getProjectStaticUrl() + "/statProStatusCountByTime";
    }

    /**
     * 获取水表报装列表信息的url
     *
     * @return
     */
    public String getWaterMeterProList() {
        return getProjectJmbzServerUrl() + "/getProList";
    }

    /**
     * 获取水表报装统计信息按年月日查询
     *
     * @return
     */
    public String statProByTimeAndStatus() {
        return getProjectStaticUrl() + "/statProByTimeAndStatus";
    }

    /**
     * 获取安全管理项目信息的url
     *
     * @return
     */
    public String querySafeProList() {
        return getProjectJmSafeServerUrl() + "/querySafeProList";
    }

    /**
     * 获取安全管理项目中整改列表信息的url
     *
     * @return
     */
    public String querySafeList() {
        return getProjectJmSafeServerUrl() + "/querySafeList";
    }

    /**
     * 获取安全管理创建页面的url
     *
     * @return
     */
    public String getCreatePage() {
        return getProjectJmSafeServerUrl() + "/mobile/getCreatePage";
    }

    /**
     * 获取安全管理问题详情的Tab页
     *
     * @return
     */
    public String getEventStep() {
        return getProjectJmSafeServerUrl() + "/getEventStep";
    }

    /**
     * 获取安全管理问题详情的Tab页
     *
     * @return
     */
    public String getEventInfo(String step) {
        return getProjectJmSafeServerUrl() + "/mobile/getEventInfo" + step;
    }

    /**
     * 获取安全管理项提交创建信息的url(表单信息)
     *
     * @return
     */
    public String saveEventInfo() {
        return getProjectJmSafeServerUrl() + "/saveEventInfo";
    }
    /**
     * 获取安全管理项提交审核或整改信息的url(第二页)
     *
     * @return
     */
    public String saveCheckOrZgInfo() {
        return getProjectJmSafeServerUrl() + "/saveCheckOrZgInfo";
    }
    /**
     * 获取安全管理项保存反馈汇总信息的url(第三页)
     *
     * @return
     */
    public String saveZgFeedbak() {
        return getProjectJmSafeServerUrl() + "/saveZgFeedbak";
    }

    /**
     * 获取上传附件通用接口（江门市政工程）
     *
     * @return
     */
    public String uploadFile() {
        return getProjectJmpServerRootUrl() + "/mobile/uploadFile";
    }

    public String getGroupsTreeUrl() {
        return getProjectJmpServerRootUrl() + "/queryGroupsAndUsers";
    }

    private static String getProjectServerRootUrl() {
        return getServiceRootUrl() + "/rest/services/JmpMobileServer";
    }

    private static String getProjectServerNonFlowUrl() {
        return getServiceRootUrl() + "/rest/services/JmpNonFlowServer";
    }

    private static String getProjectStaticUrl() {
        return getServiceRootUrl() + "/rest/services/JmbzStatisticServer";
    }

    private static String getProjectJmbzServerUrl() {
        return getServiceRootUrl() + "/rest/services/JmbzServer";
    }

    private static String getProjectJmSafeServerUrl() {
        return getServiceRootUrl() + "/rest/services/JmSafeServer";
    }

    private static String getServiceRootUrl() {
        int serverType = SettingsManager.getInstance().getServerType();
        String virtualPath = "";
        if (0 == serverType) {
            virtualPath = SettingsManager.getInstance().getServerVirtualPathA();
        } else if (1 == serverType) {
            virtualPath = SettingsManager.getInstance().getServerVirtualPathB();
        } else {
            // no logic to do.
        }

        return getDomain() + "/" + virtualPath;
    }

    public static String getDomain() {
        int serverType = SettingsManager.getInstance().getServerType();
        String protocol = "";
        String ip = "";
        String port = "";
        if (0 == serverType) {
            protocol = SettingsManager.getInstance().getProtocolA();
            ip = SettingsManager.getInstance().getServerIPA();
            port = SettingsManager.getInstance().getServerPortA();
        } else if (1 == serverType) {
            protocol = SettingsManager.getInstance().getProtocolB();
            ip = SettingsManager.getInstance().getServerIPB();
            port = SettingsManager.getInstance().getServerPortB();
        } else {
            // no logic to do.
        }

        return protocol + "://" + ip + ":" + port;
    }
}
