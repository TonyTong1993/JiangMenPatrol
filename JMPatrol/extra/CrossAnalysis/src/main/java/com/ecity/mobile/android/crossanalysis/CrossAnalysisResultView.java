package com.ecity.mobile.android.crossanalysis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CrossAnalysisResultView extends View {
	private Paint outLinePaint;
	private Paint outPointPaint;
	private Paint pipeFramePaint;
	private Paint pipePaint;
	@SuppressWarnings("rawtypes")
	private ArrayList<ArrayList> XmlAnalysis_result;
	/** the width of my screen. */
	public float CSAWidth;
	/** the width of the view. */
	public float viewWidth;
	/** the height of my screen. */
	public float CSAHeight;
	/** the density of my screen. */
	private float density;
	/**
	 * The view should be fill of my screen, so we must have a scale to finish
	 * it.
	 */
	public float scale;
	/** x coordinate of center point in my screen. */
	private float centerx;
	/** y coordinate of center point in my screen. */
	private float centery;
	/** two size of outpoint text. */
	private float textSize1;
	/** two size of outpoint text. */
	private float textSize2;
	/** When the zoomin button was pressed, there exists a zoomin scale. */
	private float zoomInScale;
	/** When the zoomout button was pressed, there exists a zoomin scale. */
	private float zoomOutScale;
	/** default stoking width of pipe frames paint to draw. */
	private float paintWidth_pipeFrame = 5;
	/** default stoking width of pipes paint to draw. */
	private float paintWidth_pipe = 3;
	/** default stoking width of outline paint to draw. */
	private float paintWidth_outLine = 3;
	/** default stoking width of outpoint paint to draw. */
	private float paintWidth_outPoint = 3;
	/**
	 * the cache of all the data to draw the view. Only the reset button was
	 * pressed, they will be used.
	 */
	private float originalScale;
	private float originalCenterx;
	private float originalCentery;
	private float originalOutline_x;
	private float originalOutline_y;
	private float originalPipe_x;
	private float originalPipe_y;
	/**
	 * To make the outline & pipe in the center, they should be translated a
	 * offset.
	 */
	private float xoffset_outline;
	private float yoffset_outline;
	public float xoffset_pipe;
	public float yoffset_pipe;
	/** Save the max & min of the pipe or pipeframe. */
	private float x[] = new float[2];
	private float y[] = new float[2];
	/** range of outline. */
//	public static float range = 450;
	private int backGroundWidth = 400;
	private int backGroundHeight = 400;
	/** title of the activity. */
	public static float title = 45;
	/** State 1 : No action happened. */
	public static int NONE = 1;
	/** State 1 : There exsits a drag action. */
	public static int DRAG = 2;
	/** State 1 : There exsits a zoom action. */
	public static int ZOOM = 3;
	public int actionEvent = NONE;
	/** some data to do a analysis of action event. */
	private int highLight = 0;
	private float oldX = 0, oldY = 0, newX = 0, newY = 0;
	private float oldDistance = 0, newDistance;
	private float translateX, translateY;
	private long upTime = 0, downTime = 0;
	private float zoomScale;
	Context context;
	private Bitmap CSAbitmap;

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文信息.
	 * @param attrs
	 *            A collection of attributes.
	 */
	@SuppressLint("ClickableViewAccessibility")
	public CrossAnalysisResultView(Context context, AttributeSet attrs) {
		super(context, attrs);		
		if (isInEditMode()) {
			return;
		}
		this.context = context;
		ViewParameterInit();
		PaintInit();
		setDrawingCacheEnabled(true);
		this.setClickable(true);
		this.setLongClickable(true);
		this.setOnTouchListener(new OnTouchListener() {
			boolean zoom = false;

			@SuppressLint("ClickableViewAccessibility")
			@Override			
			public boolean onTouch(View v, MotionEvent event) {			
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:				
					oldX = event.getX();
					oldY = event.getY();
					downTime = event.getEventTime();
					actionEvent = DRAG;
					break;
				case MotionEvent.ACTION_POINTER_DOWN:			
					zoom = true;
					oldDistance = (float) Math.sqrt((event.getX(0) - event
							.getX(1))
							* (event.getX(0) - event.getX(1))
							+ (event.getY(0) - event.getY(1))
							* (event.getY(0) - event.getY(1)));
					actionEvent = ZOOM;
					break;
				case MotionEvent.ACTION_MOVE:
					if (actionEvent == DRAG) {
						newX = event.getX();
						newY = event.getY();
						translateX = newX - oldX;
						translateY = newY - oldY;
						Translate(translateX, translateY);
						oldX = newX;
						oldY = newY;
					} else if (actionEvent == ZOOM) {
						newDistance = (float) Math.sqrt((event.getX(0) - event
								.getX(1))
								* (event.getX(0) - event.getX(1))
								+ (event.getY(0) - event.getY(1))
								* (event.getY(0) - event.getY(1)));
						zoomScale = newDistance / oldDistance;
						zoomIn(zoomScale);
						oldDistance = newDistance;
					}
					break;
				case MotionEvent.ACTION_UP:
					upTime = event.getEventTime();
					// 触屏时间太短且没有位移判定为单击
					if (upTime - downTime < 200 && (Math.abs(translateX) <= 5 && Math	.abs(translateY) <= 5)&& false == zoom)
					{
						ClickQueryPopup(oldX, oldY);
					}
					actionEvent = NONE;
					zoom = false;
					break;
				case MotionEvent.ACTION_POINTER_UP:
					actionEvent = NONE;
					break;
				}
				return true;
			}
		});
	}

	/**
	 * Draw the background, outline, outpoint, pipeframe, pipe.
	 * */
	protected void onDraw(Canvas canvas) {
		// 造成错误的代码段
		// 初始化外框点字体大小
		if (density == 1.5) {
			textSize1 = scale * 10;
			textSize2 = scale * 8;
		} else if (density == 3) {
			textSize1 = scale * 8;       
			textSize2 = scale * 6;
		}
		canvas.drawColor(Color.rgb(255, 250, 240));
		//没有管架的时候背景贴图
		if(0 == XmlAnalysis_result.get(2).size()){
			DrawBackGround(canvas);
		}		
		DrawOutLine(canvas);
		DrawPipeFrame(canvas);
		DrawPipeCrosssection(canvas);
		DrawOutPoint(canvas);		
		super.onDraw(canvas);
	}

	@SuppressWarnings("deprecation")
	private void DrawBackGround(Canvas canvas) {
		// TODO Auto-generated method stub
		Paint paint = new Paint();
	    // 声明变量
    	paint.setStyle(Style.FILL);
    	Resources res=getResources(); 
    	InputStream is=null;
    	BitmapDrawable bmpDraw=null;
    	Bitmap bmp = null;
    	Rect dst = null;
    	//第一部分是路面以上
    	is = res.openRawResource(R.drawable.bmp_load_up);
    	bmpDraw = new BitmapDrawable(is);
    	bmp = bmpDraw.getBitmap();
    	Rect src = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
    	dst = new Rect((int)(scale * 0 + xoffset_outline), (int)(scale
				* 0 + yoffset_outline),(int)( scale * backGroundWidth
				+ xoffset_outline),(int)( scale * backGroundHeight/3
				+ yoffset_outline));
    	canvas.drawBitmap(bmp,src,dst,paint);
    	//第二部分是路面下延伸部分
    	is=res.openRawResource(R.drawable.bmp_load_down);
    	bmpDraw=new BitmapDrawable(is);
    	bmp=bmpDraw.getBitmap();
    	src = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
    	dst = new Rect((int)(scale * 0 + xoffset_outline),(int)( scale * backGroundHeight/3	+ yoffset_outline), 
    			(int)( scale * backGroundWidth	+ xoffset_outline),(int)( scale * backGroundHeight/3*2+ yoffset_outline));
    	canvas.drawBitmap(bmp,src,dst,paint);
    	dst = new Rect((int)(scale * 0 + xoffset_outline),(int)( scale * backGroundHeight/3*2	+ yoffset_outline), 
    			(int)( scale * backGroundWidth	+ xoffset_outline),(int)( scale * backGroundHeight+ yoffset_outline));
    	canvas.drawBitmap(bmp,src,dst,paint);
	}

	
	private void OutLinePaintInit() {
		outLinePaint = new Paint();
		@SuppressWarnings("unchecked")
		ArrayList<ArrayList<String>> OutLineInfo = this.XmlAnalysis_result
				.get(0);
		for (int i = 0; i < OutLineInfo.size(); i++) {
			ArrayList<String> LinInfo = OutLineInfo.get(i);
			// 区分直线跟圆，size==5时，是圆，笔刷用空心
			if (LinInfo.size() == 5) {
				// 设置空心圆
				outLinePaint.setStyle(Style.STROKE);
			} else
				;
		}
		// 设置线条宽度
		outLinePaint.setStrokeWidth(paintWidth_outLine);
		// 消除锯齿
		outLinePaint.setAntiAlias(true);
	}

	private void OutPointPaintInit() {
		outPointPaint = new Paint();
		if (density == 1.5) {
			textSize1 = scale * 10;
			textSize2 = scale * 8;
		} else if (density == 3) {
			textSize1 = scale * 8;
			textSize2 = scale * 6;
		}else
		{
			textSize1 = scale * 10;
			textSize2 = scale * 11;
		}
		outPointPaint.setTextSize(textSize1);
		outPointPaint.setStrokeWidth(paintWidth_outPoint);
		// 设置填充
		outPointPaint.setStyle(Style.FILL);
		// 消除锯齿
		outPointPaint.setAntiAlias(true);
	}

	private void pipeFramePaintInit() {
		pipeFramePaint = new Paint();
		// 设置线条宽度
		pipeFramePaint.setStrokeWidth(paintWidth_pipeFrame);
		// 消除锯齿,会影响绘图速度
		pipeFramePaint.setAntiAlias(true);
		// 设置阴影,alpha越小越透明
		pipeFramePaint.setShadowLayer((float) Math.PI, 5, 5,
				Color.argb(200, 0, 0, 0));
		// 图像抖动处理，使图像更平滑饱满
		pipeFramePaint.setDither(true);
	}

	private void pipePaintInit() {
		pipePaint = new Paint();
		// 设置线条宽度
		pipePaint.setStrokeWidth(paintWidth_pipe);
		// 消除锯齿
		pipePaint.setAntiAlias(true);
		// 设置空心
		pipePaint.setStyle(Style.STROKE);
		// 设置阴影,alpha越小越透明
		pipePaint.setShadowLayer((float) Math.PI, 5, 5,
				Color.argb(200, 0, 0, 0));
		// 图像抖动处理，使图像更平滑饱满
		pipePaint.setDither(true);
	}

	// 绘制外框线
	@SuppressWarnings("unchecked")
	protected void DrawOutLine(Canvas canvas) {
		if (XmlAnalysis_result.get(0).size() != 0) {
			ArrayList<ArrayList<String>> OutLineInfo = this.XmlAnalysis_result
					.get(0);
			for (int i = 0; i < OutLineInfo.size(); i++) {
				ArrayList<String> LinInfo = OutLineInfo.get(i);
				if (LinInfo.size() == 5) {
					String strColor = LinInfo.get(1);
					int intcolor = stringToRGBint(strColor);
					outLinePaint.setColor(intcolor);
					float circleX = Float.parseFloat(LinInfo.get(2));
					float circleY = backGroundHeight - Float.parseFloat(LinInfo.get(3));
					float rad = Float.parseFloat(LinInfo.get(4));
					canvas.drawCircle(scale * circleX + xoffset_outline, scale
							* circleY + yoffset_outline, scale * rad,
							outLinePaint);
				} else {
					String strColor = LinInfo.get(1);
					int intcolor = stringToRGBint(strColor);
					outLinePaint.setColor(intcolor);
					String lxy = LinInfo.get(2);
					float line[] = stringToFloat(lxy);
					float line1[] = TranslateLine2(line);
					// 绘制多段线
					int j = 0;
					float stx = line1[j];
					float sty = line1[j + 1];
					for (j = 0; j + 2 < line1.length; j += 2) {
						float endx = line1[j + 2];
						float endy = line1[j + 3];
						canvas.drawLine(scale * stx + xoffset_outline, scale
								* sty + yoffset_outline, scale * endx
								+ xoffset_outline, scale * endy
								+ yoffset_outline, outLinePaint);
						stx = endx;
						sty = endy;
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void DrawOutPoint(Canvas canvas) {// 绘制外框点
		if (XmlAnalysis_result.get(1).size() != 0) {
			ArrayList<ArrayList<String>> OutPointInfo = this.XmlAnalysis_result
					.get(1);
			outPointPaint.setTextSize(textSize1);
			for (int i = 0; i < OutPointInfo.size(); i++) {
				ArrayList<String> AnnInfo = OutPointInfo.get(i);
				String text = AnnInfo.get(3);
				if (i == 0) {
					try {
						double temp = Double.valueOf(text);
						DecimalFormat fnum = new DecimalFormat("##0.000");
						text = fnum.format(temp);
						String strColor = AnnInfo.get(8);
						int intcolor = stringToRGBint(strColor);
						outPointPaint.setColor(intcolor);
					} catch (Exception e) {
						
					}
				}
				/*
				 * float textx = Float.parseFloat(AnnInfo.get(1)); float texty =
				 * range - Float.parseFloat(AnnInfo.get(2));
				 * canvas.drawText(text, scale*textx+xoffset_outline,
				 * scale*texty+yoffset_outline, outPointPaint);
				 * outPointPaint.setTextSize(textSize2);
				 */
		
				float textx = Float.parseFloat(AnnInfo.get(1));
				float texty = Float.parseFloat(AnnInfo.get(2));
				float oxy[] = new float[2];
				oxy[0] = textx;
				oxy[1] = texty;
				float xy[] = TranslateLine2(oxy);
				canvas.drawText(text, scale * xy[0] + xoffset_pipe, scale
						* xy[1] + yoffset_pipe, outPointPaint);
				outPointPaint.setTextSize(textSize2);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void DrawPipeFrame(Canvas canvas) {// 绘制管架
		if (XmlAnalysis_result.get(2).size() != 0) {
			ArrayList<ArrayList<String>> PipeFrameInfo = this.XmlAnalysis_result
					.get(2);
			for (int i = 0; i < PipeFrameInfo.size(); i++) {
				ArrayList<String> PipeLinInfo = PipeFrameInfo.get(i);
				String strColor = PipeLinInfo.get(1);
				int intcolor = stringToRGBint(strColor);
				pipeFramePaint.setColor(intcolor);
				String lxy = PipeLinInfo.get(2);
				float line[] = stringToFloat(lxy);
				float line1[] = TranslateLine2(line);
				// 绘制多段线
				int j = 0;
				float stx = line1[j];
				float sty = line1[j + 1];
				for (j = 0; j + 2 < line1.length; j += 2) {
					float endx = line1[j + 2];
					float endy = line1[j + 3];
					canvas.drawLine(scale * stx + xoffset_pipe, scale * sty
							+ yoffset_pipe, scale * endx + xoffset_pipe, scale
							* endy + yoffset_pipe, pipeFramePaint);
					stx = endx;
					sty = endy;
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void DrawPipeCrosssection(Canvas canvas) {// 绘制断面分析
		if (XmlAnalysis_result.get(3).size() != 0) {
			// 管线未选择状态
			if (highLight == 0) {
				ArrayList<Lininfo> PipeCrosssection = this.XmlAnalysis_result
						.get(3);
				for (int i = 0; i < PipeCrosssection.size(); i++) {
					Lininfo CrosssectionLininfo = PipeCrosssection.get(i);
					String strColor = CrosssectionLininfo.getColor();
					int intcolor = stringToRGBint(strColor);
					pipePaint.setColor(intcolor);
					float sectionX = Float.parseFloat(CrosssectionLininfo
							.getCenterX());
					float sectionY = backGroundHeight
							- Float.parseFloat(CrosssectionLininfo.getCenterY());
					float rad = Float.parseFloat(CrosssectionLininfo.getRad());
					// 有管架时，xoffset_pipe是管架偏移，无管架时是管子偏移
					canvas.drawCircle(scale * sectionX + xoffset_pipe, scale
							* sectionY + yoffset_pipe, scale * rad, pipePaint);
				}
			}
			// 管线在选择状态
			else {
				ArrayList<Lininfo> PipeCrosssection = this.XmlAnalysis_result
						.get(3);
				for (int i = 0; i < PipeCrosssection.size(); i++) {
					Lininfo CrosssectionLininfo = PipeCrosssection.get(i);
					// 如果是选中的状态，让管子进入高亮状态
					if (highLight == Integer.parseInt(CrosssectionLininfo
							.getGid())) {
						pipePaint.setColor(Color.rgb(0, 255, 255));
						pipePaint.setStrokeWidth(paintWidth_pipe * 2);
					} else {
						String strColor = CrosssectionLininfo.getColor();
						int intcolor = stringToRGBint(strColor);
						pipePaint.setColor(intcolor);
						pipePaint.setStrokeWidth(paintWidth_pipe);
					}
					float sectionX = Float.parseFloat(CrosssectionLininfo
							.getCenterX());
					float sectionY = backGroundHeight
							- Float.parseFloat(CrosssectionLininfo.getCenterY());
					float rad = Float.parseFloat(CrosssectionLininfo.getRad());
					// 有管架时，xoffset_pipe是管架偏移，无管架时是管子偏移
					canvas.drawCircle(scale * sectionX + xoffset_pipe, scale
							* sectionY + yoffset_pipe, scale * rad, pipePaint);
				}
			}
		}
	}

	/**
	 * 绘图笔刷初始化
	 */
	private void PaintInit() {
		OutLinePaintInit();
		OutPointPaintInit();
		pipeFramePaintInit();
		pipePaintInit();
	}

	/**
	 * 绘图参数初始化
	 */
	private void ViewParameterInit() {
		// 接收xml分析数据
		XmlAnalysis_result = XmlAnalysisData.xmlAnalysis_result;
		density = CrossAnalysisResultActivity.density;
		// 计算屏幕宽高
		CSAWidth = CrossAnalysisResultActivity.screenWidth;
		viewWidth = CSAWidth*4/5 ;
		CSAHeight = CrossAnalysisResultActivity.screenHeight - title * density
					- 0.5f;// title = 45dip

		setDrawingCacheEnabled(true); // 这函数是要打开图形缓存，这样才能getDrawingCache
		new Canvas();

		CSAbitmap = Bitmap.createBitmap((int) CSAWidth, (int) CSAHeight,
				Bitmap.Config.ARGB_8888);
		new Canvas(CSAbitmap);

		// 初始化放大缩小比例因子
		zoomInScale = (float) (density * 0.4);
		zoomOutScale = (float) (density * 0.4);
		// 设置外框线，外框点，管架，管子的线宽
		if (density == 1.5) {
			paintWidth_outLine = 2;
			paintWidth_outPoint = 2;
			paintWidth_pipeFrame = 3;
			paintWidth_pipe = 2;
		} else if (density == 3) {
			paintWidth_outLine = 3;
			paintWidth_outPoint = 3;
			paintWidth_pipeFrame = 5;
			paintWidth_pipe = 3;
		}
		/**
		 * 计算scale，使得绘的断面图始终填充屏幕宽或屏幕高
		 * */
		if (/*CSAWidth < CSAHeight  */CSAHeight/backGroundHeight > viewWidth/backGroundWidth) //宽度先填充满
			scale = viewWidth / backGroundWidth;
		else  //高度先满
			scale = CSAHeight / backGroundHeight;
		// 屏幕中心线x，y坐标初始化
		centerx = CSAWidth / 2;
		centery = CSAHeight / 2 - 20 * density;
		// 外框线x，y偏移量初始化
		yoffset_outline = centery - scale * backGroundHeight / 2;
		xoffset_outline = centerx - scale * backGroundWidth / 2;
		// 如果有管架，把管架跟管子偏移到屏幕中央，否则管子随着外框线偏移
		if(XmlAnalysis_result.get(2)!=null && XmlAnalysis_result.get(2).size()!=0){
			float pipe[] = new float[2];// x和y坐标
			pipe = offset();
			xoffset_pipe = pipe[0];
			yoffset_pipe = pipe[1];
		}
		else{
			xoffset_pipe = xoffset_outline;
			yoffset_pipe = yoffset_outline;
		}
		// 缓存初始数据，复位时用
		originalScale = scale;
		originalCenterx = centerx;
		originalCentery = centery;
		originalOutline_x = xoffset_outline;
		originalOutline_y = yoffset_outline;
		originalPipe_x = xoffset_pipe;
		originalPipe_y = yoffset_pipe;
	}

	/**
	 * 计算管子跟管架的偏移量
	 */
	@SuppressWarnings("unchecked")
	private float[] offset() {

		float pipe[] = new float[2];

		if (null == XmlAnalysis_result)
			return pipe;
		float sortArr[] = null; // 原，将要排序的管子数据(String)
		float sortArr_[] = null; // 转换后的管子数据(float)
		float sortArr_x[] = null; // 转换后的管子x坐标
		float sortArr_y[] = null; // 转换后的管子y坐标
		float x_max = 0, x_min = 0, y_max = 0, y_min = 0;
		// 没有管架，有管子，用管子算x跟y偏移量
		if (XmlAnalysis_result.get(2).size() == 0
				&& XmlAnalysis_result.get(3).size() != 0) {
			ArrayList<Lininfo> PipeCrosssection = this.XmlAnalysis_result
					.get(3);
			String sortx = null;
			String sorty = null;
			int i;
			// 统一加上“,”,便于转换
			for (i = 0; i < PipeCrosssection.size() - 1; i++) {
				Lininfo lininfo;
				if (sortx == null) {
					lininfo = PipeCrosssection.get(i);
					sortx = lininfo.getCenterX() + ",";
					sorty = lininfo.getCenterY() + ",";
				} else {
					lininfo = PipeCrosssection.get(i);
					sortx += lininfo.getCenterX() + ",";
					sorty += lininfo.getCenterY() + ",";
				}
			}
			Lininfo temp = PipeCrosssection.get(i);
			sortx += temp.getCenterX();
			sorty += temp.getCenterY();
			sortArr_x = stringToFloat(sortx);
			sortArr_y = stringToFloat(sorty);
			sortArr_y = TranslateLine1(sortArr_y);
			x_max = x_min = sortArr_x[0];
			y_max = y_min = sortArr_y[0];
			// 寻找管子中x最大值跟最小值
			for (int m = 0; m < sortArr_x.length; m++) {
				if (sortArr_x[m] > x_max)
					x_max = sortArr_x[m];
				if (sortArr_x[m] < x_min)
					x_min = sortArr_x[m];
			}
			// 寻找管子中y最大值跟最小值
			for (int m = 0; m < sortArr_y.length; m++) {
				if (sortArr_y[m] > y_max)
					y_max = sortArr_y[m];
				if (sortArr_y[m] < y_min)
					y_min = sortArr_y[m];
			}
			// 这里的xoffset_pipe其实是管架的偏移
			this.x[0] = x_min;
			this.x[1] = x_max;
			this.y[0] = y_min;
			this.y[1] = y_max;
			yoffset_pipe = (float) (centery - scale * (y_max - y_min) / 2 - scale
					* y_min);
			xoffset_pipe = (float) (centerx - scale * (x_max - x_min) / 2 - scale
					* x_min);
			pipe[0] = xoffset_pipe;
			pipe[1] = yoffset_pipe;
		}
		// 有管架，用管架算x，y偏移量
		else if (XmlAnalysis_result.get(2).size() != 0) {
			ArrayList<ArrayList<String>> PipeFrameInfo = this.XmlAnalysis_result
					.get(2);
			String sort = null;
			int i;
			// 统一加上“,”,便于转换
			for (i = 0; i < PipeFrameInfo.size() - 1; i++) {
				ArrayList<String> lxy;
				if (sort == null) {
					lxy = PipeFrameInfo.get(i);
					sort = lxy.get(2) + ",";
				} else {
					lxy = PipeFrameInfo.get(i);
					sort += lxy.get(2) + ",";
				}
			}
			ArrayList<String> temp = PipeFrameInfo.get(i);
			sort += temp.get(2);
			sortArr = stringToFloat(sort);
			sortArr_ = TranslateLine2(sortArr);
			x_max = x_min = sortArr_[0];
			y_max = y_min = sortArr_[1];
			// 寻找管架中x最大值跟最小值
			for (int m = 0; m < sortArr_.length; m += 2) {
				if (sortArr_[m] > x_max)
					x_max = sortArr_[m];
				if (sortArr_[m] < x_min)
					x_min = sortArr_[m];
			}
			// 寻找管架中y最大值跟最小值
			for (int m = 1; m < sortArr_.length; m += 2) {
				if (sortArr_[m] > y_max)
					y_max = sortArr_[m];
				if (sortArr_[m] < y_min)
					y_min = sortArr_[m];
			}
			this.x[0] = x_min;
			this.x[1] = x_max;
			this.y[0] = y_min;
			this.y[1] = y_max;
			// 这里的xoffset_pipe其实是管架的偏移
			yoffset_pipe = (float) (centery - scale * (y_max - y_min) / 2 - scale
					* y_min);
			xoffset_pipe = (float) (centerx - scale * (x_max - x_min) / 2 - scale
					* x_min);
			pipe[0] = xoffset_pipe;
			pipe[1] = yoffset_pipe;
		}
		return pipe;
	}

	/**
	 * 把字符串"X,X,X,…"转换成double数组
	 *
	 * @param str
	 * @return
	 */
	private float[] stringToFloat(String str) {
		float floatResult[] = null;
		String[] strarr;
		try {
			strarr = str.split(",");
			floatResult = new float[strarr.length];
			for (int i = 0; i < strarr.length; i++)
				floatResult[i] = Float.parseFloat(strarr[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return floatResult;
	}

	/**
	 * 解析结果是电脑坐标，坐标原点在左下，右x上y， 要转换成屏幕坐标，坐标原点在左上，右x下y， 该方法会把所有y坐标全部转换
	 *
	 * @param f
	 *            将要转换的y坐标数组
	 * @return 转换成功的y坐标数组
	 */
	private float[] TranslateLine1(float f[]) {
		float f1[] = null;
		// 把所有坐标都作转换
		for (int i = 0; i < f.length; i++)
			f[i] = backGroundHeight - f[i];
		f1 = f;
		return f1;
	}

	/**
	 * 解析结果是电脑坐标，坐标原点在左下，右x上y 要转换成屏幕坐标，坐标原点在左上，右x下y 该方法会把y坐标全部转换
	 *
	 * @param f
	 *            将要转换的坐标数组(有x有y)
	 * @return 转换成功的坐标数组
	 */
	private float[] TranslateLine2(float f[]) {
		float f1[] = null;
		for (int i = 1; i < f.length; i += 2) {
			f[i] = backGroundHeight - f[i];
		}
		f1 = f;
		return f1;
	}

	/**
	 * 把字符串"RGB(X,X,X)"转换成颜色数值
	 *
	 * @param str
	 *            将要转换的颜色字符串
	 * @return 转换成功的int类型的颜色数值
	 */
	private static int stringToRGBint(String str) {
		int color = 0;
		String[] strarr = null;
		try {
			// 去掉左括号，因为()是正则表达式符号，所以要加[]或者\\
			strarr = str.split("[(]");
			// 去掉RGB跟右括号
			strarr = strarr[1].split("[)]");
			// 去掉逗号
			strarr = strarr[0].split(",");
			int result[] = new int[strarr.length];
			for (int i = 0; i < strarr.length; i++)
				result[i] = Integer.parseInt(strarr[i]);
			if (result[0] >= 0 && result[0] <= 255 && result[1] >= 0
					&& result[1] <= 255 && result[2] >= 0 && result[2] <= 255)
				color = Color.rgb(result[0], result[1], result[2]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return color;
	}

	/**
	 * Zoom in the picture of the section analysis result. Only when zoomin
	 * button was pressed, it is called.
	 */
	public void zoomIn() {
		this.scale += zoomInScale;
		OffsetReset();
		invalidate();
	}

	/**
	 * Zoom in the picture of the section analysis result. Only when the zoomin
	 * gesture was done, it is called.
	 *
	 * @param zoomInScale
	 *            the scale of zooming in will be used.
	 */
	public void zoomIn(float zoomInScale) {
		if (this.scale * zoomInScale > 0.1) {
			this.scale *= zoomInScale;
			OffsetReset();
			invalidate();
		}
	}

	/**
	 * Zoom out the picture of the section analysis result. Only when zoomin
	 * button was pressed, it is called.
	 */
	public void zoomOut() {
		if (this.scale - zoomOutScale > 0) {
			this.scale -= zoomOutScale;
			OffsetReset();
			invalidate();
		}
	}

	/**
	 * Recover the initial location of the picture of the section analysis
	 * result. Only when zoomin button was pressed, it is called.
	 */
	public void reSet() {
		centerx = originalCenterx;
		centery = originalCentery;
		scale = originalScale;
		xoffset_outline = originalOutline_x;
		yoffset_outline = originalOutline_y;
		xoffset_pipe = originalPipe_x;
		yoffset_pipe = originalPipe_y;
		highLight = 0;
		pipePaint.setStrokeWidth(paintWidth_pipe);
		invalidate();
	}

	/**
	 * When the finger drags, the picture of section should be tanslated.
	 *
	 * @param x
	 *            the draging distance of x direction.
	 * @param y
	 *            the draging distance of y direction.
	 */
	public void Translate(float x, float y) {
		// 中心线随之改变
		centerx += x;
		centery += y;
		OffsetReset();
		invalidate();
	}

	/**
	 * 重新计算外框线跟管线的偏移量
	 */
	private void OffsetReset() {
		yoffset_outline = centery - scale * backGroundHeight / 2;
		xoffset_outline = centerx - scale * backGroundWidth / 2;
		if(XmlAnalysis_result.get(2)!=null && XmlAnalysis_result.get(2).size()!=0){
			yoffset_pipe = (float) (centery - scale * (this.y[1] - this.y[0]) / 2 - scale
					* this.y[0]);
			xoffset_pipe = (float) (centerx - scale * (this.x[1] - this.x[0]) / 2 - scale
					* this.x[0]);
		}else{
			yoffset_pipe = yoffset_outline;
			xoffset_pipe = 	xoffset_outline;
		}
	}

	/**
	 * When the pipes image was clicked, the information of pipes will popup. If
	 * nothing was clicked, a warning box will popup.
	 *
	 * @param x
	 *            the x coordinate of clicking location.
	 * @param y
	 *            the y coordinate of clicking location.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void ClickQueryPopup(float x, float y) {
		int i;
		// 设置容差
		final float tolerance = 5 * density;
		ArrayList<ArrayList> XmlAnalysis_result = this.XmlAnalysis_result;
		ArrayList<Lininfo> pipeCrosssection = (XmlAnalysis_result.get(3));
		ArrayList<ArrayList> circleCollision = new ArrayList<ArrayList>(1);
		for (i = 0; i < pipeCrosssection.size(); i++) {
			Lininfo crossSectionLininfo = pipeCrosssection.get(i);
			float rad = Float.parseFloat(crossSectionLininfo.getRad());
			float cx = Float.parseFloat(crossSectionLininfo.getCenterX());
			// 转换为屏幕坐标
			float cy = backGroundHeight - Float.parseFloat(crossSectionLininfo.getCenterY());
			// 转换为显示坐标
			cx = scale * cx + xoffset_pipe;
			cy = scale * cy + yoffset_pipe;
			rad = scale * rad;
			// 计算圆心之间距离
			double sqrtX = Math.pow(Math.abs(cx - x), 2);
			double sqrtY = Math.pow(Math.abs(cy - y), 2);
			float distance = (float) Math.sqrt(sqrtX + sqrtY);// 圆心距离
			// 如果符合查询条件，把半径、编号、半径存入
			if (CircleCollision(distance, rad, tolerance)) {
				ArrayList<Number> temp = new ArrayList<Number>(2);
				temp.add(distance);
				temp.add(i);
				temp.add(rad);
				circleCollision.add(temp);
			}
		}
		// 只有一个管子符合条件
		if (circleCollision.size() == 1) {
			int m = (Integer) circleCollision.get(0).get(1);
			Lininfo CrosssectionLininfo = pipeCrosssection.get(m);
			HighLight(CrosssectionLininfo);
			PopupShow(CrosssectionLininfo);

		}
		// 多个管子
		else if (circleCollision.size() != 1 && circleCollision.size() != 0) {
			float minDistance = (Float) circleCollision.get(0).get(0);
			int minNum = (Integer) circleCollision.get(0).get(1);
			float minRad = (Float) circleCollision.get(0).get(2);
			for (int j = 1; j < circleCollision.size(); j++) {
				// 求出距离单击点最近的管子
				if (minDistance - (Float) circleCollision.get(j).get(0) > 0.05) {// 距离大且不近似相等
					minDistance = (Float) circleCollision.get(j).get(0);
					minNum = (Integer) circleCollision.get(j).get(1);
					minRad = (Float) circleCollision.get(j).get(2);
				}
				// 大管套小管，比较半径，选小的
				else if (Math.abs(minDistance
						- (Float) circleCollision.get(j).get(0)) < 0.05) {// 近似相等
					if (minRad > (Float) circleCollision.get(j).get(2)) {
						minRad = (Float) circleCollision.get(j).get(2);
						minNum = (Integer) circleCollision.get(j).get(1);
					}
				}
			}
			Lininfo CrosssectionLininfo = pipeCrosssection.get(minNum);
			HighLight(CrosssectionLininfo);
			PopupShow(CrosssectionLininfo);
		}
		// 遍寻管子，没有交叉说明没点击到
		if (circleCollision.size() == 0)
			setToast("没有准确点击到管线断面，请重试！");
	}

	/**
	 * 判断在容差范围内，手指是否点击到管线
	 *
	 * @param distance
	 *            圆心间距
	 * @param rad
	 *            管线的半径
	 * @param tolerance
	 *            设置的容差
	 * @return
	 */
	private boolean CircleCollision(float distance, float rad, float tolerance) {
		// 圆心距离小于半径和，说明碰撞
		if (distance < (rad + tolerance))
			return true;
		else
			return false;
	}

	/**
	 * 弹出popup
	 *
	 * @param CrosssectionLininfo
	 *            被查找到的管线信息
	 */
	@SuppressLint("InflateParams")
	private void PopupShow(Lininfo CrosssectionLininfo) {

		QueryPopup querypopup = new QueryPopup(context, this,
											   CrosssectionLininfo);
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		// 获取父控件
		View view = inflater.inflate(R.layout.activity_crossanalysisresult,
				null);
		querypopup.showPopup(view);
	}

	private void setToast(String toastTextString) {
		Toast.makeText(context, toastTextString, Toast.LENGTH_SHORT).show();
	}

	/**
	 * When the pipe was clicked, the color of it will be change.
	 * 
	 * @param crossSectionLininfo
	 *            the pipe information which is clicked.
	 */
	public void HighLight(Lininfo crossSectionLininfo) {
		highLight = Integer.parseInt(crossSectionLininfo.getGid());
		invalidate();
	}

	/***
	 * 保存图片到相册或是固定路径-共外部使用
	 * 
	 * @param filePath
	 */
	public boolean saveCanvasToAlbum(String filePath) { // 图片可以成功加入到相册当中，没有问题
		Bitmap bitmap = getDrawingCache();
		if (null == filePath || null == bitmap)
			return false;
		try {
			saveBitmapToFile(bitmap, filePath);
			Toast.makeText(context, "保存到本地成功！", Toast.LENGTH_SHORT).show();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(context, "保存失败：" + e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
		return false;
	}

	/**
	 * Save Bitmap to a file.保存图片到SD卡。
	 * 
	 * @param bitmap
	 * @param file
	 * @return error message if the saving is failed. null if the saving is
	 *         successful.
	 * @throws IOException
	 */
	private void saveBitmapToFile(Bitmap bitmap, String _file)
			throws IOException {
		BufferedOutputStream os = null;
		try {
			File file = new File(_file);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			os = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					Toast.makeText(context, "保存失败：" + e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}