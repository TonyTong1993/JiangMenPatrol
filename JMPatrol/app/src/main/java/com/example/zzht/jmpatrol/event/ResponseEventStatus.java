package com.example.zzht.jmpatrol.event;

public class ResponseEventStatus {
    public static final int OK = 1;
    public static final int ERROR = 0;

    private static final int EVENT_BASE = 10000000;
    public static final int TestConnectionEvent = EVENT_BASE + 1;
    public static final int LoginEvent = EVENT_BASE + 2;
    public static final int GET_MAPCONFIG_EVENT = EVENT_BASE + 3;
    public static final int WatchEvent = EVENT_BASE + 5;
    public static final int WatchState = EVENT_BASE + 6;
    public static final int UPLOAD_SINGLE_FILE = EVENT_BASE + 7;
    public static final int UPLOAD_ALL_FILES = EVENT_BASE + 8;
    public static final int UPDATE_PATROL_MAN_STATE = EVENT_BASE + 9;

    //值班签到签退
    public static final int SIGN_IN_EVENT = EVENT_BASE + 100;
    public static final int SIGN_OUT_EVENT = SIGN_IN_EVENT + 1;
    public static final int GET_ALL_PATROL_MAN = SIGN_IN_EVENT + 2;
    //获取巡检员轨迹
    public static final int GET_PATROL_MAN_TRACK = SIGN_IN_EVENT + 3;
    public static final int MAIN_TAB_GET_CONTACT = SIGN_IN_EVENT + 4;

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
    public static final int WORKORDER_DOWN_WORKORDER_BY_WORKORDERID = WORKORDER_BASE + 35;
    public static final int WORKORDER_DOWN_WORKORDER_BY_PROCESSINSTANCEID = WORKORDER_BASE + 36;

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
    public static final int WORKORDER_GET_DETAIL_TIME = WORKORDER_BASE + 45;
    public static final int WORKORDER_GET_URGE_INFO = WORKORDER_BASE + 46;
    public static final int WORKORDER_SEND_URGE_MSG_INFO = WORKORDER_BASE + 47;
    public static final int WORKORDER_GET_USER_FUNCS = WORKORDER_BASE + 48;


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
    public static final int PLANNING_LINE_ARRIVED = PLANNINGTASK_BASE +7;
    public static final int MAP_GET_PLAN_AND_PIPE_ADDRESS = PLANNINGTASK_BASE + 8;
    public static final int PLANNING_OUT_OF_RANGE = PLANNINGTASK_BASE + 9;
    public static final int PLANNING_DUTY_SIGN_IN = PLANNINGTASK_BASE + 10;
    public static final int PLANNING_DUTY_SIGN_OUT = PLANNINGTASK_BASE + 11;
    public static final int PLANNING_GET_WORK_TIME = PLANNINGTASK_BASE + 12;
    public static final int PLANNING_TASK_TYPE = PLANNINGTASK_BASE + 13;
    public static final int PLANNING_TASK_GET_ALL_FEEDBACK_FORMS = PLANNINGTASK_BASE + 14;

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
    public static final int EVENT_GET_ALL_REGIONS = EVENT_REPORT_BASE + 10;
    public static final int EVENT_GET_ALL_REGIONS_FOR_REPORT_COUT_CIRCLE = EVENT_REPORT_BASE + 11;
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
    public static final int EMERGENCY_EARTHQUAKE_INFO_SEARCH_PARAMS = EMERGENCY_BASE + 9;
    public static final int EMERGENCY_EARTHQUAKE_XCDC_REPORT = EMERGENCY_BASE + 10;
    public static final int EMERGENCY_GET_UNUSUAL_REPORT_FORM = EMERGENCY_BASE + 11;
    public static final int EMERGENCY_GET_INVESGATION_INFO = EMERGENCY_BASE + 12;
    public static final int EMERGENCY_GET_DISTRION_QUAKE_INFO = EMERGENCY_BASE + 13;

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

    // 江门工程 end

    //知识库
    public static final int KNOWLEDGE_BASE = EVENT_BASE + 1001;
    public static final int EMERGENCY_GET_KNOWBASE_EMERGENCY_PLAN = KNOWLEDGE_BASE + 1;

    //工单汇总统计
    public static final int WORKORDER_GET_PIE_DATA = EVENT_BASE + 1002;
    public static final int USER_GET_PATROL_TREE = WORKORDER_GET_PIE_DATA + 1002;
    public static final int WORKORDER_GET_PIE_DATA_STATIC = WORKORDER_GET_PIE_DATA + 1003;
    public static final int WORKORDER_GET_BAR_DATA_STATIC = WORKORDER_GET_PIE_DATA + 1004;
    public static final int WORKORDER_GET_BAR_DATA_STATIC_Leader = WORKORDER_GET_PIE_DATA + 1005;

    public static final int GET_EQUIP_RECORD = EVENT_BASE + 2000;
    public static final int GET_ASSET_INFO = GET_EQUIP_RECORD + 1;
    public static final int GET_EQUIP_RECORD_DETAIL_INFO = GET_EQUIP_RECORD + 2;
    public static final int GET_ASSET_MODIFY_RECORD = GET_EQUIP_RECORD + 3;
    public static final int GET_ASSET_INFO_BY_EQUIP_ID = GET_EQUIP_RECORD + 4;
    public static final int GET_ASSET_LAST_FEED_INFO = GET_EQUIP_RECORD + 5;

