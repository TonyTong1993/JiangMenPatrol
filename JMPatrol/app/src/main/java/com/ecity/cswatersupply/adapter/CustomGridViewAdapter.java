package com.ecity.cswatersupply.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ecity.cswatersupply.R;
import com.ecity.medialibrary.utils.MediaCacheManager;

public class CustomGridViewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    public CustomGridViewAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return (MediaCacheManager.imgbmp.size() + 1);
    }

    @Override
    public Object getItem(int position) {
        return MediaCacheManager.imgbmp.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.custom_grid_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == MediaCacheManager.imgbmp.size()) {
            if (position == 9) {
                holder.imageView.setVisibility(View.GONE);
            } else {
                holder.imageView.setVisibility(View.VISIBLE);
                holder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.addphoto));
            }
        } else {
            holder.imageView.setImageBitmap(MediaCacheManager.imgbmp.get(position));
        }

        return convertView;
    }

    static class ViewHolder {
        private ImageView imageView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.item_grid_image);
        }
    }
}
