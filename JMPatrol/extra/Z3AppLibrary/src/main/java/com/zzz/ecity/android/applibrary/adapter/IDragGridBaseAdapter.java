package com.zzz.ecity.android.applibrary.adapter;

import java.util.List;

public interface IDragGridBaseAdapter<T> {
	/**
	 * 获取所有数据项
	 * @return
	 */
    public List<T> getItems();
    /**
     * 设置数据项
     * @param items
     */
    public void setItems(List<T> items);
    
	/**
	 * 重新排列数据项
	 * @param oldPosition
	 * @param newPosition
	 */
	public void reorderItems(int oldPosition, int newPosition);
	
	
	/**
	 * 设置某个item隐藏
	 * @param hidePosition 设置某个item隐藏
	 */
	public void setHideItem(int hidePosition);
	
	/**
	 * 删除某个item
	 * @param removePosition 删除某个item
	 */
	public void removeItem(int removePosition);

	/***
	 * 设置是否可以编辑
	 * @param isEditable 设置是否可以编辑
	 */
	public void setEditable(boolean isEditable);
	
	/***
	 * 是否可以编辑
	 * @return 是否可以编辑
	 */
	public boolean isEditable();

	/***
	 * 单元格是否可以拖动
	 * @param position
	 * @return 单元格是否可以拖动
	 */
	public boolean isDragable(int position);
	
	/**
	 * 获得空数据项
	 * @return
	 */
    public T getNullItem();
    /***
     * 判断指定元素是否为空数据项
     * @param item
     * @return
     */
    boolean isNullItem(T item);
}
