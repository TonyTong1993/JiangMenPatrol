package com.zzz.ecity.android.applibrary.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzz.ecity.android.applibrary.R;
import com.zzz.ecity.android.applibrary.model.BaseAppMenu;

public class MenuAdapter extends BaseAdapter {
	private Context mContext;
	private List<BaseAppMenu> mLists;
	private LayoutInflater mLayoutInflater;

	public MenuAdapter(Context pContext, List<BaseAppMenu> pLists) {
		this.mContext = pContext;
		this.mLists = pLists;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	@Override
    public int getCount() {

		return mLists != null ? mLists.size() : 0;
	}

	@Override
    public Object getItem(int arg0) {

		return mLists.get(arg0);
	}

	@Override
    public long getItemId(int arg0) {
		return arg0;
	}

	@Override
    public View getView(int arg0, View view, ViewGroup arg2) {
		Holder holder = null;
		if (null == view) {
			holder = new Holder();
			view = mLayoutInflater.inflate(R.layout.item_menu, null);
			holder.img_icon = (ImageView) view
					.findViewById(R.id.img_menu_func_icon);

			holder.txt_name = (TextView) view
					.findViewById(R.id.txt_menu_func_name);
			holder.txt_subname = (TextView) view
					.findViewById(R.id.txt_menu_func_subname);

			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		holder.img_icon.setImageResource(mLists.get(arg0)
				.getMenuIconResourceId());
		holder.txt_name.setText(mLists.get(arg0).getName());
		holder.txt_subname.setText(mLists.get(arg0).getSubName());
		holder.txt_subname.setVisibility(View.GONE);
		return view;
	}

	private static class Holder {
		ImageView img_icon;
		TextView txt_name;
		TextView txt_subname;
	}
}
