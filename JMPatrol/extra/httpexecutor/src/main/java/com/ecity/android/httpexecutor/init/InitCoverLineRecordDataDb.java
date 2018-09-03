package com.ecity.android.httpexecutor.init;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ecity.android.httpexecutor.daomaster.CoverLineRecordDaoMaster;
import com.ecity.android.httpexecutor.daosession.CoverLineRecordDaoSession;
import com.ecity.android.httpexecutor.util.MyDataBaseContext;

/**
 * @author xiaobei
 * @date 2018/7/23
 */
public class InitCoverLineRecordDataDb {
    private CoverLineRecordDaoMaster mDaoMaster;
    private CoverLineRecordDaoSession mDaoSession;
    private CoverLineRecordDaoMaster.DevOpenHelper mDevOpenHelper;
    private SQLiteDatabase db;
    private MyDataBaseContext myDataBaseContext;
    public static Context mcontext;
    public static InitCoverLineRecordDataDb initCacheFeedbackDataDB;

    public InitCoverLineRecordDataDb() {
        myDataBaseContext = new MyDataBaseContext(mcontext);
        setDataBase();
    }

    public static InitCoverLineRecordDataDb getInstance(Context context) {
        mcontext = context;
        initCacheFeedbackDataDB = new InitCoverLineRecordDataDb();
        return initCacheFeedbackDataDB;
    }

    public void setDataBase() {
        mDevOpenHelper = new CoverLineRecordDaoMaster.DevOpenHelper(myDataBaseContext, "CoverLineRecord", null);

        db = mDevOpenHelper.getWritableDatabase();
        mDaoMaster = new CoverLineRecordDaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }
    public CoverLineRecordDaoSession getDaoSession(){
        return mDaoSession;
    }
    public SQLiteDatabase getDB() {
        return db;
    }
}
