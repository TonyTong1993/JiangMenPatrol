package com.ecity.cswatersupply.menu;

import android.content.res.Resources;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.z3app.android.util.StringUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuFactory {
    private static final String TAG = "MenuFactory";
    public static final String KEY_TAB_MENU = "menu";
    public static final String KEY_MENU_NAME = "name";
    public static final String KEY_MENU_ICON_NAME = "IconName";
    public static final String KEY_MENU_DELETABLE = "Deleteable";
    public static final String KEY_MENU_DRAGABLE = "Dragable";
    public static final String KEY_MENU_CONFIGURABLE = "Configurable";
    public static final String KEY_MENU_SUBNAME = "SubName";
    public static final String KEY_MENU_COMMAND = "Command";
    private static Map<TabMenu, List<AppMenu>> tabMenu2Menus = new HashMap<TabMenu, List<AppMenu>>(); // 带文字标签的分页tab
    private static List<TabMenu> tabMenus = new ArrayList<TabMenu>();
    private static Map<String, AppMenu> workOrderBtns;

    public static void analyzeTablesXML(Resources resources) {
        parseInBackground(resources);
    }

    private static void parseInBackground(final Resources resources) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                parseFromXML(resources);
            }
        }).start();
    }

    private static void parseFromXML(Resources resources) {
        List<AppMenu> menuList = null;
        TabMenu tabMenu = null;
        List<TabMenu> copyList = null;
        String configurable = "";
        AppMenu menu = null;
        try {
            XmlPullParser parser = resources.getXml(ResourceUtil.getXmlResourceId("production"));
            int eventType = parser.getEventType();// 产生第一个事件
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    tabMenu2Menus.clear();
                    break;
                case XmlPullParser.START_TAG:
                    String nodeName = parser.getName();// 获取解析器当前指向的元素的名称

                    if ("Category".equalsIgnoreCase(nodeName)) {
                        String tabName = parser.getAttributeValue(null, "name");
                        String[] states = tabName.split(",");
                        configurable = parser.getAttributeValue(null, "configurable");
                        boolean isConfigurable = "1".equalsIgnoreCase(configurable);
                        String tabIconName = parser.getAttributeValue(null, "iconName");
                        // 相同配置的可按格式“A,B,C”配置
                        tabMenu = new TabMenu(states[0], tabIconName, isConfigurable);
                        tabMenus.add(tabMenu);
                        // 复制配置相同的tab页
                        if (states.length > 1) {
                            TabMenu copyTab = null;// 用于复制配置相同的tab页
                            copyList = new ArrayList<TabMenu>(4);
                            for (int i = 1; i < states.length; i++) {
                                copyTab = new TabMenu(states[i], tabIconName, isConfigurable);
                                copyList.add(copyTab);
                            }
                        }
                        menuList = new ArrayList<AppMenu>();
                    }
                    if (menuList != null) {
                        if ("Menu".equalsIgnoreCase(nodeName)) {
                            menu = new AppMenu();
                        }
                        if (menu != null) {
                            parseMenu(parser, tabMenu.getName(), nodeName, menu);
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("Category".equalsIgnoreCase(parser.getName())) {
                        if (menuList != null) {
                            tabMenu2Menus.put(tabMenu, menuList);
                            if (copyList != null && copyList.size() > 0) {
                                for (TabMenu t : copyList) {
                                    tabMenus.add(t);
                                    tabMenu2Menus.put(t, menuList);
                                }
                                copyList = null;
                            }
                        }
                        menuList = null;
                    } else if ("Menu".equalsIgnoreCase(parser.getName())) {
                        if (menu != null) {
                            menuList.add(menu);
                        }
                        menu = null;
                    }
                    break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e);
            throw new RuntimeException(e);
        }
    }

    private static void parseMenu(XmlPullParser parser, String menuCategory, String name, AppMenu menu) throws XmlPullParserException, IOException {
        if (KEY_TAB_MENU.equalsIgnoreCase(name)) {
            return;
        }

        String nodeText = parser.nextText();
        nodeText = StringUtil.isEmpty(nodeText) ? "" : nodeText;

        if (KEY_MENU_NAME.equalsIgnoreCase(name)) {
            menu.setName(nodeText);
        } else if (KEY_MENU_ICON_NAME.equalsIgnoreCase(name)) {
            menu.setIconName(nodeText);
        } else if (KEY_MENU_DELETABLE.equalsIgnoreCase(name)) {
            menu.setDeleteable(nodeText.equalsIgnoreCase("1"));
        } else if (KEY_MENU_DRAGABLE.equalsIgnoreCase(name)) {
            menu.setDragable(nodeText.equalsIgnoreCase("1"));
        } else if (KEY_MENU_CONFIGURABLE.equalsIgnoreCase(name)) {
            menu.setConfigurable(nodeText.equalsIgnoreCase("1"));
        } else if (KEY_MENU_SUBNAME.equalsIgnoreCase(name)) {
            menu.setSubName(nodeText);
        } else if (KEY_MENU_COMMAND.equalsIgnoreCase(name)) {
            menu.setAMenuCommand(getMenuCommand(nodeText));
        } else {

        }
    }

    /**
     * Get menus to be displayed in main screen tabs.
     * 
     * @return A map. key: TabMenu, value: a list of {@link AppMenu}.
     */
    public static Map<TabMenu, List<AppMenu>> getTabMenu2Menus() {
        return tabMenu2Menus;
    }

    /**
     * @return TabMenus.
     */
    public static List<TabMenu> getTabMenus() {
        List<TabMenu> mainTabs = new ArrayList<TabMenu>();
        for (TabMenu tabMenu : tabMenus) {
            mainTabs.add(tabMenu);
        }

        return mainTabs;
    }

    /**
     * Get the implementation of a given menuName.
     * 
     * @param menuName
     * @return
     */
    public static AMenuCommand getMenuCommand(String menuName) {
        if (menuName != null) {
            menuName = menuName.trim();
        }
        if (StringUtil.isEmpty(menuName)) {
            return new EmptyCommandXtd();
        }
        try {
            return (AMenuCommand) Class.forName(menuName).newInstance();
        } catch (Exception e) {
            LogUtil.e(TAG, e);
            e.printStackTrace();
        }
        return new EmptyCommandXtd();
    }

    /**
     * 获得指定tab页中所有按钮 返回List集合
     * 
     * @param name
     * @return
     */
    public static List<AppMenu> getMenuListByTab(String name) {
        List<TabMenu> tabs = MenuFactory.getTabMenus();
        for (TabMenu tabMenu : tabs) {// 获得主界面TAB
            if (tabMenu.getName().equalsIgnoreCase(name)) {
                return getTabMenu2Menus().get(tabMenu);// 获得主界面所有按钮
            }
        }
        return new ArrayList<AppMenu>();

    }

    public static void List2Map(List<AppMenu> menusList) {
        workOrderBtns = new HashMap<String, AppMenu>((int) (menusList.size() / 0.75 + 1));
        for (AppMenu appMenu : menusList) {
            workOrderBtns.put(appMenu.getName(), appMenu);
        }
    }

    public static Map<String, AppMenu> getWorkOrderBtns() {
        if (workOrderBtns == null) {
            List<AppMenu> menuList = getMenuListByTab("workOrderBtns");
            List2Map(menuList);
        }
        return workOrderBtns;
    }

    /**
     * 获得指定tab页中所有按钮 返回Map集合
     * 
     * @param string
     * @return
     */
    public static HashMap<String, AppMenu> getMenuMapByTab(String string) {
        List<TabMenu> tabs = MenuFactory.getTabMenus();
        HashMap<String, AppMenu> menuMap = new HashMap<String, AppMenu>();
        for (TabMenu tabMenu : tabs) {// 获得主界面TAB
            if (tabMenu.getName().equalsIgnoreCase(string)) {
                List<AppMenu> menuList = getTabMenu2Menus().get(tabMenu);
                if (null != menuList) {// 注意menuList可能为NULL
                    for (AppMenu menu : menuList) {
                        menuMap.put(menu.getName(), menu);
                    }
                }
            }
        }

        return menuMap;
    }

    public static void addMoreBtn(AppMenu moreBtn) {
        
    }
    /**
     * 得到配置的tab菜单，根据配置信息解析分割得到tab菜单的id
     */
    public static List<AppMenu> getDynamicTabs(List<AppMenu> appMenus) {
        List<AppMenu> tabMenus = new ArrayList<>();
        for (int i = 0; i < appMenus.size(); i++) {
            AppMenu menu = appMenus.get(i);
            String pageUrl = menu.getUrl();
            if (!StringUtil.isEmpty(pageUrl)) {
                String[] menuUrls = pageUrl.split(":");
                if ((null != menuUrls) && (menuUrls.length > 1)) {
                    if (menuUrls[0].equals("MAIN_TAB")) {
                        AppMenu tabMenu = new AppMenu();
                        if(menuUrls[1].equals("WEB")){//是网页菜单，后面配置有网页url
                            //MAIN_TAB   WEB    http         //www.baidu.com/@type_web_title
                            tabMenu.setGid(menuUrls[1]);
                            if(menuUrls.length>2){
                                String webUrl="";
                                for(int j=2;j<menuUrls.length;j++){
                                    if(j==2){
                                        webUrl+=menuUrls[2]+":";
                                    }else{
                                        webUrl+=menuUrls[j];
                                    }
                                }
                                tabMenu.setUrl(getPageUrl(webUrl));
                                tabMenu.setTitleType(getTitleType(webUrl));
                            }
                        }else{
                            tabMenu.setGid(menuUrls[1]);
                        }
                        tabMenu.setDescription(menu.getDescription());
                        tabMenus.add(tabMenu);
                    }
                }
            }
        }
        return tabMenus;
    }
    /**
     * 得到去除标题类型后的配置路径
     * @param url
     * @return
     */
    public static String getPageUrl(String url){
        String pageUrl="";
        if(StringUtil.isEmpty(url)){
            return "";
        }
        String[] urls=url.split("@");
        if((null!=urls)&&(urls.length==2)){
            pageUrl=urls[0];
        }
        return  pageUrl;
    }
    /**
     * 得到标题类型
     * @param url
     * @return
     */
    public static String getTitleType(String url){
        String titleType="";
        if(StringUtil.isEmpty(url)){
            return "";
        }
        String[] urls=url.split("@");
        if((null!=urls)&&(urls.length==2)){
            titleType=urls[1];
        }
        return  titleType;
    }
}
