package com.enn.sop.global;

import android.app.Application;
import android.text.TextUtils;

import com.ecity.android.log.LogUtil;
import com.enn.sop.model.user.TaskUnDealCountModel;
import com.enn.sop.model.user.User;
import com.z3app.android.util.StringUtil;

import org.afinal.simplecache.ACache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaobei
 * @date 2017/10/27
 */
public class GlobalFunctionInfo {
    private final static String KEY_CURRENT_USER = "KEY_CURRENT_USER";
    private final static String KEY_APP_TOKEN = "KEY_APP_TOKEN";
    private final static String KEY_APP_STATION_MODEL_LIST = "KEY_APP_STATION_MODEL_LIST";

    private final static String KEY_REQUEST_RETRY_INTERVAL = "request_retry_interval";
    private final static String KEY_REAL_TIME_POSITION_INTERVAL = "real_time_postion_interval";
    private final static String KEY_TRACE_REPORT_INTERVAL = "trace_report_interval";
    private final static String KEY_REQUEST_LOCATION_UPDATE_INTERVAL = "request_location_update_interval";
    private final static String KEY_CHECK_ARRIVE_INTERVAL = "check_arrival_interval";
    private final static String KEY_TASK_ARRIVE_BUFFER = "maintain_task_buffer";
    private final static String GIS_REPORT_EQ_TYPE = "gis_report_eq_types";

    private final static String KEY_APP_EXIT_UNEXPECTED = "KEY_APP_EXIT_UNEXPECTED";

    private final static String KEY_MAP_TPKS = "KEY_MAP_TPKS";
    private final static String KEY_MAP_EVNELOPE = "KEY_MAP_EVNELOPE";
    private final static String KEY_MAP_MAX_RESOLUTION = "KEY_MAP_MAX_RESOLUTION";
    private final static String KEY_MAP_MIN_RESOLUTION = "KEY_MAP_MIN_RESOLUTION";
    private static final String KEY_KEEP_ALIVE_INTERVAL = "KEY_KEEP_ALIVE_INTERVAL";
    private static final String KEY_CUSTOMER_MANAGE_SERVER = "KEY_CUSTOMER_MANAGE_SERVER";

    private static final String TRACE_POINT_INTERVAL_JUDGE_TIME = "TRACE_POINT_INTERVAL_JUDGE_TIME";
    private static final String TASK_UNDEAL_COUNT_DATA = "TASK_UNDEAL_COUNT_DATA";

    private static final String OFFLINE_VERSION_STATUS_UPDATE = "OFFLINE_VERSION_STATUS_UPDATE";

    private static final String KEY_PATROL_LAYER_NAME = "KEY_PATROL_LAYER_NAME";

    private static final String KEY_PREV_TASK_FUNCTION_KEY = "KEY_PREV_TASK_FUNCTION_KEY";
    private static final String KEY_DOUBLE_CHECK_ARRIVAL_INTERVAL = "KEY_DOUBLE_CHECK_ARRIVAL_INTERVAL";
    private static final String KEY_AUTO_UPLOAD_EQ_LOCATION = "KEY_AUTO_UPLOAD_EQ_LOCATION";
    private static final String KEY_FORCE_UPLOAD_EQ_LOCATION = "KEY_FORCE_UPLOAD_EQ_LOCATION";
    private static final String BDLOCATION_INTERVAL = "BDLOCATION_INTERVAL";
    public static final int GIS_REPORT_UI_EVENET = 989;
    private static GlobalFunctionInfo instance;
    private static List<StationInfoModel> stationInfoModelList = new ArrayList<>();
    private Application mApplication;
    private ACache mACache;
    private List<TaskUnDealCountModel> mTaskUnDealCountModels;
    private User currentUser;
    private String stayArea;
    private Map<String, String> patrolLayerMap = new HashMap<>();
    private Map<String, String> functionKeyMap = new HashMap<>();

