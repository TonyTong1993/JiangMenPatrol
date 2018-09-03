package com.enn.sop.global;


import com.enn.sop.model.BuildConfig;

/**
 * To Manage the URL of Service
 *
 * @author clown
 * @date 2017/10/27
 */

public class ServiceUrlManager {
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

    public String getServiceRootUrl() {
        /**
         * 用户包
         */
//        return BuildConfig.SERVICE_URL_ROOT + BuildConfig.VIRTUAL_PATH;


        /* 推广环境默认地址 */
//        return "http://promotion-service-zhyytest.oennso.enn.cn:80/api/v1/sop";

         /* 推广开发环境默认地址 */
        //return "http://promotion-service-dev-zhyytest.oennso.enn.cn:80/api/v1/sop";


//        return "http://service-test-zhyytest.ipaas.enncloud.cn:56512/api/v1/sop";

        /**
         * 可选择ip的测试包
         */
        String virtualPath = SettingsManager.getInstance().getServerVirtualPathA();
        return getDomain() + "/" + virtualPath;

    }

    public String getRootServiceUrl() {
        return getServiceRootUrl();
    }

    /**
     * 获取首页任务执行情况(数据)URL
     *
     * @return
     */
    public String getTaskExecutionDataUrl() {
        return getServiceRootUrl() + "/home/getAppHomeReport";
    }

    /**
     * 获取日志上报URL
     */
    public String getSavaLogUrl() {
        return getServiceRootUrl() + "/log/saveLog";
    }

    /**
     * 获取日志列表URL
     */
    public String getLogListUrl() {
        return getServiceRootUrl() + "/log/getLogList";
    }

    /**
     * 获取历史日志列表URL
     */
    public String getHistoryLogList() {
        return getServiceRootUrl() + "/log/getHistoryLogList";
    }

    /**
     * 获取未完成任务数量URL
     *
     * @return
     */
    public String getTaskUnDealCountUrl() {
        return getServiceRootUrl() + "/task/task/unfinish/count";
    }

    /**
     * 获取未完成任务数量URL
     *
     * @return
     */
    public String getPromotionTaskUnDealCountUrl() {
        return getServiceRootUrl() + "/task/unfinish/count";
    }

    /**
     * 获取地图管网查询URL
     *
     * @return
     */
    public String getIdentifyQueryUrl() {
        return getServiceRootUrl() + "/gis/" + GlobalFunctionInfo.getCurrentUser().getEcode() + "/pipenet/MapServer/identify";
    }

    public String getMapConfigInfo() {
        return "http://10.39.13.38:8022/ServiceEngine/rest/services/MapCatchDownloadServer/MapCatchDownload";
    }

    /*
    *  根据设备编号获取地图数据
    * **/
    public String getMapMetaByCodeUrl() {
        return getServiceRootUrl() + "/gis/gisbutt/getDataByEqCode";
    }

    /*
    *  gis错误上报提交
    * **/
    public String submitGisReportUrl() {
        return getServiceRootUrl() + "/gis/gisbutt/getGisReport";
    }

    /**
     * 获取地图管网元数据URL
     *
     * @return
     */
    public String getMapServerMetasUrl() {
        return getServiceRootUrl() + "/gis/" + GlobalFunctionInfo.getCurrentUser().getEcode() + "/pipenet/MapServer/metas";
    }

    /**
     * 获取配置信息
     *
     * @return
     */
    public String getValueByModule() {
        return getServiceRootUrl() + "/config/getValueByModule";
    }

    /**
     * 登录
     */
    public String getLoginUrl() {
        return getServiceRootUrl() + "/user/login";
    }

    /**
     * 查询用户菜单等基本信息，用于不需要登录的场景
     */
    public String getUserInfo() {
        return getServiceRootUrl() + "/user/getUserInfo";
    }

    /**
     * 查询
     */
    public String getPatrolLayerNameUrl() {
        return getServiceRootUrl() + "/patrol/layer/getPatrolLayerList";
    }

    /**
     * 人员上下线状态更新
     */
    public String getUpdatePatrolManStateUrl() {
        return getServiceRootUrl() + "/position/setUserState";
    }

    /**
     * 获取消息列表URL
     */
    public String getMessageListUrl() {
        return getServiceRootUrl() + "/fieldwork/message/list";
    }

    /**
     * 删除消息列表URL
     */
    public String getDeleteMessageUrl() {
        return getServiceRootUrl() + "/fieldwork/message/delete";
    }

    /**
     * 获取将消息标记为已读URL
     */
    public String getMessageReadUrl() {
        return getServiceRootUrl() + "/fieldwork/message/read";
    }


    /**
     * 附件上传
     */
    public String submitTaskFileUrl() {
        return getServiceRootUrl() + "/attach/batch/uploadWithEcho";
    }

    /**
     * 附件下载
     */
    public String getFileUrl() {
        //return testServiceUrl + "/attach/downloadFile?id=";
        return getServiceRootUrl() + "/attach/downloadFile?sys=android&plat=mobile&token=" + GlobalFunctionInfo.getToken() + "&id=";
    }

