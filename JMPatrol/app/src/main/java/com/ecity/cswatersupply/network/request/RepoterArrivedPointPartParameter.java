package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.planningTask.ArrivePointInfo;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class RepoterArrivedPointPartParameter implements IRequestParameter {

    private List<ArrivePointInfo> points;

    public RepoterArrivedPointPartParameter(List<ArrivePointInfo> points) {
        this.points = points;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        JSONArray array = new JSONArray();
        for (int i = 0; i < points.size(); i++) {
            JSONObject json = new JSONObject();
            try {
                json.put("taskid", points.get(i).getTaskid());
                json.put("gid", points.get(i).getGid());
                json.put("state", points.get(i).getState());
                json.put("arrivedTime", points.get(i).getArriveTime());
                User currentUser = HostApplication.getApplication().getCurrentUser();
                json.put("userid", currentUser.getGid());
                json.put("tasktype", points.get(i).getTasktype());
                array.put(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        map.put("points", array.toString());
        return map;
    }

}
