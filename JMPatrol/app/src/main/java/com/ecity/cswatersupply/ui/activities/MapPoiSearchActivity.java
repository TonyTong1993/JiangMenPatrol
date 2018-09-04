package com.ecity.cswatersupply.ui.activities;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.map.HistorySearchAdapter;
import com.ecity.cswatersupply.adapter.map.PoiSearchAdapter;
import com.ecity.cswatersupply.menu.map.GeoResultDisplayOperator;
import com.ecity.cswatersupply.model.map.SearchResult;
import com.ecity.cswatersupply.network.GeoLocatorListener;
import com.ecity.cswatersupply.ui.fragment.MapMainTabFragment;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.lee.pullrefresh.ui.PullToRefreshListView;

public class MapPoiSearchActivity extends FragmentActivity {
    private static final String PRE_SEARCH_HISTORY = "pre_search_history";
    public static final String KEY_SEARCH_HISTORY_KEYWORD = "key_search_history_keyword";
    private PoiSearchAdapter adapter;
    private List<SearchResult> results;
    private CustomTitleView place_name_query;
    //输入关键字窗口
    private AutoCompleteTextView keyWorldsView = null;
    private ArrayAdapter<String> sugAdapter = null;
    private int load_Index = 0;
    private GeoLocatorListener geoLocatorListener = null;
    private GeoSearchHandler geoHandler = null;
    private Button clearHistoryBtn;
    private SharedPreferences historyListpreference;
    private List<String> mHistoryKeywords;
    private HistorySearchAdapter hAdapter;
    private String history;
    private PullToRefreshListView plRefreshListView;
    private ListView listView;
    private ListView hlistView;
    private boolean isPOIQuerying;
    private int listSize;
    private Map<String, String> poiNamesMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poisearch);

        initUI();
        initData();
    }

    private void initUI() {
        // 初始化搜索模块，注册搜索事件监听
        place_name_query = (CustomTitleView) findViewById(R.id.place_name_query);
        place_name_query.setTitleText(R.string.place_query);
        keyWorldsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
        sugAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        keyWorldsView.setAdapter(sugAdapter);
        //当输入关键字变化时，动态更新建议列表
        keyWorldsView.addTextChangedListener(new MyTextWatcher());

        clearHistoryBtn = (Button) findViewById(R.id.clear_history_btn);
        clearHistoryBtn.setOnClickListener(new ClearHistoryListener());
        historyListpreference = HostApplication.getApplication().getSharedPreferences(PRE_SEARCH_HISTORY, Context.MODE_PRIVATE);
        mHistoryKeywords = new ArrayList<String>();
        history = historyListpreference.getString(KEY_SEARCH_HISTORY_KEYWORD, "");

        plRefreshListView = (PullToRefreshListView) findViewById(R.id.listView1);
        plRefreshListView.setVerticalScrollBarEnabled(true);
        listView = plRefreshListView.getRefreshableView();
        listView.setVerticalScrollBarEnabled(true);
        listView.setDivider(ResourceUtil.getDrawableResourceById(R.drawable.shape_list_divider));
        listView.setDividerHeight(ResourceUtil.getDimensionPixelSizeById(R.dimen.margin_spacing_level_10));
        plRefreshListView.setPullLoadEnabled(isPullLoadEnabled());
        plRefreshListView.setPullRefreshEnabled(isPullRefreshEnabled());
        plRefreshListView.setScrollLoadEnabled(true);

        hlistView = (ListView) this.findViewById(R.id.listView_history);
        hlistView.setDivider(ResourceUtil.getDrawableResourceById(R.drawable.shape_list_divider));
        hlistView.setDividerHeight(ResourceUtil.getDimensionPixelSizeById(R.dimen.margin_spacing_level_10));

    }

    private void initData() {
        adapter = new PoiSearchAdapter(this, results);
        listView.setAdapter(adapter);

        poiNamesMap = new HashMap<String, String>();
        if (!history.isEmpty()) {
            historySearchRecordsProcess();
            clearHistoryBtn.setVisibility(View.VISIBLE);
        } else {
            searchResultProcess();
            clearHistoryBtn.setVisibility(View.GONE);
        }
        geoHandler = new GeoSearchHandler(this);
        geoLocatorListener = new GeoLocatorListener(this, geoHandler);
        keyWorldsView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //隐藏软键盘 
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                        LoadingDialogUtil.show(MapPoiSearchActivity.this, R.string.map_query_address);
                    }
                    searchButtonProcess(v);
                }

                return false;
            }
        });

        plRefreshListView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt) {
                //当屏幕停止滚动时为0；当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1；
                //由于用户的操作，屏幕产生惯性滑动时为2
                if (!isPOIQuerying) {
                    switch (paramInt) {
                    // 当不滚动时
                        case OnScrollListener.SCROLL_STATE_IDLE:
                            // 判断滚动到底部
                            if (paramAbsListView.getLastVisiblePosition() == (paramAbsListView.getCount() - 1)) {
                                //数据全部显示出来时运行此处代码，如果要实现分页功能，在这里加载下一页的数据  
                                LoadingDialogUtil.show(MapPoiSearchActivity.this, R.string.map_query_address);
                                isPOIQuerying = true;
                                load_Index++;
                                saveSearchRecords();
                                searchResultProcess();
                                clearHistoryBtn.setVisibility(View.GONE);
                                EditText editSearchKey = (EditText) findViewById(R.id.searchkey);
                                new Thread(new POISearchThread(editSearchKey.getText().toString())).start();
                            }
                            break;
                    }
                }
            }

            @Override
            public void onScroll(AbsListView paramAbsListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    public List<SearchResult> getResults() {
        return results;
    }

    protected boolean isPullLoadEnabled() {
        return false;
    }

    protected boolean isPullRefreshEnabled() {
        return false;
    }

    private void searchResultProcess() {
        plRefreshListView.setVisibility(View.VISIBLE);
        hlistView.setVisibility(View.GONE);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView) view.findViewById(R.id.mpoiNameT);
                String selectText = text.getText().toString();
                String oldText = historyListpreference.getString(KEY_SEARCH_HISTORY_KEYWORD, "");
                if (!TextUtils.isEmpty(selectText) && !oldText.contains(selectText)) {
                    historyListpreference.edit().putString(KEY_SEARCH_HISTORY_KEYWORD, selectText + "," + oldText).commit();
                    mHistoryKeywords.add(0, selectText);
                }
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(MapMainTabFragment.MAP_OPERATOR, GeoResultDisplayOperator.class.getName());
                bundle.putSerializable(MapMainTabFragment.SEARCH_RESULTS, (Serializable) listView.getItemAtPosition(position));
                intent.putExtras(bundle);
                MapPoiSearchActivity.this.setResult(MapMainTabFragment.RESULT_CODE, intent);
                MapPoiSearchActivity.this.finish();
            }
        });
    }

    public void historySearchRecordsProcess() {
        if (!TextUtils.isEmpty(history)) {
            List<String> list = new ArrayList<String>();
            for (Object o : history.split(",")) {
                list.add((String) o);
            }
            mHistoryKeywords = list;
        }
        if (mHistoryKeywords.size() > 10) {
            List<String> arrtemp = new ArrayList<String>();
            for (int i = 0; i < 10; i++) {
                arrtemp.add(mHistoryKeywords.get(i));
            }
            mHistoryKeywords = arrtemp;
        }
        hAdapter = new HistorySearchAdapter(this, mHistoryKeywords);

        plRefreshListView.setVisibility(View.GONE);
        hlistView.setVisibility(View.VISIBLE);
        hlistView.setAdapter(hAdapter);
        hlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                keyWorldsView.setText(mHistoryKeywords.get(i));
                LoadingDialogUtil.show(MapPoiSearchActivity.this, R.string.map_query_address);
            }
        });
    }

    public void saveSearchRecords() {
        String text = keyWorldsView.getText().toString();
        String oldText = historyListpreference.getString(KEY_SEARCH_HISTORY_KEYWORD, "");
        if (!TextUtils.isEmpty(text) && !oldText.contains(text)) {
            historyListpreference.edit().putString(KEY_SEARCH_HISTORY_KEYWORD, text + "," + oldText).commit();
            mHistoryKeywords.add(0, text);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void afterTextChanged(Editable arg0) {
            searchResultProcess();
            clearHistoryBtn.setVisibility(View.GONE);
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            if (arg0.length() <= 0) {
                return;
            }
            load_Index = 0;
            updateDatalist(null);
            new Thread(new POISearchThread(arg0.toString())).start();
        }
    }

    private class POISearchThread implements Runnable {

        private String key;

        public POISearchThread(String key) {
            super();
            this.key = key;
        }

        @Override
        public void run() {
            poiSearch(R.string.city_center, key, load_Index);
        }
    }

    private void poiSearch(int cityId, String key, int pageNum) {
        String city = getResources().getText(cityId).toString();
        geoLocatorListener.didSearchWithKeywords(city + ";" + key, pageNum);
    }

    private class ClearHistoryListener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            cleanHistory();
        }
    }

    public void cleanHistory() {
        historyListpreference.edit().clear().commit();
        mHistoryKeywords.clear();
        hAdapter.notifyDataSetChanged();
        clearHistoryBtn.setVisibility(View.GONE);
    }

    private void updateDatalist(List<SearchResult> results) {
        this.results = results;
        listSize = null == this.results ? 0 : this.results.size();

        if (0 == listSize) {
            load_Index = 0;
            poiNamesMap.clear();
        }

        if (null != adapter) {
            LoadingDialogUtil.dismiss();
            adapter.updateDataSet(results);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 影响搜索按钮点击事件
     * @param v
     */
    public void searchButtonProcess(View v) {
        saveSearchRecords();
        searchResultProcess();
        clearHistoryBtn.setVisibility(View.GONE);
        EditText editSearchKey = (EditText) findViewById(R.id.searchkey);
        new Thread(new POISearchThread(editSearchKey.getText().toString())).start();
    }

    public void onBackButtonClicked(View v) {
        finish();
    }

    private class GeoSearchHandler extends Handler {
        private WeakReference<MapPoiSearchActivity> outer;

        public GeoSearchHandler(MapPoiSearchActivity activity) {
            outer = new WeakReference<MapPoiSearchActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MapPoiSearchActivity activity = outer.get();
            if (null == activity) {
                return;
            }
            activity.isPOIQuerying = false;
            if (msg.what == 0) {

                if (null == activity.results) {
                    activity.results = new ArrayList<SearchResult>();
                }

                List<SearchResult> tmpResults = GeoLocatorListener.getLastSearchResults();
                if (null == tmpResults) {
                    LoadingDialogUtil.dismiss();
                }
                if (null != tmpResults && tmpResults.size() > 0) {
                    int size = tmpResults.size();
                    for (int i = 0; i < size; i++) {
                        SearchResult tmpObj = tmpResults.get(i);
                        if (!activity.poiNamesMap.containsKey(tmpObj.titleName)) {
                            activity.results.add(tmpObj);
                            activity.poiNamesMap.put(tmpObj.titleName, "");
                        }
                    }
                }
                activity.updateDatalist(activity.results);
            }
            super.handleMessage(msg);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
