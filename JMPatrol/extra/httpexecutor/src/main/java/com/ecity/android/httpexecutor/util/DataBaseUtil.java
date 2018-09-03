package com.ecity.android.httpexecutor.util;

import android.content.Context;

import com.ecity.android.httpexecutor.greendao.RequestRecordDao;
import com.ecity.android.httpexecutor.init.InitRequestRecordDB;
import com.ecity.android.httpexecutor.model.MyRequestRecordDao;
import com.ecity.android.httpexecutor.model.RequestRecord;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

import java.util.List;

/**
 * @author yongzhan
 * @date 2017/12/26
 */
public class DataBaseUtil {
    private static InitRequestRecordDB initRequestRecordDB;

    public static void init(Context context) {
        initRequestRecordDB = InitRequestRecordDB.getInstance(context);
    }

    public static void insert(RequestRecord record) {
        MyRequestRecordDao dao = initRequestRecordDB.getDaoSession().getDao();
        dao.insert(record);
    }

    public static void delete(String key) {
        MyRequestRecordDao dao = initRequestRecordDB.getDaoSession().getDao();
        /*RequestRecord requestRecord = dao.queryBuilder().where(MyRequestRecordDao.Properties.Id.eq(key.trim())).build().unique();
        dao.delete(requestRecord);*/
        List<RequestRecord> list = dao.queryBuilder().where(MyRequestRecordDao.Properties.Id.eq(key.trim())).list();
        if (ListUtil.isEmpty(list)) {
            return;
        }
        for (RequestRecord requestRecord : list) {
            dao.delete(requestRecord);
        }

        //dao.queryBuilder().where(MyRequestRecordDao.Properties.Id.eq(key.trim())).buildDelete();
    }

    public static void updateRecord(RequestRecord record) {
        MyRequestRecordDao dao = initRequestRecordDB.getDaoSession().getDao();
        dao.update(record);
    }

    public static RequestRecord queryOne(int id) {
        RequestRecord record = null;
        MyRequestRecordDao dao = initRequestRecordDB.getDaoSession().getDao();
        List<RequestRecord> list = dao.queryBuilder().where(MyRequestRecordDao.Properties.UserId.eq(id)).orderAsc(MyRequestRecordDao.Properties.FailedCount).list();
        if (list != null && list.size() > 0) {
            record = list.get(0);
        }
        return record;
    }

    public static RequestRecord queryOne(String id) {
        RequestRecord record = null;
        MyRequestRecordDao dao = initRequestRecordDB.getDaoSession().getDao();
        List<RequestRecord> list = dao.queryBuilder().where(MyRequestRecordDao.Properties.Id.eq(id.trim())).list();
        if (list != null && list.size() > 0) {
            record = list.get(0);
        }
        return record;
    }

    public static RequestRecord queryOne(int userid, int requestid) {
        RequestRecord record = null;
        MyRequestRecordDao dao = initRequestRecordDB.getDaoSession().getDao();
        List<RequestRecord> list = dao.queryBuilder().where(MyRequestRecordDao.Properties.UserId.eq(userid), RequestRecordDao.Properties.RequestId.eq(requestid)).list();
        if (list != null && list.size() > 0) {
            record = list.get(0);
        }
        return record;
    }

    public static List<RequestRecord> queryList(int userId, int requestId) {
        MyRequestRecordDao dao = initRequestRecordDB.getDaoSession().getDao();
        List<RequestRecord> list = dao.queryBuilder().where(MyRequestRecordDao.Properties.UserId.eq(userId), RequestRecordDao.Properties.RequestId.eq(requestId)).list();
        return list;
    }

}