    private GlobalFunctionInfo() {

    }

    public static GlobalFunctionInfo getInstance() {
        if (instance == null) {
            instance = new GlobalFunctionInfo();
        }
        return instance;
    }

    public String getStayArea() {
        if (StringUtil.isBlank(stayArea)) {
            return "100";
        } else {
            return stayArea;
        }
    }

    public void setStayArea(String stayArea) {
        this.stayArea = stayArea;
    }

    public static String getToken() {
        String token = instance.mACache.getAsString(KEY_APP_TOKEN);
        return token;
    }

    public static void setToken(String token) {
        instance.mACache.put(KEY_APP_TOKEN, token);
    }

    public static User getCurrentUser() {
        if (instance.currentUser == null) {
            instance.currentUser = (User) instance.mACache.getAsObject(KEY_CURRENT_USER);
            if (instance.currentUser == null) {
                LogUtil.i("GlobalFunctionInfo", "currentUser is null from ACache");
            }
        }

        return instance.currentUser;
    }

    public static void setCurrentUser(User user) {
        instance.currentUser = user;
        instance.mACache.put(KEY_CURRENT_USER, user);
    }

    public static void clearCurrentUser() {
        instance.mACache.remove(KEY_CURRENT_USER);
    }

    public static List<StationInfoModel> getStationInfoModelList() {
        return stationInfoModelList;
    }

    public static void setStationInfoModelList(List<StationInfoModel> stationInfoModelList) {
        GlobalFunctionInfo.stationInfoModelList = stationInfoModelList;
    }

    public static String getRealTimePositionInterval() {
        String interval = instance.mACache.getAsString(KEY_REAL_TIME_POSITION_INTERVAL);
        return (interval == null) ? "60" : interval;
    }

    public static void setRealTimePositioInterval(String interval) {
        instance.mACache.put(KEY_REAL_TIME_POSITION_INTERVAL, interval);
    }

    public static String getRequstRetryInterval() {
        String interval = instance.mACache.getAsString(KEY_REQUEST_RETRY_INTERVAL);
        return (interval == null) ? "30" : interval;
    }

    public static void setRequestRetryInterval(String interval) {
        instance.mACache.put(KEY_REQUEST_RETRY_INTERVAL, interval);
    }

    public static String getTraceReportInterval() {
        String interval = instance.mACache.getAsString(KEY_TRACE_REPORT_INTERVAL);
        return (interval == null) ? "300" : interval;
    }

    public static void setTraceReportInterval(String interval) {
        instance.mACache.put(KEY_TRACE_REPORT_INTERVAL, interval);
    }

    public static String getRequestLocationUpdateInterval() {
        String interval = instance.mACache.getAsString(KEY_REQUEST_LOCATION_UPDATE_INTERVAL);
        return (interval == null) ? "5" : interval;
    }

    public static void setRequestLocationUpdateInterval(String interval) {
        instance.mACache.put(KEY_REQUEST_LOCATION_UPDATE_INTERVAL, interval);
    }

    public static String getCheckArriveInterval() {
        String interval = instance.mACache.getAsString(KEY_CHECK_ARRIVE_INTERVAL);
        return (interval == null) ? "10" : interval;
    }

    public static void setCheckArriveInterval(String interval) {
        instance.mACache.put(KEY_CHECK_ARRIVE_INTERVAL, interval);
    }

    public static String getDoubleCheckArrivalInterval() {
        String interval = instance.mACache.getAsString(KEY_DOUBLE_CHECK_ARRIVAL_INTERVAL);
        return (interval == null) ? "60" : interval;
    }

    public static void setDoubleCheckArriveInterval(String interval) {
        instance.mACache.put(KEY_DOUBLE_CHECK_ARRIVAL_INTERVAL, interval);
    }

