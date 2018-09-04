package com.ecity.medialibrary.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MediaCacheManager {
    public static int maxImg = 0;
    public static int maxVdo = 0;
    public static boolean act_bool = true;
    public static List<Bitmap> imgbmp = new ArrayList<Bitmap>();
    public static List<Bitmap> vdobmp = new ArrayList<Bitmap>();
    public static List<String> imgdrr = new ArrayList<String>();
    public static List<String> vdodrr = new ArrayList<String>();
    public static List<String> attachdrr = new ArrayList<String>();

    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    public static void clearImg() {
        if (null != imgdrr)
        	imgbmp.clear();
        if (null != imgdrr)
        	imgdrr.clear();
        maxImg = 0;
        act_bool = true;
        imgbmp = new ArrayList<Bitmap>();
        imgdrr = new ArrayList<String>();
    }

    public static void clearVdo() {
        if (null != vdodrr)
            vdobmp.clear();
        if (null != vdodrr)
            vdodrr.clear();
        maxVdo = 0;
        act_bool = true;
        vdobmp = new ArrayList<Bitmap>();
        vdodrr = new ArrayList<String>();
    }

    public static void clearAttach() {
        if (null != imgdrr)
            attachdrr.clear();
        act_bool = true;
        attachdrr = new ArrayList<String>();
    }
}
