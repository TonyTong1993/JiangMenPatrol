package com.enn.sop.global;

/**
 * @author xiaobei
 * @date 2017/11/21
 */
public class GlobalKey {
    public static class Path {
        public static final String MAP_ACTIVITY = "/map/mapActivity";
        public static final String HOME_ACTIVITY = "/home/activity/HomeActivity";
        public static final String REPORT_EVENT_TYPE_SELECT_ACTIVITY = "/event/reportEventTypeSelectActivity";
        public static final String SOCKET_WAIT_CONNECTION_ACTIVITY = "/leak/detect/SocketWaitConnectionActivity";
    }

    public static class IntentKey {
        public static final String MAP_TITLE = "MAP_TITLE";
        public static final String IS_BACK_TO_MSG = "IS_BACK_TO_MSG";
        public static final String IS_BACK_TO_HOME = "IS_BACK_TO_HOME";
        public static final String IS_OFFLINE_MAP_DOWNLOADED = "IS_OFFLINE_MAP_DOWNLOADED";
        public static final String EVENT_KEY_DEVICE = "device";
        public static final String MAP_OPERATOR_NAME = "MAP_OPERATOR_NAME";
        public static final String INTENT_KEY_NOTIFICATION = "INTENT_KEY_NOTIFICATION";
        public static final String TASK_ID = "TASK_ID";
    }
}
