package com.zzz.ecity.android.applibrary.model;

import java.io.Serializable;

public class SimpleSelectModel implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name = "";
	private String describe = "";
	private String value = "";
	
	private boolean isSelected = false;
	public SimpleSelectModel()
	{
		this.name = "";
		this.describe = "";
		this.isSelected = false;
	}
	public SimpleSelectModel(String name, String describe, boolean isSelected)
	{
		this.name = name;
		this.describe = describe;
		this.isSelected = isSelected;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name )
	{
		this.name = name;
	}
	
	public String getDescribe()
	{
		return describe;
	}
	public void setDescribe(String describe )
	{
		this.describe = describe;
	}
	public boolean getSelected()
	{
		return isSelected;
	}
	public void setSelected(boolean isSelected )
	{
		this.isSelected = isSelected;
	}

	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
