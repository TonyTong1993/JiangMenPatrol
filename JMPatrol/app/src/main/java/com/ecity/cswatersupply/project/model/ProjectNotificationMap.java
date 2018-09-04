package com.ecity.cswatersupply.project.model;

import com.ecity.cswatersupply.project.activity.fragment.CompleteInfoFragment;
import com.ecity.cswatersupply.project.activity.fragment.PayInfoFragment;
import com.ecity.cswatersupply.project.activity.fragment.ProspectiveInfoFragment;
import com.ecity.cswatersupply.project.activity.fragment.ProssureTestInfoFragment;
import com.ecity.cswatersupply.project.activity.fragment.StartWorkingInfoFragment;
import com.ecity.cswatersupply.project.activity.fragment.WaterReceiveInfoFragment;
import com.ecity.cswatersupply.project.activity.fragment.WorkLoadInfoFragment;

public class ProjectNotificationMap {

    public static String fragmentName = "";
    public static String titleName = "";
    public static String logType = "";
    public static String[] tabTitles = {};

    public static void getFragmetName(String type) {
        int indexOf = type.indexOf("_");
        String substring = type.substring(0, indexOf);

        if (substring.equals("kg")) {
            fragmentName = StartWorkingInfoFragment.class.getName();
            titleName = "开工申请";
            logType = substring;
            tabTitles = new String[] {};
        } else if (substring.equals("sy")) {
            fragmentName = ProssureTestInfoFragment.class.getName();
            titleName = "试压试验";
            logType = substring;
            tabTitles = new String[] {};
        } else if (substring.equals("js")) {
            fragmentName = WaterReceiveInfoFragment.class.getName();
            titleName = "接水";
            tabTitles = new String[] {};
            logType = substring;
        } else if (substring.equals("workload")) {
            fragmentName = WorkLoadInfoFragment.class.getName();
            titleName = "工作量";
            logType = substring;
            tabTitles = new String[] {};
        } else if (substring.equals("pay")) {
            fragmentName = PayInfoFragment.class.getName();
            titleName = "资金支付";
            logType = substring;
            tabTitles = new String[] {};
        } else if (substring.equals("jg")) {
            fragmentName = CompleteInfoFragment.class.getName();
            titleName = "竣工验收";
            tabTitles = new String[] {};
            logType = substring;
        } else if (substring.equals("sj")) {
            fragmentName = ProspectiveInfoFragment.class.getName();
            titleName = "勘察设计";
            tabTitles = new String[] { "基本信息", "委托信息", "延期信息", "提交信息" };
            logType = substring;
        }

        //        switch (substring) {
        //            case "kg"://开工
        //                fragmentName = StartWorkingInfoFragment.class.getName();
        //                titleName = "开工申请";
        //                logType = substring;
        //                tabTitles = new String[] {};
        //                break;
        //            case "sy"://试压
        //                fragmentName = ProssureTestInfoFragment.class.getName();
        //                titleName = "试压试验";
        //                logType = substring;
        //                tabTitles = new String[] {};
        //                break;
        //            case "js"://接水
        //                fragmentName = WaterReceiveInfoFragment.class.getName();
        //                titleName = "接水";
        //                tabTitles = new String[] {};
        //                logType = substring;
        //                break;
        //            case "workload"://工作量
        //                fragmentName = WorkLoadInfoFragment.class.getName();
        //                titleName = "工作量";
        //                logType = substring;
        //                tabTitles = new String[] {};
        //                break;
        //            case "pay"://资金支付
        //                fragmentName = PayInfoFragment.class.getName();
        //                titleName = "资金支付";
        //                logType = substring;
        //                tabTitles = new String[] {};
        //                break;
        //            case "jg"://竣工验收
        //
        //                fragmentName = CompleteInfoFragment.class.getName();
        //                titleName = "竣工验收";
        //                tabTitles = new String[] {};
        //                logType = substring;
        //                break;
        //            case "sj"://勘察设计
        //                fragmentName = ProspectiveInfoFragment.class.getName();
        //                titleName = "勘察设计";
        //                tabTitles = new String[] { "基本信息", "委托信息", "延期信息", "提交信息" };
        //                logType = substring;
        //                break;
        //
        //            default:
        //                break;
        //        }
    }
}