    /**
     * 附件下载
     */
    public String getFileInfoById() {
        return getServiceRootUrl() + "/attach/selectFileInfoListByIds";
    }

    /*********************** 事件 begin**************************************/

    /**
     * 获取事件详情URL
     */
    public String getEventDetailUrl() {
        return getServiceRootUrl() + "/event/getEventInfo";
    }

    /**
     * 获取事件列表URL
     */
    public String getEventListDataUrl() {
        return getServiceRootUrl() + "/event/getEventFormList";
    }

    /**
     * 获取事件类型列表URL
     */
    public String getEventTypeListUrl() {
        return getServiceRootUrl() + "/event/getEventList";
    }

    /**
     * 获取待办箱事件列表URL
     */
    public String getEventAllTypeListUrl() {
//        return getServiceRootUrl() + "/event/getEventAllTypeList";
        return getServiceRootUrl() + "/event/getHistoryReportList";
    }


    /**
     * 获取历史上报事件列表URL
     */
    public String getHistoryReportListUrl() {
        return getServiceRootUrl() + "/event/getHistoryReportList";
    }

    /**
     * 第三方施工上报停滞时长
     */
    public String addStayTime() {
        return getServiceRootUrl() + "/fieldwork/woController/addStayTime";
    }

    /**
     * 第三方施工上报评价信息
     */
    public String addLeaderAppraise() {
        return getServiceRootUrl() + "/fieldwork/woController/addLeaderAppraise";
    }

    /**
     * 关闭事件URL
     */
    public String getCloseEventURL() {
        return getServiceRootUrl() + "/event/endEvent";
    }

    /**
     * 提交事件表单URL
     */
    public String getStartProcessEventURL() {
        return getServiceRootUrl() + "/event/startProcessForMobile";
    }

    /**
     * 获取事件处理表单URL
     */
    public String getStartFormDataURL() {


        return getServiceRootUrl() + "/fieldwork/process/getStartFormData";
    }

    /**
     * 事件上报
     */
    public String getEventReportSubmmitUrl() {
        return getServiceRootUrl() + "/event/reportFormEvent";
    }

    /**
     * 隐患整改反馈表单上报
     */
    public String submitRectifyData() {
        return getServiceRootUrl() + "/event/submitRectifyData";
    }

    /**
     * 获取事件表单
     */
    public String getEventReportFormUrl() {
        return getServiceRootUrl() + "/event/getEventForm";
    }

    /**
     * 获取隐患管理整改反馈表单
     */
    public String getRectifyFormData() {
        return getServiceRootUrl() + "/event/getRectifyFormData";
    }

    /**
     * 事件上报（应急）
     */
    public String getEmergencySubmmitUrl() {
        return getServiceRootUrl() + "/emer/event/appReportEvent";
    }

    /**
     * 应急地图中反馈所有菜单上报（应急）
     */
    public String emerFeedback() {
        return getServiceRootUrl() + "/emer/emerFeedback";
    }

    /**
     * 事件列表-险情确认上报（应急）
     */
    public String getEmerConfirmSubmmitUrl() {
        return getServiceRootUrl() + "/emer/event/emerConfirm";
    }

    /**
     * 获取事件表单（应急上报）
     */
    public String getEmergencyReportFormUrl() {
        return getServiceRootUrl() + "/emer/form/getEmerForm";
    }

    /**
     * 获取上报过的事件列表（应急上报查看）
     */
    public String getEmergencyEventListUrl() {
        return getServiceRootUrl() + "/emer/event/getEmerEvent";
    }

    /**
     * +     * 获取事件表单（应急反馈）
     * +
     */
    public String getEmergencyFeedbackForm() {
        return getServiceRootUrl() + "/emer/getFeedbackForm";
    }


    /**
     * 获取应急指令
     */
    public String getEmergencyOrderUrl() {
        return getServiceRootUrl() + "/emer/order/getEmerOrder";
//        http://10.39.3.54:8081/api/v1/sop/emer/order/getEmerOrder?alarmId=58047dbf89f64b3da5a31290aecdb841
    }

    /**
     * 获取应急处置进展详情附件
     */
    public String getEmerFeedprocessAttachUrl(String attachId) {
        return "http://10.39.3.54:8081/api/v1/sop/attach/findById?id=" + attachId + "&token=" + GlobalFunctionInfo.getToken();
    }

    /**
     * +     * 查询处于应急各种状态的事件
     */
    public String getEmerEventAllType() {
        return getServiceRootUrl() + "/emer/event/getEmerEvent";
    }

    /**
     * +     * 获取应急处置进展
     */
    public String getEmerHandleProcessRecord() {
        return getServiceRootUrl() + "/emer/getEmerHandleProcessRecord";
    }

    /**
     * + 上报应急处置进展
     * +
     */
    public String getEmerReportFeedback() {
        return getServiceRootUrl() + "/emer/emerFeedBack";
    }

    /**
     * 获取应急进度上报列表
     *
     * @return url
     */
    public String getProgressReportListUrl() {
        return getServiceRootUrl() + "/emer/getEmerProcessType";
    }

    /**
     * 应急进度上报
     *
     * @return url
     */
    public String getProgressReportUrl() {
        return getServiceRootUrl() + "/emer/updateEmerProcessLog";
    }

