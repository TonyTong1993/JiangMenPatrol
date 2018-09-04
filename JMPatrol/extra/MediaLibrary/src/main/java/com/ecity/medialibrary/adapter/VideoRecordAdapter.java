package com.ecity.medialibrary.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.medialibrary.R;
import com.ecity.medialibrary.model.VideoModel;
import com.z3app.android.util.StringUtil;

public class VideoRecordAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private boolean showLockImage;
    private List<VideoModel> dataSource;
    private boolean supportPhotoDescription;

    /**
     * @param context
     * @param dataSource
     */
    public VideoRecordAdapter(Context context, List<VideoModel> dataSource) {
        super();
        this.mContext = context;
        this.dataSource = dataSource;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataSource.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.image_grid_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == dataSource.size()) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_addvideo_unfocused));
            if (position == 9) {
                holder.image.setVisibility(View.GONE);
            }
            holder.imgPlay.setVisibility(View.GONE);
            holder.tvDescription.setVisibility(View.GONE);
        } else {
            VideoModel model = dataSource.get(position);
            if (model.isReadOnly()) {
                holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.video_holder));
                holder.imgPlay.setVisibility(View.VISIBLE);
            } else {
                holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.video_holder));
                String pathTemp = dataSource.get(position).getPath();
                pathTemp = pathTemp.substring(pathTemp.lastIndexOf("/") + 1);
                if (pathTemp.startsWith("VID")) {
                    holder.imgPlay.setVisibility(View.VISIBLE);
                }

                if (!isSupportPhotoDescription()) {
                    holder.tvDescription.setVisibility(View.GONE);
                } else {
                    if (!StringUtil.isBlank(model.getDescription())) {
                        holder.tvDescription.setText(model.getDescription());
                    }
                }
            }
        }

        return convertView;
    }

    class ViewHolder {
        public ImageView image;
        public ImageView imgPlay;
        public TextView tvDescription;

        public ViewHolder(View convertView) {
            super();
            image = (ImageView) convertView.findViewById(R.id.image_view);
            imgPlay = (ImageView) convertView.findViewById(R.id.img_play);
            tvDescription = (TextView) convertView.findViewById(R.id.tv_description);
        }
    }

    public boolean isShowLockImage() {
        return showLockImage && (dataSource.size() > 0);
    }

    public void setShowLockImage(boolean showLockImage) {
        this.showLockImage = showLockImage;
    }

    public void setDataSource(List<VideoModel> dataSource) {
        this.dataSource = dataSource;
        notifyDataSetChanged();
    }

    public List<VideoModel> getDataSource() {
        return dataSource;
    }

    public boolean isSupportPhotoDescription() {
        return supportPhotoDescription;
    }

    public void setSupportPhotoDescription(boolean supportPhotoDescription) {
        this.supportPhotoDescription = supportPhotoDescription;
    }
}
