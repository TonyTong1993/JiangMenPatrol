package com.ecity.cswatersupply.event;

public class ResponseEventStatus {
    public static final int OK = 1;
    public static final int ERROR = 0;

    private static final int EVENT_BASE = 10000000;
    public static final int TestConnectionEvent = EVENT_BASE + 1;
    public static final int LoginGetTokenEvent = EVENT_BASE + 4;
    public static final int LoginEvent = EVENT_BASE + 2;
    public static final int GET_MAPCONFIG_EVENT = EVENT_BASE + 3;
    public static final int WatchEvent = EVENT_BASE + 5;
    public static final int WatchState = EVENT_BASE + 6;
    public static final int UPLOAD_SINGLE_FILE = EVENT_BASE + 7;
    public static final int UPLOAD_ALL_FILES = EVENT_BASE + 8;
    public static final int GET_MAPCONFIG_EVENT_CZDZ = EVENT_BASE + 9;
    public static final int NET_SERVER_QUERY_NODE = 10;
    public static final int UPDATE_PATROL_MAN_STATE = 11;

    //值班签到签退
    public static final int SIGN_IN_EVENT = EVENT_BASE + 100;
    public static final int SIGN_OUT_EVENT = SIGN_IN_EVENT + 1;
    public static final int GET_ALL_PATROL_MAN = SIGN_IN_EVENT + 2;
    //获取巡检员轨迹
    public static final int GET_PATROL_MAN_TRACK = SIGN_IN_EVENT + 3;
    //获取外勤车辆
    public static final int GET_PATROL_BUS = SIGN_IN_EVENT + 4;

    private static final int USER_BASE = EVENT_BASE + 500;
    public static final int USER_CHANGE_PASSWORD = USER_BASE + 1;

    private static final int WORKORDER_BASE = EVENT_BASE + 1000;
    public static final int WORKORDER_DOWN_ALL_WORKORDER = WORKORDER_BASE + 1;
    public static final int WORKORDER_DOWN_FILTER_WORKORDER = WORKORDER_BASE + 2;
    public static final int WORKORDER_DOWN_CATEGORY_WORKORDER = WORKORDER_BASE + 3;
    public static final int WORKORDER_DOWN_CATEGORY__FILTER_WORKORDER = WORKORDER_BASE + 28;
    public static final int WORKORDER_DOWN_CATEGORY_AMOUNT_WORKORDER = WORKORDER_BASE + 26;
    public static final int WORKORDER_SEARCH_WORKORDER_RESULT = WORKORDER_BASE + 27;
    public static final int WORKORDER_DOWN_WORKORER_SUMMARY_DETAIL = WORKORDER_BASE + 29;
    public static final int WORKORDER_DOWN_WORKORER_SUMMARY_DETAIL_FROM_TOTAL = WORKORDER_BASE + 34;
    public static final int WORKORDER_DOWN_By_WORKORDER_ID = WORKORDER_BASE + 35;

    public static final int WORKORDER_DETAIL = WORKORDER_BASE + 4;
    public static final int WORKORDER_QUERY_ATTACHMENT = WORKORDER_BASE + 5;

    public static final int WORKORDER_RETURN_EVENT = WORKORDER_BASE + 6;
    public static final int WORKORDER_ACCEPT_EVENT = WORKORDER_BASE + 7;
    public static final int WORKORDER_REPORT_TABLE = WORKORDER_BASE + 8;
    public static final int WORKORDER_ASKFINISH_EVENT = WORKORDER_BASE + 9;
    public static final int WORKORDER_POCESS_EVENT = WORKORDER_BASE + 10;
    public static final int WORKORDER_GET_CONTACT_MAN = WORKORDER_BASE + 11;
    public static final int WORKORDER_DISPATCH = WORKORDER_BASE + 12;
    public static final int WORKORDER_INSPECTITEM_REPORT = WORKORDER_BASE + 13;
    public static final int WORKORDER_GET_MATERIAL_INFO = WORKORDER_BASE + 15;
    public static final int WORKORDER_CANCEL_DELAY = WORKORDER_BASE + 16;
    public static final int WORKORDER_CANCEL_ASSIST = WORKORDER_BASE + 17;
    public static final int WORKORDER_CANCEL_TRANSFER = WORKORDER_BASE + 18;
    public static final int WORKORDER_CANCEL_RETURN = WORKORDER_BASE + 19;
    //日志回溯
    public static final int WORKORDER_OPERATION_LOGS = WORKORDER_BASE + 20;
    //工单详情信息集
    public static final int WORKORDER_GET_DETAIL_INFO = WORKORDER_BASE + 21;
    public static final int WORKORDER_GET_FLOW_DETAIL_INFO = WORKORDER_BASE + 22;
    //流程信息详情
    public static final int WORKORDER_DETAIL_FLOW_INFO = WORKORDER_BASE + 23;
    public static final int WORKORDER_GET_EVENT_2_WORK_ORDER_FORM = WORKORDER_BASE + 24;
    public static final int WORKORDER_SUBMIT_EVENT_2_WORK_ORDER_FORM = WORKORDER_BASE + 25;
    public static final int WORKORDER_GET_EVENT_2_TASK_FORM = WORKORDER_BASE + 30;
    public static final int WORKORDER_SUBMIT_EVENT_2_TASK_FORM = WORKORDER_BASE + 31;

