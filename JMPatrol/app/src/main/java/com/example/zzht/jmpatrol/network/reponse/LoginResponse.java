package com.example.zzht.jmpatrol.network.reponse;

import com.example.zzht.jmpatrol.base.AServerResponse;
import com.example.zzht.jmpatrol.model.MobileConfig;
import com.example.zzht.jmpatrol.model.TokenUsers;
import com.example.zzht.jmpatrol.model.User;
import com.zzz.ecity.android.applibrary.model.AppMenu;

import java.util.ArrayList;
import java.util.List;

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
}
