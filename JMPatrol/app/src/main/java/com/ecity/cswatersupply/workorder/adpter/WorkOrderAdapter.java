package com.ecity.cswatersupply.workorder.adpter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.model.WorkOrderGroupEnum;
import com.ecity.cswatersupply.workorder.widght.WorkOrderListItemBasicView;
import com.ecity.cswatersupply.workorder.widght.WorkOrderListItemBtnsView;
import com.ecity.cswatersupply.workorder.widght.WorkOrderListItemCustomView;

/**
 * 新工单界面适配器
 * 
 * 二级界面由公共区、自定义区和按钮区三部分组成。公共区：所有工单公用的界面；自定义区：根据分组或者工单状态不同，自定义布局；
 * 按钮区：根据工单状态，自定义组装按钮。
 * 
 * @author gaokai
 *
 */
public class WorkOrderAdapter extends AExpandableListAdapter<WorkOrderGroupEnum, WorkOrder> {
    private LayoutInflater mInflater;
    private IExpandListener mExpandListener;

    public WorkOrderAdapter(Context context, IExpandListener expandListener) {
        super(context);
        this.mExpandListener = expandListener;
        this.mInflater = LayoutInflater.from(context);
    }

    public interface IExpandListener {
        void onExpand(int position);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
        mExpandListener.onExpand(groupPosition);
        WorkOrderGroupEnum.values()[groupPosition].setHasNew(false);// 打开分组后，把红点隐藏
        notifyDataSetChanged();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
        mExpandListener.onExpand(-1);
        WorkOrderGroupEnum.values()[groupPosition].setHasNew(false);
        notifyDataSetChanged();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder = null;
        WorkOrderGroupEnum page = groupList.get(groupPosition);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_workorder_expandablelistview_group, null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        setGroupDrawable(groupViewHolder, page, isExpanded);
        setGroupTitle(groupViewHolder, page, groupPosition);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildrenViewHolder viewHolder = null;
        WorkOrder itemData = childrenList.get(groupPosition).get(childPosition);
        String currentWorkOrderState = itemData.getAttributes().get(WorkOrder.KEY_STATE);
        String btnStr = itemData.getAttributes().get(WorkOrder.KEY_OPERATE_BTNS);
        WorkOrderGroupEnum group = WorkOrderGroupEnum.values()[groupPosition];

        if (convertView != null) {
            viewHolder = (ChildrenViewHolder) convertView.getTag(R.id.tag_view);
            WorkOrderGroupEnum groupTag = (WorkOrderGroupEnum) viewHolder.basicView.getTag();
            String btnTag = (String) viewHolder.btnsView.getTag();
            String bodyTag = (String) viewHolder.customView.getTag();
            // 如果分组不一样，基本区也重新渲染
            if (groupTag != group) {
                loadBasicView(viewHolder, itemData, group, currentWorkOrderState);
            }
            // 而如果自定义区状态跟被复用的不同，要重新渲染
            if (!bodyTag.equals(currentWorkOrderState)) {
                loadCustomView(viewHolder, itemData, group, currentWorkOrderState);
            }
            // 按钮信息不同，重新渲染，如果相同，可复用
            if (!btnTag.equals(btnStr)) {
                loadBtnsView(viewHolder, groupPosition, itemData);
            }
        }

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lv_item_workorderlist, null);
            viewHolder = new ChildrenViewHolder(convertView);
            loadBasicView(viewHolder, itemData, group, currentWorkOrderState);
            loadCustomView(viewHolder, itemData, group, currentWorkOrderState);
            loadBtnsView(viewHolder, groupPosition, itemData);
            convertView.setTag(R.id.tag_view, viewHolder);
        }
        // 无论是否复用，工单展示区的数据都要重新绑定
        setWorkOrderData(viewHolder, itemData, group);
        return convertView;
    }

    /**
     * 渲染工单基本信息
     */
    private void loadBasicView(ChildrenViewHolder viewHolder, WorkOrder itemData, WorkOrderGroupEnum group, String currentWorkOrderState) {
        viewHolder.basicView.setView(itemData);
        viewHolder.basicView.setTag(group);// 完工分组，不用显示更多图标
    }

    /**
     * 渲染工单自定义信息
     */
    private void loadCustomView(ChildrenViewHolder viewHolder, WorkOrder itemData, WorkOrderGroupEnum group, String currentWorkOrderState) {
        viewHolder.customView.setView(itemData, group);
        viewHolder.customView.setTag(currentWorkOrderState);
    }

    /**
     * 渲染工单按钮
     */
    private void loadBtnsView(ChildrenViewHolder viewHolder, int groupPosition, WorkOrder itemData) {
        if (WorkOrderGroupEnum.values()[groupPosition] != WorkOrderGroupEnum.COMLETE) {
            viewHolder.btnsView.setVisibility(View.VISIBLE);
            viewHolder.btnsView.setView(itemData,null);
        } else {// 完结工单没有按钮
            viewHolder.btnsView.setVisibility(View.GONE);
        }
        viewHolder.btnsView.setTag(itemData.getAttributes().get(WorkOrder.KEY_OPERATE_BTNS));
    }

    private void setWorkOrderData(ChildrenViewHolder viewHolder, WorkOrder itemData, WorkOrderGroupEnum group) {
        viewHolder.basicView.setDisplayInfo(itemData, group);
        viewHolder.customView.setDisplayInfo(itemData, group);
        viewHolder.btnsView.setOnBtnClickListener(itemData, group);
    }

