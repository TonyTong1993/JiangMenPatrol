package com.ecity.cswatersupply.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.ResourceUtil;

@SuppressWarnings("deprecation")
public class ImageAdapter extends BaseAdapter {
    private Context context;
    private List<String> paths;
    private boolean isImage;

    public ImageAdapter(Context context, List<String> paths, boolean isImage) {
        this.context = context;
        this.paths = paths;
        this.isImage = isImage;
    }

    public boolean isImage() {
        return isImage;
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object getItem(int position) {
        return paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image = new ImageView(context);
        String url = paths.get(position);
        int width = ResourceUtil.getDimensionPixelSizeById(R.dimen.workorder_morebtn_popmenu_w);
        if (isImage) {
            Glide.with(context).load(url).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).fallback(R.drawable.ic_launcher).into(image);
        } else {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.video_holder));
        }
        image.setLayoutParams(new Gallery.LayoutParams(width, width));

        return image;
    }
}
