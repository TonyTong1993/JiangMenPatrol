package com.zzz.ecity.android.applibrary.comm;

import java.util.concurrent.FutureTask;
/***
 *
 */
public class BaseFutureTask extends FutureTask<Object> {
	private BaseFutureCallback callback;
	public BaseFutureTask(BaseCallable callable) {
		super(callable);
	}
	public BaseFutureCallback getCallback() {
		return callback;
	}

	public void setCallback(BaseFutureCallback callback) {
		this.callback = callback;
	}

	@Override
	protected void done() {
		if (callback != null) {
			callback.call();
		}
	}
}
