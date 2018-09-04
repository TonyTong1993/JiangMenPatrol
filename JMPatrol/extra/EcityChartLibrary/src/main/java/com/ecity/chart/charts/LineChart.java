
package com.ecity.chart.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.ecity.chart.data.LineData;
import com.ecity.chart.interfaces.LineDataProvider;
import com.ecity.chart.renderer.LineChartRenderer;
import com.ecity.chart.utils.FillFormatter;

/**
 * Chart that draws lines, surfaces, circles, ...
 * 
 * @author Philipp Jahoda
 */
public class LineChart extends BarLineChartBase<LineData> implements LineDataProvider {

    public LineChart(Context context) {
        super(context);
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new LineChartRenderer(this, mAnimator, mViewPortHandler);
    }

    @Override
    protected void calcMinMax() {
        super.calcMinMax();

        if (mDeltaX == 0 && mData.getYValCount() > 0)
            mDeltaX = 1;
    }
    
    @Override
    public LineData getLineData() {
        return mData;
    }
}
