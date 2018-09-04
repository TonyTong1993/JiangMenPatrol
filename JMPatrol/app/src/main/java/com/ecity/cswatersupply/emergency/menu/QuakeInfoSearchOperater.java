
package com.ecity.cswatersupply.emergency.menu;

import java.util.List;

import android.os.Bundle;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.model.SearchType;
import com.ecity.cswatersupply.emergency.network.request.GetEarthQuakeParameter;
import com.ecity.cswatersupply.emergency.network.request.GetEarthQuakeQuickReportInfosParameter;
import com.ecity.cswatersupply.emergency.network.request.GetImportEarthQuakeParameter;
import com.ecity.cswatersupply.emergency.network.request.GetSearchFormParameter;
import com.ecity.cswatersupply.emergency.service.EmergencyService;
import com.ecity.cswatersupply.emergency.test.QuakeInfoSearchInspectItm;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.menu.ACommonReportOperator1;
import com.ecity.cswatersupply.model.ResultCode;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;

/**
 * 获取地震查询条件通用类
 * @author Gxx
 */
public class QuakeInfoSearchOperater extends ACommonReportOperator1 {

    private List<InspectItem> mInspectItems;
    //上报人
    private List<String> reporters;
    //地震编号
    private int eqid;

    protected GetSearchFormParameter getRequestParameter() {
        return new GetSearchFormParameter(getSearchType());
    }

    protected void requestDataSource() {
        EmergencyService.getInstance().getSearchParams(getSearchType());
    }

    //SearchType.EARTH_QUAKE为地震信息查询；SearchType.QUICK_REPORT为速报信息查询
    protected int getSearchType() {
        return getActivity().getIntent().getExtras().getInt(Constants.EARTH_QUAKE_INFO_SEARCH_TYPE);
    }

    @Override
    public List<InspectItem> getDataSource() {
        EventBusUtil.register(this);
        handleIntent();
        LoadingDialogUtil.show(getActivity(), R.string.event_report_get_params);
        requestDataSource();
        return null;
    }

    private void handleIntent() {
        Bundle bundle = getActivity().getIntent().getExtras();
        reporters = (List<String>) bundle.getSerializable("reporters");
        eqid = bundle.getInt("eqid");
    }

    //SearchType.EARTH_QUAKE为地震信息查询；SearchType.QUICK_REPORT为速报信息查询
    public void submit2Server(List<InspectItem> mInspectItems) {
        if(SearchType.EARTH_QUAKE.getValue() == getSearchType()) {
            GetEarthQuakeParameter parameter = new GetEarthQuakeParameter(mInspectItems);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.EARTH_QUAKE_INFO_PARAM, parameter);
            getActivity().setResult(ResultCode.EARTH_QUAKE_INFO_OK, getActivity().getIntent().putExtras(bundle));
        } else if(SearchType.QUICK_REPORT.getValue() == getSearchType()) {
            GetEarthQuakeQuickReportInfosParameter parameter = new GetEarthQuakeQuickReportInfosParameter(mInspectItems,null);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.EARTH_QUAKE_QUICK_REPORT_INFO_PARAM, parameter);
            getActivity().setResult(ResultCode.EARTH_QUAKE_QUICK_REPORT_INFO_OK, getActivity().getIntent().putExtras(bundle));
        } else if(SearchType.IMPORT_EARTH_QUAKE.getValue() == getSearchType()) {
            GetImportEarthQuakeParameter parameter = new GetImportEarthQuakeParameter(mInspectItems);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.IMPORT_EARTH_QUAKE_INFO_PARAM, parameter);
            getActivity().setResult(ResultCode.IMPORT_EARTH_QUAKE_INFO_OK, getActivity().getIntent().putExtras(bundle));
        }

        getActivity().finish();
    }

    @Override
    public void notifyBackEvent(final CustomReportActivity1 activity) {
        LoadingDialogUtil.dismiss();
        EventBusUtil.unregister(this);
        activity.setResult(ResultCode.EARTH_QUAKE_INFO_SEARCH_CANCEL);
        activity.finish();
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.EMERGENCY_EARTHQUAKE_INFO_SEARCH_PARAMS:
                handleGetSearchParams(event);
                break;
            default:
                break;
        }
    }

    private void handleGetSearchParams(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        if(SearchType.EARTH_QUAKE.getValue()== getSearchType()) {
            mInspectItems = QuakeInfoSearchInspectItm.getQuakeInfoSearchInspectItems();
        } else if(SearchType.QUICK_REPORT.getValue()== getSearchType()) {
            mInspectItems = QuakeInfoSearchInspectItm.getQuickReportSearchInspectItems(reporters, eqid);
        } else if(SearchType.IMPORT_EARTH_QUAKE.getValue()== getSearchType()) {
            mInspectItems = QuakeInfoSearchInspectItm.getImportQuakeInfoSearchInspectItems();
        }
        getActivity().fillDatas(mInspectItems);
    }
}
