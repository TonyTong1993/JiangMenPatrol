package com.ecity.android.httpexecutor.daomaster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ecity.android.httpexecutor.daosession.RequestRecordDaoSession;
import com.ecity.android.httpexecutor.greendao.DaoMaster;
import com.ecity.android.httpexecutor.model.MyRequestRecordDao;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

/**
 * @author yongzhan
 * @date 2017/12/26
 */
public class RequestRecordDaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(Database db, boolean ifNotExists) {
        MyRequestRecordDao.createTable(db, ifNotExists);

    }

    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(Database db, boolean ifExists) {
        MyRequestRecordDao.dropTable(db, ifExists);
    }

    /**
     * WARNING: Drops all table on Upgrade! Use only during development.
     * Convenience method using a {@link DaoMaster.DevOpenHelper}.
     */
    public static RequestRecordDaoSession newDevSession(Context context, String name) {
        Database db = new RequestRecordDaoMaster.DevOpenHelper(context, name).getWritableDb();
        RequestRecordDaoMaster daoMaster = new RequestRecordDaoMaster(db);
        return daoMaster.newSession();
    }

    public RequestRecordDaoMaster(SQLiteDatabase db) {
        this(new StandardDatabase(db));
    }

    public RequestRecordDaoMaster(Database db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass( MyRequestRecordDao.class);
    }

    @Override
    public RequestRecordDaoSession newSession() {
        return new RequestRecordDaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }

    @Override
    public RequestRecordDaoSession newSession(IdentityScopeType type) {
        return new RequestRecordDaoSession(db, type, daoConfigMap);
    }

    /**
     * Calls {@link #createAllTables(Database, boolean)} in {@link #onCreate(Database)} -
     */
    public static abstract class OpenHelper extends DatabaseOpenHelper {
        public OpenHelper(Context context, String name) {
            super(context, name, SCHEMA_VERSION);
        }

        public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(Database db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }

    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends RequestRecordDaoMaster.OpenHelper {
        public DevOpenHelper(Context context, String name) {
            super(context, name);
        }

        public DevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }
}
