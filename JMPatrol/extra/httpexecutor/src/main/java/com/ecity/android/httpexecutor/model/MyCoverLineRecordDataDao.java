package com.ecity.android.httpexecutor.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.ecity.android.httpexecutor.daosession.CoverLineRecordDaoSession;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

/**
 * @author yongzhan
 * @date 2017/12/26
 */
public class MyCoverLineRecordDataDao extends AbstractDao<CoverLineRecordData, Long> {

    public static final String TABLENAME = "COVER_LINE_RECORD_DATA";

    /**
     * Properties of entity CoverLineRecordData.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property RecordId = new Property(0, Long.class, "recordId", true, "_id");
        public final static Property Id = new Property(1, String.class, "id", false, "ID");
        public final static Property UserId = new Property(2, int.class, "userId", false, "USER_ID");
        public final static Property CreatedOn = new Property(3, String.class, "createdOn", false, "CREATED_ON");
        public final static Property UploadedOn = new Property(4, String.class, "uploadedOn", false, "UPLOADED_ON");
        public final static Property FailedCount = new Property(5, int.class, "failedCount", false, "FAILED_COUNT");
        public final static Property UploadParameter = new Property(6, String.class, "uploadParameter", false, "UPLOAD_PARAMETER");
        public final static Property Url = new Property(7, String.class, "url", false, "URL");
        public final static Property RequestId = new Property(8, int.class, "requestId", false, "REQUEST_ID");
        public final static Property IsPost = new Property(9, boolean.class, "isPost", false, "IS_POST");
        public final static Property Uuid = new Property(10, String.class, "uuid", false, "UUID");
        public final static Property LineId = new Property(11, int.class, "lineId", false, "LINE_ID");
    }


    public MyCoverLineRecordDataDao(DaoConfig config) {
        super(config);
    }

    public MyCoverLineRecordDataDao(DaoConfig config, CoverLineRecordDaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COVER_LINE_RECORD_DATA\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: recordId
                "\"ID\" TEXT," + // 1: id
                "\"USER_ID\" INTEGER NOT NULL ," + // 2: userId
                "\"CREATED_ON\" TEXT," + // 3: createdOn
                "\"UPLOADED_ON\" TEXT," + // 4: uploadedOn
                "\"FAILED_COUNT\" INTEGER NOT NULL ," + // 5: failedCount
                "\"UPLOAD_PARAMETER\" TEXT," + // 6: uploadParameter
                "\"URL\" TEXT," + // 7: url
                "\"REQUEST_ID\" INTEGER NOT NULL ," + // 8: requestId
                "\"IS_POST\" INTEGER NOT NULL ," + // 9: isPost
                "\"UUID\" TEXT," + // 10: uuid
                "\"LINE_ID\" INTEGER NOT NULL );"); // 11: lineId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COVER_LINE_RECORD_DATA\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CoverLineRecordData entity) {
        stmt.clearBindings();

        Long recordId = entity.getRecordId();
        if (recordId != null) {
            stmt.bindLong(1, recordId);
        }

        String id = entity.getId();
        if (id != null) {
            stmt.bindString(2, id);
        }
        stmt.bindLong(3, entity.getUserId());

        String createdOn = entity.getCreatedOn();
        if (createdOn != null) {
            stmt.bindString(4, createdOn);
        }

        String uploadedOn = entity.getUploadedOn();
        if (uploadedOn != null) {
            stmt.bindString(5, uploadedOn);
        }
        stmt.bindLong(6, entity.getFailedCount());

        String uploadParameter = entity.getUploadParameter();
        if (uploadParameter != null) {
            stmt.bindString(7, uploadParameter);
        }

        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(8, url);
        }
        stmt.bindLong(9, entity.getRequestId());
        stmt.bindLong(10, entity.getIsPost() ? 1L: 0L);

        String uuid = entity.getUuid();
        if (uuid != null) {
            stmt.bindString(11, uuid);
        }
        stmt.bindLong(12, entity.getLineId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CoverLineRecordData entity) {
        stmt.clearBindings();

        Long recordId = entity.getRecordId();
        if (recordId != null) {
            stmt.bindLong(1, recordId);
        }

        String id = entity.getId();
        if (id != null) {
            stmt.bindString(2, id);
        }
        stmt.bindLong(3, entity.getUserId());

        String createdOn = entity.getCreatedOn();
        if (createdOn != null) {
            stmt.bindString(4, createdOn);
        }

        String uploadedOn = entity.getUploadedOn();
        if (uploadedOn != null) {
            stmt.bindString(5, uploadedOn);
        }
        stmt.bindLong(6, entity.getFailedCount());

        String uploadParameter = entity.getUploadParameter();
        if (uploadParameter != null) {
            stmt.bindString(7, uploadParameter);
        }

        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(8, url);
        }
        stmt.bindLong(9, entity.getRequestId());
        stmt.bindLong(10, entity.getIsPost() ? 1L: 0L);

        String uuid = entity.getUuid();
        if (uuid != null) {
            stmt.bindString(11, uuid);
        }
        stmt.bindLong(12, entity.getLineId());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    @Override
    public CoverLineRecordData readEntity(Cursor cursor, int offset) {
        CoverLineRecordData entity = new CoverLineRecordData( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // recordId
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // id
                cursor.getInt(offset + 2), // userId
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // createdOn
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // uploadedOn
                cursor.getInt(offset + 5), // failedCount
                cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // uploadParameter
                cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // url
                cursor.getInt(offset + 8), // requestId
                cursor.getShort(offset + 9) != 0, // isPost
                cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // uuid
                cursor.getInt(offset + 11) // lineId
        );
        return entity;
    }

    @Override
    public void readEntity(Cursor cursor, CoverLineRecordData entity, int offset) {
        entity.setRecordId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUserId(cursor.getInt(offset + 2));
        entity.setCreatedOn(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUploadedOn(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setFailedCount(cursor.getInt(offset + 5));
        entity.setUploadParameter(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setUrl(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setRequestId(cursor.getInt(offset + 8));
        entity.setIsPost(cursor.getShort(offset + 9) != 0);
        entity.setUuid(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setLineId(cursor.getInt(offset + 11));
    }

    @Override
    protected final Long updateKeyAfterInsert(CoverLineRecordData entity, long rowId) {
        entity.setRecordId(rowId);
        return rowId;
    }

    @Override
    public Long getKey(CoverLineRecordData entity) {
        if(entity != null) {
            return entity.getRecordId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CoverLineRecordData entity) {
        return entity.getRecordId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
}
