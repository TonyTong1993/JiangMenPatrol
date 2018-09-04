package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.z3app.android.util.StringUtil;

public class PlanningTaskParameter implements IRequestParameter {

	public User user;
	public String taskType;
	
    public PlanningTaskParameter(User user,String taskType) {
    	this.user = user;
    	this.taskType = taskType;
	}
	
	@Override
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
	    map.put("userid", user.getId());
	    if (!StringUtil.isBlank(taskType)) {
	        map.put("type", taskType);
        }
		return map;
	}
     
}
