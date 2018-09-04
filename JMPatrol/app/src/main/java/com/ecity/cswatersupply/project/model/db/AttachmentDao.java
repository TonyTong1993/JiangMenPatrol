package com.ecity.cswatersupply.project.model.db;

import com.ecity.android.db.model.DBRecord;
import com.zzz.ecity.android.applibrary.database.ABaseDao;

/**
 * @author Ma Qianli
 *
 */
public class AttachmentDao extends ABaseDao<AttachmentBeanXtd> {
    private static AttachmentDao instance;
    public static final String TABLE_NAME = "Attachment";

    static {
        instance = new AttachmentDao();
    }

    public static AttachmentDao getInstance() {
        return instance;
    }

    @Override
    protected boolean checkRecordType(DBRecord record) {
        return record != null && (record.getBean() instanceof AttachmentBeanXtd);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public void save(AttachmentBeanXtd bean) {
        insert(bean);
    }

    /**
     * @param uniqueName
     * @return localPath of document identified by uniqueName
     */
    public String getLocalPath(String uniqueName) {
        String where = " where uniqueName = ? ";
        String path = null;
        AttachmentBeanXtd bean = queryOne(where, new String[] { uniqueName });
        if (bean != null) {
            path = bean.getLocalPath();
        }

        return path;
    }

    public void saveLocalPath(String uniqueName, String path) {
        String sql = "update " + TABLE_NAME + " set localPath = '" + path + "' where uniqueName = '" + uniqueName + "'";
        execute(sql);
    }

    /**
     * @param uniqueName
     * @param downloadedOn
     */
    public void markAsDownloaded(String uniqueName, String downloadedOn) {
        String sql = "update " + TABLE_NAME + " set status = " + AttachmentBeanXtd.STATUS_DOWNLOADED + " and downloadedOn = '" + downloadedOn + "'  where uniqueName = '"
                + uniqueName + "'";
        execute(sql);
    }

    public void updateStatus(String uniqueName, String status) {
        String sql = "update " + TABLE_NAME + " set status = " + status + " where uniqueName = '" + uniqueName + "'";
        execute(sql);
    }

    public void updateDownloadTime(String uniqueName, String downloadedOn) {
        String sql = "update " + TABLE_NAME + " set downloadedOn = '" + downloadedOn + "' where uniqueName = '" + uniqueName + "'";
        execute(sql);
    }

    @Override
    protected Class<AttachmentBeanXtd> getBeanClass() {
        return AttachmentBeanXtd.class;
    }

    @Override
    protected String getCheckExistenceWhereBeforeInsert(AttachmentBeanXtd bean) {
        // TODO Auto-generated method stub
        return null;
    }
}
