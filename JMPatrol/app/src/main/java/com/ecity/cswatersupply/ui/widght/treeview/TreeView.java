package com.ecity.cswatersupply.ui.widght.treeview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ecity.cswatersupply.ui.widght.treeview.model.TreeNode;
import com.ecity.cswatersupply.ui.widght.treeview.util.TreeListViewAdapter;

public class TreeView<T> extends LinearLayout {

    private List<T> lDatas;
    private ListView lvTree;
    private TreeListViewAdapter<T> adapter;

    private Context context;

    /**
     * 树节点点击事件
     * 
     * @param onTreeNodeClick
     */
    public void setOnTreeNodeClickListener(OnTreeNodeClickListener onTreeNodeClickListener) {
        if (null != adapter) {
            adapter.setOnTreeNodeClickListener(onTreeNodeClickListener);
        }
    }

    public TreeView(Context context) {
        this(context, null);
    }

    public TreeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TreeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

        myInit(attrs);
    }

    private void myInit(AttributeSet attrs) {
        initView();
        initDatas();
    }

    private void initView() {
        lvTree = new ListView(context);
        lvTree.setDivider(null);
        addView(lvTree);
    }

    private void initDatas() {
        lDatas = new ArrayList<T>();
    }

    /**
     * 设置树的数据源
     * 
     * @param lDatas
     */
    public void setDatas(List<T> lDatas) {
        this.lDatas.clear();
        this.lDatas.addAll(lDatas);
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置树的数据源
     * 
     * @param lDatas
     */
    public void setDatas(List<T> lDatas, TreeListViewAdapter<T> adapter) {
        this.lDatas.clear();
        this.lDatas.addAll(lDatas);
        this.adapter = adapter;
        lvTree.setAdapter(this.adapter);
        adapter.notifyDataSetChangedOnly();
    }

    /**
     * 树节点点击事件
     * 
     */
    public interface OnTreeNodeClickListener {
        void onTreeNodeClick(TreeNode node, int position);
    }
}
