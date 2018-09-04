package com.ecity.cswatersupply.service;

import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.emergency.EQModuleConfig;
import com.ecity.cswatersupply.utils.SettingsManager;
import com.z3app.android.util.StringUtil;

/**
 * To Manage the URL of Service
 *
 * @author SunShan'ai
 */
public class ServiceUrlManager {
    // 管网空间查询服务
    private String spacialSearchUrl = "";
    // 巡检库查询服务
    private String patrolFeatureServer = "";
    // 巡检服务
    private String patrolServiceUrl = "";
    // 外勤服务
    private String waiQinServerUrl = "";

    //TODO:服务端支持之后删除此处代码
    //zzz 2017-06-05 巡检任务中关联设备查询的ip和端口，之前存在内外网的问题
    //江门 "http://202.168.161.166:24380/"
    private String patrolDeviceQueryIpPort = "http://222.92.12.42:6080/";

    // 配置的地址查询服务，如果配置文件中包含addressSearchServerUrl 就使用配置文件中的，否则默认使用外勤服务器中的地址
    private String addressSearchServerUrl = "";

    private static ServiceUrlManager instance;

    private ServiceUrlManager() {
    }

    public static ServiceUrlManager getInstance() {
        if (null == instance) {
            synchronized (ServiceUrlManager.class) {
                if (null == instance) {
                    instance = new ServiceUrlManager();
                }
            }
        }

        return instance;
    }

    public static String getNetServerQueryUrlWithTableName(String tableName) {
        return getServiceRootUrl() + "/rest/services/NetServer/ennproject/table/" + tableName + "/query";
    }

    public static String getWorkOrderField() {
        return getBpmServerRootUrl() + "/workorder/getworkorderfield";
    }

    public static String getLoginUrl() {
        return getUserServiceUrl() + "/login";
    }
    public  static  String getUserInfoByToken() {
        return getUserServiceUrl() + "/getUserInfoByToken";
    }

    public static String getUserTreeUrl() {
        return getUserServiceUrl() + "/getUserTree";
    }

    public static String getWatchStateUrl() {
        return getBpmServerUrl() + "/workorder/isWatchs";
    }

    public static String getHasWatchStateUrl() {
        return getBpmServerUrl() + "/workorder/hasWatchers";
    }

    public static String getChangePasswordUrl() {
        return getUserServiceUrl() + "/changePassword";
    }

    /**
     * 更新人员在线状态url
     * @return
     */
    public String updatePatrolManStateUrl() {
        return patrolServiceUrl + "/updatePatrolManState";
    }

    public String getReportEventFormUrl() {
        return patrolServiceUrl + "/reportForm";
    }

    public String getGroupsTreeUrl() {
        return patrolServiceUrl + "/getGroupsTree";
    }

    public String getNetGroupsTreeUrl() {
        return getBpmServerUrl() + "/workorder/getnetgroup";
    }

    /***
     * 更新人员在线信息
     */
    public static String updateManStateUrl() {
        return getDomain() + "/ServiceEngine/rest/czeq/zqsb/loginOrLogout";
    }

    public String getReportValveFormUrl() {
        return getBpmServerUrl() + "/workorder/submitvalveform";
    }

    public String getWorkOrderPieStaticsUrl() {
        return getBpmServerUrl() + "/workorder/statisticWorkOrder";
    }

    public String getWorkOrderBarStaticsUrl() {
        return getBpmServerUrl() + "/workorder/statisticWorkOrderDetailForUser";
    }

    public String getGroupWorkorderStatistics() {
        return getBpmServerUrl() + "/workorder/getgroupworkorderstatistics";
    }

    public String getReportImageUrl() {
        String reportMethods = "";
        switch (SessionManager.reportType) {
            case 0:
                reportMethods = "/reportFormEventFile";
                break;

            default:
                reportMethods = "/reportFormContentFile";
                break;
        }
        return patrolServiceUrl + reportMethods;
    }

    public String getReportValveImageUrl() {
        return getBpmServerUrl() + "/workorder/uploadvalveformfile";
    }

    //更新数据库中阀门状态
    public String updateValveStatesUrl() {
        return spacialSearchUrl + "/netEdits/updateAtt";
    }

    /**
     * @return 事件上报参数列表
     */
    public String getReportEventParamsUrl() {
        return patrolServiceUrl + "/queryFormByKey";
    }

