package com.ecity.cswatersupply.db;

import com.ecity.android.db.model.DBRecord;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskBeanXtd;
import com.zzz.ecity.android.applibrary.database.ABaseDao;

public class PlanTaskDao extends ABaseDao<Z3PlanTaskBeanXtd> {
    public static final String TB_NAME = "plantask";
    private static PlanTaskDao instance;
    public static PlanTaskDao getInstance(){
        if(null == instance){
            instance = new PlanTaskDao();
        }
        
        return instance;
    }
    private PlanTaskDao() {
    }

    @Override
    protected String getTableName() {
        return TB_NAME;
    }

    @Override
    protected boolean checkRecordType(DBRecord record) {
        return record != null && (record.getBean() instanceof Z3PlanTaskBeanXtd);
    }

    @Override
    protected Class<Z3PlanTaskBeanXtd> getBeanClass() {
        return Z3PlanTaskBeanXtd.class;
    }

    @Override
    protected String getCheckExistenceWhereBeforeInsert(Z3PlanTaskBeanXtd bean) {
        return "taskid = " + bean.getTaskid();
    }
}
