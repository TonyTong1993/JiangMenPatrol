package com.example.zzht.jmpatrol.constants;

import com.example.zzht.jmpatrol.global.HostApplication;

public class Constants {

    public static final String LOG_NAME = "CSWaterSupply";
    public static final String LOG_SUFFIX = ".log";

    public static final int TARGET_SERVER_NORMAL = 0;
    public static final int TARGET_SERVER_CUSTOM = 1;
    // Target Server end

    public static int POINTS_PER_MINUTE = 5;
    public static final int MIN_REPORT_INTERVAL = 20;
    public static int REPORT_INTERVAL = 30;// every 30 seconds report the
                                           // positions.
    public static final int MAX_REPORT_INTERVAL = 300;

    public static final int INPUT_LENGTH = 200;

    // Notification begin
    public static final String KEY_NOTIFICATION = "notify_who";
    public static final String KEY_NOTIFICATION_TO_MYPLAN = "notify_plan";
    // Notification end

    // WorkOrderActivity Notification begin
    public static final String KEY_NOTIFICATION_TO_WORKORDER_RECEIVED = "notify_workOrder_received";
    public static final String KEY_NOTIFICATION_TO_WORKORDER_ACCEPTED = "notify_workOrder_accepted";
    public static final String KEY_NOTIFICATION_TO_WORKORDER_DELAYAPPLY = "notify_workOrder_delayApply";
    public static final String KEY_NOTIFICATION_TO_WORKORDER_REFUSE = "notify_workOrder_refuse";
    public static final String KEY_NOTIFICATION_TO_WORKORDER_ASKFINISH = "notify_workOrder_askfinish";
    // WorkOrderActivity Notification end

    public static final String INSPECT_DELAYAPPLY = "delayApply"; //跟数据库MT_DynamicPage表的pageName字段对应
    public static final String INSPECT_POCESS = "pocess";
    public static final String INSPECT_ASKFINISH = "askFinish";

    public static final String KEY_VALVE_TYPE_NAME = "选取阀门";
    public static final String VALUE_VALVE_TYPE_NAME = "阀";
    
    public static final String KEY_SEARCH_HISTORY_KEYWORD = "key_search_history_keyword";

    //plantask begin
    public static final String INTENT_KEY_FUNCTION = "INTENT_KEY_FUNCTION";
    public static final String INTENT_VALUE_FUNCTION_PLANTASK = "INTENT_VALUE_FUNCTION_PLANTASK";
    public static final String PLANTASL_REFRESH_KEY = "PLANTASL_REFRESH_KEY";
    public static final int TASK_ARRIVAL_DETECTION_INTERVAL = 5 * 1000; // 5 seconds
    public static final int TASK_ARRIVAL_LINE_REPORT_INTERVAL = 60 * 1000; // 60 seconds

    public static final int PLANTASK_RATEFLAG_POINT_LOCATION = 0;
    public static final int PLANTASK_RATEFLAG_POINT_FEEDBACK = 1;
    public static final int PLANTASK_RATEFLAG_LINE = 2;

    public static final int PLANTASK_GETPOINT_ID_ALL = 3;
    public static final int PLANTASK_GETPOINT_ID_ARRIVED = 4;
    public static final int PLANTASK_GETPOINT_ID_FEEDBACK = 5;
    public static final int PLANTASK_GETPOINT_ID_NOT_FEEDBACK = 6;
    public static final int PLANTASK_GETPOINT_ID_UNARRIVED = 7;

    public static final int MAP_OFFLINE_RATEFLAG_LINE = 8;
    //本地缓存任务关联的
    public static final String PLANTASK_PER_KEY = "_PLANTASK_KEY";
    public static final String POINTPART_PER_KEY = "_POINTPART_KEY";
    public static final String LINEPART_PER_KEY = "_LINEPART_KEY";
    public static final String GONPART_PER_KEY = "_GONPART_KEY";
    public static final String PLAN_TASK_ID = "PLAN_TASK_ID";
    //plantask end

    public static final String RELATIVE_DEVICE = "guanlianshebei";

    public static final String WORKORDER_ALL_CATEGORIES = "myfinish,backfinish,transferfinish,myexecute,myassist,teamwaitreceive,teamexecute,mywaitdispath,mywaitreceive,mywaitcheck";
    public static final String WORKORDER_FINISH_CATEGORIES = "myfinish,backfinish,transferfinish";
    public static final String WORKORDER_OPERATOR_CATEGORIES = "myexecute,myassist,teamwaitreceive,teamexecute";
    public static final String WORKORDER_TODO_CATEGORIES = "mywaitdispath,mywaitreceive,mywaitcheck";

