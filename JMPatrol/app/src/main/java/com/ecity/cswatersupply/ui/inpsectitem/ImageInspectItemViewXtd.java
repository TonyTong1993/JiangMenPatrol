package com.ecity.cswatersupply.ui.inpsectitem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.CustomGridViewAdapter;
import com.ecity.cswatersupply.adapter.ImageAdapter;
import com.ecity.cswatersupply.adapter.checkitem.AudiosDetailListAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.FileDownloader;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.ui.widght.GridViewForScrollView;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.view.FullScreenPhotoActivity;
import com.ecity.medialibrary.activity.AlbumSelectActivity;
import com.ecity.medialibrary.activity.PhotoActivity;
import com.ecity.medialibrary.utils.ImageDimenCompressUtil;
import com.ecity.medialibrary.utils.MediaCacheManager;
import com.ecity.medialibrary.utils.MediaHelper;
import com.ecity.medialibrary.utils.StartCameraUtil;
import com.ecity.medialibrary.utils.TransPixelUtil;
import com.ecity.medialibrary.utils.WatermarkUtil;
import com.z3app.android.util.FileUtil;
import com.z3app.android.util.PreferencesUtil;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.utils.ListUtil;
import com.zzz.ecity.android.applibrary.view.ActionSheet;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ImageInspectItemViewXtd extends ABaseInspectItemView {
    private CustomGridViewAdapter mImageAdapter;
    private List<String> imagesInspectItemValues = new ArrayList<String>();
    private String currentPhotoName;
    private GridViewForScrollView gridViewImage;

    @Override
    protected void setup(View contentView) {
        EventBusUtil.register(this);
        ViewStub imgStub = (ViewStub) contentView.findViewById(R.id.viewStub_galley);
        imgStub.inflate();
        Gallery imgGallery = (Gallery) contentView.findViewById(R.id.gallery1);
        gridViewImage = (GridViewForScrollView) contentView.findViewById(R.id.gridviewImage);
        inflateImage(mInspectItem, imgGallery);
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_image_select;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.SELECT_PHOTO) {
            updateImageFile();
        } else if (requestCode == RequestCode.EDIT_PHOTO) {
            refreshImageData();
        } else if (requestCode == StartCameraUtil.CODE_TAKE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                String path = WatermarkUtil.saveBitmapWithWatermark(context, currentPhotoName, MediaHelper.getImagePath());
                MediaCacheManager.imgdrr.add(path);
                updateImageFile();
                refreshImageFolders();
            }
        }
    }

    private void inflateImage(InspectItem item, Gallery gallery) {
        if (item.isEdit()) {
            gridViewImage.setVisibility(View.VISIBLE);
            mImageAdapter = new CustomGridViewAdapter(context);
            gridViewImage.setAdapter(mImageAdapter);
            gridViewImage.setOnItemClickListener(new MyGridViewItemClickListener(EInspectItemType.IMAGE));
            // 填充上次缓存的照片
            if (!item.getValue().isEmpty()) {
                //上传的照片路径以字符串IMAGE_SPLIT_STRING为分割
//                String[] strs = item.getValue().split(Constants.IMAGE_SPLIT_STRING);
//                List<String> imagesPaths = Arrays.asList(strs);
//                MediaCacheManager.imgdrr.addAll(imagesPaths);
//                updateImageFile();
                loadValues(item);
            }
        } else {
            gridViewImage.setVisibility(View.GONE);
            if (!item.getValue().isEmpty()) {
                //
                String[] strs = item.getValue().split(",");
                final List<String> imagesPaths = new ArrayList<String>(Arrays.asList(strs));
                // 服务返回的是图片名字，本地组装成完整路径
                for (int i = 0; i < imagesPaths.size(); i++) {
                    String path = ServiceUrlManager.getInstance().getImageUrl() + imagesPaths.get(i);
                    imagesPaths.set(i, path);
                }
                ImageAdapter adapter = new ImageAdapter(context, imagesPaths, true);
                gallery.setAdapter(adapter);
                gallery.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList(FullScreenPhotoActivity.KEY_PATH, (ArrayList<String>) imagesPaths);
                        bundle.putInt(FullScreenPhotoActivity.KEY_POSITION, position);
                        UIHelper.startActivityWithExtra(FullScreenPhotoActivity.class, bundle);
                    }
                });
            }
        }
    }

    private void loadValues(InspectItem item) {
        String[] strs = item.getValue().split(",");
        if(null == strs || 0 == strs.length) {
            return;
        }
        final List<String> imagesPaths = new ArrayList<String>(Arrays.asList(strs));
        // 服务返回的是图片名字，本地组装成完整路径
        for (int i = 0; i < imagesPaths.size(); i++) {
            String fileUrl = ServiceUrlManager.getInstance().getFZImageUrl() + imagesPaths.get(i);
            String targetPath = MediaHelper.getImagePath() + System.currentTimeMillis() + ".jpg";
            //目标文件已存在但没有下载成功，则删除
            File sourceFile = new File(targetPath);
            FileUtil.deleteFile(sourceFile);
            FileDownloader.execute(fileUrl, targetPath, CustomReportActivity1.class);
        }
    }

    private class MyGridViewItemClickListener implements OnItemClickListener {
        private String[] menus;

        public MyGridViewItemClickListener(EInspectItemType type) {
            menus = context.getResources().getStringArray(R.array.custom_action_sheet_image_menus);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            showImageMenu(position);
        }

        private void showImageMenu(int position) {
            if (position == MediaCacheManager.imgbmp.size()) {
                ActionSheet.show(context, context.getString(R.string.action_menu_title), menus, new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                takePhoto();
                                ActionSheet.dismiss();
                                break;
                            case 1:
                                selectPicture();
                                ActionSheet.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
            } else {
                Intent intent = new Intent(context, PhotoActivity.class);
                intent.putExtra("ID", position);
                startActivityForResult(intent, RequestCode.EDIT_PHOTO);
            }
        }

        private void takePhoto() {
            mCustomInflater.setPendingInspectItemView(ImageInspectItemViewXtd.this);
            CustomViewInflater.pendingViewInflater = mCustomInflater;
            currentPhotoName = System.currentTimeMillis() + ".jpg";
            StartCameraUtil.launchCamera(context, StartCameraUtil.CODE_TAKE_PHOTO, MediaHelper.getImagePath(), currentPhotoName);
        }

        private void selectPicture() {
            if (null != MediaCacheManager.imgdrr) {
                MediaCacheManager.imgdrr.clear();
            }
            if (null != imagesInspectItemValues) {
                int size = imagesInspectItemValues.size();
                for (int i = 0; i < size; i++) {
                    if (!MediaCacheManager.imgdrr.contains(imagesInspectItemValues.get(i))) {
                        MediaCacheManager.imgdrr.add(imagesInspectItemValues.get(i));
                    }
                }
            }

            ArrayList<String> preferredAlbumNames = getPreferredAlbumNames();
            Intent intent = new Intent(context, AlbumSelectActivity.class);
            intent.putStringArrayListExtra(AlbumSelectActivity.INTENT_KEY_PREFERRED_ALBUM_NAMES, preferredAlbumNames);
            startActivityForResult(intent, RequestCode.SELECT_PHOTO);
        }
    }

    private ArrayList<String> getPreferredAlbumNames() {
        ArrayList<String> preferredAlbumNames = new ArrayList<String>();
        String deviceImageTopDirectory = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/";
        File deviceImageTopDirectoryFile = new File(deviceImageTopDirectory);
        if(deviceImageTopDirectoryFile.exists()){
            preferredAlbumNames.add(deviceImageTopDirectoryFile.getName());
        }
        File[] deviceImageDirectories = new File(deviceImageTopDirectory).listFiles();
        for (File directory : deviceImageDirectories) { // 添加系统相册
            preferredAlbumNames.add(directory.getName());
        }
        String savedImageFolder = new File(MediaHelper.getImagePath()).getName(); // App拍照后，保存的路径
        preferredAlbumNames.add(savedImageFolder);

        return preferredAlbumNames;
    }

    private void updateImageFile() {
        removeDuplicateWithOrder(MediaCacheManager.imgdrr);
        for (int i = 0; i < MediaCacheManager.imgdrr.size(); i++) {
            String originalPath = MediaCacheManager.imgdrr.get(i);
            if (!imagesInspectItemValues.contains(originalPath)) {
                imagesInspectItemValues.add(originalPath);

            }
            if (ListUtil.isEmpty(imagesInspectItemValues)) {
                return;
            }
            MediaCacheManager.imgbmp.clear();
            for (String str : imagesInspectItemValues) {
                Bitmap bm = ImageDimenCompressUtil.decodeBitmapFromFile(str, TransPixelUtil.dip2px(context, 120), TransPixelUtil.dip2px(context, 160));
                MediaCacheManager.imgbmp.add(bm);
            }
        }

        // 将图片路径信息设置到InspectItem中的Value中
        setImagesValue();
        gridViewImage.setAdapter(mImageAdapter);
        mImageAdapter.notifyDataSetChanged();
    }

    private void refreshImageData() {

        if (null != imagesInspectItemValues) {
            imagesInspectItemValues.clear();
        }
        if (null != MediaCacheManager.imgbmp) {
            MediaCacheManager.imgbmp.clear();
        }

        updateImageFile();
    }

    private void removeDuplicateWithOrder(List<String> list) {
        Set<String> setTemp = new HashSet<String>();
        List<String> listTemp = new ArrayList<String>();
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String temp = iterator.next();
            if (setTemp.add(temp)) {
                listTemp.add(temp);
            }
        }

        list.clear();
        list.addAll(listTemp);
    }

    private void setImagesValue() {
        if (null == imagesInspectItemValues) {
            return;
        }

        StringBuilder sbPaths = new StringBuilder();
        for (String path : imagesInspectItemValues) {
            if (!StringUtil.isBlank(sbPaths.toString())) {
                sbPaths.append(Constants.IMAGE_SPLIT_STRING);
            }
            sbPaths.append(path);
        }

        mInspectItem.setValue(sbPaths.toString());
    }

    private void refreshImageFolders() {
        Runnable runnable = new Runnable() {
            public void run() {
                File file = new File(MediaHelper.getImagePath(), currentPhotoName);
                MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {

                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        LogUtil.v(ImageInspectItemViewXtd.this, "scanned file: " + uri.toString());
                    }
                });
            }
        };

        new Thread(runnable).start();
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.FILE_OPERATION_DOWNLOAD_FINISH:
                LoadingDialogUtil.dismiss();
                String targetPath = event.getData();
                MediaCacheManager.imgdrr.add(targetPath);
                updateImageFile();
                EventBusUtil.unregister(this);
                break;
            default:
                break;
        }
    }
}
