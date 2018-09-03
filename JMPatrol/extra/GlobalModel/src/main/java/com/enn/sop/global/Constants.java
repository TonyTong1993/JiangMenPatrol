package com.enn.sop.global;

/**
 * @author xiaobei
 * @date 2017/10/29
 */
public class Constants {
    public static final String FORM_MAP_GET_CURRENT_POSITION = "FORM_MAP_GET_CURRENT_POSITION";
    /**
     * 上一次上报成功的时间
     */
    public static String LastTimeString;
    public static int reportSuccessCount = 0;
    /**
     * 清楚地图缓存后，需要重新加载地图标志
     */
    public static boolean isActivityMapNeedReload = false;
    public static boolean isFragmentMapNeedReload = false;

    public static final String IMAGE_SPLIT_STRING = "IMAGE_SPLIT_STRING";

    //上传图片地址类型选择（默认为检查项上报地址）
    public static int reportType = 1;

    public static final int PLANTASK_GETPOINT_ID_ALL = 3;

    public static final int TARGET_SERVER_NORMAL = 0;
    public static final int TARGET_SERVER_CUSTOM = 1;
}