    public static final int WORKORDER_BACK_TRANSFER = WORKORDER_BASE + 32;
    public static final int WORKORDER_BACK_TRANSFER_CANCEL = WORKORDER_BASE + 33;
    public static final int WORKORDER_GET_UNFINISHED_INFOS = WORKORDER_BASE + 35;


    // ####################################################
    //新的服务下，请求的eventid
    public static final int WORKORDER_QUERY_WORKORDER_LIST = WORKORDER_BASE + 36;
    public static final int WORKORDER_GET_DETAIL_TAB = WORKORDER_BASE + 37;
    public static final int WORKORDER_GET_DETAIL_BASIC = WORKORDER_BASE + 38;
    public static final int WORKORDER_GET_DETAIL_EXPLOR = WORKORDER_BASE + 39;
    public static final int WORKORDER_GET_DETAIL_AUDIT = WORKORDER_BASE + 40;
    public static final int WORKORDER_GET_DETAIL_REPAIR = WORKORDER_BASE + 41;
    public static final int WORKORDER_GET_DETAIL_MATERIAL = WORKORDER_BASE + 42;
    public static final int WORKORDER_GET_DETAIL_LOG = WORKORDER_BASE + 43;
    public static final int FORM_GET_FORM_INSPECTITEMS = WORKORDER_BASE + 44;


    // Submit Positions begin
    private static final int SUBMIT_POSITIONS_BASE = EVENT_BASE + 2000;
    public static final int SUBMIT_POSITIONS_DONE = SUBMIT_POSITIONS_BASE + 1;
    // file operation begin
    public static final int FILE_OPERATION_BASE = EVENT_BASE + 3000;
    public static final int FILE_OPERATION_DOWNLOAD_FINISH = FILE_OPERATION_BASE + 1;
    public static final int FILE_OPERATION_UPDATE_PROGRESS = FILE_OPERATION_BASE + 2;

    public static final int NOTIFICATION_BASE = EVENT_BASE + 4000;
    public static final int NOTIFICATION_REGISTER_FAIL = NOTIFICATION_BASE + 1;
    public static final int NOTIFICATION_REGISTER_SUCCESS = NOTIFICATION_BASE + 2;

    public static final int GET_WORKORDER_INSPECT_ITEMS = EVENT_BASE + 5000;

    // planningtask begin
    private static final int PLANNINGTASK_BASE = EVENT_BASE + 44000;
    public static final int PLANNINGTASK_LIST = PLANNINGTASK_BASE + 1;
    public static final int PLANNINGTASK_LIST_MAIN = PLANNINGTASK_BASE + 2;
    public static final int PLANNING_POINT_ARRIVED = PLANNINGTASK_BASE + 3;
    public static final int PLANNING_REPORTINSPECTITEMS = PLANNINGTASK_BASE + 4;
    public static final int PLANNING_ATTRS_POINTSERVICE = PLANNINGTASK_BASE + 5;
    public static final int PLANNING_ATTRS = PLANNINGTASK_BASE + 6;
    public static final int PLANNING_LINE_ARRIVED = PLANNINGTASK_BASE + 7;

    public static final int MAP_GET_PLAN_AND_PIPE_ADDRESS = PLANNINGTASK_BASE + 8;

