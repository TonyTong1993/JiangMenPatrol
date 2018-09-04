package com.ecity.cswatersupply.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.AddressInfoModel;
import com.ecity.cswatersupply.utils.ResourceUtil;

public class AddressExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> groupList;
    private Map<String, List<AddressInfoModel>> childMap;

    public AddressExpandableListAdapter(Context context,List<String> groups, Map<String, List<AddressInfoModel>> addresses) {
        super();
        this.context = context;
        this.groupList = groups;
        this.childMap = addresses;
    }

    public void setAddresses(List<String> groups, Map<String, List<AddressInfoModel>> addresses) {
        this.groupList = groups;
        this.childMap = addresses;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (null != childMap.get(groupList.get(groupPosition).toString())) {
            return childMap.get(groupList.get(groupPosition).toString()).size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childMap.get(groupList.get(groupPosition).toString()).get(childPosition);
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupHolder = null;
        if (null == convertView) {
            LayoutInflater inflater = LayoutInflater.from(context);  
            convertView = inflater.inflate(R.layout.item_expandablelist_group, null); 
            groupHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupViewHolder) convertView.getTag();
        }
        if(isExpanded) {
            groupHolder.imageView.setBackground(ResourceUtil.getDrawableResourceById(R.drawable.arrow_up));
        } else {
            groupHolder.imageView.setBackground(ResourceUtil.getDrawableResourceById(R.drawable.arrow_down));
        }
        groupHolder.textGroup.setText(groupList.get(groupPosition).toString());  // 设置大组成员名称  
        return convertView; 
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childHolder = null;
        if (null == convertView) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_expandablelist_child, null); 
            childHolder = new ChildViewHolder(convertView);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildViewHolder) convertView.getTag();
        }
        childHolder.textChild.setText(childMap.get(groupList.get(groupPosition).toString()).get(childPosition).getName());
        return convertView;
    }

    static class GroupViewHolder {
        private TextView textGroup;
        private ImageView imageView;

        public GroupViewHolder(View view) {
            textGroup = (TextView) view.findViewById(R.id.textGroup);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }

    static class ChildViewHolder {
        private TextView textChild;

        public ChildViewHolder(View view) {
            textChild = (TextView) view.findViewById(R.id.textChild);
        }
    }

}
