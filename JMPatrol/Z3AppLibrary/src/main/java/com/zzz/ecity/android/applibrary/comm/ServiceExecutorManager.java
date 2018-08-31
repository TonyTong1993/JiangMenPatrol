package com.zzz.ecity.android.applibrary.comm;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceExecutorManager {
	private static ExecutorService executorService;
	public static ExecutorService getServiceExecutor(){
		if(null == executorService){
			executorService = Executors.newCachedThreadPool();
		}
		return executorService;
	}
}
