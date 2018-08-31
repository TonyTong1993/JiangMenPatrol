package com.zzz.ecity.android.applibrary.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleTextImageView extends ImageView {
	private static final float DEFAULT_TEXT_SIZE_RATIO = 0.3f;
	private int mBGColor = Color.TRANSPARENT;// Default background color
	private int mBoardColor = Color.RED;// Default board color
	private int mCircleTextColor = Color.BLACK;// text color
	private Paint circlePaint;
	private Paint circleBoardPaint;
	private float boardWidth = 6; //Default boardWidth 6dp
	private boolean showBoard = false;
	private boolean useRandomBackgroundColor = false;// use random background color
	private boolean mSubFirstCharacter = false;

	private String text;
	private Paint textPaint;
	private float textSizeRatio = DEFAULT_TEXT_SIZE_RATIO;
	private Paint.FontMetrics fontMetrics;
	private int radius;
	private int centerX;
	private int centerY;

	public CircleTextImageView(Context context) {
		super(context);
		init();
	}

	public CircleTextImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttr(context, attrs);
		init();
	}

	public CircleTextImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initAttr(context, attrs);
		init();
	}

	private void initAttr(Context context, AttributeSet attrs) {
		if (attrs == null) {
			return;
		}
	}

	private void init() {
		circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlePaint.setStyle(Style.FILL);

		circleBoardPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circleBoardPaint.setStyle(Style.STROKE);
		circleBoardPaint.setStrokeWidth(boardWidth);

		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(mCircleTextColor);
		textPaint.setTextAlign(Paint.Align.CENTER);

		if (useRandomBackgroundColor) {
			circlePaint.setColor(Color.parseColor(CircleTextImageUtil.getRandomColor()));
		} else {
			circlePaint.setColor(mBGColor);
		}

		if (showBoard) {
			circleBoardPaint.setColor(mBoardColor);
		} else {
			circleBoardPaint.setColor(Color.TRANSPARENT);
		}
	}

	public void setBGColor(int color) {
		this.mBGColor = color;
		circlePaint.setColor(mBGColor);
		invalidate();
	}

	public void setCircleTextColor(int mCircleTextColor) {
		this.mCircleTextColor = mCircleTextColor;
		textPaint.setColor(mCircleTextColor);
		invalidate();
	}

	public void setBoardWidth(float boardWidth) {
		this.boardWidth = boardWidth;
		circleBoardPaint.setStrokeWidth(boardWidth);
		invalidate();
	}
	
	public void setBoardColor(int color) {
		this.mBoardColor = color;
		circleBoardPaint.setColor(mBoardColor);
		invalidate();
	}
	
	public void setShowBoard(boolean showBoard) {
		this.showBoard = showBoard;
		invalidate();
	}

	public boolean isSubFirstCharacter() {
		return mSubFirstCharacter;
	}

	public void setSubFirstCharacter(boolean mSubFirstCharacter) {
		this.mSubFirstCharacter = mSubFirstCharacter;
		if (mSubFirstCharacter) {
			this.text = CircleTextImageUtil.subFirstCharacter(text);
		}
		invalidate();
	}

	public boolean isUseRandomBackgroundColor() {
		return useRandomBackgroundColor;
	}

	public void setUseRandomBackgroundColor(boolean useRandomBackgroundColor) {
		this.useRandomBackgroundColor = useRandomBackgroundColor;
		if (useRandomBackgroundColor) {
			circlePaint.setColor(Color.parseColor(CircleTextImageUtil.getRandomColor()));
		} else {
			circlePaint.setColor(mBGColor);
		}
		invalidate();
	}

	public void setText4CircleImage(String text) {
		if (mSubFirstCharacter) {
			this.text = CircleTextImageUtil.subFirstCharacter(text);
		} else {
			this.text = text;
		}
		invalidate();
	}
	
	private void drawText(Canvas canvas) {
		canvas.drawCircle(centerX, centerY, getRadius(), circlePaint);
		canvas.drawText(text, 0, text.length(), centerX, centerY + Math.abs(fontMetrics.top + fontMetrics.bottom)/2, textPaint);
	}
	
	private float getRadius(){
		if(radius < boardWidth){
			return 0;
		}

		return radius - boardWidth;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int desiredWidth = 100 * 2;
		int desiredHeight = 100 * 2;

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width;
		int height;

		if (null != text && !text.trim().equals("")) {
			int realSize = (int) textPaint.measureText(text) + 60;
			if (realSize < 200) {
				realSize = 100 * 2;
			}
			// Measure Width
			if (widthMode == MeasureSpec.EXACTLY) {
				// Must be this size
				width = widthSize;
			} else if (widthMode == MeasureSpec.AT_MOST) {
				// Can't be bigger than...
				width = realSize;
			} else {
				// Be whatever you want
				width = realSize;
			}

			// Measure Height
			if (heightMode == MeasureSpec.EXACTLY) {
				// Must be this size
				height = heightSize;
			} else if (heightMode == MeasureSpec.AT_MOST) {
				// Can't be bigger than...
				height = realSize;
			} else {
				// Be whatever you want
				height = realSize;
			}
		} else {
			// Measure Width
			if (widthMode == MeasureSpec.EXACTLY) {
				// Must be this size
				width = widthSize;
			} else if (widthMode == MeasureSpec.AT_MOST) {
				// Can't be bigger than...
				width = Math.min(desiredWidth, widthSize);
			} else {
				// Be whatever you want
				width = desiredWidth;
			}

			// Measure Height
			if (heightMode == MeasureSpec.EXACTLY) {
				// Must be this size
				height = heightSize;
			} else if (heightMode == MeasureSpec.AT_MOST) {
				// Can't be bigger than...
				height = Math.min(desiredHeight, heightSize);
			} else {
				// Be whatever you want
				height = desiredHeight;
			}

		}
		// MUST CALL THIS
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// get padding
		final int paddingLeft = getPaddingLeft();
		final int paddingRight = getPaddingRight();
		final int paddingTop = getPaddingTop();
		final int paddingBottom = getPaddingBottom();
		// deal padding
		int width = getWidth() - paddingLeft - paddingRight;
		int height = getHeight() - paddingTop - paddingBottom;
		radius = Math.min(width, height) / 2;
		if(showBoard){
			canvas.drawCircle(width / 2, height / 2, getRadius(), circleBoardPaint);
		}
		
		if (null != text && !text.trim().equals("")) {
			drawText(canvas);
		} else {
			canvas.drawCircle(width / 2, height / 2, getRadius(), circlePaint);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		int contentWidth = w - paddingLeft - getPaddingRight();
		int contentHeight = h - paddingTop - getPaddingBottom();
		radius = contentWidth < contentHeight ? contentWidth / 2 : contentHeight / 2;
		centerX = paddingLeft + radius;
		centerY = paddingTop + radius;
		refreshTextSizeConfig();
	}

	private void refreshTextSizeConfig() {
		textPaint.setTextSize(textSizeRatio * 2 * 100);
		fontMetrics = textPaint.getFontMetrics();
	}
	
	public void setTextSize(float sizeRatio) {
		this.textSizeRatio = sizeRatio;
		textPaint.setTextSize(textSizeRatio * 2 * 100);
		fontMetrics = textPaint.getFontMetrics();
		invalidate();
	}

	static class CircleTextImageUtil {
		/**
		 * Get the random color.
		 * 
		 * @return
		 */
		private static String getRandomColor() {
			List<String> colorList = new ArrayList<String>();
			colorList.add("#303F9F");
			colorList.add("#FF4081");
			colorList.add("#59dbe0");
			colorList.add("#f57f68");
			colorList.add("#87d288");
			colorList.add("#f8b552");
			colorList.add("#990099");
			colorList.add("#90a4ae");
			colorList.add("#7baaf7");
			colorList.add("#4dd0e1");
			colorList.add("#4db6ac");
			colorList.add("#aed581");
			colorList.add("#fdd835");
			colorList.add("#f2a600");
			colorList.add("#ff8a65");
			colorList.add("#f48fb1");
			colorList.add("#7986cb");
			colorList.add("#FFFFE0");
			colorList.add("#ADD8E6");
			colorList.add("#DEB887");
			colorList.add("#C0C0C0");
			colorList.add("#AFEEEE");
			colorList.add("#F0FFF0");
			colorList.add("#FF69B4");
			colorList.add("#FFE4B5");
			colorList.add("#FFE4E1");
			colorList.add("#FFEBCD");
			colorList.add("#FFEFD5");
			colorList.add("#FFF0F5");
			colorList.add("#FFF5EE");
			colorList.add("#FFF8DC");
			colorList.add("#FFFACD");

			return colorList.get((int) (Math.random() * colorList.size()));
		}

		/**
		 * Interception of the first string of characters.
		 * 
		 * @param str
		 * @return
		 */
		private static String subFirstCharacter(String str) {
			if (Character.isLetter(str.charAt(0))) {
				return Character.toUpperCase(str.charAt(0)) + "";
			} else {
				return str.charAt(0) + "";
			}
		}
	}

}
