package com.zzz.ecity.android.applibrary.model;
public abstract class AMenuCommand {
	public abstract int getMenuIconResourceId(String iconName);
	public abstract boolean execute();
	public abstract AMenuCommand createInstance();
	public abstract void copyTo(AMenuCommand comand);
	public AMenuCommand copy() {
		AMenuCommand comand = createInstance();
		copyTo(comand);
		return comand;
	}
	static AMenuCommand copy(AMenuCommand comand) {
		AMenuCommand newComand = comand.createInstance();
		comand.copyTo(newComand);
		return newComand;
	}
}
