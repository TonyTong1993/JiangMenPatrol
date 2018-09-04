package com.ecity.cswatersupply.workorder.fragment;

import java.util.ArrayList;
import java.util.List;

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
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.adapter.FlowInfoListAdapter;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.FlowInfoBean;
import com.ecity.cswatersupply.ui.activities.WorkOrderDetailFragmentActivity;
import com.ecity.cswatersupply.ui.fragment.ABaseListViewFragment;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.WorkOrderDetailFlowInfoActivity;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;

@SuppressLint("ValidFragment")
public class WorkOrderDetailFlowInfoFragment extends ABaseListViewFragment<FlowInfoBean> {
    private static WorkOrderDetailFlowInfoFragment instance;
    private TextView tvMessageBlank;
    private ArrayListAdapter<FlowInfoBean> mAdapter;
    private List<FlowInfoBean> listDatas = new ArrayList<FlowInfoBean>();

    public static WorkOrderDetailFlowInfoFragment getInstance() {
        if (null == instance) {
            synchronized (WorkOrderDetailFlowInfoFragment.class) {
                if (null == instance) {
                    instance = new WorkOrderDetailFlowInfoFragment();
                }
            }
        }
        return instance;
    }

    private WorkOrderDetailFlowInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        EventBusUtil.register(this);
        initUI(view);
        return view;
    }

    @Override
    protected ArrayListAdapter<FlowInfoBean> prepareListViewAdapter() {
        mAdapter = new FlowInfoListAdapter(getActivity());
        return mAdapter;
    }

    @Override
    protected List<FlowInfoBean> prepareDataSource() {
        listDatas = ((WorkOrderDetailFragmentActivity) getActivity()).getAllFlowInfoItems();
        return listDatas;
    }

    @Override
    protected OnItemClickListener prepareOnItemClickListener() {
        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(WorkOrder.KEY_SERIAL, ((WorkOrderDetailFragmentActivity) getActivity()).getCurrentWorkOrder());
                bundle.putString("EVENTTYPE", listDatas.get(position).getType());
                bundle.putString("PROCESSINSTANCEID", listDatas.get(position).getProcessinstanceid());
                UIHelper.startActivityWithExtra(WorkOrderDetailFlowInfoActivity.class, bundle);
            }
        };
    }

    private void initUI(View convertView) {
        tvMessageBlank = (TextView) convertView.findViewById(R.id.tv_content_blank);
        refreshUI();
    }

    private void refreshUI() {
        getTitleView().setVisibility(View.GONE);
        listDatas = ((WorkOrderDetailFragmentActivity) getActivity()).getAllFlowInfoItems();
        if (ListUtil.isEmpty(listDatas)) {
            tvMessageBlank.setVisibility(View.VISIBLE);
            getRefreshListView().setVisibility(View.GONE);
        } else {
            tvMessageBlank.setVisibility(View.GONE);
            getRefreshListView().setVisibility(View.VISIBLE);
            mAdapter.setList(listDatas);
        }
    }

    public void onEventMainThread(UIEvent event) {
        if (event.getId() == UIEventStatus.WORKORDER_DETAIL_REFRESH_FLOW_INFO_FRAGMENT) {
            refreshUI();
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

}
