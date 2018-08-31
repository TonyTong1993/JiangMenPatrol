package com.zzz.ecity.android.applibrary.model;

import java.io.Serializable;
public class BaseAppMenu implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name; 				
	private String subname; 			
	private boolean deleteable; 		
	private boolean dragable; 
	private String iconName;
	private EPlacement placement;		
	private AMenuCommand comand = null;
	private boolean visibility = true ;
	
	public enum EPlacement{
		MENU_LEFT,MENU_RIGHT,MENU_CENTER,;
	}
	
	public BaseAppMenu() {
		this(null);
	}
	
	public BaseAppMenu(AMenuCommand comand) {
		deleteable = true;
		dragable = true;
		placement = EPlacement.MENU_CENTER;
		this.comand = comand;
	}
	
	public void setAMenuComand(AMenuCommand comand)
	{
		this.comand = comand;
	}
	public AMenuCommand getAMenuComand()
	{
		return comand;
	}
	
	public  boolean execute()
	{
		if(null != comand) {
            return comand.execute();
        }
		return false;
	}

	public int getMenuIconResourceId()
	{
		if(null != comand) {
            return comand.getMenuIconResourceId(iconName);
        }
		
		return -1;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getSubName() {
		return subname;
	}

	public void setSubName(String subname) {
		this.subname = subname;
	}
	public boolean isDeleteable() {
		return deleteable;
	}

	public void setDeleteable(boolean deleteable) {
		this.deleteable = deleteable;
	}

	public boolean isDragable() {
		return dragable;
	}

	public void setDragable(boolean dragable) {
		this.dragable = dragable;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public EPlacement getPlacement()
	{
		return placement;
	}
	public void setPlacement(EPlacement placement)
	{
		this.placement = placement;
	}
	
	public void setVisibility(boolean visibility)
	{
		this.visibility = visibility;
	}
	
	public boolean isVisibility()
	{
		return visibility;
	}
	
	@Override
	public String toString() {
		try {
			String str = "";
			str += name;
			str += ";";
			str += subname;
			str += ";";
			str += deleteable;
			str += ";";
			str += dragable;
			str += ";";
			str += iconName;
			str += ";";
			str += visibility;
			str += ";";
					
			return str;
		} catch (Exception e) {
			return "";
		}
	}
}