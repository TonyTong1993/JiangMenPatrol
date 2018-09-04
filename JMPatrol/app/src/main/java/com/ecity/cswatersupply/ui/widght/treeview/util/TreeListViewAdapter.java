package com.ecity.cswatersupply.ui.widght.treeview.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
import com.ecity.cswatersupply.ui.widght.treeview.TreeView.OnTreeNodeClickListener;
import com.ecity.cswatersupply.ui.widght.treeview.model.TreeNode;
import com.ecity.cswatersupply.utils.ListUtil;

public class TreeListViewAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater inflater;
    private List<T> lDatas;// 数据源
    private int defaultExpandLevel;// 要显示的树结构层次
    private List<TreeNode> lNodes;// 处理后数据
    private List<TreeNode> lVisibleNode;// 要显示的数据
    private List<TreeNode> lMenus;//三级树时，第一级作为一级菜单
    private OnTreeNodeClickListener onTreeNodeClickListener;// 树节点点击事件
    private ETreeChooseMode eTreeChooseMode;

    public enum ETreeChooseMode {
        ONLY_ONE_IN_ONE_NODE, MULTI_CHOOSE_IN_ONE_NODE, MULTI_CHOOSE_IN_ALL_NODES
    }

    public TreeListViewAdapter(Context mContext, List<T> lDatas, int defaultExpandLevel) {
        this.mContext = mContext;
        this.lDatas = lDatas;
        this.defaultExpandLevel = defaultExpandLevel;
        this.inflater = LayoutInflater.from(mContext);
        this.eTreeChooseMode = ETreeChooseMode.MULTI_CHOOSE_IN_ALL_NODES;
        this.lMenus = new ArrayList<TreeNode>();

        processDatas(lDatas, defaultExpandLevel);

    }

    public void changeDataSourcesByTreeNode(TreeNode menu) {
        if (!ListUtil.isEmpty(lNodes)) {
            lVisibleNode = TreeViewHelper.filterVisibleNode(lNodes, menu);
        }
    }

    public List<TreeNode> getlMenus() {
        return lMenus;
    }

    public void setlMenus(List<TreeNode> lMenus) {
        this.lMenus = lMenus;
    }

    public void setTreeChooseMode(ETreeChooseMode eTreeChooseMode) {
        this.eTreeChooseMode = eTreeChooseMode;
    }

    public List<TreeNode> getlNodes() {
        return lNodes;
    }

    /**
     * 树节点点击事件
     * 
     * @param onTreeNodeClick
     */
    public void setOnTreeNodeClickListener(OnTreeNodeClickListener onTreeNodeClickListener) {
        this.onTreeNodeClickListener = onTreeNodeClickListener;
    }

    /**
     * 处理源数据，让其成为可以用于展示的数据
     * 
     * @param lDatas
     * @param defaultExpandLevel
     * @throws Exception
     */
    private void processDatas(List<T> lDatas, int defaultExpandLevel) {
        List<TreeNode> lReuslts = TreeViewHelper.converDatas2Nodes(lDatas);

        if (TreeViewHelper.isIs3TreeView()) {
            lNodes = TreeViewHelper.getSortedNodesThree(lReuslts, defaultExpandLevel + 1, lMenus);
        } else {
            lNodes = TreeViewHelper.getSortedNodes(lReuslts, defaultExpandLevel);
        }

        lVisibleNode = TreeViewHelper.filterVisibleNode(lNodes);
    }

    /**
     * 树节点展开或收缩
     * 
     * @param position
     */
    public void expandOrCollapse(int position) {
        TreeNode node = lVisibleNode.get(position);

        if (node == null || node.isLeaf()) {
            return;
        }

        node.setExpand(!node.isExpand());
        TreeNode selectCategory = TreeViewHelper.getMenu();
        if (null != selectCategory) {
            lVisibleNode = TreeViewHelper.filterVisibleNode(lNodes,selectCategory);
        }else {
            lVisibleNode = TreeViewHelper.filterVisibleNode(lNodes);
        }
        // 此处必须调用父类方法
        super.notifyDataSetChanged();
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

        // 设置左边距
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_form_item_tree, parent, false);
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
                if (onTreeNodeClickListener != null) {
                    onTreeNodeClickListener.onTreeNodeClick(node, position);
                }
            }
        });

        holder.cbBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    changeAssociatedNodeValue(node, true);
                    setOtherParallelNodeFalse(node);
                } else {
                    changeAssociatedNodeValue(node, false);
                }

            }
        });

        holder.cbBox.setChecked(node.isSelected());

        if (node.isRoot() && node.isLeaf()) {
            holder.cbBox.setEnabled(true);
            switch (eTreeChooseMode) {
                case ONLY_ONE_IN_ONE_NODE:
                    break;
                case MULTI_CHOOSE_IN_ONE_NODE:
                    break;
                case MULTI_CHOOSE_IN_ALL_NODES:
                    holder.cbBox.setVisibility(View.VISIBLE);
                    if (node.isSelected()) {
                        holder.cbBox.setText("");
                        holder.cbBox.setBackgroundResource(R.drawable.checkbox_hl);
                    } else {
                        holder.cbBox.setText("");
                        holder.cbBox.setBackgroundResource(R.drawable.checkbox_nor);
                    }
                    break;
                default:
                    break;
            }
        } else if ((node.isRoot() && !node.isLeaf()) || node.isSecondNode()) {
            switch (eTreeChooseMode) {
                case MULTI_CHOOSE_IN_ALL_NODES:
                    break;
                case MULTI_CHOOSE_IN_ONE_NODE:
                    holder.cbBox.setVisibility(View.VISIBLE);
                    holder.cbBox.setEnabled(true);
                    int num = countUpSelectedNum(node, -1);
                    if (num != -1) {
                        holder.cbBox.setBackgroundResource(R.drawable.checkbox_root);
                        if (num == node.getChildren().size()) {
                            holder.cbBox.setBackgroundResource(R.drawable.checkbox_hl);
                            holder.cbBox.setText("");
                        } else {
                            holder.cbBox.setText(String.valueOf(num));
                        }
                    } else {
                        holder.cbBox.setText("");
                        holder.cbBox.setBackgroundResource(R.drawable.checkbox_nor);
                    }
                    break;
                case ONLY_ONE_IN_ONE_NODE:
                    holder.cbBox.setVisibility(View.VISIBLE);
                    holder.cbBox.setEnabled(false);
                    if (node.isHasSelected()) {
                        holder.cbBox.setBackgroundResource(R.drawable.checkbox_root);
                        holder.cbBox.setText("1");
                    } else {
                        holder.cbBox.setBackgroundResource(R.drawable.checkbox_nor);
                        holder.cbBox.setText("");
                    }
                    break;
                default:
                    break;
            }
        } else if (node.isLeaf() && !node.isRoot()) {
            switch (eTreeChooseMode) {
                case MULTI_CHOOSE_IN_ALL_NODES:
                    break;
                case MULTI_CHOOSE_IN_ONE_NODE:
                    break;
                case ONLY_ONE_IN_ONE_NODE:
                    holder.cbBox.setVisibility(View.VISIBLE);
                    holder.cbBox.setEnabled(true);
                    if (node.isSelected()) {
                        holder.cbBox.setText("");
                        holder.cbBox.setBackgroundResource(R.drawable.checkbox_hl);
                    } else {
                        holder.cbBox.setText("");
                        holder.cbBox.setBackgroundResource(R.drawable.checkbox_nor);
                    }
                    break;
                default:
                    break;
            }
        } else {
            // no logic to do.
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

    public void changeAssociatedNodeValue(TreeNode node, boolean flag) {
        if ((node.isRoot() && flag) || (node.isSecondNode() && flag)) {
            node.setSelected(true);
            for (TreeNode temp : node.getChildren()) {
                temp.setSelected(true);
            }
            node.setHasSelected(true);
            notifyDataSetChangedOnly();
        } else if ((node.isRoot() && !flag) || (node.isSecondNode() && !flag)) {
            node.setSelected(false);
            int size = node.getChildren().size();
            int numChildSelected = 0;
            for (TreeNode temp : node.getChildren()) {
                if (temp.isSelected()) {
                    numChildSelected++;
                }
            }
            if (numChildSelected == (size - 1)) {
                // no logic to do.
            } else {
                for (TreeNode temp : node.getChildren()) {
                    temp.setSelected(false);
                }
            }
            notifyDataSetChangedOnly();
        } else if (node.isLeaf() && flag) {
            node.setSelected(true);
            TreeNode parent = node.getParent();
            parent.setHasSelected(true);
            boolean isAllSelected = true;
            for (TreeNode t : parent.getChildren()) {
                if (!t.isSelected()) {
                    isAllSelected = false;
                    break;
                }
            }
            if (isAllSelected) {
                parent.setSelected(true);
            }
            notifyDataSetChangedOnly();
        } else if (node.isLeaf() && !flag) {
            node.setSelected(false);
            TreeNode parent = node.getParent();
            boolean isAllSelected = true;
            parent.setHasSelected(false);
            for (TreeNode t : parent.getChildren()) {
                if (!t.isSelected()) {
                    isAllSelected = false;
                } else {
                    parent.setHasSelected(true);
                }
            }
            if (!isAllSelected) {
                parent.setSelected(false);
            }
            notifyDataSetChangedOnly();
        } else {
            // no logic to do.
        }
    }

    public void setOtherParallelNodeFalse(TreeNode node) {
        switch (eTreeChooseMode) {
            case MULTI_CHOOSE_IN_ALL_NODES:
                break;
            case MULTI_CHOOSE_IN_ONE_NODE:
                for (TreeNode nodeTemp : lNodes) {
                    if (node.isRoot() && node.getLevel() == nodeTemp.getLevel() && !node.getId().equalsIgnoreCase(nodeTemp.getId())) {
                        setOtherChildrenValue(nodeTemp);
                    } else if (node.isLeaf() && node.getLevel() == nodeTemp.getLevel() && null != node.getParent() && null != nodeTemp.getParent()
                            && !node.getParent().getId().equalsIgnoreCase(nodeTemp.getParent().getId())) {
                        setOtherParentValue(nodeTemp);
                    } else {
                        // no logic to do.
                    }
                }
                break;
            case ONLY_ONE_IN_ONE_NODE:
                for (TreeNode nodeTemp : lNodes) {
                    if (node.isRoot() && node.getLevel() == nodeTemp.getLevel() && !node.getId().equalsIgnoreCase(nodeTemp.getId())) {
                        setOtherChildrenValue(nodeTemp);
                    } else if (node.isLeaf() && node.getLevel() == nodeTemp.getLevel() && !node.getId().equalsIgnoreCase(nodeTemp.getId())) {
                        TreeNode parentNode = node.getParent();
                        TreeNode parentTemp = nodeTemp.getParent();
                        if (null != parentNode && null != parentTemp && parentNode.getId().equalsIgnoreCase(parentTemp.getId())) {
                            setOtherParentValueExceptSelf(nodeTemp);
                        } else if (null != parentNode && null != parentTemp && !parentNode.getId().equalsIgnoreCase(parentTemp.getId())) {
                            setOtherParentValue(nodeTemp);
                        }
                    } else {
                        // no logic to do.
                    }
                }
                break;
            default:
                break;
        }

        this.notifyDataSetChangedOnly();
    }

    private void setOtherChildrenValue(TreeNode nodeTemp) {
        nodeTemp.setSelected(false);
        if (nodeTemp.isRoot()) {
            nodeTemp.setHasSelected(false);
            for (TreeNode temp : nodeTemp.getChildren()) {
                //temp.setSelected(false);
                setOtherChildrenValue(temp);
            }
        }
    }

    private void setOtherParentValue(TreeNode nodeTemp) {
        TreeNode parent = nodeTemp.getParent();
        if (null != parent) {
            nodeTemp.setSelected(false);
            parent.setHasSelected(false);
            parent.setSelected(false);
            setOtherParentValue(parent);
        }
    }

    private void setOtherParentValueExceptSelf(TreeNode nodeTemp) {
        TreeNode parent = nodeTemp.getParent();
        if (null != parent) {
            nodeTemp.setSelected(false);
            parent.setHasSelected(true);
            parent.setSelected(false);
            setOtherParentValueExceptSelf(parent);
        }
    }

    private int countUpSelectedNum(TreeNode nodeTemp, int count) {
        if (nodeTemp.isRoot() || nodeTemp.isSecondNode()) {
            for (TreeNode temp : nodeTemp.getChildren()) {
                if (temp.isLeaf() && temp.isSelected()) {
                    if (-1 == count) {
                        count = 0;
                        count++;
                    } else {
                        count++;
                    }
                } else if (temp.isRoot() && temp.isHasSelected()) {
                    countUpSelectedNum(temp, count);
                }
            }
        }

        return count;
    }
}
