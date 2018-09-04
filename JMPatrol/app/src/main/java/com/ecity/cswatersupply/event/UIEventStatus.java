package com.ecity.cswatersupply.event;

import com.ecity.android.eventcore.ReservedEvent;

public class UIEventStatus {
    public static final int OK = 1;
    public static final int ERROR = 0;

    public static final int EVENT_BASE = 10000000;

    // Common begin
    private static final int COMMON_BASE = EVENT_BASE + 1000;
    public static final int TOAST = ReservedEvent.UI.TOAST;
    public static final int INSPECT_ITEM_VALUE_CHANGED = COMMON_BASE + 1;
    // Common end

    // LoginSetting begin
    private static final int LOGINSETTING_BASE = EVENT_BASE + 2000;
    public static final int LOGINSETTING_BACK = LOGINSETTING_BASE + 1;
    public static final int LOGINSETTING_SAVE = LOGINSETTING_BASE + 2;

    private static final int WORKORDER_BASE = EVENT_BASE + 3000;
    public static final int WORKORDER_STARTRETURNSERVICE = WORKORDER_BASE + 1;
    public static final int WORKORDER_RETURN = WORKORDER_BASE + 2;
    public static final int WORKORDER_RETURNDIALOG_OK = WORKORDER_BASE + 11;
    public static final int WORKORDER_ACCEPT = WORKORDER_BASE + 3;
    public static final int WORKORDER_DELAYAPPLY = WORKORDER_BASE + 4;
    public static final int WORKORDER_POCESS = WORKORDER_BASE + 5;
    public static final int ON_LOAD_DATA_SUCCESS = WORKORDER_BASE + 6;
    public static final int ON_LOAD_DATA_FINISH = WORKORDER_BASE + 7;
    public static final int ON_LOAD_DATA_ERROR = WORKORDER_BASE + 8;
    public static final int WORKORDER_LOADING = WORKORDER_BASE + 9;
    public static final int WORKORDER_ASKFINISH = WORKORDER_BASE + 10;
    public static final int WORKORDER_RETURN_CHOOSED_CONTACT_MEN = WORKORDER_BASE + 12;
    public static final int WORKORDER_DISPATCH = WORKORDER_BASE + 13;
    public static final int WORKORDER_EXPLORE = WORKORDER_BASE + 14;
    public static final int WORKORDER_SIGNIN = WORKORDER_BASE + 15;
    public static final int WORKORDER_ASK_FOR_HELP = WORKORDER_BASE + 16;
    public static final int WORKORDER_TRANSFER = WORKORDER_BASE + 17;
    public static final int WORKORDER_AUDIT_DELAY = WORKORDER_BASE + 18;
    public static final int WORKORDER_AUDIT_ASK4HELP = WORKORDER_BASE + 19;
    public static final int WORKORDER_HANDLE_TRANSFER = WORKORDER_BASE + 20;
    public static final int WORKORDER_AUDIT_RETURN = WORKORDER_BASE + 21;
    public static final int WORKORDER_COMMON_INSPECT_REPORT = WORKORDER_BASE + 22;//工单检查项通用上报ID
    public static final int WORKORDER_SEARCH_RESULT_CLICKED = WORKORDER_BASE + 23;
    public static final int WORKORDER_SEARCH_RESULT_CLICKED_MAIN_MENU = WORKORDER_BASE + 27;
    public static final int WORKORDER_OPERATE_BACK = WORKORDER_BASE + 28;
    public static final int WORKORDER_OPERATE_REPORT = WORKORDER_BASE + 29;
    //历史回溯
    public static final int WORKORDER_LOG_DATE_BACK_NOTIFICATION = WORKORDER_BASE + 24;
    public static final int WORKORDER_DETAIL_REFRESH_FRAGMENT = WORKORDER_BASE + 25;
    public static final int WORKORDER_DETAIL_REFRESH_FLOW_INFO_FRAGMENT = WORKORDER_BASE + 26;

