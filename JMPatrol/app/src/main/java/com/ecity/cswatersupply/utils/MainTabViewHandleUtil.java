package com.ecity.cswatersupply.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.menu.AppMenu;
import com.ecity.cswatersupply.service.UserService;
import com.ecity.cswatersupply.ui.fragment.BaseWebViewFragment;
import com.ecity.cswatersupply.ui.fragment.ContactTabFragment;
import com.ecity.cswatersupply.ui.fragment.MainMenuFragment;
import com.ecity.cswatersupply.ui.fragment.MapMainTabFragment;
import com.ecity.cswatersupply.ui.fragment.MyProfileMainTabFragment;
import com.ecity.cswatersupply.ui.fragment.TodoFragment;
import com.z3app.android.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/8.
 */

public class MainTabViewHandleUtil {
    public static HostApplication.ProjectStyle projectStyle;
    public static int strId;
    public static int iconId;
    public static Fragment fragment;

    private List<List<AppMenu>> mTabs;
    private static final String TAB_INDEX_INBOX = "MSG";
    private static final String TAB_INDEX_MAIN_MENUS = "APP";
    private static final String TAB_INDEX_MAP = "MAP";
    private static final String TAB_INDEX_CONTACTS = "CONTACT";
    private static final String TAB_INDEX_MY_PROFILE = "ME";
    private static final String TAB_INDEX_WEB = "WEB";

    public MainTabViewHandleUtil() {

    }

