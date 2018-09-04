package com.ecity.cswatersupply.model;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.ResourceUtil;

/***
 * Created by MaoShouBei on 2017/5/26.
 */

public class PatrolBusInfo extends AModel {
    private static final long serialVersionUID = 1L;

    private String gid;
    private String userid;
    private String busno;
    private String username;
    private double lng;
    private double lat;
    private String deviceInfo;
    private String groupname;
    private boolean isShowFromSearch;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getBusno() {
        return busno;
    }

    public void setBusno(String busno) {
        this.busno = busno;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public boolean isShowFromSearch() {
        return isShowFromSearch;
    }

    public void setShowFromSearch(boolean showFromSearch) {
        isShowFromSearch = showFromSearch;
    }

    public String toString() {
        String stateStr = "";
        if ("1".equals(deviceInfo)) {
            stateStr = ResourceUtil.getStringById(R.string.user_online);
        } else {
            stateStr = ResourceUtil.getStringById(R.string.user_offline);
        }

        return ResourceUtil.getStringById(R.string.user_center_user_id) + "：" + userid + "\n" + ResourceUtil.getStringById(R.string.user_center_user_true_name) + "：" + username + "\n" + ResourceUtil.getStringById(R.string.patrol_bus_no_str) + "：" + busno + "\n" + ResourceUtil.getStringById(R.string.user_center_status) + "：" + stateStr + "\n" + ResourceUtil.getStringById(R.string.user_center_group_name) + "：" + groupname;
    }
}
