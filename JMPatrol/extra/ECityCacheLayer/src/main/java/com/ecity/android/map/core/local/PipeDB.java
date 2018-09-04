package com.ecity.android.map.core.local;

import android.os.Environment;
import android.util.Log;

import com.ecity.android.map.core.dbquery.ECityRect;
import com.ecity.android.map.core.dbquery.QueryWhereHelper;
import com.ecity.android.map.core.meta.DbMetaField;
import com.ecity.android.map.core.meta.DbMetaInfo;
import com.ecity.android.map.core.meta.DbMetaNet;
import com.ecity.android.map.core.util.FileHelper;
import com.ecity.android.map.core.util.ListUtil;
import com.esri.core.map.Graphic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PipeDB {

	/**
	 * TAG
	 */
	private static final String TAG = "PIPE_DB";

	/**
	 * @comments 表模型集，对应可获得表名和字段
	 */
	private ArrayList<TableModel>                       tables                    = null;
	/**
	 * @comments 元数据-符号数据表
	 */
	private HashMap<Integer, SQLiteRecordBean>          grpdataRecordBeanMap      = null;
	/**
	 * @comments 元数据-线符号数据表
	 */
	private HashMap<Integer, SQLiteRecordBean>          lsymbolRecordBeanMap      = null;
	/**
	 * @comments 元数据-点符号数据表
	 */
	private HashMap<Integer, SQLiteRecordBean>          psymbolRecordBeanMap      = null;
	/**
	 * @comments 元数据-实体管线配置数据表
	 */
	private ArrayList<SQLiteRecordBean>                 databaseMetaInfoBeanList  = null;
	/**
	 * @comments 当前所有工程的描述信息
	 */
	private HashMap<String, HashMap<Integer, NetmtFld>> recordMetaInfoBeanListMap = null;
	/**
	 * @comments 从本地db读取的元数据
	 */
	private List<DbMetaInfo>                            dbMetaInfos               = null;

	/**
	 * @comments 初始下载后显示带有ogid值的最大gid值
	 */
	HashMap<String, Integer> maxPointGidMap = null;
	/**
	 * @comments 初始下载后显示带有ogid值的最大gid值
	 */
	HashMap<String, Integer> maxLineGidMap  = null;

	/**
	 * 单例模式的实例
	 */
	private static PipeDB instance = null;

	/**
	 * 数据库的真实操作类
	 */
	private PipeDBOper projectDBOper = null;

	/**
	 * 数据库文件的绝对路径
	 */
	private String dbPathName = null;

	/**
	 * private 构造函数，不允许外部New出来实例
	 */
	private PipeDB() {
		projectDBOper = new PipeDBOper();
	}

	/**
	 * @return 数据库操作实例
	 */
	public static PipeDB getInstance() {
		if (instance == null) {
			instance = new PipeDB();

		}
		return instance;
	}

	/**
	 * 读取的数据库文件发生改变
	 */
	public void changeDB(String dbPathName) {
		this.dbPathName = dbPathName;

		projectDBOper.close();
	}

	public boolean isSQLiteDatabaseExisted() {

		dbPathName = getDbName();
		if (dbPathName == null) {
			return false;
		}

		System.out.println("db open:" + dbPathName);

		// 数据库是否开启
		if (!projectDBOper.openDatabase(dbPathName)) {
			System.out.println("db not exist" + dbPathName);
			return false;
		}

		System.out.println("db exist:" + dbPathName);

		projectDBOper.close();

		try {
			initTableModels();
			initMetaTables();
			initRecordMetaInfoTables();
			initMetas();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private String getDbName() {

		if (dbPathName == null) {
			return  FileHelper.getInstance().getMapCachePath()+"test.db";

		} else {
			return dbPathName;
		}

	}

	public List<Integer> getLineListByRect(double left, double right, double top, double bottom) {
		if (dbPathName == null) {
			return null;
		}

		// 数据库是否开启
		if (!projectDBOper.openDatabase(dbPathName)) {
			// err_msg = "数据库为成功打开";
			return null;
		}

		List<Integer> list = new ArrayList<Integer>();

		String selectWhere = "xmax >= " + (left) + " and xmin <= " + (right) + " and ymax >= " + (top) + " and ymin <= " + (bottom);

		String linTableName = "net_lin";

		ArrayList<SQLiteRecordBean> beanList = null;
		try {
			beanList = (ArrayList<SQLiteRecordBean>) projectDBOper.queryWithWhere(linTableName, selectWhere, DBTableType.DBTableType_LINE);
		} catch (Exception e) {
			Log.i(TAG, "读取管线数据报错");
			e.printStackTrace();
		}

		if (beanList != null) {
			for (Iterator<SQLiteRecordBean> iterator = beanList.iterator(); iterator.hasNext(); ) {
				SQLiteRecordBean sqLiteRecordBean = (SQLiteRecordBean) iterator.next();
				Map<String, Object> map = sqLiteRecordBean.getAttribute();
				if (map != null) {
					int id = (Integer) map.get("gid");
					list.add(id);
				}

			}
		}

		projectDBOper.close();
		return list;

	}

	public List<Integer> getNodeListByRect(double left, double right, double top, double bottom) {
		if (dbPathName == null) {
			return null;
		}
		// 数据库是否开启
		if (!projectDBOper.openDatabase(dbPathName)) {
			// err_msg = "数据库为成功打开";
			return null;
		}
		List<Integer> list = new ArrayList<Integer>();
		String selectWhere = "x >= " + (left) + " and x <= " + (right) + " and y >= " + (top) + " and y <= " + (bottom);
		String linTableName = "net_nod";

		ArrayList<SQLiteRecordBean> beanList = null;
		try {
			beanList = (ArrayList<SQLiteRecordBean>) projectDBOper.queryWithWhere(linTableName, selectWhere, DBTableType.DBTableType_POINT);
		} catch (Exception e) {
			Log.i(TAG, "读取管点数据报错");
			e.printStackTrace();
		}

		if (beanList != null) {
			for (Iterator<SQLiteRecordBean> iterator = beanList.iterator(); iterator.hasNext(); ) {
				SQLiteRecordBean sqLiteRecordBean = (SQLiteRecordBean) iterator.next();
				Map<String, Object> map = sqLiteRecordBean.getAttribute();
				if (map != null) {
					int id = (Integer) map.get("gid");
					list.add(id);
				}

			}
		}

		projectDBOper.close();
		return list;

	}

	/**
	 * 根据列表中的id组建查询的条件语句
	 */
	public String getWhereStringByIds(Set<Integer> ids) {

		if (ids == null || ids.size() == 0) {
			return null;
		}
		// String sqlNodeIDs = "";
		StringBuffer buffer = new StringBuffer();
		for (Integer tempID : ids) {
			if (buffer.length() > 0) {
				buffer.append(",");
			}
			buffer.append(tempID);
			// sqlNodeIDs += tempID;
		}

		return buffer.toString();
	}

	public List<Graphic> getAllGraphicsFromWhere(String where, ECityRect rct) {
		if (dbPathName == null) {
			return null;
		}

		// 数据库是否开启
		if (!projectDBOper.openDatabase(dbPathName)) {
			return null;
		}
		List<Graphic> nodeGraphics = getAllNodeByWhere(where, rct);

		List<Graphic> lineGraphics = getAllLineByWhere(where, rct);

		nodeGraphics.addAll(lineGraphics);

		projectDBOper.close();
		return nodeGraphics;
	}

	private List<Graphic> getAllLineByWhere(String where, ECityRect rct) {
		List<Graphic> disGraphics = new ArrayList<Graphic>();
		for (int i = 0; i < databaseMetaInfoBeanList.size(); i++) {
			SQLiteRecordBean metainRecordBean = databaseMetaInfoBeanList.get(i);
			String layerName = (String) metainRecordBean.getAttribute().get("layername");
			String pntTableName = layerName + "_lin";
			ArrayList<SQLiteRecordBean> beanList = null;
			String rctString = QueryWhereHelper.getLineRectString(rct);
			String selectWhere = where == null ? rctString : where + " and " + rctString;
			try {
				beanList = (ArrayList<SQLiteRecordBean>) projectDBOper.queryWithWhere(pntTableName, selectWhere,
																					  DBTableType.DBTableType_LINE);
			} catch (Exception e) {
				Log.i(TAG, "读取管xian数据报错");
				e.printStackTrace();
			}

			for (int j = 0; j < beanList.size(); j++) {
				try {
					disGraphics.add(ECityPipeTransUtil.recordBeantoGraphic(beanList.get(j)));
				} catch (Exception e) {
					Log.i(TAG, "管xian数据转成Graphic时报错，gid = " + beanList.get(j).getAttribute().get("gid"));
					e.printStackTrace();
				}
			}
		}

		return disGraphics;

	}

	private List<Graphic> getAllNodeByWhere(String where, ECityRect rct) {

		List<Graphic> disGraphics = new ArrayList<Graphic>();
		for (int i = 0; i < databaseMetaInfoBeanList.size(); i++) {
			SQLiteRecordBean metainRecordBean = databaseMetaInfoBeanList.get(i);
			String layerName = (String) metainRecordBean.getAttribute().get("layername");
			String pntTableName = layerName + "_nod";
			ArrayList<SQLiteRecordBean> beanList = null;
			String rctString = QueryWhereHelper.getNodeRectString(rct);
			String selectWhere = where == null ? rctString : where + " and " + rctString;
			try {
				beanList = (ArrayList<SQLiteRecordBean>) projectDBOper.queryWithWhere(pntTableName, selectWhere,
																					  DBTableType.DBTableType_POINT);
			} catch (Exception e) {
				Log.i(TAG, "读取管xian数据报错");
				e.printStackTrace();
			}

			for (int j = 0; j < beanList.size(); j++) {
				try {
					disGraphics.add(ECityPipeTransUtil.recordBeantoGraphic(beanList.get(j)));
				} catch (Exception e) {
					Log.i(TAG, "管点数据转成Graphic时报错，gid = " + beanList.get(j).getAttribute().get("gid"));
					e.printStackTrace();
				}
			}
		}

		return disGraphics;
	}

	public List<Graphic> queryNode(String layerName, List<Integer> dnos, String where, ECityRect rect) {
		return null;
	}

	public List<Graphic> queryLine(String layerName, List<Integer> dnos, String where, ECityRect rect) {
		return null;

	}

	/**
	 * @package com.ecity.android.cityrevisionpo.data
	 * @method closeWorkSpace
	 * @function 关闭工作空间
	 */
	public void closeWorkSpace() {
		releaseWorkSpace();
	}

	/**
	 * @package com.ecity.android.cityrevisionpo.data
	 * @method releaseWorkSpace
	 * @function 释放工作空间
	 */
	private void releaseWorkSpace() {
		if (projectDBOper != null) {
			projectDBOper.close();
		}
	}

	private boolean initTableModels() throws Exception {
		if (dbPathName == null) {
			return false;
		}

		// 数据库是否开启
		if (!projectDBOper.openDatabase(dbPathName)) {
			// err_msg = "数据库未成功打开";
			return false;
		}
		// Tables实例化
		if (tables == null) {
			tables = new ArrayList<TableModel>();
		}
		tables.clear();

		TableStructHelper tableStructHelper = new TableStructHelper();
		// 获取表结构信息
		ArrayList<SQLiteRecordBean> createTableSQLs = (ArrayList<SQLiteRecordBean>) projectDBOper.getDatabaseTablesInfo();
		for (int i = 0; i < createTableSQLs.size(); i++) {
			SQLiteRecordBean tableSQLBean = createTableSQLs.get(i);
			if (tableSQLBean.getAttribute() == null || tableSQLBean.getAttribute().size() <= 0) {
				continue;
			}
			String sql = (String) (tableSQLBean.getAttribute().get("sql"));
			String tableName = (String) (tableSQLBean.getTablename());
			String tableType = (String) (tableSQLBean.getTabletype());
			if (sql == null || tableName == null || tableType == null) {
				continue;
			}
			tables.add(tableStructHelper.getTableForSQL(tableName, tableType, sql));
		}
		return true;
	}

	/**
	 * @package com.ecity.android.cityrevisionpo.data
	 * @method initDataSetTables
	 * @function 初始化实体数据集
	 */
	@SuppressWarnings("deprecation")
	private boolean initRecordMetaInfoTables() throws Exception {
		if (dbPathName == null) {
			return false;
		}
		// 数据库是否开启
		if (!projectDBOper.openDatabase(dbPathName)) {
			// err_msg = "数据库没有成功打开";
			return false;
		}
		//
		if (recordMetaInfoBeanListMap == null) {
			recordMetaInfoBeanListMap = new HashMap<String, HashMap<Integer, NetmtFld>>();
		}
		recordMetaInfoBeanListMap.clear();
		// 根据元数据字段LayerName记录，拼接实体表名
		for (int i = 0; i < databaseMetaInfoBeanList.size(); i++) {
			SQLiteRecordBean metainfoRecordBean = databaseMetaInfoBeanList.get(i);
			String layerName = (String) metainfoRecordBean.getAttribute().get("layername");
			String mtName = layerName + "_mt";
			String mtFldName = layerName + "_mt_fld";
			HashMap<Integer, NetmtFld> netmtFldMap = new HashMap<Integer, NetmtFld>();
			ArrayList<SQLiteRecordBean> beanList = (ArrayList<SQLiteRecordBean>) projectDBOper.queryWithWhere(mtName, null,
																											  DBTableType.DBTableType_MT);
			// 遍历MT表内所有的字段，根据dno到MT_FLD表中获取字段名
			for (SQLiteRecordBean bean : beanList) {
				int dno = (Integer) bean.getAttribute().get("dno");
				NetmtFld netmtFld = new NetmtFld(bean);
				String selectWhere = "dno = ' " + String.valueOf(dno) + " ' order by findex";
				ArrayList<SQLiteRecordBean> beanList2 = (ArrayList<SQLiteRecordBean>) projectDBOper.queryWithWhere(mtFldName, selectWhere,
																												   DBTableType.DBTableType_MT_FLD);
				netmtFld.initMTFldModelListByBeanList(beanList2);
				// 添加到Map,后期可直接根据dno访问
				netmtFldMap.put(dno, netmtFld);
			}
			recordMetaInfoBeanListMap.put(mtName, netmtFldMap);
		}
		return true;
	}

	/**
	 * 加载元数据表信息
	 */
	private boolean initMetaTables() throws Exception {

		// 实例化
		if (grpdataRecordBeanMap == null) {
			grpdataRecordBeanMap = new HashMap<Integer, SQLiteRecordBean>();
		}
		if (lsymbolRecordBeanMap == null) {
			lsymbolRecordBeanMap = new HashMap<Integer, SQLiteRecordBean>();
		}
		if (psymbolRecordBeanMap == null) {
			psymbolRecordBeanMap = new HashMap<Integer, SQLiteRecordBean>();
		}
		if (databaseMetaInfoBeanList == null) {
			databaseMetaInfoBeanList = new ArrayList<SQLiteRecordBean>();
		}
		// 清除原先记录
		grpdataRecordBeanMap.clear();
		lsymbolRecordBeanMap.clear();
		psymbolRecordBeanMap.clear();
		databaseMetaInfoBeanList.clear();

		databaseMetaInfoBeanList = (ArrayList<SQLiteRecordBean>) projectDBOper.queryWithWhere("metainfo", null,
																							  DBTableType.DBTableType_METAINFO);
		return true;
	}

	/**
	 * description ：初始化元数据
	 */
	private void initMetas() {
		if (!ListUtil.isEmpty(databaseMetaInfoBeanList)) {
			dbMetaInfos = new ArrayList<>();
			for (int i = 0; i < databaseMetaInfoBeanList.size(); i++) {
				SQLiteRecordBean bean = databaseMetaInfoBeanList.get(i);
				DbMetaInfo metaInfo = new DbMetaInfo();
				List<DbMetaNet> dbMetaNets = new ArrayList<>();
				Map<String, Object> attrMap = bean.getAttribute();
				metaInfo.setLayername((String) attrMap.get("layername"));
				metaInfo.setType((Integer) attrMap.get("geotype"));
				metaInfo.setCode((String) attrMap.get("code"));
				metaInfo.setDescripe((String) attrMap.get("describe"));
				if (recordMetaInfoBeanListMap.size() > 0) {
					Iterator<Map.Entry<String, HashMap<Integer, NetmtFld>>> iterator = recordMetaInfoBeanListMap.entrySet().iterator();
					while (iterator.hasNext()) {
						Map.Entry<String, HashMap<Integer, NetmtFld>> entry = iterator.next();
						HashMap<Integer, NetmtFld> mtFldMap = entry.getValue();
						Iterator<Map.Entry<Integer, NetmtFld>> mtIterator = mtFldMap.entrySet().iterator();
						while (mtIterator.hasNext()) {
							Map.Entry<Integer, NetmtFld> mtEntry = mtIterator.next();
							DbMetaNet dbMetaNet = new DbMetaNet();
							NetmtFld netmtFld = mtEntry.getValue();
							dbMetaNet.setDid(netmtFld.getDno());
							dbMetaNet.setBs_prop(String.valueOf(netmtFld.getBs_prop()));
							dbMetaNet.setClsid(netmtFld.getSid());
							dbMetaNet.setDname(netmtFld.getDname());
							dbMetaNet.setDalias(netmtFld.getDalias());
							dbMetaNet.setGeo_type(netmtFld.getGeo_type());
							List<DbMetaField> dbMetaFields = new ArrayList<>();
							List<MTFldModel> mtFldModels = netmtFld.getMtFldModelList();
							if (!ListUtil.isEmpty(mtFldModels)) {
								for (int j = 0; j < mtFldModels.size(); j++) {
									MTFldModel mtFldModel = mtFldModels.get(j);
									DbMetaField dbMetaField = new DbMetaField();
									dbMetaField.setAlias(mtFldModel.getAlias());
									dbMetaField.setDefval(mtFldModel.getDefval());
									dbMetaField.setDisptype(mtFldModel.getDisptype());
									dbMetaField.setEditable(mtFldModel.getEditable() == 0 ? false : true);
									dbMetaField.setName(mtFldModel.getName());
									dbMetaField.setProp(mtFldModel.getProp());
									dbMetaField.setVisible(mtFldModel.getVisible() == 0 ? false : true);
									String fldval = mtFldModel.getFldval();
									List<Object> values = new ArrayList<>();
									if (!isEmpty(fldval)) {
										String[] fldValues = fldval.split(",");
										if (fldValues != null) {
											for (int k = 0; k < fldValues.length; k++) {
												values.add(fldValues[k]);
											}
										}
									}
									dbMetaField.setValues(values);
									dbMetaFields.add(dbMetaField);
								}
							}
							dbMetaNet.setFields(dbMetaFields);
							dbMetaNets.add(dbMetaNet);
						}
					}
				}
				metaInfo.setNet(dbMetaNets);
				dbMetaInfos.add(metaInfo);
			}
		}
	}

	public List<DbMetaInfo> getDbMetaInfosFromDB(){
		return dbMetaInfos;
	}

	public ArrayList<TableModel> getTaskPlanTables() {
		return tables;

	}

	/**
	 * 拷贝数据库到sd卡
	 */
	public static void copyDataBaseToSD(File fromFile,File  toFile ) {
		if(null == fromFile || null == toFile) {
			return;
		}

		if (! Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return;
		}

		//fromFile = new File(MainApplication.getApplication().getDatabasePath("local") + ".db");
		//toFile = new File(Environment.getExternalStorageDirectory(), "local_data.db");

		FileChannel inChannel = null, outChannel = null;
		if (FileHelper.isFileExists(toFile)) {
			FileHelper.deleteFile(toFile);
		}
		try {
			toFile.createNewFile();
			inChannel = new FileInputStream(fromFile).getChannel();
			outChannel = new FileOutputStream(toFile).getChannel();
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inChannel != null) {
					inChannel.close();
					inChannel = null;
				}
				if (outChannel != null) {
					outChannel.close();
					outChannel = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static final boolean isEmpty(String str) {

		return (null == str || str.length() == 0 || str.isEmpty())? true : false;
	}
}
