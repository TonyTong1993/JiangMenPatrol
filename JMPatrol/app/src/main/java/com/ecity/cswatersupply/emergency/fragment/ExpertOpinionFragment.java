package com.ecity.cswatersupply.emergency.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.activity.NewsAnnouncementDetail;
import com.ecity.cswatersupply.emergency.adapter.ExpertAdapter;
import com.ecity.cswatersupply.emergency.model.EmergencyPlanModel;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;

/**
 * 
 * 专家意见
 * @author 49136
 *
 */
    public class ExpertOpinionFragment extends Fragment{
//
//    private ListView ltv_download;
//    private List<EmergencyPlanModel> models;
//    private ExpertAdapter adapter;
//    private List<String> list;
//    @Override
//    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
//        View view = inflater.inflate(getResourXmlLayput(), null);
//        EventBusUtil.register(this);
//        list = new ArrayList<String>();
//        list.add("黄石:3.6级地震");
//        list.add("黄冈:9.6级地震");
//        list.add("黄山:11.8级地震");
//        list.add("蕲春:13.6级地震");
//        list.add("武汉:13.86级地震");
//        initUi(view);
//        return view;
//    }
//
//    private void initUi(View view) {
//        ltv_download = (ListView) view.findViewById(R.id.ltv_download);
//        adapter = new ExpertAdapter(getActivity());
//       // adapter.setDatas(list);
//        ltv_download.setAdapter(adapter);
//        ltv_download.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ltv_download.getItemAtPosition(position);
//                String string = list.get(position);
//                //设置一个标示 根据标示来显示相对应的详情
//                Intent intent = new Intent(getActivity(), NewsAnnouncementDetail.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    /**
//     * EventBus methods begin.
//     */
//    
//    public void onEventMainThread(UIEvent event) {
//        switch (event.getId()) {
//            case UIEventStatus.TOAST:
//                if (event.isForTarget(this)) {
//                    ToastUtil.showShort(event.getMessage());
//                }
//                break;
//            case UIEventStatus.NOTIFICATION_EXPERTOPINION:
//                parsingDatas(event);
//                break;
//            default:
//                break;
//        }
//    }
//    
//    
//    private void parsingDatas(UIEvent event) {
//        models = new ArrayList<EmergencyPlanModel>();
//        if (null == event) {
//            return;
//        }
//        List<EmergencyPlanModel> data = event.getData();
//        for (int i = 0; i < data.size(); i++) {
//            EmergencyPlanModel tmp = new EmergencyPlanModel();
//            tmp.setName(data.get(i).getName());
//            tmp.setName(data.get(i).getCreatetime());
//            tmp.setName(data.get(i).getDescribe());
//            models.add(tmp);
//        }
//        
//    }
//
//    public void onEventMainThread(ResponseEvent event) {
//        if (!event.isOK()) {
//            LoadingDialogUtil.dismiss();
//            ToastUtil.showLong(event.getMessage());
//            return;
//        }
////        switch (event.getId()) {
////            case ResponseEventStatus.EMERGENCY_GET_KNOWBASE_EMERGENCY_PLAN:
////              //  FillData(event);
////                break;
////
////            default:
////                break;
////        }
//    }
//    
//    protected int getResourXmlLayput() {
//        return R.layout.fragment_downloadlistview;
//    }
//    
//    @Override
//    public void onResume() {
//        adapter.notifyDataSetChanged();
//        super.onResume();
//    }
}