    /**
     * 应急进度应急事件范围上报
     *
     * @return url
     */
    public String getProgressReportAreaUrl() {
        return getServiceRootUrl() + "/emer/reportEmerControlArea";
    }

    /**
     * 获取应急进度应急事件范围
     *
     * @return url
     */
    public String getEmerControlArea() {
        return getServiceRootUrl() + "/emer/getEmerControlArea";
    }

    /**
     * 获取爆管分析数据
     *
     * @return url
     */
    public String getAccidentResultUrl() {
        return getServiceRootUrl() + "/emer/location/getController";
    }

    /**
     * 险情确认事件上报（应急）
     */
    public String getEmergencyDangerConfirmSubmmitUrl() {
        return getServiceRootUrl() + "/emer/event/emerConfirm";
    }

    /**
     * 获取专家名单（应急）
     */
    public String getSpecialistsDataUrl() {
        return getServiceRootUrl() + "/emer/expert/getEmerExpert";
    }

    /**
     * 获取应急预案（应急）
     */
    public String getEmerPlan() {
        return getServiceRootUrl() + "/emer/plan/getEmerPlan";
    }

    /**
     * 获取应急预案组织结构（应急）
     */
    public String getEmerOrganization() {
        return getServiceRootUrl() + "/emer/organization/getEmerOrganization";
    }

    /**
     * 获取应急现场人员及车辆（应急）
     */
    public String getEmerControlPersonCar() {
        return getServiceRootUrl() + "/emer/location/getlastposition";
    }

    /**
     * 获取应急现场人员
     */
    public String getEmerControlPerson() {
        return getServiceRootUrl() + "/emer/location/getEmerOrgUsersPosition";
    }

    /**
     * 获取应急列表重新指派时选择的人员（应急）
     */
    public String getEmerPerson() {
        return getServiceRootUrl() + "/emer/location/getEmerSceneController";
    }

    /**
     * 提交指派人员（应急）
     */
    public String changeSceneController() {
        return getServiceRootUrl() + "/emer/event/changeSceneController";
    }

    /**
     * 获取控制应急方案所有信息（应急）
     */
    public String getEmerControlInfo() {
        return getServiceRootUrl() + "/emer/location/getController";
    }

    /**
     * 提交控制应急方案所有信息（应急）
     */
    public String updateEmerControlPlan() {
        return getServiceRootUrl() + "/emer/editSceneControlPlan";
    }

    /**
     * 确认控制应急方案所有信息（应急）
     */
    public String confirmSceneControlPlan() {
        return getServiceRootUrl() + "/emer/confirmSceneControlPlan";
    }

    /**
     * 查询应急事件的所有事件类型（微量泄露，大量泄露，着火，爆炸……）
     */
    public String getEmerEventType() {
        return getServiceRootUrl() + "/emer/event/getEmerEventType";
    }

    /**
     * 查询应急上报时获取事发地点所在的站点和区域
     */
    public String getAreaInfoByEventXy() {
        return getServiceRootUrl() + "/emer/event/getAreaInfoByEventXy";
    }

    /**
     * 应急上报时根据站点获取区域相关信息
     */
    public String getAreaInfoByStationUrl() {
        return getServiceRootUrl() + "/emer/event/getEmerAreaInfo";
    }

    /**
     * 企业的启动模式（用于应急系统里分辨不同公司场景）
     */
    public String getEmerModeConf() {
        return getServiceRootUrl() + "/emer/conf/getEmerModeConf";
    }

    /**
     * 企业的启动模式（用于应急系统里分辨不同公司场景）
     */
    public String uploadAppUsage() {
        return getServiceRootUrl() + "/appUsage/upload";
    }

    /**
     * 隐患管理
     */
    public String getRiskManageList() {
        return getServiceRootUrl() + "/event/getRepairEventList";
    }


    /**
     * 坐标转换控制（目前无法可配置）
     * coors：需转换的坐标字符串，以英文,分隔
     * ecode：企业编码，只有在转换到本地坐标系时才需要，本demo中可以忽略，写死成0011(廊坊新奥)即可
     * fromSrid： 原始坐标系，支持BAIDU(百度经纬度),GD(高德经纬度),BDMecator(百度墨卡托),Mercator(84墨卡托)的相互转换
     * toSrid: 目标坐标系
     */
    public String getCoortransfer() {
        return "http://10.39.1.254:8022/ServiceEngine/rest/services/TransDataServer";
    }

    /**
     * 获取日常监管详情（东莞模式）
     */
    public String getWoDayCheckInfo() {
        return getServiceRootUrl() + "/fieldwork/woController/getPatrolControlDetailInfo";
    }

    /**
     * 获取物料移动类型
     */
    public String getMaterialYDLX() {
        return getServiceRootUrl() + "/business/businessType";
    }

    /**
     * 获取物料工厂公司
     */
    public String getMaterialFactroy() {
        return getServiceRootUrl() + "/business/businessPlace";
    }

    /**
     * 获取物料信息
     */
    public String getMaterialInfo() {
        return getServiceRootUrl() + "/material/findMetaByWlAndFCodePaging";
    }