    public static final int PLANNINGTASK_BASE = EVENT_BASE + 4000;
    public static final int PLANNINGTASK_POINT_PART_NOTIFICATION = PLANNINGTASK_BASE + 2;
    public static final int PLANNINGTASK_TRACKLINE_NOTIFICATION = PLANNINGTASK_BASE + 3;
    public static final int PLANNINGTASK_POINT_FEEDBACK_NOTIFICATION = PLANNINGTASK_BASE + 4;
    public static final int PLANNINGTASK_GET_PLANTASK = PLANNINGTASK_BASE + 5;
    public static final int PLANNINGTASK_ADAPTER_TASK = PLANNINGTASK_BASE + 6;
    public static final int PLANNINGTASK_ADAPTER_TASK_MAIN = PLANNINGTASK_BASE + 7;
    public static final int PLANNINGTASK_GET_CONTENT_MAIN = PLANNINGTASK_BASE + 8;
    public static final int PLANNINGTASK_LINE_RATE_NOTIFICATION = PLANNINGTASK_BASE + 9;
    public static final int PLANNINGTASK_REFLASH_XJ_POINT_STATUS = PLANNINGTASK_BASE + 10;
    public static final int PLANNINGTASK_REFLASH_YH_POINT_STATUS = PLANNINGTASK_BASE + 11;
    public static final int PLANNINGTASK_ALLSTATESPOINTPART_STATUS = PLANNINGTASK_BASE + 12;
    public static final int PLANNINGTASK_LINE_FEEDBACK_NOTIFICATION = PLANNINGTASK_BASE + 13;
    public static final int PLANNINGTASK_NO_DATA = PLANNINGTASK_BASE + 14;

    private static final int PUNISHLIST_BASE = EVENT_BASE + 5000;
    public static final int PUNISHSTATE_NOTIFICATION = PUNISHLIST_BASE + 1;
    public static final int PUNISHSTATE_STATUS_REPORTING = PUNISHLIST_BASE + 2;
    public static final int PUNISHSTATE_STATUS_PRINTING = PUNISHLIST_BASE + 3;
    public static final int PUNISH_EVENT_CLOSE = PUNISHLIST_BASE + 5;
    public static final int EVENT_ADDRESS_ON_CLICKED = PUNISHLIST_BASE + 6;
    public static final int EVENT_REPORT_FINISHED = PUNISHLIST_BASE + 7;

    // Notification begin
    private static final int NOTIFICATION_BASE = EVENT_BASE + 6000;
    public static final int NOTIFICATION_VIEWED = NOTIFICATION_BASE + 1;
    public static final int NOTIFICATION_SIGN_OUT = NOTIFICATION_BASE + 2;
    private static final int MAP_OFFLINE_BASE = EVENT_BASE + 7000;
    public static final int MAP_OFFLINE_DOWLOAD_BUTTOM_CLICKED = MAP_OFFLINE_BASE + 1;
    public static final int TODO_LIST_SELECT_BASE = EVENT_BASE + 8000;
    public static final int TODO_LIST_SELECT_ALL = TODO_LIST_SELECT_BASE + 1;
    public static final int TODO_LIST_SELECT_NOT_ALL = TODO_LIST_SELECT_BASE + 2;

    public static final int NOTIFICATION_EARTHQUAKE = EVENT_BASE + 120;
    public static final int NOTIFICATION_ANNOUNCEMENT = EVENT_BASE + 110;
    public static final int NOTIFICATION_EXPERTOPINION = EVENT_BASE + 114;

    public static final int XGPUSH_CLICK_VIEW_MAP = EVENT_BASE + 115;

    //江门工程
    public static final int PROJECT_SELECT_PEOPLE_DISPLAY = 7000;

    //地震
    public static final int EMERGENCY_INFO = 8000;
    public static final int EMERGENCY_DOWN_DONE = EMERGENCY_INFO + 1;
    public static final int EMERGENCY_CONTACT_GET_CONTACTS = EMERGENCY_INFO + 2;
    public static final int EMERGENCY_CONTACT_PUSH_STACK = EMERGENCY_INFO + 3;
    public static final int EMERGENCY_CONTACT_POP_STACK = EMERGENCY_INFO + 4;
    public static final int EMERGENCY_REPORT_QUERY_SEARCH = EMERGENCY_INFO + 5;
    public static final int EMERGENCY_RELATE_FINISH = EMERGENCY_INFO + 6;
    public static final int EMERGENCY_ANNONOUN_SEARCH = EMERGENCY_INFO + 7;


    //--------------------------------------------------------------------------
    //南昌外勤
    public static final int GET_FORM_INSPECTITEM = EVENT_BASE + 115;
    public static final int REFRESH_CASCAED_INSPECTITEM = EVENT_BASE + 116;


}