    // Event report begin
    private static final int EVENT_REPORT_BASE = EVENT_BASE + 5000;
    public static final int EVENT_REPORT_GET_PARAMS = EVENT_REPORT_BASE + 1;
    public static final int EVENT_REPORT_GET_EVENT_LIST = EVENT_REPORT_BASE + 2;
    public static final int EVENT_REPORT_GET_TYPE_LIST = EVENT_REPORT_BASE + 3;
    public static final int EVENT_REPORT_CLOSE_EVENT = EVENT_REPORT_BASE + 4;
    public static final int EVENT_REPORT_GET_VALVE_SWITCH = EVENT_REPORT_BASE + 5;
    public static final int EVENT_REPORT_GIS_ERROR_REPORT = EVENT_REPORT_BASE + 6;
    public static final int EVENT_REPORT_GET_WORK_ORDER_INFO = EVENT_REPORT_BASE + 7;
    public static final int EVENT_GET_ALL_PUMP_INFO = EVENT_REPORT_BASE + 8;
    public static final int EVENT_UPDATE_VALVE_INFO = EVENT_REPORT_BASE + 9;
    public static final int EVENT_GET_WORK_ORDER_FIELDS = EVENT_REPORT_BASE + 10;
    public static final int EVENT_GET_PUMP_MAINTAIN_INFO = EVENT_REPORT_BASE + 11;
    public static final int EVENT_GET_PUMP_MAINTAIN_INSPECT_ITEM = EVENT_REPORT_BASE + 12;
    public static final int EVENT_GET_FZ_MODIFY_INSPECTS = EVENT_REPORT_BASE + 13;
    // Event report end

    //Punishment begin
    private static final int PUNISHMENT_BASE = EVENT_BASE + 6000;
    public static final int PUNISHMENT_DETAILS = PUNISHMENT_BASE + 2;
    public static final int PUNISHMENT_STATE_CHANGE = PUNISHMENT_BASE + 3;

    // Message begin
    private static final int MESSAGE_BASE = EVENT_BASE + 7000;
    public static final int MESSAGE_QUERY = MESSAGE_BASE + 1;
    public static final int MESSAGE_DELETE = MESSAGE_BASE + 2;
    public static final int MESSAGE_READ = MESSAGE_BASE + 3;

    // 应急响应相关事件 begin
    public static final int EMERGENCY_BASE = EVENT_BASE + 9000;
    public static final int EMERGENCY_GET_EARTHQUAKE_ALL = EMERGENCY_BASE + 1;
    public static final int EMERGENCY_GET_EQMONITORSTATION_ALL = EMERGENCY_BASE + 2;
    public static final int EMERGENCY_GET_QBOREGIONS = EMERGENCY_BASE + 3;
    public static final int EMERGENCY_GET_QBODATDS = EMERGENCY_BASE + 4;
    public static final int EMERGENCY_GET_EQQUICKREPORT_INFO = EMERGENCY_BASE + 5;
    public static final int EMERGENCY_GET_EQQUICKREPORT_NEW_INSPECT_INFO = EMERGENCY_BASE + 6;
    public static final int EMERGENCY_GET_EQQUICKREPORT_UPDATE_INSPECT_INFO = EMERGENCY_BASE + 7;
    public static final int EMERGENCY_GET_EQQXCYJDC_INSPECT_INFO = EMERGENCY_BASE + 8;
    public static final int EMERGENCY_GET_EQQXCYJDC_INSPECT_INFO_UPDATE = EMERGENCY_BASE + 17;
    public static final int EMERGENCY_EARTHQUAKE_INFO_SEARCH_PARAMS = EMERGENCY_BASE + 9;
    public static final int EMERGENCY_EARTHQUAKE_XCDC_REPORT = EMERGENCY_BASE + 10;
    public static final int EMERGENCY_GET_UNUSUAL_REPORT_FORM = EMERGENCY_BASE + 11;
    public static final int EMERGENCY_GET_INVESGATION_INFO = EMERGENCY_BASE + 12;
    public static final int EMERGENCY_GET_DISTRION_QUAKE_INFO = EMERGENCY_BASE + 13;
    public static final int EMERGENCY_GET_NOTICE_LIST = EMERGENCY_BASE + 14;
    public static final int EMERGENCY_GET_NOTICE_Content = EMERGENCY_BASE + 15;
    public static final int EMERGENCY_GET_CONTACT = EMERGENCY_BASE + 16;
    public static final int EMERGENCY_GET_REPORT_RELATE = EMERGENCY_BASE + 17;
    //获取重要地震
    public static final int EMERGENCY_GET_IMPORT_EARTHQUAKE = EMERGENCY_BASE + 18;
    public static final int EMERGENCY_UPDATE_MAN_STATE = EMERGENCY_BASE + 19;

