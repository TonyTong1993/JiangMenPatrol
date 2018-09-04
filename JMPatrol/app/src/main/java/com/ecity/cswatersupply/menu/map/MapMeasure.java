package com.ecity.cswatersupply.menu.map;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.fragment.MapMainTabFragment;
import com.ecity.cswatersupply.ui.widght.MapFragmentTitleView;
import com.ecity.cswatersupply.ui.widght.NewMagnifier;
import com.ecity.cswatersupply.utils.CustomRenderer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.TextSymbol;

public class MapMeasure {
    private static final int POINT = 0;
    private static final int POLYLINE = 1;
    private static final int POLYGON = 2;
    private MapView mapView;
    ArrayList<Point> points = new ArrayList<Point>();
    ArrayList<Point> mpoints = new ArrayList<Point>();
    boolean midpointselected = false;
    boolean vertexselected = false;
    int insertingindex;
    int editingmode;
    private IMapOperationContext fragment;
    ArrayList<EditingStates> editingstates = new ArrayList<EditingStates>();
    protected String editingerrormessage;
    volatile int numUnInitedViews = 5;

    private GraphicsLayer sketchGraphicsLayer = null;
    private View customView = null;
    private MapFragmentTitleView mTitleView;

    public enum MeasureMode {
        Area, Length, UnKown
    }

    class EditingStates {
        ArrayList<Point> points1 = new ArrayList<Point>();
        boolean midpointselected1 = false;
        boolean vertexselected1 = false;
        int insertingindex1;

        public EditingStates(ArrayList<Point> points, boolean midpointselected, boolean vertexselected, int insertingindex) {
            this.points1.addAll(points);
            this.midpointselected1 = midpointselected;
            this.vertexselected1 = vertexselected;
            this.insertingindex1 = insertingindex;
        }
    }

    private MeasureMode measureMode;

    /**
     * @param paraContext
     *            上下文信息
     * @param mapView
     *            主视图 构造函数，继承父类方法，并初始化私有变量
     */
    public MapMeasure(IMapOperationContext fragment, MapView mapView, GraphicsLayer sketchGraphicsLayer, MeasureMode measureType) {
        this.fragment = fragment;
        this.mapView = mapView;
        measureMode = measureType;
        mTitleView = (MapFragmentTitleView) fragment.getmTitleView();
        if (fragment == null || mapView == null || sketchGraphicsLayer == null || measureMode == null) {
            return;
        }

        if (measureMode == MeasureMode.Length) {
            editingmode = POLYLINE;           
            setMapFragmentTitleView(R.string.measurelength, R.drawable.btn_mainmenu_clean);
        } else if (measureMode == MeasureMode.Area) {
            editingmode = POLYGON;
            setMapFragmentTitleView(R.string.measurearea, R.drawable.btn_mainmenu_clean);
        }
        this.sketchGraphicsLayer = sketchGraphicsLayer;
        sketchGraphicsLayer.removeAll();
        mapView.getCallout().hide();
    }

    private void setMapFragmentTitleView(int resId, int drawId) {
        mTitleView.setBackBtnVisible();
        mTitleView.setTitleText(resId);
        mTitleView.setQueryBtnGone();
        mTitleView.setOperatorBtnBackground(HostApplication.getApplication().getResources().getDrawable(drawId));
        mTitleView.setOnOperatorListener(new CleanClickListener());
        mTitleView.setOnBackListener(new BackClickListener());
    }

    public void start() {
        if (null == fragment || null == mapView || !mapView.isLoaded()) {
            return;
        }
        mapView.setOnTouchListener(new MyTouchListener(fragment.getContext(), mapView));
    } 

    public void stop() {

        if (mapView != null) {
            mapView.getCallout().hide();
        }
        resetValues();
    }

    public void setMeasureMode(MeasureMode measureType) {
        measureMode = measureType;
        if (measureMode == MeasureMode.Length) {
            editingmode = POLYLINE;
        } else if (measureMode == MeasureMode.Area) {
            editingmode = POLYGON;
        }
        resetValues();
    }
    
