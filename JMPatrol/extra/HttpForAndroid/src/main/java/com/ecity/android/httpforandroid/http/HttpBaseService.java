package com.ecity.android.httpforandroid.http;

import java.io.InterruptedIOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public abstract class HttpBaseService<T> implements Callable<T> {
	HttpBaseServiceListener<T> taskListener;
	short completeFlg = 1;
	protected HttpBaseServiceParameters actionInput;

	protected HttpBaseService(HttpBaseServiceParameters paramTaskParameters) {
		this(paramTaskParameters, null);
	}

	protected HttpBaseService(HttpBaseServiceParameters paramTaskParameters,
			HttpBaseServiceListener<T> paramTaskListener) {
		if (paramTaskListener == null) {
			this.taskListener = new HttpBaseServiceListener<T>() {
				@Override
                public void onCompletion(short s, T objs) {
				}

				@Override
                public void onError(Throwable e) {
					e.printStackTrace();
				}
			};
		} else {
			this.taskListener = paramTaskListener;
		}

		this.actionInput = paramTaskParameters;
	}

	public HttpBaseServiceParameters getActionInput() {
		return this.actionInput;
	}

	public void setActionInput(HttpBaseServiceParameters taskInput) {
		this.actionInput = taskInput;
	}

	protected abstract T execute() throws Exception;

	@Override
    public final T call() {
		T localObject1 = null;
		try {
			localObject1 = execute();
		} catch (ExecutionException localExecutionException) {
			this.completeFlg = 0;
			this.taskListener.onError(localExecutionException);
		} catch (InterruptedIOException localInterruptedIOException) {
			this.completeFlg = 0;
			this.taskListener.onError(localInterruptedIOException);
		} catch (Throwable localThrowable) {
			if (Thread.currentThread().isInterrupted()) {
				this.completeFlg = 0;
			} else {
				this.completeFlg = -1;
				this.taskListener.onError(localThrowable);
			}
		} finally {
			this.taskListener.onCompletion(this.completeFlg, localObject1);
		}
		return localObject1;
	}
}
