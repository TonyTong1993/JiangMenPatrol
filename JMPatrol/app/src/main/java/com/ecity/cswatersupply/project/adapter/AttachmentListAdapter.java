package com.ecity.cswatersupply.project.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.network.FileDownloader;
import com.ecity.cswatersupply.project.activity.ProjectDetailFragmentActivity;
import com.ecity.cswatersupply.project.model.Attachment;
import com.ecity.cswatersupply.project.service.AttachmentBusinessService;
import com.ecity.medialibrary.utils.FileLoader;

public class AttachmentListAdapter extends ArrayListAdapter<Attachment> {
    private LayoutInflater inflater;

    public AttachmentListAdapter(Activity context) {
        super(context);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Attachment document = getList().get(position);

        ViewHolder holder = null;
        if ((null == convertView) || (!document.getUniqueName().equals(convertView.getTag(R.id.lv_item_view_tag_key)))) {
            convertView = inflater.inflate(R.layout.lv_item_document, null);
            holder = new ViewHolder(convertView, document);
            convertView.setTag(R.id.lv_item_view_tag_key, document.getUniqueName());
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.initUI();

        return convertView;
    }

    static class ViewHolder {
        private TextView tvFirst;
        private TextView tvSecond;
        private Button btnDownload;
        private Attachment document;

        public ViewHolder(View view, Attachment document) {
            super();
            this.tvFirst = (TextView) view.findViewById(R.id.tv_first);
            this.tvSecond = (TextView) view.findViewById(R.id.tv_second);
            this.btnDownload = (Button) view.findViewById(R.id.btn_download);
            this.document = document;

            initUI();
            setListener();
        }

        public void initUI() {
            tvFirst.setText(document.getName());
            tvSecond.setText(document.getCreatedOn());
            if (document.isDownloaded()) {
                btnDownload.setText(R.string.documents_download_open);
            } else {
                int textId = document.isDownloading() ? R.string.documents_downloading : R.string.documents_download;
                btnDownload.setText(textId);
            }
            btnDownload.setEnabled(!document.isDownloading());
        }

        private void setListener() {
            btnDownload.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (document.isDownloaded()) {
                        FileLoader.openFile(HostApplication.getApplication(), document.getLocalPath());
                    } else {
                        document.setDownloading(true);
                        btnDownload.setText(R.string.documents_downloading);
                        btnDownload.setEnabled(false);
                        HostApplication.getApplication().addDownloadingFile(document.getUniqueName());
                        String path = FileLoader.getDownloadedPath() + "/" + document.getName();
                        FileDownloader.execute(document.getUrl(), path, ProjectDetailFragmentActivity.class);
                        document.setLocalPath(path); // Cache local path here, will need it to save in db when download is done.
                        AttachmentBusinessService.getInstance().markAsDownloading(document.getUniqueName());
                        AttachmentBusinessService.getInstance().saveLocalPath(document.getUniqueName(), document.getLocalPath());
                    }
                }
            });
        }

    }
}
