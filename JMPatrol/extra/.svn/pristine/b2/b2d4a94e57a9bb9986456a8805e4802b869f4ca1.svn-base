package com.viewpagerindicator;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TabView extends LinearLayout {
    private TabPageIndicator mParent;
    private int mIndex;
    private int iconId;

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(TabPageIndicator parent, String text, int index) {
        init(parent, text, index, iconId);
    }

    public void init(TabPageIndicator parent, String text, int index, int iconId) {
        mParent = parent;
        mIndex = index;
        
        TextView textView = (TextView) findViewById(android.R.id.text1);
        textView.setTextSize(12);
        
        textView.setText(text);

        if (iconId > 0) {
            textView.setCompoundDrawablesWithIntrinsicBounds(0, iconId, 0, 0);
        } 
    }

    
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Re-measure if we went beyond our maximum size.
        if (mParent.mMaxTabWidth > 0 && getMeasuredWidth() > mParent.mMaxTabWidth) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(mParent.mMaxTabWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
        }
    }

    public int getIndex() {
        return mIndex;
    }
}
