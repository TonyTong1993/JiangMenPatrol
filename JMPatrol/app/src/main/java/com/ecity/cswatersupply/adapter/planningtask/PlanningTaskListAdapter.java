package com.ecity.cswatersupply.adapter.planningtask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTask;

public class PlanningTaskListAdapter extends ArrayListAdapter<Z3PlanTask> {

    private LayoutInflater mInflater;
    private Context mContext;
    private boolean isConserveTask;
    private final int TYPE_COUNT = 2;
    private final int FIRST_TYPE = 0;
    private final int SECOND_TYPE = 1;
    private int currentType;

    public PlanningTaskListAdapter(Context context, boolean isConserveTask) {
        super(context);
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.isConserveTask = isConserveTask;
    }

    @Override
    public int getItemViewType(int position) {
        Z3PlanTask planTask = mList.get(position);
        if ("施工场地".equals(planTask.getTasksubtype())) {
            return SECOND_TYPE;
        } else {  
            return FIRST_TYPE;
        } 
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Z3PlanTask planTask = mList.get(position);
        View firstItemView = null;
        View secondItemView = null;
        //获取到当前位置所对应的Type
        currentType = getItemViewType(position); 
        switch (currentType) {
            case FIRST_TYPE:
                firstItemView = convertView;
                ViewHolder viewHolder = null;
                if (null == firstItemView) {
                    firstItemView = mInflater.inflate(R.layout.lv_item_planningtask, null);
                    viewHolder = new ViewHolder(firstItemView);
                    firstItemView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.planningtask_name.setText(planTask.getPlanname());
                viewHolder.planningtask_time.setText(planTask.getTaskstart() + "~" + planTask.getTaskend());

                if(SessionManager.isPointTask.get(planTask.getTaskid()+"")) {
                    viewHolder.cover_LinearLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.cover_LinearLayout.setVisibility(View.VISIBLE);
                }
                PlanTaskAvailabilityLLAdapter.getInstance().setLLData(mContext, viewHolder.planningtask_point_arrvied, viewHolder.planningtask_arrvied_progressbar,
                        viewHolder.plantask_point_arrvied_rate, planTask, Constants.PLANTASK_RATEFLAG_POINT_LOCATION);
                PlanTaskAvailabilityLLAdapter.getInstance().setLLData(mContext, viewHolder.plantask_line_arrived, viewHolder.planningtask_line_progressbar, viewHolder.plantask_line_rate,
                        planTask, Constants.PLANTASK_RATEFLAG_LINE);
                //判断是否为养护任务
                if (!isConserveTask) {
                    viewHolder.feedback_LinearLayout.setVisibility(View.GONE);
                }else{
                    PlanTaskAvailabilityLLAdapter.getInstance().setLLData(mContext, viewHolder.plantask_point_feedback, viewHolder.planningtask_feedback_progressbar,
                            viewHolder.plantask_point_feedback_rate, planTask, Constants.PLANTASK_RATEFLAG_POINT_FEEDBACK);
                }
                convertView = firstItemView;
                break; 
            case SECOND_TYPE:
                secondItemView = convertView;
                ViewHolder1 viewHolder1 = null;
                if (null == secondItemView) {  
                    secondItemView = mInflater.inflate(R.layout.lv_item_planningtask_construction,null);
                    viewHolder1 = new ViewHolder1(secondItemView);
                    secondItemView.setTag(viewHolder1);
                } else {
                    viewHolder1 = (ViewHolder1) secondItemView.getTag();
                }
                if (null == mList || position > mList.size()) {
                    return secondItemView;
                }
                viewHolder1.planningtask_construction_name.setText(planTask.getPlanname());
                viewHolder1.planningtask_construction_time.setText(planTask.getTaskstart() + "~" + planTask.getTaskend());
                viewHolder1.planningtask_construction_eventNumber.setText(String.valueOf(planTask.getEventnum()));
                convertView = secondItemView;
                break;
            default:
                break;
        }
        return convertView;
    }

    public static class ViewHolder {
        public TextView  planningtask_point_arrvied, plantask_point_feedback_rate,plantask_point_feedback, plantask_line_rate,
                planningtask_name,plantask_point_arrvied_rate, planningtask_time,plantask_line_arrived;
        public LinearLayout feedback_LinearLayout,cover_LinearLayout;
        public ProgressBar planningtask_arrvied_progressbar, planningtask_feedback_progressbar, planningtask_line_progressbar;

        public ViewHolder(View v) {
            planningtask_name = (TextView) v.findViewById(R.id.planningtask_name);
            planningtask_time = (TextView) v.findViewById(R.id.planningtask_time);
            planningtask_point_arrvied = (TextView) v.findViewById(R.id.planningtask_point_arrvied);
            plantask_point_arrvied_rate = (TextView) v.findViewById(R.id.plantask_point_arrvied_rate);
            plantask_point_feedback = (TextView) v.findViewById(R.id.plantask_point_feedback);
            plantask_point_feedback_rate = (TextView) v.findViewById(R.id.plantask_point_feedback_rate);
            plantask_line_arrived = (TextView) v.findViewById(R.id.plantask_line_arrived);
            plantask_line_rate = (TextView) v.findViewById(R.id.plantask_line_rate);
            planningtask_arrvied_progressbar = (ProgressBar) v.findViewById(R.id.planningtask_arrvied_progressbar);
            planningtask_feedback_progressbar = (ProgressBar) v.findViewById(R.id.planningtask_feedback_progressbar);
            planningtask_line_progressbar = (ProgressBar) v.findViewById(R.id.planningtask_line_progressbar);

            feedback_LinearLayout = (LinearLayout)v.findViewById(R.id.ll_feedback);
            cover_LinearLayout = (LinearLayout)v.findViewById(R.id.ll_line);
        }
    }

    public static class ViewHolder1 {
        public TextView planningtask_construction_name;
        public TextView planningtask_construction_time;
        public TextView planningtask_construction_eventNumber;

        public ViewHolder1(View v) {
            planningtask_construction_name = (TextView) v.findViewById(R.id.planningtask_construction_name);
            planningtask_construction_time = (TextView) v.findViewById(R.id.planningtask_construction_time);
            planningtask_construction_eventNumber = (TextView) v.findViewById(R.id.plantask_construction_eventnumber);
        }
    }
}
