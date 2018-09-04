package com.ecity.cswatersupply.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.checkitem.InspectItemAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.project.service.ProjectService;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;
import com.zzz.ecity.android.applibrary.view.ActionSheet;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonHisRecordListActivity extends BaseActivity {
    public static final String INTENT_KEY_PROJECT_ID = "INTENT_KEY_PROJECT_ID";
    public static final String INTENT_KEY_PROJECT_URL = "INTENT_KEY_PROJECT_URL";
    public static final String INTENT_KEY_PROJECT_CLASS_NAME = "INTENT_KEY_PROJECT_CLASS_NAME";
    public static final String INTENT_KEY_PROJECT_LOG_TYPE = "INTENT_KEY_PROJECT_LOG_TYPE";
    private String projectId;
    private List<InspectItem> items;
    private CustomViewInflater customViewInflater;
    private LinearLayout layoutContainer;
    private Button btMore;
    private String[] arrays;
    private List<String> gidList;
    private String className;
    private String logType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_project_info);

        layoutContainer = (LinearLayout) findViewById(R.id.ll_container);
        btMore = (Button) findViewById(R.id.btn_more);
        btMore.setText("显示历史详情");
        btMore.setVisibility(View.VISIBLE);
        projectId = getIntent().getStringExtra(INTENT_KEY_PROJECT_ID);
        String url = getIntent().getStringExtra(INTENT_KEY_PROJECT_URL);
        className = getIntent().getStringExtra(INTENT_KEY_PROJECT_CLASS_NAME);
        logType = getIntent().getStringExtra(INTENT_KEY_PROJECT_LOG_TYPE);
        EventBusUtil.register(this);

        Map<String, String> map = new HashMap<String, String>();
        map.put("proid", projectId);
        ProjectService.getInstance().sendGetRequest(url, ResponseEventStatus.PROJECT_GET_COMMON_HISTORY, map);
        initDate();
        setOnClickListener();
    }

    private void initDate() {
        gidList = new ArrayList<String>();
    }

    private void setOnClickListener() {
        btMore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ActionSheet.show(CommonHisRecordListActivity.this, "请选择", arrays, new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String gid = gidList.get(position);
                        Intent intent = new Intent(CommonHisRecordListActivity.this, ProjectDetailFragmentActivity.class);
                        intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_PROJECT_ID, projectId);
                        intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_RECORD_ID, gid);
                        intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_FRAGMENT_CLASS_NAME, className);
                        intent.putExtra(ProjectDetailFragmentActivity.INTENT_KEY_LOG_TYPE, logType);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    private void showInspectItems(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        JSONObject json = event.getData();
        items = InspectItemAdapter.adaptItems(json);

        for (InspectItem i : items) {
            if (i.getName().equals("gid")) {
                gidList.add(i.getValue());
            }
        }

        List<String> disList = new ArrayList<String>();
        for (int i = 0; i < gidList.size(); i++) {

            String str = "第" + (i + 1) + "次";
            disList.add(str);
        }
        arrays = disList.toArray(new String[disList.size()]);

        if (ListUtil.isEmpty(items)) {
            ToastUtil.showShort(getString(R.string.project_no_detail_info));
            return;
        }

        boolean hasEditableItem = false;
        customViewInflater = new CustomViewInflater(this);
        for (int i = 0; i < items.size(); i++) {
            layoutContainer.addView(customViewInflater.inflate(items.get(i)));
            hasEditableItem = (hasEditableItem || items.get(i).isEdit());
        }

        //        for (InspectItem item : items) {
        //            View view = customViewInflater.inflate(item);
        //            typeViewMap.put(item.getType(), view);
        //            layoutContainer.addView(customViewInflater.inflate(item));
        //            hasEditableItem = (hasEditableItem || item.isEdit());
        //        }

    }

    public void onEventMainThread(ResponseEvent event) {
        if (event.getId() == ResponseEventStatus.PROJECT_GET_COMMON_HISTORY) {
            LoadingDialogUtil.dismiss();
            if (event.isOK()) {
                showInspectItems(event);
            } else {
                ToastUtil.showLong(event.getMessage());
            }
        }
    }
}
