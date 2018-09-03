package com.ecity.android.httpexecutor.daosession;

import com.ecity.android.httpexecutor.model.CoverLineRecordData;
import com.ecity.android.httpexecutor.model.MyCoverLineRecordDataDao;

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
public class CoverLineRecordDaoSession extends AbstractDaoSession {
    private final DaoConfig daoConfig;
    private final MyCoverLineRecordDataDao dao;


    public CoverLineRecordDaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);
        daoConfig = daoConfigMap.get(MyCoverLineRecordDataDao.class).clone();
        daoConfig.initIdentityScope(type);
        dao = new MyCoverLineRecordDataDao(daoConfig, this);
        registerDao(CoverLineRecordData.class, dao);
    }


    public void clear() {
        daoConfig.clearIdentityScope();

    }

    public MyCoverLineRecordDataDao getDao() {
        return dao;
    }
}
