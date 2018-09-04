package com.ecity.cswatersupply.utils.tpk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;

public class MapOfflineListAdapter extends ArrayListAdapter<MapOfflineBean> {
    private LayoutInflater mInflater;
    public MapOfflineListAdapter(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final MapOfflineBean bean = getList().get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_map_offline, null);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.map_offline_name.setText(bean.getMapTpkName());
        String msg = String.format("%.1f", bean.getProgress()) + "%";
        viewHolder.map_offline_download_rate.setText(msg);
        viewHolder.map_offline_download_progressbar.setProgress((int) ((float) bean.getProgress()));
        return convertView;

    }
    
    public static class ViewHolder {
        public TextView map_offline_name, map_offline_download_rate, map_offline_btn;
        public SeekBar map_offline_download_progressbar;

        public ViewHolder(View v) {
            map_offline_name = (TextView) v.findViewById(R.id.tv_title_map_offline);
            map_offline_download_rate = (TextView) v.findViewById(R.id.map_offline_download_rate);
            map_offline_download_progressbar = (SeekBar) v.findViewById(R.id.progressbar_map_offline_dowload);
            map_offline_btn = (Button) v.findViewById(R.id.btn_download_map_offline);
        }
    }

}
