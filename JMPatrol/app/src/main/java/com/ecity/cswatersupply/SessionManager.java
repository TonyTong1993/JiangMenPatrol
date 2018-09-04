package com.ecity.cswatersupply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.location.Location;

import com.ecity.cswatersupply.contact.model.ContactGroup;
import com.ecity.cswatersupply.emergency.model.EarthQuakeInfoModel;
import com.ecity.cswatersupply.model.AddressInfoModel;
import com.ecity.cswatersupply.model.checkitem.Content;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTask;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskLinePart;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPointPart;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPolygonPart;
import com.ecity.cswatersupply.workorder.model.WorkOrderMTField;
import com.esri.core.geometry.Envelope;
import com.z3pipe.mobile.android.corssdk.model.EQulityType;
import com.z3pipe.mobile.android.corssdk.model.SatelliteInfo;

public class SessionManager {

    public static Z3PlanTask CurrentPlanTask = null;
    public static Z3PlanTask currentTaskIntMapOpretor = null;
    public static Z3PlanTaskPointPart currentPointPartIntMapOpretor = null;
    //任务
    public static List<Z3PlanTask> CurrentPlanTasks = null;
    //任务
    public static HashMap<String, Z3PlanTask> allPlanTasks = null;
    //点任务
    public static HashMap<String, Z3PlanTaskPointPart> pointParts = null;
    //线任务
    public static HashMap<String, Z3PlanTaskLinePart> lineParts = null;
    //区任务
    public static HashMap<String, Z3PlanTaskPolygonPart> polygonPart = null;
    //各个任务 线 点 区的标识
    public static HashMap<String, String> planTaskKeys =null;
    // 用户数据库名
    public static String CurrentUserDatabaseName = null;
    // 上次更新的Location
    public static Location currentLocation = null;
    // 上一次上报成功的时间
    public static String LastTimeString;
    public static boolean isFirstLocate = true;
    public static int reportSuccessCount = 0;
    // 记录地图上一次的显示范围
    public static Envelope lastInitExtent = null;
    //工单元数据是否解析
    public static boolean workOrderMetasHaveParsed;
    public static Map<String, WorkOrderMTField> workOrderMetas;
    //检查项
    public static List<Content> contentsList = new ArrayList<Content>();
    public static Content content = null;
    //上报的检查项子项
    public static List<InspectItem> currentInspectItems = null;
    //当前任务类型
    public static int isContent = 0;
    //上传图片地址类型选择（默认为检查项上报地址）
    public static int reportType = 1;
    //保存上次地址搜索的文本
    public static String lastSearchText = null;
    //保存上次地址搜索的结果
    public static Map<String, List<AddressInfoModel>> lastSearchResult = new HashMap<String, List<AddressInfoModel>>();
    //当前要进行检查项上报的点
    public static String currentPointId = null;
    //当前任务是否只是点任务
    public static HashMap<String, Boolean> isPointTask = new HashMap<String, Boolean>();
    //当前的处理工单处理流ID
    public static String CurrentProcessinstanceID;
    //存储所有的地震信息
    public static List<EarthQuakeInfoModel> quakeInfoList = new ArrayList<EarthQuakeInfoModel>();
    //上次请求地震信息时间
    public static long lastUpdateTime = 0;
    //patrol layerName
    public static String GISReportTableName;
    //patrol layerName
    public static int GISReportLayerID;
    //清楚地图缓存后，需要重新加载地图标志
    public static boolean isActivityMapNeedReload = false;
    public static boolean isFragmentMapNeedReload = false;

    //由于武汉地震检查项的可读可写模式都是读的缓存，为了减少检查项代码的改动，特加一标识，用来显示与否
    public static boolean isImageNotEdit = false;

    //存储福州cors连接的相关信息
    public static SatelliteInfo satelliteInfo ;

    public static String attachmentUrl = "";
    public static String whdzdistrict = "";
}

