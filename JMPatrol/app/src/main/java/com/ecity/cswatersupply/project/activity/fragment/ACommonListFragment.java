package com.ecity.cswatersupply.project.activity.fragment;

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
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.project.activity.ProjectSearchActivity;
import com.ecity.cswatersupply.project.adapter.ProjectListAdapter;
import com.ecity.cswatersupply.project.model.Project;
import com.ecity.cswatersupply.project.service.ProjectService;
import com.ecity.cswatersupply.project.util.ProjectAdapter;
import com.ecity.cswatersupply.project.view.ABaseProjectPropertyView;
import com.ecity.cswatersupply.ui.activities.MainActivity;
import com.ecity.cswatersupply.ui.fragment.ABaseListViewFragment;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ACommonListFragment extends ABaseListViewFragment<Project> {
    private int position;
    protected int pageNo = 1;
    protected static final int pageSize = 10;

    private LinearLayout ll_searchview;
    private CustomTitleView customTitleView;

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
    public void onDestroyView() {
        EventBusUtil.unregister(this);
        super.onDestroyView();
    }

    /**
     * 请求列表信息
     *
     * @return
     */
    protected abstract String requestAllInfoUrl();

    protected abstract Class<? extends ABaseProjectPropertyView> getProjectPropertyView();

    /**
     * 获取参数
     *
     * @param map
     * @param position
     */
    protected abstract void fillParamters(Map<String, String> map, int position);

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
        Map<String, String> map = new HashMap<String, String>();
        fillParamters(map, position);
        ProjectService.getInstance().getCommonProList(position, requestAllInfoUrl(), map);
    }

    protected void requestFromBegin(int position) {
        pageNo = 1;
        getDataSource().clear();
        getAdapter().notifyDataSetChanged();
        Map<String, String> map = new HashMap<String, String>();
        fillParamters(map, position);
        ProjectService.getInstance().getCommonProList(position, requestAllInfoUrl(), map);
    }

    private void setOnClickListener() {
        ll_searchview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProjectSearchActivity.class);
                getActivity().startActivityFromFragment(ACommonListFragment.this, intent, ProjectSearchActivity.REQUEST_EDIT_FITLER);
            }
        });
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.PROJECT_GET_PROSPECTIVE_LIST_NEW:
                if (position == 0) {
                    handleGetProspectiveList(event);
                }
                break;
            case ResponseEventStatus.PROJECT_GET_PROSPECTIVE_LIST_OLD:
                if (position == 1) {
                    handleGetProspectiveList(event);
                }

                break;
            case ResponseEventStatus.PROJECT_GET_PROSPECTIVE_VIEW:
                if (position == 0) {
                    ToastUtil.show("接收成功", 0);
                    gotoKcsjDetailActivity();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected ArrayListAdapter<Project> prepareListViewAdapter() {
        ABaseProjectPropertyView propertyView = null;
        try {
            propertyView = getProjectPropertyView().newInstance();
        } catch (Exception e) {
            LogUtil.e(this, e);
            throw new RuntimeException(e); // 出现这个异常必然是程序问题，直接退出
        }

        return new ProjectListAdapter(getActivity(), propertyView);
    }

    protected abstract void gotoKcsjDetailActivity();

    @Override
    protected List<Project> prepareDataSource() {
        return getDataSource();
    }

    protected abstract OnItemClickListener prepareOnItemClickListener();

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