    public static boolean isAppExitUnexpected() {
        String isExitExcepted = instance.mACache.getAsString(KEY_APP_EXIT_UNEXPECTED);
        if (StringUtil.isBlank(isExitExcepted) || Boolean.valueOf(isExitExcepted)) {
            return false;
        }
        return true;
    }

    public static void setKeyAppExitUnexpected(boolean isExitUnExcepted) {
        instance.mACache.put(KEY_APP_EXIT_UNEXPECTED, String.valueOf(isExitUnExcepted));
    }

    public static String getTaskArriveBuffer() {
        String interval = instance.mACache.getAsString(KEY_TASK_ARRIVE_BUFFER);
        return (interval == null) ? "100" : interval;
    }

    public static void setTaskArriveBuffer(String interval) {
        instance.mACache.put(KEY_TASK_ARRIVE_BUFFER, interval);
    }

    public static String getKeyMapTpks() {
        String interval = instance.mACache.getAsString(KEY_MAP_TPKS);
        return interval;
    }

    public static void setKeyMapTpks(String data) {
        instance.mACache.put(KEY_MAP_TPKS, data);
    }

    public static String getKeyMapEvnelope() {
        String interval = instance.mACache.getAsString(KEY_MAP_EVNELOPE);
        return interval;
    }

    public static void setKeyMapEvnelope(String data) {
        instance.mACache.put(KEY_MAP_EVNELOPE, data);
    }

    public static String getKeyMapMaxResolution() {
        String interval = instance.mACache.getAsString(KEY_MAP_MAX_RESOLUTION);
        if ("".equals(interval)) {
            interval = null;
        }
        return interval;
    }

    public static void setKeyMapMaxResolution(String data) {
        instance.mACache.put(KEY_MAP_MAX_RESOLUTION, data);
    }

    public static String getKeyMapMinResolution() {
        String interval = instance.mACache.getAsString(KEY_MAP_MIN_RESOLUTION);
        if ("".equals(interval)) {
            interval = null;
        }
        return interval;
    }

    public static void setKeyMapMinResolution(String data) {
        instance.mACache.put(KEY_MAP_MIN_RESOLUTION, data);
    }

    public static boolean shouldAutoUploadEqLocation() {
        String value = instance.mACache.getAsString(KEY_AUTO_UPLOAD_EQ_LOCATION);
        return !StringUtil.isBlank(value) && (!"0".equals(value));
    }

    public static void setAutoUploadEqLocation(String value) {
        instance.mACache.put(KEY_AUTO_UPLOAD_EQ_LOCATION, value);
    }

    public static boolean shouldForceUploadEqLocation() {
        String value = instance.mACache.getAsString(KEY_FORCE_UPLOAD_EQ_LOCATION);
        return !StringUtil.isBlank(value) && (!"0".equals(value));
    }

    public static void setForceUploadEqLocation(String value) {
        instance.mACache.put(KEY_FORCE_UPLOAD_EQ_LOCATION, value);
    }

    public static String getKeyKeepAliveInterval() {
        String interval = instance.mACache.getAsString(KEY_KEEP_ALIVE_INTERVAL);
        return (StringUtil.isBlank(interval) || "null".equals(interval)) ? "180" : interval;
    }

    public static void setKeyKeepAliveInterval(String data) {
        instance.mACache.put(KEY_KEEP_ALIVE_INTERVAL, data);
    }

//    gis上报设备类型
    public static void setGisReportEqType(String data){
        instance.mACache.put(GIS_REPORT_EQ_TYPE,data);
    }

    public static String getGisReportEqType(){
        return instance.mACache.getAsString(GIS_REPORT_EQ_TYPE);
    }

    public static String getTracePointIntervalJudgeTime() {
        String interval = instance.mACache.getAsString(TRACE_POINT_INTERVAL_JUDGE_TIME);
        return interval;
    }

    public static void setTracePointIntervalJudgeTime(String data) {
        instance.mACache.put(TRACE_POINT_INTERVAL_JUDGE_TIME, data);
    }


