package com.ecity.android.httpexecutor.init;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ecity.android.httpexecutor.daomaster.CacheFeedbackFormDaoMaster;
import com.ecity.android.httpexecutor.daosession.CacheFeedBackFormDaoSession;
import com.ecity.android.httpexecutor.util.MyDataBaseContext;

/**
 * @author yongzhan
 * @date 2017/12/26
 */
public class InitCacheFeedbackDataDB {
    private CacheFeedbackFormDaoMaster mDaoMaster;
    private CacheFeedBackFormDaoSession mDaoSession;
    private CacheFeedbackFormDaoMaster.DevOpenHelper mDevOpenHelper;
    private SQLiteDatabase db;
    private MyDataBaseContext myDataBaseContext;
    public static Context mcontext;
    public static InitCacheFeedbackDataDB initCacheFeedbackDataDB;

    public InitCacheFeedbackDataDB() {
        myDataBaseContext = new MyDataBaseContext(mcontext);
        setDataBase();
    }

    public static InitCacheFeedbackDataDB getInstance(Context context) {
        mcontext = context;
        initCacheFeedbackDataDB = new InitCacheFeedbackDataDB();
        return initCacheFeedbackDataDB;
    }

    public void setDataBase() {
        mDevOpenHelper = new CacheFeedbackFormDaoMaster.DevOpenHelper(myDataBaseContext, "CacheFeedbackData", null);

        db = mDevOpenHelper.getWritableDatabase();
        mDaoMaster = new CacheFeedbackFormDaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }
    public CacheFeedBackFormDaoSession getDaoSession(){
        return mDaoSession;
    }
    public SQLiteDatabase getDB() {
        return db;
    }
}
