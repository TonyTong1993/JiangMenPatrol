package com.ecity.cswatersupply.project.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.project.model.Attachment;
import com.ecity.cswatersupply.utils.ListUtil;
import com.z3app.android.util.StringUtil;

public class AttachmentUploadAdapter extends ArrayListAdapter<Attachment> {
    private LayoutInflater inflater;
    private InspectItem inspectItem;

    public AttachmentUploadAdapter(Activity context, InspectItem inspectItem) {
        super(context);
        this.inspectItem = inspectItem;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Attachment document = getList().get(position);

        ViewHolder holder = null;
        if ((null == convertView)) {
            convertView = inflater.inflate(R.layout.lv_item_document, null);
            holder = new ViewHolder(convertView);
            holder.btnDownload.setTag(position);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.btnDownload.setTag(position);
        }

        holder.initUI();
        holder.tvFirst.setText(document.getName());
        holder.btnDownload.setOnClickListener(new MyOnClickListener(holder) {
            @Override
            public void onClick(View v, ViewHolder holder) {
                int deletePosition = (Integer) holder.btnDownload.getTag();
                AttachmentUploadAdapter.this.getList().remove(deletePosition);
                AttachmentUploadAdapter.this.notifyDataSetChanged();
                setAttachValue();
            }
        });

        return convertView;
    }

    private void setAttachValue() {
        if (ListUtil.isEmpty(getList())) {
            return;
        }

        StringBuilder sbPaths = new StringBuilder();
        for (Attachment path : getList()) {
            if (!StringUtil.isBlank(sbPaths.toString())) {
                sbPaths.append(Constants.IMAGE_SPLIT_STRING);
            }
            sbPaths.append(path.getLocalPath());
        }

        inspectItem.setValue(sbPaths.toString());
    }

    private abstract class MyOnClickListener implements OnClickListener {

        private ViewHolder mHolder;

        public MyOnClickListener(ViewHolder holder) {
            this.mHolder = holder;
        }

        @Override
        public void onClick(View v) {
            onClick(v, mHolder);
        }

        public abstract void onClick(View v, ViewHolder holder);

    }

    static class ViewHolder {
        private TextView tvFirst;
        private TextView tvSecond;
        private Button btnDownload;

        public ViewHolder(View view) {
            super();
            this.tvFirst = (TextView) view.findViewById(R.id.tv_first);
            this.tvSecond = (TextView) view.findViewById(R.id.tv_second);
            this.btnDownload = (Button) view.findViewById(R.id.btn_download);

            initUI();
        }

        public void initUI() {
            btnDownload.setText("移除");
        }
    }
}
