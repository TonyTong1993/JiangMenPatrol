package com.zzz.ecity.android.applibrary.task;

import android.location.Location;

import com.zzz.ecity.android.applibrary.comm.BaseCallable;
import com.zzz.ecity.android.applibrary.service.ALoationProducer;

public class LocationSimulateCallable extends BaseCallable {
	private ALoationProducer producer;
	public LocationSimulateCallable(String name, int id) {
		this(name, id,null);
	}
	
	public LocationSimulateCallable(String name, int id,ALoationProducer producer) {
		super(name, id);
		this.producer= producer;
	}
	
	@Override
	public Location call() throws Exception {
		if(null != producer){
			return producer.generate(getId());
		}
		
		return null;
	}

}
