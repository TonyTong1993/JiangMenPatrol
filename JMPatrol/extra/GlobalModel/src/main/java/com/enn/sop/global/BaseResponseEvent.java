package com.enn.sop.global;

/**
 * @author xiaobei
 * @date 2017/11/6
 */
public class BaseResponseEvent {
    private static final int EVENT_BASE = 9000;
    // file operation begin
    public static final int FILE_OPERATION_BASE = EVENT_BASE + 30;
    public static final int FILE_OPERATION_DOWNLOAD_FINISH = FILE_OPERATION_BASE + 1;
    public static final int FILE_OPERATION_UPDATE_PROGRESS = FILE_OPERATION_BASE + 2;
}
