package com.ecity.cswatersupply.model;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.google.gson.annotations.SerializedName;

public class User extends AModel {
    private static final long serialVersionUID = 9031729432214553367L;
    private String id;
    private String gid;
    private String trueName;
    private String groupId;
    private String groupName;
    private String groupCode;
    private String groupLev;
    private String ecode;
    private String role;
    private String roleCode;
    private String company;
    private String leader;
    private boolean isLeader;
    private String password;
    //是否在线 true在线；false 离线
    private String state;
    private String type;
    //巡检时间
    private String patroltime;
    //巡检速度
    private String speed;
    //巡检平均速度
    private String avrspeed;

    //是否已经签到
    @SerializedName("iswatch")
    private boolean isWatch;
    
    @SerializedName("canSign")
    private boolean canSign;
    
    @SerializedName("signOutTime")
    private String signOutTime;

    public String getSignOutTime() {
        return signOutTime;
    }

    public void setSignOutTime(String signOutTime) {
        this.signOutTime = signOutTime;
    }

    @SerializedName("username")
    private String loginName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupLev() {
        return groupLev;
    }

    public void setGroupLev(String groupLev) {
        this.groupLev = groupLev;
    }

    public String getEcode() {
        return ecode;
    }

    public void setEcode(String ecode) {
        this.ecode = ecode;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public void setLeader(boolean isLeader) {
        this.isLeader = isLeader;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isWatch() {
        return isWatch;
    }

    public void setWatch(boolean isWatch) {
        this.isWatch = isWatch;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPatroltime() {
        return patroltime;
    }

    public void setPatroltime(String patroltime) {
        this.patroltime = patroltime;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getAvrspeed() {
        return avrspeed;
    }

    public void setAvrspeed(String avrspeed) {
        this.avrspeed = avrspeed;
    }

    public String toString() {
        String stateStr = "";
        if ("1".equals(state)) {
            stateStr = ResourceUtil.getStringById(R.string.user_online);
        } else {
            stateStr = ResourceUtil.getStringById(R.string.user_offline);
        }

        return ResourceUtil.getStringById(R.string.user_center_user_id) + "：" + id + "\n" + ResourceUtil.getStringById(R.string.user_center_user_true_name) + "：" + trueName + "\n"
                + ResourceUtil.getStringById(R.string.user_center_status) + "：" + stateStr + "\n" + ResourceUtil.getStringById(R.string.user_center_group_name) + "：" + groupName;
    }

    public boolean isCanSign() {
        return canSign;
    }

    public void setCanSign(boolean canSign) {
        this.canSign = canSign;
    }
}
