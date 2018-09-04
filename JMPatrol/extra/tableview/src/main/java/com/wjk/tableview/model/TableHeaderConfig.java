package com.wjk.tableview.model;

import android.graphics.Typeface;
import android.view.Gravity;

/**
 * Created by zhengzhuanzi on 2017/8/16.
 */

public class TableHeaderConfig {
    //是否显示标题
    public boolean showHeader = true;
    //背景颜色
    public int titleBackgroundColor = 0x00ffffff;
    //左边距
    public int paddingLeft = 20;
    //上边距
    public int paddingTop = 30;
    //下边距
    public int paddingRight = 20;
    //右边距
    public int paddingBottom = 30;
    //文字大小
    public int textSize = 18;
    //文字类型
    public int typeface = Typeface.BOLD;
    //文字颜色
    public int textColor = 0x99ffffff;
    //边框颜色
    public int borderColor = 0x99000000;
    //
    public int srokeWidth = 1;
    //
    public int gravity = Gravity.CENTER;

}
