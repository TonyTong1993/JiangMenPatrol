package com.ecity.cswatersupply.menu.map;

import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.map.SearchResultsAdapter;
import com.ecity.cswatersupply.ui.fragment.MapMainTabFragment;
import com.ecity.cswatersupply.utils.GraphicFlash;
import com.ecity.cswatersupply.utils.LayerTool;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;

import java.util.ArrayList;

public class IdentifyResultsController {
    private IMapOperationContext mMapFregment = null;
    private MapView mMapView = null;
    private ArrayList<Graphic> results = new ArrayList<Graphic>();
    private PopupWindow popupWindow;
    private PictureMarkerSymbol identifySymbol;
    private GraphicFlash graphicFlash = null;
    private ViewPager viewPager;
    private LayoutInflater inflater = null;
    private int locatorGid = -1;
    private int selectedPipeGid = -1;
    private GraphicsLayer graphicsLayer = null;
    private GraphicsLayer annimationLayer = null;
    private SearchResultsAdapter adapter;
    private boolean showActionButtons;

    public IdentifyResultsController(IMapOperationContext mapFragment, boolean showActionButtons) {
        this.mMapFregment = mapFragment;
        this.mMapView = mapFragment.getMapView();
        this.showActionButtons = showActionButtons;

        graphicsLayer = LayerTool.getGraphicsLayer(mMapView);
        annimationLayer = LayerTool.getAnimationLayer(mMapView);

        inflater = mapFragment.getContext().getLayoutInflater();
        View view = inflater.inflate(R.layout.view_searchresultviewpager, null);
        viewPager = (ViewPager) view.findViewById(R.id.results_viewpager);
        viewPager.setFocusableInTouchMode(true);
        viewPager.setFocusable(true);
        int height = showActionButtons ? R.dimen.pop_searchresult_h : R.dimen.pop_searchresult_h2;
        popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, mapFragment.getContext().getResources().getDimensionPixelSize(height));
    }

    public void showIdentifyResults(ArrayList<Graphic> graphics) {
        //每次展示所有结果前，复位两个两个标志图形的id
        if (null != viewPager) {
            clearAllResults();
        }

        results = graphics;
        locatorGid = -1;
        selectedPipeGid = -1;
        if (null == this.mMapFregment) {
            Toast.makeText(HostApplication.getApplication(), graphics.toString(), Toast.LENGTH_SHORT).show();
            return;
        }

        if (null == adapter) {
            adapter = new SearchResultsAdapter(results, mMapFregment, showActionButtons);
            viewPager.setAdapter(adapter);
            viewPager.setOnPageChangeListener(adapter);
        } else {
            viewPager.setOnPageChangeListener(null);
            adapter.updateData(results);
            viewPager.setOnPageChangeListener(adapter);
            viewPager.setCurrentItem(0);
        }
        viewPager.setOffscreenPageLimit(results.size());
        show();
    }

    private void show() {
        popupWindow.setWidth(getScreenDM().widthPixels - 25);
        popupWindow.setBackgroundDrawable(mMapFregment.getContext().getResources().getDrawable(R.drawable.maintoolbar_foot));
        View parent = getRootView();
        View achor = mMapFregment.getAchor();
        if(Build.VERSION.SDK_INT < 24) {
            popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 10);// 距离底部的位置
        } else {
            popupWindow.showAsDropDown(achor);
        }

        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);
        popupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                mMapFregment.cleanMapView();
            }
        });
        popupWindow.update();
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    private View getRootView() {
        return mMapFregment.getRootView();
    }

    private DisplayMetrics getScreenDM() {
        DisplayMetrics dm = new DisplayMetrics();
        mMapFregment.getContext().getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 此方法描述的是： 滑动到指定位置
     * 
     * @author: wangliu94@163.com
     * @version: 2015年5月15日 上午11:58:26
     */

    public void setCurrentPosition(int posisiton) {
        if (viewPager != null && viewPager.getAdapter().getCount() > posisiton) {
            viewPager.setCurrentItem(posisiton, true);
        }
    }

    public Graphic getCurrentItem() {
        if ((results != null) && (results.size() > viewPager.getCurrentItem())) {
            return results.get(viewPager.getCurrentItem());
        }

        return null;
    }

    /**
     * 此方法描述的是： 清除查询结果，包括管网查询和爆管分析
     * 
     * @author: wangliu94@163.com
     * @version: 2015年5月11日 下午3:21:51
     */

    public void clearAllResults() {
        try {
            viewPager.setOnPageChangeListener(null);
            this.results.clear();
            if (null != adapter) {
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
        }
        dismiss();
    }
}
