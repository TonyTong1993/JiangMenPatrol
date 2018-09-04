package com.ecity.android.map.core.dbquery.task;

import com.ecity.android.map.core.tile.TileCallable;

import java.util.concurrent.FutureTask;

/***
 * 瓦片请求并发任务
 * @author ZiZhengzhuan
 *
 */
public class DBQueryFutureTask extends FutureTask<Object> {

	private DBQueryFutureCallback callback;
	private int level;
	private int col;
	private int row;

	public DBQueryFutureTask(DBQueryCallable callable) {
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

	public DBQueryFutureCallback getCallback() {
		return callback;
	}

	public void setCallback(DBQueryFutureCallback callback) {
		this.callback = callback;
	}

	@Override
	protected void done() {
		if (callback != null) {
			callback.call();
		}
	}
}
