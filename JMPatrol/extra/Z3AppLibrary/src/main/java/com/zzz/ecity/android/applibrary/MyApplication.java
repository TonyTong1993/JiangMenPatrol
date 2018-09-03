package com.zzz.ecity.android.applibrary;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ecity.android.db.Database;
import com.ecity.android.db.DatabasePool;
import com.ecity.android.db.exception.Z3DBException;
import com.ecity.android.db.model.DatabaseInfo;

import com.z3app.android.app.Z3Application;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.database.GPSPositionDao;
import com.zzz.ecity.android.applibrary.database.TablesBuilder;
import com.zzz.ecity.android.applibrary.utils.FileUtil;

/** 
 * @Title Z3Application.java
 * @Package com.zzz.ecity.android.applibrary 
 * Description:<br/>
 * @version V1.0 
 * @Author ZiZhengzhuan
 * @CreateDate 2016年1月13日
 * @email 
 * @copyright ECity 2015
 */

public class MyApplication extends Z3Application {
	// 数据库
	private Map<String,DatabaseInfo> dbinfoMap;
	private static MyApplication application;
	@Override
	public void onCreate() {
		try {
			super.onCreate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		application = this;
		// 本地数据库
		initDatabase();
		customDatabaseInit();
	}
	
	public static MyApplication getApplication(){
		return application;
	}
	  /***
   * 初始化数据库
   * 
   * @return
   */
  private void initDatabase() {
      dbinfoMap = new HashMap<String, DatabaseInfo>();
      TablesBuilder.analyzeTablesXML(getResources());
      try {
          getDatabase(GPSPositionDao.TB_NAME)
                  .createTable(
                          TablesBuilder
                                  .getTableByName(GPSPositionDao.TB_NAME));
      } catch (Z3DBException e) {
          e.printStackTrace();
      }
  }
  /***
   * 可以重写
   */
  public void customDatabaseInit(){
	  
  }
  
  /***
   * 关闭所有数据库
   */
  public final synchronized void closeAllDatabase(){
      if(null != dbinfoMap){
          for(Entry<String, DatabaseInfo> entry: dbinfoMap.entrySet()){
              try {
                  getDatabase(entry.getValue().getDbName()).closeDatabase();
              } catch (Exception e) {
                  e.printStackTrace();
              }
          }
      }
  }
  
  /**
   * 获取本地数据库
   * @param dbName 数据库的名字 可以不带后缀.db  如果不带会自动补全
   * @return
   */
  public final Database getDatabase(String dbName) {
      Database database = null;
      try {
          DatabasePool databasePool = DatabasePool.getInstance(
                  getApplicationContext(), getDatabaseInfo(dbName), true);
          database = databasePool.getDatabase();
      } catch (Exception e) {
          database = null;
          e.printStackTrace();
      }
      
      return database;
  }

  /**
   * 根据数据库名称获得数据库信息，如果
   * @param dbName
   * @return
   */
  private DatabaseInfo getDatabaseInfo(String dbName){
	  
      if(dbinfoMap.containsKey(dbName)){
          return dbinfoMap.get(dbName);
      }
      
      DatabaseInfo dbInfo = new DatabaseInfo();
      String dbPath = FileUtil.getInstance(getApplication()).getMediaPath();
      File file = new File(dbPath);
      
      if (!file.exists()) {
          file.mkdirs();
      }
      
      if (StringUtil.isBlank(dbName)) {
          dbName = "ecity.db";
      }
      
      dbName = dbName.toLowerCase();
      if (!dbName.endsWith(".db")) {
          dbName += ".db";
      }
      
      // 保存在SD卡上的完整路径
      dbInfo.setDBFullPath(dbPath + dbName);
      dbInfo.setDbName(dbName);
      // 是否保存在系统路径下，如果保存在系统路径下，则无需设置SD卡路径
      dbInfo.setSystemMode(false);
      dbinfoMap.put(dbName, dbInfo);
      return dbInfo;
  }
  
}