    /**
     * 获取设备信息
     */
    public String getAllEquipment() {
        return getServiceRootUrl() + "/ldgr";
    }

    /**
     * 提交物料信息
     */
    public String commitMaterialInfo() {
        return getServiceRootUrl() + "/material/metaReserved";
    }

    /**
     * 查询领料信息明细
     */
    public String quareMaterialDetailed() {
        return getServiceRootUrl() + "/material/adoptDetailed";
    }

    /**
     * 查询第三方施工停滞时长
     */
    public String queraStayList() {
        return getServiceRootUrl() + "/fieldwork/woController/stayTimelist";
    }

    /**
     * 查询第三方施工任务总览列表
     */
    public String getThreeWorkList() {
        return getServiceRootUrl() + "/fieldwork/query/getWorkorderListByFunName";
    }


    /*********************** 事件 end**************************************/


    /*********************** 巡线 begin**************************************/

    /**
     * 巡检任务反馈表单上报
     */
    public String getPatrolFeedbackFormSubmitUrl() {
        return getServiceRootUrl() + "/patrol/task/commitFeedBack";
    }

    /**
     * 获取巡线任务URL
     */
    public String getPlanTaskListUrl() {
        return getServiceRootUrl() + "/patrol/task/selectTaskByUserid";
    }

    /**
     * 获取巡线任务列表URL
     */
    public String getSelectTaskByUseridForTestUrl() {
        return getServiceRootUrl() + "/patrol/task/selectTaskByUseridForTest";
    }

    /**
     * 获取巡线任务执行详情URL
     */
    public String getPlanTaskExecuteDetailUrl() {
        return getServiceRootUrl() + "/patrol/task/selectTaskInfoByGid";
    }

    /**
     * 获取巡线任务点反馈详情URL
     */
    public String getPlanTaskPointFeedbackDetailUrl() {
        return getServiceRootUrl() + "/patrol/task/getFeedbackInfoByid";
    }

    /**
     * 获取巡检任务反馈表单
     */
    public String getPatrolFeedbackFormUrl() {
        return getServiceRootUrl() + "/patrol/task/getContentFeedBackList";
    }

    /**
     * 获取轨迹点上报URL
     */
    public String getPositionReportUrl() {
        return getServiceRootUrl() + "/position/reportPosition";
    }

    /**
     * 获取巡线到位点上报URL
     */
    public String getArrivePointReportUrl() {
        return getServiceRootUrl() + "/patrol/task/reportTaskPoint";
    }

    /**
     * 获取巡线到位线上报URL
     */
    public String getArriveLineReportUrl() {
        return getServiceRootUrl() + "/patrol/task/reportPipeline";
    }

    /**
     * 获取养护到位点上报URL
     */
    public String getMaintainArrivePointReportUrl() {
        return getServiceRootUrl() + "/task/task/arrive";
    }

    /**
     * 获取养护到位点上报URL
     */
    public String getPromotionMaintainArrivePointReportUrl() {
        return getServiceRootUrl() + "/task/arrive";
    }

    /**
     * 巡视任务备注上报服务
     */
    public String planTaskRemarkReportUrl() {
        return getServiceRootUrl() + "/patrol/task/log/insertTaskLog";
    }

    /*********************** 巡线 end**************************************/


    /*********************** 工单 begin**************************************/

    /**
     * 获取工单列表数据URL
     */
    public String getWorkOrderListUrl() {
        return getServiceRootUrl() + "/fieldwork/query/selectFieldworkList";
    }

    /**
     * 获取工单详情Tab页URL
     */
    public String getWorkOrderDetailTabUrl() {
        return getServiceRootUrl() + "/fieldwork/query/detailtab";
    }

    /**
     * 工单流程表单页URL
     */
    public String getTaskFormDataUrl() {
        return getServiceRootUrl() + "/fieldwork/process/getTaskFormData";
    }

    /**
     * 工单流程表单页上报URL
     */
    public String getSubmitTaskFormDataUrl() {
        return getServiceRootUrl() + "/fieldwork/process/submitTaskFormData";
    }

    /**
     * 获取 根据工单流程id获取工单
     *
     * @return
     */
    public String getObtainWorkOrderByProcessinstanceId() {
        return getServiceRootUrl() + "/fieldwork/query/selectFieldworkByProcessinstancedid";
    }

    /*********************** 工单 end**************************************/


    /*********************** 养护 begin**************************************/

    /**
     * 获得阀门养护任务列表
     */
    public String getValveMaintainTask() {
        return getRootServiceUrl() + "/task/task/list";
    }

    /**
     * 获得养护反馈内容
     */
    public String getMaintainFeedbackUrl() {
        return getRootServiceUrl() + "/task/feedback/form";
    }

    /**
     * 获得表具检定养护反馈表单url
     */
    public String getMaintainMeterFeedbackUrl() {
        return getRootServiceUrl() + "/task/feedback/household/form";
    }

    /**
     * 获得表具检定养护反馈表单提交URL
     */
    public String getMaintainMeterFeedbackReportUrl() {
        return getRootServiceUrl() + "/task/feedback/household/save";
    }

