package com.ecity.android.httpexecutor.cachefeedbackrecord;

import com.ecity.android.httpexecutor.model.CacheFeedbackData;
import com.ecity.android.httpexecutor.model.RequestRecord;
import com.ecity.android.httpexecutor.uploadrecord.UploadRecord;
import com.ecity.android.httpexecutor.util.CacheFeedbackDataBaseUtil;
import com.ecity.android.httpexecutor.util.DataBaseUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author yongzhan
 * @date 2017/12/26
 */
public class CacheFeedbackRecordPersistenceManager {
    private static CacheFeedbackRecordPersistenceManager instance;

    static {
        instance = new CacheFeedbackRecordPersistenceManager();
    }

    public static CacheFeedbackRecordPersistenceManager getInstance() {
        return instance = new CacheFeedbackRecordPersistenceManager();
    }

    public void save(List<CacheFeedbackRecord> list) {
        for (CacheFeedbackRecord record : list) {
            CacheFeedbackData bean = model2DbBean(record);
            /**
             * 数据库操作类需要先初始化
             */
            CacheFeedbackDataBaseUtil.insert(bean);
        }
    }

    public void delete(String recordId) {
        CacheFeedbackDataBaseUtil.delete(recordId);
    }

    public void updateRecord(CacheFeedbackData record) {
        CacheFeedbackDataBaseUtil.updateRecord(record);
    }

    public void increaseFailCount(String recordId) {
        CacheFeedbackData bean = CacheFeedbackDataBaseUtil.queryOne(recordId);
        bean.setFailCount(bean.getFailCount() + 1);
        CacheFeedbackDataBaseUtil.updateRecord(bean);
    }

    public List<CacheFeedbackData> readList(int userId, String status) {
        List<CacheFeedbackData> beans = CacheFeedbackDataBaseUtil.queryList(userId, status);
        /*List<CacheFeedbackRecord> models = new ArrayList<CacheFeedbackRecord>();
        for (CacheFeedbackData bean : beans) {
            CacheFeedbackRecord model = dbBean2Model(bean);
            models.add(model);
        }
        return models;*/
        return beans;
    }

    public CacheFeedbackData readOne(int userId) {
        return CacheFeedbackDataBaseUtil.queryOne(userId);
        //return (bean == null) ? null : dbBean2Model(bean);
    }

    public CacheFeedbackRecord readOne(int userId, int requestId) {
        CacheFeedbackData bean = CacheFeedbackDataBaseUtil.queryOne(userId, requestId);
        return (bean == null) ? null : dbBean2Model(bean);
    }

    private CacheFeedbackData model2DbBean(CacheFeedbackRecord record) {
        CacheFeedbackData data = new CacheFeedbackData();
        data.setFailCount(record.getFailCount());
        data.setBattery(record.getBattery());
        data.setUserid(record.getUserid());
        data.setTime(record.getTime());
        data.setStatus(record.getStatus());
        data.setTableStatus(record.getTableStatus());
        data.setFileStatus(record.getFileStatus());
        data.setTag(record.getTag());
        data.setUrl(record.getUrl());
        data.setParameter(record.getParameter());
        data.setResourceId(record.getResourceId());
        data.setFileUrl(record.getFileUrl());
        data.setFileParameter(record.getFileParameter());
        data.setFilePath(record.getFilePath());
        data.setTableName(record.getTableName());
        data.setReportType(record.getReportType());
        data.setTableType(record.getTableType());
        data.setColumn(record.getColumn());

        return data;
    }

    private CacheFeedbackRecord dbBean2Model(CacheFeedbackData bean) {
        return new CacheFeedbackRecord(String.valueOf(bean.getId()), bean.getFailCount(), bean.getBattery(), bean.getUserid(), bean.getTime(), bean.getStatus(), bean.getTableStatus(), bean.getFileStatus(), bean.getTag(), bean.getUrl(), bean.getParameter(), bean.getResourceId(), bean.getFileUrl(), bean.getFileParameter(), bean.getFilePath(), bean.getTableName(), bean.getReportType(), bean.getTableType(), bean.getColumn());
    }

    public static Map<String, String> json2Map(JSONObject json) {
        Map<String, String> map = new HashMap<String, String>(8);

        Iterator<String> iterator = json.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            map.put(key, json.optString(key));
        }

        return map;
    }
}
