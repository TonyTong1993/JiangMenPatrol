package com.ecity.cswatersupply.adapter.planningtask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.model.planningTask.PointPartAttrInfo.Attr;

public class AttrListAdapter extends ArrayListAdapter<Object> {

    private LayoutInflater mInflater = null;

    public AttrListAdapter(Context context) {
        super(context);
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.lv_item_task_attr_info, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (null == mList || position > mList.size()) {
            return convertView;
        }
        Attr attr = (Attr) mList.get(position);
        viewHolder.tv_attr_title.setText(attr.getAttrKey());
        viewHolder.tv_attr_info.setText(attr.getAttrValue());
        return convertView;
    }

    public static class ViewHolder {
        public TextView tv_attr_title, tv_attr_info;

        public ViewHolder(View v) {
            tv_attr_title = (TextView) v.findViewById(R.id.tv_attr_title);
            tv_attr_info = (TextView) v.findViewById(R.id.tv_attr_info);
        }
    }
}
