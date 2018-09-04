package com.ecity.cswatersupply.adapter.map;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


/**
 * 所在位置适配器
 * 
 * @author yxx
 * 
 */
public abstract class APoiSearchAdapter<T> extends BaseAdapter {
    	
	//这些属性都是每个适配器中都能用到的，访问控制符设置为protected，以便继承的子类都能访问
    protected LayoutInflater mInflater;
    protected List<T> list;//数据源
    protected Context context;
    protected int layoutId;//item布局文件

	public APoiSearchAdapter(Context context,List<T> appGroup, int layoutId) {
		this.context = context;
		this.list = appGroup;
		mInflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
	}	

	public void updateDataSet(List<T> list){
		this.list = list;
		notifyDataSetChanged();
	}
		
	@Override
	public int getCount() {
		return list == null ?0 :list.size();
	}

	@Override
	public Object getItem(int location) {
		return list.get(location);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void addObject(List<T> mAppGroup) {
		this.list = mAppGroup;
		notifyDataSetChanged();
	}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        //初始化ViewHolder，加载我们的item布局文件
        ViewHolder holder = ViewHolder.get(context, convertView, parent, layoutId, position);
        //getItem(position)的类型就是T，这句话在子类中的具体实现就是给具体的控件初始化
        convert(holder, (T) getItem(position));
        //并赋值，初始化赋值控件时需要viewHolder和具体的数据Java bean，在这里抽象出来就是类型T
        return holder.getConvertView();//返回convertView
    }
    public abstract void convert(ViewHolder holder, T t);
}
