 
 package com.ecity.cswatersupply.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;



public class MyDialogListAdapter extends ArrayListAdapter<String> {
	private LayoutInflater mInflater;


	public MyDialogListAdapter(Context context) {
		super(context);
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		if (null != convertView) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = mInflater.inflate(R.layout.view_text_row, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		String value = getList().get(position);
		holder.tv_name.setText(value);
		return convertView;
	}

	static class ViewHolder {
		private TextView tv_name;


		public ViewHolder(View view) {
			super();
			tv_name = (TextView) view.findViewById(R.id.tv_name);

		}

	}

}
