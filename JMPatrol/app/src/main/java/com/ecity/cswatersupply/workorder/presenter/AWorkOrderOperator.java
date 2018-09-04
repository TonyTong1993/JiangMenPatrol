package com.ecity.cswatersupply.workorder.presenter;

import java.io.Serializable;

import android.os.AsyncTask;

import com.ecity.cswatersupply.menu.ACommonReportOperator1;
import com.ecity.cswatersupply.model.IListEntity;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.task.IExecuteAfterTaskDo;
import com.ecity.cswatersupply.task.ReadCacheTask;
import com.ecity.cswatersupply.task.SaveCacheTask;

/**
 * 工单上报通用操作类
 * 带缓存检查项
 * @author gaokai
 *
 */
public abstract class AWorkOrderOperator extends ACommonReportOperator1{
    protected String gid;
    private IExecuteAfterTaskDo iExecuteAfterTaskDo;
    public AsyncTask<String, Void, Serializable> mReadCacheTask = null;
    public AsyncTask<Void, Void, Void> mSaveCacheTask = null;
    
    public void saveCacheData(IListEntity<InspectItem> datas) {
        cancelSaveCacheTask();
        // 缓存数据
        mSaveCacheTask = new SaveCacheTask(getActivity(), datas, getCacheKey())
                .execute();       
    }

    public void readCacheData() {
        String key = getCacheKey();
        cancelReadCacheTask();
        mReadCacheTask = new ReadCacheTask(getActivity(), iExecuteAfterTaskDo)
                .execute(key);
    }
    
    public void cancelSaveCacheTask() {
        if (mSaveCacheTask != null) {
            mSaveCacheTask.cancel(true);
            mSaveCacheTask = null;
        }
    }

    public void cancelReadCacheTask() {
        if (mReadCacheTask != null) {
            mReadCacheTask.cancel(true);
            mReadCacheTask = null;
        }
    }
    
    public void setIExecutoAfterTaskDo(IExecuteAfterTaskDo iExecuteAfterTaskDo){
        this.iExecuteAfterTaskDo = iExecuteAfterTaskDo;
    } 
    
    public abstract String getCacheKey();
}
