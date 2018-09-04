package com.ecity.cswatersupply.ui.widght;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * A list view to use in a scroll view.
 * Refer to http://bbs.anzhuo.cn/thread-982250-1-1.html
 *
 * @author Ma Qianli
 *
 */
public class ScrollListView extends ListView {
    public ScrollListView(Context context) {
        super(context);
    }

    public ScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
