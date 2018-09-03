package com.ecity.android.httpexecutor.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * @author yongzhan
 * @date 2017/10/27
 */
public class MyDataBaseContext extends ContextWrapper {
    private Context base1;

    public MyDataBaseContext(Context base) {

        super(base);
        this.base1 = base;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }

    @Override
    public File getDatabasePath(String name) {

        //获取手机内存路径
        String dbDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath().toString();
        PackageManager pm = base1.getApplicationContext().getPackageManager();
        String app = base1.getApplicationContext().getApplicationInfo().loadLabel(pm).toString();
        dbDir = dbDir + "/" + "SOP" + "/db/";//数据库所在目录
        String dbPath = dbDir + name;//数据库路径
        //判断目录是否存在，不存在则创建该目录
        File dirFile = new File(dbDir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        //数据库文件是否创建成功
        boolean isFileCreateSuccess = false;
        //判断文件是否存在，不存在则创建该文件
        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            try {
                isFileCreateSuccess = dbFile.createNewFile();//创建文件
            } catch (IOException e) {
                Log.e("permission","lyz请打开数据存储权限");
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            isFileCreateSuccess = true;
        }

        //返回数据库文件对象
        if (isFileCreateSuccess) {
            return dbFile;
        } else {
            return null;
        }


    }
}
