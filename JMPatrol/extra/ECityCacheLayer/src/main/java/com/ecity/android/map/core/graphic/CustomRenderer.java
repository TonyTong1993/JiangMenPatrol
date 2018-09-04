package com.ecity.android.map.core.graphic;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.esri.core.renderer.ClassBreak;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleLineSymbol.STYLE;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;

/**
 * @Description 自定义渲染<br>
 * 定义一些简单的颜色和填充符号，用于在量算和查询中直接使用
 * @author ZiZhengzhuan
 * @date 2013年10月17日
 * @version v1.0
 */
public class CustomRenderer {
    /*
     * Color Constants
     */
    int PIPENETPO_UICOLOR_FORFILL;
    int PIPENETPO_UICOLOR_FORFILLOUTLINE;
    int PIPENETPO_UICOLOR_FORLINE;
    int PIPENETPO_UICOLOR_FORPOINT;
    int PIPENETPO_UICOLOR_FORPOINT2;
    int PIPENETPO_UICOLOR_FORPOINTOUTLINE;
    int PIPENETPO_UICOLOR_FORTEXT;
    int PIPENETPO_UICOLOR_FORMEASURE_POINT;
    int PIPENETPO_UICOLOR_FORMEASURE_LINE_POINT;
    int PIPENETPO_UICOLOR_FORMEASURE_LINE_POLY;
    int PIPENETPO_UICOLOR_FORMEASURE_FILL_POINT;
    int PIPENETPO_UICOLOR_FORMEASURE_FILL_POLYGON;
    private static CustomRenderer instance;
    private Resources res;
    public static CustomRenderer getInstance() {
        if (instance == null)
            instance = new CustomRenderer();
        return instance;
    }


    /**
     * 
         * 创建一个新的实例 CustomRenderer.   
         *
     */
    public CustomRenderer() {
        instance = this;
        PIPENETPO_UICOLOR_FORFILL = Color.argb((int) (0.5 * 256), (int) (0.7 * 256), (int) (256 * 0.1), (int) (256 * 0.1));
        PIPENETPO_UICOLOR_FORFILLOUTLINE = Color.argb((int) (0.5 * 256), (int) (0.1 * 256), (int) (256 * 0.5), (int) (256 * 0.5));
        PIPENETPO_UICOLOR_FORLINE = Color.argb((int) (0.8 * 256), (int) (0.7 * 256), (int) (256 * 0.1), (int) (256 * 0.1));
        PIPENETPO_UICOLOR_FORPOINT = Color.argb((int) (0.7 * 256), (int) (0), (int) (256 * 0.7), (int) (256 * 0.1));
        PIPENETPO_UICOLOR_FORPOINT2 = Color.argb(0xff, 0xff, 0x00, 0x00);
        PIPENETPO_UICOLOR_FORPOINTOUTLINE = Color.argb((int) (0.7 * 256), (int) (0.0 * 256), (int) (256 * 0.3), (int) (256 * 0.5));
        PIPENETPO_UICOLOR_FORTEXT = Color.argb((int) (0.8 * 256), (int) (0.0 * 256), (int) (256 * 1.0), (int) (1.0 * 256));
        PIPENETPO_UICOLOR_FORMEASURE_POINT = Color.GREEN;
        PIPENETPO_UICOLOR_FORMEASURE_LINE_POINT = Color.argb((int) (0.8 * 256), (int) (0.0 * 256), (int) (256 * 0.0), (int) (255));
        PIPENETPO_UICOLOR_FORMEASURE_LINE_POLY = Color.argb((int) (0.8 * 256), (int) (0.8 * 256), (int) (256 * 0.0), (int) (256 * 0));
        PIPENETPO_UICOLOR_FORMEASURE_FILL_POINT = Color.WHITE;
        PIPENETPO_UICOLOR_FORMEASURE_FILL_POLYGON = Color.argb((int) (0.5 * 256), (int) (0.6 * 256), (int) (256 * 0.1), (int) (256 * 0.1));
    }

