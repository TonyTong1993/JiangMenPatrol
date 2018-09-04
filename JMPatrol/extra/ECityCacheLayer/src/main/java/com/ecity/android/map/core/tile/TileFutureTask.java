package com.ecity.android.map.core.tile;

import java.util.concurrent.FutureTask;
/***
 * 瓦片请求并发任务
 * @author ZiZhengzhuan
 *
 */
public class TileFutureTask extends FutureTask<Object> {

	private TileFutureCallback callback;
	private int level;
	private int col;
	private int row;
	
	public TileFutureTask(TileCallable callable) {
		super(callable);
		if(null != callable){
			this.level = callable.getLevel();
			this.col = callable.getCol();
			this.row = callable.getRow();
		}
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getCol() {
		return col;
	}

	public int getRow() {
		return row;
	}

	public TileFutureCallback getCallback() {
		return callback;
	}

	public void setCallback(TileFutureCallback callback) {
		this.callback = callback;
	}

	@Override
	protected void done() {
		if (callback != null) {
			callback.call();
		}
	}
}
