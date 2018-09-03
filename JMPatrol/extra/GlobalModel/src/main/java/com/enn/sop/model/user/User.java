package com.enn.sop.model.user;

import com.enn.sop.global.StationInfoModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供用户通用信息。功能相关的信息，需实现{@link IUserInfo}接口，并调用{@link #putUserInfo}方法存储到用户对象中。获取时，调用{@link #getUserInfo}。
 *
 * @author jonathanma
 * @date 2017-10-25
 */
public class User implements Serializable {
    private static final long serialVersionUID = 9031729432214553367L;
    private String phone;
    private String id;
    private String email;
    private String type;
    private String gid;
    private String username;
    private String loginName;
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
    private String password;
    private String realtimeLocateInterval = "5";
    private String locGid;
    private String jiaobansj;
    private String stationName;
    private String locName;
    private String locCode;

    /**
     * 是否交班
     */
    private boolean isJiaoban = false;
    /**
     * 值班gid
     */
    private String zbGid;
    /**
     * 值班人id
     */
    private String zbrid;
    /**
     * 值班类型
     */
    private String classes;
    /**
     * 值班人
     */
    private String watchMan;
    /**
     * 接班时间
     */
    private String successionTime;
    private String zbEcode;
    private String stationCode;
    private String resultInfo;

    /**
     * 接班人
     */
    private String jiebanr;

    /**
     * 接班时间
     */
    private String jiebansj;

    /**
     * 场站信息
     */
    private List<StationInfoModel> stationInfoModelList = new ArrayList<>();

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isJiaoban() {
        return isJiaoban;
    }

    public void setJiaoban(boolean jiaoban) {
        isJiaoban = jiaoban;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public String getZbGid() {
        return zbGid;
    }

    public void setZbGid(String zbGid) {
        this.zbGid = zbGid;
    }

    public String getZbrid() {
        return zbrid;
    }

    public void setZbrid(String zbrid) {
        this.zbrid = zbrid;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getWatchMan() {
        return watchMan;
    }

    public void setWatchMan(String watchMan) {
        this.watchMan = watchMan;
    }

    public String getSuccessionTime() {
        return successionTime;
    }

    public void setSuccessionTime(String successionTime) {
        this.successionTime = successionTime;
    }

    public String getZbEcode() {
        return zbEcode;
    }

    public void setZbEcode(String zbEcode) {
        this.zbEcode = zbEcode;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    private final Map<String, IUserInfo> function2UserInfo = new HashMap<>(8);

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocGid() {
        return locGid;
    }

    public void setLocGid(String locGid) {
        this.locGid = locGid;
    }

    public String getJiaobansj() {
        return jiaobansj;
    }

    public void setJiaobansj(String jiaobansj) {
        this.jiaobansj = jiaobansj;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public String getJiebanr() {
        return jiebanr;
    }

    public void setJiebanr(String jiebanr) {
        this.jiebanr = jiebanr;
    }

    public String getJiebansj() {
        return jiebansj;
    }

    public void setJiebansj(String jiebansj) {
        this.jiebansj = jiebansj;
    }

    public void setRealtimeLocateInterval(String realtimeLocateInterval) {
        this.realtimeLocateInterval = realtimeLocateInterval;
    }

    public String getRealtimeLocateInterval() {
        return realtimeLocateInterval;
    }

    public IUserInfo getUserInfo(String functionKey) {
        return function2UserInfo.get(functionKey);
    }

    public void putUserInfo(String functionKey, IUserInfo userInfo) {
        function2UserInfo.put(functionKey, userInfo);
    }

    public List<StationInfoModel> getStationInfoModelList() {
        return stationInfoModelList;
    }

    public void setStationInfoModelList(List<StationInfoModel> stationInfoModelList) {
        this.stationInfoModelList = stationInfoModelList;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }
}
