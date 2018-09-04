package com.z3.android.plugin.weather.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;


/**
 * 
 * The class to help the operation of the database
 * @author SunShanai
 * @version  In March 17, 2015 10:27:45 p.m.
 */
public class DBHelper extends android.content.ContextWrapper {
	private static DatabaseHelper mDbHelper;
	private static SQLiteDatabase mDb;
	private static final String DATABASE_NAME="city.db";
	private static final int DATABASE_VERSION=1;
	private Context mCtx;
	private Cursor cursor;
	
	/**
	 * The inner class extends SQLiteOpenHelper 
	 * @author SunShanai
	 *
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper{


		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * The Constructor
	 * @param ctx Context
	 */
	public DBHelper(Context ctx) {
		super(ctx);
		this.mCtx = ctx;
		openDB();
	}
		
		/**
		 * Open the database
		 */
		public void openDB(){
			String path = "/data"
					+ Environment.getDataDirectory().getAbsolutePath()
					+ File.separator + mCtx.getPackageName() + File.separator
					+ "city.db";
			File db = new File(path);
			if (!db.exists()) {
				// L.i("db is not exists");
				try {
					InputStream is = getAssets().open("city.db");
					FileOutputStream fos = new FileOutputStream(db);
					int len = -1;
					byte[] buffer = new byte[1024];
					while ((len = is.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
						fos.flush();
					}
					fos.close();
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					//T.showLong(mApplication, e.getMessage());
					System.exit(0);
				}
			}
			mDbHelper = new DatabaseHelper(mCtx);
			//mDb=mDbHelper.getReadableDatabase();
			mDb=SQLiteDatabase.openOrCreateDatabase(path, null);
		}
		

		/**
		 * execute the SQL（with arguments）
		 * @param sql
		 * @param selectionArgs
		 */
		public String rawQuery(String sql, String[] selectionArgs){
			
			cursor=mDb.rawQuery(sql,selectionArgs);
		    cursor.moveToFirst();
			return cursor.getString(cursor.getColumnIndex("number"));
			
		}
		
		
		
		/**
		 * Close the opened connection
		 */
		public void closeConnection(){
			
			if(mDb!=null && mDb.isOpen()){
				mDb.close();
			}
			if(mDbHelper!=null){
				mDbHelper.close();
			}
			if(cursor!=null){
				cursor.close();
			}
		}
}
