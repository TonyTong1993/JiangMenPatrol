package com.ecity.cswatersupply.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

public class BitmapUtils {
	public static Bitmap drawTextToBitmap2(Context gContext, int gResId,
			String gText, int color) {
		Resources resources = gContext.getResources();
		float scale = resources.getDisplayMetrics().density;

		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(color);
		paint.setTextSize((int) (15 * scale));
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
}
