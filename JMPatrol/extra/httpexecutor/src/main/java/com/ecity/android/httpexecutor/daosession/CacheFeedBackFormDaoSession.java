package com.ecity.android.httpexecutor.daosession;

import com.ecity.android.httpexecutor.model.CacheFeedbackData;
import com.ecity.android.httpexecutor.model.MyCacheFeedbackDataDao;
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
public class CacheFeedBackFormDaoSession extends AbstractDaoSession {
    private final DaoConfig daoConfig;
    private final MyCacheFeedbackDataDao dao;


    public CacheFeedBackFormDaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);
        daoConfig = daoConfigMap.get(MyCacheFeedbackDataDao.class).clone();
        daoConfig.initIdentityScope(type);
        dao = new MyCacheFeedbackDataDao(daoConfig, this);
        registerDao(CacheFeedbackData.class, dao);
    }


    public void clear() {
        daoConfig.clearIdentityScope();

    }

    public MyCacheFeedbackDataDao getDao() {
        return dao;
    }
}
