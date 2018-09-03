package com.ecity.android.httpexecutor.init;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ecity.android.httpexecutor.daomaster.RequestRecordDaoMaster;
import com.ecity.android.httpexecutor.daosession.RequestRecordDaoSession;
import com.ecity.android.httpexecutor.util.MyDataBaseContext;

/**
 * @author yongzhan
 * @date 2017/12/26
 */
public class InitRequestRecordDB {
    private RequestRecordDaoMaster mDaoMaster;
    private RequestRecordDaoSession mDaoSession;
    private RequestRecordDaoMaster.DevOpenHelper mDevOpenHelper;
    private SQLiteDatabase db;
    private MyDataBaseContext myDataBaseContext;
    private static InitRequestRecordDB instance;

    private InitRequestRecordDB(Context context) {
        myDataBaseContext = new MyDataBaseContext(context.getApplicationContext());
        setDataBase();
    }

    public static InitRequestRecordDB getInstance(Context context) {
        if(instance == null){
            synchronized (InitRequestRecordDB.class){
                if(instance ==null){
                    instance = new InitRequestRecordDB(context);
                }
            }
        }
        return instance;
    }

    private void setDataBase() {
        mDevOpenHelper = new RequestRecordDaoMaster.DevOpenHelper(myDataBaseContext, "requestRecord", null);

        db = mDevOpenHelper.getWritableDatabase();
        mDaoMaster = new RequestRecordDaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }
    public RequestRecordDaoSession getDaoSession(){
        return mDaoSession;
    }
    public SQLiteDatabase getDB() {
        return db;
    }
}
