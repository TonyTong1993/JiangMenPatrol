package com.ecity.cswatersupply.project.activity.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.project.activity.ProjectSearchActivity;
import com.ecity.cswatersupply.project.adapter.ProjectListAdapter;
import com.ecity.cswatersupply.project.model.Project;
import com.ecity.cswatersupply.project.service.ProjectService;
import com.ecity.cswatersupply.project.util.ProjectAdapter;
import com.ecity.cswatersupply.project.view.WaterMeterListView;
import com.ecity.cswatersupply.ui.activities.MainActivity;
import com.ecity.cswatersupply.ui.fragment.ABaseListViewFragment;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;

import org.json.JSONObject;

import java.util.List;

public class WaterMeterListFragment extends ABaseListViewFragment<Project> {
    private int position;
    private String type;
    private String startTime;
    private String endTime;
    protected int pageNo = 1;
    private int eventid;
    protected static final int pageSize = 10;
    private boolean isInQuery;

    private LinearLayout ll_searchview;
    private CustomTitleView customTitleView;

    public WaterMeterListFragment() {
    }

    @SuppressLint("")
    public WaterMeterListFragment(String type, int position, String startTime, String endTime, int eventid) {
        this.position = position;
        this.eventid = eventid;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        EventBusUtil.register(this);
        initView(view);
        if (pageNo == 1) {
            requestAllList(position);
        }
        setOnClickListener();
        return view;
    }

    @Override
    protected String getTitle() {
        return "";
    }

    @Override
    public void onDestroyView() {
        EventBusUtil.unregister(this);
        super.onDestroyView();
    }


    private void handleGetProspectiveList(ResponseEvent event) {

        JSONObject response = event.getData();
        List<Project> projects = ProjectAdapter.parseProjects(response);
        ProjectAdapter.parseProjectFieldMeta(response);

        if (ListUtil.isEmpty(projects)) {
            UIEvent uiEvent = new UIEvent(UIEventStatus.TOAST);
            uiEvent.setMessage(getString(R.string.message_no_more_record));
            uiEvent.setTargetClass(MainActivity.class);
            EventBusUtil.post(uiEvent);
            getRefreshListView().setHasMoreData(false);
        } else {
            getDataSource().addAll(projects);
            updateDataSource(getDataSource());
            pageNo++;
            getRefreshListView().onPullUpRefreshComplete();
            getRefreshListView().setLastUpdateTime();
        }
    }

    private void initView(View view) {
        customTitleView = (CustomTitleView) view.findViewById(R.id.customTitleView);
        customTitleView.setVisibility(View.GONE);

        ll_searchview = (LinearLayout) view.findViewById(R.id.ll_searchview);
        ll_searchview.setVisibility(View.VISIBLE);
    }

    private void requestAllList(int position) {
        isInQuery = true;
        String state = String.valueOf(position);
        ProjectService.getInstance().getWaterMeterProList(pageNo, pageSize, type, state, startTime, endTime);
    }


    private void setOnClickListener() {
        ll_searchview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProjectSearchActivity.class);
                getActivity().startActivityFromFragment(WaterMeterListFragment.this, intent, ProjectSearchActivity.REQUEST_EDIT_FITLER);
            }
        });
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            ToastUtil.showLong(event.getMessage());
            return;
        }
        if ((event.getId() == eventid) && isInQuery) {
            isInQuery = false;
            if (event.isOK()) {
                handleGetProspectiveList(event);
            } else {
                ToastUtil.showLong(event.getMessage());
            }
        }
    }

    @Override
    protected ArrayListAdapter<Project> prepareListViewAdapter() {
        WaterMeterListView view = new WaterMeterListView();
        return new ProjectListAdapter(getActivity(), view);
    }

    @Override
    protected OnItemClickListener prepareOnItemClickListener() {
        return null;
    }

    @Override
    protected List<Project> prepareDataSource() {
        return getDataSource();
    }


    protected OnRefreshListener<ListView> prepareOnRefreshListener() {
        return new OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestAllList(position);
            }
        };
    }

    @Override
    protected boolean isPullRefreshEnabled() {
        return false;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
