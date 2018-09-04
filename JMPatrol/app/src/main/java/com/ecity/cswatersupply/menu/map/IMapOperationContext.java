package com.ecity.cswatersupply.menu.map;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.ecity.cswatersupply.model.QueryResultShowModel;
import com.ecity.cswatersupply.ui.widght.MapOperatorTipsView;
import com.esri.android.map.MapView;

/**
 * 地图操作的接口。用于MapMainTabFragment和MapActivity实现。
 * @author jonathanma
 *
 */
public interface IMapOperationContext {

    MapOperatorTipsView getmMapOperatorTipsView();

    /**
     * 显示拉框查询的查询结果。
     * @param queryResult 查询结果
     * @param showActionButtons 是否显示导航和事件上报菜单
     */
    void showSerachResult(QueryResultShowModel queryResult, boolean showActionButtons);

    Activity getContext();

    Intent getUserIntent();

    void cleanMapView();

    MapView getMapView();

    View getRootView();

    View getAchor();

    void finish();

    View getmTitleView();
    View getmRlMapZoomView();
    View getMapLocationLayout();
    void setTargetLonLat(double[] LonLat);
    OnClickListener getNaviClickListener();
}
