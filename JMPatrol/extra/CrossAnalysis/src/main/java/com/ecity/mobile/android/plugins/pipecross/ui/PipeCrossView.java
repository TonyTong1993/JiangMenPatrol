/**   
 * 文件名：PipeCrossView.java   
 *   
 * 版本信息：   
 * 日期：2016年6月30日   
 * Copyright Ecity Corporation 2016    
 * 版权所有   
 *   
 */

package com.ecity.mobile.android.plugins.pipecross.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.ecity.mobile.android.crossanalysis.PipeColorBean;
import com.ecity.mobile.android.crossanalysis.R;
import com.ecity.mobile.android.crossanalysis.util.DataUtil;
import com.ecity.mobile.android.crossanalysis.util.ListUtil;

/**
 * 此类描述的是：
 * 
 * @author: wly
 * @version: 2016年6月30日 上午9:35:47
 */

public class PipeCrossView extends View {
    Context context;
    private Bitmap csaBitmap;
    private float density;
    public static float csaWidth;
    public static float title = 45;
    public static int NONE = 1;
    public static int DRAG = 2;
    public static int ZOOM = 3;
    public int actionEvent = NONE;
    private float oldX = 0, oldY = 0, newX = 0, newY = 0;
    private float oldDistance = 0, newDistance;
    private float translateX, translateY;
    private long upTime = 0, downTime = 0;
    private float csaHeight;
    private float centerx;
    private float centery;
    // 背景图像的宽高
    int bmpWidth, bmpHeight;
    float startWidth;
    double dealWidth;
    int img2table = 10;
    float split1X;
    float split2X;
    double minDepth, maxDepth;
    // 不含屏幕坐标的
    public static List<PipeItem> dataSource = new ArrayList<PipeItem>();
    private List<PipeColorBean> pipeColorBeans = new ArrayList<PipeColorBean>();
    /**
     * 不含重复的管线用于绘制断面图图例
     */
    private static List<PipeItem> singleDataSource = new ArrayList<PipeItem>();
    Canvas canvas;
    private float zoomScale;
    float scaleX = 1.0f;
    float scaleY = 1.0f;
    float scale = 1.0f;
    private float splitY;
    private float menRoadX;
    private float menRoady;
    boolean zoom = false;

    Matrix matrix;
    float tableStartX = startWidth;
    float tableStartY = 10 + bmpHeight;
    // 记录初始数据
    private float origanlDealWidth;
    private float origanlDealHeight;
    private float origanlCenterX;
    private float origanlCenterY;
    float dealHeight = 0.8f;
    private Bitmap bitmap;
    private float scalex;
    private float scaley;
    private int bgHeight;
    private float orgStartWidth;
    private boolean show;
    private int showItem = -1;

    public PipeCrossView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        this.context = context;
        ViewParameterInit();
        this.setClickable(true);
        this.setLongClickable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;