    private class CleanClickListener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            resetValues();
        }        
    }
    
    private class BackClickListener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            mTitleView.setBackBtnGone();
            mTitleView.setTitleText(R.string.fragment_map_title);
            mTitleView.setQueryBtnVisible();
            mTitleView.setOperatorBtnBackground(HostApplication.getApplication()
                    .getResources().getDrawable(R.drawable.selector_button_map_fragment_opretor)); 
            mTitleView.setOnOperatorListener(((MapMainTabFragment)fragment).getOperatorClickListener());
            EmptyMapCommandXtd clean = new EmptyMapCommandXtd();
            clean.execute(mapView, fragment);
        }
    }

    class MyTouchListener extends MapOnTouchListener {
        MapView map;
        Context context;
        NewMagnifier mag;
        Bitmap snapshot = null;
        boolean redrawCache = true;
        boolean showmag = false;

        public MyTouchListener(Context context, MapView view) {
            super(context, view);
            this.context = context;
            map = view;
        }

        @Override
        public void onLongPress(MotionEvent point) {
            magnify(point);
            showmag = true;
        }

        @Override
        public boolean onDragPointerMove(MotionEvent from, final MotionEvent to) {
            if (showmag) {
                magnify(to);
                return true;
            }
            return super.onDragPointerMove(from, to);
        }

        @Override
        public boolean onDragPointerUp(MotionEvent from, final MotionEvent to) {
            if (showmag) {
                if (mag != null) {
                    mag.hide();
                }
                mag.postInvalidate();
                showmag = false;
                redrawCache = true;
                Point point = map.toMapPoint(new Point(to.getX(), to.getY()));
                movePoint(point);
                refresh();
                return true;
            }
            return super.onDragPointerUp(from, to);
        }

        void magnify(MotionEvent to) {
            if (mag == null) {
                mag = new NewMagnifier(context, map);
                map.addView(mag);
                mag.prepareDrawingCacheAt(to.getX(), to.getY());
            } else {
                mag.prepareDrawingCacheAt(to.getX(), to.getY());
            }
            redrawCache = false;            
        }

        @Override
        public boolean onPinchPointersUp(MotionEvent event) {
            if (showmag) {
                if (mag != null) {
                    mag.hide();
                }
                mag.postInvalidate();
                showmag = false;
            }
            return super.onPinchPointersUp(event);
        }

        @Override
        public boolean onDoubleTap(MotionEvent point) {

            if (showmag) {
                if (mag != null) {
                    mag.hide();
                }
                mag.postInvalidate();
                showmag = false;
            }
            return super.onDoubleTap(point);
        }

        @Override
        public boolean onSingleTap(final MotionEvent e) {
            if (showmag) {
                if (mag != null) {
                    mag.hide();
                }
                mag.postInvalidate();
                showmag = false;
            }

            Point point = map.toMapPoint(new Point(e.getX(), e.getY()));
            if (editingmode == POINT)
                points.clear();
            if (!midpointselected && !vertexselected) {
                int idx1 = getSelectedIndex(e.getX(), e.getY(), mpoints, map);
                if (idx1 != -1) {
                    midpointselected = true;
                    insertingindex = idx1;
                }
                if (!midpointselected) {
                    int idx2 = getSelectedIndex(e.getX(), e.getY(), points, map);
                    if (idx2 != -1) {
                        vertexselected = true;
                        insertingindex = idx2;
                    }
                }
                if (!midpointselected && !vertexselected) {
                    points.add(point);
                    editingstates.add(new EditingStates(points, midpointselected, vertexselected, insertingindex));
                }
            } else {
                int idx1 = getSelectedIndex(e.getX(), e.getY(), mpoints, map);
                int idx2 = getSelectedIndex(e.getX(), e.getY(), points, map);
                if (idx1 == -1 && idx2 == -1)
                    movePoint(point);
                else if (idx1 != -1) {
                    midpointselected = true;
                    insertingindex = idx1;
                    vertexselected = false;
                } else if (idx2 != -1) {
                    vertexselected = true;
                    insertingindex = idx2;
                    midpointselected = false;
                }
            }
            refresh();
            redrawCache = true;
            return true;
        }
    }

    public void undo() {
        editingstates.remove(editingstates.size() - 1);
        EditingStates state = editingstates.get(editingstates.size() - 1);
        points.clear();
        points.addAll(state.points1);
        midpointselected = state.midpointselected1;
        vertexselected = state.vertexselected1;
        insertingindex = state.insertingindex1;
        refresh();
    }

    void movePoint(Point point) {
        if (midpointselected) {
            points.add(insertingindex + 1, point);
            editingstates.add(new EditingStates(points, midpointselected, vertexselected, insertingindex));
        } else if (vertexselected) {
            ArrayList<Point> temp = new ArrayList<Point>();
            for (int i = 0; i < points.size(); i++) {
                if (i == insertingindex)
                    temp.add(point);
                else
                    temp.add(points.get(i));
            }
            points.clear();
            points.addAll(temp);
            editingstates.add(new EditingStates(points, midpointselected, vertexselected, insertingindex));
        }
        midpointselected = false;
        vertexselected = false;
    }

    void refresh() {
        if (sketchGraphicsLayer != null) {
            sketchGraphicsLayer.removeAll();
        }
        drawPolyline();
        drawMidPoints();
        drawVertices();
    }

    private void drawMidPoints() {
        int index;
        Graphic graphic;
        if (sketchGraphicsLayer == null)
            return;
        if (points.size() > 1) {
            mpoints.clear();
            for (int i = 1; i < points.size(); i++) {
                Point p1 = points.get(i - 1);
                Point p2 = points.get(i);
                mpoints.add(new Point((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2));
            }
            if (editingmode == POLYGON) {
                Point p1 = points.get(0);
                Point p2 = points.get(points.size() - 1);
                mpoints.add(new Point((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2));
            }
            index = 0;
            for (Point pt : mpoints) {
                if (midpointselected && insertingindex == index)
                    graphic = new Graphic(pt, CustomRenderer.getInstance().PIPENETPO_POINT_CIRCLE_RED());
                else
                    graphic = new Graphic(pt, CustomRenderer.getInstance().PIPENETPO_POINT_CIRCLE_WIHTE());
                sketchGraphicsLayer.addGraphic(graphic);
                index++;
            }
        }
    }

    /***
     * 绘制点
     */
    private void drawVertices() {
        int index;
        index = 0;
        if (sketchGraphicsLayer == null) {
            return;
        }

        for (Point pt : points) {
            if (vertexselected && index == insertingindex) {
                Graphic graphic = new Graphic(pt, CustomRenderer.getInstance().PIPENETPO_POINT_CIRCLE_RED());
                sketchGraphicsLayer.addGraphic(graphic);
            } else if (index == 0) {
                Graphic graphic = new Graphic(pt, CustomRenderer.getInstance().PIPENETPO_POINT_CIRCLE_GREEN());
                sketchGraphicsLayer.addGraphic(graphic);
            } else if (index == points.size() - 1 && !midpointselected && !vertexselected) {
                Graphic graphic = new Graphic(pt, CustomRenderer.getInstance().PIPENETPO_POINT_CIRCLE_RED());
                sketchGraphicsLayer.addGraphic(graphic);
            } else {
                Graphic graphic = new Graphic(pt, CustomRenderer.getInstance().PIPENETPO_POINT_CIRCLE_BLUE());
                sketchGraphicsLayer.addGraphic(graphic);
            }
            index++;
        }
    }

    /***
     * 绘制多边形
     */
    private void drawPolyline() {
        if (sketchGraphicsLayer == null)
            return;

        if (points.size() <= 1)
            return;
        Graphic graphic;
        MultiPath multipath;
        if (editingmode == POLYLINE)
            multipath = new Polyline();
        else
            multipath = new Polygon();
        multipath.startPath(points.get(0));
        for (int i = 1; i < points.size(); i++) {
            multipath.lineTo(points.get(i));
        }

        Point lastPoint = points.get(points.size() - 1);
        if (editingmode == POLYLINE) {
            // 计算当前线段的长度
            double len = 0;
            try {
                len = multipath.calculateLength2D();
            } catch (Exception e) {
                len = 0;
            }
            String sLength = getLengthString(len);
            setResult(lastPoint, sLength, len);
        } else if (editingmode == POLYGON) {
            //计算当前面积
            double area = 0;
            try {
                area = multipath.calculateArea2D();
            } catch (Exception e) {
            }
            String sArea = getAreaString(area);
            setResult(lastPoint, sArea, area);
        }

        if (editingmode == POLYLINE) {
            graphic = new Graphic(multipath, CustomRenderer.getInstance().PIPENETPO_LINE_SOLID());
        } else {
            graphic = new Graphic(multipath, CustomRenderer.getInstance().PIPENETPO_POLYGON_FORMEASURE_FILL());
        }
        sketchGraphicsLayer.addGraphic(graphic);
    }

    public void clear() {
        if (sketchGraphicsLayer != null) {
            sketchGraphicsLayer.removeAll();
        }
        points.clear();
        mpoints.clear();
        midpointselected = false;
        vertexselected = false;
        insertingindex = 0;
        editingstates.clear();
    }

    /**
     * return index of point in array whose distance to touch point is minimum and less than 40. 
     * */
    int getSelectedIndex(double x, double y, ArrayList<Point> points1, MapView map) {

        if (points1 == null || points1.size() == 0)
            return -1;

        int index = -1;
        double distSQ_Small = Double.MAX_VALUE;
        for (int i = 0; i < points1.size(); i++) {
            Point p = map.toScreenPoint(points1.get(i));
            double diffx = p.getX() - x;
            double diffy = p.getY() - y;
            double distSQ = diffx * diffx + diffy * diffy;
            if (distSQ < distSQ_Small) {
                index = i;
                distSQ_Small = distSQ;
            }
        }
        if (distSQ_Small < (40 * 40)) {
            return index;
        }
        return -1;

    }

    private void setResult(Point point, String result, double value) {

        if (mapView == null || result == null || point == null) {
            return;
        }

        try {
            if (mapView.getCallout().isShowing()) {
                mapView.getCallout().hide();
            }
            mapView.getCallout().show(point, getCustomView(result));
        } catch (Exception e) {
        }
    }

    private View getCustomView(String value) {
        if (null == customView) {
            ViewGroup rootView = null;
            try {
                LayoutInflater layoutInflater = (LayoutInflater) fragment.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                customView = layoutInflater.inflate(R.layout.item_callout_locator, rootView);
            } catch (Exception e) {
            }
        }
        if (null != customView) {
            try {
                ((TextView) customView.findViewById(R.id.asset_locator_title)).setText(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return customView;
    }

    public void resetValues() {
        if (sketchGraphicsLayer != null) {
            sketchGraphicsLayer.removeAll();
        }
        clear();
        try {
            if (mapView.getCallout().isShowing()) {
                mapView.getCallout().hide();
            }
        } catch (Exception e) {
        }
    }

    //绘制文字
    @SuppressWarnings("unused")
    private void drawMeasureTextAtPoint(Point curPnt, String text) {
        TextSymbol textSymbol = new TextSymbol(16, text, Color.BLACK);
        textSymbol.setOffsetX(0);
        textSymbol.setOffsetY(-20);
        Graphic textGraphic = new Graphic(curPnt, textSymbol);
        sketchGraphicsLayer.addGraphic(textGraphic);
    }

    // 计算长度
    private String getLengthString(double dValue) {
        DecimalFormat df = new DecimalFormat("0.00");
        double length = Math.abs(dValue); // 距离其实不需要考虑正负情况
        String slength = "";
        // 顺时针绘制多边形，面积为正，逆时针绘制，则面积为负
        if (length >= 1000) {
            double dlength = length / 1000.0;
            slength = df.format(dlength) + " 千米";
        } else
            slength = df.format(length) + " 米";

        return slength;
    }

    // 计算面积
    private String getAreaString(double dValue) {
        DecimalFormat df = new DecimalFormat("0.00");
        double area = Math.abs(dValue);
        String sArea = "";
        // 顺时针绘制多边形，面积为正，逆时针绘制，则面积为负
        if (area >= 10000) {
            double dArea = area / 1000000.0;
            sArea = df.format(dArea) + " 平方千米";
        } else
            sArea = df.format(area) + " 平方米";

        return sArea;
    }
}
