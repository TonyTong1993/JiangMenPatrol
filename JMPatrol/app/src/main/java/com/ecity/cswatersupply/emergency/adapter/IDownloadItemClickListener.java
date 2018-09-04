package com.ecity.cswatersupply.emergency.adapter;

import android.view.View;

public interface IDownloadItemClickListener<T> {
    void onItemClick(View view, int position, T object,boolean redownload);
}