    public static final String WORKORDER_WAIT_DEAL = "waitdeal";
    public static final String WORKORDER_DEALING = "dealing";
    public static final String WORKORDER_HAS_FINISHED = "hasfinished";

    public static final String IMAGE_SPLIT_STRING = "IMAGE_SPLIT_STRING";

    //测试用的Json数据
    public static final String jsonObj = "{\"tableName\":\"PATROL_EVENTINFO\",\"params\":[{\"name\":\"basicinformation\",\"alias\":\"基本资料\",\"type\":\"GROUP\",\"required\":0,\"defaultValue\":\"\",\"value\":\"\",\"len\":0,\"increase\":0,\"selectValues\":[],\"childs\":[{\"name\":\"TYPEID\",\"alias\":\"事件类型\",\"type\":\"DDLEXT\",\"required\":0,\"defaultValue\":\"\",\"value\":\"\",\"len\":0,\"increase\":0,\"selectValues\":[{\"1\":\"管段爆裂\"},{\"2\":\"管点维修\"}],\"childs\":[]},{\"name\":\"E_LEV\",\"alias\":\"事件级别\",\"type\":\"DDL\",\"required\":0,\"defaultValue\":\"\",\"value\":\"\",\"len\":0,\"selectValues\":[{\"1\":\"级别一\"},{\"2\":\"级别二\"}],\"increase\":0,\"childs\":[]},{\"name\":\"SENIORITY\",\"alias\":\"工龄\",\"type\":\"RDO\",\"required\":0,\"defaultValue\":\"\",\"value\":\"\",\"len\":0,\"increase\":0,\"selectValues\":[{\"1\":\"一年以下\"},{\"2\":\"一年到两年\"},{\"3\":\"三年到五年\"},{\"4\":\"十年以上\"},{\"5\":\"其它\"}],\"childs\":[]},{\"name\":\"HOBBY\",\"alias\":\"个人爱好\",\"type\":\"CHK\",\"required\":0,\"defaultValue\":\"\",\"value\":\"\",\"len\":0,\"increase\":0,\"selectValues\":[{\"1\":\"读书看报\"},{\"2\":\"游泳\"},{\"3\":\"电子游戏\"},{\"4\":\"篮球\"},{\"5\":\"足球\"}],\"childs\":[]},{\"name\":\"E_DESC\",\"alias\":\"事件描述\",\"type\":\"TXT\",\"required\":0,\"defaultValue\":\"\",\"value\":\"\",\"len\":0,\"increase\":0,\"selectValues\":[],\"childs\":[]},{\"name\":\"E_ADDR\",\"alias\":\"事件地址\",\"type\":\"TXT\",\"required\":0,\"defaultValue\":\"\",\"value\":\"\",\"len\":0,\"increase\":0,\"selectValues\":[],\"childs\":[]},{\"name\":\"REPORTERID\",\"alias\":\"上报人\",\"type\":\"TXT\",\"required\":0,\"defaultValue\":\"\",\"value\":\"\",\"len\":0,\"increase\":0,\"selectValues\":[],\"childs\":[]},{\"name\":\"REPORTER\",\"alias\":\"上报人姓名\",\"type\":\"TXT\",\"required\":0,\"defaultValue\":\"\",\"value\":\"\",\"len\":0,\"increase\":0,\"selectValues\":[],\"childs\":[]},{\"name\":\"EMERGENCY\",\"alias\":\"是否紧急事件\",\"type\":\"DDL\",\"required\":0,\"defaultValue\":\"\",\"value\":\"\",\"len\":0,\"increase\":0,\"selectValues\":[{\"0\":\"否\"},{\"1\":\"是\"}],\"childs\":[]},{\"name\":\"GEOM\",\"alias\":\"空间位置\",\"type\":\"GEOM\",\"required\":0,\"defaultValue\":\"\",\"value\":\"\",\"len\":0,\"increase\":0,\"selectValues\":[],\"childs\":[]},{\"name\":\"MEDIA\",\"alias\":\"照片\",\"type\":\"IMG\",\"required\":0,\"defaultValue\":\"\",\"value\":\"\",\"len\":0,\"increase\":0,\"selectValues\":[],\"childs\":[]}]},{\"name\":\"materialrequirements\",\"alias\":\"物料需求\",\"type\":\"GROUP\",\"required\":0,\"defaultValue\":\"\",\"value\":\"\",\"len\":0,\"increase\":1,\"selectValues\":[],\"childs\":[{\"name\":\"material\",\"alias\":\"物料\",\"type\":\"DDL\",\"required\":0,\"defaultValue\":\"\",\"value\":\"\",\"len\":0,\"increase\":0,\"selectValues\":[{\"0\":\"钢管\"},{\"1\":\"塑料管\"}],\"childs\":[]},{\"name\":\"amount\",\"alias\":\"数量\",\"type\":\"TXT\",\"required\":0,\"defaultValue\":\"1\",\"value\":\"\",\"len\":0,\"increase\":0,\"selectValues\":[],\"childs\":[]},{\"name\":\"description\",\"alias\":\"描述\",\"type\":\"TXT\",\"required\":0,\"defaultValue\":\"\",\"value\":\"\",\"len\":0,\"increase\":0,\"selectValues\":[],\"childs\":[]}]},{\"name\":\"staffovertime\",\"alias\":\"加班信息\",\"type\":\"GROUP\",\"required\":0,\"defaultValue\":\"\",\"value\":\"\",\"len\":0,\"increase\":1,\"selectValues\":[],\"childs\":[{\"name\":\"username\",\"alias\":\"人员\",\"type\":\"DDL\",\"required\":0,\"defaultValue\":\"\",\"value\":\"\",\"len\":0,\"increase\":0,\"selectValues\":[{\"0\":\"张三\"},{\"1\":\"李四\"}],\"childs\":[]},{\"name\":\"stafftime\",\"alias\":\"加班时长\",\"type\":\"TXT\",\"required\":0,\"defaultValue\":\"0\",\"value\":\"\",\"len\":0,\"increase\":0,\"selectValues\":[],\"childs\":[]}]}],\"success\":true}";

