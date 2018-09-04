package com.ecity.cswatersupply.adapter.planningtask;

import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTask;
import com.ecity.cswatersupply.ui.activities.planningtask.PlanningTaskManager;

public class PlanTaskAvailabilityLLAdapter {

    private static PlanTaskAvailabilityLLAdapter instance;

    private PlanTaskAvailabilityLLAdapter() {
    }

    static {
        instance = new PlanTaskAvailabilityLLAdapter();
    }

    public static PlanTaskAvailabilityLLAdapter getInstance() {
        return instance;
    }

    public void setLLData(Context mContext, TextView tv_num, ProgressBar pb, TextView tv_rate, Z3PlanTask plantask, int rateFlag) {
        if (plantask == null) {
            return;
        }
        switch (rateFlag) {
            case Constants.PLANTASK_RATEFLAG_POINT_LOCATION:
                int arrivedPointCount = plantask.getArrviedPointCount();
                tv_num.setText(arrivedPointCount + "/" + plantask.getPointcount());
                if ((arrivedPointCount > 0) && (plantask.getPointcount() != 0)) {
                    String pointIsInPlaceRate = PlanningTaskManager.getInstance().getRate(mContext, arrivedPointCount, plantask.getPointcount());
                    pb.setProgress(Integer.parseInt(pointIsInPlaceRate));
                } else {
                    pb.setProgress(0);
                }
                break;
            case Constants.PLANTASK_RATEFLAG_LINE:
                double arrivedLength = plantask.getArrivedLinelen();
                if (arrivedLength > 0) {
                    String rate = PlanningTaskManager.getInstance().getRate(mContext,arrivedLength,plantask.getLinelen());
                    pb.setProgress(Integer.parseInt(rate));
                    tv_rate.setText(rate + "%");
                } else {
                    pb.setProgress(0);
                    tv_rate.setText("0%");
                }
                break;
            case Constants.PLANTASK_RATEFLAG_POINT_FEEDBACK:
                int feedBackedPointCount = plantask.getFeedbackedPointCount();
                tv_num.setText(feedBackedPointCount + "/" + plantask.getPointcount());
                    if (feedBackedPointCount > 0) {
                        String pointIsFeedBackedRate = PlanningTaskManager.getInstance().getRate(mContext, feedBackedPointCount, plantask.getPointcount());
                        pb.setProgress(Integer.parseInt(pointIsFeedBackedRate));
                    } else {
                        pb.setProgress(0);
                    }
                break;
            default:
                break;
        }

    }
}
