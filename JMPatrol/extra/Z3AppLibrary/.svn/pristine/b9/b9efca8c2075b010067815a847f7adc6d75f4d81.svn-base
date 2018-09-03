package com.zzz.ecity.android.applibrary.comm;

import java.util.concurrent.ExecutionException;

public abstract class BaseFutureCallback {
	private BaseFutureTask futureTask;
	public BaseFutureCallback(BaseFutureTask futureTask) {
		this.futureTask = futureTask;
	}

	public BaseFutureTask getFutureTask() {
		return futureTask;
	}

	public void setFutureTask(BaseFutureTask futureTask) {
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
