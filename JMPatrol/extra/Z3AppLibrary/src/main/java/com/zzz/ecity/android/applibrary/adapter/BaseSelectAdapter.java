package com.zzz.ecity.android.applibrary.adapter;

import java.util.ArrayList;
import java.util.List;

import com.zzz.ecity.android.applibrary.model.SimpleSelectModel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseSelectAdapter extends BaseAdapter {
	protected Context mContext;
	protected List<SimpleSelectModel> dataSource = new ArrayList<SimpleSelectModel>();
	public BaseSelectAdapter(Context pContext,List<SimpleSelectModel> dataSource) {
		this.mContext = pContext;
		this.dataSource = dataSource;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (null == dataSource) ? 0 : dataSource.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if(null == dataSource)
		{
			return null;
		}
		
		if(arg0 >= dataSource.size())
		{
			return null;
		}
		
		return dataSource.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}


	/***
	 * 某一列被点击
	 * @param position
	 */
	public abstract void clickAtPosition(int position); 
}
