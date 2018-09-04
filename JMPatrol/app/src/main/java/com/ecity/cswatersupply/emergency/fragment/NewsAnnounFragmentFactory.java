package com.ecity.cswatersupply.emergency.fragment;

import android.support.v4.app.Fragment;

import com.ecity.cswatersupply.emergency.model.NewsAnnounFragmentType;
import com.shizhefei.fragment.LazyFragment;

public class NewsAnnounFragmentFactory {
    private static EarthquakeFragment earthFragment;
    private static AnnouncementFragment announcementFragment;
    private static EQMessageFragment messageFragment;
    private static ExpertOpinionFragment expertOpinionFragment;
    /**
    * 获取fragment
    * 
    * @param
    *
    * @return fragment
    */
    public static LazyFragment getFragment(NewsAnnounFragmentType type) {
        LazyFragment fragment = null;
        switch (type) {
            case TASKPLAN: //任务计划
                if (messageFragment == null) {
                    messageFragment = new EQMessageFragment();
                }
                fragment = messageFragment;
                break;


            case ANNOUNCEMENT: //消息
                if (announcementFragment == null) {
                    announcementFragment = new AnnouncementFragment();
                }
                fragment = announcementFragment;
                break;
                
            case EARTHQUAKE: //本地震情
                if (earthFragment == null) {
                    earthFragment = new EarthquakeFragment();
                }
                fragment =  earthFragment;
                
                break;
                
//            case EXPERTOPINION:
//                if (expertOpinionFragment == null) {
//                    expertOpinionFragment = new ExpertOpinionFragment();
//                }
//
//                fragment = expertOpinionFragment;
//                break;
            default:
                break;
        }

        return fragment;
    }
}