    /*==========================================================================
    点类：包括点的形状、颜色（来自颜色类）和大小 有单一标记型和图片型两种
    ==========================================================================*/
    /*单一标记---圆形 -红*/
    public SimpleMarkerSymbol PIPENETPO_POINT_CIRCLE_RED() {
        SimpleMarkerSymbol POINT_CIRCLE = new SimpleMarkerSymbol(Color.RED, 12, SimpleMarkerSymbol.STYLE.CIRCLE);
        POINT_CIRCLE.setOutline(new SimpleLineSymbol(Color.parseColor("#ffa500"), 2));
        return POINT_CIRCLE;
    }
    public SimpleMarkerSymbol PIPENETPO_POINT_CIRCLE_RED48() {
        SimpleMarkerSymbol POINT_CIRCLE = new SimpleMarkerSymbol(Color.rgb(255, 128, 0), 48, SimpleMarkerSymbol.STYLE.CIRCLE);
        POINT_CIRCLE.setOutline(new SimpleLineSymbol(Color.parseColor("#ffa500"), 8));
        return POINT_CIRCLE;
    }
    /*单一标记---圆形-绿*/
    public SimpleMarkerSymbol PIPENETPO_POINT_CIRCLE_GREEN() {
        SimpleMarkerSymbol POINT_CIRCLE = new SimpleMarkerSymbol(Color.GREEN, 12, SimpleMarkerSymbol.STYLE.CIRCLE);
        POINT_CIRCLE.setOutline(new SimpleLineSymbol(Color.parseColor("#ffa500"), 2));
        return POINT_CIRCLE;
    }

    /*单一标记---圆形-白色*/
    public SimpleMarkerSymbol PIPENETPO_POINT_CIRCLE_WIHTE() {
        SimpleMarkerSymbol POINT_CIRCLE = new SimpleMarkerSymbol(Color.WHITE, 12, SimpleMarkerSymbol.STYLE.CIRCLE);
        POINT_CIRCLE.setOutline(new SimpleLineSymbol(Color.parseColor("#ffa500"), 2));
        return POINT_CIRCLE;
    }

    /*单一标记---圆形 - 蓝*/
    public SimpleMarkerSymbol PIPENETPO_POINT_CIRCLE_BLUE() {
        SimpleMarkerSymbol POINT_CIRCLE = new SimpleMarkerSymbol(Color.BLUE, 15, SimpleMarkerSymbol.STYLE.CIRCLE);
        POINT_CIRCLE.setOutline(new SimpleLineSymbol(Color.parseColor("#ffa500"), 2));
        return POINT_CIRCLE;
    }

    /*==========================================================================
    线类：包括线的形状、颜色（来自颜色类）和宽度
    ==========================================================================*/

    public SimpleLineSymbol PIPENETPO_LINE_SOLID() {
        SimpleLineSymbol LINE_SOLID = new SimpleLineSymbol(PIPENETPO_UICOLOR_FORLINE, 3, STYLE.SOLID);
        return LINE_SOLID;
    }

    public SimpleLineSymbol PIPENETPO_LINE_SOLID_SELECTED() {
        SimpleLineSymbol LINE_SOLID = new SimpleLineSymbol(Color.GREEN, 3, STYLE.SOLID);
        return LINE_SOLID;
    }
    
    public SimpleLineSymbol PIPENETPO_LINE_SOLID_RED() {
        SimpleLineSymbol LINE_SOLID = new SimpleLineSymbol(Color.rgb(255, 128, 0), 20, STYLE.SOLID);
        return LINE_SOLID;
    }

    /*==========================================================================
    区类：包括区的内部颜色（来自颜色类）、外轮廓线（来自线类）
    ==========================================================================*/
    public SimpleFillSymbol PIPENETPO_POLYGON_FILL() {
        SimpleFillSymbol myFillSymbol = new SimpleFillSymbol(PIPENETPO_UICOLOR_FORFILL);
        //外轮廓
        SimpleLineSymbol myOutlineSymbol = new SimpleLineSymbol(PIPENETPO_UICOLOR_FORFILLOUTLINE, 3);
        myFillSymbol.setOutline(myOutlineSymbol);
        return myFillSymbol;
    }

    /*==========================================================================
     量算类：包括点线区
     ==========================================================================*/
    /*量算标记---菱形点*/
    public SimpleMarkerSymbol PIPENETPO_POINT_FORMEASURE_DIAMOND() {
        SimpleMarkerSymbol POINT_DIAMOND = new SimpleMarkerSymbol(PIPENETPO_UICOLOR_FORMEASURE_POINT, 10, SimpleMarkerSymbol.STYLE.DIAMOND);
        return POINT_DIAMOND;
    }

    /*量算标记---区域填充*/
    public SimpleFillSymbol PIPENETPO_POLYGON_FORMEASURE_FILL() {
        SimpleFillSymbol myFillSymbol = new SimpleFillSymbol(PIPENETPO_UICOLOR_FORMEASURE_FILL_POLYGON);
        //外轮廓
        SimpleLineSymbol myOutlineSymbol = new SimpleLineSymbol(PIPENETPO_UICOLOR_FORMEASURE_LINE_POLY, 3);
        myFillSymbol.setOutline(myOutlineSymbol);
        return myFillSymbol;
    }