//    private boolean checkBtnsSame(String lastState, String currentWorkOrderState) {
//        List<AppMenu> btns1 = MenuFactory.getMenuListByTab(lastState);
//        List<AppMenu> btns2 = MenuFactory.getMenuListByTab(currentWorkOrderState);
//        if (btns1.size() != btns2.size()) {
//            return false;
//        } else {
//            for (AppMenu appMenu : btns1) {
//                if (!containBtn(appMenu, btns2)) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }

//    private boolean containBtn(AppMenu appMenu, List<AppMenu> btns2) {
//        for (AppMenu appMenu2 : btns2) {
//            if (appMenu.getName().equals(appMenu2.getName())) {
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * 设置图标和指示器
     */
    private void setGroupDrawable(GroupViewHolder groupViewHolder, WorkOrderGroupEnum page, boolean isExpanded) {
        boolean hasNew = page.hasNew();
        Drawable d = null;
        if (isExpanded) {
            d = mContext.getResources().getDrawable(R.drawable.indicator_expanded);
        } else if (hasNew) {
            d = mContext.getResources().getDrawable(R.drawable.tips_red_no_text);
        } else {
            d = mContext.getResources().getDrawable(R.drawable.indicator_collapsed);
        }
        groupViewHolder.ivIndicator.setImageDrawable(d);
        groupViewHolder.ivGroupIcon.setImageDrawable(mContext.getResources().getDrawable(page.getIconResId()));
    }

    private void setGroupTitle(GroupViewHolder groupViewHolder, WorkOrderGroupEnum page, int groupPosition) {
        int childrenCount = childrenList.get(groupPosition).size();
        groupViewHolder.tvGroup.setText(mContext.getResources().getString(page.getTitle()));
        String msg = "";
        if (childrenCount != 0) {
            // 有工单，根据指定的颜色，显示个性化提示
            msg = ResourceUtil.getStringById(R.string.workorder_tips_count, childrenCount);
        }else{
            msg = ResourceUtil.getStringById(R.string.workorder_empty, childrenCount);
          
        }
        groupViewHolder.tvTips.setText(msg);
        int color = ResourceUtil.getColorById(page.getColorResId());
        groupViewHolder.tvTips.setTextColor(color);
    }

    private static class GroupViewHolder {
        private ImageView ivGroupIcon;
        private TextView tvGroup;
        private TextView tvTips;
        private ImageView ivIndicator;

        public GroupViewHolder(View convertView) {
            ivGroupIcon = (ImageView) convertView.findViewById(R.id.iv_group);
            ivIndicator = (ImageView) convertView.findViewById(R.id.iv_indicator);
            tvGroup = (TextView) convertView.findViewById(R.id.tv_group_label);
            tvTips = (TextView) convertView.findViewById(R.id.tv_tips);
        }
    }

    private static class ChildrenViewHolder {
        private WorkOrderListItemBasicView basicView;
        private WorkOrderListItemCustomView customView;
        private WorkOrderListItemBtnsView btnsView;

        public ChildrenViewHolder(View v) {
            basicView = (WorkOrderListItemBasicView) v.findViewById(R.id.view_list_item_common);
            customView = (WorkOrderListItemCustomView) v.findViewById(R.id.view_list_item_custom);
            btnsView = (WorkOrderListItemBtnsView) v.findViewById(R.id.view_list_item_btns);
        }
    }
}