    public String getUploadAttachmentsUrl(String tableName, String objectId) {
        return patrolFeatureServer + "/table/" + tableName + "/" + objectId + "/addAttachment";
    }

    //(异常速报)事件上报，上报表单
    public String getReportFormEventUrl() {
        return patrolServiceUrl + "/reportFormEvent";
    }

    //获取事件类型
    public String getEventListUrl() {
        return patrolServiceUrl + "/getEventList";
    }

    //获取事件表单
    public String getEventFormUrl() {
        return patrolServiceUrl + "/getEventForm";
    }

    //获取阀门开关表单
    public String getValveSwitchFormUrl() {
        return getBpmServerUrl() + "/workorder/getsubmitform";
    }

    //获取泵房养护详情
    public String getPumpRepairMaintainInfo() {
        return patrolServiceUrl + "/getPumpRepairMaintainInfo";
    }

    //获取泵房养护上报项
    public String getFormContent() {
        return patrolServiceUrl + "/getFormContent";
    }

    /**
     * 获取事件列表
     *
     * @return
     */
    public String getEventFormListUrl() {
        return patrolServiceUrl + "/getEventFormList";
    }

    public String submitGisErrorUrl(String layerId) {
        return patrolFeatureServer + "/" + layerId + "/applyEdits";
    }

    //关闭事件
    public String getCloseEventUrl() {
        return patrolServiceUrl + "/closeevent";
    }

    //获取罚单详情
    public String getEventInfoUrl() {
        return patrolServiceUrl + "/getEventInfo";
    }

    //预算单打印修改罚单状态
    public String getPrintPunishUrl() {
        return patrolServiceUrl + "/printPunish";
    }

    //填写罚单金额之后罚单状态改变
    public String getPunishTicketUrl() {
        return patrolServiceUrl + "/punshTicket";
    }

    public String getReportPositionUrl() {
        return patrolServiceUrl + "/reportPosition";
    }

    // 计划任务列表
    public String getPlanningTaskUrl() {
        return patrolServiceUrl + "/getPlans";
    }

    public String getInspectItemUrl() {
        return patrolServiceUrl + "/getContents";
    }

    //到位反馈
    public String getInPlaceFeedBackUrl() {
        return patrolServiceUrl + "/reportFormContent";
    }

    // 到位上报
    public String getInPlaceReportUrl() {
        return patrolServiceUrl + "/reportPointState";
    }

    public String getReportInspectItemsUrl() {
        return patrolServiceUrl + "/reportContent";
    }

    public String getWorkOrderMetasUrl() {
        return patrolFeatureServer + "/table/WorkOrder";
    }

    public String getSpacialSearchUrl() {
        return spacialSearchUrl;
    }

    public void setSpacialSearchUrl(String spacialSearchUrl) {
        this.spacialSearchUrl = spacialSearchUrl;
    }

    public String getPatrolFeatureServer() {
        return patrolFeatureServer;
    }

    public void setPatrolFeatureServer(String patrolFeatureServer) {
        this.patrolFeatureServer = patrolFeatureServer;
    }

    public String getPatrolServiceUrl() {
        return patrolServiceUrl;
    }

    public void setPatrolServiceUrl(String patrolServiceUrl) {
        this.patrolServiceUrl = patrolServiceUrl;
    }

    public String getWaiQinServerUrl() {
        return waiQinServerUrl;
    }

    public void setWaiQinServerUrl(String waiQinServerUrl) {
        this.waiQinServerUrl = waiQinServerUrl;
    }

    public String getPatrolDeviceQueryIpPort() {
        if(StringUtil.isBlank(patrolDeviceQueryIpPort)) {
            patrolDeviceQueryIpPort = getDomain();
        }

        if(!patrolDeviceQueryIpPort.endsWith("/")){
            patrolDeviceQueryIpPort += "/";
        }

        return patrolDeviceQueryIpPort;
    }

    public void setPatrolDeviceQueryIpPort(String patrolDeviceQueryIpPort) {
        this.patrolDeviceQueryIpPort = patrolDeviceQueryIpPort;
    }



    public static String getAPPUrl() {
        return getDomain() + "/map/mobileConfig/";
    }

    public static String getCZUrl() {
        return getAPPUrl() + "serviceurl.json";
    }

    public static String getAppVersionCheckUrl() {
        return getAPPUrl() + "version.json";
    }

