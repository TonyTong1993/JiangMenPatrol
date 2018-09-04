package com.ecity.medialibrary.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.ecity.medialibrary.activity.FileChooserActivity;
import com.ecity.medialibrary.activity.TakeVideoActivity;
import com.ecity.medialibrary.model.VideoModel;
import com.z3app.android.util.StringUtil;

public class VideoUpdateUtil {
    public static void updateVideoFile(List<String> mVideosSdPaths) {
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
        } catch (IOException e) {
            Log.e("VideoUpdateUtil", e.toString());
        }
    }

    public static void removeDuplicateWithOrder(List<String> list) {
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

    public static void updateTakeVideosData(Intent intent, List<VideoModel> mVideomodels) {
        Bundle bundle = intent.getExtras();
        String currentVideoPath = bundle.getString(TakeVideoActivity.CURRENT_VIDEO_PATH);
        if (!StringUtil.isBlank(currentVideoPath)) {
            Bitmap videoImage = VideoThumbnailLoadUtil.getInstance().getVideoThumbnail(currentVideoPath);
            MediaCacheManager.vdodrr.add(currentVideoPath);
            MediaCacheManager.vdobmp.add(videoImage);
        }

        updateData(mVideomodels);
    }

    public static void updateSelectVideosData(Intent intent, List<VideoModel> mVideomodels) {
        Bundle bundle = intent.getExtras();
        List<File> files = (List<File>) bundle.getSerializable(FileChooserActivity.PATHS);
        for (File file : files) {
            String currentVideoPath = file.getPath();
            Bitmap videoImage = VideoThumbnailLoadUtil.getInstance().getVideoThumbnail(currentVideoPath);
            MediaCacheManager.vdodrr.add(currentVideoPath);
            MediaCacheManager.vdobmp.add(videoImage);

        }

        updateData(mVideomodels);
    }

    public static void updateData(List<VideoModel> mVideomodels) {
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
    }

    public static void updateDeleteData(List<VideoModel> mVideomodels) {
        mVideomodels.clear();

        for (String filePath : MediaCacheManager.vdodrr) {
            VideoModel spotFile = new VideoModel(filePath, VideoModel.TYPE_VIDEO);
            mVideomodels.add(spotFile);
        }
    }
}
