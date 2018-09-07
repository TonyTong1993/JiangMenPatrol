package com.ecity.cswatersupply.network.response.loginresponse;

import java.util.ArrayList;
import java.util.List;

import com.ecity.cswatersupply.menu.AppMenu;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.response.AServerResponse;

public class LoginResponse extends AServerResponse {
    private String time;
    private User user;
    private ArrayList<AppMenu> menus;
    private ArrayList<MobileConfig> mobileCfg;
    private ArrayList<TokenUsers> tokenUsers;

    public User getUser() {
        return user;
    }

    public String getTime() {
        return time;
    }

    public List<AppMenu> getMenus() {
        return menus;
    }

    public ArrayList<MobileConfig> getMobileConfigs() {
        return mobileCfg;
    }

    public List<TokenUsers> getTokenUsers() {
        return tokenUsers;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMenus(ArrayList<AppMenu> menus) {
        this.menus = menus;
    }
}
