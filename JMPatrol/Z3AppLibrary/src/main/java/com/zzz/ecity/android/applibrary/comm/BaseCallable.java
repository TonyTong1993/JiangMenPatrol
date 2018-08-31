package com.zzz.ecity.android.applibrary.comm;

import java.util.concurrent.Callable;
/***
 * 
 */
public class BaseCallable implements Callable<Object> {
	private String name;
	private int id;

	public BaseCallable(String name, int id) {
		super();
		this.name = name;
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	@Override
	public Object call() throws Exception {
		
		return null;
	}

}