    public String getWorkOrderDownloadUrl(String tag) {
        return waiQinServerUrl + "/WorkOrder/Query2Me";
    }

    public String getExtraDetailInfoUrl() {
        return waiQinServerUrl + "/WorkOrder/ExtraDetailInfo";
    }

    //工单详情信息
    public String getDetailBasicInfoUrl() {
        return getBpmServerUrl() + "/workorder/getworkorderinfo";
    }

    /**
     * 工单汇总信息详情（超期、处理中、未分派、延期、已完成）
     *
     * @return 工单汇总信息详情URL
     */
    public String getDetailSummaryUrl() {
        return getBpmServerUrl() + "/workorder/statisticWorkOrderDetail";
    }

    public String getWorkOrderDetailFlowInfoUrl() {
        return getBpmServerUrl() + "/workorder/getworkorderflowinfo";
    }

    public String getWorkOrderDetailFlowInfoDetailUrl() {
        return getBpmServerUrl() + "/workorder/getworkorderflowinfodetail";
    }

    public String getEvent2WorkOrderFormUrl() {
        return getBpmServerUrl() + "/workorder/onekeytoworkorder";
    }

    public String getEvent2TaskFormUrl() {
        return patrolServiceUrl + "/geteventtotaskform";
    }

    public String getSubmitEvent2WorkOrderUrl() {
        return getBpmServerUrl() + "/workorder/create";
    }

    public String getSubmitEvent2TaskUrl() {
        return patrolServiceUrl + "/eventTransToTaskMobile";
    }

    //获取工作流
    public static String getBpmServerUrl() {
        return getServiceRootUrl() + "/rest/services/BpmServer";
    }

    public String getAttachmentUrl(String tableName, String gid) {
        return patrolFeatureServer + "/table/" + tableName + "/" + gid + "/attachments";
    }

    public String getSubmitFormDataUrl() {
        return getBpmServerRootUrl() + "/workflow/form/submitformdata";
    }

    public String getFormDataUrl() {
        return getBpmServerRootUrl() + "/workflow/form/formdata";
    }

    public String submitWorkOrderInspectItemsUrl() {
        return getBpmServerRootUrl() + "/workflow/form/submitformdata";
    }

    public String getWorkOrderSubFormDataUrl() {
        return getBpmServerRootUrl() + "/workflow/runtime/startFormData";
    }

    public String getStartSubWorkFlowUrl() {
        return getBpmServerRootUrl() + "/workflow/runtime/startProcessInstance";
    }

    public String getWorkOrderReportUrl() {
        return waiQinServerUrl + "/WorkOrder/report";
    }

    public String getAllWorkOrdersUrl() {
        return getBpmServerRootUrl() + "/workorder/getuserworkorder";
    }

    public String getWorkOrdersFilterUrl() {
        return getBpmServerRootUrl() + "/workorder/getcategoryworkorderlist";
    }

    public String getWorkOrderCategoryUrl() {
        return getBpmServerRootUrl() + "/workorder/getcategory";
    }

    public String getWorkOrderCategoryAmountUrl() {
        return getBpmServerRootUrl() + "/workorder/getcategoryamount";
    }

    public String getGroupWorkOrderInfoUrl() {
        return getBpmServerRootUrl() + "/workorder/getgroupworkorderinfo";
    }

    public String getWorkOrderOperationLogUrl() {
        return getBpmServerRootUrl() + "/workorder/workorderlog";
    }

    public String getContactManUrl() {
        return getBpmServerRootUrl() + "/workorder/getgroupuser";
    }

    public String getEvent2TaskContactManUrl() {
        return patrolServiceUrl + "/getcontactman";
    }

    public String getMaterialInfoUrl() {
        return getBpmServerRootUrl() + "/workorder/getmeterialpipe";
    }

    public String getDispatchWorkOrderUrl() {
        return getBpmServerRootUrl() + "/workorder/paidan";
    }

    public String getAcceptWorkOrderUrl() {
        return getBpmServerRootUrl() + "/workorder/jiedan";
    }

    public String getWorkOrderReportAttachmentsUrl() {
        return getBpmServerRootUrl() + "/workorder/upfile";
    }

    public String getImageUrl() {
        return getBpmServerRootUrl() + "/workorder/getimg?filename=";
    }