    /**
     * 获得阀门请求的列表详情
     */
    public String getValveMaintainDetailUrl() {
        return getRootServiceUrl() + "/task/task/detail";
    }

    /**
     * 提交养护反馈内容
     */
    public String getMaintainFeedbackSaveUrl() {
        return getRootServiceUrl() + "/task/feedback/save";
    }

    /**
     * 提交养护反馈内容
     */
    public String getPromotionMaintainFeedbackSaveUrl() {
        return getRootServiceUrl() + "/task/feedback";
    }

    /**
     * 获取【切断/放散】调试表单URL
     */
    public String getValueDebugFromUrl() {
        return getServiceRootUrl() + "/task/feedback/regulator/debug/form";
    }

    /**
     * 获取【切断/放散】调试表单上报URL
     */
    public String getValueDebugFromCommitUrl() {
        return getServiceRootUrl() + "/task/feedback/regulator/debug/save";
    }

    /**
     * 获取阀门【切断/放散】调试表单详情URL
     */
    public String getValueDebugFromDetailUrl() {
        return getServiceRootUrl() + "/task/feedback/valve/debug/detail";
    }

    /**
     * 获取阀门 历史信息表单详情URL
     */
    public String getValueHistoryDetailUrl() {
        return getServiceRootUrl() + "/task/task/history";
    }

    /**
     * 获取所有未完成的养护任务
     *
     * @return
     */
    public String getMaintainTaskListUrl() {
        return getServiceRootUrl() + "/task/task/list/todo";
    }

    /**
     * 获取养护任务转交，查询某一站点下人员的接口Url
     *
     * @return
     */
    public String getStationUserUrl() {
        return getServiceRootUrl() + "/station/stationUser";
    }

    /**
     * 获取查询所有人员的接口Url
     *
     * @return
     */
    public String getUserListUrl() {
        return getServiceRootUrl() + "/station/user/list";
    }

    /**
     * 根据userid 查询相关人员url
     */
    public String getUserListUrl1() {
        return getServiceRootUrl() + "/user/getUserOrg";
    }

    /**
     * 获取养护任务转交,提交的接口Url
     *
     * @return
     */
    public String getTaskHandOverUrl() {
        return getServiceRootUrl() + "/task/task/handover";
    }

    /**
     * 获取巡线任务转交,提交的接口Url
     *
     * @return
     */
    public String getPlanTaskHandOverUrl() {
        return getServiceRootUrl() + "/patrol/task/handoverTask";
    }

    /**
     * 获取延长停气,表单的接口Url
     *
     * @return
     */
    public String getExtendsStopGasFormUrl() {
        return getServiceRootUrl() + "/stopgas/getStopGasDelayForm";
    }

    /**
     * 获取延长停气表单上报的接口Url
     *
     * @return
     */
    public String getExtendsStopGasFromReportUrl() {
        return getServiceRootUrl() + "/stopgas/updateStopGas ";
    }

    /**
     * 获取申请停气,表单的接口Url
     *
     * @return
     */
    public String getApplyStopGasFormUrl() {
        return getServiceRootUrl() + "/stopgas/getStopGasForm";
    }

    /**
     * 获取停气列表数据URL
     *
     * @return
     */
    public String getStopGasListUrl() {
        return getServiceRootUrl() + "/stopgas/getStopGasListByCondition";
    }

    /**
     * 获取停气列表数据URL
     *
     * @return
     */
    public String getPlanTaskDetailUrl() {
        return getServiceRootUrl() + "/patrol/task/selectTaskInfoByGid";
    }

    /**
     * 获取申请停气表单上报的接口Url
     *
     * @return
     */
    public String getApplyStopGasFromReportUrl() {
        return getServiceRootUrl() + "/stopgas/reportStopGas";
    }

    /**
     * 获取设备选取坐标位置上报的接口Url
     *
     * @return
     */
    public String getDeviceSetLocationReportUrl() {
        return getServiceRootUrl() + "/ldgr";
    }

    /**
     * 获取养护设备信息的接口Url
     *
     * @return
     */
    public String getMaintainTaskBriefUrl() {
        return getServiceRootUrl() + "/task/task/list/brief";
    }

    /**
     * 获取养护设备信息的接口Url
     *
     * @return
     */
    public String getPromotionMaintainTaskBriefUrl() {
        return getServiceRootUrl() + "/task/list/brief";
    }

    /**
     * 获取处理维保任务工单Url
     *
     * @return
     */
    public String getReportTaskFormEventUrl() {
        return getServiceRootUrl() + "/event/reportTaskFormEvent";
    }

    /**
     * 获取蓝牙检漏信息上报的接口Url
     *
     * @return
     */
    public String getBTTaskReportUrl() {
        return getServiceRootUrl() + "/task/feedback/checkleak/info/save";
    }

    /**
     * 获取蓝牙检漏信息上报详情的接口Url
     *
     * @return
     */
    public String getBTTaskFeedbackDetailUrl() {
        return getServiceRootUrl() + "/task/feedback/checkleak/detail";
    }

    /**
     * 获取巡检车检漏信息上报的接口Url
     *
     * @return
     */
    public String getCarLeakTaskReportUrl() {
        return getServiceRootUrl() + "/patrol/leakcar/insert";
    }

