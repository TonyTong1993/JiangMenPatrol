package com.zzz.ecity.android.applibrary.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;

import com.zzz.ecity.android.applibrary.MyApplication;

public class DrawableUtil {
    
    public static Drawable createDrawable(Bitmap bgImg, int width, int height){
        Bitmap bmpTmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpTmp);
        Paint paint = new Paint();
        paint.setDither(true);
        paint.setFilterBitmap(true);
        Rect src = new Rect(0, 0, bgImg.getWidth(), bgImg.getHeight());
        Rect dst = new Rect(0, 0, width, height);
        canvas.drawBitmap(bgImg, src, dst, paint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return  new BitmapDrawable(MyApplication.getApplication().getResources(), bmpTmp);
    }
    
    public static Drawable createDrawable(Bitmap bgImg, int width, int height, int color, float textSize, String txt){
        Bitmap bmpTmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpTmp);
        Paint paint = new Paint();
        paint.setDither(true);
        paint.setFilterBitmap(true);
        Rect src = new Rect(0, 0, bgImg.getWidth(), bgImg.getHeight());
        Rect dst = new Rect(0, 0, width, height);
        canvas.drawBitmap(bgImg, src, dst, paint);
        
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setColor(color);
        
        float textLength = paint.measureText(txt);
        int tx = 0;
        int ty = 0;
        tx = (int)(width-textLength)/2;
        ty = height/2;
        int offset = height/16 -2;
        ty = ty - offset;
        canvas.drawText(txt, tx, ty, textPaint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return  new BitmapDrawable(MyApplication.getApplication().getResources(), bmpTmp);
    }
    /**
     * Draw a given text in the bottom-right of src bitmap.
     * 
     * @param srcBitmap a new Bitmap with text on it.
     * @param text
     * @return
     */
    public static BitmapDrawable createDrawableForText(Bitmap bottomImg, int imgSize, int color, float textSize, String text) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(text.trim())) {
            return null;
        }
        int margin = 5;
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setColor(color);
        
        Rect textRect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textRect);
        int textHeight = textRect.height();
        int textWidth = textRect.width();
        
        int srcWidth = textWidth;
        int srcHeight = textHeight;
        if(srcWidth < imgSize){
            srcWidth = imgSize;
        }
        
        srcHeight = srcHeight+imgSize;
        srcWidth = srcWidth+margin*2;
        srcHeight = srcHeight+margin*2;
        
        Bitmap newBmp = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBmp);
        int txtStart = (int)((srcWidth-textWidth)/2.0-0.5);
        canvas.drawText(text,txtStart, srcHeight-imgSize-margin, textPaint);
        if(null != bottomImg){
            Paint paint = new Paint();
            paint.setDither(true);
            paint.setFilterBitmap(true);
            
            float left = (srcWidth-imgSize)/2;
            float top = textHeight+margin*2;
            float right = (srcWidth-imgSize)/2+imgSize;
            float bottom = srcHeight;
            Rect src = new Rect(0, 0, bottomImg.getWidth(), bottomImg.getHeight());
            Rect dst = new Rect((int)left, (int)top, (int)right, (int)bottom);
            canvas.drawBitmap(bottomImg, src, dst, paint);
        }
        
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return new BitmapDrawable(MyApplication.getApplication().getResources(), newBmp);
    }
}
