package com.ecity.android.map.core.local;

import android.database.sqlite.SQLiteDatabase;

import com.ecity.android.map.core.meta.DbMetaNet;
import com.ecity.android.map.core.util.ListUtil;

import java.util.List;

/**
 * 管网.
 *
 * @author wangfeng
 * @date 2017/4/24
 */

public class DbMetaNetDAO {

	private DBHelper dbHelper;

	public DbMetaNetDAO(DBHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	// 插入操作
	public void insertData(DbMetaNet dbMetaNet) {
		if (null != dbMetaNet) {
			String sql = "insert into net_mt (dno,dname,dalias,geo_type,bs_prop,sid,cnn_num)values(?,?,?,?,?,?,?)";
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			db.execSQL(sql, new Object[]{dbMetaNet.getDid(),
										 dbMetaNet.getDname(),
										 dbMetaNet.getDalias(),
										 dbMetaNet.getGeo_type(),
										 dbMetaNet.getBs_prop(),
										 dbMetaNet.getSrid(),
										 dbMetaNet.getCnn_num()});
		}
	}

	public void insertData(List<DbMetaNet> dbMetaNets) {
		if(ListUtil.isEmpty(dbMetaNets)){
			return;
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			for(int i=0;i<dbMetaNets.size();i++){
				DbMetaNet dbMetaNet=dbMetaNets.get(i);
				String sql = "insert into net_mt (dno,dname,dalias,geo_type,bs_prop,sid,cnn_num)values(?,?,?,?,?,?,?)";
				db.execSQL(sql, new Object[]{dbMetaNet.getDid(),
											 dbMetaNet.getDname(),
											 dbMetaNet.getDalias(),
											 dbMetaNet.getGeo_type(),
											 dbMetaNet.getBs_prop(),
											 dbMetaNet.getSrid(),
											 dbMetaNet.getCnn_num()});
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	// 其它操作

}