    /**
     * 获取蓝牙检漏信息上报详情的接口Url
     *
     * @return
     */
    public String getCarTaskFeedbackDetailUrl() {
        return getServiceRootUrl() + "/patrol/leakcar/getLeakCarListByCondition";
    }

    /**
     * 获取检漏任务停止的接口Url
     *
     * @return
     */
    public String getLeakTaskFinishUrl() {
        return getServiceRootUrl() + "/task/task/finish";
    }

    /**
     * 获取根据养护任务id获取养护任务信息的接口Url
     *
     * @return
     */
    public String getMaintainTaskByTaskId() {
        return getServiceRootUrl() + "/task/task/list";
    }

    /**
     * 获取根据养护任务id获取养护任务信息的接口Url
     *
     * @return
     */
    public String getMaintainTaskByTaskIdNew() {
        return getServiceRootUrl() + "/task/task/single";
    }

    /**
     * 获取根据养护任务id获取养护任务信息的接口Url
     *
     * @return
     */
    public String getPromotionMaintainTaskByTaskIdNew() {
        return getServiceRootUrl() + "/task/single";
    }

    /**
     * 获取根据工商户id获取工商户关联设备Url
     *
     * @return
     */
    public String getFindEqptByGshUrl() {
        return getServiceRootUrl() + "/gshEqptFind/findEqptByGsh";
    }

    /**
     * 获取根据设备id以及设备编号获取作业类型Url
     *
     * @return
     */
    public String getFunctionByEqUrl() {
        return getServiceRootUrl() + "/config/function/list/by/eq";
    }


    /*********************** 养护 end**************************************/


    /*********************** 场站 begin**************************************/

    /**
     * 场站主界面tab组URL
     */
    public String getStationMainActiviyGroupUrl() {
        return getServiceRootUrl() + "/task/plan/groups";
    }


    /**
     * 新场站主界面tab组URL
     */
    public String getStationMainGroupTab() {
        return getRootServiceUrl() + "/station/area/list";
    }

    /**
     * 新场站设备单元tab
     */
    public String getEquipmentUnitList() {
        return getRootServiceUrl() + "/station/equipment/unit/list";
    }

    /**
     * 任务检查项上报
     */
    public String getEquipmentUploader() {
        return getRootServiceUrl() + "/station/feedback/multi";
    }

    /**
     * 场站卡片数据
     */
    public String getStationMainActivityTaskCardUrl() {
        return getServiceRootUrl() + "/task/task/card/list/groupname";
    }

    /**
     * 新场站卡片数据
     */
    public String getStationMainTaskCardUrl() {
        return getServiceRootUrl() + "/station/pad/summary";
    }

    /**
     * 场站任务按时间查询表单数据
     */
    public String getStationTaskByTime() {
        return getServiceRootUrl() + "/station/pad/summary/detail";
    }

    /**
     * 场站按时间查询的表单数据组
     */
    public String getStationTaskTimeItemUrl() {
        return getServiceRootUrl() + "/task/task/list/time";
    }

    /**
     * 场站按时间查询下一个任务
     */
    public String getStationNextTaskUrl() {
        return getServiceRootUrl() + "/task/task/list/time";
    }

    /**
     * 场站预防性维护
     */
    public String getPreventiveTaskUrl() {
        return getServiceRootUrl() + "/task/task/card/list/groupname";
    }

    /**
     * 预防性维护任务
     */
    public String getPreventiveTaskList() {
        return getRootServiceUrl() + "/prevmaintain/tasks";
    }

    /**
     * 预防性维护任务详情
     */
    public String getPreventiveTaskDetail() {
        return getRootServiceUrl() + "/prevmaintain/task/detail";
    }

    /**
     * 预防性维护任务反馈
     */
    public String getPreventiveTaskFeedback() {
        return getRootServiceUrl() + "/prevmaintain/feedback";
    }

    /**
     * record排序
     */
    public String getSortPlans() {
        return getServiceRootUrl() + "/task/plan/plans";
    }

    /**
     * 上报计划名排序
     */
    public String getSortUploader() {
        return getServiceRootUrl() + "/task/plan/plan/order";
    }

    /**
     * 新上报equipmentUnit计划名排序
     */
    public String getSortEquipmentUnit() {
        return getServiceRootUrl() + "/station/equipment/unit/order";
    }

    /**
     * 上报tab组名排序
     */
    public String getSortTabUploader() {
        return getServiceRootUrl() + "/task/plan/group/order";
    }

    /**
     * 新上报tab组名排序
     */
    public String getSortTabAreaUploader() {
        return getRootServiceUrl() + "/station/area/order";
    }

    /**
     * 获取加臭记录url
     */
    public String getJiaChouRecordUrl() {
        return getRootServiceUrl() + "/station/smelly/record/list";
    }

    /**
     * 获取加臭记录表单url
     */
    public String getJiaChouRecordFormUrl() {
        return getRootServiceUrl() + "/station/smelly/form";
    }

    /**
     * 场站信息 url
     */
    public String getStationInfoUrl() {
        return getServiceRootUrl() + "/user/userinfo/location/B";
    }

