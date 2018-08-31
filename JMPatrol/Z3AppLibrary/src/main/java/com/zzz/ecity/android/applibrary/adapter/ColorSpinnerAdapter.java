package com.zzz.ecity.android.applibrary.adapter;
import java.util.ArrayList;
import java.util.List;

import com.zzz.ecity.android.applibrary.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ColorSpinnerAdapter extends BaseAdapter {
	LayoutInflater inflater;
	Context context;
	ArrayList<Integer> list;

	public ColorSpinnerAdapter(Context context, ArrayList<Integer> list) {
		super();
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_spinner_dropdown_items, null);
			viewHolder = new ViewHolder();
			viewHolder.layout = (RelativeLayout) convertView.findViewById(R.id.spinner_dropdown_layout);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.spinner_dropdown_txt);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(list.size() == position+1){
			viewHolder.layout.setBackgroundResource(R.drawable.css_default_input_edit);
		}else{
			viewHolder.layout.setBackgroundResource(R.drawable.css_default_input_edit);
		}
		viewHolder.textView.setText("");
		viewHolder.textView.setBackgroundColor(list.get(position));
		return convertView;
	}

	public class ViewHolder {
		RelativeLayout layout;
		TextView textView;
	}

	public void refresh(List<Integer> l) {
		this.list.clear();
		list.addAll(l);
		notifyDataSetChanged();
	}

	public void add(Integer coll) {
		list.add(coll);
		notifyDataSetChanged();
	}

	public void add(ArrayList<Integer> coll) {
		list.addAll(coll);
		notifyDataSetChanged();

	}
}
