package com.ecity.cswatersupply.task;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ecity.cswatersupply.utils.CacheManager;

public class ReadCacheTask extends AsyncTask<String, Void, Serializable> {
    private final WeakReference<Context> mContext;
    private IExecuteAfterTaskDo iExecuteAfterTaskDo;

    public ReadCacheTask(Context context, IExecuteAfterTaskDo iExecuteAfterTaskDo) {
        mContext = new WeakReference<Context>(context);
        this.iExecuteAfterTaskDo = iExecuteAfterTaskDo;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("gk", "准备开始读取缓存");
    }

    @Override
    protected Serializable doInBackground(String... params) {
        Log.i("gk", "开始读取缓存");
        Serializable ser = CacheManager.readObject(mContext.get(), params[0]);
        Log.i("gk", "缓存读取成功");
        if (ser == null) {
            return null;
        } else {
            return ser;
        }
    }

    @Override
    protected void onPostExecute(Serializable result) {
        super.onPostExecute(result);
        if (result != null) {
            iExecuteAfterTaskDo.executeOnTaskSuccess(result);
        } else {
            iExecuteAfterTaskDo.executeTaskError();
        }
        iExecuteAfterTaskDo.executeOnTaskFinish();
    }
}
