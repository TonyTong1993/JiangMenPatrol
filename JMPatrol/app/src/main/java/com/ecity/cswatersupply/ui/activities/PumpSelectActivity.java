package com.ecity.cswatersupply.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.checkitem.PumpSelectAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.checkitem.PumpInsSelectValue;
import com.ecity.cswatersupply.service.ReportEventService;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.inpsectitem.PumpInspectItemViewXtd;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PumpSelectActivity extends BaseActivity {
    public final static String SELECT_PUMP = "SELECT_PUMP";
    public final static int REQPUMP = 0;

    private PumpSelectAdapter adapter;
    private EditText searchTxt;
    private ListView mListView;
    private List<PumpInsSelectValue> selectValueList;
    private boolean isFromPumpInspect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtil.register(this);
        setContentView(R.layout.activity_pump_select);
        initView();
        initData();
        bindEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPumps();
    }

    private void initView() {
        TextView title = (TextView)this.findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.event_management_type_pump_select_title));

        mListView = (ListView) this.findViewById(R.id.listview);
        searchTxt = (EditText) findViewById(R.id.edit_search);
        adapter = new PumpSelectAdapter(this);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        isFromPumpInspect = bundle.getBoolean(PumpInspectItemViewXtd.INTENT_FROM_PUMP_INSPECT, false);
    }

    private void requestPumps() {
        LoadingDialogUtil.show(this, R.string.str_searching);
        //查询泵房的图层id为11
        String url = ServiceUrlManager.getInstance().getSpacialSearchUrl() + "/11/query";
        ReportEventService.getInstance().queryAllPumps(url);
    }

    private void bindEvent() {
        searchTxt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchTxt.getText().length() == 0) {
                    requestPumps();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String key = s.toString();
                List<PumpInsSelectValue> filteredPumpList = filterSelectPump(selectValueList, key);
                if(null == filteredPumpList) {
                    return;
                }
                updateListView(filteredPumpList);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(SELECT_PUMP, selectValueList.get(position));
                Intent intent = new Intent();
                intent.putExtras(bundle);
                if (isFromPumpInspect) {
                    setResult(1, intent);
                } else {
                    intent.setClass(PumpSelectActivity.this, PumpDetailActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    /**
     * 过滤方法
     */
    private List<PumpInsSelectValue> filterSelectPump(List<PumpInsSelectValue> allSelectValue, String key) {
        if (null == allSelectValue) {
            return null;
        }

        if(StringUtil.isBlank(key)) {
            return allSelectValue;
        }

        List<PumpInsSelectValue> filteredPumpList = new ArrayList<PumpInsSelectValue>();
        for (int i = 0; i < allSelectValue.size(); i++) {
            String alias =  allSelectValue.get(i).getAlias();
            if(alias.contains(key)) {
                filteredPumpList.add(allSelectValue.get(i));
            }
        }

        return filteredPumpList;
    }

    private void updateListView(List<PumpInsSelectValue> filteredPumpList) {
        selectValueList = filteredPumpList;
        adapter.setList(selectValueList);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    public void onEventMainThread(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        if (!event.isOK()) {
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.EVENT_GET_ALL_PUMP_INFO:
                handleAllPumps(event);
                break;
            default:
                break;
        }
    }

    private void handleAllPumps(ResponseEvent event) {
        selectValueList = event.getData();
        MyComparator comparator = new MyComparator();
        Collections.sort(selectValueList, comparator);

        List<PumpInsSelectValue> temp = new ArrayList<>();
        for(PumpInsSelectValue pump : selectValueList) {
            if(StringUtil.isBlank(pump.getName()) || "".equals(pump.getName())) {
                temp.add(pump);
            }
        }
        selectValueList.removeAll(temp);
        temp.clear();

        adapter.setList(selectValueList);
        mListView.setAdapter(adapter);
    }

    private class MyComparator implements Comparator {
        public int compare(Object arg0, Object arg1) {
            PumpInsSelectValue pump0 = (PumpInsSelectValue) arg0;
            PumpInsSelectValue pump1 = (PumpInsSelectValue) arg1;
            return pump0.getName().compareTo(pump1.getName());
        }
    }

}