    public String getFZImageUrl() {
        return getPatrolServiceUrl() + "/getfile?filename=";
    }

    //本地查询地址入口
    public String getPlanAndPipeAddress() {

        if(StringUtil.isBlank(addressSearchServerUrl)) {
            return getServiceRootUrl() + "/rest/services/SearchServer/addr";
        }

        return addressSearchServerUrl;
    }

    public void setPlanAndPipeAddress(String addressSearchServerUrl) {
        this.addressSearchServerUrl = addressSearchServerUrl;
    }
    /**
     * 取消延期、取消协助、取消转办和取消退单的操作的url
     *
     * @author jonathanma
     */
    public String getCancelWorkOrderSubProcessApplicationUrl() {
        return getBpmServerRootUrl() + "/workflow/task/complete";
    }

    public String getQueryMessageUrl() {
        return getBpmServerRootUrl() + "/workorder/message/list";
    }

    public String getReadMessageUrl() {
        return getBpmServerRootUrl() + "/workorder/message/read";
    }

    public String getDeleteMessageUrl() {
        return getBpmServerRootUrl() + "/workorder/message/delete";
    }

    //值班签到
    public static String getSignInUrl() {
        return getBpmServerRootUrl() + "/workorder/startwatch";
    }

    //值班签退
    public static String getSignOutUrl() {
        return getBpmServerRootUrl() + "/workorder/endwatch";
    }

    //获取所有外勤人员
    public String getAllPatroMenUrl() {
        return patrolServiceUrl + "/getAllPatrolMan";
    }

    //获取所有外勤车辆
    public String getAllPatroBusUrl() {
        return patrolServiceUrl + "/queryPatrolBus";
    }

    private static String getBpmServerRootUrl() {
        return getServiceRootUrl() + "/rest/services/BpmServer";
    }

    public static String getEarthQuickBaseUrl() {
        return getServiceRootUrl() + "/rest/czeq";
    }

    /***
     * 获取地震信息的服务地址
     * @return
     */
    public static String getEarthQuickInfoUrl() {
        try {
            if (EQModuleConfig.getConfig().isCZModuleUseable()) {
                return getDomain() + "/ServiceEngine/rest/services/czdz3/GetEarthQuakeEventInfoBySearchValue";
            } else {
                return getDomain() + "/ServiceEngine/rest/czeq/GetEarthQuakeEventInfoBySearchValue";
            }
        } catch (Exception e) {
            return "";
        }
    }
//    public static String getEarthQuickInfoUrl() {
//        return getDomain() + "/ServiceEngine/rest/services/czdz3/GetEarthQuakeEventInfoBySearchValue";
//    }

    /***
     * 获取台站信息的服务地址
     * @return
     */
    public static String getEQStationStationUrl() {
        try {
            if (EQModuleConfig.getConfig().isCZModuleUseable()) {
                return getDomain() + "/ServiceEngine/rest/services/czdz3/GetStationBaseInfo";
            } else {
                return getDomain() + "/ServiceEngine/rest/czeq/GetStationBaseInfo";
            }
        } catch (Exception e) {
            return "";
        }
    }
//    public static String getEQStationStationUrl() {
//        return getDomain() + "/ServiceEngine/rest/services/czdz3/GetStationBaseInfo";
//    }

    /***
     * 获取联系人
     */
    public static String getEmerContactUrl() {
        return getDomain() + "/ServiceEngine/rest/czeq2/getEmerContact";
    }


    /***
     * 获取所有避难所（常州地震）
     */
    public static String getEQRefugeInfoUrl() {
        return getDomain().replace("8888", "8099") + "/ServiceEngine/rest/services/bncs/MapServer/0/query";
    }

    /***
     * 获取查询条件表单的服务地址
     * @return
     */
    public static String getSearchParamUrl() {
        return getDomain() + "/ServiceEngine/rest/info";
    }

    /***
     * 获取速报总览数据的服务地址
     * @return
     */
    public static String getQBODatasUrl() {
        return getDomain() + "/ServiceEngine/rest/czeq/zqsb/getZqsbTotal";
    }

    /***
     * 获取消息公告中 计划任务 的地址
     * @return
     */
    public static String getEQMessageList() {
        return getDomain() + "/ServiceEngine/rest/czeq/zqsb/getMessageList";
    }

