package com.ecity.cswatersupply.workorder.presenter;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Gxx on 2017/4/12.
 */
public abstract class AFromDataSourceProvider {
    private Activity activity;

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    public Activity getActivity() {
        return this.activity;
    }

    public abstract void requestDataSource();

}
