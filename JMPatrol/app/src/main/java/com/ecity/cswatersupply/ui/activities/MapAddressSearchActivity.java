package com.ecity.cswatersupply.ui.activities;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.adapter.AddressExpandableListAdapter;
import com.ecity.cswatersupply.adapter.AddressTypeGridAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.AddressInfoModel;
import com.ecity.cswatersupply.model.map.SearchResult;
import com.ecity.cswatersupply.network.GeoLocatorListener;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.network.request.GetPlanAndPipeAddressParameter;
import com.ecity.cswatersupply.service.AddressSearchService;
import com.ecity.cswatersupply.ui.fragment.MapMainTabFragment;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.ui.widght.HistoryRecordPopupWindow;
import com.ecity.cswatersupply.ui.widght.HistoryRecordPopupWindow.OnHistoryListItemClickListener;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

public class MapAddressSearchActivity extends BaseActivity {
    private final static String BAIDU_ADDRESS = ResourceUtil.getStringById(R.string.map_address);
    private final static String PLAN_ADDRESS = ResourceUtil.getStringById(R.string.map_plan_address);
    private final static String PIPE_ADDRESS = ResourceUtil.getStringById(R.string.map_pipe_address);

    private CustomTitleView customTitleView;
    private EditText searchEtx;
    private GridView gridView;
    private ExpandableListView expandableListView;
    private List<String> addressType;
    private String searchTxt = "";
    private String historySearchTxt = "";
    private List<String> mHistoryKeywords;
    private AddressTypeGridAdapter typeAdapter;
    private AddressExpandableListAdapter expandableListAdapter;
    private List<AddressInfoModel> baiduAddress;
    private List<AddressInfoModel> planAddress;
    private List<AddressInfoModel> pipeAddress;
    private List<String> groupList;
    private Map<String, List<AddressInfoModel>> addresses;
    private ExecutorService executorService;
    private GeoSearchHandler geoHandler;
    private GeoLocatorListener geoLocatorListener;
    private HistoryRecordPopupWindow popupWindow;
    private SharedPreferences historypreference;
    private boolean isGroupClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_search);
        EventBusUtil.register(this);
        initUI();
        initData();
        bindEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHistoryKeywords = str2List(historySearchTxt);
        if (null != SessionManager.lastSearchText) {
            loadLastSearchResult();
        } else {
            if(mHistoryKeywords.size() > 0 ) {
                new Handler().postDelayed(new ShowPopRunnable(mHistoryKeywords), 1000);
            }
        }
    }

    private void initUI() {
        customTitleView = (CustomTitleView) this.findViewById(R.id.customTitleView);
        customTitleView.setBtnStyle(BtnStyle.ONLY_BACK);
        customTitleView.setTitleText(R.string.map_address_search);

        searchEtx = (EditText) this.findViewById(R.id.search_text);
        gridView = (GridView) this.findViewById(R.id.address_types);
        expandableListView = (ExpandableListView) this.findViewById(R.id.expandableListView);
    }

    private void initData() {
        addressType = new ArrayList<String>();
        baiduAddress = new ArrayList<AddressInfoModel>();
        planAddress = new ArrayList<AddressInfoModel>();
        pipeAddress = new ArrayList<AddressInfoModel>();
        groupList = new ArrayList<String>();
        addresses = new HashMap<String, List<AddressInfoModel>>();
        executorService = Executors.newScheduledThreadPool(1);

        addressType.add(BAIDU_ADDRESS);
        addressType.add(PLAN_ADDRESS);
        addressType.add(PIPE_ADDRESS);
        //默认选择所有地址类型
        groupList.addAll(addressType);

        typeAdapter = new AddressTypeGridAdapter(this, addressType);
        gridView.setAdapter(typeAdapter);

        addresses.put(BAIDU_ADDRESS, baiduAddress);
        addresses.put(PLAN_ADDRESS, planAddress);
        addresses.put(PIPE_ADDRESS, pipeAddress);
        expandableListAdapter = new AddressExpandableListAdapter(this, groupList, addresses);
        expandableListView.setAdapter(expandableListAdapter);
        geoHandler = new GeoSearchHandler(this);
        geoLocatorListener = new GeoLocatorListener(this, geoHandler);
        historypreference = HostApplication.getApplication().getSharedPreferences("SP", Context.MODE_PRIVATE);
        historySearchTxt = historypreference.getString(Constants.KEY_SEARCH_HISTORY_KEYWORD, "");
        mHistoryKeywords = new ArrayList<String>();
        popupWindow = new HistoryRecordPopupWindow(this);

        int groupCount = expandableListView.getCount();
        for (int i = 0; i< groupCount; i++) {
            expandableListView.expandGroup(i);
        }
    }

    private void bindEvent() {
        searchEtx.addTextChangedListener(new SearchTxtListener());
        searchEtx.setOnEditorActionListener(new CustomEditorActionListener());
        gridView.setOnItemClickListener(new OnGridViewItemClickListener());

        expandableListView.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                List<AddressInfoModel> tempList = addresses.get(groupList.get(groupPosition).toString());
                if (ListUtil.isEmpty(tempList) && !isGroupClick) {
                    Toast.makeText(getApplicationContext(), R.string.map_no_match_address, Toast.LENGTH_SHORT).show();
                }
                isGroupClick = !isGroupClick;
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                AddressInfoModel addressInfoModel = addresses.get(groupList.get(groupPosition).toString()).get(childPosition);
                saveSearchRecords(Constants.KEY_SEARCH_HISTORY_KEYWORD,addressInfoModel.getName());
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(MapMainTabFragment.SEARCH_RESULTS, addressInfoModel);
                intent.putExtras(bundle);
                setResult(MapMainTabFragment.RESULT_CODE, intent);
                finish();
                return true;
            }
        });
    }

    private void loadLastSearchResult() {
        searchEtx.setText(SessionManager.lastSearchText);

        if( null == SessionManager.lastSearchResult ) {
            return;
        }

        List<AddressInfoModel> temp = SessionManager.lastSearchResult.get(BAIDU_ADDRESS);

        if(null == temp) {
            baiduAddress.addAll(new ArrayList<AddressInfoModel>());
        } else {
            baiduAddress.addAll(SessionManager.lastSearchResult.get(BAIDU_ADDRESS));
        }

        if( null == planAddress){
            planAddress = new ArrayList<AddressInfoModel>();
        }

        if( null == pipeAddress){
            pipeAddress = new ArrayList<AddressInfoModel>();
        }

        if( null != SessionManager.lastSearchResult.get(PLAN_ADDRESS)) {
            planAddress.addAll(SessionManager.lastSearchResult.get(PLAN_ADDRESS));
        }

        if( null != SessionManager.lastSearchResult.get(PIPE_ADDRESS)) {
            pipeAddress.addAll(SessionManager.lastSearchResult.get(PIPE_ADDRESS));
        }

        updateExpListData(BAIDU_ADDRESS);
        updateExpListData(PLAN_ADDRESS);
        updateExpListData(PIPE_ADDRESS);
    }

    private void initPopWindow(List<String> historyKeywords) {
        popupWindow = new HistoryRecordPopupWindow(this);
        popupWindow.initPopup(historyKeywords);
        popupWindow.setOnHistoryListItemClickListener(new OnHistoryListItemClickListener() {
            @Override
            public void onClick(String itemStr) {
                searchEtx.setText(itemStr);
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 点击软键盘的搜索按钮
     */
    public void searchButtonProcess() {
        if(null != popupWindow) {
            popupWindow.dismiss();
        }
        String text = searchEtx.getText().toString();
        saveSearchRecords(Constants.KEY_SEARCH_HISTORY_KEYWORD,text);
        executorService.execute(new RequestAddressRunnable());
    }

    public void saveSearchRecords(String key, String text) {
        String oldText = historypreference.getString(key, "");
        SessionManager.lastSearchText = text;
        if (!TextUtils.isEmpty(text) && !oldText.contains(text)) {
            historypreference.edit().putString(key, text + "," + oldText).commit();
        }
    }

    private void updateExpListData(String str) {
        if (str.equals(BAIDU_ADDRESS)) {
            buildExpListData(str, baiduAddress);
        } else if (str.equals(PLAN_ADDRESS)) {
            buildExpListData(str, planAddress);
        } else if (str.equals(PIPE_ADDRESS)) {
            buildExpListData(str, pipeAddress);
        }
    }

    private void buildExpListData(String type, List<AddressInfoModel> infoModels) {
        groupList.add(type);
        Collections.sort(groupList);
        removeDuplicate(groupList);
        addresses.put(type, infoModels);
        expandableListAdapter.setAddresses(groupList, addresses);
        expandableListAdapter.notifyDataSetChanged();
    }

    private void requestBaiduAddress() {
        String city = getResources().getText(R.string.city_center).toString();
        //0 查找第一页的地址
        geoLocatorListener.didSearchWithKeywords(city + ";" + searchTxt, 0);
    }

    private void requestPlanAndPipeAddress() {
        //20  一次查询的条数
        IRequestParameter parameter = new GetPlanAndPipeAddressParameter(searchTxt, "20");
        AddressSearchService.getInstance().getPlanAndPipeAddress(parameter);
    }

    public void onBackButtonClicked(View v) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        addresses.clear();
        executorService.shutdown();
        if (null != popupWindow) {
            popupWindow.dismiss();
            popupWindow = null;
        }
        EventBusUtil.unregister(this);
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.MAP_GET_PLAN_AND_PIPE_ADDRESS:
                handlePlanAndPipeAddress(event);
                break;
            default:
                break;
        }
    }

    private void handlePlanAndPipeAddress(ResponseEvent event) {
        Map<String, Object> map = event.getData();
        planAddress = (List<AddressInfoModel>) map.get(PLAN_ADDRESS);
        pipeAddress = (List<AddressInfoModel>) map.get(PIPE_ADDRESS);
        removeDuplicate(planAddress);
        removeDuplicate(pipeAddress);
        SessionManager.lastSearchResult.put(PLAN_ADDRESS, planAddress);
        SessionManager.lastSearchResult.put(PIPE_ADDRESS, pipeAddress);
        if (!ListUtil.isEmpty(groupList)) {
            if (groupList.contains(PLAN_ADDRESS)) {
                updateExpListData(PLAN_ADDRESS);
            }
            if (groupList.contains(PIPE_ADDRESS)) {
                updateExpListData(PIPE_ADDRESS);
            }
        }
    }

    private <T> void removeDuplicate(List<T> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }
    }

    private class SearchTxtListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            searchTxt = s.toString();
            if ("".equals(searchTxt)) {
                if(null != popupWindow && mHistoryKeywords.size() > 0 ) {
                    if(popupWindow.isShowing()) {
                        popupWindow.updateList(mHistoryKeywords);
                    } else {
                        //延时时间不能太长，否则会多次new HistoryRecordPopupWindow
                        new Handler().postDelayed(new ShowPopRunnable(mHistoryKeywords), 50);
                    }
                } 
                return;
            }
            addressMatch(searchTxt);
            executorService.execute(new RequestAddressRunnable());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private void addressMatch(String searchTxt) {
        if("".equals(searchTxt)) {
            return;
        }
        String addStr = historypreference.getString(Constants.KEY_SEARCH_HISTORY_KEYWORD, "");
        List<String> temp = str2List(addStr);
        List<String> mHistoryKeywords = new ArrayList<String>();
        for(String str : temp) {
            if(str.contains(searchTxt)) {
                mHistoryKeywords.add(str);
            }
        }
        if(ListUtil.isEmpty(mHistoryKeywords) || (1 == mHistoryKeywords.size())) {
            if(null != popupWindow) {
                popupWindow.dismiss();
            }
        } else {
            if(null != popupWindow && popupWindow.isShowing()) {
                popupWindow.updateList(mHistoryKeywords);
            } else {
                popupWindow.dismiss();
                //延时时间不能太长，否则会多次new HistoryRecordPopupWindow
                new Handler().postDelayed(new ShowPopRunnable(mHistoryKeywords), 50);
            }
        }
    }

    private List<String> str2List(String string) {
        List<String> list = new ArrayList<String>();
        if(TextUtils.isEmpty(string)) {
            return list;
        }
        for (String str : string.split(",")) {
            list.add(str);
        }
        if (list.size() > 10) {
            List<String> arrtemp = new ArrayList<String>();
            for (int i = 0; i < 10; i++) {
                arrtemp.add(list.get(i));
            }
            list = arrtemp;
        }
        return list;
    }

    private class ShowPopRunnable implements Runnable {
        private List<String> list;

        public ShowPopRunnable(List<String> list) {
            this.list = list;
        }

        public void run() {
            initPopWindow(list);
        }
    }

    private class RequestAddressRunnable implements Runnable {
        
        public void run() {
            requestBaiduAddress();
            requestPlanAndPipeAddress();
        }
    }

    private class CustomEditorActionListener implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                //隐藏软键盘 
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    LoadingDialogUtil.show(MapAddressSearchActivity.this, R.string.map_query_address);
                }
                searchButtonProcess();
            }
            return false;
        }
    }

    private class OnGridViewItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView textView = (TextView) view.findViewById(R.id.address_type);
            String currentType = addressType.get(position);
            if (groupList.contains(currentType)) {
                groupList.remove(currentType);
                addresses.remove(currentType);
                expandableListAdapter.setAddresses(groupList, addresses);
                expandableListAdapter.notifyDataSetChanged();
                textView.setBackgroundColor(ResourceUtil.getColorById(R.color.white));
            } else {
                updateExpListData(currentType);
                textView.setBackgroundColor(ResourceUtil.getColorById(R.color.ics_blue));
            }
        }
    }

    private class GeoSearchHandler extends Handler {
        private WeakReference<MapAddressSearchActivity> outer;

        public GeoSearchHandler(MapAddressSearchActivity mapAddressSearchActivity) {
            outer = new WeakReference<MapAddressSearchActivity>(mapAddressSearchActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            MapAddressSearchActivity activity = outer.get();
            if (null == activity) {
                return;
            }
            switch (msg.what) {
                case 0:
                    LoadingDialogUtil.dismiss();
                    List<SearchResult> tmpResults = GeoLocatorListener.getLastSearchResults();
                    activity.baiduAddress.clear();
                    convertToAddressInfoList(activity, tmpResults);
                    removeDuplicate(tmpResults);
                    SessionManager.lastSearchResult.put(BAIDU_ADDRESS, activity.baiduAddress);
                    if (!ListUtil.isEmpty(groupList) && groupList.contains(BAIDU_ADDRESS)) {
                        updateExpListData(BAIDU_ADDRESS);
                    }
                    break;
                case 1:
                    LoadingDialogUtil.dismiss();
                    activity.baiduAddress.clear();
                    SessionManager.lastSearchResult.put(BAIDU_ADDRESS, activity.baiduAddress);
                    if (!ListUtil.isEmpty(groupList) && groupList.contains(BAIDU_ADDRESS)) {
                        updateExpListData(BAIDU_ADDRESS);
                    }
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }

        private void convertToAddressInfoList(MapAddressSearchActivity activity, List<SearchResult> tmpResults) {
            AddressInfoModel addressInfoModel;
            for (SearchResult searchResult : tmpResults) {
                addressInfoModel = new AddressInfoModel();
                addressInfoModel.setType(BAIDU_ADDRESS);
                addressInfoModel.setName(searchResult.titleName);
                addressInfoModel.setAddress(searchResult.address);
                addressInfoModel.setPoint(searchResult.centerPoint);
                activity.baiduAddress.add(addressInfoModel);
            }
        }
    }

}