    public SimpleFillSymbol PIPENETPO_POLYGON_RED_FILL() {
        SimpleFillSymbol myFillSymbol = new SimpleFillSymbol(Color.rgb(255, 128, 0));
        //外轮廓
        SimpleLineSymbol myOutlineSymbol = new SimpleLineSymbol(Color.GREEN, 3);
        myFillSymbol.setOutline(myOutlineSymbol);
        return myFillSymbol;
    }
    /*图片标记*/
    public PictureMarkerSymbol PIPENETPO_POINT_PICTURE(int index) {
        // 根据名字获取Drawable 对象
        //String pic = "/res/drawable-hdpi/icon_mark"+String.valueOf(index)+".png";
        /** 利用 getResource()函数返回图片的url 在传参数的时候有两种方式
                             一、不指定路径的时候是相对路径，在该类路径下、
                               二、参数指定路径即可搜索一定路径下的图片
        */
        //URL url = this.getClass().getResource(pic); // 如果返回的值为空 ，则会在新建pic时调到Catch
        //pic =url.toString();

        String name = "icon_mark" + String.valueOf(index);
        PictureMarkerSymbol POINT_PICTURE = new PictureMarkerSymbol(getDrawableByName(name));
        POINT_PICTURE.setOffsetY(16);
        return POINT_PICTURE;
    }

    private Drawable getDrawableByName(String name) {
        int id = -1;
        try {
            id = res.getIdentifier(name, "drawable","");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res.getDrawable(id);
    }

    //
    public Symbol[] generateSymbols() {
        SimpleMarkerSymbol[] symbols;
        SimpleLineSymbol simpleLineSymbol;
        SimpleFillSymbol simpleFillSymbol;
        SimpleFillSymbol simpleFillSymbol2;
        Integer[] colors = new Integer[] { Color.argb(0xff, 0xff, 0x00, 0x00),// 红
                Color.argb(0xff, 0xff, 0x7f, 0x00),// 橙
                Color.argb(0xff, 0xff, 0xff, 0x00),// 黄
                Color.argb(0xff, 0x00, 0xff, 0x00),// 绿
                Color.argb(0xff, 0x00, 0xff, 0xff),// 青
                Color.argb(0xff, 0x00, 0x00, 0xff),// 蓝
                Color.argb(0xff, 0xff, 0x00, 0xff) // 紫
        };

        symbols = new SimpleMarkerSymbol[7];

        for (int i = 0; i < colors.length; i++) {
            ClassBreak simpleMarkerClassBreak = new ClassBreak();
            simpleMarkerClassBreak.setClassMaxValue(i);
            SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(colors[i], 15, SimpleMarkerSymbol.STYLE.CIRCLE);
            simpleMarkerClassBreak.setSymbol(simpleMarkerSymbol);
            symbols[i] = simpleMarkerSymbol;
        }

        ClassBreak simpleLineClassBreak = new ClassBreak();

        simpleLineClassBreak.setClassMaxValue(8);

        simpleLineSymbol = new SimpleLineSymbol(Color.BLUE, 5);

        simpleLineClassBreak.setSymbol(simpleLineSymbol);
        ClassBreak simpleFillClassBreak = new ClassBreak();
        simpleFillClassBreak.setClassMaxValue(9);
        simpleFillSymbol = new SimpleFillSymbol(Color.BLACK);
        simpleFillSymbol2 = new SimpleFillSymbol(Color.CYAN);

        simpleFillSymbol.setAlpha(50);
        simpleFillSymbol2.setAlpha(50);
        simpleFillSymbol.setOutline(new SimpleLineSymbol(Color.BLACK, 2));
        simpleFillSymbol2.setOutline(new SimpleLineSymbol(Color.MAGENTA, 1));

        simpleFillClassBreak.setSymbol(simpleFillSymbol);
        simpleFillClassBreak.setSymbol(simpleFillSymbol2);

        Symbol[] symbolList = new Symbol[11];
        for (int i = 0; i < 7; i++) {
            symbolList[i] = symbols[i];
        }
        symbolList[7] = simpleLineSymbol;
        symbolList[8] = simpleFillSymbol;
        symbolList[9] = simpleFillSymbol2;

        SimpleFillSymbol simpleFillSymbolDetour = new SimpleFillSymbol(Color.GREEN);
        simpleFillSymbolDetour.setAlpha(50);

        SimpleLineSymbol simpleOutLineSymbolDetour = new SimpleLineSymbol(Color.parseColor("#ff4500"), 2);
        simpleOutLineSymbolDetour.setStyle(STYLE.DASHDOTDOT);
        simpleFillSymbolDetour.setOutline(simpleOutLineSymbolDetour);

        symbolList[10] = simpleFillSymbolDetour;

        return symbolList;
    }
}
