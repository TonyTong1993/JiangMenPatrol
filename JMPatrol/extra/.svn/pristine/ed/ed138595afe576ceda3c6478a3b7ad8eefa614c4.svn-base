package com.ecity.android.map.core.tile;

import java.util.concurrent.ExecutionException;

public abstract class TileFutureCallback {
	private TileFutureTask futureTask;
	public TileFutureCallback(TileFutureTask futureTask) {
		this.futureTask = futureTask;
	}

	public TileFutureTask getFutureTask() {
		return futureTask;
	}

	public void setFutureTask(TileFutureTask futureTask) {
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
