package com.ecity.android.map.core;

import java.util.concurrent.Callable;
/***
 * 瓦片请求过程
 * @author ZiZhengzhuan
 *
 */
public class TileCallable implements Callable<Object> {
	private int level;
	private int col;
	private int row;
	private String url;
	private TimeInfo timeinfo;
	
	public TileCallable(int level, int col, int row,String url,TimeInfo timeinfo) {
		super();
		this.url = url;
		this.level = level;
		this.col = col;
		this.row = row;
		this.timeinfo = timeinfo;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public TimeInfo getTimeinfo() {
		return timeinfo;
	}

	public void setTimeinfo(TimeInfo timeinfo) {
		this.timeinfo = timeinfo;
	}

	@Override
	public Object call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
