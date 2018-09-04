package com.ecity.cswatersupply.adapter.checkitem;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.afilechooser.FileChooserActivity2;
import com.ecity.afilechooser.utils.FileUtils2;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.utils.DateUtil;

/**
 * 
 * @author SunShan'ai
 * 附件选择的适配器
 */
public class AttachmentsListAdapter extends BaseAdapter {
    public static final String PATHS = FileChooserActivity2.PATHS;
    private static final int TYPE_FIRST = 0;
    private static final int TYPE_SECOND = 1;

    private ArrayList<File> mFiles = new ArrayList<File>();
    private LayoutInflater mInflater = null;
    private Activity mActivity;

    public AttachmentsListAdapter(Activity activity, ArrayList<File> files) {
        mInflater = LayoutInflater.from(activity);
        this.mActivity = activity;
        this.mFiles = files;
    }

    @Override
    public int getCount() {
        return mFiles.size() + 1;
    }

    @Override
    public File getItem(int position) {
        return mFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == (mFiles.size())) {
            return TYPE_SECOND;
        } else {
            return TYPE_FIRST;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        File file = null;
        if (null != mFiles && position < mFiles.size()) {
            file = mFiles.get(position);
        }
        ViewHolderFirst viewHolder1 = null;
        ViewHolderSecond viewHolder2 = null;
        int type = getItemViewType(position);
        if (null == convertView) {
            switch (type) {
                case TYPE_FIRST:
                    convertView = mInflater.inflate(R.layout.item_attachmentlist, null);
                    viewHolder1 = new ViewHolderFirst(convertView);
                    convertView.setTag(viewHolder1);
                    break;
                case TYPE_SECOND:
                    convertView = mInflater.inflate(R.layout.custom_form_file_list_item, null);
                    viewHolder2 = new ViewHolderSecond(convertView);
                    convertView.setTag(viewHolder2);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case TYPE_FIRST:
                    viewHolder1 = (ViewHolderFirst) convertView.getTag();
                    break;
                case TYPE_SECOND:
                    viewHolder2 = (ViewHolderSecond) convertView.getTag();
                    break;
                default:
                    break;
            }
        }

        switch (type) {
            case TYPE_FIRST:
                viewHolder1.iv_file.setImageResource(R.drawable.file);
                viewHolder1.tv_fileName.setText(file.getName());
                viewHolder1.tv_fileCreateTime.setText(DateUtil.fromLong(file.lastModified()));
                break;
            case TYPE_SECOND:
                viewHolder2.iv_add.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FileUtils2.mFileFileterBySuffixs.acceptSuffixs(null);
                        Intent intent = new Intent(AttachmentsListAdapter.this.mActivity, FileChooserActivity2.class);
                        mActivity.startActivityForResult(intent, RequestCode.SELECT_ATTACHMENT);
                    }
                });
                break;
            default:
                break;
        }

        return convertView;
    }

    static class ViewHolderFirst {
        private TextView tv_fileName;
        private TextView tv_fileCreateTime;
        private ImageView iv_file;

        public ViewHolderFirst(View view) {
            super();
            this.iv_file = (ImageView) view.findViewById(R.id.iv_file);
            this.tv_fileName = (TextView) view.findViewById(R.id.tv_file_name);
            this.tv_fileCreateTime = (TextView) view.findViewById(R.id.tv_file_create_time);
        }
    }

    static class ViewHolderSecond {
        private ImageView iv_add;

        public ViewHolderSecond(View view) {
            this.iv_add = (ImageView) view.findViewById(R.id.iv_add);
        }
    }

    public void remove(int position) {
        notifyDataSetChanged();
    }

    public void updatePlayState(int position, int state) {
        notifyDataSetChanged();
    }

    public ArrayList<File> getFiles() {
        return mFiles;
    }

    public void setFiles(ArrayList<File> files) {
        this.mFiles = files;
    }

    public void addAllFiles(ArrayList<File> files) {
        if (null != mFiles) {
            for (int i = 0; i < files.size(); i++) {
                if (!mFiles.contains(files.get(i))) {
                    mFiles.add(files.get(i));
                }
            }
        }
    }
}