    // 排水管理相关事件 begin
    public static final int DRAIN_BASE = EVENT_BASE + 15000;
    public static final int DRAIN_GET_FLEFLOW_RECORDS = DRAIN_BASE + 1;
    public static final int DRAIN_GET_FLEFLOW_RECORDS_FEEDBACK_DETAIL = DRAIN_BASE + 2;
    public static final int DRAIN_GET_DAILY_FORM = DRAIN_BASE + 3;
    public static final int DRAIN_GET_DAILY_PLAN_FORM = DRAIN_BASE + 4;
    public static final int DRAIN_GET_FEEDBACK_FORM = DRAIN_BASE + 5;
    public static final int GET_PLAN_APPLY_FROM = DRAIN_BASE + 6;
    public static final int GET_PLAN_AUDIT_FROM = DRAIN_BASE + 7;
    public static final int GET_PLANS_INFO = DRAIN_BASE + 8;
    public static final int GET_PLANS_LOG_BACK_INFO = DRAIN_BASE + 9;
    public static final int GET_PLAN_MODIFY_FROM = DRAIN_BASE + 10;
    public static final int GET_PLAN_DETAIL_INFO = DRAIN_BASE + 11;
    public static final int GET_PLAN_MODIFY_RECORD = DRAIN_BASE + 12;
    public static final int DRAIN_GET_FLEFLOW_RECORDS_FOR_DAILY = DRAIN_BASE + 13;
    public static final int DRAIN_GET_FLEFLOW_RECORDS_IN_FRAGMENT = DRAIN_BASE + 14;
    public static final int DRAIN_GET_PLAN_STATES = DRAIN_BASE + 15;
    public static final int DRAIN_GET_PLAN_EXCEL = DRAIN_BASE + 16;
    public static final int DRAIN_GET_PLAN_EXCEL_IN_FRAGMENT = DRAIN_BASE + 17;
    public static final int DRAIN_GET_FLEFLOW_RECORDS_IN_FRAGMENT_ADPTER = DRAIN_BASE + 18;
    public static final int GET_DAILY_PLAN_CHANGE_RECORD = DRAIN_BASE + 19;
    public static final int GET_DAILY_PLAN_INFO_IN_AUDITING = DRAIN_BASE + 20;
    public static final int GET_DAILY_PLAN_INFO_AUDITED = DRAIN_BASE + 21;

    public static final int GET_MONTH_PLAN_INFO_IN_AUDITING = DRAIN_BASE + 22;
    public static final int GET_MONTH_PLAN_INFO_ALREADY_AUDITED = DRAIN_BASE + 23;
    public static final int GET_MONTH_PLAN_INFO_IN_DOING = DRAIN_BASE + 24;
    public static final int GET_MONTH_PLAN_INFO_ALREADY_DONE = DRAIN_BASE + 25;

    public static final int DRAIN_GET_IN_AUDITING_PLAN_STATES = DRAIN_BASE + 26;
    public static final int DRAIN_GET_ALREADY_AUDITED_PLAN_STATES = DRAIN_BASE + 27;
    public static final int DRAIN_GET_IN_DOING_PLAN_STATES = DRAIN_BASE + 28;
    public static final int DRAIN_GET_ALREADY_DONE_PLAN_STATES = DRAIN_BASE + 29;

    public static final int DRAIN_POST_PUMP_IN_DATA = DRAIN_BASE + 30;
    public static final int DRAIN_POST_PUMP_OUT_DATA = DRAIN_BASE + 31;
    public static final int DRAIN_GET_ATTENDANCE_STATE = DRAIN_BASE + 32;
    public static final int DRAIN_GET_ATTENDANCE_RECORD_FOR_CURRENT_USER = DRAIN_BASE + 33;
    public static final int GET_ATTENDANCE_SIGN_IN_FROM = DRAIN_BASE + 34;
    public static final int GET_ATTENDANCE_SIGN_OUT_FROM = DRAIN_BASE + 35;
    public static final int DRAIN_GET_ATTENDANCE_RECORD_FOR_ALL = DRAIN_BASE + 36;
    public static final int DRAIN_GET_ATTENDANCE_RECORD_FOR_OUT_CIRCLE_REPORT = DRAIN_BASE + 37;
    public static final int DRAIN_REPORT_OUT_CIRCLE_DATA = DRAIN_BASE + 38;
    public static final int GET_PLANS_INFO_FOR_OUT_CIRCLE = DRAIN_BASE + 39;
    public static final int DRAIN_GET_IN_DOING_PLAN_STATES_FOR_REPORT_OUT_CIRCLE = DRAIN_BASE + 40;
    public static final int DRAIN_REPORT_GET_IN_CIRCLE_DATA = DRAIN_BASE + 41;
    public static final int DRAIN_GET_OUT_OF_CIRCLE_RECORD = DRAIN_BASE + 42;
}
