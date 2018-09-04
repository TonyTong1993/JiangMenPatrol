package com.ecity.cswatersupply.adapter.map;

import java.util.List;

import android.content.Context;
import android.widget.TextView;

import com.ecity.cswatersupply.R;


/**
 * 所在位置适配器
 * 
 * @author yxx
 * 
 */
public class HistorySearchAdapter extends APoiSearchAdapter<String> {
  
	public HistorySearchAdapter(Context context, List<String> appGroup) {
        super(context, appGroup, R.layout.activity_poi_search_history_item);
    }
	
    @Override
    public void convert(ViewHolder holder, String result) {        
        TextView addressname = holder.getView(R.id.addressname);
        addressname.setText(result);         
    }
}
