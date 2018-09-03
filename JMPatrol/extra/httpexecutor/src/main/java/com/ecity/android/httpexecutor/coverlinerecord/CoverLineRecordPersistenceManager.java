package com.ecity.android.httpexecutor.coverlinerecord;

import com.ecity.android.httpexecutor.model.CoverLineRecordData;
import com.ecity.android.httpexecutor.util.CoverLineRecordDbUtil;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

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
public class CoverLineRecordPersistenceManager {
    private static CoverLineRecordPersistenceManager instance;

    static {
        instance = new CoverLineRecordPersistenceManager();
    }

    public static CoverLineRecordPersistenceManager getInstance() {
        return instance = new CoverLineRecordPersistenceManager();
    }

    public void save(List<CoverLineRecord> list) {
        for (CoverLineRecord record : list) {
            CoverLineRecordData bean = model2DbBean(record);
            /**
             * 数据库操作类需要先初始化
             */
            CoverLineRecordDbUtil.insert(bean);
        }
    }

    public void delete(String recordId) {
        CoverLineRecordDbUtil.delete(recordId);
    }

    public void updateRecord(CoverLineRecordData record) {
        CoverLineRecordDbUtil.updateRecord(record);
    }

    public void increaseFailCount(String recordId) {
        CoverLineRecordData bean = CoverLineRecordDbUtil.queryOne(recordId);
        bean.setFailedCount(bean.getFailedCount() + 1);
        CoverLineRecordDbUtil.updateRecord(bean);
    }

    public List<CoverLineRecordData> readList(int userId, String status) {
        List<CoverLineRecordData> beans = CoverLineRecordDbUtil.queryList(userId, status);

        return beans;
    }

    public CoverLineRecord readOne(int userId) {
        CoverLineRecordData bean = CoverLineRecordDbUtil.queryOne(userId);
        return (bean == null) ? null : dbBean2Model(bean);
    }

    public CoverLineRecord readOneByLineId(int lineId) {
        CoverLineRecordData bean = CoverLineRecordDbUtil.queryOneByLineId(lineId);
        return (bean == null) ? null : dbBean2Model(bean);
    }

    public CoverLineRecord readOneByUuid(String uuid) {
        CoverLineRecordData bean = CoverLineRecordDbUtil.queryOneByUuid(uuid);
        return (bean == null) ? null : dbBean2Model(bean);
    }

    public List<CoverLineRecord> readRecordsByLineId(int lineId) {
        List<CoverLineRecordData> beans = CoverLineRecordDbUtil.queryRecordByLineId(lineId);
        if (ListUtil.isEmpty(beans)) {
            return null;
        }
        List<CoverLineRecord> records = new ArrayList<>();
        for (CoverLineRecordData data : beans) {
            records.add(dbBean2Model(data));
        }
        return records;
    }

    public CoverLineRecord readOne(int userId, int requestId) {
        CoverLineRecordData bean = CoverLineRecordDbUtil.queryOne(userId, requestId);
        return (bean == null) ? null : dbBean2Model(bean);
    }

    private CoverLineRecordData model2DbBean(CoverLineRecord record) {
        CoverLineRecordData data = new CoverLineRecordData();
        String params = new JSONObject(record.getUploadParameter()).toString();
        data.setLineId(record.getLineId());
        data.setUuid(record.getUuid());
        data.setFailedCount(record.getFailedCount());
        data.setPost(record.isPost());
        data.setCreatedOn(record.getCreatedOn());
        data.setId(record.getId());
        data.setUserId(record.getUserId());
        data.setUrl(record.getUrl());
        data.setRequestId(record.getRequestId());
        data.setUploadParameter(params.toString());
        data.setUploadedOn(record.getUploadedOn());

        return data;
    }

    private CoverLineRecord dbBean2Model(CoverLineRecordData bean) {
        JSONObject json = null;
        try {
            json = new JSONObject(bean.getUploadParameter());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Map<String, String> map = json2Map(json);
        return new CoverLineRecord(String.valueOf(bean.getRecordId()), bean.getUserId(), bean.getCreatedOn(), bean.getUploadedOn(), bean.getFailedCount(), map, bean.getUrl(), bean.getRequestId(), bean.getIsPost(), bean.getUuid(), bean.getLineId());
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
