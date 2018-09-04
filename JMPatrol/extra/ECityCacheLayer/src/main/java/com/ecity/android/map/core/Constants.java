package com.ecity.android.map.core;

public class Constants {

	public static final String LOG_NAME   = "MapDbApplication";
	public static final String LOG_SUFFIX = ".log";

	public static final int MAX_DIR_SIZE_MB         = 100;// 文件路径所占最大空间
	public static final int MAX_FILE_EXSIST_DAY     = 20;// 单个文件存在的最长时间（天）
	public static final int MAX_SINGLE_FILE_SIZE_MB = 2 * MAX_DIR_SIZE_MB / MAX_FILE_EXSIST_DAY;// 单个文件最大空间

	public static final String PRJ_DIR_NAME = "mapapp";

	public static final int QUERY_DB_FINISH = 3519;


}
