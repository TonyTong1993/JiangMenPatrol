package com.ecity.cswatersupply.emergency.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.model.EmergencyItem;

public class ExpertAdapter extends BaseAdapter {
    private List<EmergencyItem> models;
    private LayoutInflater inflater;
    private Context mContext;

    public ExpertAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);

    }

    public ExpertAdapter(Context context, List<EmergencyItem> models) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        this.models = models;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.opinion_item_list, null);
            viewHolder = new ViewHolder();
            viewHolder.name_of_earthquake = (TextView) convertView.findViewById(R.id.tv_first);
            viewHolder.earthquake_location = (TextView) convertView.findViewById(R.id.tv_second);
            //            viewHolder.latitude_and_longitude = (TextView) convertView.findViewById(R.id.latitude_and_longitude);
            //          viewHolder.magnitude_count = (TextView) convertView.findViewById(R.id.magnitude_count);
            //            viewHolder.opinion_expert = (TextView) convertView.findViewById(R.id.opinion_expert);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String string2 = models.get(position).getImgurl().toString();
        String substring1 = string2.substring(string2.indexOf("分") + 1, string2.length());
        String string = models.get(position).getTitle().toString();
        String getSignInfo = string.substring(string.indexOf("[") + 1, string.indexOf("]"));
        viewHolder.name_of_earthquake.setText("震源地址：" + substring1);
        viewHolder.earthquake_location.setText("发布时间：" + getSignInfo);
        return convertView;
    }

    class ViewHolder {
        public TextView name_of_earthquake;
        public TextView earthquake_location;
        //     public TextView latitude_and_longitude;
        // public TextView magnitude_count;
        //     public TextView opinion_expert;
    }
}
