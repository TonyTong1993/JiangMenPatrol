package com.ecity.cswatersupply.ui.widght;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * With ScrollablityConfigurableViewPager, user can configure whether a ViewPager is scrollable or not.
 * @author jonathanma
 *
 */
public class ScrollablityConfigurableViewPager extends ViewPager {
    private boolean isScrollable;

    public ScrollablityConfigurableViewPager(Context context) {
        super(context);
    }

    public ScrollablityConfigurableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return isScrollable && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return isScrollable && super.onInterceptTouchEvent(event);
    }

    public boolean isScrollable() {
        return isScrollable;
    }

    public void setScrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }
}
