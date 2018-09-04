package com.ecity.cswatersupply.workorder.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.adapter.FlowInfoListAdapter;
import com.ecity.cswatersupply.model.FlowInfoBean;
import com.ecity.cswatersupply.model.WorkOrderDetailTabModel;
import com.ecity.cswatersupply.service.WorkOrderService;
import com.ecity.cswatersupply.ui.activities.WorkOrderDetailFragmentActivity1;
import com.ecity.cswatersupply.ui.fragment.ABaseListViewFragment;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.WorkOrderDetailFlowInfoActivity1;
import com.ecity.cswatersupply.workorder.WorkOrderUtil;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ValidFragment")
public class WorkOrderDetailFlowInfoFragment1 extends ABaseListViewFragment<FlowInfoBean> {
    private TextView tvMessageBlank;
    private ArrayListAdapter<FlowInfoBean> mAdapter;
    private List<FlowInfoBean> listDatas = new ArrayList<FlowInfoBean>();
    private WorkOrderDetailTabModel tab;
    private int eventId;
    private WorkOrderDetailFragmentActivity1 mWorkOrderDetailActivity;
    private static Map<String, WorkOrderDetailFlowInfoFragment1> tabName2Instances = new HashMap<>();

    private WorkOrderDetailFlowInfoFragment1(WorkOrderDetailTabModel tab, int eventId) {
        this.tab = tab;
        this.eventId = eventId;
    }

    public static WorkOrderDetailFlowInfoFragment1 getInstance(WorkOrderDetailTabModel tab, int eventId) {
        WorkOrderDetailFlowInfoFragment1 instance = tabName2Instances.get(tab.getName());
        if (instance == null) {
            instance = new WorkOrderDetailFlowInfoFragment1(tab, eventId);
            tabName2Instances.put(tab.getName(), instance);
        }

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBusUtil.register(this);
        mWorkOrderDetailActivity = (WorkOrderDetailFragmentActivity1) getActivity();
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initUI(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestAduitInfo();
    }

    @Override
    public void onDestroyView() {
        EventBusUtil.unregister(this);
        super.onDestroyView();
    }

    @Override
    protected ArrayListAdapter<FlowInfoBean> prepareListViewAdapter() {
        mAdapter = new FlowInfoListAdapter(mWorkOrderDetailActivity);
        return mAdapter;
    }

    @Override
    protected List<FlowInfoBean> prepareDataSource() {
        return listDatas;
    }

    @Override
    protected OnItemClickListener prepareOnItemClickListener() {
        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("TYPE", listDatas.get(position).getType());
                bundle.putString("GID", listDatas.get(position).getProcessinstanceid());
                UIHelper.startActivityWithExtra(WorkOrderDetailFlowInfoActivity1.class, bundle);
            }
        };
    }

    private void requestAduitInfo() {
        LoadingDialogUtil.show(mWorkOrderDetailActivity,R.string.workorder_downloading);
        String url = WorkOrderUtil.getWorkOrderDetailUrl(tab);
        String processInstanceId = mWorkOrderDetailActivity.getProcessInstanceId();
        Map<String, String> map = WorkOrderUtil.getWorkOrderDetailParam(tab, processInstanceId);
        WorkOrderService.instance.getWorkOrderAduitInfo(url, map, eventId);
    }

    private void initUI(View convertView) {
        tvMessageBlank = (TextView) convertView.findViewById(R.id.tv_content_blank);
        refreshUI();
    }

    private void refreshUI() {
        getTitleView().setVisibility(View.GONE);
        if (ListUtil.isEmpty(listDatas)) {
            tvMessageBlank.setVisibility(View.VISIBLE);
            getRefreshListView().setVisibility(View.GONE);
        } else {
            tvMessageBlank.setVisibility(View.GONE);
            getRefreshListView().setVisibility(View.VISIBLE);
            mAdapter.setList(listDatas);
        }
    }

    @Override
    protected OnRefreshListener<ListView> prepareOnRefreshListener() {
        return null;
    }

    @Override
    protected String getTitle() {
        return null;
    }

    @Override
    protected boolean isPullLoadEnabled() {
        return false;
    }

    @Override
    protected boolean isPullRefreshEnabled() {
        return false;
    }

    public void onEventMainThread(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        if(!event.isOK()) {
            ToastUtil.showLong(event.getMessage());
            return;
        }

        if(eventId == event.getId()) {
            handleGetWorkOrderFlowInfoEvent(event);
        }
    }

    private void handleGetWorkOrderFlowInfoEvent(ResponseEvent event) {
        listDatas = event.getData();
        refreshUI();
    }
}
