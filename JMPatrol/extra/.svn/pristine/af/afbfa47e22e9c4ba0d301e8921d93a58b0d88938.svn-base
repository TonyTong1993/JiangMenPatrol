package com.ecity.chart.interfaces;

import com.ecity.chart.components.YAxis.AxisDependency;
import com.ecity.chart.data.BarLineScatterCandleBubbleData;
import com.ecity.chart.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    int getMaxVisibleCount();
    boolean isInverted(AxisDependency axis);
    
    int getLowestVisibleXIndex();
    int getHighestVisibleXIndex();

    BarLineScatterCandleBubbleData getData();
}
