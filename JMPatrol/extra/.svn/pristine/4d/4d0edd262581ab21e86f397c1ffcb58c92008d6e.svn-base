package com.ecity.medialibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 照片压缩
 * 在不影响图片质量的情况下将图片采样压缩为指定尺寸120dp*160dp(由于各手机像素密度不同故最后图片实际像素比也有所不同)
 * @author lotus
 *
 */
public class ImageDimenCompressUtil {
    /**
     * 计算采样
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (width > height) {
            int tmp = reqWidth;
            reqWidth = reqHeight;
            reqHeight = tmp;
        }
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 图像压缩后解析
     * @param pathName 图片地址
     * @param reqWidth 指定宽度
     * @param reqHeight 指定高度
     * @return
     */
    public static Bitmap decodeBitmapFromFile(String pathName, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }
}
