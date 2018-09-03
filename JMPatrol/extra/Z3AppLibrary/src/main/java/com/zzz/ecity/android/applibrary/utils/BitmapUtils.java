package com.zzz.ecity.android.applibrary.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;

import com.ecity.android.log.LogUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BitmapUtils {
    private static final float PHOTO_DEFAULT_MAX_OF_WIDTH_AND_HEIGHT = 1280;
    public static Bitmap reduceImageSize(String path) throws IOException {
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

        int degree = readPictureDegree(path);
        Bitmap rotateBitmap = rotateBitmap(bitmap, degree);

        return rotateBitmap;
    }

    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    public static Bitmap scaleWithWH(Bitmap src, double w, double h) {
        if (w == 0 || h == 0 || src == null) {
            return src;
        } else {
            // 记录src的宽高
            int width = src.getWidth();
            int height = src.getHeight();
            // 创建一个matrix容器
            Matrix matrix = new Matrix();
            // 计算缩放比例
            float scaleWidth = (float) (w / width);
            float scaleHeight = (float) (h / height);
            // 开始缩放
            matrix.postScale(scaleWidth, scaleHeight);
            // 创建缩放后的图片
            return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
        }
    }

    public static Bitmap transImage(String fromFile) throws IOException{
        Bitmap bitmap = reduceImageSize(fromFile);
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        float height = Float.valueOf(bitmapHeight);
        float width = Float.valueOf(bitmapWidth);
        // 缩放图片的尺寸
        //        float scaleWidth = (float) width / bitmapWidth;
        //        float scaleHeight = (float) height / bitmapHeight;
        float max = height > width ? height : width;
        float scale = PHOTO_DEFAULT_MAX_OF_WIDTH_AND_HEIGHT < max ? (PHOTO_DEFAULT_MAX_OF_WIDTH_AND_HEIGHT / max) : 1;
        float scaleWidth = scale * bitmapWidth;
        float scaleHeight = scale * bitmapHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // 产生缩放后的Bitmap对象
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
        return resizeBitmap;
        // save file
        //        if (!bitmap.isRecycled()) {
        //            bitmap.recycle();//记得释放资源，否则会内存溢出
        //        }
        //        if (!resizeBitmap.isRecycled()) {
        //            resizeBitmap.recycle();
        //        }

    }

    public static Bitmap drawTextToBitmap2(Context gContext, int gResId,
                                           String gText, int color) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize((int) (10 * scale));
        paint.setDither(true);
        paint.setFilterBitmap(true);
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);
        bitmap = scaleWithWH(bitmap, bounds.width() + 10, bounds.height() + 5);
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(gText, 2, bounds.height() - 1, paint);
        return bitmap;
    }

    public static Bitmap transImage(String fromFile, int edgeMax) throws IOException {


        /**
         * 进行设置照片最大尺寸，超过进行照片缩放
         */
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeFile(fromFile);
        } catch (OutOfMemoryError err) {
            bitmap = handleOutOfMemoryError(fromFile);
        }

        if (bitmap == null) {
            return null;
        }

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        float height = Float.valueOf(bitmapHeight);
        float width = Float.valueOf(bitmapWidth);
        // 缩放图片的尺寸
        //        float scaleWidth = (float) width / bitmapWidth;
        //        float scaleHeight = (float) height / bitmapHeight;
        float max = height > width ? height : width;
        float scale = edgeMax < max ? (edgeMax / max) : 1;
        float scaleWidth = scale * bitmapWidth;
        float scaleHeight = scale * bitmapHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // 产生缩放后的Bitmap对象
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);

//        if (!bitmap.isRecycled()) {
//            bitmap.recycle();//记得释放资源，否则会内存溢出
//        }
        return resizeBitmap;
        // save file
//                if (!bitmap.isRecycled()) {
//                    bitmap.recycle();//记得释放资源，否则会内存溢出
//                }
//                if (!resizeBitmap.isRecycled()) {
//                    resizeBitmap.recycle();
//                }

    }

    public static Bitmap handleOutOfMemoryError(String fromFile){
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fromFile, opts);
        opts.inSampleSize = computeSampleSize(opts, -1, 1000 * 1000);
        opts.inJustDecodeBounds = false;
        try {

            bitmap = BitmapFactory.decodeFile(fromFile, opts);
        } catch (OutOfMemoryError err) {

            LogUtil.e("BitmapUtils", err.getMessage().toString());
        }
        return bitmap;
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

}