    /***
     * 武汉地震获取重要地震信息的服务地址
     * @return
     */
    public static String getImportEarthQuickUrl() {
        return getDomain() + "/ServiceEngine/rest/czeq2/getEQ";
    }

    /***
     * 获取灾情速报列表服务地址
     * @return
     */
    //原灾情速报地址
//    public static String getEQQuickReportInfoUrl() {
//        return getDomain() + "/ServiceEngine/rest/czeq/zqsb/getZqsbList";
//    }
    public static String getEQQuickReportInfoUrl() {
        return getDomain() + "/ServiceEngine/rest/czeq/zqsb/getExceptionList";
    }

    /***
     * 获取现场调查列表服务地址
     * @return
     */
    public static String getInvestigationInfoUrl() {
        return getDomain() + "/ServiceEngine/rest/czeq/zqsb/getZqsbDetailList";
    }

    /**
     * 新建速报时 获取灾情速报/现场调查的表单
     *
     * @return
     */
    public static String getEQQuickReportInspectInfoUrl() {
        return getDomain() + "/ServiceEngine/rest/czeq/zqsb/getFormData";
    }

    /**
     * 更新速报内容时   获取灾情速报表单
     *
     * @return
     */
    public static String getEQUpdateInspectInfoUrl() {
        return getDomain() + "/ServiceEngine/rest/czeq/zqsb/getZqsbById";
    }

    /**
     * 更新速报内容时   获取现场调查表单
     *
     * @return
     */
    public static String getEQXCDCUpdateInspectInfoUrl() {
        return getDomain() + "/ServiceEngine/rest/czeq/zqsb/getZqsbDetailById";
    }

    /**
     * 灾情速报上报
     *
     * @return
     */
    public static String reportEQZqsb() {
        return getDomain() + "/ServiceEngine/rest/czeq/zqsb/reportZqsb";
    }

    /***
     * 处理情况上报关联地震操作
     * @return
     */
    public static String reportExceptionEq() {
        return getDomain() + "/ServiceEngine/rest/czeq/zqsb/reportExceptionEq";
    }


    /**
     * 灾情速报更新
     *
     * @return
     */
    public static String updateEQZqsb() {
        return getDomain() + "/ServiceEngine/rest/czeq/zqsb/updateZqsb";
    }

    /**
     * 现场调查上报
     *
     * @return
     */
    public static String reportEQXCDCDetail() {
        return getDomain() + "/ServiceEngine/rest/czeq/zqsb/reportZqsbDetail";
    }

    public static String reportExceptionFile() {
        return getDomain() + "/ServiceEngine/rest/czeq/zqsb/reportExceptionFile";
    }

    /**
     * 现场调查更新
     *
     * @return
     */
    public static String updateEQXCDCDetail() {
        return getDomain() + "/ServiceEngine/rest/czeq/zqsb/updateZqsbDetail";
    }

    /***
     * 获取异常速报表单服务地址
     * @return
     */
    public static String getUnUsualReportFormUrl() {
        try {
            if (EQModuleConfig.getConfig().isCZModuleUseable()) {
                return getDomain() + "/ServiceEngine/rest/services/czdz3/getFormData";
            } else {
                return getDomain() + "/ServiceEngine/rest/czeq/zqsb/getFormData";
            }
        } catch (Exception e) {
            return "";
        }
    }
//    public static String getUnUsualReportFormUrl() {
//        return getDomain() + "/ServiceEngine/rest/services/czdz3/getFormData";
//    }

    /***
     * 获取异常速报上报表单服务地址
     * @return
     */
    public String getUnUsualReportFormEventUrl() {
        try {
            if (EQModuleConfig.getConfig().isCZModuleUseable()) {
                return getDomain() + "/ServiceEngine/rest/services/czdz3/reportException";
            } else {
                return getDomain() + "/ServiceEngine/rest/czeq/zqsb/reportException";
            }
        } catch (Exception e) {
            return "";
        }
    }
//    public String getUnUsualReportFormEventUrl() {//常州地震上报
//        return getDomain() + "/ServiceEngine/rest/services/czdz3/reportException";
//    }

