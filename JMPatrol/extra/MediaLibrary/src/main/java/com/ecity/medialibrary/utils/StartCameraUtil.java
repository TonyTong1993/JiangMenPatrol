package com.ecity.medialibrary.utils;

import java.io.File;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.Toast;

public class StartCameraUtil {
    public static final int CODE_TAKE_PHOTO = 100;

    /**
     * 启动手机自带相机应用工具
     * @param activity
     * @param code 自定义onActivity处理编号
     * @param dirName 图片存储的文件夹路径，不包括文件名
     *  @param fileName 照片的文件名
     */
    public static void launchCamera(Activity activity, int code, String dirName, String fileName) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(activity, "没有储存卡", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            File dir = new File(dirName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            Uri uri = Uri.fromFile(file);
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(intent, code);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "没有找到储存目录", Toast.LENGTH_LONG).show();
        }
    }

    public static void launchCameraFromFragment(Fragment fragment, int code, String dirName, String fileName) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(fragment.getActivity(), "没有储存卡", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            File dir = new File(dirName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            Uri uri = Uri.fromFile(file);
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            fragment.startActivityForResult(intent, code);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(fragment.getActivity(), "没有找到储存目录", Toast.LENGTH_LONG).show();
        }
    }
}
