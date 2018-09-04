package com.ecity.cswatersupply.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.MemoryInfoModel;

/**
 * 2017-1-24
 * @author Gxx
 *
 */
public class MemoryListExpdListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> memoType;
    private Map<String, List<MemoryInfoModel>> memoList;
    private IExpdCheckBoxCallBack callBack;

    public interface IExpdCheckBoxCallBack {
        void onChlidCheckBoxClick(MemoryInfoModel child);
    }

    public MemoryListExpdListAdapter(Context context,List<String> groups, Map<String, List<MemoryInfoModel>> childList,IExpdCheckBoxCallBack callBack) {
        super();
        this.context = context;
        this.memoType = groups;
        this.memoList = childList;
        this.callBack = callBack;
    }

    public void setData(List<String> groups, Map<String, List<MemoryInfoModel>> childList) {
        this.memoType = groups;
        this.memoList = childList;
    }

    @Override
    public int getGroupCount() {
        return memoType.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return memoList.get(memoType.get(groupPosition).toString()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return memoType.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return memoList.get(memoType.get(groupPosition).toString()).get(childPosition);
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
            convertView = inflater.inflate(R.layout.item_memorylist_group, null); 
            groupHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupViewHolder) convertView.getTag();
        }
        if(isExpanded) {
            groupHolder.imageView.setBackgroundResource(R.drawable.arrow_down);
        } else {
            groupHolder.imageView.setBackgroundResource(R.drawable.arrow_right);
        }
        groupHolder.textGroup.setText(memoType.get(groupPosition));  // 设置大组成员名称  
        return convertView; 
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childHolder = null;
        if (null == convertView) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_memorylist_child, null); 
            childHolder = new ChildViewHolder(convertView);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildViewHolder) convertView.getTag();
        }

        MemoryInfoModel child = memoList.get(memoType.get(groupPosition)).get(childPosition);
        String name = child.getMemoryName();
        if (name.equalsIgnoreCase("images")) {
            childHolder.textChild.setText(context.getString(R.string.system_settings_clear_memory_images));
        } else if (name.equalsIgnoreCase("videos")) {
            childHolder.textChild.setText(context.getString(R.string.system_settings_clear_memory_videos));
        } else if (name.equalsIgnoreCase("audios")) {
            childHolder.textChild.setText(context.getResources().getString(R.string.system_settings_clear_memory_audios));
        } else {
            childHolder.textChild.setText(name);
        }
        if(child.isCheck()) {
            childHolder.childImageView.setBackgroundResource(R.drawable.checkbox_hl);
        } else {
            childHolder.childImageView.setBackgroundResource(R.drawable.checkbox_nor);
        }

        childHolder.childImageView.setOnClickListener(new OnSelectBtnClickListener(child, callBack));
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
        private ImageView childImageView;

        public ChildViewHolder(View view) {
            textChild = (TextView) view.findViewById(R.id.textChild);
            childImageView = (ImageView) view.findViewById(R.id.childImageView);
        }
    }

    class OnSelectBtnClickListener implements OnClickListener {
        private MemoryInfoModel child;
        private IExpdCheckBoxCallBack listener;

        public OnSelectBtnClickListener(MemoryInfoModel child, IExpdCheckBoxCallBack listener) {
            this.child = child;
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            if(child.isCheck()) {
                child.setCheck(false);
                v.setBackgroundResource(R.drawable.checkbox_nor);
            } else {
                child.setCheck(true);
                v.setBackgroundResource(R.drawable.checkbox_hl);
            }
            if(null != listener){
                listener.onChlidCheckBoxClick(child);
            }
        }
    }

}
