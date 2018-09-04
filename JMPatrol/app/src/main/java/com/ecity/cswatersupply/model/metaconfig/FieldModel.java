package com.ecity.cswatersupply.model.metaconfig;

import java.io.Serializable;

public class FieldModel implements Serializable {
	//字段名
	private String name;
	//字段类型
	private String type;
	//字段别名
	private String alias;
	//字段长度
	private int length;
	//默认值
	private String defaultvalue;
	//可选值
	private String SelValue;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getDefaultvalue() {
		return defaultvalue;
	}

	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}

	public String getSelValue() {
		return SelValue;
	}

	public void setSelValue(String SelValue) {
		this.SelValue = SelValue;
	}

	@Override
	public String toString() {
		return alias;
	}
}
