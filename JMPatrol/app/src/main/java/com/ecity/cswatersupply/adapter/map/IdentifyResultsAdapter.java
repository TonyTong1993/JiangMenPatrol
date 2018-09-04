package com.ecity.cswatersupply.adapter.map;

import java.util.ArrayList;
import java.util.Map;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.menu.map.IMapOperationContext;
import com.esri.android.map.MapView;
import com.esri.core.map.Graphic;

public class IdentifyResultsAdapter extends PagerAdapter {
    private ArrayList<Graphic> mResults;
    private IMapOperationContext mMapFragment;
    private MapView mMapView;
    private LayoutInflater mInflater;

    public IdentifyResultsAdapter(ArrayList<Graphic> searchResults, IMapOperationContext fragment, MapView mMapView) {
        this.mResults = searchResults;
        this.mMapFragment = fragment;
        this.mMapView = mMapView;
        this.mInflater = LayoutInflater.from(mMapFragment.getContext());
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Graphic graphic = mResults.get(position);
        Map<String, Object> attributes = graphic.getAttributes();

        View view = mInflater.inflate(R.layout.identify_results_item, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv1 = (TextView) view.findViewById(R.id.tv_1);
        TextView tv2 = (TextView) view.findViewById(R.id.tv_2);
        TextView tvDetail = (TextView) view.findViewById(R.id.tv_detail);
        tv_title.setText(String.valueOf(attributes.get("layerId")));
        tv1.setText(String.valueOf(attributes.get("x")));
        tv2.setText(String.valueOf(attributes.get("y")));

        tvDetail.setOnClickListener(new MyTvOnClickListener());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof View) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0.equals(arg1);
    }

    private class MyTvOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
        }
    }

}
