package com.ecity.android.map.core.dbquery.task;

import java.util.concurrent.ExecutionException;

public abstract class DBQueryFutureCallback {
	private DBQueryFutureTask futureTask;
	public DBQueryFutureCallback(DBQueryFutureTask futureTask) {
		this.futureTask = futureTask;
	}

	public DBQueryFutureTask getFutureTask() {
		return futureTask;
	}

	public void setFutureTask(DBQueryFutureTask futureTask) {
		this.futureTask = futureTask;
	}

	public Object getReturnedObject() {
		try {
			return this.futureTask.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public abstract void call();

}
