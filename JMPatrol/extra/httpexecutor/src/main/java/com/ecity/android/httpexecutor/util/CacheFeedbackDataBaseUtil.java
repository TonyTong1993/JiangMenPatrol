package com.ecity.android.httpexecutor.util;

import android.content.Context;

import com.ecity.android.httpexecutor.cachefeedbackrecord.CacheFeedbackRecord;
import com.ecity.android.httpexecutor.greendao.RequestRecordDao;
import com.ecity.android.httpexecutor.init.InitCacheFeedbackDataDB;
import com.ecity.android.httpexecutor.init.InitRequestRecordDB;
import com.ecity.android.httpexecutor.model.CacheFeedbackData;
import com.ecity.android.httpexecutor.model.MyCacheFeedbackDataDao;
import com.ecity.android.httpexecutor.model.MyRequestRecordDao;
import com.ecity.android.httpexecutor.model.RequestRecord;
import com.enn.sop.global.GlobalFunctionInfo;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

import java.util.List;

/**
 * @author yongzhan
 * @date 2017/12/26
 */
public class CacheFeedbackDataBaseUtil {
    private static InitCacheFeedbackDataDB initCacheFeedbackDataDB;

    public static void init(Context context) {
        initCacheFeedbackDataDB = InitCacheFeedbackDataDB.getInstance(context);
    }

    public static void insert(CacheFeedbackData record) {
        MyCacheFeedbackDataDao dao = initCacheFeedbackDataDB.getDaoSession().getDao();
        dao.insert(record);
    }

    public static void delete(String key) {
        MyCacheFeedbackDataDao dao = initCacheFeedbackDataDB.getDaoSession().getDao();
        List<CacheFeedbackData> list = dao.queryBuilder().where(MyCacheFeedbackDataDao.Properties.RecordId.eq(key.trim())).list();
        if (ListUtil.isEmpty(list)) {
            return;
        }
        for (CacheFeedbackData cacheFeedbackData : list) {
            dao.delete(cacheFeedbackData);
        }

    }

    public static void updateRecord(CacheFeedbackData record) {
        MyCacheFeedbackDataDao dao = initCacheFeedbackDataDB.getDaoSession().getDao();
        dao.update(record);
    }

    public static CacheFeedbackData queryOne(int id) {
        CacheFeedbackData record = null;
        if (null == initCacheFeedbackDataDB) {
            initCacheFeedbackDataDB = InitCacheFeedbackDataDB.getInstance(GlobalFunctionInfo.getInstance().getApplication());
        }
        MyCacheFeedbackDataDao dao = initCacheFeedbackDataDB.getDaoSession().getDao();
        List<CacheFeedbackData> list = dao.queryBuilder().where(MyCacheFeedbackDataDao.Properties.UserId.eq(id)).orderAsc(MyCacheFeedbackDataDao.Properties.FailCount).list();
        if (list != null && list.size() > 0) {
            record = list.get(0);
        }
        return record;
    }

    public static CacheFeedbackData queryOne(String id) {
        CacheFeedbackData record = null;
        MyCacheFeedbackDataDao dao = initCacheFeedbackDataDB.getDaoSession().getDao();
        List<CacheFeedbackData> list = dao.queryBuilder().where(MyCacheFeedbackDataDao.Properties.RecordId.eq(id.trim())).list();
        if (list != null && list.size() > 0) {
            record = list.get(0);
        }
        return record;
    }

    public static CacheFeedbackData queryOne(int userid, int requestid) {
        CacheFeedbackData record = null;
        MyCacheFeedbackDataDao dao = initCacheFeedbackDataDB.getDaoSession().getDao();
        List<CacheFeedbackData> list = dao.queryBuilder().where(MyCacheFeedbackDataDao.Properties.UserId.eq(userid)).list();
        if (list != null && list.size() > 0) {
            record = list.get(0);
        }
        return record;
    }

    public static List<CacheFeedbackData> queryList(int userId, String status) {
        if (null == initCacheFeedbackDataDB) {
            initCacheFeedbackDataDB = InitCacheFeedbackDataDB.getInstance(GlobalFunctionInfo.getInstance().getApplication());
        }
        MyCacheFeedbackDataDao dao = initCacheFeedbackDataDB.getDaoSession().getDao();
        List<CacheFeedbackData> list = dao.queryBuilder().where(MyCacheFeedbackDataDao.Properties.UserId.eq(userId), MyCacheFeedbackDataDao.Properties.Status.eq(status)).orderAsc(MyCacheFeedbackDataDao.Properties.Time).list();
        return list;
    }

}
