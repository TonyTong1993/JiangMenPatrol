package com.ecity.cswatersupply.ui.inpsectitem;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.project.adapter.AttachmentUploadAdapter;
import com.ecity.cswatersupply.project.model.Attachment;
import com.ecity.cswatersupply.ui.widght.ListViewForScrollView;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.z3app.android.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AttachmentViewItemUploadXtd extends ABaseInspectItemView {
    private TextView tv_item_value;
    private ListViewForScrollView lvAddachment;
    private String guid;
    private AttachmentUploadAdapter attachmentUploadAdapter;
    private List<Attachment> list = new ArrayList<>();


    @Override
    protected void setup(View contentView) {
//        EventBusUtil.register(this);
        lvAddachment = (ListViewForScrollView) contentView.findViewById(R.id.lv_attachments);
        tv_item_value = (TextView) contentView.findViewById(R.id.tv_item_value);
        tv_item_value.setText("点击选择附件");
        guid = mInspectItem.getValue();

        attachmentUploadAdapter = new AttachmentUploadAdapter(context, mInspectItem);
        lvAddachment.setAdapter(attachmentUploadAdapter);


        setOnClickListener();
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_attachment_select;
    }

    private void setOnClickListener() {
        tv_item_value.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor actualimagecursor = context.managedQuery(uri, proj, null, null, null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String file_path = actualimagecursor.getString(actual_image_column_index);
            File file = new File(file_path);

            String fileName = getFileName(file_path);
            ToastUtil.show(fileName, 0);

            displayAttachList(fileName, file_path);
            setAttachValue();
        }
    }

    private String getFileName(String path) {
        String fileName = path.substring(path.lastIndexOf("/") + 1, path.length());
        return fileName;
    }


    private void displayAttachList(String name, String path) {

        Attachment attach = new Attachment();
        attach.setLocalPath(path);
        attach.setName(name);
        list.add(attach);
        attachmentUploadAdapter.setList(list);
    }

    private void setAttachValue() {
        if (ListUtil.isEmpty(attachmentUploadAdapter.getList())) {
            return;
        }

        StringBuilder sbPaths = new StringBuilder();
        for (Attachment path : attachmentUploadAdapter.getList()) {
            if (!StringUtil.isBlank(sbPaths.toString())) {
                sbPaths.append(Constants.IMAGE_SPLIT_STRING);
            }
            sbPaths.append(path.getLocalPath());
        }

        mInspectItem.setValue(sbPaths.toString());
    }

//    public void onEventMainThread(ResponseEvent event) {
//        if (!event.isOK()) {
//            LoadingDialogUtil.dismiss();
//            ToastUtil.showLong(event.getMessage());
//
//            return;
//        }
//
//        switch (event.getId()) {
////            case ResponseEventStatus.PROJECT_GET_PROJECT_ATTACHMENT:
////                LoadingDialogUtil.dismiss();
////                break;
//            default:
//                break;
//        }
//    }

}
