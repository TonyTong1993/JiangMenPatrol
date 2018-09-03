package com.ecity.android.httpexecutor.uploadrecord;

import com.ecity.android.httpexecutor.model.RequestRecord;
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
public class RequestRecordPersistenceManager {
    private static RequestRecordPersistenceManager instance;

    static {
        instance = new RequestRecordPersistenceManager();
    }

    public static RequestRecordPersistenceManager getInstance() {
        return instance = new RequestRecordPersistenceManager();
    }

    public void save(List<UploadRecord> list){
        for(UploadRecord record:list){
            RequestRecord bean = model2DbBean(record);
            /**
             * 数据库操作类需要先初始化
             */
            DataBaseUtil.insert(bean);
        }
    }
    public void delete(String recordId) {
        DataBaseUtil.delete(recordId);
    }
    public void increaseFailCount(String recordId) {
        RequestRecord bean = DataBaseUtil.queryOne(recordId);
        bean.setFailedCount(bean.getFailedCount() + 1);
        DataBaseUtil.updateRecord(bean);
    }
    public List<UploadRecord> readList(int userId, int requestId) {
        List<RequestRecord> beans = DataBaseUtil.queryList( userId , requestId);
        List<UploadRecord> models = new ArrayList<UploadRecord>();
        for (RequestRecord bean : beans) {
            UploadRecord model = dbBean2Model(bean);
            models.add(model);
        }
        return models;
    }
    public UploadRecord readOne(int userId) {
       RequestRecord bean = DataBaseUtil.queryOne(userId);
        return (bean == null) ? null : dbBean2Model(bean);
    }

    public UploadRecord readOne(int userId, int requestId) {
        RequestRecord bean = DataBaseUtil.queryOne( userId , requestId);
        return (bean == null) ? null : dbBean2Model(bean);
    }

    private RequestRecord model2DbBean(UploadRecord record) {
        String params = new JSONObject(record.getUploadParameter()).toString();
        RequestRecord record1 = new RequestRecord();
        record1.setId(record.getId());
        record1.setUserId(record.getUserId());
        record1.setCreatedOn(record.getCreatedOn());
        record1.setUploadedOn(record.getUploadedOn());
        record1.setUploadParameter(params.toString());
        record1.setFailedCount(record.getFailedCount());
        record1.setUrl(record.getUrl());
        record1.setRequestId(record.getRequestId());
        record1.setIsPost(record.isPost());
        return record1;
    }
    private UploadRecord dbBean2Model(RequestRecord bean) {
        JSONObject json = null;
        try {
            json = new JSONObject(bean.getUploadParameter());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Map<String, String> map = json2Map(json);
        return new UploadRecord(bean.getId(), bean.getUserId(), bean.getCreatedOn(), bean.getUploadedOn(), map, bean.getFailedCount(), bean.getUrl(), bean.getRequestId(), bean.getIsPost());
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
