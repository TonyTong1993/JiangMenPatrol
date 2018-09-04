package com.ecity.cswatersupply.workorder.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.workorder.WorkOrderUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.z3app.android.util.StringUtil;

/**
 * 工单搜索界面的适配器
 * @author jonathanma
 *
 */
public class WorkOrderSearchAdapter extends AExpandableListAdapter<String, WorkOrder> {
    private LayoutInflater mInflater;

    public WorkOrderSearchAdapter(Context context) {
        super(context);
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder = null;
        String title = groupList.get(groupPosition);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lv_item_work_order_search_group_title, null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        groupViewHolder.tvTitle.setText(title);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildrenViewHolder childViewHolder = null;
        WorkOrder workOrder = childrenList.get(groupPosition).get(childPosition);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lv_item_work_order_search, null);
            childViewHolder = new ChildrenViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildrenViewHolder) convertView.getTag();
        }
        String title = groupList.get(groupPosition);
        String detail = getDetailInfo(title, workOrder);
        String status = WorkOrderUtil.getWorkOrderStateString(workOrder.getAttributes());
        childViewHolder.tvTitle.setText(workOrder.getAttributes().get(WorkOrder.KEY_CODE));
        childViewHolder.tvDetail.setText(detail);
        childViewHolder.tvStatus.setText(status);
        int visibility = StringUtil.isBlank(detail) ? View.GONE : View.VISIBLE;
        childViewHolder.tvDetail.setVisibility(visibility);

        return convertView;
    }

    private String getDetailInfo(String groupTitle, WorkOrder workOrder) {
        String attrKey = "";
        if (groupTitle.equals(mContext.getString(R.string.workorder_search_group_code))) {
            attrKey = WorkOrder.KEY_CODE;
        } else if (groupTitle.equals(mContext.getString(R.string.workorder_search_group_content))) {
            attrKey = WorkOrder.KEY_CONTENT;
        } else if (groupTitle.equals(mContext.getString(R.string.workorder_search_group_place))) {
            attrKey = WorkOrder.KEY_ADDRESS;
        } else if (groupTitle.equals(mContext.getString(R.string.workorder_search_group_main_assignee))) {
            attrKey = WorkOrder.KEY_MAIN_MAN;
        } else if (groupTitle.equals(mContext.getString(R.string.workorder_search_group_source))) {
            attrKey = WorkOrder.KEY_FROM_ALIAS;
        } else {

        }

        return workOrder.getAttributes().get(attrKey);
    }

    private static class GroupViewHolder {
        private TextView tvTitle;

        public GroupViewHolder(View convertView) {
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
        }
    }

    private static class ChildrenViewHolder {
        private TextView tvTitle;
        private TextView tvDetail;
        private TextView tvStatus;

        public ChildrenViewHolder(View v) {
            tvTitle = (TextView) v.findViewById(R.id.tv_title);
            tvDetail = (TextView) v.findViewById(R.id.tv_detail);
            tvStatus = (TextView) v.findViewById(R.id.tv_status);
        }
    }
}
