package com.ecity.cswatersupply.db;

import com.ecity.android.db.model.DBRecord;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskLinePartBeanXtd;
import com.zzz.ecity.android.applibrary.database.ABaseDao;

public class PlanTaskLinePartDao extends ABaseDao<Z3PlanTaskLinePartBeanXtd> {
    public static final String TB_NAME = "PlanTaskLinePart";
    private static PlanTaskLinePartDao instance;
    public static PlanTaskLinePartDao getInstance(){
        if(null == instance){
            instance = new PlanTaskLinePartDao();
        }
        
        return instance;
    }
    private PlanTaskLinePartDao() {
    }

    @Override
    protected String getTableName() {
        return TB_NAME;
    }

    @Override
    protected boolean checkRecordType(DBRecord record) {
        return record != null && (record.getBean() instanceof Z3PlanTaskLinePartBeanXtd);
    }

    @Override
    protected Class<Z3PlanTaskLinePartBeanXtd> getBeanClass() {
        return Z3PlanTaskLinePartBeanXtd.class;
    }

    @Override
    protected String getCheckExistenceWhereBeforeInsert(Z3PlanTaskLinePartBeanXtd bean) {
        return "gid = " + bean.getGid();
    }
}