    //emergency start
    public static final double EARTH_RADIUS = 6378137.0;

    public static final String EARTH_QUAKE_BASE_RIGHTBTN = "earth_quake_base_rightbtn";
    public static final String EARTH_QUAKE_LIST_CLICK = "earth_quake_list_click";

    //查询返回标识
    public static final int EARTH_QUAKE_REPORT_RESULT = 1;
    public static final int EARTH_QUAKE_EARTHQUAKE_INFO_RESULT = 2;
    public static final String EARTH_QUAKE_INFO_SEARCH_TYPE = "earth_quake_info_search_type";
    public static final String EARTH_QUAKE_INFO_PARAM = "earth_quake_info_param";
    public static final String EARTH_QUAKE_QUICK_REPORT_INFO_PARAM = "earth_quake_quick_report_info_param";
    public static final String EARTH_QUAKE_QUICK_REPORT_LONGITUDE = "earth_quake_quick_report_longitude";
    public static final String EARTH_QUAKE_QUICK_REPORT_LATITUDE = "earth_quake_quick_report_latitude";
    public static final String FORM_MAP_GET_CURRENT_POSITION = "FORM_MAP_GET_CURRENT_POSITION";
    public final static String REQUEST_CODE_FLAG = "earth_quake_request_code_flag";
    
    //工单过滤
    public static String WAIT_PROCESS = "mywaitdispath,mywaitreceive,mywaitcheck";
    public static String EXCUTE_PEOCESS = "myexecute,myassist,teamwaitreceive,teamexecute";
    public static String FINISH_PROCESS = "myfinish,backfinish,transferfinish";
    public static String WAIT_PROCESS_ORDER = "order by assign_time desc";
    public static String EXCUTE_PEOCESS_ORDER ="order by accept_time desc";
    public static String FINISH_PROCESS_ORDER = "order by reportfinish_deal_time desc";
    
    public static final String WORK_ORDER_GROUP_TILE = "WORK_ORDER_GROUP_TILE";
    public static final String WORK_ORDER_GROUP_POSITION = "WORK_ORDER_GROUP_POSITION";
    public static final String WORK_ORDER_GROUP_ORDER_BY = "WORK_ORDER_GROUP_ORDER_BY";
    
    // Common Intent Key begin
    public static final String INTENT_KEY_NEXT_STEP_ID = "INTENT_KEY_NEXT_STEP_ID";
    public static final String INTENT_KEY_GOUP_NEXT_STEP_ID = "INTENT_KEY_GOUP_NEXT_STEP_ID";//区分从大类传的还是小类传的
    public static final String INTENT_KEY_NEXT_CATEGORY_NAME = "INTENT_KEY_NEXT_CATEGORY_NAME";
    public static final String WORKORDER_SUMMARY_DETAIL = "WORKORDER_SUMMARY_DETAIL";
    //网页菜单
    public static final String MENUNAME = "menuName";
    public static final String URL = "url";
    public static final String TITLETYPE = "titleType";
    public static final String TYPE_NO_TITLE = "type_no_title";
    public static final String TYPE_MENU_TITLE = "type_menu_title";
    public static final String TYPE_WEB_TITLE = "type_web_title";

    public static final String LAST_RUNNING_CODE = "com.zzz.app.lastrunningcode";
}