    // 江门工程 begin
    private static final int PROJECT_BASE = EVENT_BASE + 10000;
    public static final int PROJECT_GET_PROJECTS = PROJECT_BASE + 1;
    public static final int PROJECT_GET_PROJECT_TYPE_COUNT = PROJECT_BASE + 3;
    public static final int PROJECT_GET_PROJECT_DETAIL_TAB_BASEINFO = PROJECT_BASE + 4;
    public static final int PROJECT_GET_PROJECT_DETAIL_TAB_WORKLOAD = PROJECT_BASE + 5;
    public static final int PROJECT_GET_PROJECT_DETAIL_TAB_PROGRESS = PROJECT_BASE + 6;
    public static final int PROJECT_GET_PROJECT_DETAIL_TAB_CHANGE = PROJECT_BASE + 7;
    public static final int PROJECT_GET_PROJECT_DETAIL_TAB_PAY = PROJECT_BASE + 8;
    public static final int PROJECT_GET_PROJECT_SUMMARY_COUNT = PROJECT_BASE + 9;
    public static final int PROJECT_GET_PROJECT_ATTACHMENT = PROJECT_BASE + 10;
    public static final int PROJECT_GET_PROJECTS_LOG_BACK = PROJECT_BASE + 11;

    private static final int PROJECT_KAN_CHA_SHE_JI_BASE = PROJECT_BASE + 1000;
    public static final int PROJECT_GET_PROSPECTIVE_LIST_NEW = PROJECT_KAN_CHA_SHE_JI_BASE + 1;
    public static final int PROJECT_GET_PROSPECTIVE_LIST_OLD = PROJECT_KAN_CHA_SHE_JI_BASE + 2;
    public static final int PROJECT_GET_PROSPECTIVE_VIEW = PROJECT_KAN_CHA_SHE_JI_BASE + 3;
    public static final int PROJECT_GET_PROSPECTIVE_VIEW_FROM_MSG = PROJECT_KAN_CHA_SHE_JI_BASE + 4;

    private static final int PROJECT_WORKLOAD_BASE = PROJECT_BASE + 2000;
    public static final int PROJECT_WORKLOAD_GET_PROJECTS = PROJECT_WORKLOAD_BASE + 1;
    public static final int PROJECT_WORKLOAD_GET_RECORD_DETAIL = PROJECT_WORKLOAD_BASE + 2;
    public static final int PROJECT_WORKLOAD_GET_RECORD_LIST = PROJECT_WORKLOAD_BASE + 3;
    public static final int PROJECT_WORKLOAD_CHECK = PROJECT_WORKLOAD_BASE + 4;

    public static final int PROJECT_WORKLOAD_GET_ALL_RECORDS = PROJECT_WORKLOAD_BASE + 5;

    //勘察设计
    public static final int PROJECT_GET_PROSPECTIVE_DETAIL_TAB_BASEINFO = PROJECT_BASE + 14;
    public static final int PROJECT_GET_PROJECT_DETAIL_TAB_APPOINT = PROJECT_BASE + 15;
    public static final int PROJECT_GET_PROJECT_DETAIL_TAB_DELAY = PROJECT_BASE + 16;
    public static final int PROJECT_GET_PROJECT_DETAIL_TAB_COMMIT = PROJECT_BASE + 17;
    public static final int PROJECT_GET_PROJECT_CKSJ_CHECK = PROJECT_BASE + 18;

    //开工申请
    public static final int PROJECT_GET_START_WORKING_DETAIL = PROJECT_BASE + 19;
    public static final int PROJECT_GET_START_WORKING_CHECK = PROJECT_BASE + 20;

    //资金支付
    public static final int PROJECT_GET_PAY_DETAIL = PROJECT_BASE + 21;
    public static final int PROJECT_GET_PAY_CHECK = PROJECT_BASE + 22;

