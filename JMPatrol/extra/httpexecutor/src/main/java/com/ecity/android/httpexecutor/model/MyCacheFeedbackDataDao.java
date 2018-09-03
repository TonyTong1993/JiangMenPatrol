package com.ecity.android.httpexecutor.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.ecity.android.httpexecutor.daosession.CacheFeedBackFormDaoSession;
import com.ecity.android.httpexecutor.daosession.RequestRecordDaoSession;
import com.ecity.android.httpexecutor.greendao.DaoSession;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

/**
 * @author yongzhan
 * @date 2017/12/26
 */
public class MyCacheFeedbackDataDao extends AbstractDao<CacheFeedbackData, Long> {
    public static final String TABLENAME = "CACHE_FEED_BACK_DATA";

    /**
     * Properties of entity RequestRecord.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property RecordId = new Property(0, Long.class, "recordId", true, "_id");
        public final static Property FailCount = new Property(1, int.class, "failCount", false, "FAIL_COUNT");
        public final static Property Battery = new Property(2, String.class, "battery", false, "BATTERY");
        public final static Property UserId = new Property(3, int.class, "userId", false, "USER_ID");
        public final static Property Time = new Property(4, String.class, "time", false, "TIME");
        public final static Property Status = new Property(5, int.class, "status", false, "STATUS");
        public final static Property TableStatus = new Property(6, int.class, "tableStatus", false, "TABLE_STATUS");
        public final static Property FileStatus = new Property(7, int.class, "fileStatus", false, "FILE_STATUS");
        public final static Property Tag = new Property(8, String.class, "tag", false, "TAG");
        public final static Property Url = new Property(9, String.class, "url", false, "URL");
        public final static Property Parameter = new Property(10, String.class, "parameter", false, "PARAMETER");
        public final static Property ResourceId = new Property(11, int.class, "resourceId", false, "RESOURCE_ID");
        public final static Property FileUrl = new Property(12, String.class, "fileUrl", false, "FILE_URL");
        public final static Property FileParameter = new Property(13, String.class, "fileParameter", false, "FILE_PARAMETER");
        public final static Property FilePath = new Property(14, String.class, "filePath", false, "File_PATH");
        public final static Property TableName = new Property(15, String.class, "tableName", false, "TABLE_NAME");
        public final static Property ReportType = new Property(16, int.class, "reportType", false, "REPORT_TYPE");
        public final static Property TableType = new Property(17, int.class, "tableType", false, "TABLE_TYPE");
        public final static Property column = new Property(18, String.class, "column", false, "COLUMN");

    }


    public MyCacheFeedbackDataDao(DaoConfig config) {
        super(config);
    }

    public MyCacheFeedbackDataDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    public MyCacheFeedbackDataDao(DaoConfig config, CacheFeedBackFormDaoSession daoSession) {
        super(config, daoSession);
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"CACHE_FEED_BACK_DATA\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: recordId
                "\"FAIL_COUNT\" INTEGER NOT NULL ," + // 1: failCount
                "\"BATTERY\" TEXT," + // 2: battery
                "\"USER_ID\" INTEGER NOT NULL ," + // 3: userid
                "\"TIME\" TEXT," + // 4: time
                "\"STATUS\" INTEGER NOT NULL ," + // 5: status
                "\"TABLE_STATUS\" INTEGER NOT NULL ," + // 6: tableStatus
                "\"FILE_STATUS\" INTEGER NOT NULL ," + // 7: fileStatus
                "\"TAG\" TEXT," + // 8: tag
                "\"URL\" TEXT," + // 9: url
                "\"PARAMETER\" TEXT," + // 10: parameter
                "\"RESOURCE_ID\" TEXT," + // 11: resourceId
                "\"FILE_URL\" TEXT," + // 12: fileUrl
                "\"FILE_PARAMETER\" TEXT," + // 13: fileParameter
                "\"FILE_PATH\" TEXT," + // 14: filePath
                "\"TABLE_NAME\" TEXT," + // 15: tableName
                "\"REPORT_TYPE\" INTEGER NOT NULL ," + // 16: reportType
                "\"TABLE_TYPE\" INTEGER NOT NULL ," + // 17: tableType
                "\"COLUMN\" TEXT);"); // 18: column
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CACHE_FEED_BACK_DATA\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CacheFeedbackData entity) {
        stmt.clearBindings();

        Long recordId = entity.getRecordId();
        if (recordId != null) {
            stmt.bindLong(1, recordId);
        }
        stmt.bindLong(2, entity.getFailCount());

        String battery = entity.getBattery();
        if (battery != null) {
            stmt.bindString(3, battery);
        }
        stmt.bindLong(4, entity.getUserid());

        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(5, time);
        }
        stmt.bindLong(6, entity.getStatus());
        stmt.bindLong(7, entity.getTableStatus());
        stmt.bindLong(8, entity.getFileStatus());

        String tag = entity.getTag();
        if (tag != null) {
            stmt.bindString(9, tag);
        }

        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(10, url);
        }

        String parameter = entity.getParameter();
        if (parameter != null) {
            stmt.bindString(11, parameter);
        }

        String resourceId = entity.getResourceId();
        if (resourceId != null) {
            stmt.bindString(12, resourceId);
        }

        String fileUrl = entity.getFileUrl();
        if (fileUrl != null) {
            stmt.bindString(13, fileUrl);
        }

        String fileParameter = entity.getFileParameter();
        if (fileParameter != null) {
            stmt.bindString(14, fileParameter);
        }

        String filePath = entity.getFilePath();
        if (filePath != null) {
            stmt.bindString(15, filePath);
        }

        String tableName = entity.getTableName();
        if (tableName != null) {
            stmt.bindString(16, tableName);
        }
        stmt.bindLong(17, entity.getReportType());
        stmt.bindLong(18, entity.getTableType());
        String column = entity.getColumn();
        if (column != null) {
            stmt.bindString(19, column);
        }

    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CacheFeedbackData entity) {
        stmt.clearBindings();

        Long recordId = entity.getRecordId();
        if (recordId != null) {
            stmt.bindLong(1, recordId);
        }
        stmt.bindLong(2, entity.getFailCount());

        String battery = entity.getBattery();
        if (battery != null) {
            stmt.bindString(3, battery);
        }
        stmt.bindLong(4, entity.getUserid());

        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(5, time);
        }
        stmt.bindLong(6, entity.getStatus());
        stmt.bindLong(7, entity.getTableStatus());
        stmt.bindLong(8, entity.getFileStatus());

        String tag = entity.getTag();
        if (tag != null) {
            stmt.bindString(9, tag);
        }

        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(10, url);
        }

        String parameter = entity.getParameter();
        if (parameter != null) {
            stmt.bindString(11, parameter);
        }

        String resourceId = entity.getResourceId();
        if (resourceId != null) {
            stmt.bindString(12, resourceId);
        }

        String fileUrl = entity.getFileUrl();
        if (fileUrl != null) {
            stmt.bindString(13, fileUrl);
        }

        String fileParameter = entity.getFileParameter();
        if (fileParameter != null) {
            stmt.bindString(14, fileParameter);
        }

        String filePath = entity.getFilePath();
        if (filePath != null) {
            stmt.bindString(15, filePath);
        }

        String tableName = entity.getTableName();
        if (tableName != null) {
            stmt.bindString(16, tableName);
        }
        stmt.bindLong(17, entity.getReportType());
        stmt.bindLong(18, entity.getTableType());
        String column = entity.getColumn();
        if (column != null) {
            stmt.bindString(19, column);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    @Override
    public CacheFeedbackData readEntity(Cursor cursor, int offset) {
        CacheFeedbackData entity = new CacheFeedbackData(
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // recordId
                cursor.getInt(offset + 1), // failCount
                cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // battery
                cursor.getInt(offset + 3), // userid
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // time
                cursor.getInt(offset + 5), // status
                cursor.getInt(offset + 6), // tableStatus
                cursor.getInt(offset + 7), // fileStatus
                cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // tag
                cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // url
                cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // parameter
                cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // resourceId
                cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // fileUrl
                cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // fileParameter
                cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // filePath
                cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // tableName
                cursor.getInt(offset + 16), // reportType
                cursor.getInt(offset + 17), // tableType
                cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18) // column

        );
        return entity;
    }

    @Override
    public void readEntity(Cursor cursor, CacheFeedbackData entity, int offset) {
        entity.setRecordId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFailCount(cursor.getInt(offset + 1));
        entity.setBattery(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUserid(cursor.getInt(offset + 3));
        entity.setTime(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setStatus(cursor.getInt(offset + 5));
        entity.setTableStatus(cursor.getInt(offset + 6));
        entity.setFileStatus(cursor.getInt(offset + 7));
        entity.setTag(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setUrl(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setParameter(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setResourceId(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setFileUrl(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setFileParameter(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setFilePath(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setTableName(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setReportType(cursor.getInt(offset + 16));
        entity.setTableType(cursor.getInt(offset + 17));
        entity.setColumn(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
    }

    @Override
    protected final Long updateKeyAfterInsert(CacheFeedbackData entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    @Override
    public Long getKey(CacheFeedbackData entity) {
        if (entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CacheFeedbackData entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
}