    public static String getKeyCustomerManageServer() {
        String interval = instance.mACache.getAsString(KEY_CUSTOMER_MANAGE_SERVER);
        return interval;
    }

    public static void setKeyCustomerManageServer(String data) {
        instance.mACache.put(KEY_CUSTOMER_MANAGE_SERVER, data);
    }

//    设置百度定位间隔
    public static void setBdLocationInterval(String time){
        instance.mACache.put(BDLOCATION_INTERVAL,time);
    }

    public static String getBdLoctionInterval(){
        return TextUtils.isEmpty(instance.mACache.getAsString(BDLOCATION_INTERVAL)) ? "5" : instance.mACache.getAsString(BDLOCATION_INTERVAL);
    }

    public static void setOfflineVersionStatusUpdate(boolean data) {
        String status = "0";
        if (data) {
            status = "1";
        }
        instance.mACache.put(OFFLINE_VERSION_STATUS_UPDATE, status);
    }

    public static boolean getOfflineVersionStatusUpdate() {
        boolean status = false;
        String interval = instance.mACache.getAsString(OFFLINE_VERSION_STATUS_UPDATE);
        if ("1".equals(interval)) {
            status = true;
        }
        return status;
    }

    public static void clearData() {
        instance.mACache.remove(KEY_REAL_TIME_POSITION_INTERVAL);
        instance.mACache.remove(KEY_REQUEST_RETRY_INTERVAL);
        instance.mACache.remove(KEY_TRACE_REPORT_INTERVAL);
        instance.mACache.remove(KEY_REQUEST_LOCATION_UPDATE_INTERVAL);
        instance.mACache.remove(KEY_CHECK_ARRIVE_INTERVAL);
        instance.mACache.remove(KEY_TASK_ARRIVE_BUFFER);
    }

    private ACache getACache() {
        return mACache = ACache.get(mApplication);
    }

    public Application getApplication() {
        return mApplication;
    }

    public void setApplication(Application application) {
        this.mApplication = application;
        getACache();
    }

    public List<TaskUnDealCountModel> getTaskUnDealCountModels() {
        if (null != mTaskUnDealCountModels) {
            return mTaskUnDealCountModels;
        }
        mTaskUnDealCountModels = (List<TaskUnDealCountModel>) mACache.getAsObject(TASK_UNDEAL_COUNT_DATA);
        if (null == mTaskUnDealCountModels) {
            mTaskUnDealCountModels = new ArrayList<TaskUnDealCountModel>();
        }
        return mTaskUnDealCountModels;
    }

    public void setTaskUnDealCountModels(List<TaskUnDealCountModel> cascadeModel) {
        this.mTaskUnDealCountModels = cascadeModel;
    }

    public Map<String, String> getLayerPatrolNameMap() {
        if (null != patrolLayerMap) {
            return patrolLayerMap;
        }
        patrolLayerMap = (Map<String, String>) mACache.getAsObject(KEY_PATROL_LAYER_NAME);
        if (null == patrolLayerMap) {
            patrolLayerMap = new HashMap<>();
        }
        return patrolLayerMap;
    }

    public void setPatrolLayerMap(Map<String, String> patrolLayerMap) {
        this.patrolLayerMap = patrolLayerMap;
    }

    public Map<String, String> getFunctionKeyMap() {
        if (null != functionKeyMap) {
            return functionKeyMap;
        }
        functionKeyMap = (Map<String, String>) mACache.getAsObject(KEY_PREV_TASK_FUNCTION_KEY);
        if (null == functionKeyMap) {
            functionKeyMap = new HashMap<>();
        }
        return functionKeyMap;
    }

    public void setFunctionKeyMap(Map<String, String> functionKeyMap) {
        this.functionKeyMap = functionKeyMap;
    }

    public void clearTaskUnDealCountModels() {
        if (null != mTaskUnDealCountModels) {
            mTaskUnDealCountModels.clear();
        }
    }
}
