package com.ecity.cswatersupply.menu;

import java.util.List;

import com.ecity.cswatersupply.utils.ResourceUtil;

/**
 * 按钮菜单页，包括： 侧滑按钮页 主界面的九宫格按钮页 工单处置界面的tab页
 * 也可用于标识标签按钮
 * @author gaokai
 *
 */
public class TabMenu extends AMenu {
    private static final long serialVersionUID = 584526118573425490L;

    private boolean isConfigurable;
    private List<AppMenu> menus;

    public TabMenu() {
        this("", "", false);
    }

    public TabMenu(String name) {
        this(name, "", false);
    }

    public TabMenu(String name, String iconName, boolean isConfigurable) {
        this.name = name;
        this.iconName = iconName;
        this.isConfigurable = isConfigurable;
    }

    public TabMenu(String name, int iconId, boolean isConfigurable) {
        this.name = name;
        this.iconId = iconId;
        this.isConfigurable = isConfigurable;
    }

    public int getIconId() {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    public List<AppMenu> getMenus() {
        return menus;
    }

    public void setMenus(List<AppMenu> menus) {
        this.menus = menus;
    }

    public boolean isConfigurable() {
        return isConfigurable;
    }

    public void setConfigurable(boolean isConfigurable) {
        this.isConfigurable = isConfigurable;
    }
}