    //试压试验
    public static final int PROJECT_GET_PROSSURE_DETAIL = PROJECT_BASE + 23;
    public static final int PROJECT_GET_PROSSURE_CHECK = PROJECT_BASE + 24;

    //接水
    public static final int PROJECT_GET_WATER_RECEIVE_DETAIL = PROJECT_BASE + 25;
    public static final int PROJECT_GET_WATER_RECEIVE_CHECK = PROJECT_BASE + 26;

    //竣工验收
    public static final int PROJECT_GET_COMPLETE_DETAIL = PROJECT_BASE + 27;
    public static final int PROJECT_GET_COMPLETE_CHECK = PROJECT_BASE + 28;
    public static final int PROJECT_GET_COMMON_HISTORY = PROJECT_BASE + 29;

    //水表报装统计信息
    public static final int PROJECT_GET_WATERMETER_INFO = PROJECT_BASE + 30;
    public static final int PROJECT_GET_WATERMETER_SUMMARY = PROJECT_BASE + 31;
    public static final int PROJECT_GET_WATERMETER_LIST1 = PROJECT_BASE + 32;
    public static final int PROJECT_GET_WATERMETER_LIST2 = PROJECT_BASE + 33;
    public static final int PROJECT_GET_WATERMETER_LIST3 = PROJECT_BASE + 34;
    public static final int PROJECT_GET_WATERMETER_LIST4 = PROJECT_BASE + 35;
    public static final int PROJECT_GET_WATERMETER_LIST5 = PROJECT_BASE + 36;

    //负责人汇总统计
    public static final int PROJECT_GET_STATISTICS_VIEW = PROJECT_BASE + 37;


    //工程一张图项目信息
    public static final int PROJECT_INFO_FOR_MAP = EVENT_BASE + 1003;

    //安全管理系统
    public static final int PROJECT_SAFE_MANAGE_GET_PROJECT = PROJECT_BASE + 38;
    public static final int PROJECT_SAFE_MANAGE_GET_EVENT_LIST = PROJECT_BASE + 39;
    public static final int PROJECT_SAFE_MANAGE_GET_DETAIL_TAB = PROJECT_BASE + 40;
    public static final int PROJECT_SAFE_MANAGE_GET_DETAIL_INFO1 = PROJECT_BASE + 41;
    public static final int PROJECT_SAFE_MANAGE_GET_DETAIL_INFO2 = PROJECT_BASE + 42;
    public static final int PROJECT_SAFE_MANAGE_GET_DETAIL_INFO3 = PROJECT_BASE + 43;
    public static final int PROJECT_SAFE_CREATE_PAGE_INFO = PROJECT_BASE + 44;
    public static final int PROJECT_SAFE_CREATE_PAGE_SUBMIT_DONE = PROJECT_BASE + 45;
    // 江门工程 end

    //知识库
    public static final int KNOWLEDGE_BASE = EVENT_BASE + 1001;
    public static final int EMERGENCY_GET_KNOWBASE_EMERGENCY_PLAN = KNOWLEDGE_BASE + 1;
    public static final int EMERGENCY_GET_KNOWBASE_EMERGENCY_IDEA = KNOWLEDGE_BASE + 2;
    public static final int EMERGENCY_GET_KNOWBASE_EMERGENCY_COMMON = KNOWLEDGE_BASE + 3;
    public static final int EMERGENCY_GET_REFUGE_INFO = KNOWLEDGE_BASE + 4;
    public static final int EMERGENCY_GET_REFUGE_INFO_1 = KNOWLEDGE_BASE + 5;
    public static final int EMERGENCY_GET_MESSAGE_LIST = KNOWLEDGE_BASE + 6;


    //工单汇总统计
    public static final int WORKORDER_GET_PIE_DATA = EVENT_BASE + 1002;
    public static final int USER_GET_PATROL_TREE = WORKORDER_GET_PIE_DATA + 1002;
    public static final int WORKORDER_GET_PIE_DATA_STATIC = WORKORDER_GET_PIE_DATA + 1003;
    public static final int WORKORDER_GET_BAR_DATA_STATIC = WORKORDER_GET_PIE_DATA + 1004;
    public static final int WORKORDER_GET_BAR_DATA_STATIC_Leader = WORKORDER_GET_PIE_DATA + 1005;
}
