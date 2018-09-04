package com.ecity.android.map.core.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ecity.android.map.core.meta.DbMetaField;
import com.ecity.android.map.core.meta.DbMetaInfo;
import com.ecity.android.map.core.meta.DbMetaNet;
import com.ecity.android.map.core.meta.QueryLayerIDs;
import com.ecity.android.map.core.util.ListUtil;
import com.ecity.android.map.core.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * File description.
 *
 * @author wangfeng
 * @date 2017/4/24
 */

public class DBHelper extends SQLiteOpenHelper {

	private final static String DB_NAME = "local.db";//数据库名
	private final static int    VERSION = 1;//版本号

	//自带的构造方法
	public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	//为了每次构造时不用传入dbName和版本号，自己得新定义一个构造方法
	public DBHelper(Context cxt) {
		this(cxt, DB_NAME, null, VERSION);//调用上面的构造方法
	}

	//版本变更时
	public DBHelper(Context cxt, int version) {
		this(cxt, DB_NAME, null, version);
	}

	//当数据库创建的时候,创建5张表
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table  if not exists  metainfo(" +
					 "layername text primary key," + "describe text," + "geotype integer," +
					 "code text)";
		String net_mtSql = "create table  if not exists  net_mt(" +
						   "dno integer primary key," + "dname text," + "dalias text," + "geo_type integer," + "bs_prop integer," + "sid integer," +
						   "cnn_num integer" +
						   ")";
		String net_mt_fldSql = "create table  if not exists  net_mt_fld(" +
							   "dno integer," + "dname text," + "name text," + "alias text," + "disptype integer," + "defval text," +
							   "fldval text," + "prop text," + "visible integer," + "editable integer," + "findex integer" +
							   ")";
		String net_linSql = getNet_linSql();
		String net_nodSql = getNet_NodSql();

		db.beginTransaction();
		try {
			db.execSQL(sql);
			db.execSQL(net_mtSql);
			db.execSQL(net_mt_fldSql);
			db.execSQL(net_linSql);
			db.execSQL(net_nodSql);

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

	}

	/**
	 * 得到创建net_lin的sql语句
	 */
	public String getNet_linSql() {
		String sql = "";
		Map<String, String> fieldsMap = new HashMap<>();
		List<DbMetaInfo> dbMetaInfos = QueryLayerIDs.getInstance().getDbMetaInfoswithNet();
		if (!ListUtil.isEmpty(dbMetaInfos)) {
			for (int i = 0; i < dbMetaInfos.size(); i++) {
				DbMetaInfo dbMetaInfo = dbMetaInfos.get(i);
				List<DbMetaNet> dbMetaNets = dbMetaInfo.getNet();
				if (!ListUtil.isEmpty(dbMetaNets)) {
					for (int j = 0; j < dbMetaNets.size(); j++) {
						DbMetaNet dbMetaNet = dbMetaNets.get(j);
						if (dbMetaNet.getGeo_type() == 0) {
							List<DbMetaField> dbMetaFields = dbMetaNet.getFields();
							if (!ListUtil.isEmpty(dbMetaFields)) {
								for (int k = 0; k < dbMetaFields.size(); k++) {
									FieldModel field = new FieldModel();
									field.setType(dbMetaFields.get(k).getEsritype());
									fieldsMap.put(dbMetaFields.get(k).getAlias(), DataTypesHelper.fieldDataType(field));
								}
							}
						}
					}
				}
			}
		}
		sql = "create table  if not exists  net_lin(";
		for (Map.Entry<String, String> entry : fieldsMap.entrySet()) {
			if(entry.getKey().equals("gid")){//添加唯一主键
				sql += entry.getKey() + " " + entry.getValue() + " primary key,";
			}else{
				sql += entry.getKey() + " " + entry.getValue() + ",";
			}
		}
		//加上x，y构成的范围以及离线查询所需的sid,geom,layerId
		sql+="layerId integer,"+"geom blob,"+"xmin real,"+"ymin real,"+"xmax real,"+"ymax real";
//		sql = sql.substring(0, sql.length() - 1);
		sql += ")";
		return sql;
	}

	/**
	 * 得到创建net_nod的sql语句
	 */
	public String getNet_NodSql() {
		String sql = "";
		Map<String, String> fieldsMap = new HashMap<>();
		List<DbMetaInfo> dbMetaInfos = QueryLayerIDs.getInstance().getDbMetaInfoswithNet();
		if (!ListUtil.isEmpty(dbMetaInfos)) {
			for (int i = 0; i < dbMetaInfos.size(); i++) {
				DbMetaInfo dbMetaInfo = dbMetaInfos.get(i);
				List<DbMetaNet> dbMetaNets = dbMetaInfo.getNet();
				if (!ListUtil.isEmpty(dbMetaNets)) {
					for (int j = 0; j < dbMetaNets.size(); j++) {
						DbMetaNet dbMetaNet = dbMetaNets.get(j);
						if (dbMetaNet.getGeo_type() == 1) {
							List<DbMetaField> dbMetaFields = dbMetaNet.getFields();
							if (!ListUtil.isEmpty(dbMetaFields)) {
								for (int k = 0; k < dbMetaFields.size(); k++) {
									FieldModel field = new FieldModel();
									field.setType(dbMetaFields.get(k).getEsritype());
									fieldsMap.put(dbMetaFields.get(k).getAlias(), DataTypesHelper.fieldDataType(field));
								}
							}
						}
					}
				}
			}
		}
		sql = "create table  if not exists  net_nod(";
		for (Map.Entry<String, String> entry : fieldsMap.entrySet()) {
			if((!StringUtil.isEmpty(entry.getKey())) && (!StringUtil.isEmpty(entry.getValue()))){
				if(entry.getKey().equals("gid")){//添加唯一主键
					sql += entry.getKey() + " " + entry.getValue() + " primary key,";
				}else{
					sql += entry.getKey() + " " + entry.getValue() + ",";
				}
			}
		}
		//加上x，y
		sql+="layerId integer,"+"x real,"+"y real";
//		sql = sql.substring(0, sql.length() - 1);

		sql += ")";
		return sql;
	}


	//版本更新时调用
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
