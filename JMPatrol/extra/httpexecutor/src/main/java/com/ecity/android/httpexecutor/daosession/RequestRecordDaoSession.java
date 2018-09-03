package com.ecity.android.httpexecutor.daosession;

import com.ecity.android.httpexecutor.model.MyRequestRecordDao;
import com.ecity.android.httpexecutor.model.RequestRecord;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

/**
 * @author yongzhan
 * @date 2017/12/26
 */
public class RequestRecordDaoSession extends AbstractDaoSession {
    private final DaoConfig daoConfig;
    private final MyRequestRecordDao dao;


    public RequestRecordDaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap){
        super(db);
        daoConfig = daoConfigMap.get(MyRequestRecordDao.class).clone();
        daoConfig.initIdentityScope(type);
        dao = new MyRequestRecordDao(daoConfig, this);
        registerDao(RequestRecord.class, dao);
    }



    public void clear() {
        daoConfig.clearIdentityScope();

    }

    public MyRequestRecordDao getDao() {
        return dao;
    }
}
