package com.ecity.cswatersupply.workorder.data;

import java.io.Serializable;

import android.content.Context;
import android.os.AsyncTask;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.task.IExecuteAfterTaskDo;
import com.ecity.cswatersupply.task.ReadCacheTask;
import com.ecity.cswatersupply.task.SaveCacheTask;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrderList;
import com.ecity.cswatersupply.workorder.presenter.WorkOrderPresenter;
import com.squareup.leakcanary.watcher.Preconditions;

/**
 * 本地数据资源 负责本地缓存的存取
 * 
 * @author gaokai
 *
 */
public class TasksLocalDataSource implements TasksDataSource {
    private Context mContext;
    private static TasksLocalDataSource INSTANCE = null;
    private AsyncTask<Void, Void, Void> mSaveCacheTask = null;
    private AsyncTask<String, Void, Serializable> mReadCacheTask = null;

    public TasksLocalDataSource(Context context) {
        this.mContext = context;
        Preconditions.checkNotNull(context, context.getClass().getName());
    }

    public static TasksDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TasksLocalDataSource(context);
        }
        return INSTANCE;
    }

    private void cancelReadCacheTask() {
        if (mReadCacheTask != null) {
            mReadCacheTask.cancel(true);
            mReadCacheTask = null;
        }
    }

    private void cancelSaveCacheTask() {
        if (mSaveCacheTask != null) {
            mSaveCacheTask.cancel(true);
            mSaveCacheTask = null;
        }
    }

    @Override
    public void getTask(String id) {
        cancelReadCacheTask();
        mReadCacheTask = new ReadCacheTask(mContext, iExecuteAfterTaskDo).execute(id);
    }

    @Override
    public void saveData(String id, WorkOrderList data) {
        cancelSaveCacheTask();
        mSaveCacheTask = new SaveCacheTask(mContext, data, id).execute();
    }

    IExecuteAfterTaskDo iExecuteAfterTaskDo = new IExecuteAfterTaskDo() {

        @Override
        public void executeOnTaskSuccess(Serializable result) {
            EventBusUtil.post(new UIEvent(UIEventStatus.ON_LOAD_DATA_SUCCESS, result));
        }

        @Override
        public void executeOnTaskFinish() {
            EventBusUtil.post(new UIEvent(UIEventStatus.ON_LOAD_DATA_FINISH));
        }

        @Override
        public void executeTaskError() {
            String msg = ResourceUtil.getStringById(R.string.read_cache_error);
            EventBusUtil.post(new UIEvent(UIEventStatus.ON_LOAD_DATA_ERROR, msg, WorkOrderPresenter.class));
        }
    };
}
