package com.ecity.cswatersupply.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.metaconfig.QueryDbMetaNet;
import com.ecity.cswatersupply.utils.ListUtil;

import java.util.List;

public class QueryLayerAdapter extends ArrayListAdapter<QueryDbMetaNet> {
    private LayoutInflater mInflater;

    public QueryLayerAdapter(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        List<QueryDbMetaNet> nets = getList();
        if(ListUtil.isEmpty(nets)) {
            return convertView;
        }
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_query_layer_control, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.layerName.setText(nets.get(position).getDname());
        if(nets.get(position).isSelected()) {
            holder.imageView.setImageResource(R.drawable.ic_checkbox_checked);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_checkbox_unchecked);
        }
        return convertView;
    }

    class ViewHolder {
        private TextView layerName;
        private ImageView imageView;

        public ViewHolder(View convertView) {
            this.layerName = (TextView) convertView.findViewById(R.id.layer_name);
            this.imageView = (ImageView) convertView.findViewById(R.id.select_layer);
        }
    }
}