    public static void getTabViewStr(int position, List<List<AppMenu>> mTabs) {
        projectStyle = HostApplication.getApplication().getProjectStyle();
        List<AppMenu> dynamicTabMenus=UserService.getInstance().getDynamicTabMenus();
        if(!ListUtil.isEmpty(dynamicTabMenus)){
            AppMenu tabMenu=dynamicTabMenus.get(position);
            String gid=tabMenu.getGid();
            if(!StringUtil.isEmpty(gid)){
                if(gid.equals(TAB_INDEX_INBOX)){
                    strId = R.string.main_tab_message;
                    iconId = R.drawable.main_tab_messege;
                    fragment = new TodoFragment();
                }else if(gid.equals(TAB_INDEX_MAIN_MENUS)){
                    strId = R.string.main_tab_application;
                    iconId = R.drawable.main_tab_application;
                    fragment = new MainMenuFragment(mergeMainMenus(mTabs));
                }else if(gid.equals(TAB_INDEX_MAP)){
                    strId = R.string.main_tab_map;
                    iconId = R.drawable.main_tab_map;
                    fragment = new MapMainTabFragment();
                }else if(gid.equals(TAB_INDEX_CONTACTS)){
                    strId = R.string.inbox_tab_contacts;
                    iconId = R.drawable.main_tab_my;
                    fragment = new ContactTabFragment();
                }else if(gid.equals(TAB_INDEX_MY_PROFILE)){
                    strId = R.string.main_tab_my;
                    iconId = R.drawable.main_tab_my;
                    fragment = new MyProfileMainTabFragment();
                }else if(gid.equals(TAB_INDEX_WEB)){
                    strId = R.string.main_tab_web;
                    iconId = R.drawable.main_tab_my;
                    String menuName = tabMenu.getName();
                    String loadUrl = tabMenu.getUrl();
                    String titleType = tabMenu.getTitleType();
                    fragment = newInstance(menuName, loadUrl, titleType);
                }
            }
        }else{
            if (projectStyle == HostApplication.ProjectStyle.PROJECT_FZXJ) {
                switch (position) {
                    case 0://tab页 消息
                        strId = R.string.main_tab_message;
                        iconId = R.drawable.main_tab_messege;
                        fragment = new TodoFragment();
                        break;

                    case 1://tab页 菜单
                        strId = R.string.main_tab_application;
                        iconId = R.drawable.main_tab_application;
                        fragment = new MainMenuFragment(mergeMainMenus(mTabs));
                        break;
                    case 2://tab页 地图
                        strId = R.string.main_tab_map;
                        iconId = R.drawable.main_tab_map;
                        fragment = new MapMainTabFragment();
                        break;
                    //                case 3://tab页 联系人
                    //                    strId = R.string.inbox_tab_contacts;
                    //                    iconId = R.drawable.main_tab_my;
                    //                    fragment = new ContactTabFragment();
                    //                    break;
                    case 3://tab页 个人中心（我）
                        strId = R.string.main_tab_my;
                        iconId = R.drawable.main_tab_my;
                        fragment = new MyProfileMainTabFragment();
                        break;
                    default:
                        break;
                }
            } else if (projectStyle == HostApplication.ProjectStyle.PROJECT_CZDZ) {
                switch (position) {
                    //                case 0://tab页 消息
                    //                    strId = R.string.main_tab_message;
                    //                    iconId = R.drawable.main_tab_messege;
                    //                    fragment = new TodoFragment();
                    //                    break;

                    case 0://tab页 菜单
                        strId = R.string.main_tab_application;
                        iconId = R.drawable.main_tab_application;
                        fragment = new MainMenuFragment(mergeMainMenus(mTabs));
                        break;
                    case 1://tab页 地图
                        strId = R.string.main_tab_map;
                        iconId = R.drawable.main_tab_map;
                        fragment = new MapMainTabFragment();
                        break;
                    //                case 3://tab页 联系人
                    //                    strId = R.string.inbox_tab_contacts;
                    //                    iconId = R.drawable.main_tab_my;
                    //                    fragment = new ContactTabFragment();
                    //                    break;
                    case 2://tab页 个人中心（我）
                        strId = R.string.main_tab_my;
                        iconId = R.drawable.main_tab_my;
                        fragment = new MyProfileMainTabFragment();
                        break;
                    default:
                        break;
                }
            } else if (projectStyle == HostApplication.ProjectStyle.PROJECT_WHDZ) {
                switch (position) {
                    //                case 0://tab页 消息
                    //                    strId = R.string.main_tab_message;
                    //                    iconId = R.drawable.main_tab_messege;
                    //                    fragment = new TodoFragment();
                    //                    break;

                    case 0://tab页 菜单
                        strId = R.string.main_tab_application;
                        iconId = R.drawable.main_tab_application;
                        fragment = new MainMenuFragment(mergeMainMenus(mTabs));
                        break;
                    case 1://tab页 地图
                        strId = R.string.main_tab_map;
                        iconId = R.drawable.main_tab_map;
                        fragment = new MapMainTabFragment();
                        break;
                    case 2://tab页 联系人
                        strId = R.string.inbox_tab_contacts;
                        iconId = R.drawable.main_tab_my;
                        fragment = new ContactTabFragment();
                        break;
                    case 3://tab页 个人中心（我）
                        strId = R.string.main_tab_my;
                        iconId = R.drawable.main_tab_my;
                        fragment = new MyProfileMainTabFragment();
                        break;
                    default:
                        break;
                }
            } else {
                switch (position) {
                    case 0://tab页 消息
                        strId = R.string.main_tab_message;
                        iconId = R.drawable.main_tab_messege;
                        fragment = new TodoFragment();
                        break;
                    case 1://tab页 菜单
                        strId = R.string.main_tab_application;
                        iconId = R.drawable.main_tab_application;
                        fragment = new MainMenuFragment(mergeMainMenus(mTabs));
                        break;
                    case 2://tab页 地图
                        strId = R.string.main_tab_map;
                        iconId = R.drawable.main_tab_map;
                        fragment = new MapMainTabFragment();
                        break;
                    //                case 3://tab页 联系人
                    //                    strId = R.string.inbox_tab_contacts;
                    //                    iconId = R.drawable.main_tab_my;
                    //                    fragment = new ContactTabFragment();
                    //                    break;
                    case 3://tab页 个人中心（我）
                        strId = R.string.main_tab_my;
                        iconId = R.drawable.main_tab_my;
                        fragment = new MyProfileMainTabFragment();
                        break;
                    default:
                        break;
                }
            }
        }

    }

    private static List<AppMenu> mergeMainMenus(List<List<AppMenu>> tabs) {
        List<AppMenu> menus = new ArrayList<AppMenu>();

        if (ListUtil.isEmpty(tabs)) {
            return menus;
        }

        for (List<AppMenu> menuList : tabs) {
            if (!ListUtil.isEmpty(menuList)) {
                menus.addAll(menuList);
            }
        }

        return menus;
    }

    public static BaseWebViewFragment newInstance(String menuName, String url, String titleType) {
        BaseWebViewFragment fragment = new BaseWebViewFragment();
        Bundle args = new Bundle();
        args.putString(Constants.MENUNAME, menuName);
        args.putString(Constants.URL, url);
        args.putString(Constants.TITLETYPE, titleType);
        fragment.setArguments(args);
        return fragment;
    }
}
