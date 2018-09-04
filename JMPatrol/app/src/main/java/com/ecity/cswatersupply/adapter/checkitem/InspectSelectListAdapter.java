package com.ecity.cswatersupply.adapter.checkitem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.ChildrenItem;
import com.ecity.cswatersupply.model.checkitem.GroupItem;
import com.ecity.cswatersupply.utils.ListUtil;

public class InspectSelectListAdapter extends BaseExpandableListAdapter {

    private List<GroupItem> dataList;
    private LayoutInflater inflater;
    private List<String> checkedChildren = new ArrayList<String>();
    private List<GroupItem> checkedGroupList;
    private Map<String, Integer> groupCheckedStateMap = new HashMap<String, Integer>(); 

    @SuppressLint("UseSparseArrays")
    public InspectSelectListAdapter(Context context, List<GroupItem> dataList) {
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);
        checkedGroupList = new ArrayList<GroupItem>();
      //默认设置所有的父列表项和子列表项都为选中״̬
        int groupCount = getGroupCount();
        for (int groupPosition = 0; groupPosition < groupCount; groupPosition++) {
            try {
                GroupItem groupItem = dataList.get(groupPosition);
                if (groupItem==null || groupItem.getChildrenItems() == null
                        || groupItem.getChildrenItems().isEmpty()) {
                    groupCheckedStateMap.put(groupItem.getName(), 1);
                    checkedGroupList.add(groupItem);
                    continue;
                }
                
                groupCheckedStateMap.put(groupItem.getName(), 1);
                checkedGroupList.add(groupItem);
                List<ChildrenItem> childrenItems = groupItem.getChildrenItems();
                for (ChildrenItem childrenItem : childrenItems) {
                    checkedChildren.add(childrenItem.getName());
                }
            } catch (Exception e) {
                LogUtil.e(this, e);
            }
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        final GroupItem groupItem = dataList.get(groupPosition);
        if (groupItem==null || groupItem.getChildrenItems()==null
                || groupItem.getChildrenItems().isEmpty()) {
            return null;
        }
        return groupItem.getChildrenItems().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition,
            boolean isLastChild, View convertView, final ViewGroup parent) {
        ChildrenItem childrenItem = (ChildrenItem) getChild(groupPosition, childPosition);
        
        ChildViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.children_item, null);
            viewHolder = new ChildViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }
        viewHolder.children_alias.setText(childrenItem.getAlias());
        final String childrenId = childrenItem.getName();
        viewHolder.children_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    
                    if (!checkedChildren.contains(childrenId)) {
                        checkedChildren.add(childrenId);
                    }
                }else {
                    checkedChildren.remove(childrenId);
                }
                setGroupItemCheckedState(dataList.get(groupPosition));
                InspectSelectListAdapter.this.notifyDataSetChanged();
            }
        });
        if (checkedChildren.contains(childrenId)) {
            viewHolder.children_cb.setChecked(true);
            viewHolder.children_cb.setButtonDrawable(R.drawable.checkbox_hl);
        }else {
            viewHolder.children_cb.setChecked(false);
            viewHolder.children_cb.setButtonDrawable(R.drawable.checkbox_nor);
        }
        
        return convertView;
    }
    
    @Override
    public int getChildrenCount(int groupPosition) {
        final GroupItem groupItem = dataList.get(groupPosition);
        if (groupItem== null || groupItem.getChildrenItems()==null
                || groupItem.getChildrenItems().isEmpty()) {
            return 0;
        }
        return groupItem.getChildrenItems().size();
    }
    @Override
    public Object getGroup(int groupPosition) {
        if (dataList==null) {
            return null;
        }
        return dataList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        if (dataList==null) {
            return 0;
        }
        return dataList.size();
    }
    
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        try {
            GroupItem groupItem = dataList.get(groupPosition);
            
            GroupViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.group_item, null);
                viewHolder = new GroupViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (GroupViewHolder) convertView.getTag();
            }
           
            viewHolder.groupCBLayout.setOnClickListener(new GroupCBLayoutOnClickListener(viewHolder.groupCBImg,groupItem));
            viewHolder.groupNameTV.setText(groupItem.getAlias());
            if (!ListUtil.isEmpty(groupItem.getChildrenItems())) {
                int state = groupCheckedStateMap.get(groupItem.getName());
                switch (state) {
                case 1:
                    viewHolder.groupCBImg.setImageResource(R.drawable.checkbox_hl);
                    break;
                case 2:
                    viewHolder.groupCBImg.setImageResource(R.drawable.ck_partial_checked);
                    break;
                case 3:
                    viewHolder.groupCBImg.setImageResource(R.drawable.checkbox_nor);
                    break;
                default:
                    break;
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return convertView;
    }
    
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public static class GroupViewHolder {
        TextView groupNameTV;
        ImageView groupCBImg;
        LinearLayout groupCBLayout;
        public GroupViewHolder(View view) {
            super();
            this.groupNameTV = (TextView) view.findViewById(R.id.group_name);
            this.groupCBImg = (ImageView) view.findViewById(R.id.group_cb_img);
            this.groupCBLayout = (LinearLayout) view.findViewById(R.id.cb_layout);
        }
    }
    
    public static class ChildViewHolder {
        public TextView children_alias;
        public CheckBox children_cb;

        public ChildViewHolder(View view) {
            super();
            this.children_alias = (TextView) view.findViewById(R.id.children_alias);
            this.children_cb = (CheckBox) view.findViewById(R.id.children_cb);
        }
    }
    
    private void setGroupItemCheckedState(GroupItem groupItem){
        List<ChildrenItem> childrenItems = groupItem.getChildrenItems();
//        if (childrenItems==null || childrenItems.isEmpty()) {
//            groupCheckedStateMap.put(groupItem.getName(), 3);
//            return;
//        }
        
        int  checkedCount = 0;
        for (ChildrenItem childrenItem : childrenItems) {
            if (checkedChildren.contains(childrenItem.getName())) {
                checkedCount ++;
            }
        }
        int state = 1;
        if (checkedCount==0) {
            state = 3;
        }else if (checkedCount==childrenItems.size()) {
            state = 1;
        }else {
            state = 2;
        }
        
        groupCheckedStateMap.put(groupItem.getName(), state);
    }
    
    public class GroupCBLayoutOnClickListener implements OnClickListener{
        private GroupItem groupItem;
        private ImageView iv;
        public GroupCBLayoutOnClickListener(ImageView iv,GroupItem groupItem){
            this.groupItem = groupItem;
            this.iv = iv;
        }
        @Override
        public void onClick(View v) {
            List<ChildrenItem> childrenItems = groupItem.getChildrenItems();
            if (childrenItems==null || childrenItems.isEmpty()) {
                //对于没有item的组
                int state = groupCheckedStateMap.get(groupItem.getName());
                if (state == 3) {
                    groupCheckedStateMap.put(groupItem.getName(), 1);
                    iv.setImageResource(R.drawable.checkbox_hl);
                }else if (state == 1) {
                    groupCheckedStateMap.put(groupItem.getName(), 3);
                    iv.setImageResource(R.drawable.checkbox_nor);
                }
                return;
            }
            //有item的组
            int  checkedCount = 0;
            for (ChildrenItem childrenItem : childrenItems) {
                if (checkedChildren.contains(childrenItem.getName())) {
                    checkedCount ++;
                }
            }
            boolean checked = false;
            if (checkedCount==childrenItems.size()) {
                checked = false;
                groupCheckedStateMap.put(groupItem.getName(), 3);
            }else{
                checked = true;
                groupCheckedStateMap.put(groupItem.getName(), 1);
            }
            for (ChildrenItem childrenItem : childrenItems) {
                String holderKey = childrenItem.getName();
                if (checked) {
                    if (!checkedChildren.contains(holderKey)) {
                        checkedChildren.add(holderKey);
                    }
                }else {
                    checkedChildren.remove(holderKey);
                }
            }
            InspectSelectListAdapter.this.notifyDataSetChanged();
        }
    }
    
    public List<String> getCheckedRecords() {
        return checkedChildren;
    }
    
    public List<String> getCheckedChildren() {
        return checkedChildren;
    }

    public Map<String, Integer> getGroupCheckedStateMap() {
        return groupCheckedStateMap;
    }
}
