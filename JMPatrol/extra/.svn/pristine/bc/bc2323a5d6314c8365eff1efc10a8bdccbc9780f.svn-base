package com.zzz.ecity.android.applibrary.service;

import android.location.Location;

public abstract class ALoationProducer {
	private int num = 100;
	private int interval = 1; //秒

	/**
	 * 生成总数
	 * 即:一共会调用 num 次generate方法
	 * @return
	 */
	public int getNum() {
		return num;
	}

	/**
	 * 生成总数 
	 * 即:一共会调用 num 次generate方法
	 * @param num
	 */
	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * 生成间隔 秒
	 * 即:每interval秒会调用一次generate方法
	 * @return
	 */
	public int getInterval() {
		return interval;
	}

	/**
	 * 生成间隔 秒
	 * 即:每interval秒会调用一次generate方法
	 * @param interval
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}


	public abstract Location generate(int index);
}
