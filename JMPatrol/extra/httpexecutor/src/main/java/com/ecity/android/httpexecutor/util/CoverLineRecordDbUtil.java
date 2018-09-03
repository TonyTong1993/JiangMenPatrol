package com.ecity.android.httpexecutor.util;

import android.content.Context;

import com.ecity.android.httpexecutor.init.InitCoverLineRecordDataDb;
import com.ecity.android.httpexecutor.model.CoverLineRecordData;
import com.ecity.android.httpexecutor.model.MyCoverLineRecordDataDao;
import com.enn.sop.global.GlobalFunctionInfo;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

import java.util.List;

/**
 * @author xiaobei
 * @date 2018/7/23
 */
public class CoverLineRecordDbUtil {
    private static InitCoverLineRecordDataDb initCoverLineRecordDataDB;

    public static void init(Context context) {
        initCoverLineRecordDataDB = InitCoverLineRecordDataDb.getInstance(context);
    }

    public static void insert(CoverLineRecordData record) {
        MyCoverLineRecordDataDao dao = initCoverLineRecordDataDB.getDaoSession().getDao();
        dao.insert(record);
    }

    public static void delete(String key) {
        MyCoverLineRecordDataDao dao = initCoverLineRecordDataDB.getDaoSession().getDao();
        List<CoverLineRecordData> list = dao.queryBuilder().where(MyCoverLineRecordDataDao.Properties.RecordId.eq(key.trim())).list();
        if (ListUtil.isEmpty(list)) {
            return;
        }
        for (CoverLineRecordData coverLineRecordData : list) {
            dao.delete(coverLineRecordData);
        }

    }

    public static void deleteByLineId(int lineId) {
        MyCoverLineRecordDataDao dao = initCoverLineRecordDataDB.getDaoSession().getDao();
        List<CoverLineRecordData> list = dao.queryBuilder().where(MyCoverLineRecordDataDao.Properties.LineId.eq(lineId)).list();
        if (ListUtil.isEmpty(list)) {
            return;
        }
        for (CoverLineRecordData cacheFeedbackData : list) {
            dao.delete(cacheFeedbackData);
        }
    }

    public static void updateRecord(CoverLineRecordData record) {
        MyCoverLineRecordDataDao dao = initCoverLineRecordDataDB.getDaoSession().getDao();
        dao.update(record);
    }

    public static CoverLineRecordData queryOne(int id) {
        CoverLineRecordData record = null;
        if (null == initCoverLineRecordDataDB) {
            initCoverLineRecordDataDB = InitCoverLineRecordDataDb.getInstance(GlobalFunctionInfo.getInstance().getApplication());
        }
        MyCoverLineRecordDataDao dao = initCoverLineRecordDataDB.getDaoSession().getDao();
        List<CoverLineRecordData> list = dao.queryBuilder().where(MyCoverLineRecordDataDao.Properties.UserId.eq(id)).orderAsc(MyCoverLineRecordDataDao.Properties.FailedCount).list();
        if (list != null && list.size() > 0) {
            record = list.get(0);
        }
        return record;
    }

    public static CoverLineRecordData queryOne(String id) {
        CoverLineRecordData record = null;
        MyCoverLineRecordDataDao dao = initCoverLineRecordDataDB.getDaoSession().getDao();
        List<CoverLineRecordData> list = dao.queryBuilder().where(MyCoverLineRecordDataDao.Properties.RecordId.eq(id.trim())).list();
        if (list != null && list.size() > 0) {
            record = list.get(0);
        }
        return record;
    }

    public static CoverLineRecordData queryOneByLineId(int id) {
        CoverLineRecordData record = null;
        MyCoverLineRecordDataDao dao = initCoverLineRecordDataDB.getDaoSession().getDao();
        List<CoverLineRecordData> list = dao.queryBuilder().where(MyCoverLineRecordDataDao.Properties.LineId.eq(id)).list();
        if (list != null && list.size() > 0) {
            record = list.get(0);
        }
        return record;
    }

    public static CoverLineRecordData queryOneByUuid(String id) {
        CoverLineRecordData record = null;
        MyCoverLineRecordDataDao dao = initCoverLineRecordDataDB.getDaoSession().getDao();
        List<CoverLineRecordData> list = dao.queryBuilder().where(MyCoverLineRecordDataDao.Properties.Uuid.eq(id.trim())).list();
        if (list != null && list.size() > 0) {
            record = list.get(0);
        }
        return record;
    }

    public static List<CoverLineRecordData> queryRecordByLineId(int id) {
        MyCoverLineRecordDataDao dao = initCoverLineRecordDataDB.getDaoSession().getDao();
        List<CoverLineRecordData> list = dao.queryBuilder().where(MyCoverLineRecordDataDao.Properties.LineId.eq(id)).list();

        return list;
    }

    public static CoverLineRecordData queryOne(int userid, int requestid) {
        CoverLineRecordData record = null;
        MyCoverLineRecordDataDao dao = initCoverLineRecordDataDB.getDaoSession().getDao();
        List<CoverLineRecordData> list = dao.queryBuilder().where(MyCoverLineRecordDataDao.Properties.UserId.eq(userid)).list();
        if (list != null && list.size() > 0) {
            record = list.get(0);
        }
        return record;
    }

    public static List<CoverLineRecordData> queryList(int userId, String status) {
        if (null == initCoverLineRecordDataDB) {
            initCoverLineRecordDataDB = InitCoverLineRecordDataDb.getInstance(GlobalFunctionInfo.getInstance().getApplication());
        }
        MyCoverLineRecordDataDao dao = initCoverLineRecordDataDB.getDaoSession().getDao();
        List<CoverLineRecordData> list = dao.queryBuilder().where(MyCoverLineRecordDataDao.Properties.UserId.eq(userId)).list();
        return list;
    }
}
