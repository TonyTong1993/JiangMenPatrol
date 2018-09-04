package com.ecity.cswatersupply.adapter.map;

import java.util.List;

import android.content.Context;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.map.SearchResult;


/**
 * 所在位置适配器
 * 
 * @author yxx
 * 
 */
public class PoiSearchAdapter extends APoiSearchAdapter<SearchResult> {
  
	public PoiSearchAdapter(Context context, List<SearchResult> appGroup) {
        super(context, appGroup, R.layout.activity_poi_search_item);
    }
	
    @Override
    public void convert(ViewHolder holder, SearchResult result) {        
        TextView mpoi_name = holder.getView(R.id.mpoiNameT);
        mpoi_name.setText(result.titleName);
        TextView mpoi_address = holder.getView(R.id.mpoiAddressT);
        mpoi_address.setText(result.address);    
    }
}
