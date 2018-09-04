package com.ecity.cswatersupply.ui.inpsectitem;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.checkitem.AttachmentsListAdapter;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.ui.widght.ListViewForScrollView;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.zzz.ecity.android.applibrary.dialog.AlertView;
import com.zzz.ecity.android.applibrary.dialog.AlertView.AlertStyle;
import com.zzz.ecity.android.applibrary.dialog.AlertView.OnAlertViewListener;

public class AttachmentInspectItemViewXtd extends ABaseInspectItemView {
    private ArrayList<File> mFiles = new ArrayList<File>();
    private AttachmentsListAdapter mAttachmentsAdapter;

    @Override
    protected void setup(View contentView) {
        ListViewForScrollView lvAddachment = (ListViewForScrollView) contentView.findViewById(R.id.lv_attachments);
        lvAddachment.setOnItemLongClickListener(new FileListViewItemClickListener());
        mAttachmentsAdapter = new AttachmentsListAdapter(context, mFiles);
        lvAddachment.setAdapter(mAttachmentsAdapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_attachment_select;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == RequestCode.SELECT_ATTACHMENT) && (data != null)) {
            @SuppressWarnings("unchecked")
            ArrayList<File> files = (ArrayList<File>) data.getSerializableExtra(CustomViewInflater.PATHS);
            mAttachmentsAdapter.addAllFiles(files);
            mAttachmentsAdapter.notifyDataSetChanged();
            setAttachmentsValue();
        }
    }

    private class FileListViewItemClickListener implements OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            if (position == mFiles.size()) {
                return true;
            }

            String title = context.getString(R.string.dialog_title_prompt);
            String message = context.getString(R.string.event_report_delete_item_tips);
            AlertView dialog = new AlertView(context, title, message, new OnAlertViewListener() {

                @Override
                public void back(boolean result) {
                    if (!result) {
                        return;
                    }

                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mFiles.remove(position);
                            setAttachmentsValue();
                            mAttachmentsAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }, AlertStyle.OK_CANCEL);
            dialog.show();

            return true;
        }
    }

    private void setAttachmentsValue() {
        ArrayList<File> files = mAttachmentsAdapter.getFiles();
        StringBuilder sbPaths = new StringBuilder();
        for (int i = 0; i < files.size(); i++) {
            sbPaths.append(files.get(i)).append(",");
            if (i == files.size() - 1) {
                sbPaths.deleteCharAt(sbPaths.length() - 1);
            }
        }

        mInspectItem.setValue(sbPaths.toString());
    }
}
