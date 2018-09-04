package com.ecity.cswatersupply.db;

import com.ecity.android.db.model.DBRecord;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPointPartBeanXtd;
import com.zzz.ecity.android.applibrary.database.ABaseDao;

public class PlanTaskPointPartDao extends ABaseDao<Z3PlanTaskPointPartBeanXtd> {
    public static final String TB_NAME = "PlanTaskPointPart";
    private static PlanTaskPointPartDao instance;
    public static PlanTaskPointPartDao getInstance(){
        if(null == instance){
            instance = new PlanTaskPointPartDao();
        }
        
        return instance;
    }
    private PlanTaskPointPartDao() {
    }

    @Override
    protected String getTableName() {
        return TB_NAME;
    }

    @Override
    protected boolean checkRecordType(DBRecord record) {
        return record != null && (record.getBean() instanceof Z3PlanTaskPointPartBeanXtd);
    }

    @Override
    protected Class<Z3PlanTaskPointPartBeanXtd> getBeanClass() {
        return Z3PlanTaskPointPartBeanXtd.class;
    }

    @Override
    protected String getCheckExistenceWhereBeforeInsert(Z3PlanTaskPointPartBeanXtd bean) {
        return "gid = " + bean.getGid();
    }
}