    /**
     * 加臭记录类型 url
     */
    public String getSmellyOperTypesUrl() {
        return getServiceRootUrl() + "/station/smelly/oper/types";
    }

    /**
     * 加臭机类型 url
     */
    public String getSmellyMachineListUrl() {
//        return getServiceRootUrl() + "/station/smelly/machine/list";
        return getServiceRootUrl() + "/station/smelly/association/machine/list";
    }

    /**
     * 加臭机状态 url
     */
    public String getSmellyMachineStatusUrl() {
        return getServiceRootUrl() + "/station/smelly/machine/status";
    }

    /**
     * 加臭记录详情 url
     */
    public String getSmellyRecordDetailUrl() {
        return getServiceRootUrl() + "/station/smelly/record/detail";
    }

    /**
     * 添加加臭记录 url
     */
    public String getAddSmellyRecordUrl() {
        return getServiceRootUrl() + "/station/smelly/record/new";
    }

    /**
     * 查询场站设备位置树 url
     */
    public String getEqLocationTreeUrl() {
        return getServiceRootUrl() + "/eqLocation/tree";
    }

    /**
     * 查询场站设备分类树 url
     */
    public String getEqClassificationTreeUrl() {
        return getServiceRootUrl() + "/eqClassification/tree";
    }

    /**
     * 查询设备信息 url
     */
    public String getDeviceInfoUrl() {
        return getServiceRootUrl() + "/ldgr";
    }

    /**
     * 查询场站故障类型树 url
     */
    public String getFaultFailureTreeUrl() {
        return getServiceRootUrl() + "/failure";
    }

    /**
     * 场站调压记录列表
     */
    public String getRegulatorRecordlist() {
        return getRootServiceUrl() + "/adjust/pressure/query/adjust/pressure/record";
    }

    /**
     * 获得场站调压记录检查对象
     */
    public String getAdjustPressureCheckTarget() {
        return getRootServiceUrl() + "/adjust/pressure/check/target/list";
    }

    /**
     * 上报添加调压记录
     */
    public String getUploaderAdjustPressureRecord() {
        return getRootServiceUrl() + "/adjust/pressure/save/adjust/pressure/record";
    }

    /**
     * 查询调压管路
     */
    public String getQueryAdjustPressurePipeline() {
        return getRootServiceUrl() + "/adjust/pressure/query/adjust/pressure/target";
    }

    /**
     * 查询调压记录详情
     */
    public String getAdjustPressureDetail() {
        return getRootServiceUrl() + "/adjust/pressure/detail";
    }

    /**
     * 查询调压对象的检查项列表
     */
    public String getAdjustPressureCheckTargetList() {
        return getRootServiceUrl() + "/adjust/pressure/query/check/targets";
    }

    /**
     * 获取场站事件处理表单URL
     */
    public String getCZStartFormDataURL() {
        return getServiceRootUrl() + "/event/getCZStartFormData";
    }

    /**
     * 获取场站事件处理表单上报URL
     */
    public String getStartProcessForCZHandleURL() {
        return getServiceRootUrl() + "/event/startProcessForCZHandle";
    }

    /**
     * 场站工单处理流程名称URL
     */
    public String getStationWorkOrderProcessListUrl() {
        return getServiceRootUrl() + "/fieldwork/query/processlist";
    }

    /**
     * 场站工单处理流程名称URL
     */
    public String getStationWorkOrderUserTaskUrl() {
        return getServiceRootUrl() + "/fieldwork/process/getUserTask";
    }

    /*********************** 场站 end**************************************/

    /**
     * 获取最新的值班信息
     */
    public String getNewOndutyInfoUrl() {
        return getServiceRootUrl() + "/station/duty/zbrz/last";
    }

    /**
     * 获取值班班次
     */
    public String getDutyWorkShift() {
        return getServiceRootUrl() + "/station/query/work/time";
    }

    /**
     * 获取交接班班信息
     */
    public String getOndutyInfoUrl() {
        return getServiceRootUrl() + "/station/duty/mb/last";
    }

    /**
     * 提交交班信息
     */
    public String getSubmitTurnoverOndutyInfoUrl() {
        return getServiceRootUrl() + "/station/duty/zbrz/jiaoban";
    }

    /**
     * 更新交班信息
     */
    public String getUpDateTurnoverOndutyInfoUrl() {
        return getServiceRootUrl() + "/station/duty/zbrz/updatezhiban";
    }

    /**
     * 提交接班信息
     */
    public String getSubmitTurnOndutyInfoUrl() {
        return getServiceRootUrl() + "/station/duty/zbrz/jieban";
    }

    /**
     * 提交当班信息
     */
    public String getSubmitOndutyInfoUrl() {
        return getServiceRootUrl() + "/station/duty/zbrz/zhiban";
    }

    /*********************** 停滞点 end**************************************/
    public String getStopPointServiceUrl() {
        return getServiceRootUrl() + "/patrol/staypos/batchInsertStayPos";
    }
    /*********************** 场站 end**************************************/

    /*********************** 预防性维护 end**************************************/

