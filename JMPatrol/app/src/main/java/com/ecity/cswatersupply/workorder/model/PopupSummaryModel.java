package com.ecity.cswatersupply.workorder.model;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.ResourceUtil;

/**
 * 
 * 
 * @author qiwei
 *
 */
public enum PopupSummaryModel {
    SUMMARY_POPUP_STATICS(ResourceUtil.getStringById(R.string.menu_summary_static)), 
    SUMMARY_POPUP_DETAILS(ResourceUtil.getStringById(R.string.menu_summary_details));

    private String name;

    /**
     * 
     * @param name
     *            用于显示
     */
    PopupSummaryModel(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static PopupSummaryModel getPageByName(String name) {
        for (PopupSummaryModel p : values()) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }
}
