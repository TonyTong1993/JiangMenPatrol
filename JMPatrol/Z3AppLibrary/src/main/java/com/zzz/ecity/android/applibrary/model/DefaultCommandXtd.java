package com.zzz.ecity.android.applibrary.model;

import com.zzz.ecity.android.applibrary.R;

public class DefaultCommandXtd extends AMenuCommand {

	@Override
	public int getMenuIconResourceId(String iconName) {
		return R.drawable.ic_launcher;
	}

	@Override
	public boolean execute() {
		return true;
	}

	@Override
	public AMenuCommand createInstance() {
		return new DefaultCommandXtd();
	}

	@Override
	public void copyTo(AMenuCommand comand) {
		
	}
}
