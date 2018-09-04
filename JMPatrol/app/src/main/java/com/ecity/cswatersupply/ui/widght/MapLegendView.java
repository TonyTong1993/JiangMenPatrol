package com.ecity.cswatersupply.ui.widght;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.planningtask.PointPartLegendListAdapter;
import com.ecity.cswatersupply.model.planningTask.PointPartLegendInfo;

public class MapLegendView extends RelativeLayout {

    private ListView Legend_list;
    private PointPartLegendListAdapter mAdapter;
    private Context mContext;
    
	public MapLegendView(Context context) {
		this(context, null);
	}

	public MapLegendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_map_legend_list_content, this, true);
        this.mContext = context;
        initView();
        initData(null);
    }

	public void initData(List<PointPartLegendInfo> infos) {
	    mAdapter = new PointPartLegendListAdapter(mContext);
	    mAdapter.setList(infos);
	    Legend_list.setAdapter(mAdapter);
	    mAdapter.notifyDataSetChanged();
    }
	
	public void updataListView(List<PointPartLegendInfo> infos){
	    mAdapter.setList(infos);
	    mAdapter.notifyDataSetChanged();
	}
	

    private void initView() {
	    Legend_list = (ListView) findViewById(R.id.Legend_list);
	}

}
