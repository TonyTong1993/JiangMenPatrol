/*
 * Copyright (C) 2009 Teleca Poland Sp. z o.o. <android@teleca.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zzz.ecity.android.applibrary.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Nice wrapper-abstraction around ArrayList
 * 
 * @author tianshaoliang
 *
 * @param <T>
 */
public abstract class ArrayListAdapter<T> extends BaseAdapter {

    protected List<T> mList;
    protected Context mContext;
    protected ListView mListView;

    public ArrayListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if (mList != null)
            return mList.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    abstract public View getView(int position, View convertView, ViewGroup parent);

    public void setList(List<T> orders) {
        if (orders == null) {
            orders = new ArrayList<T>();
        }
        this.mList = orders;
        notifyDataSetChanged();
    }

    public List<T> getList() {
        if (mList == null) {
            mList = new ArrayList<T>();
        }
        return mList;
    }

    public void add(T obj) {
        if (obj == null) {
            return;
        }
        mList.add(obj);
    }

    public void setList(T[] list) {
        ArrayList<T> arrayList = new ArrayList<T>(list.length);
        for (T t : list) {
            arrayList.add(t);
        }
        setList(arrayList);
    }

    public ListView getListView() {
        return mListView;
    }

    public void setListView(ListView listView) {
        mListView = listView;
    }

    public void remove(T t) {
        if (mList != null && t != null) {
            mList.remove(t);
            notifyDataSetChanged();
        }
    }

    public void replace(T t) {
        if (mList != null && t != null) {
            mList.remove(t);
            mList.add(0, t);
            notifyDataSetChanged();
        }
    }

    public Context getContext() {
        return mContext;
    }
}
