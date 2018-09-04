package com.ecity.cswatersupply.emergency.fragment;

import android.support.v4.app.Fragment;

import com.ecity.cswatersupply.emergency.model.EDownloadFragmentType;
import com.ecity.cswatersupply.event.ResponseEventStatus;

public class KnowLedgeFragmentFactory {

    /**
     * 获取fragment
     *
     * @param fragment类型 FragmentType枚举
     * @return fragment
     */
    public Fragment getFragment(EDownloadFragmentType type) {
        Fragment fragment = null;
        switch (type) {
            case EMERGENCY_PLAN:
                fragment = new EmergencyPlanFragment(1, ResponseEventStatus.EMERGENCY_GET_KNOWBASE_EMERGENCY_PLAN);

                break;

            case EMERGENCY_SUGGEST:
                fragment = new EmergencyPlanFragment(2, ResponseEventStatus.EMERGENCY_GET_KNOWBASE_EMERGENCY_IDEA);

                break;

            case EARTHQUAKE_KNOWLEDGE:
                fragment = new EmergencyPlanFragment(3, ResponseEventStatus.EMERGENCY_GET_KNOWBASE_EMERGENCY_COMMON);
                break;
            default:
                break;
        }

        return fragment;
    }
}