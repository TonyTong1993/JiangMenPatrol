package com.zzz.ecity.android.applibrary.adapter;

import java.util.List;

import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.R;
import com.zzz.ecity.android.applibrary.model.SimpleSelectModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

/***
 * 多选适配器
 * @author ZiZhengzhuan
 *
 */
public class MultipleSelectAdapter extends BaseSelectAdapter{
	private LayoutInflater mLayoutInflater;
	public MultipleSelectAdapter(Context pContext,
			List<SimpleSelectModel> dataSource) {
		super(pContext, dataSource);
		// TODO Auto-generated constructor stub
		mLayoutInflater = LayoutInflater.from(mContext);
	}
	
	
	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub

		ViewHolder viewHolder = null;
		final View view;
		SimpleSelectModel entity = dataSource.get(arg0);

		if (convertView==null) {
			view = mLayoutInflater.inflate(R.layout.item_simpleselect_multiple, null);
			setViewHolder(view);
		}
		else if (((ViewHolder)convertView.getTag()).needInflate) {
			view = mLayoutInflater.inflate(R.layout.item_simpleselect_multiple, null);
			setViewHolder(view);
		}
		else {
			view = convertView;
		}
		
		viewHolder = (ViewHolder)view.getTag();

		if(entity != null)
		{
			viewHolder.txt_name.setText(entity.getName());
			viewHolder.txt_describe.setText(entity.getDescribe());
			
			if(StringUtil.isEmpty(entity.getDescribe()))
			{
				viewHolder.txt_describe.setVisibility(View.GONE);
			}
			viewHolder.chk_selected.setChecked(entity.getSelected());
		}
		return view;
	
	}

	@Override
	public void clickAtPosition(int position) {
		// TODO Auto-generated method stub
		if(null == dataSource || position >= dataSource.size())
		{
			return;
		}
		boolean isChecked = dataSource.get(position).getSelected();
		dataSource.get(position).setSelected(!isChecked);
		notifyDataSetChanged();
	}
	
	private void setViewHolder(View view) {
		
		ViewHolder vh = new ViewHolder();
		vh.chk_selected=(CheckBox)view.findViewById(R.id.chk_selected);
		vh.txt_name=(TextView)view.findViewById(R.id.txt_name);
		vh.txt_describe=(TextView)view.findViewById(R.id.txt_describe);
		vh.needInflate = false;
		view.setTag(vh);
	}
		
		
	public static class ViewHolder
	{
		CheckBox chk_selected;
		TextView txt_name;
		TextView txt_describe;
		public boolean needInflate;
	}
}
