package com.ecity.medialibrary.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import com.z3app.android.util.FileUtil;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class VideoThumbnailLoadUtil {
    private Bitmap bitmap = null;


    public interface IProgressBarUpdateListener {
        void onProgressBegin(int current, int total);

        void onProgressUpdate(int current, int total, ProgressBar progressBar);

        void onProgressEnd(int current, int total, String filePath, ImageView imageView, ProgressBar progressBars);
    }

    private VideoThumbnailLoadUtil() {
    }

    private static volatile VideoThumbnailLoadUtil instance;
    private IProgressBarUpdateListener progressBarListener;
    private ImageView imageView;
    private ProgressBar progressBar;

    public static VideoThumbnailLoadUtil getInstance() {
        if (null == instance) {
            synchronized (VideoThumbnailLoadUtil.class) {
                if (null == instance) {
                    instance = new VideoThumbnailLoadUtil();
                }
            }
        }

        return instance;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }

        return bitmap;
    }

    public void startGetVideoThumbnail(final String srcFilePath) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                bitmap = createVideoThumbnail(srcFilePath, 240, 320);
            }
        }).start();
    }

    public String displayVideoThumbnail(final String srcFilePath, ImageView imageView, ProgressBar progressBar,
            IProgressBarUpdateListener progressBarListener) {

        this.progressBarListener = progressBarListener;
        this.imageView = imageView;
        this.progressBar = progressBar;
        CompressFormat format = Bitmap.CompressFormat.JPEG;
        int startIndex = srcFilePath.lastIndexOf("/");
        int endIndex = srcFilePath.lastIndexOf(".");
        String fullPath = FileUtil.getInstance(null).getMediaPath() + "/" + srcFilePath.substring(startIndex + 1, endIndex) + ".jpg";
        OutputStream os = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            os = new FileOutputStream(fullPath);
            bitmap.compress(format, 100, bos);
            InputStream is = new ByteArrayInputStream(bos.toByteArray());
            byte[] buffer = new byte[1024];
            int len = 0;
            int current = 0;
            int total = is.available();

            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
                current += len;
                if (null != progressBarListener && null != progressBar) {
                    this.progressBarListener.onProgressUpdate(current, total, this.progressBar);
                }
            }
            if (null != progressBarListener && null != progressBar) {
                this.progressBarListener.onProgressEnd(current, total, fullPath, this.imageView, this.progressBar);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fullPath;
    }

    public Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(filePath);//, new HashMap<String, String>()
        bitmap = retriever.getFrameAtTime();
        retriever.release();

        return bitmap;
    }

    public String getVideoThumbnailPath(String srcFilePath) {
        Bitmap bitmap = getVideoThumbnail(srcFilePath);
        CompressFormat format = Bitmap.CompressFormat.JPEG;
        int startIndex = srcFilePath.lastIndexOf("/");
        int endIndex = srcFilePath.lastIndexOf(".");
        String fullPath = FileUtil.getInstance(null).getMediaPath() + "/" + srcFilePath.substring(startIndex + 1, endIndex) + ".jpg";
        OutputStream os = null;
        try {
            os = new FileOutputStream(fullPath);
            bitmap.compress(format, 100, os);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return fullPath;
    }
}
