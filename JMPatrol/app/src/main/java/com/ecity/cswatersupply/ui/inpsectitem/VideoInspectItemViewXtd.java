package com.ecity.cswatersupply.ui.inpsectitem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ImageAdapter;
import com.ecity.cswatersupply.adapter.VideoRecordAdapter;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.widght.GridViewForScrollView;
import com.ecity.medialibrary.activity.FileChooserActivity;
import com.ecity.medialibrary.activity.PlayVideoActivity;
import com.ecity.medialibrary.activity.TakeVideoActivity;
import com.ecity.medialibrary.model.VideoModel;
import com.ecity.medialibrary.utils.FileSuffixUtils;
import com.ecity.medialibrary.utils.MediaCacheManager;
import com.ecity.medialibrary.utils.VideoThumbnailLoadUtil;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.view.ActionSheet;

public class VideoInspectItemViewXtd extends ABaseInspectItemView {
    private ArrayList<VideoModel> mVideomodels = new ArrayList<VideoModel>();
    private VideoRecordAdapter mVideoAdapter;
    private List<String> mVideosSdPaths = new ArrayList<String>();
    private List<String> mVideosZ3Paths = new ArrayList<String>();

    @Override
    protected void setup(View contentView) {
        ViewStub videoStub = (ViewStub) contentView.findViewById(R.id.viewStub_galley);
        videoStub.inflate();
        Gallery videoGallery = (Gallery) contentView.findViewById(R.id.gallery1);
        GridViewForScrollView gridViewVideo = (GridViewForScrollView) contentView.findViewById(R.id.gridviewVideo);
        inflateVideo(gridViewVideo, videoGallery);
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_video_select;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.TAKE_VIDEOS) {
            if (resultCode == Activity.RESULT_OK) {
                updateTakeVideosData(data);
                String currentVideoPath = data.getExtras().getString(TakeVideoActivity.CURRENT_VIDEO_PATH);
                MediaCacheManager.vdodrr.add(currentVideoPath);
                updateVideoFile();
            }
        } else if (requestCode == RequestCode.SELECT_VIDEOS) {
            if (null == data) {
                return;
            }
            updateSelectVideosData(data);
            updateVideoFile();
        }
    }

    private void inflateVideo(GridViewForScrollView gridViewVideo, Gallery gallery) {
        if (mInspectItem.isEdit()) {
            gridViewVideo.setVisibility(View.VISIBLE);
            mVideoAdapter = new VideoRecordAdapter(context, mVideomodels);
            gridViewVideo.setAdapter(mVideoAdapter);
            gridViewVideo.setOnItemClickListener(new MyGridViewItemClickListener(EInspectItemType.VIDEO));
            if (!mInspectItem.getValue().isEmpty()) {
                String[] strs = mInspectItem.getValue().split(",");
                List<String> videosPaths = Arrays.asList(strs);
                MediaCacheManager.vdodrr.addAll(videosPaths);
                updateVideoFile();
            }
            updateData();
        } else {
            gridViewVideo.setVisibility(View.GONE);
            if (!mInspectItem.getValue().isEmpty()) {
                String[] strs = mInspectItem.getValue().split(",");
                final List<String> imagesPaths = new ArrayList<String>(Arrays.asList(strs));
                for (int i = 0; i < imagesPaths.size(); i++) {
                    String path = ServiceUrlManager.getInstance().getImageUrl() + imagesPaths.get(i);
                    imagesPaths.set(i, path);
                }
                ImageAdapter adapter = new ImageAdapter(context, imagesPaths, false);
                gallery.setAdapter(adapter);
                gallery.setOnItemClickListener(new OnItemClickListener() {//hun

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String videoPath = imagesPaths.get(position);
                        Intent intent = new Intent(context, PlayVideoActivity.class);
                        intent.putExtra("INTENT_KEY_VIDEOPATH", videoPath);
                        context.startActivity(intent);
                    }
                });
            }
        }
    }

    private class MyGridViewItemClickListener implements OnItemClickListener {
        private String[] menus;

        public MyGridViewItemClickListener(EInspectItemType type) {
            menus = context.getResources().getStringArray(R.array.custom_action_sheet_video_menus);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            showVideoMenu(position);
        }

        private void showVideoMenu(int position) {
            if (position == mVideomodels.size()) {
                ActionSheet.show(context, context.getResources().getString(R.string.action_menu_title), menus, new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                Intent takeVideoIntent = new Intent(context, TakeVideoActivity.class);
                                startActivityForResult(takeVideoIntent, RequestCode.TAKE_VIDEOS);
                                ActionSheet.dismiss();
                                break;
                            case 1:
                                FileSuffixUtils.mFileFileterBySuffixs.acceptSuffixs("mp4");
                                Intent selectVideoIntent = new Intent(context, FileChooserActivity.class);
                                startActivityForResult(selectVideoIntent, RequestCode.SELECT_VIDEOS);
                                ActionSheet.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
            } else {
                VideoModel temp = (VideoModel) mVideoAdapter.getItem(position);
                String path = temp.getPath();
                Intent intent = new Intent(context, PlayVideoActivity.class);
                intent.putExtra("INTENT_KEY_VIDEOPATH", path);
                context.startActivity(intent);
            }
        }
    }

    private void updateVideoFile() {
        removeDuplicateWithOrder(MediaCacheManager.vdodrr);
        try {
            for (int i = 0; i < MediaCacheManager.vdodrr.size(); i++) {
                String originalPath = MediaCacheManager.vdodrr.get(i);
                if (!mVideosSdPaths.contains(originalPath)) {
                    mVideosSdPaths.add(originalPath);
                    Bitmap bm = MediaCacheManager.revitionImageSize(originalPath);
                    MediaCacheManager.vdobmp.add(bm);
                }
            }

            for (int i = 0; i < mVideosSdPaths.size(); i++) {
                String originalPath = mVideosSdPaths.get(i);
                if (!mVideosZ3Paths.contains(originalPath)) {
                    mVideosZ3Paths.add(originalPath);
                }
            }
        } catch (IOException e) {
            LogUtil.e(context, e);
        }

        setVideosValue();
        mVideoAdapter.notifyDataSetChanged();
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

    private void setVideosValue() {
        StringBuilder sbVideoPaths = new StringBuilder();
        if (null != mVideosZ3Paths && mVideosZ3Paths.size() > 0) {
            for (int i = 0; i < mVideosZ3Paths.size(); i++) {
                if (!StringUtil.isBlank(sbVideoPaths.toString())) {
                    sbVideoPaths.append(",");
                }
                sbVideoPaths.append(mVideosZ3Paths.get(i));
            }
        }

        mInspectItem.setValue(sbVideoPaths.toString());
    }

    private void updateTakeVideosData(Intent intent) {
        Bundle bundle = intent.getExtras();
        String currentVideoPath = bundle.getString(TakeVideoActivity.CURRENT_VIDEO_PATH);
        if (!StringUtil.isBlank(currentVideoPath)) {
            Bitmap videoImage = VideoThumbnailLoadUtil.getInstance().getVideoThumbnail(currentVideoPath);
            MediaCacheManager.vdodrr.add(currentVideoPath);
            MediaCacheManager.vdobmp.add(videoImage);
        }

        updateData();
    }

    private void updateSelectVideosData(Intent intent) {
        Bundle bundle = intent.getExtras();
        List<File> files = (List<File>) bundle.getSerializable(FileChooserActivity.PATHS);
        for (File file : files) {
            String currentVideoPath = file.getPath();
            Bitmap videoImage = VideoThumbnailLoadUtil.getInstance().getVideoThumbnail(currentVideoPath);
            MediaCacheManager.vdodrr.add(currentVideoPath);
            MediaCacheManager.vdobmp.add(videoImage);

        }

        updateData();
    }

    private void updateData() {
        List<String> videoPaths = new ArrayList<String>();
        for (VideoModel model : mVideomodels) {
            if (!videoPaths.contains(model.getPath())) {
                videoPaths.add(model.getPath());
            }
        }

        for (String filePath : MediaCacheManager.vdodrr) {
            if (!videoPaths.contains(filePath)) {
                VideoModel spotFile = new VideoModel(filePath, VideoModel.TYPE_VIDEO);
                mVideomodels.add(spotFile);
            }
        }
        mVideoAdapter.setDataSource(mVideomodels);
    }

}
