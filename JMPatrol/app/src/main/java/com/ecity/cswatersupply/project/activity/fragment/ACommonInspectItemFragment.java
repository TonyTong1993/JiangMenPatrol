package com.ecity.cswatersupply.project.activity.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.checkitem.InspectItemAdapter;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;
import com.ecity.cswatersupply.project.service.ProjectService;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 若子类里有提交的操作。需要实现submitXXX相关的方法。
 *
 * @author jonathanma
 */
public abstract class ACommonInspectItemFragment extends Fragment {
    protected String projectId;
    protected String recordId;

    public LinearLayout layoutContainer;
    private Button btnSubmit;
    public List<InspectItem> items;
    public CustomViewInflater customViewInflater;
    private boolean isInQuery;

    public Map<EInspectItemType, Integer> typeViewMap = new HashMap<EInspectItemType, Integer>();//获取view的index

    /**
     * 子类
     *
     * @return
     */
    protected abstract Class<? extends AProjectCommonInspectItemFragment> getFragmentClass();

    /**
     * 查询信息的请求地址
     *
     * @return
     */
    protected abstract String getQueryInfoUrl();

    /**
     * 查询信息的请求id
     *
     * @return
     */
    protected abstract int getQueryInfoRequestId();

    /**
     * 查询信息的提示信息。如“正在查询”。
     *
     * @return
     */
    protected abstract String getQueryInfoMessage();

    /**
     * 查询信息的请求参数
     *
     * @return
     */
    protected abstract void fillQueryParameters(Map<String, String> map);

    /**
     * 提交信息的请求地址
     *
     * @return
     */
    protected String getSubmitInfoUrl() {
        throw new RuntimeException("Not implemented.");
    }

    /**
     * 提交信息的请求基本参数。指的是检查项意外的参数，如userid，username。
     *
     * @return
     */
    protected AReportInspectItemParameter getSubmitInfoParameter() {
        throw new RuntimeException("Not implemented.");
    }

    /**
     * 提交信息的请求id
     *
     * @return
     */
    protected int getSubmitInfoRequestId() {
        return -1;
    }

    /**
     * 提交信息时的提示信息。如“正在提交”。
     *
     * @return
     */
    protected String getSubmitInfoMessage() {
        return getString(R.string.str_submiting);
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBusUtil.register(this);

        View convertView = inflater.inflate(R.layout.fragment_project_info, null);
        btnSubmit = (Button) convertView.findViewById(R.id.btn_submit);
        setOnClickListener();
        layoutContainer = (LinearLayout) convertView.findViewById(R.id.ll_container);

        return convertView;
    }

    @Override
    public void onDestroyView() {
        EventBusUtil.unregister(this);
        super.onDestroyView();
    }

    /**
     * 发送请求，查询数据源。
     */
    protected void queryInfo() {
        if (recordId.isEmpty() && getFragmentClass() != ProjectInfoFragment.class && getFragmentClass() != ProspectiveInfoFragment.class) {
            ToastUtil.show(getString(R.string.project_finish_apply_for_pc), 0);
            return;
        }
        LoadingDialogUtil.show(getActivity(), getQueryInfoMessage());
        Map<String, String> map = new HashMap<String, String>();
        fillQueryParameters(map);
        isInQuery = true;
        ProjectService.getInstance().sendGetRequest(getQueryInfoUrl(), getQueryInfoRequestId(), map);
    }

    private void showInspectItems(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        JSONObject json = event.getData();
        items = InspectItemAdapter.adaptItems(json);
        if (ListUtil.isEmpty(items)) {
//            ToastUtil.showShort(getString(R.string.project_no_detail_info))
            return;
        }

        boolean hasEditableItem = false;
        customViewInflater = new CustomViewInflater(getActivity());
        for (int i = 0; i < items.size(); i++) {
                layoutContainer.addView(customViewInflater.inflate(items.get(i)));
                hasEditableItem = (hasEditableItem || items.get(i).isEdit());
                typeViewMap.put(items.get(i).getType(), i);
        }

        //        for (InspectItem item : items) {
        //            View view = customViewInflater.inflate(item);
        //            typeViewMap.put(item.getType(), view);
        //            layoutContainer.addView(customViewInflater.inflate(item));
        //            hasEditableItem = (hasEditableItem || item.isEdit());
        //        }

        for (InspectItem i : items) {
            if (i.getAlias().equals("设计委托接收")) {
                btnSubmit.setText("退回");
            }
        }

        if (hasEditableItem) {
            btnSubmit.setVisibility(View.VISIBLE);
        }
    }

    private void handleSelectPeopleIsDisplay(UIEvent event) {
        boolean idDisplay = event.getData();
        if (idDisplay) {
            int index1 = typeViewMap.get(EInspectItemType.CONTACTMEN_MULTIPLE_PROJECT);
            if (index1 >= 0) {
                for (InspectItem j : items) {
                    if (j.getType().equals(EInspectItemType.CONTACTMEN_MULTIPLE_PROJECT)) {
                        layoutContainer.addView(customViewInflater.inflate(j), index1);
                    }
                }
            }
        } else {
            int index2 = typeViewMap.get(EInspectItemType.CONTACTMEN_MULTIPLE_PROJECT);
            for (InspectItem i : items) {
                if (i.getType().equals(EInspectItemType.CONTACTMEN_SINGLE_PROJECT) || i.getType().equals(EInspectItemType.CONTACTMEN_MULTIPLE_PROJECT)) {
                    if (index2 >= 0) {
                        layoutContainer.removeViewAt(index2);
                    }
                }
            }
        }
    }

    private void setOnClickListener() {
        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LoadingDialogUtil.show(getActivity(), getSubmitInfoMessage());
                ProjectService.getInstance().postCommonCheck(getSubmitInfoUrl(), getSubmitInfoRequestId(), getSubmitInfoParameter());
            }
        });
    }

    public void onEventMainThread(UIEvent event) {
        switch (event.getId()) {
            case UIEventStatus.PROJECT_SELECT_PEOPLE_DISPLAY:
                handleSelectPeopleIsDisplay(event);
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        if ((event.getId() == getQueryInfoRequestId()) && isInQuery) {
            isInQuery = false;
            if (event.isOK()) {
                showInspectItems(event);
            } else {
                ToastUtil.showLong(event.getMessage());
            }
        } else if (event.getId() == getSubmitInfoRequestId()) {
            if (event.isOK()) {
                ToastUtil.showShort(getString(R.string.str_submit_success));
                getActivity().finish();
            } else {
                ToastUtil.showLong(event.getMessage());

            }
        }
    }

    public CustomViewInflater getCustomViewInflater() {
        return customViewInflater;
    }
}