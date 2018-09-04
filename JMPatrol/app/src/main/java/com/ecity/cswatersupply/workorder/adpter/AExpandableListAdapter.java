package com.ecity.cswatersupply.workorder.adpter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.widget.ExpandableListAdapter;

/**
 * 扩展ListView适配器
 * @author gaokai
 *
 * @param <T> 一级列表数据模型
 * @param <E> 二级列表数据模型
 */
public abstract class AExpandableListAdapter<T, E> implements ExpandableListAdapter {
    private final DataSetObservable mDataSetObservable = new DataSetObservable();
    protected List<T> groupList;
    protected List<List<E>> childrenList;
    protected Context mContext;

    public AExpandableListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    /**
     * Notifies the attached observers that the underlying data has been changed
     * and any View reflecting the data set should refresh itself.
     */
    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    /**
     * Notifies the attached observers that the underlying data is no longer
     * valid or available. Once invoked this adapter is no longer valid and
     * should not report further data set changes.
     */
    public void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childrenList.get(groupPosition).size();
    }

    @Override
    public T getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public E getChild(int groupPosition, int childPosition) {
        return childrenList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    public List<T> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<T> groupList) {
        if (groupList == null) {
            groupList = new ArrayList<T>();
        }
        this.groupList = groupList;

    }

    public List<List<E>> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<List<E>> childrenList) {
        if (childrenList == null) {
            childrenList = new ArrayList<List<E>>();
        }
        this.childrenList = childrenList;

        notifyDataSetChanged();
    }
}
