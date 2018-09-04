package com.ecity.cswatersupply.emergency.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.activity.NoticeDetailActivity;
import com.ecity.cswatersupply.emergency.adapter.EQMessageListAdapter;
import com.ecity.cswatersupply.emergency.adapter.NoticeListAdapter;
import com.ecity.cswatersupply.emergency.adapter.QuakeInfoListAdapter;
import com.ecity.cswatersupply.emergency.network.response.GetNoticeListResponse;
import com.ecity.cswatersupply.emergency.network.response.NoticeModel;
import com.ecity.cswatersupply.emergency.service.EmergencyService;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.shizhefei.fragment.LazyFragment;

import java.util.List;

/**
 * @author 49136
 *         计划任务
 */

public class EQMessageFragment extends LazyFragment {
    public static final String INTENT_KEY_GID = "INTENT_KEY_GID";

    private static int pageNo = 1;
    private static int pageSize = 1000;
    private ListView lv_notice;
    private PullToRefreshListView pullRefreshListView;
    private EQMessageListAdapter adapter;
    private List<NoticeModel> features;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_message);
        EventBusUtil.register(this);
        initView();
        requestMessageList();
        setOnClickListener();
    }

    private void initView() {
        pullRefreshListView = (PullToRefreshListView) findViewById(R.id.ltv_download);
        pullRefreshListView.setPullLoadEnabled(false);
        pullRefreshListView.setPullRefreshEnabled(true);

        lv_notice = pullRefreshListView.getRefreshableView();
        lv_notice.setDividerHeight(ResourceUtil.getDimensionPixelSizeById(R.dimen.margin_10));

        adapter = new EQMessageListAdapter(getActivity());
        lv_notice.setAdapter(adapter);
    }

    private void requestMessageList() {
        LoadingDialogUtil.show(getActivity(), "正在查询消息");
        EmergencyService.getInstance().getEQMessageList();
    }

    private void handleGetNoticeList(ResponseEvent event) {
        List<NoticeModel> list = event.getData();
        adapter.setList(list);
    }

    private void setOnClickListener() {
        pullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestMessageList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

//        lv_notice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), NoticeDetailActivity.class);
//                intent.putExtra(INTENT_KEY_GID, features.get(position).getGid());
//                startActivity(intent);
//            }
//        });
    }


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
//
    public void onEventMainThread(ResponseEvent event) {
        pullRefreshListView.onPullUpRefreshComplete();
        pullRefreshListView.onPullDownRefreshComplete();
        pullRefreshListView.setLastUpdateTime();
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.EMERGENCY_GET_MESSAGE_LIST:
                LoadingDialogUtil.dismiss();
                handleGetNoticeList(event);
                break;

            default:
                break;
        }
    }
}