        // 初始化外框点字体大小
        canvas.drawColor(Color.rgb(255, 250, 240));
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setDither(true);
        drawBackGround(canvas, paint);
        drawHelpLine(canvas, paint);
        dealData(canvas, paint);
        drawTable(canvas, paint);
        drawTuLi(canvas, paint);
        drawCompass(canvas, paint);
        super.onDraw(canvas);
    }

    // 部分参数初始化
    private void ViewParameterInit() {
        density = CrossResultActivity.density;
        // 计算屏幕宽
        csaWidth = CrossResultActivity.screenWidth;
        csaHeight = CrossResultActivity.screenHeight - title * density - 0.5f;// title
        csaBitmap = Bitmap.createBitmap((int) csaWidth, (int) csaHeight,
                Bitmap.Config.ARGB_8888);
        new Canvas(csaBitmap);
        // 屏幕中心线x，y坐标初始化
        centerx = csaWidth / 2;
        centery = (csaHeight / 2 - 20 * density + 10);
        origanlCenterX = centerx;
        origanlCenterY = centery;

        Resources res = getResources();
        // bitmap:1221*450
        bitmap = BitmapFactory.decodeResource(res, R.drawable.bg);
        int orgWidth = bitmap.getWidth();
        dealWidth = csaWidth / orgWidth;
        dealWidth = Math.round(dealWidth * 100) / 100.00 - 0.01;
        origanlDealHeight = dealHeight;
        origanlDealWidth = (float) dealWidth;
    }

    // 绘制背景
    protected void drawBackGround(Canvas canvas, Paint paint) {
        // 定义矩阵对象
        matrix = new Matrix();
        // 缩放原图
        matrix.postScale((float) dealWidth * scale, dealHeight * scale);
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        // 缩放后宽高
        bmpWidth = dstbmp.getWidth();
        bmpHeight = dstbmp.getHeight();
        bgHeight = canvas.getHeight();
        orgStartWidth = startWidth = (csaWidth - bmpWidth) / 2;
        startWidth = (csaWidth - bmpWidth) / 2 + scalex;
        canvas.drawBitmap(dstbmp, startWidth, 10 * scale + scaley, paint);
    }

    protected void drawHelpLine(Canvas canvas, Paint paint) {
        // 从背景图换算为屏幕分辨率
        paint.setColor(Color.BLUE);
        // 刻度线 人行道距顶部108 距左端250 图片高450
        menRoadX = (float) (startWidth + 250.0 / 1221.0 * bmpWidth);
        menRoady = (float) (108 / 450.0 * bmpHeight + 10 * scale) + scaley;
        canvas.drawLine(menRoadX, menRoady, menRoadX, bmpHeight + 10 * scale
                + scaley, paint);
        paint.setColor(Color.BLACK);
        canvas.drawLine(menRoadX, bmpHeight + 10 * scale + scaley, menRoadX,
                bmpHeight + 195 * scale + scaley, paint);
        // 刻度横线
        float centerY = (float) (129 / 450.0 * bmpHeight + 10 * scale) + scaley;
        float kedu = (bmpHeight - centerY + 10 * scale + scaley) / 5;
        paint.setStyle(Paint.Style.FILL);// 设置填满
        paint.setTextSize(10 * scale);

        double maxDep = PipeAnalysis.maxPipeDep;
        if (maxDep == 0) {
            for (float i = centerY, j = 0; i <= (bmpHeight + 10 * scale)
                    && j < 6;) {
                if (j != 0) {
                    canvas.drawText("-" + String.valueOf(j),
                            (menRoadX - 15 * scale), i, paint);
                } else {
                    canvas.drawText("0", (menRoadX - 15 * scale), i, paint);
                }
                // 刻度横线 160px背景图中左人行道起始位置(px), 缺省值
                canvas.drawLine(menRoadX, i, (menRoadX + 10 * scale), i, paint);
                i += kedu;
                j++;
            }
        } else {
            maxDep = Math.round(maxDep * 100) / 100.00;
            double yTemp;
            double yPipe = maxDep + 3.00;
            for (float i = centerY, j = 0; i <= (bmpHeight + 10 * scale)
                    && j < 6; j++) {
                yTemp = maxDep - yPipe / 5.0 * j;
                yTemp = Math.round(yTemp * 100) / 100.00;
                canvas.drawText(yTemp + "", (menRoadX - 15 * scale), i, paint);
                // 刻度横线 160px背景图中左人行道起始位置(px), 缺省值
                canvas.drawLine(menRoadX, i, (menRoadX + 10 * scale), i, paint);
                i += kedu;
            }
        }

        paint.setColor(Color.BLACK);
        // 虚线
        paint.setPathEffect(new DashPathEffect(new float[] { 3, 2 }, 0));
        // 分割线 1 距左端352
        split1X = (float) (startWidth + 352.0 / 1221.0 * bmpWidth);
        splitY = (float) (129 / 450.0 * bmpHeight + 10 * scale) + scaley;
        // 分割线 2 距左端932
        split2X = (float) (startWidth + 932.0 / 1221.0 * bmpWidth);
        canvas.drawLine(split1X, splitY, split1X, bmpHeight + 10 * scale
                + scaley, paint);
        canvas.drawLine(split2X, splitY, split2X, bmpHeight + 10 * scale
                + scaley, paint);
        // 中心线 距顶部129
        canvas.drawLine((float) (startWidth + 643.0 / 1221.0 * bmpWidth),
                splitY, (float) (startWidth + 643.0 / 1221.0 * bmpWidth),
                bmpHeight + 10 * scale + scaley, paint);
    }

    /**
     * 
     * 此方法描述的是： 管网
     * 
     * @author: wly
     * @version: 2016年7月16日 下午2:02:38
     */
    protected void dealData(Canvas canvas, Paint paint) {
        double queryX1 = CrossResultActivity.qureyX1;
        double queryX2 = CrossResultActivity.qureyX2;
        double queryY1 = CrossResultActivity.qureyY1;
        double queryY2 = CrossResultActivity.qureyY2;
        dataSource = PipeAnalysis.data;
        pipeColorBeans = DataUtil.getColorBeans();
        handlerPipeData();
        float length = split2X - split1X;
        double denstyX = length / (queryX2 - queryX1);
        // 画管子
        for (int i = 0; i < dataSource.size(); i++) {
            // off来处理偏移
            int off = 0;
            int offCount = 2;
            PipeItem item = dataSource.get(i);
            float itemX = (float) item.getX();
            float itemY = (float) item.getY();
            // 管间距
            double interSpace = Math.sqrt(Math.pow(itemX - queryX1, 2)
                    + Math.pow(itemY - queryY1, 2));
            double x = item.getX();
            // split1.2距离（坐标）
            double splitLen = Math.sqrt(Math.pow(queryX1 - queryX2, 2)
                    + Math.pow(queryY1 - queryY2, 2));
            // 管网距分割点1
            double x1Len = Math.sqrt(Math.pow(itemX - queryX1, 2)
                    + Math.pow(itemY - queryY1, 2));
            double pipeDepth = item.getPipeDepth();
            double diamaters = item.getDianeters()* 2;// 由于解析数据的时候除以了2
            double groundAltitude = item.getGroundAltitude();
            double pipeAltitude = item.getPipeAltitude();
            itemX = (float) (x1Len / splitLen * length + split1X);
            double y = (PipeAnalysis.maxPipeDep - pipeAltitude)
                    / (PipeAnalysis.maxPipeDep + 3);
            itemY = (float) (splitY + y * (bmpHeight - splitY + scaley));
            item.setItemX(itemX);
            item.setItemY(itemY);
            paint.setColor(Color.BLUE);
            int pipeColor = item.getPipeColor();
            paint.setColor(pipeColor);
            canvas.drawPoint((float) itemX, (float) itemY, paint);
            paint.setPathEffect(null);
            paint.setStyle(Paint.Style.STROKE);
            if (show) {
                if (showItem == i) {
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.RED);
                }
            }
            canvas.drawCircle((float) itemX, (float) itemY, (float) 10 * scale,
                    paint);
            paint.setPathEffect(new DashPathEffect(new float[] { 3, 2 }, 0));
            paint.setStyle(Paint.Style.FILL);// 设置填满
            // 当两个管子考的很近时，进行偏移 距离10
            if (i < dataSource.size() - 1) {
                PipeItem itemNext = dataSource.get(i + 1);
                double xNext = itemNext.getX();
                double xLeng = (xNext - x) * denstyX;
                if (xLeng < 20 * scale) {
                    off = 1;
                    if (xLeng < 5 * scale) {
                        offCount--;
                    }
                } else {
                    off = 0;
                }
            } else {
                off = 0;
            }

            if (off == 0) {
                canvas.drawLine(itemX, itemY, itemX, bmpHeight + 10 * scale
                        + scaley, paint);
                paint.setPathEffect(null);
                paint.setColor(Color.BLACK);
                drawText(canvas, item.getPipeName(), itemX, bmpHeight + 10
                        * scale + scaley, paint, -90);
                canvas.drawLine(itemX, bmpHeight + 10 * scale + scaley, itemX,
                        bmpHeight + 195 * scale + scaley, paint);
            } else {
                // 10---20
                canvas.drawLine(itemX, itemY, itemX - 10 * scale * offCount,
                        itemY + 10 * scale, paint);
                canvas.drawLine(itemX - 10 * scale * offCount, itemY + 10
                        * scale, itemX - 10 * scale * offCount, bmpHeight + 10
                        * scale + scaley, paint);
                paint.setPathEffect(null);
                paint.setColor(Color.BLACK);
                drawText(canvas, item.getPipeName(), itemX - 10 * scale
                        * offCount, bmpHeight + 10 * scale + scaley, paint, -90);
                canvas.drawLine(itemX - 10 * scale * offCount, bmpHeight + 10
                        * scale + scaley, itemX - 10 * scale * offCount,
                        bmpHeight + 195 * scale + scaley, paint);
            }

            java.text.DecimalFormat df = new java.text.DecimalFormat("0.0");
            String[] tableContent = { groundAltitude + "", pipeAltitude + "",
                    df.format(pipeDepth) + "", df.format(diamaters) + "",
                    df.format(interSpace) + "" };
            paint.setTextSize(10 * scale);// 设置字体大小
            for (int j = 0; j < 5; j++) {
                // 填表格 坐标为左下角坐标
                drawText(canvas, tableContent[j], itemX + 10 * scale - 10
                        * scale * off * offCount, (bmpHeight + 50 * scale + j
                        * 35 * scale)
                        + scaley, paint, -90);
            }
        }
    }
    /**
     * 对于管线颜色及管线所处位置等进行设置
     * 
     * @author WangFeng<br/>
     *         Create at 20172017-1-18下午2:26:44
     */
    private void handlerPipeData() {
        if (ListUtil.isEmpty(dataSource)) {
            return;
        }
        if (ListUtil.isEmpty(pipeColorBeans)) {
            return;
        }
        for (int i = 0; i < dataSource.size(); i++) {
            PipeItem pipeItem = dataSource.get(i);
            for (int j = 0; j < pipeColorBeans.size(); j++) {
                if (pipeItem.getCode().equalsIgnoreCase(
                        pipeColorBeans.get(j).getCode())) {
                    pipeItem.setPipeColor(pipeColorBeans.get(j).getColor());
                    pipeItem.setPipeAltitudeType(pipeColorBeans.get(j)
                            .getPipeAltitudeType());
                    dataSource.set(i, pipeItem);
                    singleDataSource.add(pipeItem);
                    break;
                }
            }
        }
    }
    /*
     * 画表格 单元格高35 5*1 表格与图间隔10
     */
    protected void drawTable(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < 6; i++) {
            canvas.drawLine(startWidth, (bmpHeight + 20 * scale + i * 35
                    * scale)
                    + scaley, (startWidth + bmpWidth),
                    (bmpHeight + 20 * scale + i * 35 * scale) + scaley, paint);
        }

        String[] str = { "地面高程(米)", "管线高程(米)", "埋深(米)", "管径(毫米)", "间距(米)" };
        paint.setTextSize(16 * scale);
        paint.setPathEffect(null);
        for (int i = 0; i < 5; i++) {
            canvas.drawText(str[i], (startWidth + 2 * scale),
                    (float) (bmpHeight + 40 * scale + i * 35 * scale) + scaley,
                    paint);
        }
        canvas.drawLine(startWidth, (bmpHeight + 20 * scale) + scaley,
                startWidth, (bmpHeight + 195 * scale) + scaley, paint);
        canvas.drawLine((startWidth + bmpWidth), (bmpHeight + 20 * scale)
                + scaley, (startWidth + bmpWidth), (bmpHeight + 195 * scale)
                + scaley, paint);
    }
    /**
     *  通过循环删除 code相同的管线图例
     * @author WangFeng<br/>
     * Create at 20172017-1-20下午5:12:27
     */
    public static void removeAlike(List<PipeItem> list) {  
        int size = list.size();  
        for (int i = 0; i < size - 1; i++) {  
            for (int j = size - 1; j > i; j--) {  
                if (list.get(j).getCode().equalsIgnoreCase(list.get(i).getCode())) {  
                    list.remove(j); 
                    size--;
                }  
            }  
        }  
    }  
    /**
     * 
     * 此方法描述的是： 画图例
     * 
     * @author: wly
     * @version: 2016年7月14日 下午1:55:13
     */
    protected void drawTuLi(Canvas canvas, Paint paint) {
        if(ListUtil.isEmpty(singleDataSource)){
            return; 
         }
         removeAlike(singleDataSource);
         paint.setStyle(Paint.Style.STROKE);
         paint.setColor(Color.WHITE);
         canvas.drawRect((startWidth + bmpWidth - 80 * scale),
                 (bmpHeight - 10 * scale) + scaley, (startWidth + bmpWidth),
                 (bmpHeight - 10 * scale) - (singleDataSource.size()) * 20
                         * scale + scaley, paint);
         for (int i = 0; i < singleDataSource.size(); i++) {
             paint = new Paint();
             PipeItem item = singleDataSource.get(i);
             paint.setColor(item.getPipeColor());
             canvas.drawCircle((startWidth + bmpWidth - 70 * scale), (bmpHeight
                     - 20 * scale - i * 20 * scale)
                     + scaley, 3 * scale, paint);
             paint.setStyle(Paint.Style.FILL);
             canvas.drawText(item.getPipeName(),
                     (startWidth + bmpWidth - 60 * scale), (bmpHeight - 20
                             * scale + 3 * scale - i * 20 * scale)
                             + scaley, paint);
         }
    }

    /**
     * compass 此方法描述的是：指南针
     * 
     * @author: wly
     * @version: 2016年7月8日 下午2:51:01
     */
    protected void drawCompass(Canvas canvas, Paint paint) {

        // 半径35 背景图距顶端10 字体10
        paint.setColor(Color.rgb(254, 246, 231));

        canvas.drawCircle((startWidth + 35 * scale), 45 * scale + scaley,
                35 * scale, paint);
        paint.setColor(Color.RED);
        canvas.drawCircle((startWidth + 35 * scale), 45 * scale + scaley,
                5 * scale, paint);
        paint.setTextSize(10 * scale);
        canvas.drawText("W", (startWidth + 5 * scale), 50 * scale + scaley,
                paint);
        canvas.drawText("N", (startWidth + 35 * scale), 20 * scale + scaley,
                paint);
        canvas.drawText("E", (startWidth + 60 * scale), 50 * scale + scaley,
                paint);
        canvas.drawText("S", (startWidth + 30 * scale), 75 * scale + scaley,
                paint);

        double theta = PipeAnalysis.getCompass();
        float comX = (float) (startWidth + 35 * scale + 35 * Math.sin(theta)
                * scale);
        float comY = (float) (45 + Math.cos(theta) * 35) * scale + scaley;
        Path path = new Path();
        path.moveTo((startWidth + 30 * scale), 45 * scale + scaley);
        path.lineTo((startWidth + 40 * scale), 45 * scale + scaley);
        path.lineTo(comX, comY);
        path.close();
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
            oldX = event.getX();
            oldY = event.getY();
            downTime = event.getDownTime();
            actionEvent = DRAG;
            show = false;
            showItem = -1;
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
            zoom = true;
            oldDistance = (float) Math.sqrt((event.getX(0) - event.getX(1))
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
                newDistance = (float) Math.sqrt((event.getX(0) - event.getX(1))
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
            if (upTime - downTime < 200 && false == zoom
                    && (Math.abs(translateX) <= 5 && Math.abs(translateY) <= 5)) {
                ClickQueryPopUp(oldX, oldY);
            }
            actionEvent = NONE;
            zoom = false;
            break;
        case MotionEvent.ACTION_POINTER_UP:
            actionEvent = NONE;
            show = false;
            showItem = -1;
            break;
        }
        return true;
    }

    /**
     * 
     * 此方法描述的是： 点击查询 距离最近
     * 
     * @author: wly
     * @version: 2016年7月11日 上午11:40:41
     */
    protected void ClickQueryPopUp(float x, float y) {

        if (dataSource.size() != 0) {
            float itemx;
            float itemy;
            int min = 0;
            double minlength = Double.POSITIVE_INFINITY;
            double length = Double.POSITIVE_INFINITY;
            for (int i = 0; i < dataSource.size(); i++) {
                itemx = dataSource.get(i).getItemX();
                itemy = dataSource.get(i).getItemY();
                length = Math.sqrt(Math.pow(itemx - x, 2)
                        + Math.pow(itemy - y, 2));
                if (length < minlength) {
                    minlength = length;
                    min = i;
                }
            }
            if (minlength < 50) {
                PipeItem pipe = dataSource.get(min);
                show = true;
                showItem = min;
                invalidate();
                PopupShow(pipe);
            } else {
                Toast.makeText(context, "没有点击到相应的管网类型，请重试！！！",
                        Toast.LENGTH_SHORT).show();
            }

        } else {

        }
    }

    /**
     * 
     * 此方法描述的是： 弹出查询框
     * 
     * @author: wly
     * @version: 2016年7月11日 上午11:57:05
     */
    private void PopupShow(PipeItem item) {
        QueryPopup queryPopup = new QueryPopup(context, this, item);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_cross_result, null);
        queryPopup.showPopup(view);
    }

    /**
     * 
     * 此方法描述的是： 平移
     * 
     * @author: wly
     * @version: 2016年7月14日 下午1:58:06
     */
    protected void Translate(float x, float y) {
        // 中心线随之改变
        centerx += x;
        centery += y;
        offSetReset();
        invalidate();
    }

    /*
     * * 此方法描述的是： 放大
     * 
     * @author: wly
     * 
     * @version: 2016年7月16日 下午5:14:43
     */
    private void zoomIn(float zoomInScale) {
        if (this.scale * zoomInScale > 0.1) {
            this.scale *= zoomInScale;
            if (this.scale > 2)
                this.scale = 2;
            invalidate();
        }
    }

    /**
     * 
     * 此方法描述的是： 偏移
     * 
     * @author: wly
     * @version: 2016年7月16日 下午5:14:17
     */
    private void offSetReset() {
        scalex = centerx - bmpWidth / 2 - orgStartWidth;
        scaley = centery - bgHeight / 2 + 10 * scale;
    }

    /**
     * 
     * 此方法描述的是： 写横向文字
     * 
     * @author: wly
     * @version: 2016年7月14日 下午1:56:30
     */
    public void drawText(Canvas canvas, String text, float x, float y,
            Paint paint, float angle) {
        paint.setTextSize(10 * scale);
        if (angle != 0) {
            canvas.rotate(angle, x, y);
        }
        canvas.drawText(text, x, y, paint);
        if (angle != 0) {
            canvas.rotate(-angle, x, y);
        }
    }

    /**
     * 此方法描述的是：图片缩小
     * 
     * @author: wly
     * @version: 2016年7月8日 上午10:44:35
     */
    protected void zoomOut() {
        if (this.scale > 0.4) {
            this.scale -= 0.2f;
            offSetReset();
            invalidate();
        }
    }

    /**
     * 此方法描述的是：图片放大
     * 
     * @author: wly
     * @version: 2016年7月8日 上午10:44:35
     */
    protected void zoomIn() {

        if (scale < 2) {
            this.scale += 0.2;
        } else {
            Toast.makeText(context, "已放大到最大尺度", Toast.LENGTH_SHORT).show();
        }

        offSetReset();
        invalidate();
    }

    /**
     * 此方法描述的是：图片重置
     * 
     * @author: wly
     * @version: 2016年7月8日 上午10:44:35
     */
    protected void reSet() {
        dealWidth = origanlDealWidth;
        dealHeight = origanlDealHeight;
        zoomScale = 0;
        centerx = origanlCenterX;
        centery = origanlCenterY;
        scale = 1.0f;
        scalex = 0;
        scaley = 0;
        invalidate();
        show = false;
        showItem = -1;
    }

}
