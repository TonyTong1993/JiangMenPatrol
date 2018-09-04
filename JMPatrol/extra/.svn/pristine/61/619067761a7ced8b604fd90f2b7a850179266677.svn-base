package com.ecity.android.map.core.local;

import android.database.sqlite.SQLiteDatabase;

import com.ecity.android.map.core.meta.DbMetaInfo;
import com.ecity.android.map.core.util.ListUtil;

import java.util.List;

/**
 * 管网.
 *
 * @author wangfeng
 * @date 2017/4/24
 */

public class MetaInfoDAO {

	private DBHelper dbHelper;

	public MetaInfoDAO(DBHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	// 插入操作
	public void insertData(DbMetaInfo metaInfo) {
		if (null != metaInfo) {
			String sql = "insert into metainfo (layername,describe,geotype,code)values(?,?,?,?)";
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			db.execSQL(sql, new Object[]{metaInfo.getLayername(),
										 metaInfo.getDescripe(),
										 metaInfo.getType(),
										 metaInfo.getCode()});
		}

	}

	public void insertData(List<DbMetaInfo> metaInfos) {
		if(ListUtil.isEmpty(metaInfos)){
			return;
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			for(int i=0;i<metaInfos.size();i++){
				DbMetaInfo metaInfo=metaInfos.get(i);
				String sql = "insert into metainfo (layername,describe,geotype,code)values(?,?,?,?)";
				db.execSQL(sql, new Object[]{metaInfo.getLayername(),
											 metaInfo.getDescripe(),
											 metaInfo.getType(),
											 metaInfo.getCode()});
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	// 其它操作

}
