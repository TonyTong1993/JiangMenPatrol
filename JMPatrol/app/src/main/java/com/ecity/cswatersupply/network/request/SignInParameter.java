package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

/*签到参数拼接*/
public class SignInParameter implements IRequestParameter {
    private User user;
    private String endTime;
    private String startWatchtime;

    public SignInParameter(User user, String endTime, String startWatchtime) {
        this.user = user;
        this.endTime = endTime;
        this.startWatchtime = startWatchtime;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", user.getGid());
        map.put("endwatchtime", endTime);
        map.put("startwatchtime", startWatchtime);
        return map;
    }

}
