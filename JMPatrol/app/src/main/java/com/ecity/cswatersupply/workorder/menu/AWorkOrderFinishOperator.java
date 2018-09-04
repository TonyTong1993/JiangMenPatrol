package com.ecity.cswatersupply.workorder.menu;

import java.io.Serializable;

import android.os.AsyncTask;

import com.ecity.cswatersupply.model.IListEntity;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.task.IExecuteAfterTaskDo;
import com.ecity.cswatersupply.task.ReadCacheTask;
import com.ecity.cswatersupply.task.SaveCacheTask;

public abstract class AWorkOrderFinishOperator extends AWorkOrderFinishCommonOperator {

    protected String gid;
    private IExecuteAfterTaskDo iExecuteAfterTaskDo;
    public AsyncTask<String, Void, Serializable> mReadCacheTask = null;
    public AsyncTask<Void, Void, Void> mSaveCacheTask = null;

    public void saveCacheData(IListEntity<InspectItem> datas) {
        cancelSaveCacheTask();
        mSaveCacheTask = new SaveCacheTask(getActivity(), datas, getCacheKey()).execute();
    }

    /**
     * 读取缓存的的检查项页面。当网络请求失败时，从本地读取缓存的检查项模板，供用户填写。这个方法不是加载缓存的检查项的值。
     */
    public void readCacheData() {
        String key = getCacheKey();
        cancelReadCacheTask();
        mReadCacheTask = new ReadCacheTask(getActivity(), iExecuteAfterTaskDo).execute(key);
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

    public void setIExecutoAfterTaskDo(IExecuteAfterTaskDo iExecuteAfterTaskDo) {
        this.iExecuteAfterTaskDo = iExecuteAfterTaskDo;
    }

    public abstract String getCacheKey();
}
