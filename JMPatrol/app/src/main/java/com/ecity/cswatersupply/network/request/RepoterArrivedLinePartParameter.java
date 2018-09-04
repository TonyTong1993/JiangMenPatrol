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
import com.ecity.cswatersupply.model.planningTask.ArriveLineInfo;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class RepoterArrivedLinePartParameter implements IRequestParameter {

    private List<ArriveLineInfo> lines;

    public RepoterArrivedLinePartParameter(List<ArriveLineInfo> lines) {
        this.lines = lines;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        JSONArray array = new JSONArray();
        for (int i = 0; i < lines.size(); i++) {
            JSONObject json = new JSONObject();
            try {
                json.put("taskid", lines.get(i).getTaskid());
                json.put("gid", lines.get(i).getGid());
                json.put("state", lines.get(i).getState());
                json.put("arrivelen", lines.get(i).getArriveLength());
                json.put("arrivedTime", lines.get(i).getArriveTime());
                json.put("tasktype", lines.get(i).getTasktype());
                json.put("geotype", lines.get(i).getGeoType());
                User currentUser = HostApplication.getApplication().getCurrentUser();
                json.put("userid", currentUser.getGid());
                array.put(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        map.put("points", array.toString());
        return map;
    }

}
