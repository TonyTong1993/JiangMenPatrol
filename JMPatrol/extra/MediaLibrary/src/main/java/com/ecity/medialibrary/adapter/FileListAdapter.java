/*
 * Copyright (C) 2012 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ecity.medialibrary.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.medialibrary.R;
import com.ecity.medialibrary.model.FileModel;

/**
 * List adapter for Files.
 * 
 * @version 2016-5-17
 * @author SunShan'ai
 */
public class FileListAdapter extends BaseAdapter {

    private final static int ICON_FOLDER = R.drawable.ic_folder_blue;
    private final static int ICON_FILE = R.drawable.ic_file_blue;

    private final LayoutInflater mInflater;

    private List<FileModel> mData = new ArrayList<FileModel>();

    private int selectTotalCount = 0;
    private int maxSelectFileCount = 1;
    private TextCallback textcallback = null;
    private Handler mHandler;
    public Map<String, String> map = new HashMap<String, String>();

    public FileListAdapter(Context context, Handler handler) {
        mInflater = LayoutInflater.from(context);
        this.mHandler = handler;
    }

    public void add(FileModel file) {
        mData.add(file);
        notifyDataSetChanged();
    }

    public void remove(FileModel file) {
        mData.remove(file);
        notifyDataSetChanged();
    }

    public void insert(FileModel file, int index) {
        mData.add(index, file);
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void setSelectTotal(int selectTotal) {
        this.selectTotalCount = selectTotal;
    }

    public void setMaxSelectFileCount(int maxSelectFileCount) {
        this.maxSelectFileCount = maxSelectFileCount;
    }

    @Override
    public FileModel getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public List<FileModel> getListItems() {
        return mData;
    }

    /**
     * Set the list items without notifying on the clear. This prevents loss of
     * scroll position.
     *
     * @param data
     */
    public void setListItems(List<FileModel> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_file_choose, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Get the file at the current position
        final FileModel file = getItem(position);
        viewHolder.tvFileInfo.setText(file.getmFile().getName());

        // If the item is not a directory, use the file icon
        int icon = file.getmFile().isDirectory() ? ICON_FOLDER : ICON_FILE;
        int visibleStatus = file.getmFile().isDirectory() ? View.GONE : View.VISIBLE;
        viewHolder.ivSelected.setVisibility(visibleStatus);
        viewHolder.tvFileInfo.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);

        if (file.isSelected) {
            viewHolder.ivSelected.setImageResource(R.drawable.icon_data_select);
            viewHolder.tvFileInfo.setBackgroundResource(R.drawable.css_relatly_line);
        } else {
            viewHolder.ivSelected.setImageBitmap(null);
            viewHolder.tvFileInfo.setBackgroundResource(0x00000000);
        }
        if (visibleStatus == View.VISIBLE) {
            viewHolder.tvFileInfo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if ((selectTotalCount) < maxSelectFileCount) {
                        file.isSelected = !file.isSelected;
                        if (file.isSelected) {
                            viewHolder.ivSelected.setImageResource(R.drawable.icon_data_select);
                            viewHolder.tvFileInfo.setBackgroundResource(R.drawable.css_relatly_line);
                            selectTotalCount++;
                            if (textcallback != null) textcallback.onListen(selectTotalCount);
                            map.put(file.getmFile().getPath(), file.getmFile().getPath());
                        } else if (!file.isSelected) {
                            viewHolder.ivSelected.setImageResource(-1);
                            viewHolder.tvFileInfo.setBackgroundResource(0x00000000);
                            selectTotalCount--;
                            if (textcallback != null) textcallback.onListen(selectTotalCount);
                            map.remove(file.getmFile().getPath());
                        }
                    } else if ((selectTotalCount) >= maxSelectFileCount) {
                        if (file.isSelected == true) {
                            file.isSelected = !file.isSelected;
                            viewHolder.ivSelected.setImageResource(-1);
                            viewHolder.tvFileInfo.setBackgroundResource(0x00000000);
                            selectTotalCount--;
                            if (textcallback != null) textcallback.onListen(selectTotalCount);
                            map.remove(file.getmFile().getPath());
                        } else {
                            Message message = Message.obtain(mHandler, 0);
                            message.sendToTarget();
                        }
                    }
                }
            });
        }


        return convertView;
    }

    static class ViewHolder {
        private TextView tvFileInfo;
        private ImageView ivSelected;

        public ViewHolder(View convertView) {
            tvFileInfo = (TextView) convertView.findViewById(R.id.tv_fileinfo);
            ivSelected = (ImageView) convertView.findViewById(R.id.iv_selected_icon);
        }
    }

    public interface TextCallback {
        void onListen(int count);
    }

    public void setTextCallback(TextCallback listener) {
        textcallback = listener;
    }

}