    /**
     * 预防性维护
     */
    public String getPreventiveGroupTab() {
        return getServiceRootUrl() + "/task/function/list";
    }

    public String getLoginDahuaUrl() {
        return getServiceRootUrl() + "/emer/dh/video/loginDHApp";
    }

    public String getLogoutDahuaUrl() {
        return getServiceRootUrl() + "/emer/dh/video/logoutDHApp";
    }

    /*********************** 考勤 start*************************************/
    /**
     * 获取打卡操作名称 url
     *
     * @return
     */
    public String getAttendanceNextStepUrl() {
        return getServiceRootUrl() + "/duty/sign/nextstep";
    }

    /**
     * 获取打卡上报 url
     *
     * @return
     */
    public String getAttendanceSignUrl() {
        return getServiceRootUrl() + "/duty/sign/new";
    }

    /**
     * 获取调休申请上报 url
     *
     * @return
     */
    public String getRestApplyReportUrl() {
        return getServiceRootUrl() + "/duty/sign/rest/new";
    }

    /**
     * 获取考勤打卡记录 url
     *
     * @return
     */
    public String getAttendanceSignRecordUrl() {
        return getServiceRootUrl() + "/duty/sign/list";
    }

    /**
     * 获取考勤打卡记录(新) url
     *
     * @return
     */
    public String getAttendanceSignRecordNewUrl() {
        return getServiceRootUrl() + "/duty/sign/list/new";
    }

    /**
     * 获取自己的考勤打卡记录 url
     *
     * @return
     */
    public String getAttendanceSignRecordItSelfUrl() {
        return getServiceRootUrl() + "/duty/sign/list/for/self";
    }

    /**
     * 上传考勤记录
     */
    public String getUploaderSignCheck() {
        return getServiceRootUrl() + "/duty/sign/check";
    }

    /*********************** 考勤 end*************************************/

    /*********************** 管网监测 start*************************************/

    /**
     * 查询管网监测类型
     */
    public String getPipeDetectTaskType() {
        return getServiceRootUrl() + "/detection/types";
    }

    /**
     * 查询管网监测任务列表
     */
    public String getPipeDetectTaskList() {
        return getServiceRootUrl() + "/emer/location/getDetectionMessage";
    }

    /*********************** 管网监测 end*************************************/

    /*********************** 设备台账 start*************************************/

    /**
     * 查询设备隐患记录
     */
    public String getYHRecord() {
        return getServiceRootUrl() + "/fieldwork/query/selectYHRecord";
    }

    /**
     * 查询设备维修历史记录
     */
    public String getHistoryWorkerOrder() {
        return getServiceRootUrl() + "/fieldwork/query/selectHistoryWorkOrder";
    }

    /**
     * 查询设备技术参数
     */
    public String getEquipmentProperty() {
        return getServiceRootUrl() + "/spcf/";
    }

    /**
     * 查询设备台账
     */
    public String getEquipmentAccount() {
        return getServiceRootUrl() + "/ldgr/";
    }

    /**
     * 查询设备台账
     */
    public String getEquipmentPreventive() {
        return getServiceRootUrl() + "/prevmaintain/task/list/eq";
    }

    /**
     * 查询设备台账
     */
    public String getEquipmentMaintain() {
        return getServiceRootUrl() + "/task/task/list/by/eq";
    }

    /**
     * 查询设备预防性维护任务数量
     */
    public String getEquipmentPrevNum() {
        return getServiceRootUrl() + "/prevmaintain/task/count/eq";
    }

    /**
     * 查询设备养护任务数量
     */
    public String getEquipmentMaintainNum() {
        return getServiceRootUrl() + "/task/task/count/by/eq";
    }

    /**
     * 查询设备维修任务数量
     */
    public String getEquipmentRepairNum() {
        return getServiceRootUrl() + "/fieldwork/query/selectHistoryWorkOrderCount";
    }

    /**
     * 查询设备隐患任务数量
     */
    public String getEquipmentYHNum() {
        return getServiceRootUrl() + "/fieldwork/query/selectYHRecordCount";
    }

    /*
    *  获取用气上报表单信息
    * **/
    public String getEnergyInfosUrl() {
        return getServiceRootUrl() + "/task/feedback/form";
    }


    /*********************** 设备台账 end*************************************/

    /*********************** 预防性维护 begin*************************************/

    /**
     * 查询预防性维护任务列表
     */
    public String getPrevMaintainTaskList() {
        return getServiceRootUrl() + "/task/list/for/pad";
    }

    /**
     * 查询预防性维护反馈提交
     */
    public String getPrevMaintainFeedbackUploader() {
        return getServiceRootUrl() + "/task/feedback";
    }

    /**
     * 查询预防性维护任务详情
     */
    public String getPrevMaintainTaskDetail() {
        return getServiceRootUrl() + "/task/detail";
    }


    /**
     * 查询预防性维护任务详情
     */
    public String getPrevFunctionKeyList() {
        return getServiceRootUrl() + "/config/function/list";
    }

    /**
     * 计划性维护任务 转交
     */
    public String getPrevHandOver() {
        return getServiceRootUrl() + "/task/transfer";
    }


    /*********************** 预防性维护 end*************************************/
}
