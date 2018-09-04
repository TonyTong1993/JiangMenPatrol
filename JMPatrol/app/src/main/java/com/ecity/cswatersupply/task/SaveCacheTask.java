package com.ecity.cswatersupply.task;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import android.content.Context;
import android.os.AsyncTask;

import com.ecity.cswatersupply.utils.CacheManager;

/**
 * 缓存任务
 * @author gaokai
 *
 */
public class SaveCacheTask extends AsyncTask<Void, Void, Void> {
    private WeakReference<Context> mContext;
    private String mKey;
    private Serializable seri;

    public SaveCacheTask(Context context, Serializable seri, String key) {
        mContext = new WeakReference<Context>(context);
        this.mKey = key;
        this.seri = seri;
    }

    @Override
    protected Void doInBackground(Void... params) {
        CacheManager.saveObject(mContext.get(), seri, mKey);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