    /***
     * 获取异常速报上报附件服务地址
     * @return
     */
    public String getUnUsualReportFileUrl() {
        try {
            if (EQModuleConfig.getConfig().isCZModuleUseable()) {
                return getDomain() + "/ServiceEngine/rest/services/czdz3/reportExceptionFile";
            } else {
                return getDomain() + "/ServiceEngine/rest/czeq/zqsb/reportExceptionFile";
            }
        } catch (Exception e) {
            return "";
        }
    }
//    public String getUnUsualReportFileUrl() {//常州地震上报
//        return getDomain() + "/ServiceEngine/rest/services/czdz3/reportExceptionFile";
//    }


    /**
     * 获取消息公告服务地址
     */
    public static String GetNewsBulletinBoardService() {
        return "http://www.csi.ac.cn/publish/main/256/100024/index.html";
    }

    /**
     * 获取消息通知公告服务地址
     */
    public static String getNoticeList() {
        return getDomain() + "/ServiceEngine/rest/services/czdz3/getNoticeList";
//        return "192.168.8.100:8888" + "/ServiceEngine/rest/services/czdz3/getNoticeList";
    }

    /**
     * 获取消息通知公告服务详情地址
     */
    public static String getNoticeContent() {
        return getDomain() + "/ServiceEngine/rest/services/czdz3/getNoticeContent";
//        return "192.168.8.100:8888" + "/ServiceEngine/rest/services/czdz3/getNoticeContent";
    }

    /**
     * 应急预案服务服务地址
     *
     * @return
     */
    public static String getEmergencyResponseUrl() {
        try {
            if (EQModuleConfig.getConfig().isCZModuleUseable()) {
                return getDomain().replace("8888", "8099") + "/ServiceEngine/rest/czeq/getEmerPlanTree";
            } else {
                return getDomain() + "/ServiceEngine/rest/czeq2/getEmerPlanTree?type=1&doc=";
            }
        } catch (Exception e) {
            return "";
        }

    }
//    public static String getEmergencyResponseUrl() {
//        return getDomain().replace("8888","8099") + "/ServiceEngine/rest/czeq/getEmerPlanTree";
//    }

    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    // 南昌外勤服务地址

    public String getFieldWorkServiceUrl() {
        return getServiceRootUrl() + "/rest/services/fieldworkService";
    }

    //获取工单列表服务地址
    public String queryWorkOrdersUrl() {
        return getFieldWorkServiceUrl() + "/getUserDataList";
    }

    //获取工单详情tab页服务地址
    public String getWorkOrderDetailTabsUrl() {
        return getFieldWorkServiceUrl() + "/getworkorderdetailtab";
    }

    //获取外勤表单服务地址
    public String getTaskFormDataUrl() {
        return getFieldWorkServiceUrl() + "/getTaskFormData";
    }

    //提交外勤表单数据服务地址
    public String submitTaskFormDataUrl() {
        return getFieldWorkServiceUrl() + "/submitTaskFormData";
    }

    //提交外勤附件服务地址
    public String submitTaskFileUrl() {
        return getFieldWorkServiceUrl() + "/upfile";
    }

    //获取外勤审批流程详细信息服务地址
    public String getWorkOrderFlowDetailUrl() {
        return getFieldWorkServiceUrl() + "/getProcessDetailInfo";
    }

    //获取工单搜索服务地址
    public String getWorkOrderSearchUrl() {
        return getServiceRootUrl() + "/rest/services/workorderService/getworkorderlist";
    }

    public String getMaterialInfoUrl1() {
        return getFieldWorkServiceUrl() + "/getmeterialpipe";
    }

    /**
     * Base service urls.
     */
    private static String getUserServiceUrl() {
        return getServiceRootUrl() + "/api/v1.1/omsService/user";
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
        if (StringUtil.isEmpty(virtualPath)) {
            return getDomain();
        }else  {
            return getDomain() + "/" + virtualPath;
        }

    }

    private static String getDomain() {
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
        } else  {
            protocol = SettingsManager.getInstance().getProtocolA();
            ip = SettingsManager.getInstance().getServerIPA();
            port = SettingsManager.getInstance().getServerPortA();
        }

        return protocol + "://" + ip + ":" + port;
    }

    public String getTestUrl() {
        return "";
    }

    private String getPatrolUrl() {
        String patrolUrl = "";
        String[] ss = spacialSearchUrl.split("/");
        patrolUrl = ss[0];
        for (int i = 1; i < ss.length - 1; i++) {
            patrolUrl = patrolUrl + "/" + ss[i];
        }

        return patrolUrl + "/patrol";
    }
}
