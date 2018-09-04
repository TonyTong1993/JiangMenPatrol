package com.wjk.tableview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by zhengzhuanzi on 2017/8/2.
 */
@SuppressLint("DrawAllocation")
public class BorderTextView extends TextView {

    private int borderColor = 0x99000000;
    private int srokeWidth = 1;
    private boolean mFollowTextColor = false;

    public BorderTextView(Context context) {
        super(context);
    }

    public BorderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public int getSrokeWidth() {
        return srokeWidth;
    }

    public void setSrokeWidth(int srokeWidth) {
        this.srokeWidth = srokeWidth;
    }

    public boolean isFollowTextColor() {
        return mFollowTextColor;
    }

    public void setFollowTextColor(boolean mFollowTextColor) {
        this.mFollowTextColor = mFollowTextColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);     // 空心效果
        paint.setAntiAlias(true);               // 设置画笔为无锯齿
        paint.setStrokeWidth(srokeWidth);      // 线宽

        // 设置边框线的颜色, 如果声明为边框跟随文字颜色且当前边框颜色与文字颜色不同时重新设置边框颜色
        if (mFollowTextColor && borderColor != getCurrentTextColor()) {
            borderColor = getCurrentTextColor();
        }
        paint.setColor(borderColor);

        RectF rectF = new RectF();
        // 画空心圆角矩形
        rectF.left = rectF.top = 0.5f * srokeWidth;
        rectF.right = getMeasuredWidth() - srokeWidth;
        rectF.bottom = getMeasuredHeight() - srokeWidth;
        canvas.drawRoundRect(rectF, 0, 0, paint);

        super.onDraw(canvas);
    }
}
