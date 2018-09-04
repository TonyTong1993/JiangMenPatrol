package com.ecity.cswatersupply.ui.widght.treeview.util;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.widght.treeview.model.TreeNode;
import com.ecity.cswatersupply.utils.ListUtil;

/**
 *三级树控件 
 *
 */
public class Tree3TitleAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<TreeNode> mDatas;

    public Tree3TitleAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (!ListUtil.isEmpty(mDatas)) {
            return mDatas.size();
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (!ListUtil.isEmpty(mDatas)) {
            return mDatas.get(position);
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TreeNode treeNode = mDatas.get(position);
        ViewHolder holder = null;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.lv_item_3treeview_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvPopupMenuItem.setText(treeNode.getName());

        return convertView;
    }

    static class ViewHolder {
        private TextView tvPopupMenuItem;

        public ViewHolder(View convertView) {
            tvPopupMenuItem = (TextView) convertView.findViewById(R.id.tv_popup_menu_item);
        }
    }

    public List<TreeNode> getDatas() {
        return mDatas;
    }

    public void setDatas(List<TreeNode> mDatas) {
        this.mDatas = mDatas;
    }
}
