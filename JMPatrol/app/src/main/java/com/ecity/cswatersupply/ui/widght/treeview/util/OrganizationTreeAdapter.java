package com.ecity.cswatersupply.ui.widght.treeview.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.activities.SelectGroupTreeActivity;
import com.ecity.cswatersupply.ui.widght.treeview.OrganizationTreeView.OnTreeNodeClickListener;
import com.ecity.cswatersupply.ui.widght.treeview.model.TreeNode;

public class OrganizationTreeAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater inflater;
    private List<T> lDatas;// 数据源
    private int defaultExpandLevel;// 要显示的树结构层次
    private List<TreeNode> lNodes;// 处理后数据
    private List<TreeNode> lVisibleNode;// 要显示的数据
    private List<TreeNode> lMenus;//三级树时，第一级作为一级菜单
    private OnTreeNodeClickListener onTreeNodeClickListener;// 树节点点击事件
    private TreeNode selectedNode;
    boolean isDismissMode;

    public enum ETreeChooseMode {
        ONLY_ONE_IN_ONE_NODE, MULTI_CHOOSE_IN_ONE_NODE, MULTI_CHOOSE_IN_ALL_NODES
    }

    /**
     * 树列表适配器
     * @param mContext
     * @param lDatas
     * @param defaultExpandLevel
     * @param isDismissMode     是否是点击树节点之后直接结束activity
     */
    public OrganizationTreeAdapter(Context mContext, List<T> lDatas, int defaultExpandLevel, boolean isDismissMode) {
        this.mContext = mContext;
        this.lDatas = lDatas;
        this.defaultExpandLevel = defaultExpandLevel;
        this.inflater = LayoutInflater.from(mContext);
        this.lMenus = new ArrayList<TreeNode>();
        this.isDismissMode = isDismissMode;

        processDatas(lDatas, defaultExpandLevel);

    }

    public List<TreeNode> getlMenus() {
        return lMenus;
    }

    public void setlMenus(List<TreeNode> lMenus) {
        this.lMenus = lMenus;
    }

    public List<TreeNode> getlNodes() {
        return lNodes;
    }

    public void setOnTreeNodeClickListener(OnTreeNodeClickListener onTreeNodeClickListener2) {
        this.onTreeNodeClickListener = onTreeNodeClickListener2;
    }

    private void processDatas(List<T> lDatas, int defaultExpandLevel) {
        List<TreeNode> lReuslts = new ArrayList<TreeNode>();
        if (isDismissMode) {
            lReuslts = TreeViewHelper.converDatas2ApartmentNodes(lDatas);
        } else {
            lReuslts = TreeViewHelper.converDatas2Nodes(lDatas);
        }
        if (TreeViewHelper.isIs3TreeView()) {
            lNodes = TreeViewHelper.getSortedNodesThree(lReuslts, defaultExpandLevel + 1, lMenus);
        } else {
            lNodes = TreeViewHelper.getSortedNodes(lReuslts, defaultExpandLevel);
        }

        lVisibleNode = TreeViewHelper.filterVisibleNode(lNodes);
    }

    public void expandOrCollapse(int position) {
        TreeNode node = lVisibleNode.get(position);
        //叶子节点在人员选择自动跳转功能需要用到。故，不屏蔽之。
        if (node == null || (node.isLeaf() && !isDismissMode)) {
            return;
        }

        node.setExpand(!node.isExpand());
        TreeNode selectCategory = TreeViewHelper.getMenu();
        if (null != selectCategory) {
            lVisibleNode = TreeViewHelper.filterVisibleNode(lNodes, selectCategory);
        } else {
            lVisibleNode = TreeViewHelper.filterVisibleNode(lNodes);
        }
        super.notifyDataSetChanged();
        if (isDismissMode) {
            if (node.getChildren().size() != 0) {
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(SelectGroupTreeActivity.TREE_DATA_SOURCE, lVisibleNode.get(position));
            ((Activity) mContext).setResult(SelectGroupTreeActivity.RESULT_OK, intent);
            ((Activity) mContext).finish();
        } else {
            if (onTreeNodeClickListener != null) {
                onTreeNodeClickListener.onTreeNodeClick(node, position);
            }
        }
    }

    public void notifyDataSetChangedOnly() {
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        processDatas(lDatas, defaultExpandLevel);
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lVisibleNode.size();
    }

    @Override
    public Object getItem(int position) {
        return lVisibleNode.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TreeNode node = lVisibleNode.get(position);
        convertView = getConvertView(node, position, convertView, parent);
        convertView.setPadding(30 * node.getLevel(), 3, 3, 3);

        return convertView;
    }

    /**
     * 获取每个Item的View,若item不使用默认的可以重写此方法
     * 
     * @param node
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getConvertView(final TreeNode node, final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_form_organization_tree, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (node.getIcon() <= 0) {
            holder.imgIcon.setVisibility(View.INVISIBLE);
        } else {
            holder.imgIcon.setVisibility(View.VISIBLE);
            holder.imgIcon.setImageResource(node.getIcon());
        }
        holder.tvNode.setText(node.getName());
        holder.llTreeContainer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                expandOrCollapse(position);
            }
        });

        holder.cbBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeAssociatedNodeValue(node, isChecked, holder);

            }
        });

        holder.cbBox.setChecked(node.isSelected());

        if ((node.isRoot() && !node.isLeaf()) || node.isSecondNode()) {//具有子节点
            holder.cbBox.setEnabled(true);
            holder.cbBox.setVisibility(View.VISIBLE);
            if (node.isHasSelected()) {
                holder.cbBox.setBackgroundResource(R.drawable.checkbox_hl);
            } else {
                holder.cbBox.setBackgroundResource(R.drawable.checkbox_nor);
                holder.cbBox.setText("");
            }
        } else if (node.isLeaf() && !node.isRoot()) {//叶子节点
            holder.cbBox.setEnabled(false);
            holder.cbBox.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        private LinearLayout llTreeContainer;
        private ImageView imgIcon;
        private TextView tvNode;
        private CheckBox cbBox;

        public ViewHolder(View convertView) {
            llTreeContainer = (LinearLayout) convertView.findViewById(R.id.ll_tree_container);
            imgIcon = (ImageView) convertView.findViewById(R.id.tree_icon);
            tvNode = (TextView) convertView.findViewById(R.id.tree_node);
            cbBox = (CheckBox) convertView.findViewById(R.id.tree_cbox);
        }
    }

    public void changeAssociatedNodeValue(TreeNode node, boolean isChecked, ViewHolder holder) {
        node.setSelected(isChecked);
        if (isChecked) {
            setSelectedNode(node);
            holder.cbBox.setBackgroundResource(R.drawable.checkbox_hl);
        } else {
            setSelectedNode(null);
            holder.cbBox.setBackgroundResource(R.drawable.checkbox_nor);
        }

    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }
}
